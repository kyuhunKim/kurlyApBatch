package com.lgcns.wcs.kurly.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.lgcns.wcs.kurly.dto.InvoiceSortCompletData;

/**
 * 
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @설명 : WCS 방면 분류 완료 정보   Repository
 */
@Mapper
@Repository
public interface InvoiceSortCompletRepository  {
	List<InvoiceSortCompletData> selectInvoiceSortComplet();
	
	void updateInvoiceSortComplet(Map<String, String> data);
	
	void updateInvoiceSortCompletList(Map<String, Object> data);
}
