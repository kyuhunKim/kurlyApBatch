package com.lgcns.wcs.kurly.dto.box;

public class CellTypeVO {
	
	private String cellCd;
	private String cellName;
	private String warehouseKey;
	
	private double cellDepth;
	private double cellHeight;
	private double cellWidth;
	private double cellCBM;
	private double cellFirst;
	private double cellSecond;
	private double cellThird;
	private double cellTotal;
	
	public CellTypeVO()
	{
		cellDepth = 0;
		cellHeight = 0;
		cellWidth = 0;
	}
	
	public String getCellCd() {
		return cellCd;
	}


	public void setCellCd(String cellCd) {
		this.cellCd = cellCd;
	}


	public String getCellName() {
		return cellName;
	}


	public void setCellName(String cellName) {
		this.cellName = cellName;
	}


	public String getWarehouseKey() {
		return warehouseKey;
	}


	public void setWarehouseKey(String warehouseKey) {
		this.warehouseKey = warehouseKey;
	}


	public double getCellCBM() {
		return cellCBM;
	}

	public void setCellCBM(double cellCBM) {
		this.cellCBM = cellCBM;
	}

	public double getCellFirst() {
		return cellFirst;
	}

	public void setCellFirst(double cellFirst) {
		this.cellFirst = cellFirst;
	}

	public double getCellSecond() {
		return cellSecond;
	}

	public void setCellSecond(double cellSecond) {
		this.cellSecond = cellSecond;
	}

	public double getCellThird() {
		return cellThird;
	}

	public void setCellThird(double cellThird) {
		this.cellThird = cellThird;
	}

	public double getCellTotal() {
		return cellTotal;
	}

	public void setCellTotal(double cellTotal) {
		this.cellTotal = cellTotal;
	}

	public double getFirst()
	{
		return cellFirst;
	}
	
	public double getSecond()
	{
		return cellSecond;
	}
	
	public double getThird()
	{
		return cellThird;
	}
	

	private double max(double a, double b)
	{
		if(a>b)
			return a;
		return b;
	}
	
	private double min(double a, double b)
	{
		if(a<b)
			return a;
		return b;
	}
	

	public double getCellDepth() {
		return cellDepth;
	}

	public void setCellDepth(double cellDepth) {
		this.cellDepth = cellDepth;
	}

	public double getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(double cellHeight) {
		this.cellHeight = cellHeight;
	}

	public double getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(double cellWidth) {
		this.cellWidth = cellWidth;
	}
	
	public void calCellInfo()
	{
		cellTotal = cellDepth + cellHeight + cellWidth;
		cellCBM = cellDepth * cellHeight * cellWidth;
		
		cellFirst = max(cellDepth, max(cellHeight, cellWidth));
		cellThird = min(cellDepth, min(cellHeight, cellWidth));
		cellSecond = cellTotal - cellFirst - cellSecond;
	}
	
	
}