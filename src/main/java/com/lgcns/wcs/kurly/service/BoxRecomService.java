package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.box.BoxTypeVO;
import com.lgcns.wcs.kurly.dto.box.CellTypeVO;
import com.lgcns.wcs.kurly.dto.box.OrdInfoList;
import com.lgcns.wcs.kurly.dto.box.OrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.OrdLineVO;
import com.lgcns.wcs.kurly.dto.box.SearchOrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.SearchVO;
import com.lgcns.wcs.kurly.dto.box.SkuTypeMap;
import com.lgcns.wcs.kurly.dto.box.SkuTypeVO;
import com.lgcns.wcs.kurly.dto.box.WifShipmentDtlVO;
import com.lgcns.wcs.kurly.dto.box.WifShipmentVO;


/**
 * 
 * @작성일 : 2020. 08. 05.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 05. 최초작성
 * @설명 : order분활   Service
 */
@Service
public interface BoxRecomService {
	public List<SkuTypeVO> selectSkuMasterList() ;

	public List<BoxTypeVO> selectBoxTypeList() ;

	public List<CellTypeVO> selectCellTypeList() ;
	
	public List<OrdLineVO> selectOrdLineList(Map<String, String> data) ;
	
	public List<OrdInfoVO> selectOrdInfoList();
	
	public String insertOrdShipmentHdr(Map<String, String> data)  throws Exception;

	public void insertOrdShipmentDtl(Map<String, String> data)  throws Exception;
	
	public OrdInfoList selectOrdList(SkuTypeMap skuMaster) ;
	
	public String getShipUidKey();
	
	public void updateWifShipmentHdr(Map<String, String> data) ;
	
	public void insertOrdShipmentDtlAll(Map<String, String> data) throws Exception ;
	
	public void insertOrdShipmentDtl(OrdInfoVO tempOrd, String shipUidKey, int splitSeqNum) throws Exception ;
	
	public String selectDate() ;
	
	public List<BoxTypeVO> selectBoxTypeMaxList() ;
	
	public int selectOrdInfoSearchCount() ;
	
	public List<OrdInfoVO> selectOrdInfoSearchList(SearchVO svo) ;

	public List<SearchOrdInfoVO> selectOrdLineListAll(SearchVO svo) ;

	public void insertOrdShipmentListType(List<WifShipmentVO> data, List<WifShipmentDtlVO> data2);

	public void updateWifShipmentHdrList(Map<String, Object> data) ;
	
}