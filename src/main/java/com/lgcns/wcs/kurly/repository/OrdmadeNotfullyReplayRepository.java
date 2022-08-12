package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplayData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 미출오더 상품보충용 추가피킹정보 연계  Repository
 */
@Mapper
@Repository
public interface OrdmadeNotfullyReplayRepository  {
	List<OrdmadeNotfullyReplayData> selectOrdmadeNotfullyReplay();
	
	void updateOrdmadeNotfullyReplay(Map<String, String> data);
	
	void updateOrdmadeNotfullyReplayList(Map<String, Object> data);
}
