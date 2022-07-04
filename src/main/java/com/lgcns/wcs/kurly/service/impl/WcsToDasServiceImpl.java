package com.lgcns.wcs.kurly.service.impl;


import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.dto.OrderData;
import com.lgcns.wcs.kurly.dto.PickingInfoData;
import com.lgcns.wcs.kurly.dto.WorkBatchOrderData;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.repository.WcsToDasRepository;
import com.lgcns.wcs.kurly.service.WcsToDasService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @작성일 : 2022. 04. 27.
 * @작성자 : hwan.bae
 * @변경이력 : 2022. 04. 27. 최초작성
 * @설명 : WCS DAS 최적화 주문 데이터를 WCS-DAS-API로 전송 Service
 */
@Slf4j
@Service
public class WcsToDasServiceImpl implements WcsToDasService {

    @Autowired
    WcsToDasRepository wcsToDasRepository;

    @Autowired
    LogApiStatusRepository logApiStatusRepository;

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW, rollbackFor= SQLException.class)
    public List<WorkBatchOrderData> selectWorkBatchOrder() {
        return wcsToDasRepository.selectWorkBatchOrder();
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
    public void updateWorkBatchOrderList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList) {
        wcsToDasRepository.updateWorkBatchOrderList(upListMap);

        Map<String, Object> logList = new HashMap<String, Object>();
        logList.put("logApiStatusList", logApiStatusList);
        logApiStatusRepository.createLogApiStatusList(logList);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
    public List<PickingInfoData> selectPickingEndToteInfo() {
        return wcsToDasRepository.selectPickingEndToteInfo();
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
    public void updatePickingCompletList(List<Map<String, Object>> upList, List<LogApiStatus> logApiStatusList) {
        for(Map<String, Object> param : upList) {
            wcsToDasRepository.updatePickingCompletList(param);
        }
        Map<String, Object> logList = new HashMap<String, Object>();
        logList.put("logApiStatusList", logApiStatusList);
        logApiStatusRepository.createLogApiStatusList(logList);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
    public void createDocInfo(List<OrderData> docList) throws Exception{
        List<OrderData> u_updateMapList = new ArrayList<OrderData>();
        for (int i = 0; i < docList.size(); i++) {
            u_updateMapList.add(docList.get(i));
            //50 건 씩 처리
            if ((i > 2 && i % 50 == 0) || (i == docList.size() - 1)) {

                Map<String, Object> upListMap = new HashMap<String, Object>();
                upListMap.put("updateList", u_updateMapList);

                //update
                try {
                    wcsToDasRepository.createDocInfo(upListMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                //초기화
                u_updateMapList = new ArrayList<OrderData>();
            }
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW, rollbackFor= SQLException.class)
    public List<WorkBatchOrderData> selectWorkBatchOrderUpdate() {
        return wcsToDasRepository.selectWorkBatchOrderUpdate();
    }
}