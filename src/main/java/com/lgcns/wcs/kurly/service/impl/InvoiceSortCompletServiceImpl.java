package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.InvoiceSortCompletData;
import com.lgcns.wcs.kurly.dto.LogApiStatus;
import com.lgcns.wcs.kurly.repository.InvoiceSortCompletRepository;
import com.lgcns.wcs.kurly.repository.LogApiStatusRepository;
import com.lgcns.wcs.kurly.service.InvoiceSortCompletService;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 방면 분류 완료 정보   ServiceImpl
 */
@Service
public class InvoiceSortCompletServiceImpl implements InvoiceSortCompletService {

	@Autowired
	InvoiceSortCompletRepository invoiceSortCompletRepository;

	@Autowired
	LogApiStatusRepository logApiStatusRepository;
	
	/**
	 * 
	 * @Method Name : selectInvoiceSortComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 방면 분류 완료 정보  정보 조회
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<InvoiceSortCompletData> selectInvoiceSortComplet() {
		List<InvoiceSortCompletData> resultData = invoiceSortCompletRepository.selectInvoiceSortComplet();
		return resultData;
	}

	/**
	 * 
	 * @Method Name : updateInvoiceSortComplet
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : WCS 방면 분류 완료 정보  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=SQLException.class)
	public void updateInvoiceSortComplet(Map<String, String> data) {
		invoiceSortCompletRepository.updateInvoiceSortComplet(data);
	}

	/**
	 * 
	 * @Method Name : updateInvoiceSortCompletList
	 * @작성일 : 2020. 12. 22.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 22. 최초작성
	 * @Method 설명 : WCS 방면 분류 완료 정보  처리 와 로그  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void updateInvoiceSortCompletList(Map<String, Object> upListMap, List<LogApiStatus> logApiStatusList)   {
				
		invoiceSortCompletRepository.updateInvoiceSortCompletList(upListMap);

		Map<String, Object> logList = new HashMap<String, Object>();
		logList.put("logApiStatusList",logApiStatusList);
		
    	//logApi insert
//		logApiStatusRepository.createLogApiStatusList(logList);
	}
}