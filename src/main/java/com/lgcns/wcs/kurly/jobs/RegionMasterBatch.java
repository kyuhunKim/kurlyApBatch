package com.lgcns.wcs.kurly.jobs;

import java.io.IOException;
import java.time.LocalDateTime;
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
import com.lgcns.wcs.kurly.dto.RegionMasterData;
import com.lgcns.wcs.kurly.dto.RegionMasterDetailData;
import com.lgcns.wcs.kurly.dto.RegionMasterHeaderData;
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
    	log.info("=======RegionMasterBatch start=======");
    	log.info("The current date  : " + LocalDateTime.now());
    	long apiRunTimeStart = 0;
		long apiRunTimeEnd   = 0;
		String apiRunTime    = "";
		
		apiRunTimeStart = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();
		List<RegionMasterData> updateMapList = new ArrayList<RegionMasterData>();

		String r_ifYn = KurlyConstants.STATUS_Y;
		
    	try {
    		
    		RegionMasterHeaderData reqData = regionMasterService.insertRegionMaster();
    					
    		if(reqData.getError_code() == 0 ) {

    	    	for(RegionMasterDetailData master : reqData.getData() ) {
    	    		
//    	    		if(vvv > 1) break;  //테스트용
//    	    		log.info("master='{}'", master.toString());
    	    		
    	    		//##2021.01.18 센터코드 상관없이 cc_code : CC02 인 값만 처리 
    	    		if(KurlyConstants.DEFAULT_REGION_CCCODE.equals(master.getCc_code())
    	    				&& "1".equals(master.getDelivery_round())) {
//    	    		if(KurlyConstants.DEFAULT_REGION_CENTERCODE.equals(master.getCenter_code())) {
    	    			
    	    			RegionMasterData rMaster = new RegionMasterData();
    	    			rMaster.setCoCd("MK");
    	    			rMaster.setRgnCd(master.getRegzcd());
    	    			rMaster.setRgnNm(master.getRegznm());
    	    			rMaster.setRegionGroupCode(master.getRegzky());
    	    			rMaster.setDeliveryRound(master.getDelivery_round());
    	    			rMaster.setRgnKy(master.getRegzky());
    	    			rMaster.setUseYn(KurlyConstants.STATUS_Y);
    	    			rMaster.setRegId(KurlyConstants.DEFAULT_USERID);
    	    			rMaster.setUpdId(KurlyConstants.DEFAULT_USERID);
    	    			rMaster.setRgnKyGroupCode(master.getRegzky_group_code());
    	    			rMaster.setCcCode(master.getCc_code());
    	    			
//    	    			regionMasterRepository.insertRegionMaster(rMaster);
    	    			
    	    			updateMapList.add(rMaster);

    		    		executeCount++;	
    	    		}
    	    	}

    	    	try
    	    	{
		    		List<RegionMasterData> u_updateMapList = new ArrayList<RegionMasterData>();
		
		    		for(int i=0; i <updateMapList.size(); i++) {
		
		    			u_updateMapList.add(updateMapList.get(i));
		    			
		    			//100 건 씩 처리
			    		if( (i>2 && i%50 == 0 ) 
			    				|| ( i == updateMapList.size()-1 ) ) {
		
//							log.info(">>>RegionMaster i : ["+i+"]"  );
		
							Map<String, Object> uList = new HashMap<String, Object>();
							uList.put("regionMasterList",u_updateMapList);
							
							//toteRelease update
							regionMasterService.insertRegionMasterList(uList);
							
							//초기화
							u_updateMapList = new ArrayList<RegionMasterData>();
			    		}
		    		}
    	    	} catch (Exception e1) {
            		result = "error";
        			log.error( " === RegionMaster insert  error e1" +e1 );
        			resultMessage = e1.toString();
        			r_ifYn = KurlyConstants.STATUS_N;
            	}
        		
    		}
			
			    	
    	} catch (Exception e) {
    		result = "error";
			log.error( " === RegionMasterBatch  error" +e );
			resultMessage = e.toString();
			r_ifYn = KurlyConstants.STATUS_N;
    	} finally {
    		
    		//로그 정보 insert
			LogApiStatus logApiStatus = new LogApiStatus();

	    	logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);

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
	    	if(updateMapList.size() > 0) {
		    	try {
					ObjectMapper mapper = new ObjectMapper();
					String jsonStr = mapper.writeValueAsString(updateMapList);

					logApiStatus.setApiInfo(jsonStr);
				} catch (IOException e) {
//		            e.printStackTrace();
					logApiStatus.setApiInfo(updateMapList.toString());
		        }
	    	}

	    	logApiStatus.setIntfYn(r_ifYn) ; //'Y': 전송완료, 'N': 미전송
	    	if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
	    		String c_intfMemo = StringUtil.cutString(resultMessage, 3500, "");
				logApiStatus.setIntfMemo(c_intfMemo);
	    	} else {
		    	logApiStatus.setIntfMemo(KurlyConstants.STATUS_OK);
	    	}

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;
			
			logApiStatus.setApiRuntime(apiRunTime);
	    	
	    	logApiStatusService.createLogApiStatus(logApiStatus);
	    	
    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

        	log.info("======= apiRunTime(ms) : "+ apiRunTime);

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
    	
    	log.info("=======RegionMasterBatch end=======");
    	
    }

}

