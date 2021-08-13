package com.lgcns.wcs.kurly.dto;

import lombok.Data;

import java.util.List;

/*
 * 권역(Sorting Code)마스터
 * */
@Data
public class RegionMasterDetailData {
	private String code;
	private List<RegionMasterCodeData> regions;
}