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
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyData;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.repository.OrdmadeNotfullyRepository;
import com.lgcns.wcs.kurly.service.OrdmadeNotfullyService;

/**
 * 
 * @작성일 : 2020. 07. 20.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 20. 최초작성
 * @설명 :WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계   ServiceImpl
 */
@Service
public class OrdmadeNotfullyServiceImpl implements OrdmadeNotfullyService {

	@Autowired
	OrdmadeNotfullyRepository ordmadeNotfullyRepository;

	@Autowired
	LogApiStatusRepository logApiStatusRepository;
	
	/**
	 * 
	 * @Method Name : selectOrdmadeNotfully
	 * @작성일 : 2020. 07. 20.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 20. 최초작성
	 * @Method 설명 : WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계 정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<OrdmadeNotfullyData> selectOrdmadeNotfully() {
		List<OrdmadeNotfullyData> resultData = ordmadeNotfullyRepository.selectOrdmadeNotfully();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateOrdmadeNotfully
	 * @작성일 : 2020. 07. 20.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 20. 최초작성
	 * @Method 설명 : WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계 처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updateOrdmadeNotfully(Map<String, String> data) {
		ordmadeNotfullyRepository.updateOrdmadeNotfully(data);
	}

	/**
	 * 
	 * @Method Name : updateOrdmadeNotfullyList
	 * @작성일 : 2020. 12. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 22. 최초작성
	 * @Method 설명 : WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계  처리 와 로그  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateOrdmadeNotfullyList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList)   {
				
		ordmadeNotfullyRepository.updateOrdmadeNotfullyList(upListMap);

		Map<String, Object> logList = new HashMap<String, Object>();
		logList.put("logApiStatusList",logApiStatusList);
		
    	//logApi insert
		logApiStatusRepository.createLogApiStatusList(logList);
	}
}