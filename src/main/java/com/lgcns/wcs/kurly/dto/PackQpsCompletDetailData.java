package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * WCS 오더 패킹 완료 정보 detail
 * */
@Data
@Alias("PackQpsCompletDetailData")
public class PackQpsCompletDetailData {
	
	/*출하문서순번(WMS)
	 * */
	private String shipOrderItemSeq ;
	/*그룹배치번호
	 * */
	private String groupNo ;
	/*작업배치번호
	 * */
	private String workBatchNo ;
	/*출고오더UID(WCS)
	 * */
	private String shipUidKey ;
	/*출고오더UID순번(WCS)
	 * */
	private String shipUidItemSeq ;
	/*상품코드
	 * */
	private String skuCode ;
	/*Description(상품명)
	 * */
	private String skuName ;
	/*Description(센터상품명)
	 * */
	private String skuSubName ;
	/*QPS패킹완료수량
	 * */
	private double qtyQpsPacked ;
	/*QPS패킹완료일자
	 * */
	private String qpsPackingDate ;
	/*QPS패킹완료시간
	 * */
	private String qpsPackingTime ;
	/*QPS실제피킹작업자명
	 * */
	private String qpsWorkerName ;
	
}