package com.lgcns.wcs.kurly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.QpsNumUseCellData;
import com.lgcns.wcs.kurly.repository.QpsNumUseCellRepository;
import com.lgcns.wcs.kurly.service.QpsNumUseCellService;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @작성일 : 2020. 08. 25.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 25. 최초작성
 * @설명 : QPS 호기별 가용셀 정보  ServiceImpl
 */
@Slf4j
@Service
public class QpsNumUseCellServiceImpl implements QpsNumUseCellService {

	@Autowired
	QpsNumUseCellRepository qpsNumUseCellRepository;
	
	public List<QpsNumUseCellData> selectQpsNumUseCellList() {
		List<QpsNumUseCellData> resultData = qpsNumUseCellRepository.selectQpsNumUseCellList();
		return resultData;
		
	}

}