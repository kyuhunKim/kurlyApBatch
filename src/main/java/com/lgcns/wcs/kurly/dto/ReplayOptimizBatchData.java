package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * 최적화 작업배치정보 연계 header
 * */
@Data
@Alias("ReplayOptimizBatchData")
public class ReplayOptimizBatchData {
	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey	;

	/*출하문서순번(WMS)
	 * */
	private String shipOrderItemSeq	;
	/*센터
	 * */
	private String warehouseKey;
	/*MAKE : 피킹 배치 생성시, CANCEL : 피킹 배치 취소시 (피킹 취소 이후 재생성)
	 * */
	private String batchTypeStatus	;
	/*문서생성일자
	 * */
	private String docDate	;
	/*개별문서상태
	 * */
	private String itemStatus	;
	/*상품코드
	 * */
	private String skuCode	;
	/*Ship order quantity(주문수량)
	 * */
	private double qtyShipOrder	;
	/*Allocated quantity(할당수량)
	 * */
	private double qtyAllocated	;
	/*Description(상품명)
	 * */
	private String skuName	;
	/*Description(센터상품명)
	 * */
	private String skuSubName	;
	/*Alternative SKU 1
	 * */
	private String skuAlterCode	;
	/*allocType
	 * */
	private String allocType	;
	/*그룹배치번호
	 * */
	private String groupNo	;
	/*작업배치번호
	 * */
	private String workBatchNo 	;
	/*피킹배치번호
	 * */
	private String pickingWaveNo	;
	/*QPS 호기번호
	 * */
	private String qpsNum	;

	/*데이터생성자
	 * */
	private String insertedUser ;

	/*피킹배치취소일자
	 * */
	private String waveBatchCnclDate ;
	/*피킹배치취소시간
	 * */
	private String waveBatchCnclTime ;
	/*피킹배치취소작업자
	 * */
	private String waveBatchCnclUser ;

	/*DAS셀그룹번호(DAS출고시활용)
	 * */
	private String dasCellGroupId ;
	/*순번
	 * */
	private String allocSeq ; 
	
	/*인터페이스 테이블용 optimizeBatchSeq
	 * */
	private String optimizeBatchSeq ;

	/*인터페이스 테이블용 적용여부
	 * */
	private String applyYn ;
	//order table 출하문서번호(WMS)
	private String dtShipOrderKey ; 
	//order table 출하문서순번(WMS)
	private String dtDhipOrderItemSeq ;
	//order table 그룹배치번호
	private String dtGroupNo ;

	/* 배치내 오더라인 수 */
	private int lineCnt;

	/* ROC 오더라인 순서 */
	private int rocOrdSeq;

	/* ROC 상품 순서 */
	private int rocSkuSeq;
}