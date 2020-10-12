package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.InvoicePrintCompletData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 운송장 발행 정보   Repository
 */
@Mapper
@Repository
public interface InvoicePrintCompletRepository  {
	List<InvoicePrintCompletData> selectInvoicePrintComplet();
	
	void updateInvoicePrintComplet(Map<String, String> data);
}
