package com.lgcns.wcs.kurly.dto.box;

import lombok.Data;

@Data
public class WifShipmentDtlVO {

private String shipUidKey ;
	
	private String shipUidItemSeq ;

	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey	;
	/*출하문서순번(WMS)
	 * */
	private String shipOrderItemSeq ;
	/*개별문서상태
	 * */
	private String itemStatus ;
	/*상품코드
	 * */
	private String skuCode ;
	
	/*Original Order quantity(원주문수량)	
	 * */
	private double qtyOriginOrder ;
	/*Ship order quantity(주문수량)	
	 * */
	private double qtyShipOrder ;
	/*Appointed quantity(피킹수량)	
	 * */
	private double qtyPicking ;
	/*QPS주문처리수량
	 * */
	private String qtyQpsOrder ;

	/*Allocated quantity(할당수량)	
	 * */
	private double qtyAllocated ;
	/*Job completed quantity(패킹수량)	
	 * */
	private double qtyPacking ;
	/*Shipped quantity(출고수량)
	 * */
	private double qtyShipCompleted ;
	/*Cancelled quantity of shipping(취소수량)
	 * */
	private double qtyShipCancelled ;
	/*Quantity by unit of measure
	 * */
	private String qtyByUom ;
	/*단위구성
	 * */
	private String measureKey ;
	/*단위
	 * */
	private String uomKey ;
	/*Units per measure	
	 * */
	private double uomQty ;
	/*단위
	 * */
	private String defaultUomKey ;
	/*Default units per measure	
	 * */
	private double defaultUomQty ;
	/*Description(상품명)
	 * */
	private String skuName ;
	/*Description(센터상품명)
	 * */
	private String skuSubName ;
	/*Alternative SKU 1	
	 * */
	private String skuAlterCode ;
	
	/*SKU Group 01(상품그룹)	
	 * */
	private String skuGroup01 ;
	/*SKU Group 02(상품그룹내부)
	 * */
	private String skuGroup02 ;
	/*SKU Group 03(상품그룹외부)	
	 * */
	private String skuGroup03 ;
	/*SKU Group 04	
	 * */
	private String skuGroup04 ;
	/*sellingType
	 * */
	private String sellingType ;
	/*Gross weight	
	 * */
	private double grossWeight ;
	/*Net weight	
	 * */
	private double netWeight ;
	/*Weight unit	
	 * */
	private String weightUnit ;
	/*Length	
	 * */
	private double length ;
	/*Width	
	 * */
	private double width ;
	/*Height	
	 * */
	private double height ;
	/*Cubic meter	
	 * */
	private double cbm ;

	/*Name 1 (상품옵션명)
	 * */
	private String skuOptionName ;
	/*Description(쇼핑몰상품명)
	 * */
	private String skuShopmallName ;
	/*REMARK 항목을 SHOPMALL_NAME으로 변경 (쇼핑몰상품명)
	 * */
	private String remark ;
	/*수정일자
	 * */
	private String modifiedDate	;
	/*수정시간
	 * */
	private String modifiedTime	;
	/*수정자
	 * */
	private String modifiedUser	;
	/*화주
	 * */
	private String owner ;

	/*Ship order number from external system(송장번호)
	 * */
	private String invoiceNo ;
	private String groupNo ;
	private String workBatchNo ;
	private String qpsNum ;
	private String wmsBatchYmd ;

}