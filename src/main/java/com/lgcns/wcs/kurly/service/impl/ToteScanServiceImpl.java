package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.ToteScanData;
import com.lgcns.wcs.kurly.repository.ToteScanRepository;
import com.lgcns.wcs.kurly.service.ToteScanService;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : WCS 토트 자동화 설비 투입    ServiceImpl
 */
@Service
public class ToteScanServiceImpl implements ToteScanService {

	@Autowired
	ToteScanRepository toteScanRepository;
	
	/**
	 * 
	 * @Method Name : selectToteScan
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 토트 자동화 설비 투입  정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<ToteScanData> selectToteScan() {
		List<ToteScanData> resultData = toteScanRepository.selectToteScan();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateToteScan
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 토트 문제 처리용 피킹정보 연계  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updateToteScan(Map<String, String> data) {
		toteScanRepository.updateToteScan(data);
	}
}