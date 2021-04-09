package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * 상품바코드 정보 Dtl
 * */
@Data
@Alias("SkuDetailData")
public class SkuDetailData {
	/*화주
	 * */
	private String owner	;
	/*상품코드
	 * */
	private String skuCode	;
	/*Alternative SKU 1 (상품바코드)
	SKUKEY,제조사코드,KAN,EAN 코드
	 * */
	private String skuAlterCode	;
	
	/*바코드 유형
	 * */
	private String barcodeType	;
	
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
	/*수정일자
	 * */
	private String modifiedDate	;
	/*수정시간
	 * */
	private String modifiedTime	;
	/*수정자
	 * */
	private String modifiedUser	;
}

