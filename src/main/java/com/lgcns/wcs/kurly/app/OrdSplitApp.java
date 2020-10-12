package com.lgcns.wcs.kurly.app;

import java.util.List;

import com.lgcns.wcs.kurly.dto.box.CellTypeList;
import com.lgcns.wcs.kurly.dto.box.CellTypeVO;
import com.lgcns.wcs.kurly.dto.box.OrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.OrdLineVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdSplitApp {
	private OrdInfoVO ordInfoList;
	private CellTypeList cellTypeList;
	
	public OrdSplitApp(OrdInfoVO inOrder, CellTypeList inCellType)
	{
		setOrderInfo(inOrder);
		setCellTypeList(inCellType);
	}

	public void setOrderInfo(OrdInfoVO inOrder)
	{
		ordInfoList = inOrder;
	}
	
	public void setCellTypeList(CellTypeList inCellType)
	{
		cellTypeList = inCellType;
	}
	
	private boolean cellCheckBySku(OrdLineVO ordLine, CellTypeVO cellType)
	{
		if(ordLine.getFirst() <= cellType.getFirst()
		   && ordLine.getSecond() <= cellType.getSecond()
		   && ordLine.getThird() <= cellType.getThird())
			return true;
		return false;
	}
	
	
	private double min(double a, double b)
	{
		if(a<b)
			return a;
		return b;
	}
	
	public int runOrdSplit()
	{
		//코드 작성
		
		double cellFillRate = 1.0;
		
		//1. sku 할당 가능 셀 리스트 작성
		List<CellTypeVO> cellList = cellTypeList.getCellList();
		List<OrdLineVO> ordList = ordInfoList.getOrdList();

		double minCellCBM = 99999999;
		for(CellTypeVO itCellType : cellList)
		{
			boolean check = true;
			for(OrdLineVO itOrdLine : ordList)
			{

				//WarehouseKey 같을 경우만 실행
				if(!itCellType.getWarehouseKey().equals(itOrdLine.getWarehouseKey())) {
					continue;
				}

				if(cellCheckBySku(itOrdLine, itCellType) != true)
				{
					check = false;
					break;
				}
			}

			if(check)
			{
				minCellCBM = min(minCellCBM, itCellType.getCellCbm());
			}

		} // 여기까지. ableCellList에 가능 셀만 꽂힘.
		
		if(minCellCBM == 99999999)
		{
			//이형 sku 가 들어옴
			//Exception 처리 요망.
			log.info("OrdSplitApp : 실패");
			return -1;		
		}

		//오더라인 분해.
		//라인 분할여부 결정.
		int i = 0;
		while(ordList.size() > i) {
			OrdLineVO itOrdLine = ordList.get(i);
			if(itOrdLine.getOrdLineCBM() > minCellCBM * cellFillRate) // 적치율 곱해야함, default 1.0
			{
				//라인 분해 해야 함..
				int tempTotalOrdQty = itOrdLine.getOrdQty();
				int tempMaxOrderQty = (int)(minCellCBM * cellFillRate / itOrdLine.getSkuCBM()); // (셀 크기/sku 크기)를 버림한값 = 최대 들어가는 양
				
				itOrdLine.setOrdQty(tempMaxOrderQty); //수량 변경
				
				//새로운 오더라인 생성 및 추가
				tempTotalOrdQty = tempTotalOrdQty - tempMaxOrderQty;
//				ordInfoList.addOrdLine(itOrdLine.getSkuCode(), tempTotalOrdQty);
//				ordInfoList.addOrdLine(itOrdLine.getSkuCode(), tempTotalOrdQty,
//						itOrdLine.getShipOrderKey(), itOrdLine.getWarehouseKey(),
//						itOrdLine.getOwnerKey(), itOrdLine.getOrderNo(),
//						itOrdLine.getShipOrderItemSeq());
				ordInfoList.addOrdLine(itOrdLine.getSkuCode(), tempTotalOrdQty,
						itOrdLine.getShipOrderKey(), itOrdLine.getWarehouseKey(),
						itOrdLine.getOwnerKey(), itOrdLine.getOrderNo(), itOrdLine.getShipOrderItemSeq(),
						itOrdLine.getSkuDepth(), itOrdLine.getSkuHeight(), itOrdLine.getSkuWidth() );
			}
			i++;
		}

		//분할 (셀 기반, Bin Packing, BFD)
		return ordInfoList.runOrdSplit((int)(minCellCBM * cellFillRate));
	}
	
	
}
