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
import com.lgcns.wcs.kurly.dto.ToteCellExceptTxnData;
import com.lgcns.wcs.kurly.dto.ToteCellExceptTxnSelectData;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.ToteCellExceptTxnService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : ToteCellExceptTxnBatch
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * 			2020. 11. 12. RunTime 로직 수정
 * @Method 설명 : 토트 문제 처리용 피킹정보 연계  (WCS => WMS)
 */
@Slf4j
@Component
public class ToteCellExceptTxnBatch {

    @Autowired
    KurlyWcsToWmsProducer wcsProducer;
	
    @Autowired
    ToteCellExceptTxnService toteCellExceptTxnService;
    
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;
        

    public void ToteCellExceptTxnTask()  {
    	
    	log.info("=================ToteCellExceptTxnBatch start===============");
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
        	
    		//토트 문제 처리용 피킹정보 데이타 조회
    		List<ToteCellExceptTxnSelectData> listToteCellExceptTxnSelect = toteCellExceptTxnService.selectToteCellExceptTxn();
	    	
	    	//조회 건수 
//	    	executeCount = listToteCellExceptTxnSelect.size();
	    	log.info("toteCellExceptTxn size ==> "+ listToteCellExceptTxnSelect.size());
	    	
	    	for(ToteCellExceptTxnSelectData toteCellExceptTxnSelectData : listToteCellExceptTxnSelect ) {

	    		//건당 시간 체크용
	    		long apiRunTimeStartFor = System.currentTimeMillis();
	        	
    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			
	    			log.info("==toteId=="+toteCellExceptTxnSelectData.getToteId());
	    			ToteCellExceptTxnData toteCellExceptTxnData = new ToteCellExceptTxnData();
	    			
	    			toteCellExceptTxnData.setToteId(toteCellExceptTxnSelectData.getToteId());            
	    			toteCellExceptTxnData.setToteIdSeq(toteCellExceptTxnSelectData.getToteIdSeq());
	    			toteCellExceptTxnData.setWarehouseKey(toteCellExceptTxnSelectData.getWarehouseKey());
	    			toteCellExceptTxnData.setWmsPickingDate(toteCellExceptTxnSelectData.getWmsPickingDate());
	    			toteCellExceptTxnData.setPickingType(toteCellExceptTxnSelectData.getPickingType());
	    			toteCellExceptTxnData.setAllocType(toteCellExceptTxnSelectData.getAllocType());
	    			toteCellExceptTxnData.setExceptTxnType(toteCellExceptTxnSelectData.getExceptTxnType());
	    			toteCellExceptTxnData.setGroupNo(toteCellExceptTxnSelectData.getGroupNo());
	    			toteCellExceptTxnData.setWorkBatchNo(toteCellExceptTxnSelectData.getWorkBatchNo());
	    			toteCellExceptTxnData.setSkuCode(toteCellExceptTxnSelectData.getSkuCode());
	    			toteCellExceptTxnData.setSkuName(toteCellExceptTxnSelectData.getSkuName());
	    			toteCellExceptTxnData.setSkuSubName(toteCellExceptTxnSelectData.getSkuSubName());
	    			toteCellExceptTxnData.setQtyQpsExcept(toteCellExceptTxnSelectData.getQtyQpsExcept());
	    			toteCellExceptTxnData.setToSkuCode(toteCellExceptTxnSelectData.getToSkuCode());
	    			toteCellExceptTxnData.setToSkuName(toteCellExceptTxnSelectData.getToSkuName());
	    			toteCellExceptTxnData.setToSkuSubName(toteCellExceptTxnSelectData.getToSkuSubName());
	    			toteCellExceptTxnData.setFromSkuCode(toteCellExceptTxnSelectData.getFromSkuCode());
	    			toteCellExceptTxnData.setFromSkuName(toteCellExceptTxnSelectData.getFromSkuName());
	    			toteCellExceptTxnData.setFromSkuSubName(toteCellExceptTxnSelectData.getFromSkuSubName());
	    			toteCellExceptTxnData.setPickBatchGroup(toteCellExceptTxnSelectData.getPickBatchGroup());
	    			toteCellExceptTxnData.setInsertedDate(toteCellExceptTxnSelectData.getInsertedDate());
	    			toteCellExceptTxnData.setInsertedTime(toteCellExceptTxnSelectData.getInsertedTime());
	    			toteCellExceptTxnData.setInsertedUser(toteCellExceptTxnSelectData.getInsertedUser());
	    			
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendToteCellExceptTxnObject(toteCellExceptTxnData);
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
	    			
	    			log.info("=================updateToteCellExceptTxn===============1");
			    	//인터페이스 처리내역 update
	    			String r_toteId = toteCellExceptTxnSelectData.getToteId();
	    			String r_toteIdSeq = toteCellExceptTxnSelectData.getToteIdSeq();
	    			int r_cellExceptSeq = toteCellExceptTxnSelectData.getCellExceptSeq();
					Map<String, String> updateMap = new HashMap<String, String>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("exceptIfYn",KurlyConstants.STATUS_N);
						updateMap.put("exceptIfRetCode",KurlyConstants.STATUS_NG);
						updateMap.put("exceptIfRetMessage",retMessage);
					} else {
						updateMap.put("exceptIfYn",KurlyConstants.STATUS_Y);
						updateMap.put("exceptIfRetCode",KurlyConstants.STATUS_OK);
						updateMap.put("exceptIfRetMessage","");
					}
					updateMap.put("modifiedUser",KurlyConstants.DEFAULT_USERID);
					updateMap.put("toteId",r_toteId);
					updateMap.put("toteIdSeq",r_toteIdSeq);
					updateMap.put("cellExceptSeq",r_cellExceptSeq+"");

			    	toteCellExceptTxnService.updateToteCellExceptTxn(updateMap);

			    	log.info("=================updateToteCellExceptTxn===============2");
			    	
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + toteCellExceptTxnSelectData.getToteId());  
	    			retMessage = ex.getMessage().substring(0, 90);
    				r_ifYn = KurlyConstants.STATUS_N;
	    		} finally {
	    			log.info("====finally createLogApiStatus===============1");
	    			
					//전송로그 정보 insert
			    	LogApiStatus logApiStatus = new LogApiStatus();

			    	if(toteCellExceptTxnSelectData.getWarehouseKey() ==null ||
							"".equals(toteCellExceptTxnSelectData.getWarehouseKey())) {
						logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
					}

			    	logApiStatus.setExecMethod(KurlyConstants.METHOD_TOTECELLEXCEPTTXN);
			    	
			    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
			    	logApiStatus.setShipOrderItemSeq("");  //출하문서순번(WMS)

			    	logApiStatus.setInvoiceNo("");  //송장번호

			    	logApiStatus.setStatus("");  //상태
			    	
			    	logApiStatus.setQtyOrder(0);  //지시수량
			    	logApiStatus.setWcsStatus("");  //WCS 작업상태
			    	
			    	logApiStatus.setApiUrl(KurlyConstants.METHOD_TOTECELLEXCEPTTXN);

			    	String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
				    logApiStatus.setApiYyyymmdd(sYyyymmdd);
				    
					if(toteCellExceptTxnSelectData != null) {
				    	logApiStatus.setToteId(toteCellExceptTxnSelectData.getToteId());  //토트ID번호
				    	logApiStatus.setGroupNo(toteCellExceptTxnSelectData.getGroupNo());  //그룹배치번호
				    	logApiStatus.setWorkBatchNo(toteCellExceptTxnSelectData.getWorkBatchNo());  //작업배치번호
				    	
				    	logApiStatus.setShipUidWcs(toteCellExceptTxnSelectData.getShipUidKey());  //출고오더UID(WCS)
				    	logApiStatus.setShipUidSeq(toteCellExceptTxnSelectData.getShipUidItemSeq());  //출고오더UID순번(WCS)

				    	logApiStatus.setQtyComplete(toteCellExceptTxnSelectData.getQtyQpsExcept());  //작업완료수량
				    	
				    	logApiStatus.setSkuCode(toteCellExceptTxnSelectData.getSkuCode());  //상품코드
				    	logApiStatus.setApiInfo(toteCellExceptTxnSelectData.toString());
					} else {
						logApiStatus.setToteId("");  //토트ID번호
				    	logApiStatus.setGroupNo("");  //그룹배치번호
				    	logApiStatus.setWorkBatchNo("");  //작업배치번호
				    	
				    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
				    	logApiStatus.setShipUidSeq("");  //출고오더UID순번(WCS)

				    	logApiStatus.setQtyComplete(0);  //작업완료수량
				    	
				    	logApiStatus.setSkuCode("");  //상품코드
				    	logApiStatus.setApiInfo("");
					}
					
			    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
			    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
			    		logApiStatus.setIntfMemo(retMessage);
			    	} else {
			    		logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
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
			log.info( " === ToteCellExceptTxnBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

			apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;
	    	
        	log.info("================= apiRunTime(ms) : "+ apiRunTime);
        	

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_TOTECELLEXCEPTTXN);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_TOTECELLEXCEPTTXN +" Sucess("+apiRunTime+"ms)");
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
    	log.info("=================ToteCellExceptTxnBatch end=============== ");    	

    }
}

