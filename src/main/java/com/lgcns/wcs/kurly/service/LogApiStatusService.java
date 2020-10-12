package com.lgcns.wcs.kurly.service;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.LogApiStatus;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : log Api 정보   Service
 */
@Service
public interface LogApiStatusService {

	public int createLogApiStatus(LogApiStatus logApiStatus);
}