package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.DasNumUseCellData;

/**
 * 
 * @작성일 : 2020. 11. 24.
 * @작성자 : jooni
 * @변경이력 : 2020. 11. 24. 최초작성
 * @설명 : DAS 호기별 가용셀 정보  Repository
 */
@Mapper
@Repository
@Transactional
public interface DasNumUseCellRepository  {
	
	List<DasNumUseCellData> selectDasNumUseCellList();
	
	void updateDasNumUseCell(Map<String, String> data);
	int selectDasNumUseCellCount(Map<String, String> data);
}
