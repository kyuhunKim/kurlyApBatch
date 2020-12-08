package com.lgcns.wcs.kurly.app;

import com.lgcns.wcs.kurly.dto.box.BoxTypeList;
import com.lgcns.wcs.kurly.dto.box.BoxTypeVO;
import com.lgcns.wcs.kurly.dto.box.OrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.OrdLineVO;

import lombok.extern.slf4j.Slf4j;

public class BoxRecommendApp {

	public BoxRecommendApp(OrdInfoVO ordList, BoxTypeList boxTypeMaster)
	{

		double minCBM = 999999999;
		String minBoxType = "";
		
		//step 1. 가능 박스 리스트 작성 & box추천.
		//박스CBM추천기준(LEN_CBM_MAX:길이체크,CBM_MIN_MAX:CBM체크)
		int ch = 1;
		int ableBoxNum = 0;
		for(BoxTypeVO itBoxType : boxTypeMaster.getBoxMaster())
		{
//			System.out.println(ordList.getWarehouseKey() + ", " + itBoxType.getWarehouseKey());
			
			//Warehouse check.
			if(!ordList.getWarehouseKey().equals(itBoxType.getWarehouseKey()))
				continue;
			
//			System.out.println("222222"+ordList.getWarehouseKey() + ", " + itBoxType.getWarehouseKey());
			
			ch = 1;
			if(itBoxType.getLogicCd().compareTo("LEN_CBM_MAX") == 0)
			{
				for(OrdLineVO itOrdLine : ordList.getOrdList())
				{
					//warehouse 체크 해서.. continue
					//logic 을 1로 갈지, 2로 갈지를 선택
					if(itBoxType.checkBox(itOrdLine.getFirst(), 
							              itOrdLine.getSecond(), 
							              itOrdLine.getThird()))
					{
						ch = 0;
					}
				}
				
				//System.out.println(ch + ", " + itBoxType.getBoxCBM() + ", " + fillRate + ", " + itBoxType.getBoxCBM() * fillRate + ", " + ordList.getOrdCBM());
				if(ch == 0 && itBoxType.getBoxMaxCBM() > ordList.getOrdCBM())
				{
					if(minCBM > itBoxType.getBoxMaxCBM())
					{
						minCBM = itBoxType.getBoxMaxCBM();
						minBoxType = itBoxType.getBoxTypeCD();
					}
					ableBoxNum ++ ;
				}
			}
			else
			{
				//2번 로직, minCBm, MaxCBM
				if(ordList.getOrdCBM() >= itBoxType.getBoxMinCBM() && ordList.getOrdCBM() <= itBoxType.getBoxMaxCBM())
				{
					if(minCBM > itBoxType.getBoxCBM())
					{
						minCBM = itBoxType.getBoxCBM();
						minBoxType = itBoxType.getBoxTypeCD();
					}
					ableBoxNum ++ ;
				}
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
