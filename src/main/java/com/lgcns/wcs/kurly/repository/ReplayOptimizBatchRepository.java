package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.ReplayOptimizBatchData;

/**
 * 
 * @작성일 : 2021. 02. 25.
 * @작성자 : jooni
 * @변경이력 : 2021. 02. 25. 최초작성
 * @설명 : 최적화 작업배치정보 연계 미처리된 건수 재작업 Repository
 */
@Mapper
@Repository
@Transactional
public interface ReplayOptimizBatchRepository  {

	List<ReplayOptimizBatchData> selectReplayOptimize();
	
	void updateOptimizBatchMake(ReplayOptimizBatchData data);
	
	void updateOptimizBatchCancel(ReplayOptimizBatchData data);
		
	void updateOrdShipmentHdr(ReplayOptimizBatchData data);

}
