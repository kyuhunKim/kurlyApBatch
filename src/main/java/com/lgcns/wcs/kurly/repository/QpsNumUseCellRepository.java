package com.lgcns.wcs.kurly.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.QpsNumUseCellData;

/**
 * 
 * @작성일 : 2020. 08. 25.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 25. 최초작성
 * @설명 : QPS 호기별 가용셀 정보  Repository
 */
@Mapper
@Repository
public interface QpsNumUseCellRepository  {
	
	List<QpsNumUseCellData> selectQpsNumUseCellList();

}
