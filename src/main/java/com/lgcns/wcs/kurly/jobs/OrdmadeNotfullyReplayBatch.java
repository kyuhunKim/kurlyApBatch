package com.lgcns.wcs.kurly.jobs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplayData;
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplaySendData;
import com.lgcns.wcs.kurly.dto.ResponseMesssage;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.OrdmadeNotfullyReplayService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : OrdmadeNotfullyReplayBatch
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * 			2020. 11. 12. RunTime 로직 수정
 * @Method 설명 : WCS 미출오더 상품보충용 추가피킹정보 연계  (WCS => WMS)
 */
@Slf4j
@Component
public class OrdmadeNotfullyReplayBatch {

	@Autowired
    KurlyWcsToWmsProducer wcsProducer;
	
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;

    @Autowired
    OrdmadeNotfullyReplayService ordmadeNotfullyReplayService;

    public void OrdmadeNotfullyReplayTask() {
    	log.info("=======OrdmadeNotfullyReplayBatch start=======");
    	long apiRunTimeStart = 0;
		long apiRunTimeEnd   = 0;
		String apiRunTime    = "";
		
		apiRunTimeStart = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();
    	try
    	{
    		//WCS 미출오더 상품보충용 추가피킹정보  데이타 조회
    		//##2021.02.03 (OD.QTY_NOTFULLY_REQPICK - OD.NOTFULLY_REQ_IF_REQPICK ) > 0 인건 만 인터페이스 함
    		List<OrdmadeNotfullyReplayData> listOrdmadeNotfullyReplay = ordmadeNotfullyReplayService.selectOrdmadeNotfullyReplay();

	    	//조회 건수 
//	    	executeCount = listOrdmadeNotfullyReplay.size();
//	    	log.info("listOrdmadeNotfullyReplay size ==> "+ listOrdmadeNotfullyReplay.size());

	    	List<Map<String, Object>> updateMapList = new ArrayList<Map<String, Object>>();
	    	List<LogApiStatus> logApiStatusList = new ArrayList<LogApiStatus>();
	    	
	    	for(OrdmadeNotfullyReplayData ordmadeNotfullyReplayData : listOrdmadeNotfullyReplay ) {
	    		
	    		OrdmadeNotfullyReplaySendData ordmadeNotfullyReplaySendData = new OrdmadeNotfullyReplaySendData();
	    		
	    		ordmadeNotfullyReplaySendData.setShipOrderKey(ordmadeNotfullyReplayData.getShipOrderKey());
	    		ordmadeNotfullyReplaySendData.setShipOrderItemSeq(ordmadeNotfullyReplayData.getShipOrderItemSeq());
	    		ordmadeNotfullyReplaySendData.setWarehouseKey(ordmadeNotfullyReplayData.getWarehouseKey());
	    		ordmadeNotfullyReplaySendData.setShipUidKey(ordmadeNotfullyReplayData.getShipUidKey());
	    		ordmadeNotfullyReplaySendData.setShipUidItemSeq(ordmadeNotfullyReplayData.getShipUidItemSeq());
	    		ordmadeNotfullyReplaySendData.setPickingType(ordmadeNotfullyReplayData.getPickingType());
	    		ordmadeNotfullyReplaySendData.setInvoiceNo(ordmadeNotfullyReplayData.getInvoiceNo());
	    		ordmadeNotfullyReplaySendData.setGroupNo(ordmadeNotfullyReplayData.getGroupNo());
	    		ordmadeNotfullyReplaySendData.setWorkBatchNo(ordmadeNotfullyReplayData.getWorkBatchNo());
	    		ordmadeNotfullyReplaySendData.setSkuCode(ordmadeNotfullyReplayData.getSkuCode());
	    		ordmadeNotfullyReplaySendData.setSkuName(ordmadeNotfullyReplayData.getSkuName());
	    		ordmadeNotfullyReplaySendData.setSkuSubName(ordmadeNotfullyReplayData.getSkuSubName());
	    		ordmadeNotfullyReplaySendData.setQtyNotfullyReqpick(ordmadeNotfullyReplayData.getQtyNotfullyReqpick());
	    		ordmadeNotfullyReplaySendData.setInsertedDate(ordmadeNotfullyReplayData.getInsertedDate());
	    		ordmadeNotfullyReplaySendData.setInsertedTime(ordmadeNotfullyReplayData.getInsertedTime());
	    		ordmadeNotfullyReplaySendData.setInsertedUser(ordmadeNotfullyReplayData.getInsertedUser());
	    		
	    		
	    		//건당 시간 체크용
	    		long apiRunTimeStartFor = System.currentTimeMillis();

    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendOrdmadeNotfullyReplayObject(ordmadeNotfullyReplaySendData);
	    			
	    			ResponseEntity<ResponseMesssage> res = (ResponseEntity<ResponseMesssage>)deferredResult.getResult();
	    			retStatus = (String)res.getBody().getStatus();
	    			retMessage = (String)res.getBody().getMessage();
	    			log.info(" >>>>>>OrdmadeNotfullyReplayBatch retStatus=>"+retStatus);
//	    			log.info(" >>>>>>"+retMessage);
//	    	    	log.info(" >>>>>>OrdmadeNotfullyReplayBatch deferredResult.getResult()="+ deferredResult.getResult());
	    	    	
//	    			if(deferredResult.getResult().toString().indexOf("SUCCESS") > -1) {
	    	    	if(retStatus.equals("SUCCESS")) {
	    				r_ifYn = KurlyConstants.STATUS_Y;
	    			} else {
	    				r_ifYn = KurlyConstants.STATUS_N;
	    			}

	    		} catch (Exception ex) {	
	    			log.info("== send error == " + ordmadeNotfullyReplayData.getInvoiceNo());  
	    			retMessage = ex.getMessage().substring(0, 90);
    				r_ifYn = KurlyConstants.STATUS_N;
	    		} finally {
			    	//인터페이스 처리내역 update
	    			String r_invoiceNo = ordmadeNotfullyReplayData.getInvoiceNo();
	    			String r_invoiceSeq = ordmadeNotfullyReplayData.getInvoiceSeq();
	    			double r_qtyNotfullyReqpick = ordmadeNotfullyReplayData.getQtyNotfullyReqpick();
	    			
					Map<String, Object> updateMap = new HashMap<String, Object>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("notfullyReqIfYn",KurlyConstants.STATUS_N);
						updateMap.put("notfullyReqIfRetCode",KurlyConstants.STATUS_NG);
						updateMap.put("notfullyReqIfRetMessage",retMessage.substring(0, 990));
					} else {
						updateMap.put("notfullyReqIfYn",KurlyConstants.STATUS_Y);
						updateMap.put("notfullyReqIfRetCode",KurlyConstants.STATUS_OK);
						updateMap.put("notfullyReqIfRetMessage","");
					}
					updateMap.put("modifiedUser",KurlyConstants.DEFAULT_USERID);
					updateMap.put("invoiceNo",r_invoiceNo);
					updateMap.put("qtyNotfullyReqpick",""+r_qtyNotfullyReqpick);
					updateMap.put("invoiceSeq",r_invoiceSeq);

//					ordmadeNotfullyReplayService.updateOrdmadeNotfullyReplay(updateMap);
					apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;
	    			
					updateMap.put("apiRunTime",apiRunTime);
	    			
					//update list data
	    			updateMapList.add(updateMap);
	    	    	
	    	    	//로그 저장  수집
	    	    	LogApiStatus logApiStatus = new LogApiStatus();
	    	    	logApiStatus = logApiStatusVo(updateMap, ordmadeNotfullyReplayData);
					
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
        			
        			//100 건 씩 처리 ##2021.01.13 50건으로 변경
		    		if( (i>2 && i%50 == 0 ) 
		    				|| ( i == updateMapList.size()-1 ) ) {

						Map<String, Object> upListMap = new HashMap<String, Object>();
						upListMap.put("updateList",u_updateMapList);
				    	
						//update
						ordmadeNotfullyReplayService.updateOrdmadeNotfullyReplayList(upListMap, u_logApiStatusList);
						
						//초기화
						u_updateMapList = new ArrayList<Map<String, Object>>();
				    	u_logApiStatusList = new ArrayList<LogApiStatus>();
						
		    		}
        		}

	    	} catch (Exception e1) {
        		result = "error";
    			log.error( " === OrdmadeNotfullyReplayBatch  error e1" +e1 );
    			resultMessage = e1.toString();
        	}
    	} catch (Exception e) {
    		result = "error";
			log.info( " === OrdmadeNotfullyReplayBatch  error" +e );
			resultMessage = e.toString();
    	} finally {

			apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_ORDMADENOTFULLYREPLAY);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_ORDMADENOTFULLYREPLAY +" Sucess("+apiRunTime+"ms)");	
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
    	log.info("=======OrdmadeNotfullyReplayBatch end=======");
    	
    }

    /**
     * 
     * @Name : logApiStatusVo
     * @작성일 : 2020. 12. 22.
     * @작성자 : jooni
     * @변경이력 : 2020. 12. 22. 최초작성
     * @Method 설명 : logApiStatus Vo 생성
     */
    public LogApiStatus logApiStatusVo(Map<String, Object> updateMap, OrdmadeNotfullyReplayData ordmadeNotfullyReplayData) {
		
    	//로그 정보 insert
    	LogApiStatus logApiStatus = new LogApiStatus();

    	String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
	    logApiStatus.setApiYyyymmdd(sYyyymmdd);
    	logApiStatus.setExecMethod(KurlyConstants.METHOD_ORDMADENOTFULLYREPLAY);
    	
    	logApiStatus.setGroupNo("");  //그룹배치번호
    	logApiStatus.setWorkBatchNo("");  //작업배치번호
    	
    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
    	logApiStatus.setShipUidSeq("");  //출고오더UID순번(WCS)
    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
    	logApiStatus.setShipOrderItemSeq("");  //출하문서순번(WMS)

    	logApiStatus.setToteId("");  //토트ID번호
    	logApiStatus.setInvoiceNo("");  //송장번호

    	logApiStatus.setStatus("");  //상태
    	
    	logApiStatus.setQtyOrder(0);  //지시수량
    	logApiStatus.setQtyComplete(0);  //작업완료수량
    	
    	logApiStatus.setSkuCode("");  //상품코드
    	logApiStatus.setWcsStatus("");  //WCS 작업상태

		if(ordmadeNotfullyReplayData != null) {

			if(ordmadeNotfullyReplayData.getWarehouseKey() ==null ||
					"".equals(ordmadeNotfullyReplayData.getWarehouseKey())) {
				logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
			} else {
				logApiStatus.setWarehouseKey(ordmadeNotfullyReplayData.getWarehouseKey());
			}
			
	    	logApiStatus.setGroupNo(ordmadeNotfullyReplayData.getGroupNo());  //그룹배치번호
	    	logApiStatus.setWorkBatchNo(ordmadeNotfullyReplayData.getWorkBatchNo());  //작업배치번호
	    	
	    	logApiStatus.setShipUidWcs(ordmadeNotfullyReplayData.getShipUidKey());  //출고오더UID(WCS)
	    	logApiStatus.setShipUidSeq(ordmadeNotfullyReplayData.getShipUidItemSeq());  //출고오더UID순번(WCS)
	    	logApiStatus.setShipOrderKey(ordmadeNotfullyReplayData.getShipOrderKey());  //출하문서번호(WMS)
	    	logApiStatus.setShipOrderItemSeq(ordmadeNotfullyReplayData.getShipOrderItemSeq());  //출하문서순번(WMS)
	    	logApiStatus.setInvoiceNo(ordmadeNotfullyReplayData.getInvoiceNo());  //송장번호

	    	logApiStatus.setQtyOrder(ordmadeNotfullyReplayData.getQtyNotfullyReqpick());  //지시수량
	    	logApiStatus.setSkuCode(ordmadeNotfullyReplayData.getSkuCode());  //상품코드
	    	
//	    	logApiStatus.setApiInfo(ordmadeNotfullyReplayData.toString());
	    	//##20210106  json 타입으로 저장 
			try {
				ObjectMapper mapper = new ObjectMapper();
				String jsonStr = mapper.writeValueAsString(ordmadeNotfullyReplayData);

				logApiStatus.setApiInfo(jsonStr);
			} catch (IOException e) {
//	            e.printStackTrace();
				logApiStatus.setApiInfo(ordmadeNotfullyReplayData.toString());
	        }
			
		} else {
			logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
	    	logApiStatus.setGroupNo("");  //그룹배치번호
	    	logApiStatus.setWorkBatchNo("");  //작업배치번호
	    	
	    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
	    	logApiStatus.setShipUidSeq("");  //출고오더UID순번(WCS)
	    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
	    	logApiStatus.setShipOrderItemSeq("");  //출하문서순번(WMS)
	    	logApiStatus.setInvoiceNo("");  //송장번호

	    	logApiStatus.setQtyOrder(0);  //지시수량
	    	logApiStatus.setSkuCode("");  //상품코드
	    	logApiStatus.setApiInfo("");
		}

		String l_apiRunTime = updateMap.get("apiRunTime").toString();
		String l_intfYn = updateMap.get("notfullyReqIfYn").toString();
		String l_intfMemo = updateMap.get("notfullyReqIfRetMessage").toString();
		
		
    	logApiStatus.setApiUrl(KurlyConstants.METHOD_ORDMADENOTFULLYREPLAY);
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
}