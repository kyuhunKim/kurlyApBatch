package com.lgcns.wcs.kurly.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.dto.ReplayOptimizBatchData;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.service.ReplayOptimizBatchService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : ReplayOptimizBatch
 * @작성일 : 2021. 02. 24.
 * @작성자 : jooni
 * @변경이력 : 2021. 02. 24. 최초작성
 * @Method 설명 : 최적화 배치 다시 적용
 */
@Slf4j
@Component
public class ReplayOptimizBatch  {

    @Autowired
    ReplayOptimizBatchService replayOptimizBatchService;

	@Autowired
	LogApiStatusService logApiStatusService;
	
    @Autowired
    LogBatchExecService logBatchExecService;
    
    public void ReplayOptimizBatchTask()  {
    	log.info("=======ReplayOptimizBatchTask start=======");

    	long apiRunTimeStart = 0;
		long apiRunTimeEnd   = 0;
		String apiRunTime    = "";
		
		apiRunTimeStart = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();

    	List<LogApiStatus> logApiStatusList = new ArrayList<LogApiStatus>();
    	
    	try {
	    	
    		List<ReplayOptimizBatchData> selectList = replayOptimizBatchService.selectReplayOptimize();
	    	
    		log.info(">>>selectList : " + selectList.size());
    		
    		if( selectList.size() > 0 ) {
	
	    		for(ReplayOptimizBatchData optimizBatchData : selectList)
	    		{
		    		long apiRunTimeStartFor = System.currentTimeMillis();
		    		
	    			
		    		//MAKE : 피킹 배치 생성시, CANCEL : 피킹 배치 취소시 (피킹 취소 이후 재생성)
					String batchTypeStatus = optimizBatchData.getBatchTypeStatus();
					
					String insertedUser = optimizBatchData.getInsertedUser() ;

					if(StringUtil.isEmpty(insertedUser) ) {
						insertedUser = KurlyConstants.DEFAULT_USERID;
					}
					
					optimizBatchData.setInsertedUser(insertedUser);
					
					//MAKE : 피킹 배치 생성시, CANCEL : 피킹 배치 취소시 (피킹 취소 이후 재생성)
					if("MAKE".equals(batchTypeStatus)) {
						
						String shipOrderKey = optimizBatchData.getShipOrderKey() ;
						String shipOrderItemSeq = optimizBatchData.getShipOrderItemSeq() ;
						String warehouseKey = optimizBatchData.getWarehouseKey() ;
						String docDate = optimizBatchData.getDocDate() ;
						String itemStatus = optimizBatchData.getItemStatus() ;
						String skuCode = optimizBatchData.getSkuCode() ;
						String skuName = optimizBatchData.getSkuName() ;
						String skuSubName = optimizBatchData.getSkuSubName() ;
						String skuAlterCode = optimizBatchData.getSkuAlterCode() ;
						String allocType = optimizBatchData.getAllocType() ;
						String groupNo = optimizBatchData.getGroupNo() ;
						String workBatchNo = optimizBatchData.getWorkBatchNo() ;
						String pickingWaveNo = optimizBatchData.getPickingWaveNo() ;
						String qpsNum = optimizBatchData.getQpsNum() ;
						String dasCellGroupId = optimizBatchData.getDasCellGroupId() ;
						String allocSeq = optimizBatchData.getAllocSeq() ;
						String waveBatchCnclDate = optimizBatchData.getWaveBatchCnclDate() ;
						String waveBatchCnclTime = optimizBatchData.getWaveBatchCnclTime() ;
						String waveBatchCnclUser = optimizBatchData.getWaveBatchCnclUser() ;

						if(shipOrderKey != null && !shipOrderKey.equals("")
								&& shipOrderItemSeq != null && !shipOrderItemSeq.equals("") )  {
							
							if(StringUtil.isEmpty(warehouseKey) ) {
								optimizBatchData.setWarehouseKey("") ;
							}
							if(StringUtil.isEmpty(docDate) ) {
								optimizBatchData.setDocDate(" ") ;
							}
							if(StringUtil.isEmpty(itemStatus) ) {
								optimizBatchData.setItemStatus(" ") ;
							}
							if(StringUtil.isEmpty(skuCode) ) {
								optimizBatchData.setSkuCode(" ") ;
							}
							if(StringUtil.isEmpty(skuName) ) {
								optimizBatchData.setSkuName(" ") ;
							}
							if(StringUtil.isEmpty(skuSubName) ) {
								optimizBatchData.setSkuSubName(" ") ;
							}
							if(StringUtil.isEmpty(skuAlterCode) ) {
								optimizBatchData.setSkuAlterCode(" ") ;
							}
							if(StringUtil.isEmpty(allocType) ) {
								optimizBatchData.setAllocType("") ;
							}
							if(StringUtil.isEmpty(groupNo) ) {
								optimizBatchData.setGroupNo("") ;
							}
							if(StringUtil.isEmpty(workBatchNo) ) {
								optimizBatchData.setWorkBatchNo("") ;
							}
							if(StringUtil.isEmpty(pickingWaveNo) ) {
								optimizBatchData.setPickingWaveNo("") ;
							}
							if(StringUtil.isEmpty(qpsNum) ) {
								optimizBatchData.setQpsNum("") ;
							}
							if(StringUtil.isEmpty(dasCellGroupId) ) {
								optimizBatchData.setDasCellGroupId("") ;
							}
							if(StringUtil.isEmpty(allocSeq) ) {
								optimizBatchData.setAllocSeq("") ;
							}
							//20201117 allocType:S일 경우 QpsNum "" 처리 
							if("S".equals(allocType)) {
								optimizBatchData.setQpsNum("") ;
							}
							//2021.01.14 allocType:QDAS일 경우 QpsNum "" 처리
							if("QDAS".equals(allocType)) {
								optimizBatchData.setQpsNum("") ;
							}

							if(StringUtil.isEmpty(waveBatchCnclDate) ) {
								optimizBatchData.setWaveBatchCnclDate("") ;
							}
							if(StringUtil.isEmpty(waveBatchCnclTime) ) {
								optimizBatchData.setWaveBatchCnclTime("") ;
							}
							if(StringUtil.isEmpty(waveBatchCnclUser) ) {
								optimizBatchData.setWaveBatchCnclUser("") ;
							}
							
							// 20201127 TB_ORD_SHIPMENT_DTL 테이블에 update 
							// pickingWaveNo 는 인터페이스 항목에서 제외되고  pickingWaveNo에 workBatchNo값을 넣어준다
							//group_no update
							replayOptimizBatchService.updateOptimizBatchMake(optimizBatchData);

							//allocType update
							replayOptimizBatchService.updateOrdShipmentHdr(optimizBatchData);
							

						} else {
							result = "shipOrderKey is Null error MAKE";
						}
					} 
					else if("CANCEL".equals(batchTypeStatus)) {

						String shipOrderKey = optimizBatchData.getShipOrderKey() ;
						String shipOrderItemSeq = optimizBatchData.getShipOrderItemSeq() ;
						String waveBatchCnclUser = optimizBatchData.getWaveBatchCnclUser() ;

						String warehouseKey = optimizBatchData.getWarehouseKey() ;
						String docDate = optimizBatchData.getDocDate() ;
						String itemStatus = optimizBatchData.getItemStatus() ;
						String skuCode = optimizBatchData.getSkuCode() ;
						String skuName = optimizBatchData.getSkuName() ;
						String skuSubName = optimizBatchData.getSkuSubName() ;
						String skuAlterCode = optimizBatchData.getSkuAlterCode() ;
						String allocType = optimizBatchData.getAllocType() ;
						String groupNo = optimizBatchData.getGroupNo() ;
						String workBatchNo = optimizBatchData.getWorkBatchNo() ;
						String pickingWaveNo = optimizBatchData.getPickingWaveNo() ;
						String qpsNum = optimizBatchData.getQpsNum() ;
						String dasCellGroupId = optimizBatchData.getDasCellGroupId() ;
						String allocSeq = optimizBatchData.getAllocSeq() ;
						String waveBatchCnclDate = optimizBatchData.getWaveBatchCnclDate() ;
						String waveBatchCnclTime = optimizBatchData.getWaveBatchCnclTime() ;

						if(shipOrderKey != null && !shipOrderKey.equals("")
								&& shipOrderItemSeq != null && !shipOrderItemSeq.equals("") )  {
							
							if(StringUtil.isEmpty(warehouseKey) ) {
								optimizBatchData.setWarehouseKey("") ;
							}
							if(StringUtil.isEmpty(docDate) ) {
								optimizBatchData.setDocDate(" ") ;
							}
							if(StringUtil.isEmpty(itemStatus) ) {
								optimizBatchData.setItemStatus(" ") ;
							}
							if(StringUtil.isEmpty(skuCode) ) {
								optimizBatchData.setSkuCode(" ") ;
							}
							if(StringUtil.isEmpty(skuName) ) {
								optimizBatchData.setSkuName(" ") ;
							}
							if(StringUtil.isEmpty(skuSubName) ) {
								optimizBatchData.setSkuSubName(" ") ;
							}
							if(StringUtil.isEmpty(skuAlterCode) ) {
								optimizBatchData.setSkuAlterCode(" ") ;
							}
							if(StringUtil.isEmpty(allocType) ) {
								optimizBatchData.setAllocType("") ;
							}
							if(StringUtil.isEmpty(groupNo) ) {
								optimizBatchData.setGroupNo("") ;
							}
							if(StringUtil.isEmpty(workBatchNo) ) {
								optimizBatchData.setWorkBatchNo("") ;
							}
							if(StringUtil.isEmpty(pickingWaveNo) ) {
								optimizBatchData.setPickingWaveNo("") ;
							}
							if(StringUtil.isEmpty(qpsNum) ) {
								optimizBatchData.setQpsNum("") ;
							}
							if(StringUtil.isEmpty(dasCellGroupId) ) {
								optimizBatchData.setDasCellGroupId("") ;
							}
							if(StringUtil.isEmpty(allocSeq) ) {
								optimizBatchData.setAllocSeq("") ;
							}
							//20201117 allocType:S일 경우 QpsNum "" 처리 
							if("S".equals(allocType)) {
								optimizBatchData.setQpsNum("") ;
							}
							//2021.01.14 allocType:QDAS일 경우 QpsNum "" 처리
							if("QDAS".equals(allocType)) {
								optimizBatchData.setQpsNum("") ;
							}
							
							if(StringUtil.isEmpty(waveBatchCnclUser) ) {
								waveBatchCnclUser = KurlyConstants.DEFAULT_USERID;
							}

							if(StringUtil.isEmpty(waveBatchCnclDate) ) {
								optimizBatchData.setWaveBatchCnclDate("") ;
							}
							if(StringUtil.isEmpty(waveBatchCnclTime) ) {
								optimizBatchData.setWaveBatchCnclTime("") ;
							}
							if(StringUtil.isEmpty(waveBatchCnclUser) ) {
								optimizBatchData.setWaveBatchCnclUser("") ;
							}
							
							optimizBatchData.setInsertedUser(waveBatchCnclUser);
							optimizBatchData.setWaveBatchCnclUser(waveBatchCnclUser);

							replayOptimizBatchService.updateOptimizBatchCancel(optimizBatchData);

							
						} else {
							result = "shipOrderKey is Null error CANCEL";
						}
						
					}  
					else {
						result = "error";
					}

					apiRunTimeEnd = System.currentTimeMillis();
	    			apiRunTime = StringUtil.formatInterval(apiRunTimeStartFor, apiRunTimeEnd) ;
	    			
	    			Map<String, Object> updateMap = new HashMap<String, Object>();
	    			
					updateMap.put("apiRunTime",apiRunTime);
					if("error".equals(result)) {
						updateMap.put("resultYn",KurlyConstants.STATUS_N);
						updateMap.put("resultMessage",KurlyConstants.STATUS_NG);
					} else {
						updateMap.put("resultYn",KurlyConstants.STATUS_Y);
						updateMap.put("resultMessage",KurlyConstants.STATUS_OK);
					}
					
	    	    	//로그 저장  수집
	    	    	LogApiStatus logApiStatus = new LogApiStatus();
	    	    	logApiStatus = logApiStatusVo(updateMap, optimizBatchData);
					
	    	    	logApiStatusList.add(logApiStatus);
	    	    	
    	    		executeCount++;
    			
	    		}

	    		try 
	        	{
	
	    			List<LogApiStatus> u_logApiStatusList = new ArrayList<LogApiStatus>();
	
		        	for(int k=0; k <logApiStatusList.size(); k++) {
			
			        	u_logApiStatusList.add(logApiStatusList.get(k));
			        			
			        	//100 건 씩 처리
			    	    if( (k>2 && k%100 == 0 ) 
			    	    		|| ( k == logApiStatusList.size()-1 ) ) {
			
							if(u_logApiStatusList.size() > 0) {
		    			    	
		    					//update
								logApiStatusService.createLogApiStatusList(u_logApiStatusList);
								
							}
	    					u_logApiStatusList = new ArrayList<LogApiStatus>();
	    					
	    	    		}
	        		}
	        		
	        	} catch (Exception e1) {
	        		result = "error";
	    			log.error( " === ReplayOptimizBatch update error " +e1 );
	    			resultMessage = e1.toString();
	        	}
    		}
    	} catch (Exception e) {
    		result = "error";
			log.error( " === ReplayOptimizBatch  error" +e );
			resultMessage = e.toString();
//			throw new Exception(e);
    	} finally {

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod("replayOptimizBatch");
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog("replayOptimizBatch Sucess("+apiRunTime+"ms)");
        	} else {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
            	logBatchExec.setMessageLog(resultMessage);
        	}
        	logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
        	logBatchExec.setExecuteCount(executeCount);
        	logBatchExec.setStartDate(startDate);
        	
        	logBatchExecService.createLogBatchExec(logBatchExec);

    	}
    	
