package com.lgcns.wcs.kurly.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.wcs.kurly.dto.*;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.WcsToDasService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @Name : WcsToDasBatch
 * @작성일 : 2022. 04. 27.
 * @작성자 : hwan.bae
 * @변경이력 : 2022. 04. 27. 최초작성
 * @Method 설명 : WCS DAS 데이터를 WCS-DAS-API로 kafka전송
 *               1. 워크배치 오더정보
 *               2. 피킹완료 토트정보
 *               3. 피킹완료 토트취소정보
 */

@Slf4j
@Component
public class WcsToDasBatch {
    @Autowired
    KurlyWcsToWmsProducer wcsProducer;

    @Autowired
    WcsToDasService wcsToDasService;

    @Autowired
    LogBatchExecService logBatchExecService;
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    public void workBatchOrderInfoTask(){
        log.info("======workBatchOrderInfoTask start======");

        long apiRunTimeStart = 0;
        long apiRunTimeEnd   = 0;
        String apiRunTime    = "";

        apiRunTimeStart = System.currentTimeMillis();
        String result = KurlyConstants.STATUS_OK;
        String resultMessage = "";
        int executeCount = 0;
        Date startDate = Calendar.getInstance().getTime();

        try{
            //한번에 모든 대상 데이터들을 조회하고 Stream을 이용해서 전송 형태를 만듬.
            List<WorkBatchOrderData> listWorkBatchOrder = wcsToDasService.selectWorkBatchOrder();

            if(listWorkBatchOrder != null && listWorkBatchOrder.size() > 0) {
                List<Map<String, Object>> updateMapList = new ArrayList<Map<String, Object>>();
                List<LogApiStatus> logApiStatusList = new ArrayList<LogApiStatus>();

                log.info("==========listWorkBatchOrder.size() : " + listWorkBatchOrder.size());

                //workBatchNo로 그룹핑
                List<WorkBatchOrderData> workBatchGroupList = listWorkBatchOrder.stream()
                        .filter(distinctByKey(p -> p.getWorkBatchNo()))
                        .collect(Collectors.toList());

                log.info(">>>>>>>" + workBatchGroupList.size());
                WorkBatchOrderSendData orderSendData = null;
                //workBatchNo로 그룹핑된 결과로 반복
                for(WorkBatchOrderData w : workBatchGroupList) {
                    log.info("workBatchNo: " + w.getWorkBatchNo());
                    //건당 시간 체크용
                    long apiRunTimeStartFor = System.currentTimeMillis();

                    //카프카 전송 데이터 준비
                    orderSendData = new WorkBatchOrderSendData();
                    orderSendData.setWarehouseKey(w.getWarehouseKey()); //센터코드
                    orderSendData.setWorkBatchNo(w.getWorkBatchNo());   //워크배치 번호

                    //특정 워크배치내에 속한 주문번호 리스트
                    List<WorkBatchOrderData> orderNoList = listWorkBatchOrder.stream()
                            .filter(o -> w.getWorkBatchNo().equals(o.getWorkBatchNo()))
                            .collect(Collectors.toList());

                    //orderNo로 그룹핑
                    List<WorkBatchOrderData> orderNoGroupList = orderNoList.stream()
                            .filter(distinctByKey(k -> k.getOrderNo()))
                            .collect(Collectors.toList());

                    List<OrderData> orderDataList = new ArrayList<>();
                    OrderData od = null;
                    for(WorkBatchOrderData g : orderNoGroupList){
                        od = new OrderData();
                        od.setCellNo(g.getCellNo());
                        od.setOrderNo(g.getOrderNo());
                        od.setOrderType(g.getOrderType());
                        od.setOrderTypeAll(g.getOrderTypeAll());
                        od.setInvoiceNo(g.getInvoiceNo());
                        od.setCustomerName(g.getCustomerName());
                        od.setRecipientName(g.getRecipientName());
                        od.setReceiverAddress(g.getReceiverAddress());
                        od.setReceiverRoadAddress(g.getReceiverRoadAddress());
                        od.setRegionGroupCode(g.getRegionGroupCode());
                        od.setRegionCode(g.getRegionCode());
                        od.setRegionGu(g.getRegionGu());
                        od.setRegionDong(g.getRegionDong());
                        od.setAllcateSeq(g.getAllcateSeq());
                        od.setDeliveryRound(g.getDeliveryRound());
                        od.setReusablePackageYn(g.getReusablePackageYn());
                        od.setSpecialMgntCustYn(g.getSpecialMgntCustYn());

                        orderDataList.add(od);

                        List<OrderItemData> orderItemDataList = new ArrayList<>();

                        //orderNo로 그룹핑된 결과로 반복
                        List<WorkBatchOrderData> orderItemList = orderNoList.stream()
                                .filter((i -> g.getOrderNo().equals(i.getOrderNo())))
                                .collect(Collectors.toList());

                        OrderItemData oid = null;
                        for(WorkBatchOrderData s : orderItemList){
                            oid = new OrderItemData();
                            oid.setSkuCode(s.getSkuCode());
                            oid.setSkuName(s.getSkuName());
                            oid.setSkuAlterCode(s.getSkuAlterCode());
                            oid.setProductCode(s.getProductCode());
                            oid.setQuantity(s.getQuantity());

                            orderItemDataList.add(oid);
                        }
                        od.setOrderItemList(orderItemDataList);
                    }
                    orderSendData.setOrderList(orderDataList);

                    String docInfoCreateYn = KurlyConstants.STATUS_N;
                    try{
                        List<OrderData> docList = orderSendData.getOrderList().stream()
                                .filter(distinctByKey(p -> p.getInvoiceNo()))
                                .collect(Collectors.toList());

                        wcsToDasService.createDocInfo(docList);
                        docInfoCreateYn = KurlyConstants.STATUS_Y;
                    }catch (Exception e) {
                        e.printStackTrace();
                        docInfoCreateYn = KurlyConstants.STATUS_N;
                    }

                    if(KurlyConstants.STATUS_Y.equals(docInfoCreateYn)) {
                        String r_ifYn = KurlyConstants.STATUS_N;
                        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

                        String retStatus = "";
                        String retMessage = "";
                        try {
                            //kafka 전송
                            deferredResult = wcsProducer.sendWorkBatchOrder(orderSendData);

                            ResponseEntity<ResponseMesssage> res = (ResponseEntity<ResponseMesssage>) deferredResult.getResult();
                            retStatus = (String) res.getBody().getStatus();
                            retMessage = (String) res.getBody().getMessage();

                            //log.info(" >>>>>>WcsToDasBatch retMessage=>" + retMessage);
                            //log.info(" >>>>>>WcsToDasBatch retMessage.length=>" + retMessage.length());
                            if ("SUCCESS".equals(retStatus)) {
                                r_ifYn = KurlyConstants.STATUS_Y;
                            } else {
                                r_ifYn = KurlyConstants.STATUS_N;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            log.error("== send error == " + orderSendData.getWorkBatchNo());
                            result = "error";
                            retMessage = ex.getMessage().substring(0, 90);
                            r_ifYn = KurlyConstants.STATUS_N;
                        } finally {
                            log.info("retStatus==" + retStatus);
                            //인터페이스 처리결과 셋팅
                            Map<String, Object> updateMap = null;

                            for (WorkBatchOrderData c : orderNoList) {
                                updateMap = new HashMap<String, Object>();
                                updateMap.put("shipUidKey", c.getShipUidKey());
                                updateMap.put("shipUidItemSeq", c.getShipUidItemSeq());
                                updateMap.put("dasIntfYn", r_ifYn);
                                updateMap.put("modifiedUser", KurlyConstants.DEFAULT_USERID);

                                updateMap.put("dasIntfMemo", retMessage);

                                updateMapList.add(updateMap);
                            }

                            Map<String, Object> logMap = new HashMap<String, Object>();

                            apiRunTimeEnd = System.currentTimeMillis();
                            apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd);
                            logMap.put("apiRunTime", apiRunTime);
                            logMap.put("dasIntfYn", r_ifYn);
                            logMap.put("dasIntfMemo", retMessage);

                            //로그 저장  수집
                            logApiStatusList.addAll(logApiStatus(logMap, orderNoList));
                        }
                        executeCount++;
                    } //end if
                }

                try
                {
                    List<Map<String, Object>> u_updateMapList = new ArrayList<Map<String, Object>>();
                    List<LogApiStatus> u_logApiStatusList = new ArrayList<LogApiStatus>();
                    //인터페이스 처리내역 update
                    for(int i = 0; i < updateMapList.size(); i++) {

                        u_updateMapList.add(updateMapList.get(i));
                        u_logApiStatusList.add(logApiStatusList.get(i));
                        //50 건 씩 처리
                        if( (i > 2 && i % 50 == 0 ) || ( i == updateMapList.size() - 1 ) ) {

                            Map<String, Object> upListMap = new HashMap<String, Object>();
                            upListMap.put("updateList",u_updateMapList);

                            //update
                            wcsToDasService.updateWorkBatchOrderList(upListMap, u_logApiStatusList);

                            //초기화
                            u_updateMapList = new ArrayList<Map<String, Object>>();
                            u_logApiStatusList = new ArrayList<LogApiStatus>();
                        }
                    }
                } catch (Exception e1) {
                    result = "error";
                    log.error( " === WcsToDasBatch  error e1" + e1 );
                    resultMessage = e1.toString();
                }
            }
        }catch(Exception e){
            result = "error";
            log.error( " === WcsToDasBatch  error" + e );
            e.printStackTrace();
            resultMessage = e.toString();
        }finally {
            apiRunTimeEnd = System.currentTimeMillis();
            apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

            //배치 로그 정보 insert
            LogBatchExec logBatchExec = new LogBatchExec();

            logBatchExec.setExecMethod(KurlyConstants.METHOD_WORKBATCHORDER);
            if(KurlyConstants.STATUS_OK.equals(result)){
                logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
                logBatchExec.setMessageLog(KurlyConstants.METHOD_WORKBATCHORDER +" Sucess("+apiRunTime+"ms)");
            } else {
                logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
                logBatchExec.setMessageLog(resultMessage);
            }
            logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
            logBatchExec.setExecuteCount(executeCount);
            logBatchExec.setStartDate(startDate);

            //로그정보 적재
            logBatchExecService.createLogBatchExec(logBatchExec);
        }

        log.info("======workBatchOrderInfoTask end======");
    }

