package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.box.BoxTypeVO;
import com.lgcns.wcs.kurly.dto.box.CellTypeVO;
import com.lgcns.wcs.kurly.dto.box.OrdInfoList;
import com.lgcns.wcs.kurly.dto.box.OrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.OrdLineVO;
import com.lgcns.wcs.kurly.dto.box.SearchVO;
import com.lgcns.wcs.kurly.dto.box.SkuTypeMap;
import com.lgcns.wcs.kurly.dto.box.SkuTypeVO;
import com.lgcns.wcs.kurly.repository.BoxRecomRepository;
import com.lgcns.wcs.kurly.service.BoxRecomService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @작성일 : 2020. 08. 05.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 05. 최초작성
 * @설명 : order분활   ServiceImpl
 */
@Slf4j
@Service
public class BoxRecomServiceImpl implements BoxRecomService {

	@Autowired
	BoxRecomRepository boxRecomRepository;

	/**
	 * 
	 * @Method Name : selectSkuMasterList
	 * @작성일 : 2020. 08. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 10. 최초작성
	 * @Method 설명 : sku master 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<SkuTypeVO> selectSkuMasterList() {
		List<SkuTypeVO> resultData = boxRecomRepository.selectSkuMasterList();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectBoxTypeList
	 * @작성일 : 2020. 08. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 10. 최초작성
	 * @Method 설명 : box type 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<BoxTypeVO> selectBoxTypeList() {
		List<BoxTypeVO> resultData = boxRecomRepository.selectBoxTypeList();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectCellTypeList
	 * @작성일 : 2020. 08. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 10. 최초작성
	 * @Method 설명 : cell type 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<CellTypeVO> selectCellTypeList() {
		List<CellTypeVO> resultData = boxRecomRepository.selectCellTypeList();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectOrdLineList
	 * @작성일 : 2020. 08. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 10. 최초작성
	 * @Method 설명 : order 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OrdLineVO> selectOrdLineList(Map<String, String> data) {
		List<OrdLineVO> resultData = boxRecomRepository.selectOrdLineList(data);
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectOrdLineList
	 * @작성일 : 2020. 08. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 10. 최초작성
	 * @Method 설명 : cell type 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OrdInfoVO> selectOrdInfoList() {
		
		List<OrdInfoVO> ordList = boxRecomRepository.selectOrdInfoList();
//		for(OrdInfoVO ordInfoVO : ordList) {
//
//			Map<String, String> param = new HashMap<String, String>();
//			param.put("orderNo", ordInfoVO.getOrderNo());
//			
//			List<OrdLineVO> ordLines = boxRecomRepository.selectOrdLineList(param);
//			ordInfoVO.setOrdLines(ordLines);
////			if(ordLines != null) {
////
////				double ordCBM = 0;
////				for(OrdLineVO itOrdLine : ordLines)
////				{
////					ordInfoVO.addOrdLine(itOrdLine.getSkuCode(), itOrdLine.getOrdQty());
////					itOrdLine.setOrdLine(skuMaster, itOrdLine.getSkuCode(), itOrdLine.getOrdQty());
////					ordCBM += itOrdLine.getOrdLineCBM();	
////				}
////				ordInfoVO.setOrdCBM(ordCBM);
////			}
//			
//		}
		
		return ordList;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public OrdInfoList selectOrdList(SkuTypeMap skuMaster) {

		OrdInfoList ordList = new OrdInfoList();	
		
		List<OrdInfoVO> selectList = boxRecomRepository.selectOrdInfoList();
		
			
		for(OrdInfoVO ordInfoVO : selectList) {	
			OrdInfoVO ordVO = new OrdInfoVO(skuMaster);

			ordVO.setOrderNo(ordInfoVO.getOrderNo());
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("orderNo", ordInfoVO.getOrderNo());	
			
			List<OrdLineVO> ordLines = boxRecomRepository.selectOrdLineList(param);
//			ordVO.setOrdLines(ordLines);
			if(ordLines != null) {
				for(OrdLineVO itOrdLine : ordLines)
				{
		    		log.info("ddd " + itOrdLine.getSkuCode());
					ordVO.addOrdLine(itOrdLine);
//					ordVO.addOrdLine(itOrdLine.getSkuCode(), itOrdLine.getOrdQty());
				}
			}
			ordList.addOrd(ordVO);
			
		}
		
		return ordList;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor= Throwable.class)
	public int insertOrdShipmentHdr(Map<String, String> data) throws Exception {
		int shipUidKey = this.getShipUidKey();
		data.put("shipUidKey", ""+shipUidKey);
		
		boxRecomRepository.insertOrdShipmentHdr(data);
		
		return shipUidKey;
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void insertOrdShipmentDtl(Map<String, String> data) throws Exception {
		boxRecomRepository.insertOrdShipmentDtl(data);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public int getShipUidKey() {
		int resultData = boxRecomRepository.getShipUidKey();
		return resultData;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void updateWifShipmentHdr(Map<String, String> data)  {
		boxRecomRepository.updateWifShipmentHdr(data);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void insertOrdShipmentDtlAll(Map<String, String> data) throws Exception {
		boxRecomRepository.insertOrdShipmentDtlAll(data);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void insertOrdShipmentDtl(OrdInfoVO tempOrd, int shipUidKey, int splitSeqNum) throws Exception {

		int f_shipUidItemSeq = 1;
		String v_shipUidItemSeq = "";

		for(OrdLineVO itOrdLine : tempOrd.getOrdList())
		{
			if(splitSeqNum == 1) {
				v_shipUidItemSeq = itOrdLine.getShipOrderItemSeq();
			} else {
				v_shipUidItemSeq = ""+f_shipUidItemSeq;
			}
			
			log.info( "ordNo : " + tempOrd.getOrderNo() + ", "+
			 "Sku : " + itOrdLine.getSkuCode() + ", "+
			 "WarehouseKey : " + itOrdLine.getWarehouseKey() + ", "+
			 "ordQty : " + itOrdLine.getOrdQty() + ", "+
			 "splitSeq : " + itOrdLine.getSplitSeq() + ", "+
			 "ordCBM : " + itOrdLine.getOrdLineCBM() + ", "+
			 "shipOrderKey : " + itOrdLine.getShipOrderKey() + ", "+
			 "shipUidKey : " + shipUidKey + ", "+
	    	 "shipUidItemSeq : " + v_shipUidItemSeq + ", "+
	    	 "owner : " + itOrdLine.getOwnerKey()  );
			
			Map<String, String> dParam = new HashMap<String, String>();
			dParam.put("shipOrderKey", tempOrd.getShipOrderKey());
			dParam.put("shipUidKey", ""+shipUidKey);
			dParam.put("shipUidItemSeq", v_shipUidItemSeq);
			dParam.put("skuCode", itOrdLine.getSkuCode());
			dParam.put("qtyQpsOrder", ""+itOrdLine.getOrdQty());

			//##test
			if("MF0000041067".equals(itOrdLine.getSkuCode())) {
				dParam.put("owner", "ownerownerowner");
			}
			else {
				dParam.put("owner", ""+itOrdLine.getOwnerKey());
			}
			
			log.info( "----------dParam " + dParam + "------------------------ " );
			boxRecomRepository.insertOrdShipmentDtl(dParam);
			
			f_shipUidItemSeq++;
		}
		
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public int selectOrdShipmentCount(Map<String, String> data){
		int count = boxRecomRepository.selectOrdShipmentCount(data);
		return count;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public String selectDate() {
		String resDate = ""; 
		resDate = boxRecomRepository.selectDate();
    	return resDate;
	}	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<BoxTypeVO> selectBoxTypeMaxList() {
		List<BoxTypeVO> resultData = boxRecomRepository.selectBoxTypeMaxList();
		return resultData;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public int selectOrdInfoSearchCount() {
		int resultData = boxRecomRepository.selectOrdInfoSearchCount();
		return resultData;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OrdInfoVO> selectOrdInfoSearchList(SearchVO svo) {
		List<OrdInfoVO> resultData = boxRecomRepository.selectOrdInfoSearchList(svo);
		return resultData;
	}
	
}