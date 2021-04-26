package com.lgcns.wcs.kurly.dto;

import java.util.Date;

import lombok.Data;

/*
 * 권역(Sorting Code)마스터
 * */
@Data
public class RegionMasterDetailData {
	/*
	private String regnky ;
	private String regncd ;
	private String regnnm ;
	private String credat ;
	private String cretim ;
	private String creusr ;
	private String lmodat ;
	private String lmotim ;
	private String lmousr ;
	private String indbzl ;
	private String indarc ;
	private int updchk ;
	private int regnsq ;
	private String daytype ;
	private String regnky_group_code ;
	private String delivery_round ;
	private String center_code ;
	 */
	private String regzky ;
	private String regzcd ;
	private String regznm ;
	private String credat ;
	private String cretim ;
	private String creusr ;
	private String lmodat ;
	private String lmotim ;
	private String lmousr ;
	private String daytype ;
	private String regzky_group_code ;
	private String delivery_round ;
	private String center_code ;
	private String cc_code ;
	private String operation_time;
}