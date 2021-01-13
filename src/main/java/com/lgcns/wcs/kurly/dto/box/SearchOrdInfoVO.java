package com.lgcns.wcs.kurly.dto.box;

import lombok.Data;
import lombok.ToString;

/*
 * 오더분할용 VO
 * */
@ToString
@Data
public class SearchOrdInfoVO {

	private String shipUidKey ;
	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey	;
	/*센터
	 * */
	private String warehouseKey ;
	/*출하유형
	 * */
	private String shipType ;
	/*문서상태
	 * */
	private String docStatus ;
	/*문서일자
	 * */
	private String docDate ;
	/*Order of Priority(회원등급)
	 * */
	private String memberGrade ;
	
	/*화주
	 * */
	private String ownerKey ;
	/*Requested ship date(배송요청일)
	 * */
	private String reqShipDate ;
	
	/*Business partner document number(거래처코드)
	 * */
	private String supplierCode ;
	/*Business partner : Receiver(고객명)
	 * */
	private String customerName ;
	/*Partner Group 1(주문유형)[일반:GEN, 이벤트:ENT]
	 * */
	private String orderType ;
	/*Partner Group 2(운송유형)[쇼핑몰:01, 배송대행:02]
	 * */
	private String transportType ;
	/*Partner Group 3(주문번호)
	 * */
	private String orderNo ;
	
	/*Ship order number from external system(송장번호)
	 * */
	private String invoiceNo ;
	/*Target location by distribution task(무인택배함)
	 * */
	private String courierBoxPassword ;
	/*수령자명
	 * */
	private String recipientName ;
	/*수령자연락처
	 * */
	private String recipientTelNo ;
	/*도로명기본주소
	 * */
	private String roadAddr ;
	/*도로명상세주소
	 * */
	private String roadAddrDetail ;
	/*고객ID
	 * */
	private String customerId ;
	/*고객전화번호
	 * */
	private String customerTelNo ;
	/*구우편번호
	 * */
	private String zipCode ;
	/*고객핸드폰번호
	 * */
	private String customerPhoneNo ;
	/*신우편번호
	 * */
	private String newZipCode ;
	/*지번기본주소
	 * */
	private String jibunBasicAddr ;
	
	/*지번상세주소
	 * */
	private String jibunDetailAddr ;
	/*비고
	 * */
	private String remark ;
	/*주소 타입
	 * */
	private String baseAddrType ;
	
	/*주소 구 정보
	 * */
	private String orderAddrGugun ;
	/*주소 동 정보
	 * */
	private String orderAddrDong ;
	/*배송사 코드
	 * */
	private String courierCode ;
	
	/*
	 * */
	private String note ;
	/*
	 * */
	private String csNotice ;
	/*
	 * */
	private String customerMessage ;
	/*
	 * */
	private String regionGroupCode ;
	/*
	 * */
	private String regionCode ;
	/*
	 * */
	private String deliveryRound ;
	/*
	 * */
	private String allocType ;
	/*
	 * */
	private String allocSeq ;
	/*
	 * */
	private String originInvoiceNo ;
	/*데이터생성일자
	 * */
	private String insertedDate ;
	/*데이터생성시간
	 * */
	private String insertedTime ;
	/*데이터생성자
	 * */
	private String insertedUser ;
	/*수정일자
	 * */
	private String modifiedDate	;
	/*수정시간
	 * */
	private String modifiedTime	;
	/*수정자
	 * */
	private String modifiedUser	;

	
	private String packBoxSplitYn ;

	private String packBoxTypeRecom ;
	
	private String specialMgntCustYn ;
	private String manualProcYn ;
	private String shipmentCnclYn ;
	private String boxSplitCheckYn ;
	
	private String shipUidItemSeq ;

	/*출하문서순번(WMS)
	 * */
	private String shipOrderItemSeq ;
	/*개별문서상태
	 * */
	private String itemStatus ;
	/*상품코드
	 * */
	private String skuCode ;
	private int ordQty ;
	private double skuDepth = 0; //상품 깊이
	private double skuHeight = 0; //상품 높이
	private double skuWidth = 0; //상품 너비
	
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
	/*화주
	 * */
	private String owner ;
	private String dtRemark ;

	/*Ship order number from external system(송장번호)
	 * */
	private String groupNo ;
	private String workBatchNo ;
	private String qpsNum ;
	private String wmsBatchYmd ;
	
	private String skuNotYn ;
	private String hdWmsBatchYmd ;
}