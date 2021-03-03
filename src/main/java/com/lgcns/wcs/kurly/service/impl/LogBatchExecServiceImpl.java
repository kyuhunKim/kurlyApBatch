package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.repository.LogBatchExecRepository;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.util.HttpUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 배치 실행정보   ServiceImpl
 */
@Slf4j
@Service
public class LogBatchExecServiceImpl implements LogBatchExecService {

	@Autowired
	LogBatchExecRepository logBatchExecRepository;
	
	/**
	 * 
	 * @Method Name : createLogBatchExec
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS BATCH 실행로그 관리
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public int createLogBatchExec(LogBatchExec logBatchExec) {
		
		if(logBatchExec.getWarehouseKey() ==null ||
				"".equals(logBatchExec.getWarehouseKey())) {
			logBatchExec.setWarehouseKey(KurlyConstants.DEFAULT_WAREHOUSEKEY);
		}
		String serverHostName = "";
		String serverIp = "";

		serverHostName = HttpUtil.getHostName();
		serverIp = HttpUtil.getLocalIp();
		
		logBatchExec.setServerIp(serverIp);
		logBatchExec.setServerHost(serverHostName);
		
		Date endDate = Calendar.getInstance().getTime();
		logBatchExec.setEndDate(endDate);

		String v_messageLog = logBatchExec.getMessageLog();
		String c_messageLog = StringUtil.cutString(v_messageLog, 3500, "");
		logBatchExec.setMessageLog(c_messageLog);
    	
		int seqId = logBatchExecRepository.createLogBatchExec(logBatchExec);

    	
		return seqId;
	}

	/**
	 * 
	 * @Method Name : createLogBatchExecMap
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS BATCH 실행로그 관리
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public int createLogBatchExecMap(HashMap<String, String> param) {
		
		int seqId = 0; 

		if(param != null) {
			String serverHostName = "";
			String serverIp = "";

			serverHostName = HttpUtil.getHostName();
			serverIp = HttpUtil.getLocalIp();
			
			String p_startDate = param.get("startDate");
			String p_execMethod = param.get("execMethod");
			String p_warehouseKey = param.get("warehouseKey");
			String p_messageLog = param.get("messageLog");
			String p_successYn = param.get("successYn");
			String p_executeCount = param.get("executeCount");
			int executeCount = 0; 
			
			Date startDate = new Date(p_startDate);
			Date endDate = Calendar.getInstance().getTime();
			
			if(p_executeCount!=null&& !"".equals(p_executeCount)) {
				executeCount = Integer.parseInt(param.get("executeCount"));
			}
			
	    	LogBatchExec logBatchExec = new LogBatchExec();
			logBatchExec.setServerIp(serverIp);
			logBatchExec.setServerHost(serverHostName);
			logBatchExec.setWarehouseKey(p_warehouseKey);
			logBatchExec.setExecMethod(p_execMethod);

			String c_messageLog = StringUtil.cutString(p_messageLog, 3500, "");
			logBatchExec.setMessageLog(c_messageLog);
			
			logBatchExec.setSuccessYn(p_successYn);
			logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
			logBatchExec.setExecuteCount(executeCount);
			logBatchExec.setStartDate(startDate);
			logBatchExec.setEndDate(endDate);
			
			seqId = logBatchExecRepository.createLogBatchExec(logBatchExec);
		}

		
    	return seqId;
	}	
}