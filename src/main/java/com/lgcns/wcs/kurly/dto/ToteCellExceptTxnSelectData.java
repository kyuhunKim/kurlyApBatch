package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * WCS 토트 문제 처리용 피킹정보 연계
 * */
@Data
@Alias("ToteCellExceptTxnSelectData")
public class ToteCellExceptTxnSelectData {
	
	/*셀피킹예외사항관리용 시퀀스
	 * */
	private int cellExceptSeq;
	
	/*토트ID번호
	 * */
	private String toteId;
	/*토트ID번호순번
	 * */
	private String toteIdSeq;
	/*센터
	 * */
	private String warehouseKey;
	/*WMS상품피킹일자
	 * */
	private String wmsPickingDate;
	/*피킹구분
	 * */
	private String pickingType;
	/*프로세스유형(SINGLE/MULTI)
	 * */
	private String allocType;
	/*피킹예외처리유형코드
	 * */
	private String exceptTxnType;
	/*그룹배치번호
	 * */
	private String groupNo;
	/*작업배치번호
	 * */
	private String workBatchNo;
	/*상품코드
	 * */
	private String skuCode;
	/*Description(상품명)
	 * */
	private String skuName;
	/*Description(센터상품명)
	 * */
	private String skuSubName;
	/*QPS예외처리완료수량
	 * */
	private double qtyQpsExcept;
	/*상품코드(TO)
	 * */
	private String toSkuCode;
	/*Description(TO상품명)
	 * */
	private String toSkuName;
	/*Description(TO센터상품명)
	 * */
	private String toSkuSubName;
	/*상품코드(FROM)
	 * */
	private String fromSkuCode;
	/*Description(FROM상품명)
	 * */
	private String fromSkuName;
	/*Description(FROM센터상품명)
	 * */
	private String fromSkuSubName;
	/*WMS피킹지시그룹
	 * */
	private String pickingBatchNo;

	/*데이터생성일자
	 * */
	private String insertedDate	;
	/*데이터생성시간
	 * */
	private String insertedTime	;
	/*데이터생성자
	 * */
	private String insertedUser	;

	private String shipUidKey;
	private String shipUidItemSeq;

	private String shipOrderKey	;
	private String shipOrderItemSeq	;
	private String qpsNum	;

}