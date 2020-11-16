package com.lgcns.wcs.kurly.dto.box;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lgcns.wcs.kurly.service.BoxRecomService;;

public class BoxTypeList {

    @Autowired
    BoxRecomService boxRecomService;
    
	List<BoxTypeVO> boxTypeMaster;
	
	public BoxTypeList()
	{
		boxTypeMaster = new ArrayList<BoxTypeVO>();
	}

	//List Type을 넣어서 생성할 수 있는 생성자.. (20200806)
	public BoxTypeList(List<BoxTypeVO> inList)
	{
		boxTypeMaster = new ArrayList<BoxTypeVO>();
		for(BoxTypeVO it : inList)
		{
			addBoxType(it);
		}
	}
	
	public int addBoxType(BoxTypeVO inBoxType)
	{
		boxTypeMaster.add(inBoxType);
		return boxTypeMaster.size();
	}
	
	public List<BoxTypeVO> getBoxMaster()
	{
		return boxTypeMaster;
	}

	public int getInitBoxMaster(List<BoxTypeVO> boxTypeList)
	{
		BoxTypeVO tempBoxType;
		

		for(BoxTypeVO boxTypeVO : boxTypeList )
		{
			tempBoxType = new BoxTypeVO();
			tempBoxType.setBoxTypeCD(boxTypeVO.getBoxTypeCD());
			tempBoxType.setBoxTypeName(boxTypeVO.getBoxTypeName());
			tempBoxType.setBoxShape(boxTypeVO.getBoxShape());
			tempBoxType.setBoxWidth(boxTypeVO.getBoxWidth());
			tempBoxType.setBoxDepth(boxTypeVO.getBoxDepth());
			tempBoxType.setBoxHeight(boxTypeVO.getBoxHeight());
			tempBoxType.setWarehouseKey(boxTypeVO.getWarehouseKey());
			tempBoxType.setLogicCd(boxTypeVO.getLogicCd());
			tempBoxType.setBoxMinCBM(boxTypeVO.getBoxMinCBM());
			tempBoxType.setBoxMaxCBM(boxTypeVO.getBoxMaxCBM());
			addBoxType(tempBoxType);
		}

		return this.boxTypeMaster.size();
	}

	public String getMaxBox(List<BoxTypeVO> boxTypeList, String wareHouseKey)
	{
		String boxTypeCD = "";
		//wareHouseKey 없을 경우 "" 으로 리턴
		if(wareHouseKey == null || wareHouseKey.equals("")) {
			return "";
		}
		
		for(BoxTypeVO boxTypeVO : boxTypeList )
		{
			//wareHouseKey 동일값일때만 처리
			if(wareHouseKey.equals(boxTypeVO.getWarehouseKey())) {
				boxTypeCD = boxTypeVO.getBoxTypeCD();
				break;
			}
		}

		return boxTypeCD;
	}
	
	public int sampleInitBoxMaster()
	{
		BoxTypeVO tempBoxType;
		
		//4호 박스
		tempBoxType = new BoxTypeVO();
		tempBoxType.setBoxTypeCD("M4");
		tempBoxType.setBoxTypeName("냉장박스 4호");
		tempBoxType.setBoxShape("A1");
		tempBoxType.setBoxWidth(220.0);
		tempBoxType.setBoxDepth(160.0);
		tempBoxType.setBoxHeight(150.5);
		tempBoxType.setWarehouseKey("GGM1");
		tempBoxType.setLogicCd("1");
		tempBoxType.setBoxMinCBM(0);
		tempBoxType.setBoxMaxCBM(4092000);
		addBoxType(tempBoxType);
		
		//7호 박스
		tempBoxType = new BoxTypeVO();
		tempBoxType.setBoxTypeCD("M7");
		tempBoxType.setBoxTypeName("냉장박스 7호");
		tempBoxType.setBoxShape("A1");
		tempBoxType.setBoxWidth(270.0);
		tempBoxType.setBoxDepth(180.0);
		tempBoxType.setBoxHeight(180.0);
		tempBoxType.setWarehouseKey("GGM1");
		tempBoxType.setLogicCd("1");
		tempBoxType.setBoxMinCBM(4092000);
		tempBoxType.setBoxMaxCBM(6561000);
		addBoxType(tempBoxType);
		
		//11호 박스
		tempBoxType = new BoxTypeVO();
		tempBoxType.setBoxTypeCD("M11");
		tempBoxType.setBoxTypeName("냉장박스 11호");
		tempBoxType.setBoxShape("A1");
		tempBoxType.setBoxWidth(290.0);
		tempBoxType.setBoxDepth(310.0);
		tempBoxType.setBoxHeight(150.5);
		tempBoxType.setWarehouseKey("GGM1");
		tempBoxType.setLogicCd("1");
		tempBoxType.setBoxMinCBM(6561000);
		tempBoxType.setBoxMaxCBM(10450875);
		addBoxType(tempBoxType);
		
		//16호 박스
		tempBoxType = new BoxTypeVO();
		tempBoxType.setBoxTypeCD("M16");
		tempBoxType.setBoxTypeName("냉장박스 16호");
		tempBoxType.setBoxShape("A1");
		tempBoxType.setBoxWidth(350.0);
		tempBoxType.setBoxDepth(250.5);
		tempBoxType.setBoxHeight(230.0);
		tempBoxType.setWarehouseKey("GGM1");
		tempBoxType.setLogicCd("1");
		tempBoxType.setBoxMinCBM(10450875);
		tempBoxType.setBoxMaxCBM(16422000);
		addBoxType(tempBoxType);
		
		
		//35호 박스
		tempBoxType = new BoxTypeVO();
		tempBoxType.setBoxTypeCD("M35");
		tempBoxType.setBoxTypeName("냉장박스 35호");
		tempBoxType.setBoxShape("A1");
		tempBoxType.setBoxWidth(450.0);
		tempBoxType.setBoxDepth(330.0);
		tempBoxType.setBoxHeight(270.5);
		tempBoxType.setWarehouseKey("GGM1");
		tempBoxType.setLogicCd("1");
		tempBoxType.setBoxMinCBM(16422000);
		tempBoxType.setBoxMaxCBM(32670000);
		addBoxType(tempBoxType);
		
		//완전큰박스
		tempBoxType = new BoxTypeVO();
		tempBoxType.setBoxTypeCD("M350");
		tempBoxType.setBoxTypeName("냉장박스 350호");
		tempBoxType.setBoxShape("A1");
		tempBoxType.setBoxWidth(750.0);
		tempBoxType.setBoxDepth(530.0);
		tempBoxType.setBoxHeight(470.5);
		tempBoxType.setWarehouseKey("GGM1");
		tempBoxType.setLogicCd("1");
		tempBoxType.setBoxMinCBM(32670000);
		tempBoxType.setBoxMaxCBM(186825000);
		addBoxType(tempBoxType);
		
		return this.boxTypeMaster.size();
	}
	
}

