package com.lgcns.wcs.kurly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.RegionMasterData;
import com.lgcns.wcs.kurly.dto.RegionMasterDetailData;
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
	
	public String insertRegionMaster() {

		String result = "";
		String inputUrl = "https://tms.api.dev.kurly.com/tms/v1/common/wms/region/items"; 
		StringBuffer insertData = new StringBuffer();
		try
		{
			String method = "GET";
			result = HttpUtil.getUrlToJson(inputUrl, "", method);

			RegionMasterHeaderData reqData = new RegionMasterHeaderData();
			
			ObjectMapper mapper = new ObjectMapper();
			reqData = mapper.readValue(result, RegionMasterHeaderData.class);
			if(reqData.getError_code() == 0 ) {
				int vvv =0 ;
		    	for(RegionMasterDetailData master : reqData.getData() ) {
		    		
//		    		if(vvv > 1) break;  //테스트용
		    		log.info("master='{}'", master.toString());
		    		
		    		if(KurlyConstants.DEFAULT_REGION_CENTERCODE.equals(master.getCenter_code())) {
		    			
		    			RegionMasterData rMaster = new RegionMasterData();
		    			rMaster.setCoCd("MK");
		    			rMaster.setRgnCd(master.getRegncd());
		    			rMaster.setRgnNm(master.getRegnnm());
		    			rMaster.setRegionGroupCode(master.getRegnky_group_code());
		    			rMaster.setDeliveryRound(master.getDelivery_round());
		    			rMaster.setRgnKy(master.getRegnky());
		    			rMaster.setUseYn(KurlyConstants.STATUS_Y);
		    			rMaster.setRegId(KurlyConstants.DEFAULT_USERID);
		    			rMaster.setUpdId(KurlyConstants.DEFAULT_USERID);
		    			
		    			regionMasterRepository.insertRegionMaster(rMaster);
		    			
		    			insertData.append(rMaster);
		    			vvv++;	
		    		}
		    	}

	    		log.info("insert Data count = "+ vvv);
	    		
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return insertData.toString();
		
	}

}