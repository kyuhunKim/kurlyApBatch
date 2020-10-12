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
    	long start = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();
    	try {
    		
    		String retData = regionMasterService.insertRegionMaster();

    		log.info("====finally createLogApiStatus===============1");

			long endFor = System.currentTimeMillis(); 
			long diffTimeFor = ( endFor - start ); //ms

			//로그 정보 insert
			LogApiStatus logApiStatus = new LogApiStatus();

			logApiStatus.setWarehouseKey(" ");
			
			logApiStatus.setApiYyyymmdd(""); 
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
	    	logApiStatus.setApiRuntime(diffTimeFor+"");
	    	
	    	logApiStatus.setIntfYn("Y") ; //'Y': 전송완료, 'N': 미전송
	    	
	    	logApiStatus.setIntfMemo("");
	    	
	    	logApiStatusService.createLogApiStatus(logApiStatus);
			log.info("====finally createLogApiStatus===============2");
			    	
    	} catch (Exception e) {
    		result = "error";
			log.error( " === ToteScanBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

        	long end = System.currentTimeMillis();
        	long diffTime = ( end - start );  //m

        	log.info("================= diffTime(ms) : "+ diffTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_REGIONMASTER);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog("");	
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
    	
    	log.info("=================RegionMasterBatch end===============");
    	
    }

}

