package com.lgcns.wcs.kurly.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderData {
    private int    cellNo;                  //셀배치번호
    private String orderNo;                 //주문번호
    private String orderType;               //주문유형 ex) 210, 220, 225
    private String orderTypeAll;            //주문유형명 ex) 합포, 냉동, 노랑
    private String invoiceNo;               //운송장번호
    private String customerName;            //주문자이름
    private String recipientName;           //수취인이름
    private String receiverAddress;         //수취인 지번 주소
    private String receiverRoadAddress;     //수취인 도로명 주소
    private String regionGroupCode;         //권역그룹코드
    private String regionCode;              //권역코드
    private String regionGu;                //권역 구 정보
    private String regionDong;              //권역 동 정보
    private int    allcateSeq;              //할당회자
    private int    deliveryRound;           //배송회자
    private String reusablePackageYn;       //재사용포장재여부
    private String specialMgntCustYn;       //특별관리고객여부
    
    private List<OrderItemData> orderItemList;  //주문품목 목록
}
