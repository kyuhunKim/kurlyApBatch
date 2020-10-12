package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.PackQpsCompletData;
import com.lgcns.wcs.kurly.dto.PackQpsCompletDetailData;
import com.lgcns.wcs.kurly.repository.PackQpsCompletRepository;
import com.lgcns.wcs.kurly.service.PackQpsCompletService;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 오더 패킹 완료 정보  ServiceImpl
 */
@Service
public class PackQpsCompletServiceImpl implements PackQpsCompletService {

	@Autowired
	PackQpsCompletRepository packQpsCompletRepository;
	
	/**
	 * 
	 * @Method Name : selectPackQpsComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 오더 패킹 완료 정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<PackQpsCompletData> selectPackQpsComplet() {
		List<PackQpsCompletData> resultData = packQpsCompletRepository.selectPackQpsComplet();
		return resultData;
	}
	/**
	 * 
	 * @Method Name : selectPackQpsCompletDetail
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 오더 패킹 완료 정보 조회 detail
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<PackQpsCompletDetailData> selectPackQpsCompletDetail(PackQpsCompletData data) {
		List<PackQpsCompletDetailData> resultData = packQpsCompletRepository.selectPackQpsCompletDetail(data);
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updatePackQpsComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 오더 패킹 완료 정보  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updatePackQpsComplet(Map<String, String> data) {
		packQpsCompletRepository.updatePackQpsComplet(data);
	}
}