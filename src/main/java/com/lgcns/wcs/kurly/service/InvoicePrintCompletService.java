package com.lgcns.wcs.kurly.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lgcns.wcs.kurly.dto.InvoicePrintCompletData;
import com.lgcns.wcs.kurly.dto.LogApiStatus;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 운송장 발행 정보   Service
 */
@Service
public interface InvoicePrintCompletService {

	public List<InvoicePrintCompletData> selectInvoicePrintComplet() ;
	public void updateInvoicePrintComplet(Map<String, String> data) ;
	
	public void updateInvoicePrintCompletList(Map<String, Object> data, List<LogApiStatus> logApiStatusList)  ;
}