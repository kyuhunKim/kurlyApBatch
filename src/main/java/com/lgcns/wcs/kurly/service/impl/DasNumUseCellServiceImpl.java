package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.DasNumUseCellData;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.repository.DasNumUseCellRepository;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.service.DasNumUseCellService;


/**
 * 
 * @작성일 : 2020. 11. 24.
 * @작성자 : jooni
 * @변경이력 : 2020. 11. 24. 최초작성
 * @설명 : DAS 호기별 가용셀 정보  ServiceImpl
 */
@Service
public class DasNumUseCellServiceImpl implements DasNumUseCellService {

	@Autowired
	DasNumUseCellRepository dasNumUseCellRepository;

	@Autowired
	LogApiStatusRepository logApiStatusRepository;
	
	/**
	 * 
	 * @Method Name : selectDasNumUseCellList
	 * @작성일 : 2020. 11. 24.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 11. 24. 최초작성
	 * @Method 설명 : DAS 호기별 가용셀 정보  select
	 */
	public List<DasNumUseCellData> selectDasNumUseCellList() {
		List<DasNumUseCellData> resultData = dasNumUseCellRepository.selectDasNumUseCellList();
		return resultData;
		
	}
	/**
	 * 
	 * @Method Name : updateDasNumUseCell
	 * @작성일 : 2020. 11. 24.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 11. 24. 최초작성
	 * @Method 설명 : DAS 호기별 가용셀 정보 처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateDasNumUseCell(Map<String, String> data) {
		dasNumUseCellRepository.updateDasNumUseCell(data);
	}
	/**
	 * 
	 * @Method Name : selectDasNumUseCellCount
	 * @작성일 : 2020. 11. 24.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 11. 24. 최초작성
	 * @Method 설명 : DAS count update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public int selectDasNumUseCellCount(Map<String, String> data) {
		int resultData = dasNumUseCellRepository.selectDasNumUseCellCount(data);
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateDasNumUseCellList
	 * @작성일 : 2021. 01. 21.
	 * @작성자 : jooni
	 * @변경이력 : 2021. 01. 21. 최초작성
	 * @Method 설명 : DAS 호기별 가용셀 정보 처리 와 로그  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateDasNumUseCellList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList)   {
				
		dasNumUseCellRepository.updateDasNumUseCellList(upListMap);

		Map<String, Object> logList = new HashMap<String, Object>();
		logList.put("logApiStatusList",logApiStatusList);
		
    	//logApi insert
//		logApiStatusRepository.createLogApiStatusList(logList);
	}
}