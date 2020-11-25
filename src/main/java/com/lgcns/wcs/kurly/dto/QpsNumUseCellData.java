package com.lgcns.wcs.kurly.dto;

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
	/*가용셀수배치수행일자
	 * */
	private String batchRunDate ;
	/*가용셀수배치수행시간
	 * */
	private String batchRunTime ;

}