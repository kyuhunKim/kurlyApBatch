package com.lgcns.wcs.kurly.dto.box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/*
 * 박스분할용 ordLine
 * */
@ToString
public class OrdInfoVO {

	private String shipOrderKey;
	private String warehouseKey;
	private String ownerKey;
	private String orderNo;
	private List<OrdLineVO> ordLines; //오더라인 리스트
	private SkuTypeMap skuMaster;
	private double ordCBM;
	private int splitSeqNum; //총 splitSeq 수
	private String boxType = "";
	private String boxSplitCheckYn = "Y";
	private String allocType = "";
	private String invoiceNo = "";  //송장번호
	
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

	public List<OrdLineVO> getOrdLines() {
		return ordLines;
	}

	public void setOrdLines(List<OrdLineVO> ordLines) {
		this.ordLines = ordLines;
	}

	public SkuTypeMap getSkuMaster() {
		return skuMaster;
	}

	public void setSkuMaster(SkuTypeMap skuMaster) {
		this.skuMaster = skuMaster;
	}

	public int getSplitSeqNum() {
		return splitSeqNum;
	}

	public void setSplitSeqNum(int splitSeqNum) {
		this.splitSeqNum = splitSeqNum;
	}

	public void setOrdCBM(double ordCBM) {
		this.ordCBM = ordCBM;
	}

	public void setBoxType(String inBoxType)
	{
		boxType = inBoxType;
	}
	
	public String getBoxType()
	{
		return boxType;
	}
	
	public OrdInfoVO(SkuTypeMap skuMaster)
	{
		ordLines = new ArrayList<OrdLineVO>();
		this.skuMaster = skuMaster;
		ordCBM = 0;
	}

	public OrdInfoVO(SkuTypeMap skuMaster, String warehouseKey)
	{
		ordLines = new ArrayList<OrdLineVO>();
		this.skuMaster = skuMaster;
		this.warehouseKey = warehouseKey;
		ordCBM = 0;
	}
	
	public OrdInfoVO()
	{
		ordLines = new ArrayList<OrdLineVO>();
		ordCBM = 0;
	}
	
	public List<OrdLineVO> getOrdList()
	{
		return this.ordLines;
	}
	
	public void addOrdLine(String skuCode, int ordQty) //상품코드, 주문 수량
	{
		OrdLineVO tempOrdLine;
		
		tempOrdLine = new OrdLineVO();
		tempOrdLine.setOrdLine(this.skuMaster, skuCode, ordQty);
		ordLines.add(tempOrdLine);
		
		ordCBM = 0;
		
		for(OrdLineVO itOrdLine : ordLines)
		{
			ordCBM += itOrdLine.getOrdLineCBM();	
		}
	}
	
	public void addOrdLine(OrdLineVO inOrdLine) //오더라인으로 입력 받았을 경우.
	{

		ordLines.add(inOrdLine);
		ordCBM = 0;
		for(OrdLineVO itOrdLine : ordLines)
		{
			ordCBM += itOrdLine.getOrdLineCBM();	
		} 
	}
	public void addOrdLine(String skuCode, int ordQty,
			String shipOrderKey, String warehouseKey, String ownerKey, 
			String orderNo, String shipOrderItemSeq) //상품코드, 주문 수량
	{
		OrdLineVO tempOrdLine;
		
		tempOrdLine = new OrdLineVO();
		tempOrdLine.setOrdLine(this.skuMaster, skuCode, ordQty,
				shipOrderKey,  warehouseKey,  ownerKey,  orderNo, shipOrderItemSeq);
		
		ordLines.add(tempOrdLine);
		
		ordCBM = 0;
		
		for(OrdLineVO itOrdLine : ordLines)
		{
			ordCBM += itOrdLine.getOrdLineCBM();	
		}
	}

	public void addOrdLine(String skuCode, int ordQty,
			String shipOrderKey, String warehouseKey, String ownerKey, 
			String orderNo, String shipOrderItemSeq,
			double skuDepth, double skuHeight, double skuWidth) //상품코드, 주문 수량
	{
		OrdLineVO tempOrdLine;
		
		tempOrdLine = new OrdLineVO();
		tempOrdLine.setOrdLine(skuCode, ordQty,
				shipOrderKey,  warehouseKey,  ownerKey,  orderNo, shipOrderItemSeq,
				skuDepth, skuHeight, skuWidth);
		
		ordLines.add(tempOrdLine);
		
		ordCBM = 0;
		
		for(OrdLineVO itOrdLine : ordLines)
		{
			ordCBM += itOrdLine.getOrdLineCBM();	
		}
	}
	public double getOrdCBM()
	{
		return ordCBM;
	}

