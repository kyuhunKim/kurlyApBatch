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
	/*Region code
	 * */
	private String rgnCd ;
	/*Region name
	 * */
	private String rgnNm ;
	/*Region group code
	 * */
	private String regionGroupCode ;
	/*delivery Round
	 * */
	private String deliveryRound ;
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
}