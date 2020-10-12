package com.lgcns.wcs.kurly.dto.box;

import java.util.ArrayList;
import java.util.List;

public class OrdInfoList {
	
	private List<OrdInfoVO> OrdList;
	
	public OrdInfoList()
	{
		OrdList = new ArrayList<OrdInfoVO>();
	}
	
	public OrdInfoList(List<OrdInfoVO> inList)
	{
		OrdList = inList;
	}
	
	public List<OrdInfoVO> getList()
	{
		return OrdList;
	}
	
	public void addOrd(OrdInfoVO inOrder)
	{
		OrdList.add(inOrder);
	}
}
