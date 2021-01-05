package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.PackQpsCompletData;
import com.lgcns.wcs.kurly.dto.PackQpsCompletDetailData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 오더 패킹 완료 정보  Repository
 */
@Mapper
@Repository
public interface PackQpsCompletRepository  {
	List<PackQpsCompletData> selectPackQpsComplet();
	
	List<PackQpsCompletDetailData> selectPackQpsCompletDetail(PackQpsCompletData data) ;
	
	void updatePackQpsComplet(Map<String, String> data);
	
	void updatePackQpsCompletList(Map<String, Object> data);
}
