package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplayData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 미출오더 상품보충용 추가피킹정보 연계  Service
 */
@Service
public interface OrdmadeNotfullyReplayService {

	public List<OrdmadeNotfullyReplayData> selectOrdmadeNotfullyReplay() ;
	public void updateOrdmadeNotfullyReplay(Map<String, String> data) ;
	
	public void updateOrdmadeNotfullyReplayList(Map<String, Object> data, List<LogApiStatus> logApiStatusList)  ;
}