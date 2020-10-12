package com.lgcns.wcs.kurly.dto.box;

import lombok.Data;

/*
 * 박스분할용 상품 마스터
 * */
@Data
public class SkuTypeVO {
	private String skuCode = ""; //상품코드
	private double skuDepth = 0; //상품 깊이
	private double skuHeight = 0; //상품 높이
	private double skuWidth = 0; //상품 너비

	public double getSkuCBM() {
		return skuDepth * skuHeight * skuWidth;
	}
}