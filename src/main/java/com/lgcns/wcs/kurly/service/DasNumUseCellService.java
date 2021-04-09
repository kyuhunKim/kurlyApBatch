package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.DasNumUseCellData;
import com.lgcns.wcs.kurly.dto.LogApiStatus;

/**
 * 
 * @작성일 : 2020. 11. 24.
 * @작성자 : jooni
 * @변경이력 : 2020. 11. 24. 최초작성
 * @설명 : DAS 호기별 가용셀 정보  Service
 */
@Service
public interface DasNumUseCellService {

	public List<DasNumUseCellData> selectDasNumUseCellList();
	public void updateDasNumUseCell(Map<String, String> data);
	public int selectDasNumUseCellCount(Map<String, String> data);

	public void updateDasNumUseCellList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList) ;
}