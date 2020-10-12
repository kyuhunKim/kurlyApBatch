package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.ToteReleaseParamData;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 토트 초기화   Repository
 */
@Mapper
@Repository
public interface ToteReleaseRepository  {
	List<ToteReleaseParamData> selectToteRelease();
	
	void updateToteRelease(Map<String, String> data);
}
