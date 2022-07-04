package com.lgcns.wcs.kurly.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("PickingInfoData")
public class PickingInfoData {
    private String wifPickToteHdrSeq;
    private String workBatchNo;
    private String toteId;
    private String status;
}
