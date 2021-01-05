package com.lgcns.wcs.kurly.jobs;

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

import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.dto.ResponseMesssage;
import com.lgcns.wcs.kurly.dto.ToteScanData;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.ToteScanService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : ToteScanBatch
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * 			2020. 11. 12. RunTime 로직 수정
 * @Method 설명 : WCS 토트 자동화 설비 투입 정보 (마스터)  (WCS => WMS)
 */
@Slf4j
@Component
public class ToteScanBatch  {

	@Autowired
    KurlyWcsToWmsProducer wcsProducer;
	
    @Autowired
    ToteScanService toteScanService;
    
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;
    
    public void ToteScanTask()  {
    	log.info("=======ToteScanBatch start=======");

    	long apiRunTimeStart = 0;
		long apiRunTimeEnd   = 0;
		String apiRunTime    = "";
		
		apiRunTimeStart = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();

    	List<Map<String, Object>> updateMapList = new ArrayList<Map<String, Object>>();
    	List<LogApiStatus> logApiStatusList = new ArrayList<LogApiStatus>();
    	
    	try {
    		//토트 자동화 설비 투입 정보 조죄
    		List<ToteScanData> listToteScan = toteScanService.selectToteScan();
	    	
        	log.info("toteScan size ==> "+ listToteScan.size());

	    	
        	// 전송할 데이타를 Kafka로 전송 및 전송결과 List에 저장
        	for(ToteScanData toteScanData : listToteScan ) {
        		//건당 시간 체크용
	    		long apiRunTimeStartFor = System.currentTimeMillis();

    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendToteScanObject(toteScanData);	
	    			ResponseEntity<ResponseMesssage> res = (ResponseEntity<ResponseMesssage>)deferredResult.getResult();
	    			retStatus = (String)res.getBody().getStatus();
	    			retMessage = (String)res.getBody().getMessage();
	    			log.info(" >>>>>>toteScan retStatus=>"+retStatus);
//	    			log.info(" >>>>>>"+retMessage);
//	    	    	log.info(" >>>>>>deferredResult.getResult()="+ deferredResult.getResult());
	    	    	
	    	    	if(retStatus.equals("SUCCESS")) {
	    				r_ifYn = KurlyConstants.STATUS_Y;
	    			} else {
	    				r_ifYn = KurlyConstants.STATUS_N;
	    			}
	    	    	
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + toteScanData.getToteId());  
	    			retMessage = ex.getMessage().substring(0, 90);
    				r_ifYn = KurlyConstants.STATUS_N;
	    			ex.printStackTrace();
	    		} finally {
	    			
	    			int r_toteUniqueNo = toteScanData.getToteUniqueNo();
					Map<String, Object> updateMap = new HashMap<String, Object>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("toteScanIfYn",KurlyConstants.STATUS_N);
						updateMap.put("toteScanIfRetCode",KurlyConstants.STATUS_NG);
						updateMap.put("toteScanIfRetMessage",retMessage.substring(0, 990));
					} else {
						updateMap.put("toteScanIfYn",KurlyConstants.STATUS_Y);
						updateMap.put("toteScanIfRetCode",KurlyConstants.STATUS_OK);
						updateMap.put("toteScanIfRetMessage","");
					}
					updateMap.put("toteUniqueNo",""+r_toteUniqueNo);

//			    	toteScanService.updateToteScan(updateMap);

	    			apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;
	    			
					updateMap.put("apiRunTime",apiRunTime);
	    			
					//update list data
	    			updateMapList.add(updateMap);
	    	    	
	    	    	//로그 저장  수집
	    	    	LogApiStatus logApiStatus = new LogApiStatus();
	    	    	logApiStatus = logApiStatusVo(updateMap, toteScanData);
					
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
        			//100 건 씩 처리
    	    		if( (i>2 && i%100 == 0 ) 
    	    				|| ( i == updateMapList.size()-1 ) ) {
    					
    					Map<String, Object> upListMap = new HashMap<String, Object>();
    					upListMap.put("updateList",u_updateMapList);
    			    	
    					//update
    					toteScanService.updateToteScanList(upListMap, u_logApiStatusList);
    					
    					//초기화
    					u_updateMapList = new ArrayList<Map<String, Object>>();
    					u_logApiStatusList = new ArrayList<LogApiStatus>();
    					
    	    		}	
        		}
        		
        	} catch (Exception e1) {
        		result = "error";
    			log.error( " === ToteScanBatch update error " +e1 );
    			resultMessage = e1.toString();
        	}
    	} catch (Exception e) {
    		result = "error";
			log.error( " === ToteScanBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

        	log.info("======= apiRunTime(ms) : "+ apiRunTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_TOTESCAN);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_TOTESCAN +" Sucess("+apiRunTime+"ms)");
        	} else {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
            	logBatchExec.setMessageLog(resultMessage);
        	}
        	logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
        	logBatchExec.setExecuteCount(executeCount);
        	logBatchExec.setStartDate(startDate);
        	
        	logBatchExecService.createLogBatchExec(logBatchExec);
    	}
    	
    	log.info("=======ToteScanBatch end=======");
    	
    }
    /**
     * 
     * @Name : logApiStatusVo
     * @작성일 : 2020. 07. 14.
     * @작성자 : jooni
     * @변경이력 : 2020. 07. 14. 최초작성
     * 			2020. 11. 12. RunTime 로직 수정
     * @Method 설명 : logApiStatus Vo 생성
     */
    public LogApiStatus logApiStatusVo(Map<String, Object> updateMap, ToteScanData toteScanData) {
		
    	//로그 정보 insert
    	LogApiStatus logApiStatus = new LogApiStatus();

    	String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
	    logApiStatus.setApiYyyymmdd(sYyyymmdd);
    	logApiStatus.setExecMethod(KurlyConstants.METHOD_TOTESCAN);
    	
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

		if(toteScanData != null) {

	    	if(toteScanData.getWarehouseKey() ==null ||
					"".equals(toteScanData.getWarehouseKey())) {
				logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
			} else {
				logApiStatus.setWarehouseKey(toteScanData.getWarehouseKey());
			}
	    	
	    	logApiStatus.setToteId(toteScanData.getToteId());  //토트ID번호
	    	logApiStatus.setApiInfo(toteScanData.toString());
			
		} else {
	    	logApiStatus.setToteId("");  //토트ID번호
	    	logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
	    	logApiStatus.setApiInfo("");
		}

		String l_apiRunTime = updateMap.get("apiRunTime").toString();
		String l_intfYn = updateMap.get("toteScanIfYn").toString();
		String l_intfMemo = updateMap.get("toteScanIfRetMessage").toString();
		
		
    	logApiStatus.setApiUrl(KurlyConstants.METHOD_TOTESCAN);
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

