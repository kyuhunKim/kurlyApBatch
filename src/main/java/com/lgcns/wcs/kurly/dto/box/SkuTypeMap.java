package com.lgcns.wcs.kurly.dto.box;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lgcns.wcs.kurly.service.BoxRecomService;

public class SkuTypeMap {
    
    @Autowired
    BoxRecomService boxRecomService;
	
	private Map<String, SkuTypeVO> skuMaster;
	
	public SkuTypeMap()
	{
		skuMaster = new HashMap<String, SkuTypeVO>();
	}
	
	//List Type을 넣어서 생성할 수 있는 생성자.. (20200806)
	public SkuTypeMap(List<SkuTypeVO> inList)
	{
		skuMaster = new HashMap<String, SkuTypeVO>();
		for(SkuTypeVO it : inList)
		{
			skuMaster.put(it.getSkuCode(), it);
		}
	}
	
	public int addMap(SkuTypeVO in)
	{
		String skuCD = in.getSkuCode();
		
		if(skuMaster.containsKey(skuCD))
		{
			return -1; //실패, 사유 : 중복키
		}
		
		skuMaster.put(skuCD, in);
		
		return skuMaster.size();
	}
	
	public SkuTypeVO getSkuTypeVO(String inSkuCD)
	{
		if(skuMaster.containsKey(inSkuCD) == false)
		{
			return null;
		}
		
		return skuMaster.get(inSkuCD);
	}
	
	public int getSkuNum()
	{
		return this.skuMaster.size();
	}
	
	public double getSkuCBM(String inSkuCD)
	{
		if(skuMaster.containsKey(inSkuCD) == false)
		{
			return -1; //SKU CD가 마스터에 없음.
		}
		
		return skuMaster.get(inSkuCD).getSkuCBM();
		
	}
	public int getInit(List<SkuTypeVO> skuTypeList)
	{
		
		SkuTypeVO tempSkuType;
		for(SkuTypeVO skuTypeVO : skuTypeList )
		{
			tempSkuType = new SkuTypeVO();
			tempSkuType.setSkuCode(skuTypeVO.getSkuCode());
			tempSkuType.setSkuDepth(skuTypeVO.getSkuDepth());
			tempSkuType.setSkuHeight(skuTypeVO.getSkuHeight());
			tempSkuType.setSkuWidth(skuTypeVO.getSkuWidth());
			this.addMap(tempSkuType);
		}

		return this.getSkuNum();
	}
	
	public int sampleInit()
	{
		SkuTypeVO tempSkuType;
		
		//1번 sku
		tempSkuType = new SkuTypeVO();
		tempSkuType.setSkuCode("8801");
		tempSkuType.setSkuDepth(60.000);
		tempSkuType.setSkuHeight(60.000);
		tempSkuType.setSkuWidth(120.000);
		this.addMap(tempSkuType);
		
		//2번 sku
		tempSkuType = new SkuTypeVO();
		tempSkuType.setSkuCode("8802");
		tempSkuType.setSkuDepth(215.000);
		tempSkuType.setSkuHeight(200.000);
		tempSkuType.setSkuWidth(70.000);
		this.addMap(tempSkuType);
		
		//3번 sku
		tempSkuType = new SkuTypeVO();
		tempSkuType.setSkuCode("8803");
		tempSkuType.setSkuDepth(220.000);
		tempSkuType.setSkuHeight(220.000);
		tempSkuType.setSkuWidth(50.000);
		this.addMap(tempSkuType);		

		//4번 sku
		tempSkuType = new SkuTypeVO();
		tempSkuType.setSkuCode("8804");
		tempSkuType.setSkuDepth(140.000);
		tempSkuType.setSkuHeight(250.000);
		tempSkuType.setSkuWidth(20.000);
		this.addMap(tempSkuType);		
		
		//4번 sku
		tempSkuType = new SkuTypeVO();
		tempSkuType.setSkuCode("8805");
		tempSkuType.setSkuDepth(71.000);
		tempSkuType.setSkuHeight(71.000);
		tempSkuType.setSkuWidth(233.000);
		this.addMap(tempSkuType);		
				
		return this.getSkuNum();
	}
}
