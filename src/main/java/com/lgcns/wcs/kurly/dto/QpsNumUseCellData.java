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
	/*센터
	 * */
	private String warehouseKey ;
	/*QPS 1호기 가용셀수
	 * */
	private int qpsNumUseCell01 ;
	/*QPS 2호기 가용셀수
	 * */
	private int qpsNumUseCell02 ;
	/*DAS 오더처리 가용셀수
	 * */
	private int dasNumUseCell ;
	/*가용셀수배치수행일자
	 * */
	private String batchRunDate ;
	/*가용셀수배치수행시간
	 * */
	private String batchRunTime ;
	
	/*DAS 호기용 예비컬럼1
	 * */
	private String dasNumAttr1 = "";
	/*DAS 호기용 예비컬럼2
	 * */
	private String dasNumAttr2 = "" ;
	/*DAS셀그룹번호(DAS출고시활용)
	 * */
	private String dasCellGroupId ;


}