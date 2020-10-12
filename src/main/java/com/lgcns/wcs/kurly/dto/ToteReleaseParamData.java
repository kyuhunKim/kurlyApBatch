package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * 토트 마스터 초기화(Release) 정보 연계
 * */
@Data
@Alias("ToteReleaseParamData")
public class ToteReleaseParamData {
	
	/*토트ID번호
	 * */
	private String toteId ;
	/*센터
	 * */
	private String warehouseKey ;
	/*릴리즈유형
	 * RELEASE
	 * */
	private String releaseType ="RELEASE" ;
	/*릴리즈일자
	 * */
	private String releaseDate ;
	/*릴리즈시간
	 * */
	private String releaseTime ;
	/*릴리즈유저
	 * */
	private String releaseUser ;
	/*토트이력확인용번호
	 * */
	private int toteUniqueNo = 0;
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