	public int sampleInit()
	{
		//1번 오더라인
		this.addOrdLine("8801", 3);
		
		//2번 오더라인
		this.addOrdLine("8802", 2);

		//3번 오더라인
		this.addOrdLine("8803", 5);
		
		//4번 오더라인
		this.addOrdLine("8804", 4);
		
		//5번 오더라인
		this.addOrdLine("8805", 10);
		
		return ordLines.size();
	}
	

	public int runOrdSplit(double maxCellCBM)
	{
		return OrderSplitLogic.OrderSplit(this, skuMaster, maxCellCBM);
	}
//	public int runOrdSplit(double maxCellCBM)
//	{
//		int currentUpperBound = (int)(ordCBM / maxCellCBM) + 1; //초기 어퍼 바운드. (나눈 수치 + 1);
//		boolean assignedCheck = false;
//		//오더라인 정렬 --> CBM DESC 순으로..
//		Collections.sort(ordLines, new CompareOrdLinesDesc());
//
//		if(ordCBM > maxCellCBM)
//		{
//			//1. 변수 선언
//			List<OrdInfoVO> tempOrdAssign = new ArrayList<OrdInfoVO>();
//			OrdInfoVO tempOrdInfo;
//			
//			do
//			{
//				assignedCheck = true;
//				//2.Bean 생성
//				for(int i=0;i<currentUpperBound;i++)
//				{
//					tempOrdInfo = new OrdInfoVO(skuMaster);
//					tempOrdAssign.add(tempOrdInfo);
//				}
//				
//				//3. Line 별 assign
//				for(OrdLineVO itOrdLine : ordLines)
//				{
//					//1. 가장 CBM이 적은 Bean 선택
//					double minCBM = 999999999.;
//					int minIndex = -1;
//					for(int i=0;i<currentUpperBound;i++)
//					{
//						if(tempOrdAssign.get(i).getOrdCBM() < minCBM)
//						{
//							minCBM = tempOrdAssign.get(i).getOrdCBM();
//							minIndex = i;
//						}
//					}
//					
//					tempOrdAssign.get(minIndex).addOrdLine(itOrdLine);
//					if(tempOrdAssign.get(minIndex).getOrdCBM() > maxCellCBM)
//					{
//						assignedCheck = false;
//						break;
//					}
//				}
//				
//				if(!assignedCheck)
//				{	
//					//메모리 삭제.
//					tempOrdAssign.removeAll(tempOrdAssign);
//					currentUpperBound ++;
//					continue;
//				}
//			}while(!assignedCheck);
//			
//			//assign 정보 대로 기록
//			for(int i=0;i<currentUpperBound;i++)
//			{
//				
//				for(OrdLineVO itOrdLine : tempOrdAssign.get(i).getOrdList())
//				{
//					itOrdLine.setSplitSeq(i+1);
//				}
//				this.splitSeqNum = i + 1;
//			}
//			//메모리 삭제.
//			for(OrdInfoVO itOrd : tempOrdAssign)
//			{
//				itOrd.getOrdList().removeAll(itOrd.getOrdList());
//			}
//			tempOrdAssign.removeAll(tempOrdAssign);
//		}
//		else
//		{
//			for(OrdLineVO itOrdLine : ordLines)
//			{
//				itOrdLine.setSplitSeq(1);
//			}
//			this.splitSeqNum = 1;
//		}
//		
//		return 1;
//	}
	
	public int getSplitNum(){
		return splitSeqNum;
	}

	public String getBoxSplitCheckYn() {
		return boxSplitCheckYn;
	}

	public void setBoxSplitCheckYn(String boxSplitCheckYn) {
		this.boxSplitCheckYn = boxSplitCheckYn;
	}

	public String getAllocType() {
		return allocType;
	}

	public void setAllocType(String allocType) {
		this.allocType = allocType;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
}