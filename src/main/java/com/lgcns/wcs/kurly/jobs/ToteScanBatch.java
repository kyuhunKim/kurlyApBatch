package com.lgcns.wcs.kurly.jobs;

import java.time.LocalDateTime;
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
    	log.info("=================ToteScanBatch start===============");
    	log.info("The current date  : " + LocalDateTime.now());
    	long apiRunTimeStart = 0;
		long apiRunTimeEnd   = 0;
		String apiRunTime    = "";
		
		apiRunTimeStart = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();
    	try {
    		//토트 자동화 설비 투입 정보 조죄
    		List<ToteScanData> listToteScan = toteScanService.selectToteScan();

	    	//조회 건수 
//	    	executeCount = listToteScan.size();
	    	
        	log.info("toteScan size ==> "+ listToteScan.size());
        	
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
	    			log.info(" >>>>>>>>>>>"+retStatus);
	    			log.info(" >>>>>>>>>>>"+retMessage);
	    	    	log.info(" >>>>>>>>>>>deferredResult.getResult()="+ deferredResult.getResult());
	    	    	
	    	    	if(retStatus.equals("SUCCESS")) {
	    				r_ifYn = KurlyConstants.STATUS_Y;
	    			} else {
	    				r_ifYn = KurlyConstants.STATUS_N;
	    			}
	    			
	    			String r_toteId = toteScanData.getToteId();
	    			String r_warehouseKey = toteScanData.getWarehouseKey();
	    			int r_toteUniqueNo = toteScanData.getToteUniqueNo();
					Map<String, String> updateMap = new HashMap<String, String>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("toteScanIfYn",KurlyConstants.STATUS_N);
						updateMap.put("toteScanIfRetCode",KurlyConstants.STATUS_NG);
						updateMap.put("toteScanIfRetMessage",retMessage);
					} else {
						updateMap.put("toteScanIfYn",KurlyConstants.STATUS_Y);
						updateMap.put("toteScanIfRetCode",KurlyConstants.STATUS_OK);
						updateMap.put("toteScanIfRetMessage","");
					}
					updateMap.put("toteUniqueNo",""+r_toteUniqueNo);

			    	toteScanService.updateToteScan(updateMap);

			    	log.info("=================updateToteRelease===============2");
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + toteScanData.getToteId());  
	    			retMessage = ex.getMessage().substring(0, 90);
    				r_ifYn = KurlyConstants.STATUS_N;
	    			ex.printStackTrace();
	    		} finally {
	    			log.info("====finally createLogApiStatus===============1");

	    			apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;

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
					
			    	logApiStatus.setApiUrl(KurlyConstants.METHOD_TOTESCAN);
			    	logApiStatus.setApiRuntime(apiRunTime);
			    	
			    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
			    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
			    		logApiStatus.setIntfMemo(retMessage);
			    	} else {
			    		logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
			    	}
			    	
			    	logApiStatusService.createLogApiStatus(logApiStatus);
	    			log.info("====finally createLogApiStatus===============2");
			    	
	    		}
	    		executeCount++;
        	}	
        	
    	} catch (Exception e) {
    		result = "error";
			log.error( " === ToteScanBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

        	log.info("================= apiRunTime(ms) : "+ apiRunTime);

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
        	log.info("=================createLogBatchExec end=============== ");    		
    	}
    	
    	log.info("=================ToteScanBatch end===============");
    	
    }

}

