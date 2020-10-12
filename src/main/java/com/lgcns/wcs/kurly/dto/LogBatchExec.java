package com.lgcns.wcs.kurly.dto;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * WCS BATCH 실행로그 관리
 * */
@Data
@Alias("LogBatchExec")
public class LogBatchExec {
	
	/*배치이력관리용 시퀀스
	 * */
	private int batchExecSeq ;
	/*배치로그생성일자
	 * */
	private String logYyyymmdd ;
	/*실행서버 IP
	 * */
	private String serverIp ;
	/*실행서버 HOST명
	 * */
	private String serverHost ;
	/*센터
	 * */
	private String warehouseKey ;
	/*실행함수
	 * */
	private String execMethod ;
	/*메세지LOG
	 * */
	private String messageLog ;
	/*성공여부
	 * */
	private String successYn ;
	/*강제실행 여부
	 * */
	private String executeDirectYn ;
	/*실행건수
	 * */
	private int executeCount ;
	/*실행시작일자
	 * */
	private Date startDate ;
	/*실행종료일자
	 * */
	private Date endDate ;
	/*데이터삽입일자
	 * */
	private String insertedDate ;
	/*데이터삽입시간
	 * */
	private String insertedTime ;
}