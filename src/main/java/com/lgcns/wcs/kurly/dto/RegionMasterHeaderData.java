package com.lgcns.wcs.kurly.dto;

import java.util.List;

import lombok.Data;

/*
 * 권역(Sorting Code)마스터
 * */
@Data
public class RegionMasterHeaderData {

	
	/*error code
	 * */
	private int error_code ;

	/*error message
	 * */
	private String error_message ;

	/*error data
	 * */
	private String error_data ;

	/*error code
	 * */
	private List<RegionMasterDetailData> data ;
}