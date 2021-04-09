package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.ToteScanData;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : WCS 토트 자동화 설비 투입    Service
 */
@Service
public interface ToteScanService {

	public List<ToteScanData> selectToteScan() ;
	public void updateToteScan(Map<String, String> data) ;
	
	public void updateToteScanList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList) ;
}