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
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplayData;
import com.lgcns.wcs.kurly.dto.ResponseMesssage;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.OrdmadeNotfullyReplayService;
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
    	log.info("=================OrdmadeNotfullyReplayBatch start===============");
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
    		//WCS 미출오더 상품보충용 추가피킹정보  데이타 조회
    		List<OrdmadeNotfullyReplayData> listOrdmadeNotfullyReplay = ordmadeNotfullyReplayService.selectOrdmadeNotfullyReplay();

	    	//조회 건수 
//	    	executeCount = listOrdmadeNotfullyReplay.size();
	    	log.info("listOrdmadeNotfullyReplay size ==> "+ listOrdmadeNotfullyReplay.size());
	    	
	    	for(OrdmadeNotfullyReplayData ordmadeNotfullyReplayData : listOrdmadeNotfullyReplay ) {
	    		//건당 시간 체크용
	    		long apiRunTimeStartFor = System.currentTimeMillis();

    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendOrdmadeNotfullyReplayObject(ordmadeNotfullyReplayData);
	    			
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
	    			
	    			log.info("=================updateOrdmadeNotfullyReplay===============1");
			    	//인터페이스 처리내역 update
	    			String r_invoiceNo = ordmadeNotfullyReplayData.getInvoiceNo();
	    			String r_invoiceSeq = ordmadeNotfullyReplayData.getInvoiceSeq();
	    			double r_qtyNotfullyReqpick = ordmadeNotfullyReplayData.getQtyNotfullyReqpick();
	    			
					Map<String, String> updateMap = new HashMap<String, String>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("notfullyReqIfYn",KurlyConstants.STATUS_N);
						updateMap.put("notfullyReqIfRetCode",KurlyConstants.STATUS_NG);
						updateMap.put("notfullyReqIfRetMessage",retMessage);
					} else {
						updateMap.put("notfullyReqIfYn",KurlyConstants.STATUS_Y);
						updateMap.put("notfullyReqIfRetCode",KurlyConstants.STATUS_OK);
						updateMap.put("notfullyReqIfRetMessage","");
					}
					updateMap.put("modifiedUser",KurlyConstants.DEFAULT_USERID);
					updateMap.put("invoiceNo",r_invoiceNo);
					updateMap.put("qtyNotfullyReqpick",""+r_qtyNotfullyReqpick);
					updateMap.put("invoiceSeq",r_invoiceSeq);

					ordmadeNotfullyReplayService.updateOrdmadeNotfullyReplay(updateMap);

			    	log.info("=================updateOrdmadeNotfullyReplay===============2");
			    	
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + ordmadeNotfullyReplayData.getInvoiceNo());  
	    			retMessage = ex.getMessage().substring(0, 90);
    				r_ifYn = KurlyConstants.STATUS_N;
	    		} finally {
	    			log.info("====finally createLogApiStatus===============1");

	    			apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;

					//전송로그 정보 insert
			    	LogApiStatus logApiStatus = new LogApiStatus();

					if(ordmadeNotfullyReplayData.getWarehouseKey() ==null ||
							"".equals(ordmadeNotfullyReplayData.getWarehouseKey())) {
						logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
					}
					
			    	logApiStatus.setApiYyyymmdd(ordmadeNotfullyReplayData.getInsertedDate());
			    	logApiStatus.setExecMethod(KurlyConstants.METHOD_ORDMADENOTFULLYREPLAY);
			    	
			    	logApiStatus.setGroupNo(ordmadeNotfullyReplayData.getGroupNo());  //그룹배치번호
			    	logApiStatus.setWorkBatchNo(ordmadeNotfullyReplayData.getWorkBatchNo());  //작업배치번호
			    	
			    	logApiStatus.setShipUidWcs(ordmadeNotfullyReplayData.getShipUidWcs());  //출고오더UID(WCS)
			    	logApiStatus.setShipUidSeq(ordmadeNotfullyReplayData.getShipUidSeq());  //출고오더UID순번(WCS)
			    	logApiStatus.setShipOrderKey(ordmadeNotfullyReplayData.getShipOrderKey());  //출하문서번호(WMS)
			    	logApiStatus.setShipOrderItemSeq(ordmadeNotfullyReplayData.getShipOrderItemSeq());  //출하문서순번(WMS)

			    	logApiStatus.setToteId(" ");  //토트ID번호
			    	logApiStatus.setInvoiceNo(ordmadeNotfullyReplayData.getInvoiceNo());  //송장번호
			    	
			    	logApiStatus.setStatus("");  //상태
			    	
			    	logApiStatus.setQtyOrder(ordmadeNotfullyReplayData.getQtyNotfullyReqpick());  //지시수량
			    	logApiStatus.setQtyComplete(0);  //작업완료수량
			    	
			    	logApiStatus.setSkuCode(ordmadeNotfullyReplayData.getSkuCode());  //상품코드
			    	logApiStatus.setWcsStatus("");  //WCS 작업상태

			    	logApiStatus.setApiUrl(KurlyConstants.METHOD_ORDMADENOTFULLYREPLAY);
			    	logApiStatus.setApiInfo(ordmadeNotfullyReplayData.toString());
			    	logApiStatus.setApiRuntime(apiRunTime);

			    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
			    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
			    		logApiStatus.setIntfMemo(retMessage);
			    	} else {
			    		logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
			    	}
			    	
			    	//로그정보 적재
			    	logApiStatusService.createLogApiStatus(logApiStatus);
	    			log.info("====finally createLogApiStatus===============2");
			    	
	    		}
	    		executeCount++;
	    	}
    	} catch (Exception e) {
    		result = "error";
			log.info( " === OrdmadeNotfullyReplayBatch  error" +e );
			resultMessage = e.toString();
    	} finally {

			apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

        	log.info("================= apiRunTime(ms) : "+ apiRunTime);
        	
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
	    	
        	log.info("=================createLogBatchExec end=============== ");  
        }
    	log.info("=================OrdmadeNotfullyReplayBatch end===============");
    	
    }
}