package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * WCS 토트 자동화 설비 투입 정보 (마스터)
 * */
@Data
@Alias("ToteScanData")
public class ToteScanData {
	
	/*토트ID번호
	 * */
	private String toteId ;
	/*센터
	 * */
	private String warehouseKey ;
	
	/*피킹구분
	 * */
	private String pickingType ;
	/*프로세스유형(SINGLE/MULTI)
	 * */
	private String processType ;
	/*QPS 호기번호
	 * */
	private String qpsUnitNo ;
	/*설비번호(FACTOVA)
	 * */
	private String facEqpId ;
	/*설비명(FACTOVA)
	 * */
	private String facEqpName ;
	/*토트스캔일자
	 * */
	private String toteScanDate ;
	/*토트스캔시간
	 * */
	private String toteScanTime ;
	/*토트이력확인용번호
	 * */
	private int toteUniqueNo ;

	/*데이터생성일자
	 * */
	private String insertedDate	;
	/*데이터생성시간
	 * */
	private String insertedTime	;
	/*데이터생성자
	 * */
	private String insertedUser	;
}