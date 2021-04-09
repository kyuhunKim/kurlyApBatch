package com.lgcns.wcs.kurly.dto.box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderSplitLogic {
	
	public static int OrderSplit(OrdInfoVO ord, SkuTypeMap skuMaster, double maxCellCBM)
	{
		int currentUpperBound = (int)(ord.getOrdCBM() / maxCellCBM) + 1; //초기 어퍼 바운드. (나눈 수치 + 1);
		boolean assignedCheck = false;
		//오더라인 정렬 --> CBM DESC 순으로..
		Collections.sort(ord.getOrdLines(), new CompareOrdLinesDesc());
		
		if(ord.getOrdCBM() > maxCellCBM)
		{
			//1. 변수 선언
			List<OrdInfoVO> tempOrdAssign = new ArrayList<OrdInfoVO>();
			OrdInfoVO tempOrdInfo;
			
			do
			{
				assignedCheck = true;
				//2.Bean 생성
				for(int i=0;i<currentUpperBound;i++)
				{
					tempOrdInfo = new OrdInfoVO(skuMaster, ord.getWarehouseKey());
					tempOrdAssign.add(tempOrdInfo);
				}
				
				//3. Line 별 assign
				for(OrdLineVO itOrdLine : ord.getOrdLines())
				{
					//1. 가장 CBM이 적은 Bean 선택
					double minCBM = 999999999.;
					int minIndex = -1;
					for(int i=0;i<currentUpperBound;i++)
					{
						if(tempOrdAssign.get(i).getOrdCBM() < minCBM)
						{
							minCBM = tempOrdAssign.get(i).getOrdCBM();
							minIndex = i;
						}
					}
					
					tempOrdAssign.get(minIndex).addOrdLine(itOrdLine);
					if(tempOrdAssign.get(minIndex).getOrdCBM() > maxCellCBM)
					{
						assignedCheck = false;
						break;
					}
				}
				
				if(!assignedCheck)
				{	
					//메모리 삭제.
					tempOrdAssign.removeAll(tempOrdAssign);
					currentUpperBound ++;
					continue;
				}
			}while(!assignedCheck);
			
			//assign 정보 대로 기록
			for(int i=0;i<currentUpperBound;i++)
			{
				
				for(OrdLineVO itOrdLine : tempOrdAssign.get(i).getOrdList())
				{
					itOrdLine.setSplitSeq(i+1);
				}
				ord.setSplitSeqNum(i + 1);
			}
			//메모리 삭제.
			for(OrdInfoVO itOrd : tempOrdAssign)
			{
				itOrd.getOrdList().removeAll(itOrd.getOrdList());
			}
			tempOrdAssign.removeAll(tempOrdAssign);
		}
		else
		{
			for(OrdLineVO itOrdLine : ord.getOrdLines())
			{
				itOrdLine.setSplitSeq(1);
			}
			ord.setSplitSeqNum(1);
		}
		
		return 1;
	}
}
