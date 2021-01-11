package com.lgcns.wcs.kurly.dto;

public final class KurlyConstants {
	private KurlyConstants() {}

	/**
     * 상태값
     */
    public static final String STATUS_Y = "Y";
    public static final String STATUS_N = "N";
    public static final String STATUS_E = "E";
    public static final String STATUS_T = "T";
    
    public static final String STATUS_OK = "OK";
    public static final String STATUS_NG = "NG";

    public static final String DEFAULT_USERID = "BATCH";
    
  	//센터
    public static final String DEFAULT_WAREHOUSEKEY = "0000";
    public static final String DEFAULT_REGION_CENTERCODE = "SJM2";
	
	public static final String DEFAULT_OWNER = "TF";
	
	//sku barcode type
	public static final String BARCODETYPE_DEFAULT = "SKUCODE";
	
	public static final String BARCODETYPE_KANCODE = "KANCODE";

	/**
     * 토트 마스터 초기화(Release) 정보 연계
     */
    public static final String METHOD_TOTERELEASE = "toteRelease";
    /**
     * WCS 토트 자동화 설비 투입 정보 (마스터)
     * */
    public static final String METHOD_TOTESCAN = "toteScan";
    /**
     * WCS 토트 문제 처리용 피킹정보 연계
     * */
    public static final String METHOD_TOTECELLEXCEPTTXN = "toteCellExceptTxn";
    /**
     * WCS 미출오더 상품보충용 추가피킹정보 연계
     * */
    public static final String METHOD_ORDMADENOTFULLYREPLAY = "ordmadeNotfullyReplay";
    /**
     * WCS 미출오더 처리가능여부 확인(WMS 피킹 시작정보 연계)
     * */
    public static final String METHOD_ORDMADENOTFULLYREPLAYCONFIRM = "ordmadeNotfullyReplayConfirm";
    /**
     * WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계
     * */
    public static final String METHOD_ORDMADENOTFULLY = "ordmadeNotfully";
    /**
     * WCS 오더 패킹 완료 정보
     * */
    public static final String METHOD_PACKQPSCOMPLET = "packQpsComplet";
    /**
     * WCS 오더 피킹 완료 정보
     * */
    public static final String METHOD_PICKQPSCOMPLET = "pickQpsComplet";
    /**
     * WCS 운송장 발행 정보
     * */
    public static final String METHOD_INVOICEPRINTCOMPLET = "invoicePrintComplet";
    /**
     * WCS 방면 분류 완료 정보
     * */
    public static final String METHOD_INVOICESORTCOMPLET = "invoiceSortComplet";
    /**
     * ord 분할
     * */
    public static final String METHOD_BOXRECOM = "BoxRecom";
    /**
     * 전역정보
     * */
    public static final String METHOD_REGIONMASTER = "RegionMaster";
    /**
     * QPS 호기별 가용셀 정보
     * */
    public static final String METHOD_QPSNUMUSECELL = "QpsNumUseCell";

    /**
     * DAS 셀그룹번호(DAS출고시 활용)별 가용셀 정보
     * */
    public static final String METHOD_DASNUMUSECELL = "DasNumUseCell";
}