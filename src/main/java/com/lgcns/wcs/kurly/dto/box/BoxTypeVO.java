package com.lgcns.wcs.kurly.dto.box;

public class BoxTypeVO {

	private String warehouseKey;
	private String logicCd; // 예제 : 1번 또는 2번
	private double boxMinCBM;
	private double boxMaxCBM;
	public String getWarehouseKey() {
		return warehouseKey;
	}
	public void setWarehouseKey(String warehouseKey) {
		this.warehouseKey = warehouseKey;
	}
	public String getLogicCd() {
		return logicCd;
	}
	public void setLogicCd(String logicCd) {
		this.logicCd = logicCd;
	}
	public double getBoxMinCBM() {
		return boxMinCBM;
	}
	public void setBoxMinCBM(double boxMinCBM) {
		this.boxMinCBM = boxMinCBM;
	}
	public double getBoxMaxCBM() {
		return boxMaxCBM;
	}
	public void setBoxMaxCBM(double boxMaxCBM) {
		this.boxMaxCBM = boxMaxCBM;
	}

	private double boxWidth;
	private double boxHeight;
	private double boxDepth;
	private String boxTypeCD;
	private String boxTypeName;
	private String boxShape;
	public double getBoxWidth() {
		return boxWidth;
	}
	public void setBoxWidth(double boxWidth) {
		this.boxWidth = boxWidth;
	}
	public double getBoxHeight() {
		return boxHeight;
	}
	public void setBoxHeight(double boxHeight) {
		this.boxHeight = boxHeight;
	}
	public double getBoxDepth() {
		return boxDepth;
	}
	public void setBoxDepth(double boxDepth) {
		this.boxDepth = boxDepth;
	}
	public String getBoxTypeCD() {
		return boxTypeCD;
	}
	public void setBoxTypeCD(String boxTypeCD) {
		this.boxTypeCD = boxTypeCD;
	}
	public String getBoxTypeName() {
		return boxTypeName;
	}
	public void setBoxTypeName(String boxTypeName) {
		this.boxTypeName = boxTypeName;
	}
	public String getBoxShape() {
		return boxShape;
	}
	public void setBoxShape(String boxShape) {
		this.boxShape = boxShape;
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
		double boxFirst = max(this.boxWidth, max(this.boxHeight, this.boxDepth));
		double boxThird = min(this.boxWidth, min(this.boxHeight, this.boxDepth));
		double boxSecond = this.boxWidth + this.boxHeight + this.boxDepth;
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
		return this.boxWidth * this.boxHeight * this.boxDepth;
	}
}