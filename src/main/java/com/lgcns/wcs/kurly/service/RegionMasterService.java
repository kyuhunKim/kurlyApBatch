package com.lgcns.wcs.kurly.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.RegionMasterData;
import com.lgcns.wcs.kurly.dto.RegionMasterHeaderData;

/**
 * 
 * @작성일 : 2020. 08. 11.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 11. 최초작성
 * @설명 : region master   Service
 */
@Service
public interface RegionMasterService {

	public RegionMasterHeaderData insertRegionMaster() ;
	
	public void insertRegionMaster(RegionMasterData data) ;
	public void insertRegionMasterList(Map<String, Object> upListMap) ;
}