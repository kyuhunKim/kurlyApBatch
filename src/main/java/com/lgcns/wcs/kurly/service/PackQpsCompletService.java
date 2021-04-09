package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.PackQpsCompletData;
import com.lgcns.wcs.kurly.dto.PackQpsCompletDetailData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 오더 패킹 완료 정보  Service
 */
@Service
public interface PackQpsCompletService {

	public List<PackQpsCompletData> selectPackQpsComplet() ;
	public List<PackQpsCompletDetailData> selectPackQpsCompletDetail(PackQpsCompletData data) ;
	public void updatePackQpsComplet(Map<String, String> data) ;
	
	public void updatePackQpsCompletList(Map<String, Object> data, List<LogApiStatus> logApiStatusList)  ;
}