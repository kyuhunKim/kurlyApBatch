package com.lgcns.wcs.kurly.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.wcs.kurly.dto.SkuMasterData;

@Service
@Transactional
public interface SkuMasterService {

	/*
	 * 상품 마스터 등록
	 * */
	public void insertSkuMaster(SkuMasterData skuMasterData) ;

	public void insertSkuMasterList(List<SkuMasterData> skuMasterDataList) ;
}