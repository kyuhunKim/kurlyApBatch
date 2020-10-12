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

import com.lgcns.wcs.kurly.dto.InvoiceSortCompletData;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.dto.ResponseMesssage;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.InvoiceSortCompletService;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : InvoiceSortCompletBatch
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @Method 설명 : WCS 방면 분류 완료 정보  (WCS => WMS)
 */
@Slf4j
@Component
public class InvoiceSortCompletBatch  {

	@Autowired
    KurlyWcsToWmsProducer wcsProducer;
	
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;
    
    @Autowired
    InvoiceSortCompletService invoiceSortCompletService;

    public void InvoiceSortCompletTask() {
    	log.info("=================InvoiceSortCompletBatch start===============");
    	log.info("The current date  : " + LocalDateTime.now());
    	long start = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();
    	try
    	{
    		//WCS 방면 분류 완료 정보 데이타 조회
    		List<InvoiceSortCompletData> listInvoiceSortComplet = invoiceSortCompletService.selectInvoiceSortComplet();
	    	
	    	//조회 건수 
	    	executeCount = listInvoiceSortComplet.size();
	    	log.info("invoiceSortComplet size ==> "+ listInvoiceSortComplet.size());
	    	
	    	for(InvoiceSortCompletData invoiceSortCompletData : listInvoiceSortComplet ) {

				long startFor = System.currentTimeMillis();

    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendInvoiceSortCompletObject(invoiceSortCompletData);
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
	    			
	    			log.info("=================updateInvoiceSortComplet===============1");
			    	//인터페이스 처리내역 update
	    			String r_invoiceNo = invoiceSortCompletData.getInvoiceNo();
	    			String r_warehouseKey = invoiceSortCompletData.getWarehouseKey();
	    			String r_shipOrderKey = invoiceSortCompletData.getShipOrderKey();
					Map<String, String> updateMap = new HashMap<String, String>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("invoiceSortIntfYn",KurlyConstants.STATUS_N);
						updateMap.put("invoiceSortIntfCode",KurlyConstants.STATUS_NG);
						updateMap.put("invoiceSortIntfMemo",retMessage);
					} else {
						updateMap.put("invoiceSortIntfYn",KurlyConstants.STATUS_Y);
						updateMap.put("invoiceSortIntfCode",KurlyConstants.STATUS_OK);
						updateMap.put("invoiceSortIntfMemo","");
					}
					updateMap.put("modifiedUser",KurlyConstants.DEFAULT_USERID);
					updateMap.put("invoiceNo",r_invoiceNo);
					updateMap.put("warehouseKey",r_warehouseKey);
					updateMap.put("shipOrderKey",r_shipOrderKey);
					
			    	invoiceSortCompletService.updateInvoiceSortComplet(updateMap);

			    	log.info("=================updateInvoiceSortComplet===============2");
			    	
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + invoiceSortCompletData.getInvoiceNo());  
	    			retMessage = ex.getMessage().substring(0, 90);
//	    			throw new Exception("", e);
	    		} finally {
	    			log.info("====finally createLogApiStatus===============1");
	    			
					long endFor = System.currentTimeMillis(); 
					long diffTimeFor = ( endFor - startFor ); //ms

					//전송로그 정보 insert
			    	LogApiStatus logApiStatus = new LogApiStatus();

			    	if(invoiceSortCompletData.getWarehouseKey() ==null ||
							"".equals(invoiceSortCompletData.getWarehouseKey())) {
						logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
					}

			    	logApiStatus.setApiYyyymmdd(invoiceSortCompletData.getInsertedDate()); 
			    	logApiStatus.setExecMethod(KurlyConstants.METHOD_INVOICESORTCOMPLET);
			    	
			    	logApiStatus.setGroupNo("");  //그룹배치번호
			    	logApiStatus.setWorkBatchNo("");  //작업배치번호
			    	
			    	logApiStatus.setShipUidWcs(invoiceSortCompletData.getShipUidWcs());  //출고오더UID(WCS)
			    	logApiStatus.setShipUidSeq("");  //출고오더UID순번(WCS)
			    	logApiStatus.setShipOrderKey(invoiceSortCompletData.getShipOrderKey());  //출하문서번호(WMS)
			    	logApiStatus.setShipOrderItemSeq("");  //출하문서순번(WMS)

			    	logApiStatus.setToteId("");  //토트ID번호
			    	logApiStatus.setInvoiceNo(invoiceSortCompletData.getInvoiceNo());  //송장번호

			    	logApiStatus.setStatus("");  //상태
			    	
			    	logApiStatus.setQtyOrder(0);  //지시수량
			    	logApiStatus.setQtyComplete(0);  //작업완료수량
			    	
			    	logApiStatus.setSkuCode("");  //상품코드
			    	logApiStatus.setWcsStatus(invoiceSortCompletData.getInvoiceStatus());  //WCS 작업상태
			    	
			    	logApiStatus.setApiUrl(KurlyConstants.METHOD_INVOICESORTCOMPLET);
			    	logApiStatus.setApiInfo(invoiceSortCompletData.toString());
			    	logApiStatus.setApiRuntime(diffTimeFor+"");
			    	
			    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
			    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
			    		logApiStatus.setIntfMemo(retMessage);
			    	} else {
			    		logApiStatus.setIntfMemo("");
			    	}
			    	
			    	//로그정보 적재
			    	logApiStatusService.createLogApiStatus(logApiStatus);
	    			log.info("====finally createLogApiStatus===============2");
			    	
	    		}

	    	}
    	
    	} catch (Exception e) {
    		result = "error";
			log.info( " === InvoiceSortCompletBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

        	long end = System.currentTimeMillis();
        	long diffTime = ( end - start );  //m

        	log.info("================= diffTime(ms) : "+ diffTime);
        	

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_INVOICESORTCOMPLET);
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
        	
    		
	    	//로그정보 적재
        	logBatchExecService.createLogBatchExec(logBatchExec);
	    	
        	log.info("=================InvoiceSortCompletBatch end=============== ");    		
    	}
    	log.info("=================InvoiceSortCompletBatch end===============");
    	
    }
}