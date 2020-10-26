package com.lgcns.wcs.kurly.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * QPS 호기별 가용셀 정보
 * */
@Data
@Alias("QpsNumUseCell")
public class QpsNumUseCellData {
	/*회사 코드
	 * */
	private String warehouseKey ;
	/*회사 코드
	 * */
	private int qpsNumUseCell01 ;
	/*회사 코드
	 * */
	private int qpsNumUseCell02 ;
	/*회사 코드
	 * */
	private int dasNumUseCell ;
	/*회사 코드
	 * */
	private String batchRunDate ;
	/*회사 코드
	 * */
	private String batchRunTime ;
	
	/*DAS 호기용 예비컬럼1
	 * */
	private String dasNumAttr1 = "";
	/*DAS 호기용 예비컬럼2
	 * */
	private String dasNumAttr2 = "" ;

}