package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplayData;
import com.lgcns.wcs.kurly.repository.OrdmadeNotfullyReplayRepository;
import com.lgcns.wcs.kurly.service.OrdmadeNotfullyReplayService;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 미출오더 상품보충용 추가피킹정보 연계  ServiceImpl
 */
@Service
public class OrdmadeNotfullyReplayServiceImpl implements OrdmadeNotfullyReplayService {

	@Autowired
	OrdmadeNotfullyReplayRepository ordmadeNotfullyReplayRepository;
	
	/**
	 * 
	 * @Method Name : selectOrdmadeNotfullyReplay
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 미출오더 상품보충용 추가피킹정보 연계  정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<OrdmadeNotfullyReplayData> selectOrdmadeNotfullyReplay() {
		List<OrdmadeNotfullyReplayData> resultData = ordmadeNotfullyReplayRepository.selectOrdmadeNotfullyReplay();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateOrdmadeNotfullyReplay
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 미출오더 상품보충용 추가피킹정보 연계  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updateOrdmadeNotfullyReplay(Map<String, String> data) {
		ordmadeNotfullyReplayRepository.updateOrdmadeNotfullyReplay(data);
	}
}