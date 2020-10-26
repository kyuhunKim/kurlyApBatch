package com.lgcns.wcs.kurly.dto.box;

public class BoxTypeVO {

	private String BoxTypeCD;
	private String BoxTypeName;
	private String BoxShape;
	private String warehouseKey;
	private double BoxDepth;
	private double BoxWidth;
	private double BoxHeight;

	private double fillRate;
	
	public String getWarehouseKey() {
		return warehouseKey;
	}
	public void setWarehouseKey(String warehouseKey) {
		this.warehouseKey = warehouseKey;
	}
	public double getBoxWidth() {
		return BoxWidth;
	}
	public void setBoxWidth(double boxWidth) {
		BoxWidth = boxWidth;
	}
	public double getBoxHeight() {
		return BoxHeight;
	}
	public void setBoxHeight(double boxHeight) {
		BoxHeight = boxHeight;
	}
	public double getBoxDepth() {
		return BoxDepth;
	}
	public void setBoxDepth(double boxDepth) {
		BoxDepth = boxDepth;
	}
	public String getBoxTypeCD() {
		return BoxTypeCD;
	}
	public void setBoxTypeCD(String boxTypeCD) {
		BoxTypeCD = boxTypeCD;
	}
	public String getBoxTypeName() {
		return BoxTypeName;
	}
	public void setBoxTypeName(String boxTypeName) {
		BoxTypeName = boxTypeName;
	}
	public String getBoxShape() {
		return BoxShape;
	}
	public void setBoxShape(String boxShape) {
		BoxShape = boxShape;
	}
	
	double max(double a, double b)
	{
		if(a>b)
			return a;
		return b;
	}
	double min(double a, double b)
	{
		if(a<b)
			return a;
		return b;
	}
	
	public boolean checkBox(double first, double second, double third)
	{
		double boxFirst = max(this.BoxWidth, max(this.BoxHeight, this.BoxDepth));
		double boxThird = min(this.BoxWidth, min(this.BoxHeight, this.BoxDepth));
		double boxSecond = BoxWidth + BoxHeight + BoxDepth;
		boxSecond -= boxFirst;
		boxSecond -= boxThird;
		
		if(first < boxFirst && second < boxSecond && third < boxThird)
		{
			return true;
		}
		return false;
	}
	
	public double getBoxCBM()
	{
		return this.BoxWidth * this.BoxHeight * this.BoxDepth;
	}
	
	public double getFillRate() {
		return fillRate;
	}
	public void setFillRate(double fillRate) {
		this.fillRate = fillRate;
	}
}