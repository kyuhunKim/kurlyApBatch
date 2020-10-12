package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * [WCS] 인터페이스 API로그
 * */
@Data
@Alias("LogApiStatus")
public class LogApiStatus {
	
	/*API이력관리용 시퀀스
	 * */
	private int apiStatusSeq ;
	/*센터
	 * */
	private String warehouseKey = "";
	/*API실행일자
	 * */
	private String apiYyyymmdd = "";
	/*실행함수
	 * */
	private String execMethod = "";
	/*작업배치번호
	 * */
	private String workBatchNo = "";
	/*그룹배치번호
	 * */
	private String groupNo = "";
	/*출고오더UID(WCS)
	 * */
	private String shipUidWcs = "";
	/*출고오더UID순번(WCS)
	 * */
	private String shipUidSeq = "";
	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey = "";
	/*출하문서순번(WMS)
	 * */
	private String shipOrderItemSeq = "";
	/*토트ID번호
	 * */
	private String toteId = "";
	/*송장번호(Ship order number from external system)
	 * */
	private String invoiceNo = "";
	/*상태
	 * */
	private String status = "";
	/*지시수량
	 * */
	private double qtyOrder = 0;
	/*작업완료수량
	 * */
	private double qtyComplete = 0;
	/*상품코드
	 * */
	private String skuCode = "";
	/*WCS 작업상태
	 * */
	private String wcsStatus = "";
	/*API URL
	 * */
	private String apiUrl = "";
	/*API 호출정보
	 * */
	private String apiInfo = "";
	/*API 수행시간
	 * */
	private String apiRuntime = "";
	/*I/F FLAG [N:미반영/Y:완료/E:에러/1:처리중]
	 * */
	private String intfYn = "";
	/*인터페이스 메세지
	 * */
	private String intfMemo = "";
	/*데이터삽입일자
	 * */
	private String insertedDate = "";
	/*데이터삽입시간
	 * */
	private String insertedTime = "";
}