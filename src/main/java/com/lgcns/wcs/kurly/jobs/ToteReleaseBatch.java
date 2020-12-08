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
import com.lgcns.wcs.kurly.dto.ToteReleaseParamData;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.ToteReleaseService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @Name : ToteReleaseBatch
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * 			2020. 11. 12. RunTime 로직 수정
 * @Method 설명 : 토트 마스트 초기화 연계  (WCS => WMS)
 */
@Slf4j
@Component
public class ToteReleaseBatch  {

    @Autowired
    KurlyWcsToWmsProducer wcsProducer;
	
    @Autowired
    ToteReleaseService toteReleaseService;
    
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;
        

    public void ToteReleaseTask()  {
    	
    	log.info("=================ToteReleaseBatch start===============");
    	log.info("The current date  : " + LocalDateTime.now());
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
		Date startDate = Calendar.getInstance().getTime();
		
		long apiRunTimeStart = 0;
		long apiRunTimeEnd   = 0;
		String apiRunTime    = "";
		
		apiRunTimeStart = System.currentTimeMillis();
		
    	try {
        	
	    	List<ToteReleaseParamData> listToteRelease = toteReleaseService.selectToteRelease();
	    	
	    	//조회 건수 
//	    	executeCount = listToteRelease.size();
	    	
	    	log.info("toteRelease size ==> "+ listToteRelease.size());
	    	for(ToteReleaseParamData toteReleaseData : listToteRelease ) {

	    		//건당 시간 체크용
	    		long apiRunTimeStartFor = System.currentTimeMillis();

    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
    			
    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			
	    			log.info("==toteId=="+toteReleaseData.getToteId());
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendToteReleaseObject(toteReleaseData);
	    			ResponseEntity<ResponseMesssage> res = (ResponseEntity<ResponseMesssage>)deferredResult.getResult();
	    			retStatus = (String)res.getBody().getStatus();
	    			retMessage = (String)res.getBody().getMessage();
	    			log.info(" >>>>>>>>>>>"+retStatus);
	    			log.info(" >>>>>>>>>>>"+retMessage);
	    	    	log.info(" >>>>>>>>>>>deferredResult.getResult()="+ deferredResult.getResult());
	    	    	
//	    			if(deferredResult.getResult().toString().indexOf("SUCCESS") > -1) {
	    	    	if(retStatus.equals("SUCCESS")) {
	    	    		r_ifYn = KurlyConstants.STATUS_Y;
	    			} else {
	    				r_ifYn = KurlyConstants.STATUS_N;
	    			}
					
			    	String r_toteId = toteReleaseData.getToteId();
			    	int r_toteUniqueNo = toteReleaseData.getToteUniqueNo();
					Map<String, String> updateMap = new HashMap<String, String>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("toteReleaseIntfYn",KurlyConstants.STATUS_N);
						updateMap.put("toteReleaseIntfCode",KurlyConstants.STATUS_NG);
						updateMap.put("toteReleaseIntfMemo",retMessage.substring(0, 990));
					} else {
						updateMap.put("toteReleaseIntfYn",KurlyConstants.STATUS_Y);
						updateMap.put("toteReleaseIntfCode",KurlyConstants.STATUS_OK);
						updateMap.put("toteReleaseIntfMemo","");
					}
					updateMap.put("modifiedUser",KurlyConstants.DEFAULT_USERID);
//					updateMap.put("toteId",r_toteId);
					updateMap.put("toteUniqueNo",""+r_toteUniqueNo);

			    	log.info("=================updateToteRelease===============1");
					toteReleaseService.updateToteRelease(updateMap);

			    	log.info("=================updateToteRelease===============2");
			    	
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + toteReleaseData.getToteId());  
	    			retMessage = ex.getMessage().substring(0, 90);
    				r_ifYn = KurlyConstants.STATUS_N;
	    		} finally {
	    			log.info("====finally createLogApiStatus===============1");

	    			//로그 정보 insert
			    	LogApiStatus logApiStatus = new LogApiStatus();

			    	logApiStatus.setExecMethod(KurlyConstants.METHOD_TOTERELEASE);
			    	
			    	logApiStatus.setGroupNo("");  //그룹배치번호
			    	logApiStatus.setWorkBatchNo("");  //작업배치번호
			    	
			    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
			    	logApiStatus.setShipUidSeq("");  //출고오더UID순번(WCS)
			    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
			    	logApiStatus.setShipOrderItemSeq("");  //출하문서순번(WMS)

			    	logApiStatus.setInvoiceNo("");  //송장번호

			    	logApiStatus.setStatus("");  //상태
			    	
			    	logApiStatus.setQtyOrder(0);  //지시수량
			    	logApiStatus.setQtyComplete(0);  //작업완료수량
			    	
			    	logApiStatus.setSkuCode("");  //상품코드
			    	logApiStatus.setWcsStatus("");  //WCS 작업상태
			    	
			    	logApiStatus.setApiUrl(KurlyConstants.METHOD_TOTERELEASE);
			    	
			    	String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
				    logApiStatus.setApiYyyymmdd(sYyyymmdd);
				    
					if(toteReleaseData != null) {
						if(toteReleaseData.getWarehouseKey() ==null ||
								"".equals(toteReleaseData.getWarehouseKey())) {
							logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
						} else {
							logApiStatus.setWarehouseKey(toteReleaseData.getWarehouseKey());
						}
				    	logApiStatus.setApiInfo(toteReleaseData.toString());
				    	logApiStatus.setToteId(toteReleaseData.getToteId());  //토트ID번호
					} else {
				    	logApiStatus.setApiInfo("");
				    	logApiStatus.setToteId("");  //토트ID번호
						logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
					}
			    	
			    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
			    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
			    		logApiStatus.setIntfMemo(retMessage);
			    	} else {
			    		logApiStatus.setIntfMemo("");
			    	}

			    	apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;
			    	logApiStatus.setApiRuntime(apiRunTime);
			    	
			    	//로그정보 적재
			    	logApiStatusService.createLogApiStatus(logApiStatus);
	    			log.info("====finally createLogApiStatus===============2");
			    	
	    		}
	    		executeCount++;
	    	}
    	
    	} catch (Exception e) {
    		result = "error";
			log.error( " === ToteReleaseBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

			apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;
			
        	log.info("================= apiRunTime(ms) : "+ apiRunTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_TOTERELEASE);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_TOTERELEASE +" Sucess("+apiRunTime+"ms)");
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
    	log.info("=================ToteReleaseTasklet end=============== ");    

    }
}

