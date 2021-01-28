package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.InvoicePrintCompletData;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.repository.InvoicePrintCompletRepository;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.service.InvoicePrintCompletService;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 운송장 발행 정보   ServiceImpl
 */
@Service
public class InvoicePrintCompletServiceImpl implements InvoicePrintCompletService {

	@Autowired
	InvoicePrintCompletRepository invoicePrintCompletRepository;

	@Autowired
	LogApiStatusRepository logApiStatusRepository;
	
	/**
	 * 
	 * @Method Name : selectInvoicePrintComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 운송장 발행 정보
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public List<InvoicePrintCompletData> selectInvoicePrintComplet() {
		List<InvoicePrintCompletData> resultData = invoicePrintCompletRepository.selectInvoicePrintComplet();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateInvoicePrintComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 운송장 발행 정보  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updateInvoicePrintComplet(Map<String, String> data) {
		invoicePrintCompletRepository.updateInvoicePrintComplet(data);
	}
	/**
	 * 
	 * @Method Name : updateInvoicePrintCompletList
	 * @작성일 : 2020. 12. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 10. 최초작성
	 * @Method 설명 : WCS 운송장 발행 정보  처리 와 로그  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateInvoicePrintCompletList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList)   {
		
		invoicePrintCompletRepository.updateInvoicePrintCompletList(upListMap);

		Map<String, Object> logList = new HashMap<String, Object>();
		logList.put("logApiStatusList",logApiStatusList);
		
    	//logApi insert
		logApiStatusRepository.createLogApiStatusList(logList);
	}
}