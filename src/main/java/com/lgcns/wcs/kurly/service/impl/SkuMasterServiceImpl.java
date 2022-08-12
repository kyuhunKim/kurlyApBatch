package com.lgcns.wcs.kurly.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.SkuDetailData;
import com.lgcns.wcs.kurly.dto.SkuMasterData;
import com.lgcns.wcs.kurly.repository.SkuMasterRepository;
import com.lgcns.wcs.kurly.service.SkuMasterService;
import com.lgcns.wcs.kurly.util.DateUtil;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SkuMasterServiceImpl implements SkuMasterService {

	@Autowired
	SkuMasterRepository skuMasterRepository;
	
    /**
	 * 
	 * @Method Name : insertSkuMaster
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 상품 마스터 연계  ( WMS => WCS)
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertSkuMaster(SkuMasterData skuMasterData) {
		
		try {

			if(skuMasterData != null) {

				String owner = skuMasterData.getOwner() ;
				String skuCode = skuMasterData.getSkuCode() ;
				
				if( skuCode != null && !skuCode.equals("") )  {

					String skuName = skuMasterData.getSkuName() ;
					String skuSubName = skuMasterData.getSkuSubName() ;
					String uomKey = skuMasterData.getUomKey() ;

					String insertedDate = skuMasterData.getInsertedDate() ;
					String insertedTime = skuMasterData.getInsertedTime() ;
					String insertedUser = skuMasterData.getInsertedUser() ;
					String useYn = skuMasterData.getUseYn() ;
					
					double uomQty = skuMasterData.getUomQty() ;
					double length = skuMasterData.getLength() ;
					double width = skuMasterData.getWidth() ;
					double height = skuMasterData.getHeight() ;
					double cbm = skuMasterData.getCbm() ;
					double grossWeight = skuMasterData.getGrossWeight() ;
					double netWeight = skuMasterData.getNetWeight() ;
					double boxLength = skuMasterData.getBoxLength() ;
					double boxWidth = skuMasterData.getBoxWidth() ;
					double boxHeight = skuMasterData.getBoxHeight() ;
					double boxCbm = skuMasterData.getBoxCbm() ;
					double boxGrossWeight = skuMasterData.getBoxGrossWeight() ;
					double boxNetWeight = skuMasterData.getBoxNetWeight() ;

					String uomLen = skuMasterData.getUomLen() ;
					String uomCbm = skuMasterData.getUomCbm() ;
					String uomWeight = skuMasterData.getUomWeight() ;
					String skuGroup01 = skuMasterData.getSkuGroup01() ;
					String lotAttr11 = skuMasterData.getLotAttr11() ;
					int boxPerQnty = skuMasterData.getBoxPerQnty() ;
					String unusalSize = skuMasterData.getUnusalSize() ;
					String goodsImageURL = "" ;
					String goodsImageThumbnailUrl = "" ;
					
					String v_insertedDate = "";
					String v_insertedTime = "";

					if(StringUtil.isEmpty(owner) ) {
						owner = KurlyConstants.DEFAULT_OWNER;
					}
					if(StringUtil.isEmpty(skuName) ) {
						skuName = " ";
					}
					if(StringUtil.isEmpty(skuSubName) ) {
						skuSubName = " ";
					}
					if(StringUtil.isEmpty(uomKey) ) {
						uomKey = " ";
					}
					if(StringUtil.isEmpty(useYn) ) {
						useYn = "N";
					}
					
					if(StringUtil.isEmpty(uomLen) ) {
						uomLen = "";
					}
					if(StringUtil.isEmpty(uomCbm) ) {
						uomCbm = "";
					}
					if(StringUtil.isEmpty(uomWeight) ) {
						uomWeight = "";
					}
					if(StringUtil.isEmpty(skuGroup01) ) {
						skuGroup01 = "";
					}
					if(StringUtil.isEmpty(lotAttr11) ) {
						lotAttr11 = "";
					}
					if(StringUtil.isEmpty(goodsImageURL) ) {
						goodsImageURL = "";
					}
					if(StringUtil.isEmpty(goodsImageThumbnailUrl) ) {
						goodsImageThumbnailUrl = "";
					}
					

					if(StringUtil.isEmpty(unusalSize) ) {
						unusalSize = "N";
					}
					
					if(insertedDate == null  ) {
						v_insertedDate = "";
					} else {
						v_insertedDate = insertedDate.toString();
					}
					if(insertedTime == null  ) {
						v_insertedTime = "";
					} else {
						v_insertedTime = insertedTime.toString();
					}
					if(insertedUser == null || insertedUser.equals("") ) {
						insertedUser = KurlyConstants.DEFAULT_USERID;
					}
					
					skuMasterData.setOwner(owner);
					skuMasterData.setSkuCode(skuCode) ;
					skuMasterData.setSkuName(skuName) ;
					skuMasterData.setSkuSubName(skuSubName) ;
					skuMasterData.setUomKey(uomKey) ;
					skuMasterData.setSkuAlterCode(skuMasterData.getSkuAlterCode()) ;
					
					skuMasterData.setUomQty(uomQty) ;
					skuMasterData.setLength(length) ;
					skuMasterData.setWidth(width) ;
					skuMasterData.setHeight(height) ;
					skuMasterData.setCbm(cbm) ;
					skuMasterData.setGrossWeight(grossWeight) ;
					skuMasterData.setNetWeight(netWeight) ;
					skuMasterData.setBoxLength(boxLength) ;
					skuMasterData.setBoxWidth(boxWidth) ;
					skuMasterData.setBoxHeight(boxHeight) ;
					skuMasterData.setBoxCbm(boxCbm) ;
					skuMasterData.setBoxGrossWeight(boxGrossWeight) ;
					skuMasterData.setBoxNetWeight(boxNetWeight) ;
					skuMasterData.setUomLen(uomLen) ;
					skuMasterData.setUomCbm(uomCbm) ;
					skuMasterData.setUomWeight(uomWeight) ;
					skuMasterData.setSkuGroup01(skuGroup01) ;
					skuMasterData.setLotAttr11(lotAttr11) ;
					skuMasterData.setBoxPerQnty(boxPerQnty) ;
					skuMasterData.setUnusalSize(unusalSize) ;
					skuMasterData.setUseYn(useYn) ;
					skuMasterData.setGoodsImageURL(goodsImageURL) ;
					skuMasterData.setGoodsImageThumbnailUrl(goodsImageThumbnailUrl) ;
					
					skuMasterData.setInsertedDate(v_insertedDate) ;
					skuMasterData.setInsertedTime(v_insertedTime) ;
					skuMasterData.setInsertedUser(insertedUser);
									
					skuMasterRepository.insertSkuMaster(skuMasterData);
					
					/**
					 *   SKU_ALTER_CODE 값이 존재하는 경우 BCRCODE_TYPE 을"KANCODE' 로 설정 후 SKU_ALTER_CODE 값을 SKU_ALTER_CODE 값으로 설정함
						 SKU_ALTER_CODE 값이 존재하지 않는 경우 BCRCODE_TYPE 을"SKUCODE' 로 설정 후 SKU_CODE 값을 SKU_CODE, SKU_ALTER_CODE 값 모두 설정함
						 USE_YN 이 'N' 인 경우 TB_COM_SKU_MST 와 TB_COM_SKU_BCD_MST에 동시에 변경 작업 수행 필요
					 */
					List<SkuDetailData> dtList = new ArrayList<SkuDetailData>();
					SkuDetailData detail = new SkuDetailData();
					detail.setOwner(owner);
					detail.setSkuCode(skuCode);
					detail.setSkuAlterCode(skuCode);
					detail.setBarcodeType(KurlyConstants.BARCODETYPE_DEFAULT);
					
					if(useYn.equals(KurlyConstants.STATUS_Y)) {
						detail.setUseYn(KurlyConstants.STATUS_Y);
					} else {
						detail.setUseYn(KurlyConstants.STATUS_N);
					}
					
					detail.setInsertedUser(insertedUser);
					
					dtList.add(detail);
					if(!StringUtil.isEmpty(skuMasterData.getSkuAlterCode()) ) {
						if(!skuMasterData.getSkuAlterCode().equals(skuMasterData.getSkuCode())  ) {
							detail = new SkuDetailData();
							detail.setOwner(owner);
							detail.setSkuCode(skuCode);
							detail.setInsertedUser(insertedUser);
							detail.setSkuAlterCode(skuMasterData.getSkuAlterCode());
							
							detail.setBarcodeType(KurlyConstants.BARCODETYPE_KANCODE);
							if(useYn.equals(KurlyConstants.STATUS_Y)) {
								detail.setUseYn(KurlyConstants.STATUS_Y);	
							} else {
								detail.setUseYn(KurlyConstants.STATUS_N);
							}

							dtList.add(detail);
						}
					}
					
					Map<String, Object> detailMap = new HashMap<String, Object>();
					detailMap.put("dtList",dtList);
					
					skuMasterRepository.insertSkuDetailList(detailMap);
					
				}
				
			}

		} catch (Exception e) {
			log.info( " ===> insertSkuMaster  error" +e );
			e.printStackTrace(); 
		}
		
	}
	
    /**
	 * 
	 * @Method Name : iinsertSkuMasterList
	 * @작성일 : 2021. 01. 08.
	 * @작성자 : jooni
	 * @변경이력 : 2021. 01. 08. 최초작성
	 * @Method 설명 : 상품 마스터 생성
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertSkuMasterList(List<SkuMasterData> skuMasterDataList)  {
		
		try {
			for(SkuMasterData skuMasterData : skuMasterDataList ) {

				if(skuMasterData != null) {
	
					String owner = skuMasterData.getOwner() ;
					String skuCode = skuMasterData.getSkuCode() ;
					
					if( skuCode != null && !skuCode.equals("") )  {
	
						String skuName = skuMasterData.getSkuName() ;
						String skuSubName = skuMasterData.getSkuSubName() ;
						String uomKey = skuMasterData.getUomKey() ;
	
						String insertedDate = skuMasterData.getInsertedDate() ;
						String insertedTime = skuMasterData.getInsertedTime() ;
						String insertedUser = skuMasterData.getInsertedUser() ;
						String useYn = skuMasterData.getUseYn() ;
						
						double uomQty = skuMasterData.getUomQty() ;
						double length = skuMasterData.getLength() ;
						double width = skuMasterData.getWidth() ;
						double height = skuMasterData.getHeight() ;
						double cbm = skuMasterData.getCbm() ;
						double grossWeight = skuMasterData.getGrossWeight() ;
						double netWeight = skuMasterData.getNetWeight() ;
						double boxLength = skuMasterData.getBoxLength() ;
						double boxWidth = skuMasterData.getBoxWidth() ;
						double boxHeight = skuMasterData.getBoxHeight() ;
						double boxCbm = skuMasterData.getBoxCbm() ;
						double boxGrossWeight = skuMasterData.getBoxGrossWeight() ;
						double boxNetWeight = skuMasterData.getBoxNetWeight() ;
	
						String uomLen = skuMasterData.getUomLen() ;
						String uomCbm = skuMasterData.getUomCbm() ;
						String uomWeight = skuMasterData.getUomWeight() ;
						String skuGroup01 = skuMasterData.getSkuGroup01() ;
						String lotAttr11 = skuMasterData.getLotAttr11() ;
						int boxPerQnty = skuMasterData.getBoxPerQnty() ;
						String unusalSize = skuMasterData.getUnusalSize() ;
						String goodsImageURL = "" ;
						String goodsImageThumbnailUrl = "" ;
						
						String v_insertedDate = "";
						String v_insertedTime = "";
	
						if(StringUtil.isEmpty(owner) ) {
							owner = KurlyConstants.DEFAULT_OWNER;
						}
						if(StringUtil.isEmpty(skuName) ) {
							skuName = " ";
						}
						if(StringUtil.isEmpty(skuSubName) ) {
							skuSubName = " ";
						}
						if(StringUtil.isEmpty(uomKey) ) {
							uomKey = " ";
						}
						if(StringUtil.isEmpty(useYn) ) {
							useYn = "N";
						}
						
						if(StringUtil.isEmpty(uomLen) ) {
							uomLen = "";
						}
						if(StringUtil.isEmpty(uomCbm) ) {
							uomCbm = "";
						}
						if(StringUtil.isEmpty(uomWeight) ) {
							uomWeight = "";
						}
						if(StringUtil.isEmpty(skuGroup01) ) {
							skuGroup01 = "";
						}
						if(StringUtil.isEmpty(lotAttr11) ) {
							lotAttr11 = "";
						}
						if(StringUtil.isEmpty(goodsImageURL) ) {
							goodsImageURL = "";
						}
						if(StringUtil.isEmpty(goodsImageThumbnailUrl) ) {
							goodsImageThumbnailUrl = "";
						}
						
	
						if(StringUtil.isEmpty(unusalSize) ) {
							unusalSize = "N";
						}
						
						if(insertedDate == null  ) {
							v_insertedDate = "";
						} else {
							v_insertedDate = insertedDate.toString();
						}
						if(insertedTime == null  ) {
							v_insertedTime = "";
						} else {
							v_insertedTime = insertedTime.toString();
						}
						if(insertedUser == null || insertedUser.equals("") ) {
							insertedUser = KurlyConstants.DEFAULT_USERID;
						}
						
						skuMasterData.setOwner(owner);
						skuMasterData.setSkuCode(skuCode) ;
						skuMasterData.setSkuName(skuName) ;
						skuMasterData.setSkuSubName(skuSubName) ;
						skuMasterData.setUomKey(uomKey) ;
						skuMasterData.setSkuAlterCode(skuMasterData.getSkuAlterCode()) ;
						
						skuMasterData.setUomQty(uomQty) ;
						skuMasterData.setLength(length) ;
						skuMasterData.setWidth(width) ;
						skuMasterData.setHeight(height) ;
						skuMasterData.setCbm(cbm) ;
						skuMasterData.setGrossWeight(grossWeight) ;
						skuMasterData.setNetWeight(netWeight) ;
						skuMasterData.setBoxLength(boxLength) ;
						skuMasterData.setBoxWidth(boxWidth) ;
						skuMasterData.setBoxHeight(boxHeight) ;
						skuMasterData.setBoxCbm(boxCbm) ;
						skuMasterData.setBoxGrossWeight(boxGrossWeight) ;
						skuMasterData.setBoxNetWeight(boxNetWeight) ;
						skuMasterData.setUomLen(uomLen) ;
						skuMasterData.setUomCbm(uomCbm) ;
						skuMasterData.setUomWeight(uomWeight) ;
						skuMasterData.setSkuGroup01(skuGroup01) ;
						skuMasterData.setLotAttr11(lotAttr11) ;
						skuMasterData.setBoxPerQnty(boxPerQnty) ;
						skuMasterData.setUnusalSize(unusalSize) ;
						skuMasterData.setUseYn(useYn) ;
						skuMasterData.setGoodsImageURL(goodsImageURL) ;
						skuMasterData.setGoodsImageThumbnailUrl(goodsImageThumbnailUrl) ;
						
						skuMasterData.setInsertedDate(v_insertedDate) ;
						skuMasterData.setInsertedTime(v_insertedTime) ;
						skuMasterData.setInsertedUser(insertedUser);
										
						skuMasterRepository.insertSkuMaster(skuMasterData);
						
						/**
						 *   SKU_ALTER_CODE 값이 존재하는 경우 BCRCODE_TYPE 을"KANCODE' 로 설정 후 SKU_ALTER_CODE 값을 SKU_ALTER_CODE 값으로 설정함
							 SKU_ALTER_CODE 값이 존재하지 않는 경우 BCRCODE_TYPE 을"SKUCODE' 로 설정 후 SKU_CODE 값을 SKU_CODE, SKU_ALTER_CODE 값 모두 설정함
							 USE_YN 이 'N' 인 경우 TB_COM_SKU_MST 와 TB_COM_SKU_BCD_MST에 동시에 변경 작업 수행 필요
						 */
						List<SkuDetailData> dtList = new ArrayList<SkuDetailData>();
						SkuDetailData detail = new SkuDetailData();
						detail.setOwner(owner);
						detail.setSkuCode(skuCode);
						detail.setSkuAlterCode(skuCode);
						detail.setBarcodeType(KurlyConstants.BARCODETYPE_DEFAULT);
						
						detail.setUseYn(KurlyConstants.STATUS_Y);
						
						detail.setInsertedUser(insertedUser);
						
						dtList.add(detail);
						
						//상품바코드가 있을 경우 추가함 
						if(!StringUtil.isEmpty(skuMasterData.getSkuAlterCode()) ) {
							
							detail = new SkuDetailData();
							detail.setOwner(owner);
							detail.setSkuCode(skuCode);
							detail.setInsertedUser(insertedUser);
							detail.setSkuAlterCode(skuMasterData.getSkuAlterCode());
								
							detail.setBarcodeType(KurlyConstants.BARCODETYPE_KANCODE);
							detail.setUseYn(KurlyConstants.STATUS_Y);	
							
							dtList.add(detail);
							
						}
						
						Map<String, Object> detailMap = new HashMap<String, Object>();
						detailMap.put("dtList",dtList);
						
						skuMasterRepository.insertSkuDetailList(detailMap);
						
					}
					
				}
			}
		} catch (Exception e) {
			log.info( " ===> insertSkuMaster  error" +e );
			e.printStackTrace(); 
		}
		
	}
}