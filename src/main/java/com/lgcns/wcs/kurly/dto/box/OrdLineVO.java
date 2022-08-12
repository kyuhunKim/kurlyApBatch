package com.lgcns.wcs.kurly.dto.box;

/*
 * 박스분할용 ordLine
 * */
public class OrdLineVO {

	private String shipOrderKey;
	private String warehouseKey;
	private String ownerKey;
	private String orderNo;
	private String shipOrderItemSeq;
	
	private String skuCode; //상품 코드
	private int ordQty; //주문 수량
	
	private double ordLineCBM; // 가장 큰 오더라인의 CBM
	private double ordLineLengthFirst; // 가장 큰 오더라인의 First 큰 값
	private double ordLineLengthSecond; //두번째로 큰 길이
	private double ordLineLengthThird; //세번째로 큰 길이
	private double skuCBM;
	private double lengthTotal;
	
	private double skuDepth = 0; //상품 깊이
	private double skuHeight = 0; //상품 높이
	private double skuWidth = 0; //상품 너비
	
	private int splitSeq; //스플릿 된 오더 번호
	
	
	private double max(double a, double b)
	{
		if (a>b)
			return a;
		return b;
	}
	
	private double min(double a, double b)
	{
		if(a<b)
			return a;
		return b;
	}
	
	public double getSkuCBM()
	{
		return skuCBM;
	}
	
	public OrdLineVO()
	{
		shipOrderKey = "";
		warehouseKey = "";
		ownerKey = "";
		orderNo = "";
		
		skuCode = "";
		shipOrderItemSeq = "";
		ordQty = 0;
		
		ordLineCBM = 0;
		ordLineLengthFirst = 0;
		ordLineLengthSecond = 0;
		ordLineLengthThird = 0;
	}
	
	public OrdLineVO(SkuTypeMap skuMaster)
	{
		shipOrderKey = "";
		warehouseKey = "";
		ownerKey = "";
		orderNo = "";
		shipOrderItemSeq = "";
		
		skuCode = "";
		ordQty = 0;
		
		ordLineCBM = 0;
		ordLineLengthFirst = 0;
		ordLineLengthSecond = 0;
		ordLineLengthThird = 0;
		
	}
	
	public int setOrdLine(SkuTypeMap skuMaster, String inSkuCd, int inOrdQty)
	{
		if(skuMaster.getSkuTypeVO(inSkuCd) == null)
		{
			return -1; // 실패.
		}

		this.ordQty = inOrdQty;
		this.skuCode = inSkuCd;
		
		SkuTypeVO inSkuType = skuMaster.getSkuTypeVO(inSkuCd);
		skuCBM = inSkuType.getSkuDepth() * inSkuType.getSkuHeight() * inSkuType.getSkuWidth();
		lengthTotal = inSkuType.getSkuDepth() + inSkuType.getSkuHeight() + inSkuType.getSkuWidth();
		ordLineLengthFirst = max(inSkuType.getSkuDepth(), max(inSkuType.getSkuHeight(), inSkuType.getSkuWidth()));
		ordLineLengthThird = min(inSkuType.getSkuDepth(), min(inSkuType.getSkuHeight(), inSkuType.getSkuWidth()));
		ordLineLengthSecond = lengthTotal - ordLineLengthFirst - ordLineLengthThird;
		
		this.ordLineCBM = ordLineLengthFirst * ordLineLengthThird * ordLineLengthSecond * inOrdQty;
		
		return 1; //성공
	}
	public int setOrdLine(SkuTypeMap skuMaster, String inSkuCd, int inOrdQty, 
			String shipOrderKey, String warehouseKey, String ownerKey, String orderNo, String shipOrderItemSeq)
	{
		if(skuMaster.getSkuTypeVO(inSkuCd) == null)
		{
			return -1; // 실패.
		}

		this.shipOrderKey = shipOrderKey;
		this.warehouseKey = warehouseKey;
		this.ownerKey = ownerKey;
		this.orderNo = orderNo;
		this.ordQty = inOrdQty;
		this.skuCode = inSkuCd;
		this.shipOrderItemSeq = shipOrderItemSeq;
		
		SkuTypeVO inSkuType = skuMaster.getSkuTypeVO(inSkuCd);
		skuCBM = inSkuType.getSkuDepth() * inSkuType.getSkuHeight() * inSkuType.getSkuWidth();
		lengthTotal = inSkuType.getSkuDepth() + inSkuType.getSkuHeight() + inSkuType.getSkuWidth();
		ordLineLengthFirst = max(inSkuType.getSkuDepth(), max(inSkuType.getSkuHeight(), inSkuType.getSkuWidth()));
		ordLineLengthThird = min(inSkuType.getSkuDepth(), min(inSkuType.getSkuHeight(), inSkuType.getSkuWidth()));
		ordLineLengthSecond = lengthTotal - ordLineLengthFirst - ordLineLengthThird;
		
		this.ordLineCBM = ordLineLengthFirst * ordLineLengthThird * ordLineLengthSecond * inOrdQty;
		
		return 1; //성공
	}

