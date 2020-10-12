package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.ToteCellExceptTxnSelectData;
import com.lgcns.wcs.kurly.repository.ToteCellExceptTxnRepository;
import com.lgcns.wcs.kurly.service.ToteCellExceptTxnService;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 토트 문제 처리용 피킹정보 연계   ServiceImpl
 */
@Service
public class ToteCellExceptTxnServiceImpl implements ToteCellExceptTxnService {

	@Autowired
	ToteCellExceptTxnRepository toteCellExceptTxnRepository;
	
	/**
	 * 
	 * @Method Name : selectToteCellExceptTxn
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 토트 문제 처리용 피킹정보 연계 정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<ToteCellExceptTxnSelectData> selectToteCellExceptTxn() {
		List<ToteCellExceptTxnSelectData> resultData = toteCellExceptTxnRepository.selectToteCellExceptTxn();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateToteCellExceptTxn
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 토트 문제 처리용 피킹정보 연계  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updateToteCellExceptTxn(Map<String, String> data) {
		toteCellExceptTxnRepository.updateToteCellExceptTxn(data);
	}
	
	
}