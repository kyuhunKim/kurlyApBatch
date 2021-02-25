package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.ReplayOptimizBatchData;
import com.lgcns.wcs.kurly.repository.ReplayOptimizBatchRepository;
import com.lgcns.wcs.kurly.service.ReplayOptimizBatchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ReplayOptimizBatchServiceImpl implements ReplayOptimizBatchService {

	@Autowired
	ReplayOptimizBatchRepository replayOptimizBatchRepository;

	/**
	 * 
	 * @Method Name : selectReplayOptimize
	 * @작성일 : 2021. 02. 25.
	 * @작성자 : jooni
	 * @변경이력 : 2021. 02. 25. 최초작성
	 * @Method 설명 :  미적용된 최적화 작업배치정보 조회 
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public List<ReplayOptimizBatchData> selectReplayOptimize() {
		List<ReplayOptimizBatchData> resultData = replayOptimizBatchRepository.selectReplayOptimize();
		return resultData;
	}

	/*
	 * 최적화 배치 tb_ord_shipment_dtl update
	 * */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateOptimizBatchMake(ReplayOptimizBatchData data) {
		replayOptimizBatchRepository.updateOptimizBatchMake(data);
	}

	/*
	 * 최적화 배치 tb_ord_shipment_hdr update
	 * */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateOrdShipmentHdr(ReplayOptimizBatchData data) {
		replayOptimizBatchRepository.updateOrdShipmentHdr(data);
	}
	
	/*
	 * 최적화 배치 취소 tb_ord_shipment_dtl update
	 * */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateOptimizBatchCancel(ReplayOptimizBatchData data) {
		replayOptimizBatchRepository.updateOptimizBatchCancel(data);
	}

}