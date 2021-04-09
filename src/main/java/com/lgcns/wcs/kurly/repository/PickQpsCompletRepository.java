package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.PickQpsCompletData;
import com.lgcns.wcs.kurly.dto.PickQpsCompletDetailData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 오더 피킹 완료 정보   Repository
 */
@Mapper
@Repository
public interface PickQpsCompletRepository  {
	List<PickQpsCompletData> selectPickQpsComplet();

	List<PickQpsCompletDetailData> selectPickQpsCompletDetail(PickQpsCompletData data) ;
	
	void updatePickQpsComplet(Map<String, String> data);
	
	void updatePickQpsCompletList(Map<String, Object> data);
}
