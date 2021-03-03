package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * 상품마스터 정보 master
 * */
@Data
@Alias("SkuMasterData")
public class SkuMasterData {
	/*화주
	 * */
	private String owner	;
	/*상품코드
	 * */
	private String skuCode	;
	/*Description(상품명)
	 * */
	private String skuName	;
	/*Description(센터상품명)
	 * */
	private String skuSubName	;
	/*단위
	 * */
	private String uomKey	;
	/*Units per measure
	 * */
	private double uomQty  ;
	/*가로(SKU)
	 * */
	private double length	 ;
	/*세로(SKU)
	 * */
	private double width	 ;
	/*높이(SKU)
	 * */
	private double height	 ;
	/*CBM (부피cm**3)
	 * */
	private double cbm	 ;
	/*Gross Weight
	 * */
	private double grossWeight	 ;
	/*Net Weight
	 * */
	private double netWeight	 ;
	/*가로(PACK)
	 * */
	private double boxLength	 ;
	/*세로(PACK)
	 * */
	private double boxWidth	 ;
	/*높이(PACK)
	 * */
	private double boxHeight ;
	/*CBM (부피cm**3)
	 * */
	private double boxCbm	 ;
	private double boxGrossWeight	;
	private double boxNetWeight	 ;
	/*길이 단위
	 * */
	private String uomLen	;
	/*CBM 단위
	 * */
	private String uomCbm	;
	/*중량 단위
	 * */
	private String uomWeight	;
	/*SKU Group 01(상품그룹)
	 * */
	private String skuGroup01	;
	/*Lot Attribute Label11 (상품규격)
	 * */
	private String lotAttr11	;
	/*박스당상품수량
	 * */
	private int boxPerQnty	= 0 ;
	/*이형상품
	 * */
	private String unusalSize	;
	/*삭제여부[Y:정상/N:삭제]
	 * */
	private String useYn	;
	/*데이터생성일자
	 * */
	private String insertedDate	;
	/*데이터생성시간
	 * */
	private String insertedTime	;
	/*데이터생성자
	 * */
	private String insertedUser	;
	/*Alternative SKU 1 (상품바코드)
	SKUKEY,제조사코드,KAN,EAN 코드
	 * */
	private String skuAlterCode	;
	
	/*상품이미지URL정보
	 * */
	private String goodsImageURL ;
	
	/*바코드 리스트 정보
	 * */
//	private List<SkuDetailData> detail;


	/*상품이미지URL정보 - 썸내일
	 * */
	private String goodsImageThumbnailUrl ;
}
