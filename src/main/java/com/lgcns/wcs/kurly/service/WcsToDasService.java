package com.lgcns.wcs.kurly.service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.OrderData;
import com.lgcns.wcs.kurly.dto.PickingInfoData;
import com.lgcns.wcs.kurly.dto.WorkBatchOrderData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 
 * @작성일 : 2020. 08. 05.
 * @작성일 : 2022. 04. 27.
 * @작성자 : hwan.bae
 * @변경이력 : 2022. 04. 27. 최초작성
 * @설명 : WCS DAS 데이터를 WCS-DAS-API로 전송 Service
 */
@Service
public interface WcsToDasService {


    List<WorkBatchOrderData> selectWorkBatchOrder();

    void updateWorkBatchOrderList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList);

    List<PickingInfoData> selectPickingEndToteInfo();

    void updatePickingCompletList(List<Map<String, Object>> upList, List<LogApiStatus> u_logApiStatusList);

    List<PickingInfoData> selectPickingCnclToteInfo();

    void updatePickingCnclList(List<Map<String, Object>> upList, List<LogApiStatus> u_logApiStatusList);

    void createDocInfo(List<OrderData> docList) throws Exception;

    List<WorkBatchOrderData> selectWorkBatchOrderUpdate();
}