
package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * WCS 방면 분류 완료 정보
 * */
@Data
@Alias("InvoiceSortCompletData")
public class InvoiceSortCompletData {

	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey ;
	/*송장번호(Ship order number from external system)
	 * */
	private String invoiceNo ;
	/*센터
	 * */
	private String warehouseKey ;
	/*원송장번호
	 * */
	private String originInvoiceNo ;
	/*출고오더UID(WCS)
	 * */
	private String shipUidWcs ;
	/*패킹박스분할여부
	 * N': 패킹박스분할하지않음, 'Y':패킹박스분할
	 * */
	private String packBoxSplitYn ;
	/*추천패킹박스타입
	 * */
	private String packBoxTypeRecom ;
	/*운송장상태코드
	 * 운송장상태코드정의
	 * 	 00 : 운송장삭제
	 * 	 10 : 운송장발행완료 (PRINT)
	 * 	 15 : 운송장재발행완료 (REPRINT)
	 * 	 20 : 운송장검수완료
	 * 	 90 : 운송장방면분류완료
	 * */
	private String invoiceStatus ;
	/*운송장방면분류완료슈트
	 * */
	private String invoiceSortRsltChute ;
	/*운송장방면분류완료메시지
	 * */
	private String invoiceSortRsltMsg ;
	/*소터방면분류완료일자
	 * */
	private String sorterSortDate ;
	/*소터방면분류완료시간
	 * */
	private String sorterSortTime ;

	/*데이터생성일자
	 * */
	private String insertedDate ;
	/*데이터생성시간
	 * */
	private String insertedTime ;
	/*데이터생성자
	 * */
	private String insertedUser ;
}