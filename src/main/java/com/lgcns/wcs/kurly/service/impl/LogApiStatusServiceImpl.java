package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : log Api 정보   ServiceImpl
 */
@Slf4j
@Service
public class LogApiStatusServiceImpl implements LogApiStatusService {
	
	@Autowired
	LogApiStatusRepository logApiStatusRepository;

	/**
	 * 
	 * @Method Name : createLogApiStatus
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 토트매핑 정보전송
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public int createLogApiStatus(LogApiStatus logApiStatus) {
		
		log.info("=================createLogApiStatus end========");
		if(logApiStatus.getApiYyyymmdd() ==null ||
				"".equals(logApiStatus.getApiYyyymmdd())) {
	        String sYyyymmdd = DateUtil.getToday("yyyyMMdd");
	        logApiStatus.setApiYyyymmdd(sYyyymmdd);
		}
		if(logApiStatus.getWarehouseKey() ==null ||
				"".equals(logApiStatus.getWarehouseKey())) {
			logApiStatus.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
		}
		String v_ApiInfo = logApiStatus.getApiInfo();
//		String c_ApiInfo = StringUtil.cutString(v_ApiInfo, 3500, "");
		logApiStatus.setApiInfo(v_ApiInfo);
		
		String v_intfMemo = logApiStatus.getIntfMemo();
		String c_intfMemo = StringUtil.cutString(v_intfMemo, 3500, "");
    	logApiStatus.setIntfMemo(c_intfMemo);
    	
		int seqId = logApiStatusRepository.createLogApiStatus(logApiStatus);
		
    	log.info("=================createLogApiStatus end========"+ "["+seqId+"]");
		return seqId;
	}

}