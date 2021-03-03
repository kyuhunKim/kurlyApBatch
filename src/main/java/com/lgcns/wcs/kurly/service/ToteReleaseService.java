package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.ToteReleaseParamData;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 토트 초기화   Service
 */
@Service
public interface ToteReleaseService {

	public List<ToteReleaseParamData> selectToteRelease() ;
	
	public void updateToteRelease(Map<String, String> data) ;
	
	public void updateToteReleaseList(Map<String, Object> data, List<LogApiStatus> logApiStatusList)  ;

}