    /**
     *
     * @Name : logApiStatus
     * @작성일 : 2020. 12. 22.
     * @작성자 : jooni
     * @변경이력 : 2020. 12. 22. 최초작성
     * @Method 설명 : logApiStatus list 생성
     */
    public List<LogApiStatus> logApiStatus(Map<String, Object> logMap, List<WorkBatchOrderData> orderNoList) {
        List<LogApiStatus> logApiStatusList = new ArrayList<LogApiStatus>();

        for(WorkBatchOrderData w : orderNoList) {
            //로그 정보 insert
            LogApiStatus logApiStatus = new LogApiStatus();

            String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
            logApiStatus.setApiYyyymmdd(sYyyymmdd);
            logApiStatus.setExecMethod(KurlyConstants.METHOD_WORKBATCHORDER);

            logApiStatus.setWarehouseKey(w.getWarehouseKey());
            logApiStatus.setWorkBatchNo(w.getWorkBatchNo());  //작업배치번호

            logApiStatus.setShipUidWcs(w.getShipUidKey());  //출고오더UID(WCS)
            logApiStatus.setShipUidSeq(w.getShipUidItemSeq());  //출고오더UID순번(WCS)
            logApiStatus.setInvoiceNo(w.getInvoiceNo());  //송장번호
            logApiStatus.setSkuCode(w.getSkuCode());  //상품코드

            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonStr = mapper.writeValueAsString(w);

                logApiStatus.setApiInfo(jsonStr);
            } catch (IOException e) {
                logApiStatus.setApiInfo(w.toString());
            }

            String l_apiRunTime = logMap.get("apiRunTime").toString();
            String l_intfYn = logMap.get("dasIntfYn").toString();
            String l_intfMemo = logMap.get("dasIntfMemo").toString();

            logApiStatus.setApiUrl(KurlyConstants.METHOD_WORKBATCHORDER);
            logApiStatus.setApiRuntime(l_apiRunTime);

            logApiStatus.setIntfYn(l_intfYn); //'Y': 전송완료, 'N': 미전송
            if (KurlyConstants.STATUS_N.equals(l_intfYn)) {
                String c_intfMemo = StringUtil.cutString(l_intfMemo, 3500, "");
                logApiStatus.setIntfMemo(c_intfMemo);
            } else {
                logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
            }

            logApiStatusList.add(logApiStatus);
        }
        return logApiStatusList;
    }

    public void pickingCompleteToteInfoTask() {
        log.info("======pickingCompleteToteInfoTask start======");

        long apiRunTimeStart = 0;
        long apiRunTimeEnd   = 0;
        String apiRunTime    = "";

        apiRunTimeStart = System.currentTimeMillis();
        String result = KurlyConstants.STATUS_OK;
        String resultMessage = "";
        int executeCount = 0;
        Date startDate = Calendar.getInstance().getTime();

        try{
            //대상 조회.
            List<PickingInfoData> listPickingEndTote = wcsToDasService.selectPickingEndToteInfo();
            log.info("pickingToteCount=" + listPickingEndTote.size());

            List<Map<String, Object>> updateMapList = new ArrayList<Map<String, Object>>();
            List<LogApiStatus> logApiStatusList = new ArrayList<LogApiStatus>();

            for(PickingInfoData sendData : listPickingEndTote){
                //건당 시간 체크용
                long apiRunTimeStartFor = System.currentTimeMillis();

                String r_ifYn = KurlyConstants.STATUS_N;
                DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

                String retStatus = "";
                String retMessage = "";

                try {

                    //kafka 전송
                    deferredResult = wcsProducer.sendPickingInfoObject(sendData);

                    ResponseEntity<ResponseMesssage> res = (ResponseEntity<ResponseMesssage>)deferredResult.getResult();
                    retStatus = (String)res.getBody().getStatus();
                    retMessage = (String)res.getBody().getMessage();

                    //log.info(" >>>>>>sendCompleteTote retStatus=>"+retStatus);
	    			//log.info(" >>>>>>"+retMessage);

                    if(retStatus.equals("SUCCESS")) {
                        r_ifYn = KurlyConstants.STATUS_Y;
                    } else {
                        r_ifYn = KurlyConstants.STATUS_N;
                    }

                } catch (Exception ex) {
                    log.info("== send error == " + sendData.toString());
                    retMessage = ex.getMessage().substring(0, 90);
                    r_ifYn = KurlyConstants.STATUS_N;
                } finally {
                    Map<String, Object> updateMap = new HashMap<String, Object>();

                    if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
                        updateMap.put("dasIntfYn", KurlyConstants.STATUS_N);
                        updateMap.put("dasIntfMemo", retMessage.substring(0, 990));
                    } else {
                        updateMap.put("dasIntfYn", KurlyConstants.STATUS_Y);
                        updateMap.put("dasIntfMemo", "");
                    }

                    updateMap.put("modifiedUser", KurlyConstants.DEFAULT_USERID);
                    updateMap.put("toteId", sendData.getToteId());
                    updateMap.put("workBatchNo", sendData.getWorkBatchNo());

                    apiRunTimeEnd = System.currentTimeMillis();
                    apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;

                    updateMap.put("apiRunTime", apiRunTime);
                    updateMap.put("execMethod", KurlyConstants.METHOD_PICKINGINFO);
                    //update list data
                    updateMapList.add(updateMap);

                    //로그 저장  수집
                    LogApiStatus logApiStatus = new LogApiStatus();
                    logApiStatus = logApiStatusVo(updateMap, sendData);
                    logApiStatus.setExecMethod(KurlyConstants.METHOD_PICKINGINFO);

                    logApiStatusList.add(logApiStatus);

                    executeCount++;
                }

            }

            try
            {
                List<Map<String, Object>> u_updateMapList = new ArrayList<Map<String, Object>>();
                List<LogApiStatus> u_logApiStatusList = new ArrayList<LogApiStatus>();

                for(int i=0; i <updateMapList.size(); i++) {

                    u_updateMapList.add(updateMapList.get(i));
                    u_logApiStatusList.add(logApiStatusList.get(i));

                    if( (i>2 && i%50 == 0 ) || ( i == updateMapList.size()-1 ) ) {

                        Map<String, Object> upListMap = new HashMap<String, Object>();
                        upListMap.put("updateList",u_updateMapList);

                        //update
                        wcsToDasService.updatePickingCompletList(upListMap, u_logApiStatusList);

                        //초기화
                        u_updateMapList = new ArrayList<Map<String, Object>>();
                        u_logApiStatusList = new ArrayList<LogApiStatus>();

                    }
                }

            } catch (Exception e1) {
                result = "error";
                log.error( " === PackQpsCompletBatch  error e1" +e1 );
                e1.printStackTrace();
                resultMessage = e1.toString();
            }
        }catch(Exception e){
            result = "error";
            log.error( " === WcsToDasBatch  error" + e );
            e.printStackTrace();
            resultMessage = e.toString();
        }finally {

            apiRunTimeEnd = System.currentTimeMillis();
            apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

            //배치 로그 정보 insert
            LogBatchExec logBatchExec = new LogBatchExec();

            logBatchExec.setExecMethod(KurlyConstants.METHOD_PICKINGINFO);
            if(KurlyConstants.STATUS_OK.equals(result)){
                logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
                logBatchExec.setMessageLog(KurlyConstants.METHOD_PICKINGINFO +" Sucess("+apiRunTime+"ms)");
            } else {
                logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
                logBatchExec.setMessageLog(resultMessage);
            }
            logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
            logBatchExec.setExecuteCount(executeCount);
            logBatchExec.setStartDate(startDate);

            //로그정보 적재
            logBatchExecService.createLogBatchExec(logBatchExec);

        }

        log.info("======pickingCompleteToteInfoTask end======");
    }

    /**
     *
     * @Name : logApiStatusVo
     * @작성일 : 2020. 12. 22.
     * @작성자 : jooni
     * @변경이력 : 2020. 12. 22. 최초작성
     * @Method 설명 : logApiStatus Vo 생성
     */
    public LogApiStatus logApiStatusVo(Map<String, Object> updateMap, PickingInfoData pickingData) {

        //로그 정보 insert
        LogApiStatus logApiStatus = new LogApiStatus();

        String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
        logApiStatus.setApiYyyymmdd(sYyyymmdd);

        logApiStatus.setWorkBatchNo(pickingData.getWorkBatchNo());  //작업배치번호
        logApiStatus.setToteId(pickingData.getToteId());  //토트ID번호
        logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);

        // json 타입으로 저장
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(pickingData);

            logApiStatus.setApiInfo(jsonStr);
        } catch (IOException e) {
            logApiStatus.setApiInfo(pickingData.toString());
        }

        String l_apiRunTime = updateMap.get("apiRunTime").toString();
        String l_intfYn = updateMap.get("dasIntfYn").toString();
        String l_intfMemo = updateMap.get("dasIntfMemo").toString();

        logApiStatus.setApiUrl(updateMap.get("execMethod").toString());
        logApiStatus.setApiRuntime(l_apiRunTime);

        logApiStatus.setIntfYn(l_intfYn) ; //'Y': 전송완료, 'N': 미전송
        if(KurlyConstants.STATUS_N.equals(l_intfYn)) {
            String c_intfMemo = StringUtil.cutString(l_intfMemo, 3500, "");
            logApiStatus.setIntfMemo(c_intfMemo);
        } else {
            logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
        }

        return logApiStatus;
    }

    public void pickingCnclToteInfoTask() {
        log.info("======pickingCnclToteInfoTask start======");

        long apiRunTimeStart = 0;
        long apiRunTimeEnd   = 0;
        String apiRunTime    = "";

        apiRunTimeStart = System.currentTimeMillis();
        String result = KurlyConstants.STATUS_OK;
        String resultMessage = "";
        int executeCount = 0;
        Date startDate = Calendar.getInstance().getTime();

        try{
            //대상 조회.
            List<PickingInfoData> listPickingCnclTote = wcsToDasService.selectPickingCnclToteInfo();
            log.info("pickingCnclToteCount=" + listPickingCnclTote.size());

            List<Map<String, Object>> updateMapList = new ArrayList<Map<String, Object>>();
            List<LogApiStatus> logApiStatusList = new ArrayList<LogApiStatus>();

            for(PickingInfoData sendData : listPickingCnclTote){
                //건당 시간 체크용
                long apiRunTimeStartFor = System.currentTimeMillis();

                String r_ifYn = KurlyConstants.STATUS_N;
                DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

                String retStatus = "";
                String retMessage = "";

                try {

                    //kafka 전송
                    deferredResult = wcsProducer.sendPickingInfoObject(sendData);

                    ResponseEntity<ResponseMesssage> res = (ResponseEntity<ResponseMesssage>)deferredResult.getResult();
                    retStatus = (String)res.getBody().getStatus();
                    retMessage = (String)res.getBody().getMessage();

                    //log.info(" >>>>>>sendCompleteTote retStatus=>"+retStatus);
                    //log.info(" >>>>>>"+retMessage);

                    if(retStatus.equals("SUCCESS")) {
                        r_ifYn = KurlyConstants.STATUS_Y;
                    } else {
                        r_ifYn = KurlyConstants.STATUS_N;
                    }

                } catch (Exception ex) {
                    log.info("== send error == " + sendData.toString());
                    retMessage = ex.getMessage().substring(0, 90);
                    r_ifYn = KurlyConstants.STATUS_N;
                } finally {
                    Map<String, Object> updateMap = new HashMap<String, Object>();

                    if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
                        updateMap.put("dasIntfYn", KurlyConstants.STATUS_N);
                        updateMap.put("dasIntfMemo", retMessage.substring(0, 990));
                    } else {
                        updateMap.put("dasIntfYn", KurlyConstants.STATUS_Y);
                        updateMap.put("dasIntfMemo", "");
                    }

                    updateMap.put("modifiedUser", KurlyConstants.DEFAULT_USERID);
                    updateMap.put("toteId", sendData.getToteId());
                    updateMap.put("workBatchNo", sendData.getWorkBatchNo());

                    apiRunTimeEnd = System.currentTimeMillis();
                    apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;

                    updateMap.put("apiRunTime", apiRunTime);
                    updateMap.put("execMethod", KurlyConstants.METHOD_PICKINGINFO);

                    //update list data
                    updateMapList.add(updateMap);

                    //로그 저장  수집
                    LogApiStatus logApiStatus = new LogApiStatus();
                    logApiStatus = logApiStatusVo(updateMap, sendData);
                    logApiStatus.setExecMethod(KurlyConstants.METHOD_PICKINGINFO);

                    logApiStatusList.add(logApiStatus);

                    executeCount++;
                }

            }

            try
            {
                List<Map<String, Object>> u_updateMapList = new ArrayList<Map<String, Object>>();
                List<LogApiStatus> u_logApiStatusList = new ArrayList<LogApiStatus>();

                for(int i=0; i <updateMapList.size(); i++) {

                    u_updateMapList.add(updateMapList.get(i));
                    u_logApiStatusList.add(logApiStatusList.get(i));

                    if( (i > 2 && i % 50 == 0 ) || ( i == updateMapList.size()-1 ) ) {

                        Map<String, Object> upListMap = new HashMap<String, Object>();
                        upListMap.put("updateList",u_updateMapList);

                        //update
                        wcsToDasService.updatePickingCnclList(upListMap, u_logApiStatusList);

                        //초기화
                        u_updateMapList = new ArrayList<Map<String, Object>>();
                        u_logApiStatusList = new ArrayList<LogApiStatus>();

                    }
                }

            } catch (Exception e1) {
                result = "error";
                e1.printStackTrace();
                resultMessage = e1.toString();
            }
        }catch(Exception e){
            result = "error";
            e.printStackTrace();
            resultMessage = e.toString();
        }finally {

            apiRunTimeEnd = System.currentTimeMillis();
            apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

            //배치 로그 정보 insert
            LogBatchExec logBatchExec = new LogBatchExec();

            logBatchExec.setExecMethod(KurlyConstants.METHOD_PICKINGINFO);
            if(KurlyConstants.STATUS_OK.equals(result)){
                logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
                logBatchExec.setMessageLog(KurlyConstants.METHOD_PICKINGINFO +" Sucess("+apiRunTime+"ms)");
            } else {
                logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
                logBatchExec.setMessageLog(resultMessage);
            }
            logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
            logBatchExec.setExecuteCount(executeCount);
            logBatchExec.setStartDate(startDate);

            //로그정보 적재
            logBatchExecService.createLogBatchExec(logBatchExec);

        }

        log.info("======pickingCnclToteInfoTask end======");
    }
}
