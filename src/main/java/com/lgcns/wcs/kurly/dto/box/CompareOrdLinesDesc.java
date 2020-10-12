package com.lgcns.wcs.kurly.dto.box;

import java.util.Comparator;

public class CompareOrdLinesDesc implements Comparator<OrdLineVO>{

	@Override
	public int compare(OrdLineVO o1, OrdLineVO o2) {
		// TODO Auto-generated method stub
		if(o1.getOrdLineCBM() < o2.getOrdLineCBM())
			return 1;
		else if(o1.getOrdLineCBM() > o2.getOrdLineCBM())
			return -1;
		return 0;
	}
	

}
