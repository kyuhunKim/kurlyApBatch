package com.lgcns.wcs.kurly.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("WrokBatchOrderData")
public class WorkBatchOrderData {
    private String shipUidKey;
    private String shipUidItemSeq;
    private String warehouseKey;
    private String workBatchNo;
    private String documentDate;
    private int    cellNo;
    private String orderNo;
    private String orderType;
    private String orderTypeAll;
    private String invoiceNo;
    private String customerName;
    private String recipientName;
    private String receiverAddress;
    private String receiverRoadAddress;
    private String regionGroupCode;
    private String regionCode;
    private String regionGu;
    private String regionDong;
    private int    allcateSeq;
    private int    deliveryRound;
    private String reusablePackageYn;
    private String specialMgntCustYn;
    private String skuCode;
    private String skuName;
    private String skuAlterCode;
    private String productCode;
    private int quantity;
}
