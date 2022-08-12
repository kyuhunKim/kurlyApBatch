package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.ToteReleaseParamData;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.repository.ToteReleaseRepository;
import com.lgcns.wcs.kurly.service.ToteReleaseService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 토트 초기화   ServiceImpl
 */
@Slf4j
@Service
@Transactional
public class ToteReleaseServiceImpl implements ToteReleaseService {

	@Autowired
	ToteReleaseRepository toteReleaseRepository;

	@Autowired
	LogApiStatusRepository logApiStatusRepository;
	
	/**
	 * 
	 * @Method Name : selectToteRelease
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 토트 초기화 정보 조회
	 */
	public List<ToteReleaseParamData> selectToteRelease() {
		List<ToteReleaseParamData> resultData = toteReleaseRepository.selectToteRelease();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateToteRelease
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : tote 처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updateToteRelease(Map<String, String> data) {
		toteReleaseRepository.updateToteRelease(data);
	}

	/**
	 * 
	 * @Method Name : updateToteReleaseList
	 * @작성일 : 2020. 12. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 22. 최초작성
	 * @Method 설명 : tote 처리 와 로그  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateToteReleaseList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList)   {
				
		toteReleaseRepository.updateToteReleaseList(upListMap);

		Map<String, Object> logList = new HashMap<String, Object>();
		logList.put("logApiStatusList",logApiStatusList);
		
    	//logApi insert
		logApiStatusRepository.createLogApiStatusList(logList);
	}
	
}