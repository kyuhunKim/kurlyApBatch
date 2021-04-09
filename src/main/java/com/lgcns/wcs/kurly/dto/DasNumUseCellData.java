package com.lgcns.wcs.kurly.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/*
 * DAS 셀그룹번호(DAS출고시 활용)별 가용셀 정보
 * */
@Data
@Alias("DasNumUseCell")
public class DasNumUseCellData {
	/*센터
	 * */
	private String warehouseKey ;
	/*DAS 셀상태구분코드
	 * DAS 셀그룹번호 생성시 : MAKE ,DAS 셀그룹번호 삭제시 : DELETE
	 * */
	private String cellTypeStatus ;
	/*DAS셀그룹번호(DAS출고시활용)
	 * WCS 상에서 채번된 DAS CELL 그룹 번호임 (DELETE 인 경우도 셀그룹번호는 전송함)
	 * */
	private String dasCellGroupId ;
	/*DAS 셀그룹번호별 오더셀 수
	 * DAS 셀그룹번호별 오더셀 수 (DELETE인 경우 셀수는 0 으로 전송함)
	 * */
	private int dasNumUseCell ;
	
	/*DAS 호기용 예비컬럼1
	 * */
	private String dasNumAttr1 = "";
	/*DAS 호기용 예비컬럼2
	 * */
	private String dasNumAttr2 = "" ;
	/*가용셀수배치수행일자
	 * */
	private String batchRunDate ;
	/*가용셀수배치수행시간
	 * */
	private String batchRunTime ;
	


}