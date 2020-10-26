package com.lgcns.wcs.kurly.app;

import com.lgcns.wcs.kurly.dto.box.BoxTypeList;
import com.lgcns.wcs.kurly.dto.box.BoxTypeVO;
import com.lgcns.wcs.kurly.dto.box.OrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.OrdLineVO;

import lombok.extern.slf4j.Slf4j;

public class BoxRecommendApp {

	public BoxRecommendApp(OrdInfoVO ordList, BoxTypeList boxTypeMaster, double fillRate)
	{

		double minCBM = 999999999;
		String minBoxType = "";
		
		//step 1. 가능 박스 리스트 작성 & box추천.
		int ch = 1;
		int ableBoxNum = 0;
		double cbmSum;
		for(BoxTypeVO itBoxType : boxTypeMaster.getBoxMaster())
		{
			ch = 1;
			cbmSum = 0;
			for(OrdLineVO itOrdLine : ordList.getOrdList())
			{
				//WarehouseKey 같을 경우만 실행
				if(!itBoxType.getWarehouseKey().equals(itOrdLine.getWarehouseKey())) {
					continue;
				}
				cbmSum += itOrdLine.getOrdLineCBM();
				if(itBoxType.checkBox(itOrdLine.getFirst(), 
						              itOrdLine.getSecond(), 
						              itOrdLine.getThird()))
				{
					ch = 0;
				}
			}
			if(ch == 0 && itBoxType.getBoxCBM() * fillRate > cbmSum)
			{
				if(minCBM > itBoxType.getBoxCBM())
				{
					minCBM = itBoxType.getBoxCBM();
					minBoxType = itBoxType.getBoxTypeCD();
				}
				ableBoxNum ++ ;
			}
		}
		if(ableBoxNum == 0)
		{
			ordList.setBoxType("NoBox");
		}
		else
		{
			ordList.setBoxType(minBoxType);
		}
	}


	public BoxRecommendApp(OrdInfoVO ordList, BoxTypeList boxTypeMaster)
	{

		double minCBM = 999999999;
		String minBoxType = "";
		
		//step 1. 가능 박스 리스트 작성 & box추천.
		int ch = 1;
		int ableBoxNum = 0;
		double cbmSum;
		for(BoxTypeVO itBoxType : boxTypeMaster.getBoxMaster())
		{
			ch = 1;
			cbmSum = 0;
			for(OrdLineVO itOrdLine : ordList.getOrdList())
			{
				//WarehouseKey 같을 경우만 실행
				if(!itBoxType.getWarehouseKey().equals(itOrdLine.getWarehouseKey())) {
					continue;
				}
				cbmSum += itOrdLine.getOrdLineCBM();
				if(itBoxType.checkBox(itOrdLine.getFirst(), 
						              itOrdLine.getSecond(), 
						              itOrdLine.getThird()))
				{
					ch = 0;
				}
			}
			if(ch == 0 && itBoxType.getBoxCBM() * itBoxType.getFillRate() > cbmSum)
			{
				if(minCBM > itBoxType.getBoxCBM())
				{
					minCBM = itBoxType.getBoxCBM();
					minBoxType = itBoxType.getBoxTypeCD();
				}
				ableBoxNum ++ ;
			}
		}
		if(ableBoxNum == 0)
		{
			ordList.setBoxType("NoBox");
		}
		else
		{
			ordList.setBoxType(minBoxType);
		}
	}
	
}
