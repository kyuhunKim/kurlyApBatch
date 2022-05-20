package com.lgcns.wcs.kurly.repository;

import com.lgcns.wcs.kurly.dto.OrderData;
import com.lgcns.wcs.kurly.dto.PickingInfoData;
import com.lgcns.wcs.kurly.dto.WorkBatchOrderData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @작성일 : 2022. 04. 27.
 * @작성자 : hwan.bae
 * @변경이력 : 2022. 04. 27. 최초작성
 * @설명 : WCS DAS 데이터를 WCS-DAS-API로 전송 Repository
 */
@Mapper
@Repository
public interface WcsToDasRepository {
    List<WorkBatchOrderData> selectWorkBatchOrder();

    void updateWorkBatchOrderList(Map<String, Object> upListMap);

    List<PickingInfoData> selectPickingEndToteInfo();

    void updatePickingCompletList(Map<String, Object> upListMap);

    List<PickingInfoData> selectPickingCnclToteInfo();

    void updatePickingCnclList(Map<String, Object> upListMap);

    void createDocInfo(Map<String, Object> upListMap);
}
