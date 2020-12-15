package com.lgcns.wcs.kurly.jobs;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.RegionMasterService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : RegionMasterBatch
 * @작성일 : 2020. 08. 11.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 11. 최초작성
 * @Method 설명 : WMS 분류 권역 정보 변경 (URL 호출)
 */
@Slf4j
@Component
public class RegionMasterBatch  {
	
    @Autowired
    RegionMasterService regionMasterService;
    
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;

    public void RegionMasterTask()  {
    	log.info("=================RegionMasterBatch start===============");
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
    		
    		String retData = regionMasterService.insertRegionMaster();

			//로그 정보 insert
			LogApiStatus logApiStatus = new LogApiStatus();

//			logApiStatus.setWarehouseKey(" ");

	    	String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
		    logApiStatus.setApiYyyymmdd(sYyyymmdd);
			logApiStatus.setExecMethod(KurlyConstants.METHOD_REGIONMASTER);
			    	
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
	    	
	    	logApiStatus.setApiUrl(KurlyConstants.METHOD_REGIONMASTER);
	    	
	    	logApiStatus.setApiInfo(retData);
	    	
	    	logApiStatus.setIntfYn("Y") ; //'Y': 전송완료, 'N': 미전송
	    	
	    	logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;
			
			logApiStatus.setApiRuntime(apiRunTime);
	    	
	    	logApiStatusService.createLogApiStatus(logApiStatus);
			    	
    	} catch (Exception e) {
    		result = "error";
			log.error( " === ToteScanBatch  error" +e );
			resultMessage = e.toString();
    	} finally {

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

        	log.info("================= apiRunTime(ms) : "+ apiRunTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_REGIONMASTER);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_REGIONMASTER +" Sucess("+apiRunTime+"ms)");
        	} else {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
            	logBatchExec.setMessageLog(resultMessage);
        	}
        	logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
        	logBatchExec.setExecuteCount(executeCount);
        	logBatchExec.setStartDate(startDate);
        	
        	logBatchExecService.createLogBatchExec(logBatchExec);
    	}
    	
    	log.info("=================RegionMasterBatch end===============");
    	
    }

}

