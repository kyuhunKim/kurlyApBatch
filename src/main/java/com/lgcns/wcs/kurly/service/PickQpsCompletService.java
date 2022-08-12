package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.PickQpsCompletData;
import com.lgcns.wcs.kurly.dto.PickQpsCompletDetailData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 오더 피킹 완료 정보   Service
 */
@Service
public interface PickQpsCompletService {

	public List<PickQpsCompletData> selectPickQpsComplet() ;
	public List<PickQpsCompletDetailData> selectPickQpsCompletDetail(PickQpsCompletData data) ;
	public void updatePickQpsComplet(Map<String, String> data) ;
	
	public void updatePickQpsCompletList(Map<String, Object> data, List<LogApiStatus> logApiStatusList)  ;
}