	public int setOrdLine(String inSkuCd, int inOrdQty, 
			String shipOrderKey, String warehouseKey, String ownerKey, 
			String orderNo, String shipOrderItemSeq,
			double skuDepth, double skuHeight, double skuWidth)
	{

		this.shipOrderKey = shipOrderKey;
		this.warehouseKey = warehouseKey;
		this.ownerKey = ownerKey;
		this.orderNo = orderNo;
		this.ordQty = inOrdQty;
		this.skuCode = inSkuCd;
		this.shipOrderItemSeq = shipOrderItemSeq;
		this.skuDepth = skuDepth;
		this.skuHeight = skuHeight;
		this.skuWidth = skuWidth;
		
		skuCBM = skuDepth * skuHeight * skuWidth;
		lengthTotal = skuDepth + skuHeight + skuWidth;
		ordLineLengthFirst = max(skuDepth, max(skuHeight, skuWidth));
		ordLineLengthThird = min(skuDepth, min(skuHeight, skuWidth));
		ordLineLengthSecond = lengthTotal - ordLineLengthFirst - ordLineLengthThird;
		
		this.ordLineCBM = ordLineLengthFirst * ordLineLengthThird * ordLineLengthSecond * inOrdQty;
		
		return 1; //성공
	}
	public double getOrdLineCBM()
	{
		return this.ordLineCBM;
	}
	
		
	public double getFirst()
	{
		return this.ordLineLengthFirst;
	}
	
	public double getSecond()
	{
		return this.ordLineLengthSecond;
	}
	
	public double getThird()
	{
		return this.ordLineLengthThird;
	}
	
	public void setSplitSeq(int splitSeq)
	{
		this.splitSeq = splitSeq;
	}
	
	public int getSplitSeq()
	{
		return this.splitSeq;
	}
		
	public int getOrdQty()
	{
		return this.ordQty;
	}
	
	public void setOrdQty(int inOrdQty)
	{
		this.ordQty = inOrdQty;
		this.ordLineCBM = this.skuCBM * inOrdQty;
	}

	public String getShipOrderKey() {
		return shipOrderKey;
	}

	public void setShipOrderKey(String shipOrderKey) {
		this.shipOrderKey = shipOrderKey;
	}

	public String getWarehouseKey() {
		return warehouseKey;
	}

	public void setWarehouseKey(String warehouseKey) {
		this.warehouseKey = warehouseKey;
	}

	public String getOwnerKey() {
		return ownerKey;
	}

	public void setOwnerKey(String ownerKey) {
		this.ownerKey = ownerKey;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public String getShipOrderItemSeq() {
		return shipOrderItemSeq;
	}

	public void setShipOrderItemSeq(String shipOrderItemSeq) {
		this.shipOrderItemSeq = shipOrderItemSeq;
	}

	public double getSkuDepth() {
		return skuDepth;
	}

	public void setSkuDepth(double skuDepth) {
		this.skuDepth = skuDepth;
	}

	public double getSkuHeight() {
		return skuHeight;
	}

	public void setSkuHeight(double skuHeight) {
		this.skuHeight = skuHeight;
	}

	public double getSkuWidth() {
		return skuWidth;
	}

	public void setSkuWidth(double skuWidth) {
		this.skuWidth = skuWidth;
	}
	
}