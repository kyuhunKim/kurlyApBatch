package com.lgcns.wcs.kurly.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.LogBatchExec;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 배치 실행정보   Repository
 */
@Mapper
@Repository
public interface LogBatchExecRepository  {
	int createLogBatchExec(LogBatchExec logBatchExec);
}
