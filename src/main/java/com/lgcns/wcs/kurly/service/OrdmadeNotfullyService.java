package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyData;

/**
 * 
 * @작성일 : 2020. 07. 20.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 20. 최초작성
 * @설명 : WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계  Service
 */
@Service
public interface OrdmadeNotfullyService {

	public List<OrdmadeNotfullyData> selectOrdmadeNotfully() ;
	public void updateOrdmadeNotfully(Map<String, String> data) ;
}