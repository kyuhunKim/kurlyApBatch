package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyData;

/**
 * 
 * @작성일 : 2020. 07. 20.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 20. 최초작성
 * @설명 : WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계  Repository
 */
@Mapper
@Repository
public interface OrdmadeNotfullyRepository  {
	List<OrdmadeNotfullyData> selectOrdmadeNotfully();
	
	void updateOrdmadeNotfully(Map<String, String> data);
	
	void updateOrdmadeNotfullyList(Map<String, Object> data);
}
