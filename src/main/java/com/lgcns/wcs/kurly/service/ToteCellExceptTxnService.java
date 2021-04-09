package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.ToteCellExceptTxnSelectData;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 토트 문제 처리용 피킹정보 연계   Service
 */
@Service
public interface ToteCellExceptTxnService {

	public List<ToteCellExceptTxnSelectData> selectToteCellExceptTxn() ;
	
	public void updateToteCellExceptTxn(Map<String, String> data) ;
	
	public void updateToteCellExceptTxnList(Map<String, Object> data, List<LogApiStatus> logApiStatusList)  ;

}