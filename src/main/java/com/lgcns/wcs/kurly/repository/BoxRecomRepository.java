package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.box.BoxTypeVO;
import com.lgcns.wcs.kurly.dto.box.CellTypeVO;
import com.lgcns.wcs.kurly.dto.box.OrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.OrdLineVO;
import com.lgcns.wcs.kurly.dto.box.SearchVO;
import com.lgcns.wcs.kurly.dto.box.SkuTypeVO;

/**
 * 
 * @작성일 : 2020. 08. 05.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 05. 최초작성
 * @설명 : order 분할   Repository
 */
@Mapper
@Repository
public interface BoxRecomRepository  {
	List<SkuTypeVO> selectSkuMasterList();
	List<BoxTypeVO> selectBoxTypeList();
	List<CellTypeVO> selectCellTypeList();

	List<OrdInfoVO> selectOrdInfoList();
	List<OrdLineVO> selectOrdLineList(Map<String, String> data);
	
	int insertOrdShipmentHdr(Map<String, String> data) ;
	int insertOrdShipmentDtl(Map<String, String> data) ;
	String getShipUidKey();

	int updateWifShipmentHdr(Map<String, String> data) ;
	
	void insertOrdShipmentDtlAll(Map<String, String> data) ;
	
	int selectOrdShipmentCount(Map<String, String> data) ;
	
	String selectDate() ;
	
	List<BoxTypeVO> selectBoxTypeMaxList();
	int selectOrdInfoSearchCount();
	List<OrdInfoVO> selectOrdInfoSearchList(SearchVO svo);
}
