package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.ToteScanData;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : WCS 토트 자동화 설비 투입    Repository
 */
@Mapper
@Repository
public interface ToteScanRepository  {
	List<ToteScanData> selectToteScan();
	void updateToteScan(Map<String, String> data);
	
	void updateToteScanList(Map<String, Object> data);
}
