package com.lgcns.wcs.kurly.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogBatchExec;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : 배치 실행정보   Service
 */
@Service
public interface LogBatchExecService {

	public int createLogBatchExec(LogBatchExec logBatchExec);
	public int createLogBatchExecMap(HashMap<String, String> param) ;
}