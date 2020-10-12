package com.lgcns.wcs.kurly.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
public interface RegionMasterRepository  {
	
	void insertRegionMaster(RegionMasterData data) ;
}
