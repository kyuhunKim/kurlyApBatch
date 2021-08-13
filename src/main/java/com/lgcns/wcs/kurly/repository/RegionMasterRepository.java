package com.lgcns.wcs.kurly.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.RegionMasterData;

/**
 * 
 * @작성일 : 2020. 08. 11.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 11. 최초작성
 * @설명 : region master  Repository
 */
@Mapper
@Repository
@Transactional
public interface RegionMasterRepository  {
	
	void insertRegionMaster(RegionMasterData data) ;
	void insertRegionMasterList(Map<String, Object> data) ;
}
