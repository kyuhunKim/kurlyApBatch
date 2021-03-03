package com.lgcns.wcs.kurly.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.SkuDetailData;
import com.lgcns.wcs.kurly.dto.SkuMasterData;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 상품 마스터 연계    Repository
 */
@Mapper
@Repository
@Transactional
public interface SkuMasterRepository  {
	void insertSkuMaster(SkuMasterData data);
	void insertSkuDetail(SkuDetailData data);
	
	void insertSkuDetailList(Map<String, Object> list);
}
