package com.lgcns.wcs.kurly.dto;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * WCS 오더 패킹 완료 정보
 * */
@Data
@Alias("PackQpsCompletData")
public class PackQpsCompletData {

	/* 송장번호UID(WCS)
	 * */
	private String invoiceUidKey ;
	/*출하문서번호(WMS)
	 * */
	private String shipOrderKey ;
	/*송장번호(Ship order number from external system)
	 * */
	private String invoiceNo ;
	/*센터
	 * */
	private String warehouseKey ;
	/*출고오더UID(WCS)
	 * */
	private String shipUidWcs ;
	/*셀UID번호
	 * */
	private String cellId ;
	/*원송장번호
	 * */
	private String originInvoiceNo ;
	/*출고오더매뉴얼피킹완료여부
	 * */
	private String ordmadeSplitYn ;
	/*패킹박스분할여부
	 * */
	private String packBoxSplitYn ;
	/*추천패킹박스타입
	 * */
	private String packBoxTypeRecom ;
	/*마지막출하문서번호여부
	 * WMS 출하문서기준 마지막 정보일때만 Y임
	 * */
	private String shipOrderLastYn ;
	/*디테일건수
	 * */
	private int dtlCnt ;
	/*데이터생성일자
	 * */
	private String insertedDate ;
	/*데이터생성시간
	 * */
	private String insertedTime ;
	/*데이터생성자
	 * */
	private String insertedUser ;
	
	/*detail
	 * */
	private List<PackQpsCompletDetailData> detail ;
}