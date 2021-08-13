package com.lgcns.wcs.kurly.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * 권역(Sorting Code)마스터
 * */
@Data
@Alias("RegionMaster")
public class RegionMasterData {
	
	/*회사 코드
	 * */
	private String coCd ;

	/*Region group code
	 * */
	private String regionGroupCode ;
	/*Region key
	 * */
	private String rgnKy ;
	/*사용유무
	 * */
	private String useYn ;
	/*등록자ID
	 * */
	private String regId ;
	/*등록일시
	 * */
	private Date regDt ;
	/*수정자ID
	 * */
	private String updId ;
	/*수정일시
	 * */
	private Date updDt ;
	/*REGN_KEY_GROUP_CODE
	 * */
	private String rgnKyGroupCode ;
	/*김포FC센터구분자(CC02)
	 * */
	private String ccCode ;
}