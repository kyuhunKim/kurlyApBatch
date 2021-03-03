package com.lgcns.wcs.kurly.dto;

import lombok.Data;

/*
 * WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계
 * */
@Data
public class OrdmadeNotfullySendData {
	
	/*센터
	 * */
	private String warehouseKey ;
	private String docDate ;
	/*그룹배치번호
	 * */
	private String groupNo ;
	/*작업배치번호
	 * */
	private String workBatchNo ;
	/*WMS피킹지시그룹
	 * */
	private String pickingBatchNo ;
	/*상품코드
	 * */
	private String skuCode ;
	/*Description(상품명)
	 * */
	private String skuName ;
	/*Description(센터상품명)
	 * */
	private String skuSubName ;
	/*QPS미출오더수량
	 * */
	private double qtyNotfully ;

	/*데이터생성일자
	 * */
	private String insertedDate ;
	/*데이터생성시간
	 * */
	private String insertedTime ;
	/*데이터생성자
	 * */
	private String insertedUser ;
	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey ;
	/*출하문서순번(WMS)
	 * */
	private String shipOrderItemSeq ;
}