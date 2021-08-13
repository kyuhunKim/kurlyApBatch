package com.lgcns.wcs.kurly.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.ReplayOptimizBatchData;

@Service
@Transactional
public interface ReplayOptimizBatchService {


	/*
	 * 최적화 배치 재작업 대상 조회
	 * */
	public List<ReplayOptimizBatchData> selectReplayOptimize() ;

	/*
	 * 최적화 배치 tb_ord_shipment_dtl update
	 * */
	public void updateOptimizBatchMake(ReplayOptimizBatchData data);

	/*
	 * 최적화 배치 tb_ord_shipment_hdr update
	 * */
	public void updateOrdShipmentHdr(ReplayOptimizBatchData data);
	
	/*
	 * 최적화 배치 취소 tb_ord_shipment_dtl update
	 * */
	public void updateOptimizBatchCancel(ReplayOptimizBatchData data);
	
}