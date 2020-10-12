package com.lgcns.wcs.kurly.dto.box;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lgcns.wcs.kurly.service.BoxRecomService;

import java.util.ArrayList;;

public class CellTypeList {

    @Autowired
    BoxRecomService boxRecomService;
    
	List<CellTypeVO> cellTypeMaster;
	
	
	public CellTypeList()
	{
		cellTypeMaster = new ArrayList<CellTypeVO>();
	}

	//List Type을 넣어서 생성할 수 있는 생성자.. (20200806)
	public CellTypeList(List<CellTypeVO> inList)
	{
		cellTypeMaster = new ArrayList<CellTypeVO>();
		for(CellTypeVO it : inList)
		{
			addCellType(it);
		}
	}
	public int addCellType(CellTypeVO inCellType)
	{
		inCellType.calCellInfo();
		cellTypeMaster.add(inCellType);
		return cellTypeMaster.size();
	}
	
	public List<CellTypeVO> getCellList()
	{
		return cellTypeMaster;
	}
	public int getInitCellType(List<CellTypeVO> cellTypeList)
	{
		CellTypeVO tempCellType;


		for(CellTypeVO cellTypeVO : cellTypeList )
		{
			tempCellType = new CellTypeVO();
			tempCellType.setCellCd(cellTypeVO.getCellCd());
			tempCellType.setWarehouseKey(cellTypeVO.getWarehouseKey());
			tempCellType.setCellWidth(cellTypeVO.getCellWidth());
			tempCellType.setCellHeight(cellTypeVO.getCellHeight());
			tempCellType.setCellDepth(cellTypeVO.getCellDepth());
			tempCellType.calCellInfo();
			cellTypeMaster.add(tempCellType);
		}
		
		return cellTypeMaster.size();
	}
	public int sampleInitCellType()
	{
		CellTypeVO tempCellType;
		
		
		//1. cell 1번 타입
		tempCellType = new CellTypeVO();
		tempCellType.setWarehouseKey("GGM1");
		tempCellType.setCellWidth(600.0);
		tempCellType.setCellHeight(300.0);
		tempCellType.setCellDepth(200.0);
		tempCellType.calCellInfo();
		cellTypeMaster.add(tempCellType);
		

		//2. cell 2번 타입
		tempCellType = new CellTypeVO();
		tempCellType.setWarehouseKey("GGM1");
		tempCellType.setCellWidth(300.0);
		tempCellType.setCellHeight(300.0);
		tempCellType.setCellDepth(200.0);
		tempCellType.calCellInfo();
		cellTypeMaster.add(tempCellType);
		
		
		return cellTypeMaster.size();
	}
	
}
