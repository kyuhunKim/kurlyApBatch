package com.lgcns.wcs.kurly.dto.box;


import lombok.Data;

@Data
public class WifShipmentVO {

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

	//패킹박스분할여부
	private String packBoxSplitYn ;

	//추천패킹박스타입
	private String packBoxTypeRecom ;
	//특별관리고객용W/S여부
	private String specialMgntCustYn ;
	//매뉴얼출고처리여부(Y:WMS출고처리,N:WCS설비출고처리)
	private String manualProcYn ;
	//출고오더취소여부
	private String shipmentCnclYn ;
	//주문번호별운송장총갯수
	private int orderNoInvoiceCnt = 0;
	private String wmsBatchYmd ;
	/*재활용 포장재 사용 여부(Y/N)
	 * */
	private String reusablePackageYn ;

}