    	log.info("=======ReplayOptimizBatch end=======");
    	
    }

    public LogApiStatus logApiStatusVo(Map<String, Object> updateMap, ReplayOptimizBatchData optimizBatchData) {
    	//로그 정보 적재
    	LogApiStatus logApiStatus = new LogApiStatus();
    	
		logApiStatus.setExecMethod(KurlyConstants.METHOD_REPLAY_OPTIMIZBATCH);
    	
    	logApiStatus.setShipUidWcs("");  //출고오더UID(WCS)
    	logApiStatus.setShipUidSeq("");  //출고오더UID순번(WCS)
    	logApiStatus.setShipOrderItemSeq("");  //출하문서순번(WMS)

    	logApiStatus.setToteId(" ");  //토트ID번호
    	logApiStatus.setInvoiceNo("");  //송장번호
    	
    	logApiStatus.setStatus("");  //상태
    	
    	logApiStatus.setQtyOrder(0);  //지시수량
    	logApiStatus.setQtyComplete(0);  //작업완료수량
    	
    	logApiStatus.setWcsStatus("");  //WCS 작업상태

		logApiStatus.setApiInfo("");
    	logApiStatus.setSkuCode("");  //상품코드
    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
    	logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
    	logApiStatus.setGroupNo("");  //그룹배치번호
    	logApiStatus.setWorkBatchNo("");  //작업배치번호

        String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
    	logApiStatus.setApiYyyymmdd(sYyyymmdd);
    	
    	if(optimizBatchData != null) {

	    	logApiStatus.setSkuCode(optimizBatchData.getSkuCode());  //상품코드
	    	logApiStatus.setShipOrderKey(optimizBatchData.getShipOrderKey());  //출하문서번호(WMS)
	    	logApiStatus.setWarehouseKey(optimizBatchData.getWarehouseKey());
	    	logApiStatus.setGroupNo(optimizBatchData.getGroupNo());  //그룹배치번호
	    	logApiStatus.setWorkBatchNo(optimizBatchData.getWorkBatchNo());  //작업배치번호
	    	logApiStatus.setShipOrderItemSeq(optimizBatchData.getShipOrderItemSeq());  //출하문서순번(WMS)
	    	logApiStatus.setQtyOrder(optimizBatchData.getQtyShipOrder());  //지시수량
	    	
	    	logApiStatus.setStatus(optimizBatchData.getBatchTypeStatus());  //상태
//	    	logApiStatus.setApiInfo(optimizBatchData.toString());
	    	
			//##20210106  json 타입으로 저장 
			try {
				ObjectMapper mapper = new ObjectMapper();
				String jsonStr = mapper.writeValueAsString(optimizBatchData);

				logApiStatus.setApiInfo(jsonStr);
			} catch (IOException e) {
	            e.printStackTrace();
				logApiStatus.setApiInfo(optimizBatchData.toString());
	        }
		} else {

			logApiStatus.setApiInfo("");
	    	logApiStatus.setSkuCode("");  //상품코드
	    	logApiStatus.setShipOrderKey("");  //출하문서번호(WMS)
	    	logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
	    	logApiStatus.setGroupNo("");  //그룹배치번호
	    	logApiStatus.setWorkBatchNo("");  //작업배치번호
	    	logApiStatus.setStatus("");  //상태
		}
		
		
		String l_apiRunTime = updateMap.get("apiRunTime").toString();
		String l_intfYn = updateMap.get("resultYn").toString();
		String l_intfMemo = updateMap.get("resultMessage").toString();

		logApiStatus.setApiUrl(KurlyConstants.METHOD_REPLAY_OPTIMIZBATCH);
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
