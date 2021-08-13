package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * WCS 미출오더 상품보충용 추가피킹정보 연계
 * */
@Data
@Alias("OrdmadeNotfullyReplayData")
public class OrdmadeNotfullyReplayData {
	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey ;
	/*출하문서순번(WMS)
	 * */
	private String shipOrderItemSeq ;
	/*센터
	 * */
	private String warehouseKey ;
	/*출고오더UID(WCS)
	 * */
	private String shipUidKey ;
	/*출고오더UID순번(WCS)
	 * */
	private String shipUidItemSeq ;
	/*피킹구분
	 * 미출오더추가피킹:SUPPLY
	 * */
	private String pickingType ;
	/*송장번호(Ship order number from external system)
	 * */
	private String invoiceNo ;
	/*그룹배치번호
	 * */
	private String groupNo ;
	/*작업배치번호
	 * */
	private String workBatchNo ;
	/*상품코드
	 * */
	private String skuCode ;
	/*Description(상품명)
	 * */
	private String skuName ;
	/*Description(센터상품명)
	 * */
	private String skuSubName ;
	/*QPS미출오더재피킹요청수량
	 * */
	private double qtyNotfullyReqpick ;

	/*데이터생성일자
	 * */
	private String insertedDate ;
	/*데이터생성시간
	 * */
	private String insertedTime ;
	/*데이터생성자
	 * */
	private String insertedUser ;
	/*송장번호 순번
	 * */
	private String invoiceSeq ;
}