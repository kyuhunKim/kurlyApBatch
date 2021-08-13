package com.lgcns.wcs.kurly.service.impl;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.wcs.kurly.dto.RegionMasterData;
import com.lgcns.wcs.kurly.dto.RegionMasterHeaderData;
import com.lgcns.wcs.kurly.repository.RegionMasterRepository;
import com.lgcns.wcs.kurly.service.RegionMasterService;
import com.lgcns.wcs.kurly.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @작성일 : 2020. 08. 11.
 * @작성자 : jooni
 * @변경이력 : 2020. 08. 11. 최초작성
 * @설명 : region master ServiceImpl
 */
@Slf4j
@Service
public class RegionMasterServiceImpl implements RegionMasterService {

	@Autowired
	RegionMasterRepository regionMasterRepository;

	@Value("${wms.regionMasterUrl}")
	private String REGION_MASTER_URL;

	/**
	 *
	 * @Method Name : insertRegionMasterList
	 * @작성일 : 2021. 01. 18.
	 * @작성자 : jooni
	 * @변경이력 : 2021. 01. 18. 최초작성
	 * @Method 설명 : 권역정보 URL 호출 후 수신
	 */
	public RegionMasterHeaderData getRegionMaster() {

		String result = "";
		String inputUrl = REGION_MASTER_URL;
		String errorMessage = "";
		String errorYn = "N";
		RegionMasterHeaderData reqData = new RegionMasterHeaderData();
		try
		{
			String method = "GET";
			result = HttpUtil.getUrlToJson(inputUrl, "", method);
			
			//##2021.02.14  결과값이 없을 경우 처리하지 않고 에러 메세지에 값을 넣어줌
			if(!"".equals(result)) {
				ObjectMapper mapper = new ObjectMapper();
				reqData = mapper.readValue(result, RegionMasterHeaderData.class);
			} else {
				reqData.setError_message("Result No Data ");
			}
			
		} catch(Exception e) {
			//##2021.02.14 HttpUtil.getUrlToJson throw 된 오류를 처리하기 위해 설정
			errorMessage = e.toString();
			errorYn = "Y";
			e.printStackTrace();
		} finally {
			//##2021.02.14  에러이면 오류 코드와 오류 메세지를 넣어줌
			if("Y".equals(errorYn)) {
				reqData.setError_code(500);
				reqData.setError_message(errorMessage);
			}
		}
		
		return reqData;
	}

	/**
	 * 
	 * @Method Name : insertRegionMasterList
	 * @작성일 : 2021. 01. 18.
	 * @작성자 : jooni
	 * @변경이력 : 2021. 01. 18. 최초작성
	 * @Method 설명 : 권역정보 Merge
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void insertRegionMasterList(Map<String, Object> upListMap)   {
						
    	//RegionMaster insert
		regionMasterRepository.insertRegionMasterList(upListMap);
	}
}