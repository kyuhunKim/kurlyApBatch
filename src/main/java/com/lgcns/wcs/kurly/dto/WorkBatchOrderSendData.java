package com.lgcns.wcs.kurly.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkBatchOrderSendData {
    private String warehouseKey;    //센터코드
    private String workBatchNo;     //WMS 최적화에서 생성한 워크배치 번호

    private List<OrderData> orderList;  //주문목록
}
