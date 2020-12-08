package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.KurlyConstants;
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
	 * @Method Name : selectOrdInfoList
	 * @작성일 : 2020. 08. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 10. 최초작성
	 * @Method 설명 : ordList 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OrdInfoVO> selectOrdInfoList() {
		
		List<OrdInfoVO> ordList = boxRecomRepository.selectOrdInfoList();
	
		return ordList;
	}

	/**
	 * 
	 * @Method Name : selectOrdList
	 * @작성일 : 2020. 08. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 10. 최초작성
	 * @Method 설명 : ordList 조회
	 */
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
	

	/**
	 * 
	 * @Method Name : insertOrdShipmentHdr
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : OrdShipmentHdr insert
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor= Throwable.class)
	public String insertOrdShipmentHdr(Map<String, String> data) throws Exception {
		String shipUidKey = this.getShipUidKey();
		data.put("shipUidKey", shipUidKey);

		//20201201 오더생성 전에 삭제,취소 인터페이스 왔을 경우 wif update
		int chgmgntCnt = boxRecomRepository.selectWifShipChgmgntCnt(data);
		if(chgmgntCnt > 0) {
			boxRecomRepository.updateWifShipChgmgntUpdate(data);
			data.put("shipmentCnclYn", "Y");
		} else {
			data.put("shipmentCnclYn", "N");
		}
		
		boxRecomRepository.insertOrdShipmentHdr(data);
		
		return shipUidKey;
	}

	/**
	 * 
	 * @Method Name : insertOrdShipmentDtl
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : OrdShipmentDtl insert
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void insertOrdShipmentDtl(Map<String, String> data) throws Exception {
		boxRecomRepository.insertOrdShipmentDtl(data);
	}

	/**
	 * 
	 * @Method Name : getShipUidKey
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : ShipUidKey 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public String getShipUidKey() {
		String resultData = boxRecomRepository.getShipUidKey();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateWifShipmentHdr
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : WifShipmentHdr update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void updateWifShipmentHdr(Map<String, String> data)  {
		boxRecomRepository.updateWifShipmentHdr(data);
	}

	/**
	 * 
	 * @Method Name : insertOrdShipmentDtlAll
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : OrdShipmentDtl all update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void insertOrdShipmentDtlAll(Map<String, String> data) throws Exception {
		boxRecomRepository.insertOrdShipmentDtlAll(data);
	}

	/**
	 * 
	 * @Method Name : insertOrdShipmentDtl
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : OrdShipmentDtl insert
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void insertOrdShipmentDtl(OrdInfoVO tempOrd, String shipUidKey, int splitSeqNum) throws Exception {

		int f_shipUidItemSeq = 1;
		String v_shipUidItemSeq = "";

		for(OrdLineVO itOrdLine : tempOrd.getOrdList())
		{
			if(splitSeqNum == 1) {
				v_shipUidItemSeq = itOrdLine.getShipOrderItemSeq();
			} else {
				v_shipUidItemSeq = ""+f_shipUidItemSeq;
			}
						
			Map<String, String> dParam = new HashMap<String, String>();
			dParam.put("shipOrderKey", tempOrd.getShipOrderKey());
			dParam.put("shipUidKey", ""+shipUidKey);
			dParam.put("shipUidItemSeq", v_shipUidItemSeq);
			dParam.put("skuCode", itOrdLine.getSkuCode());
			dParam.put("qtyQpsOrder", ""+itOrdLine.getOrdQty());

			dParam.put("owner", ""+itOrdLine.getOwnerKey());
			
			log.info( "----------dParam " + dParam + "------------------------ " );
			boxRecomRepository.insertOrdShipmentDtl(dParam);
			
			f_shipUidItemSeq++;
		}
		
	}

	/**
	 * 
	 * @Method Name : selectDate
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : Date select
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public String selectDate() {
		String resDate = ""; 
		resDate = boxRecomRepository.selectDate();
    	return resDate;
	}	

	/**
	 * 
	 * @Method Name : selectBoxTypeMaxList
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : BoxTypeMaxList select
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<BoxTypeVO> selectBoxTypeMaxList() {
		List<BoxTypeVO> resultData = boxRecomRepository.selectBoxTypeMaxList();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectOrdInfoSearchCount
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : OrdInfoSearchCount select
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int selectOrdInfoSearchCount() {
		int resultData = boxRecomRepository.selectOrdInfoSearchCount();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectOrdInfoSearchList
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 10. 22. 최초작성
	 * @Method 설명 : OrdInfoSearchList select
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<OrdInfoVO> selectOrdInfoSearchList(SearchVO svo) {
		List<OrdInfoVO> resultData = boxRecomRepository.selectOrdInfoSearchList(svo);
		return resultData;
	}

	/**
	 * 
	 * @Method Name : selectOrdLineListAll
	 * @작성일 : 2020. 12. 07.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 07. 최초작성
	 * @Method 설명 : OrdInfoSearchList all select
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<SearchOrdInfoVO> selectOrdLineListAll(SearchVO svo) {
		List<SearchOrdInfoVO> resultData = boxRecomRepository.selectOrdLineListAll(svo);
		return resultData;
	}

	/**
	 * 
	 * @Method Name : insertOrdShipmentListType
	 * @작성일 : 2020. 12. 07.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 07. 최초작성
	 * @Method 설명 : header, detail listType insert
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor= Throwable.class)
	public void insertOrdShipmentListType(List<WifShipmentVO> hdList, List<WifShipmentDtlVO> dtList) {
		
		String inft_yn = KurlyConstants.STATUS_Y;
		try
    	{
			//OrdShipmentHdrL insert
			HashMap<String, Object> hdMap = new HashMap<String, Object>();
			hdMap.put("hdList",hdList);
			
			boxRecomRepository.insertOrdShipmentHdrListType(hdMap);

			//OrdShipmentDtl insert
			HashMap<String, Object> dtMap = new HashMap<String, Object>();
			dtMap.put("dtList",dtList);
			
			boxRecomRepository.insertOrdShipmentDtlListType(dtMap);
		
    	} catch (Exception e) {
    		
			log.info( " === insertOrdShipmentListType  error >> " +e );
			inft_yn = KurlyConstants.STATUS_N;
			    				
			e.printStackTrace();
			
    	} finally {
    		
    		//상태 업데이트
    		HashMap<String, Object> uParam = new HashMap<String, Object>();
    		uParam.put("hdList",hdList);
    		uParam.put("receiveIntfYn", inft_yn);
    		if(KurlyConstants.STATUS_N.equals(inft_yn)) {
    			uParam.put("receiveIntfCode", "");
	    	} else {
	    		uParam.put("receiveIntfCode", KurlyConstants.STATUS_OK);
	    	}
			uParam.put("receiveIntfMemo", "");

//			log.info( "----------uParam " + uParam + "------------------------ " );
			boxRecomRepository.updateWifShipmentHdrList(uParam);

    	} //finally end
		
		
	};

	/**
	 * 
	 * @Method Name : updateWifShipmentHdrList
	 * @작성일 : 2020. 12. 07.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 07. 최초작성
	 * @Method 설명 : header 실행 결과  listType insert
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Throwable.class)
	public void updateWifShipmentHdrList(Map<String, Object> data)  {
		boxRecomRepository.updateWifShipmentHdrList(data);
	}
}