package com.lgcns.wcs.kurly.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.LogApiStatus;

/**
 * 
 * @작성일 : 2020. 07. 14.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 14. 최초작성
 * @설명 : log Api 정보   Repository
 */
@Mapper
@Repository
public interface LogApiStatusRepository  {
	int createLogApiStatus(LogApiStatus logApiStatus);

	void createLogApiStatusList(Map<String, Object> data);
}
