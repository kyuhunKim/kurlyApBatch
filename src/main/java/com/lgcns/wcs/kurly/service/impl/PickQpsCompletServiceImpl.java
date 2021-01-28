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
import com.lgcns.wcs.kurly.dto.PickQpsCompletData;
import com.lgcns.wcs.kurly.dto.PickQpsCompletDetailData;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.repository.PickQpsCompletRepository;
import com.lgcns.wcs.kurly.service.PickQpsCompletService;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 오더 피킹 완료 정보   ServiceImpl
 */
@Service
public class PickQpsCompletServiceImpl implements PickQpsCompletService {

	@Autowired
	PickQpsCompletRepository pickQpsCompletRepository;

	@Autowired
	LogApiStatusRepository logApiStatusRepository;
	
	/**
	 * 
	 * @Method Name : selectPickQpsComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 오더 피킹 완료 정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<PickQpsCompletData> selectPickQpsComplet() {
		List<PickQpsCompletData> resultData = pickQpsCompletRepository.selectPickQpsComplet();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectPickQpsCompletDetail
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 오더 피킹 완료 정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<PickQpsCompletDetailData> selectPickQpsCompletDetail(PickQpsCompletData data) {
		List<PickQpsCompletDetailData> resultData = pickQpsCompletRepository.selectPickQpsCompletDetail(data);
		return resultData;
	}
	
	/**
	 * 
	 * @Method Name : updatePickQpsComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 오더 피킹 완료 정보  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updatePickQpsComplet(Map<String, String> data) {
		pickQpsCompletRepository.updatePickQpsComplet(data);
	}

	/**
	 * 
	 * @Method Name : updatePickQpsCompletList
	 * @작성일 : 2020. 12. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 22. 최초작성
	 * @Method 설명 : WCS 오더 피킹 완료 정보  처리 와 로그  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updatePickQpsCompletList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList)   {
				
		pickQpsCompletRepository.updatePickQpsCompletList(upListMap);

		Map<String, Object> logList = new HashMap<String, Object>();
		logList.put("logApiStatusList",logApiStatusList);
		
    	//logApi insert
		logApiStatusRepository.createLogApiStatusList(logList);
	}
}