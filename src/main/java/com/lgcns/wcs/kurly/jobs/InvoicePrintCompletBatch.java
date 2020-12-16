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

import com.lgcns.wcs.kurly.dto.InvoicePrintCompletData;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.dto.ResponseMesssage;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.InvoicePrintCompletService;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : InvoicePrintCompletBatch
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * 			2020. 11. 12. RunTime 로직 수정
 * 			2020. 12. 14. ORIGIN_INVOICE_NO -0001 없이 전송하도록 수정 
 * @Method 설명 : WCS 운송장 발행 정보  (WCS => WMS)
 */
@Slf4j
@Component
public class InvoicePrintCompletBatch  {

	@Autowired
    KurlyWcsToWmsProducer wcsProducer;
	
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;
    
    @Autowired
    InvoicePrintCompletService invoicePrintCompletService;

    public void InvoicePrintCompletTask() {
    	log.info("=================InvoicePrintCompletBatch start===============");
    	log.info("The current date  : " + LocalDateTime.now());

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
        	
    		//WCS 운송장 발행 정보 데이타 조회
    		List<InvoicePrintCompletData> listInvoicePrintComplet = invoicePrintCompletService.selectInvoicePrintComplet();
	    	
	    	//조회 건수 
//	    	executeCount = listInvoicePrintComplet.size();
	    	log.info("invoicePrintComplet size ==> "+ listInvoicePrintComplet.size());
	    	
	    	for(InvoicePrintCompletData invoicePrintCompletData : listInvoicePrintComplet ) {

	    		//1건당 시간 체크용
	    		long apiRunTimeStartFor = System.currentTimeMillis();

    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendInvoicePrintCompletObject(invoicePrintCompletData);
	    			
	    			ResponseEntity<ResponseMesssage> res = (ResponseEntity<ResponseMesssage>)deferredResult.getResult();
	    			retStatus = (String)res.getBody().getStatus();
	    			retMessage = (String)res.getBody().getMessage();
	    			log.info(" >>>>>>invoicePrintComplet retStatus=>"+retStatus);
//	    			log.info(" >>>>>>"+retMessage);
//	    	    	log.info(" >>>>>>invoicePrintComplet deferredResult.getResult()="+ deferredResult.getResult());
	    	    	
//	    			if(deferredResult.getResult().toString().indexOf("SUCCESS") > -1) {
	    	    	if(retStatus.equals("SUCCESS")) {
	    				r_ifYn = KurlyConstants.STATUS_Y;
	    			} else {
	    				r_ifYn = KurlyConstants.STATUS_N;
	    			}
	    			
			    	//인터페이스 처리내역 update
	    			String r_invoiceNo = invoicePrintCompletData.getInvoiceNo();
	    			String r_warehouseKey = invoicePrintCompletData.getWarehouseKey();
	    			String r_shipOrderKey = invoicePrintCompletData.getShipOrderKey();
					Map<String, String> updateMap = new HashMap<String, String>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("invoicePrintIntfYn",KurlyConstants.STATUS_N);
						updateMap.put("invoicePrintIntfCode",KurlyConstants.STATUS_NG);
						updateMap.put("invoicePrintIntfMemo",retMessage.substring(0, 990));
					} else {
						updateMap.put("invoicePrintIntfYn",KurlyConstants.STATUS_Y);
						updateMap.put("invoicePrintIntfCode",KurlyConstants.STATUS_OK);
						updateMap.put("invoicePrintIntfMemo","");
					}
					updateMap.put("modifiedUser",KurlyConstants.DEFAULT_USERID);
					updateMap.put("invoiceNo",r_invoiceNo);
					updateMap.put("warehouseKey",r_warehouseKey);
					updateMap.put("shipOrderKey",r_shipOrderKey);
					
			    	invoicePrintCompletService.updateInvoicePrintComplet(updateMap);

			    	
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + invoicePrintCompletData.getInvoiceNo());  
	    			retMessage = ex.getMessage().substring(0, 90);
    				r_ifYn = KurlyConstants.STATUS_N;
	    		} finally {

	    			apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;

	            	log.info("================= apiRunTime(ms) : "+ apiRunTime);

					//전송로그 정보 insert
			    	LogApiStatus logApiStatus = new LogApiStatus();

			    	String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
				    logApiStatus.setApiYyyymmdd(sYyyymmdd);
			    	logApiStatus.setExecMethod(KurlyConstants.METHOD_INVOICEPRINTCOMPLET);
			    	
			    	logApiStatus.setGroupNo("");  //그룹배치번호
			    	logApiStatus.setWorkBatchNo("");  //작업배치번호

			    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
			    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
			    	logApiStatus.setShipUidSeq("");  //출고오더UID순번(WCS)
			    	logApiStatus.setShipOrderItemSeq("");  //출하문서순번(WMS)

			    	logApiStatus.setToteId("");  //토트ID번호
			    	logApiStatus.setInvoiceNo("");  //송장번호

			    	logApiStatus.setStatus("");  //상태
			    	
			    	logApiStatus.setQtyOrder(0);  //지시수량
			    	logApiStatus.setQtyComplete(0);  //작업완료수량
			    	
			    	logApiStatus.setSkuCode("");  //상품코드
			    	
			    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
			    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
			    	logApiStatus.setWcsStatus("");  //WCS 작업상태
			    	
			    	logApiStatus.setApiInfo("");
			    	
					if(invoicePrintCompletData != null) {
				    	if(invoicePrintCompletData.getWarehouseKey() ==null ||
								"".equals(invoicePrintCompletData.getWarehouseKey())) {
							logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
						} else {
							logApiStatus.setWarehouseKey(invoicePrintCompletData.getWarehouseKey());
						}
				    	
				    	logApiStatus.setShipUidWcs(invoicePrintCompletData.getShipUidKey());  //출고오더UID(WCS)
				    	logApiStatus.setShipOrderKey(invoicePrintCompletData.getShipOrderKey());  //출하문서번호(WMS)
				    	logApiStatus.setWcsStatus(invoicePrintCompletData.getInvoiceStatus());  //WCS 작업상태
				    	logApiStatus.setInvoiceNo(invoicePrintCompletData.getInvoiceNo());  //송장번호

				    	logApiStatus.setApiInfo(invoicePrintCompletData.toString());
					} else {
						logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);

				    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
				    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
				    	logApiStatus.setWcsStatus("");  //WCS 작업상태
				    	logApiStatus.setInvoiceNo("");  //송장번호
				    	
						logApiStatus.setApiInfo("");
					}
					
			    	logApiStatus.setApiUrl(KurlyConstants.METHOD_INVOICEPRINTCOMPLET);
			    	logApiStatus.setApiRuntime(apiRunTime);
			    	
			    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
			    	//전송상태 및 DB 오류 체크
			    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
			    		logApiStatus.setIntfMemo(retMessage);
			    	} else {
			    		logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
			    	}
			    	
			    	//로그정보 적재
			    	logApiStatusService.createLogApiStatus(logApiStatus);
			    	
	    		}
	    		executeCount++;
	    	}
    	
    	} catch (Exception e) {
    		result = "error";
			log.info( " === InvoicePrintCompletBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

			apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

        	log.info("================= apiRunTime(ms) : "+ apiRunTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_INVOICEPRINTCOMPLET);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_INVOICEPRINTCOMPLET+" Sucess("+apiRunTime+"ms)");	
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
    	log.info("=================InvoicePrintCompletBatch end===============");
    	
                                                                                                                                                                                                                                            }
}