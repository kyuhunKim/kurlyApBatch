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
 * @설명 : region master   ServiceImpl
 */
@Slf4j
@Service
public class RegionMasterServiceImpl implements RegionMasterService {

	@Autowired
	RegionMasterRepository regionMasterRepository;

	@Value("${wms.regionMasterUrl}")
	private String REGION_MASTER_URL;
	
	public RegionMasterHeaderData insertRegionMaster() {

		String result = "";
		String inputUrl = REGION_MASTER_URL; 
		RegionMasterHeaderData reqData = new RegionMasterHeaderData();
		try
		{
			String method = "GET";
			result = HttpUtil.getUrlToJson(inputUrl, "", method);
			
			ObjectMapper mapper = new ObjectMapper();
			reqData = mapper.readValue(result, RegionMasterHeaderData.class);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return reqData;
		
	}
	/**
	 * 
	 * @Method Name : insertRegionMasterList
	 * @작성일 : 2021. 01. 18.
	 * @작성자 : jooni
	 * @변경이력 : 2021. 01. 18. 최초작성
	 * @Method 설명 : 토트 문제 처리용 피킹정보 연계  처리  update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void insertRegionMasterList(Map<String, Object> upListMap)   {
						
    	//RegionMaster insert
		regionMasterRepository.insertRegionMasterList(upListMap);
	}
	/**
	 * 
	 * @Method Name : insertRegionMaster
	 * @작성일 : 2021. 01. 18.
	 * @작성자 : jooni
	 * @변경이력 : 2021. 01. 18. 최초작성
	 * @Method 설명 : 토트 문제 처리용 피킹정보 연계  처리   update
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SQLException.class)
	public void insertRegionMaster(RegionMasterData data)   {
						
    	//RegionMaster insert
		regionMasterRepository.insertRegionMaster(data);
	}
}