package com.lgcns.wcs.kurly.dto;

import lombok.Data;

@Data
public class OrderItemData {
    private String skuCode;         //상품코드
    private String skuName;         //상품명
    private String skuAlterCode;    //바코드
    private String productCode;     //생산코드
    private int quantity;           //수량
}
