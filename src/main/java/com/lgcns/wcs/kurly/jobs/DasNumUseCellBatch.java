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

import com.lgcns.wcs.kurly.dto.DasNumUseCellData;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.dto.ResponseMesssage;
import com.lgcns.wcs.kurly.producer.KurlyWcsToWmsProducer;
import com.lgcns.wcs.kurly.service.DasNumUseCellService;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : DasNumUseCellBatch
 * @작성일 : 2020. 11. 24.
 * @작성자 : jooni
 * @변경이력 : 2020. 11. 24. 최초작성
 * @Method 설명 : DAS 셀그룹번호(DAS출고시 활용)별 가용셀 정보
 */
@Slf4j
@Component
public class DasNumUseCellBatch  {

	@Autowired
    KurlyWcsToWmsProducer wcsProducer;
	
    @Autowired
    DasNumUseCellService dasNumUseCellService;
    
    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;

    public void DasNumUseCellTask()  {
    	log.info("=================DasNumUseCellBatch start===============");
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
    		List<DasNumUseCellData> dasNumUseCellList = dasNumUseCellService.selectDasNumUseCellList();
	    	
        	log.info("dasNumUseCellList size ==> "+ dasNumUseCellList.size());
        	
        	for(DasNumUseCellData dasNumUseCellData : dasNumUseCellList ) {
        		//건당 시간 체크용
	    		long apiRunTimeStartFor = System.currentTimeMillis();

    			String r_ifYn = KurlyConstants.STATUS_N;
    			DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

    			String retStatus = "";
    			String retMessage = "";
	    		try {
	    			
	    			// 셀 그룹ID가 삭제인 경우에는 이전에 생성된 인터페이스 데이터가 있는지 확인해서
	    			// 정상적으로 연계된 경우만 전송함   
	    			if("DELETE".equals(dasNumUseCellData.getCellTypeStatus())) {

		    			Map<String, String> countMap = new HashMap<String, String>();
		    			countMap.put("warehouseKey",dasNumUseCellData.getWarehouseKey());
		    			countMap.put("dasCellGroupId",dasNumUseCellData.getDasCellGroupId());
						
	    				int makeCount = dasNumUseCellService.selectDasNumUseCellCount(countMap);
	    				
	    				//인터페이스된 데이터가 존재하는 경우만 진행
	    				if(makeCount < 1) {
		    				r_ifYn = KurlyConstants.STATUS_N;
			    			retMessage = "make interface does not exist";
	    					continue;
	    				}
	    			}
	    			
	    			//kafka 전송
	    			deferredResult = wcsProducer.sendDasNumUseCellObject(dasNumUseCellData);	
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
	    			
	    		} catch (Exception ex) {	
	    			log.info("== send error == " + dasNumUseCellData.getWarehouseKey());  
	    			retMessage = ex.getMessage().substring(0, 90);
	    			ex.printStackTrace();
    				r_ifYn = KurlyConstants.STATUS_N;
	    		} finally {
	    			
	    			Map<String, String> updateMap = new HashMap<String, String>();
					
					if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
						updateMap.put("useCellIntfYn",KurlyConstants.STATUS_N);
						updateMap.put("useCellIntfCode",KurlyConstants.STATUS_NG);
						updateMap.put("useCellIntfMemo",retMessage);
					} else {
						updateMap.put("useCellIntfYn",KurlyConstants.STATUS_Y);
						updateMap.put("useCellIntfCode",KurlyConstants.STATUS_OK);
						updateMap.put("useCellIntfMemo","");
					}
					updateMap.put("warehouseKey",dasNumUseCellData.getWarehouseKey());
					updateMap.put("dasCellGroupId",dasNumUseCellData.getDasCellGroupId());
					updateMap.put("cellTypeStatus",dasNumUseCellData.getCellTypeStatus());

					dasNumUseCellService.updateDasNumUseCell(updateMap);
			    	
//	    			log.info("====finally createLogApiStatus===============");

	    			apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;

					//로그 정보 insert
			    	LogApiStatus logApiStatus = new LogApiStatus();

			    	String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
				    logApiStatus.setApiYyyymmdd(sYyyymmdd);
			    	logApiStatus.setExecMethod(KurlyConstants.METHOD_DASNUMUSECELL);
			    	
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
			    	logApiStatus.setApiInfo("");

					if(dasNumUseCellData != null) {
				    	if(dasNumUseCellData.getWarehouseKey() ==null ||
								"".equals(dasNumUseCellData.getWarehouseKey())) {
							logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
						} else {
							logApiStatus.setWarehouseKey(dasNumUseCellData.getWarehouseKey());
						}
				    	logApiStatus.setApiInfo(dasNumUseCellData.toString());
					} else {
						logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
						logApiStatus.setApiInfo("");
					}
					
			    	logApiStatus.setApiUrl(KurlyConstants.METHOD_DASNUMUSECELL);
			    	logApiStatus.setApiRuntime(apiRunTime);
			    	
			    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
			    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
			    		logApiStatus.setIntfMemo(retMessage);
			    	} else {
			    		logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
			    	}
			    	
			    	logApiStatusService.createLogApiStatus(logApiStatus);
//	    			log.info("====finally createLogApiStatus===============");
			    	
	    		}
	    		executeCount++;
        	}	
        	
    	} catch (Exception e) {
    		result = "error";
			log.error( " === DasNumUseCellBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

			log.info("================= apiRunTime(ms) : "+ apiRunTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_DASNUMUSECELL);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_DASNUMUSECELL +" Sucess("+apiRunTime+"ms)");	
        	} else {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
            	logBatchExec.setMessageLog(resultMessage);
        	}
        	logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
        	logBatchExec.setExecuteCount(executeCount);
        	logBatchExec.setStartDate(startDate);
        	
        	logBatchExecService.createLogBatchExec(logBatchExec);
    	}
    	
    	log.info("=================DasNumUseCellBatch end===============");
    	
    }

}

