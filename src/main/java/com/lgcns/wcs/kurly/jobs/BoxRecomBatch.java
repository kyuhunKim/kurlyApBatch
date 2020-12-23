package com.lgcns.wcs.kurly.jobs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import com.lgcns.wcs.kurly.app.BoxRecommendApp;
import com.lgcns.wcs.kurly.app.OrdSplitApp;
import com.lgcns.wcs.kurly.dto.KurlyConstants;
import com.lgcns.wcs.kurly.dto.LogBatchExec;
import com.lgcns.wcs.kurly.dto.box.BoxTypeList;
import com.lgcns.wcs.kurly.dto.box.BoxTypeVO;
import com.lgcns.wcs.kurly.dto.box.CellTypeList;
import com.lgcns.wcs.kurly.dto.box.CellTypeVO;
import com.lgcns.wcs.kurly.dto.box.OrdInfoList;
import com.lgcns.wcs.kurly.dto.box.OrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.OrdLineVO;
import com.lgcns.wcs.kurly.dto.box.SearchOrdInfoVO;
import com.lgcns.wcs.kurly.dto.box.SearchVO;
import com.lgcns.wcs.kurly.dto.box.WifShipmentDtlVO;
import com.lgcns.wcs.kurly.dto.box.WifShipmentVO;
import com.lgcns.wcs.kurly.service.BoxRecomService;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;
import com.lgcns.wcs.kurly.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Name : BoxRecomBatch
 * @작성일 : 2020. 07. 16.
 * @작성자 : jooni
 * @변경이력 : 2020. 07. 16. 최초작성
 * @Method 설명 : WCS 운송장 발행 정보  (WCS => WMS)
 */
@Slf4j
@Component
public class BoxRecomBatch  {

    @Autowired
    LogBatchExecService logBatchExecService;
    
    @Autowired
    LogApiStatusService logApiStatusService;
    
    @Autowired
    BoxRecomService boxRecomService;

    @Autowired
    DataSourceTransactionManager transactionManager;
    
    public void BoxRecomBatchTask() {
    	log.info("=======BoxRecomBatch start=======");
    	log.info("The current date  : " + LocalDateTime.now());
    	long apiRunTimeStart = 0;
		long apiRunTimeEnd   = 0;
		String apiRunTime    = "";
		
    	apiRunTimeStart = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
		int insertMaaxCount = 100;
    	
		Date startDate = Calendar.getInstance().getTime();
    	
    	try
    	{

    		/*
    	     * 이형상품은 오더분할 대상에서 제외 데이타 처리안하고 Y,N 이 아닌 T값으로 update
    	     * 오더분할된 경우 packBoxsplitYn 'Y'
    	     * 박스가 없을 경우 온도대에 해당하는 maxBox로 설정
    	     * 박스분할이 필요 없는 경우 max box로 설정(오더박스분할여부확인 'N'일 경우)
    	     * 
    	     * ##2020.11.10 
    	     *  인터페이스 받은 모든 오더를 TB_ORD_SHIPMENT_HDR,,DTL 테이블에 생성
    	     *  allocType : S, QDAS || memberGrade 가 0003 || MANUAL_PROC_YN : Y 인 경우 분할하지 않음
    	     *  오더분할 하지 않아도 박스추천 로직은 실행해서 추천된 박스가 없을 경우 온도대별 maxBox 값으로 설정
    	     * ##2020.11.19 
    	     * invoiceNo 뒤에 '-0001' 추가 오더분할이 됐을경우 분할된 순번으로 추가함
    	     *  invoiceNo 를 originInvoiceNo에 값이 없을 경우 invoiceNo값을 넣어줌
    	     * ##2020.12.08
    	     *  실행시간 개선 로직수정 
    	     * ##2020.12.18
    	     *  오더분할된 카운트를 TB_ORD_SHIPMENT_HDR 테이블의 ORDER_NO_INVOICE_CNT 필드에 셋팅
    	     * */
    		
    		//박스정보 조회
    		List<BoxTypeVO> boxTypeList = boxRecomService.selectBoxTypeList();
    		BoxTypeList boxMaster = new BoxTypeList();
    		int boxCnt = boxMaster.getInitBoxMaster(boxTypeList);
    		log.info("Box Type Cnt : " + boxCnt);
    		
    		// 온도대별 max box 조회
    		List<BoxTypeVO> boxTypeMaxList = boxRecomService.selectBoxTypeMaxList();
    		log.info(" Max Box Type: " + boxTypeMaxList.size());
    		
    		//cell type 조회
    		List<CellTypeVO> cellTypeList = boxRecomService.selectCellTypeList();
    		CellTypeList cellList = new CellTypeList();
    		int cellCnt = cellList.getInitCellType(cellTypeList);
    		log.info("Cell Type Cnt: " + cellCnt);
    		

        	long runTimeStart1 = System.currentTimeMillis();
        	
    		SearchVO svo = new SearchVO();
			svo = new SearchVO();
    	
			List<SearchOrdInfoVO> selectList = boxRecomService.selectOrdLineListAll(svo);
    		log.info(">>>selectList : " + selectList.size());
    		
    		//order 정보 조회
    		OrdInfoList ordList = new OrdInfoList();

			String bf_shipOrderKey = "";
			String af_shipOrderKey = "";

			OrdInfoVO ordVO = new OrdInfoVO();
			int k = 0;
    		for(SearchOrdInfoVO searchOrdInfoVO : selectList)
    		{
    			af_shipOrderKey = searchOrdInfoVO.getShipOrderKey();
    			if(bf_shipOrderKey.equals("") || !bf_shipOrderKey.equals(af_shipOrderKey))
    			{
    				ordVO = new OrdInfoVO();
    				ordVO.setShipOrderKey(searchOrdInfoVO.getShipOrderKey());
        			ordVO.setOrderNo(searchOrdInfoVO.getOrderNo());
        			ordVO.setWarehouseKey(searchOrdInfoVO.getWarehouseKey());
        			ordVO.setOwnerKey(searchOrdInfoVO.getOwnerKey());
        			ordVO.setBoxSplitCheckYn(searchOrdInfoVO.getBoxSplitCheckYn());
        			ordVO.setInvoiceNo(searchOrdInfoVO.getInvoiceNo());
    				ordList.addOrd(ordVO);
    			}
    			
    			ordVO.addOrdLine(searchOrdInfoVO.getSkuCode(), searchOrdInfoVO.getOrdQty(),
						searchOrdInfoVO.getShipOrderKey(), searchOrdInfoVO.getWarehouseKey(), 
						searchOrdInfoVO.getOwnerKey(), searchOrdInfoVO.getOrderNo(), searchOrdInfoVO.getShipOrderItemSeq(),
    					searchOrdInfoVO.getSkuDepth(), searchOrdInfoVO.getSkuHeight(), searchOrdInfoVO.getSkuWidth());

    			bf_shipOrderKey = af_shipOrderKey;
    			
    			/*
    			bf_shipOrderKey = searchOrdInfoVO.getShipOrderKey();
    			
    			if(bf_shipOrderKey.equals(af_shipOrderKey)) {
    				ordVO.addOrdLine(searchOrdInfoVO.getSkuCode(), searchOrdInfoVO.getOrdQty(),
    						searchOrdInfoVO.getShipOrderKey(), searchOrdInfoVO.getWarehouseKey(), 
    						searchOrdInfoVO.getOwnerKey(), searchOrdInfoVO.getOrderNo(), searchOrdInfoVO.getShipOrderItemSeq(),
	    					searchOrdInfoVO.getSkuDepth(), searchOrdInfoVO.getSkuHeight(), searchOrdInfoVO.getSkuWidth());

	    			af_shipOrderKey = searchOrdInfoVO.getShipOrderKey();

    				if(k == selectList.size()-1) {
    					ordList.addOrd(ordVO);
    				}
    				k++;
	    			continue;
    			} else {
    				if(k > 0) {
    					ordList.addOrd(ordVO);
    				}
    				ordVO = new OrdInfoVO();
    				
	    			ordVO.setShipOrderKey(searchOrdInfoVO.getShipOrderKey());
	    			ordVO.setOrderNo(searchOrdInfoVO.getOrderNo());
	    			ordVO.setWarehouseKey(searchOrdInfoVO.getWarehouseKey());
	    			ordVO.setOwnerKey(searchOrdInfoVO.getOwnerKey());
	    			ordVO.setBoxSplitCheckYn(searchOrdInfoVO.getBoxSplitCheckYn());
	    			ordVO.setInvoiceNo(searchOrdInfoVO.getInvoiceNo());
	    			
	    			ordVO.addOrdLine(searchOrdInfoVO.getSkuCode(), searchOrdInfoVO.getOrdQty(),
    						searchOrdInfoVO.getShipOrderKey(), searchOrdInfoVO.getWarehouseKey(), 
    						searchOrdInfoVO.getOwnerKey(), searchOrdInfoVO.getOrderNo(), searchOrdInfoVO.getShipOrderItemSeq(),
	    					searchOrdInfoVO.getSkuDepth(), searchOrdInfoVO.getSkuHeight(), searchOrdInfoVO.getSkuWidth());

	    			af_shipOrderKey = searchOrdInfoVO.getShipOrderKey();
	    			
	    			k++;
	    			
    			}
    			*/
    			
    		}
    		
    		long runTimeEnd1 = System.currentTimeMillis();
    		String apiRunTime1 = StringUtil.formatInterval(runTimeStart1, runTimeEnd1) ;

        	log.info("========order select======= apiRunTime1(ms) : "+ apiRunTime1);
    		
    		runTimeStart1 = System.currentTimeMillis();
    		
    		String r_ifYn = KurlyConstants.STATUS_Y;
    		
    		List<WifShipmentVO> wifShipmentVOList = new ArrayList<WifShipmentVO>();
    		List<WifShipmentDtlVO> wifShipmentDtlVOList = new ArrayList<WifShipmentDtlVO>();
    		int j = 0;
        	OrdSplitApp app;
    		for(OrdInfoVO itOrd : ordList.getList())
    		{
    			int rSplit = 0;
    			
    			//오더박스분할여부확인(Y:오더분할가능, N:오더분할불가능)
				//오더분할부가능일 경우 오더분할 없이 처리
	    		if(KurlyConstants.STATUS_N.equals(itOrd.getBoxSplitCheckYn()) ) {

	    			//분할하지 않음
    				OrdInfoVO tempOrd = new OrdInfoVO();
    				
    				for(OrdLineVO itOrderLine : itOrd.getOrdList() )
    				{
    					tempOrd.addOrdLine(itOrderLine);
    					tempOrd.setOrderNo(itOrd.getOrderNo());
    					tempOrd.setShipOrderKey(itOrd.getShipOrderKey());
    					tempOrd.setWarehouseKey(itOrd.getWarehouseKey());
    				}
    						    				
    				//분할하지 않아도 박스추천은 실행
    				//box 추천 실행
    				new BoxRecommendApp(tempOrd, boxMaster);

//    				log.info( "------------------------------------------" );
//    				log.info( "S boxType : " + tempOrd.getBoxType()  );
//    				log.info( "------------------------------------------" );
    				
    				String t_packBoxTypeRecom = tempOrd.getBoxType();
    				String t_packBoxSplitYn = KurlyConstants.STATUS_N;
    				
    				//2020.11.19 invoiceNo 뒤에 4자리 순번 추가
    				String t_invoiceNo = itOrd.getInvoiceNo();
    				t_invoiceNo = t_invoiceNo + "-" + "0001";
    				
    				//추천박스가 없을 경우 제일큰 박스 추천
    				if("NoBox".equals(t_packBoxTypeRecom)) {
    					t_packBoxTypeRecom = boxMaster.getMaxBox(boxTypeMaxList, itOrd.getWarehouseKey());
    				} 
    				
	    			WifShipmentVO wifShipmentVO = new WifShipmentVO();
	    			
    				wifShipmentVO.setShipOrderKey(itOrd.getShipOrderKey());
    				wifShipmentVO.setOrderNo(itOrd.getOrderNo());
    				wifShipmentVO.setWarehouseKey(itOrd.getWarehouseKey());
    				wifShipmentVO.setOwnerKey(itOrd.getOwnerKey());
    				wifShipmentVO.setInvoiceNo(t_invoiceNo);

    				wifShipmentVO.setPackBoxTypeRecom(t_packBoxTypeRecom);
    				wifShipmentVO.setPackBoxSplitYn(t_packBoxSplitYn);
    				wifShipmentVO.setShipmentCnclYn("N");
    				wifShipmentVO.setOrderNoInvoiceCnt(1);

    				hdrVO(selectList,  wifShipmentVO) ;
    				
    				wifShipmentVOList.add(wifShipmentVO);
    				
    				for(OrdLineVO itOrderLine : tempOrd.getOrdList() )
    				{
		    			WifShipmentDtlVO wifShipmentDtlVO = new WifShipmentDtlVO();
		    			
    					wifShipmentDtlVO.setShipOrderKey(itOrd.getShipOrderKey());
    					wifShipmentDtlVO.setShipOrderItemSeq(itOrderLine.getShipOrderItemSeq());
    					wifShipmentDtlVO.setSkuCode(itOrderLine.getSkuCode());
    					wifShipmentDtlVO.setQtyQpsOrder(""+itOrderLine.getOrdQty());
    					wifShipmentDtlVO.setOwner(itOrd.getOwnerKey());
    					wifShipmentDtlVO.setShipUidItemSeq(itOrderLine.getShipOrderItemSeq());
    					wifShipmentDtlVO.setInvoiceNo(t_invoiceNo);

    					this.dtlVO(selectList, wifShipmentDtlVO);
    					
	    				wifShipmentDtlVOList.add(wifShipmentDtlVO);
    				}
    	    		
//    	    		log.info(">>>itOrd.getShipOrderKey() : [S"+j+"]" + itOrd.getShipOrderKey() );
    	    		j++;
    			} else {
    				//order split
        			app = new OrdSplitApp(itOrd, cellList);
        			rSplit = app.runOrdSplit();
        			
        			//분할이 되질 않을 경우 TB_ORD_SHIPMENT_HDR, TB_ORD_SHIPMENT_DTL 에 분할되지 않은 상태로 생성함
        			if(rSplit == -1) {
//        				log.info( " runOrdSplit fail ["+itOrd.getShipOrderKey()+"]" );
        				
	    				String t_packBoxTypeRecom = "";
	    				String t_packBoxSplitYn = KurlyConstants.STATUS_N;
	    				//2020.11.19 invoiceNo 뒤에 4자리 순번 추가
	    				String t_invoiceNo = itOrd.getInvoiceNo();
	    				t_invoiceNo = t_invoiceNo + "-" + "0001";
	    				//추천박스가 없을 경우 제일큰 박스 추천
	    				t_packBoxTypeRecom = boxMaster.getMaxBox(boxTypeMaxList, itOrd.getWarehouseKey());
	    				
	    				WifShipmentVO wifShipmentVO = new WifShipmentVO();
		    			
	    				wifShipmentVO.setShipOrderKey(itOrd.getShipOrderKey());
	    				wifShipmentVO.setOrderNo(itOrd.getOrderNo());
	    				wifShipmentVO.setWarehouseKey(itOrd.getWarehouseKey());
	    				wifShipmentVO.setOwnerKey(itOrd.getOwnerKey());
	    				wifShipmentVO.setInvoiceNo(t_invoiceNo);

	    				wifShipmentVO.setPackBoxTypeRecom(t_packBoxTypeRecom);
	    				wifShipmentVO.setPackBoxSplitYn(t_packBoxSplitYn);
	    				wifShipmentVO.setShipmentCnclYn("N");
	    				wifShipmentVO.setOrderNoInvoiceCnt(1);

	    				this.hdrVO(selectList,  wifShipmentVO) ;
	    				
	    				wifShipmentVOList.add(wifShipmentVO);
	    				
	    				for(OrdLineVO itOrderLine : itOrd.getOrdList() )
	    				{
			    			WifShipmentDtlVO wifShipmentDtlVO = new WifShipmentDtlVO();
			    			
	    					wifShipmentDtlVO.setShipOrderKey(itOrd.getShipOrderKey());
	    					wifShipmentDtlVO.setShipOrderItemSeq(itOrderLine.getShipOrderItemSeq());
	    					wifShipmentDtlVO.setSkuCode(itOrderLine.getSkuCode());
	    					wifShipmentDtlVO.setQtyQpsOrder(""+itOrderLine.getOrdQty());
	    					wifShipmentDtlVO.setOwner(itOrd.getOwnerKey());
	    					wifShipmentDtlVO.setShipUidItemSeq(itOrderLine.getShipOrderItemSeq());
	    					wifShipmentDtlVO.setInvoiceNo(t_invoiceNo);

	    					this.dtlVO(selectList, wifShipmentDtlVO);
	    					
		    				wifShipmentDtlVOList.add(wifShipmentDtlVO);
	    				}

//	    	    		log.info(">>>itOrd.getShipOrderKey() : [N]" + itOrd.getShipOrderKey() );
        				continue;
        			}
        			
        			int splitSeqNum = itOrd.getSplitNum(); 
	    			for(int i=0;i<splitSeqNum;i++)
	    			{
	    				
	    				//데이터 정리(OrderInfoVo 1개는 1split 오더
	    				OrdInfoVO tempOrd = new OrdInfoVO();
	    				for(OrdLineVO itOrderLine : itOrd.getOrdList() )
	    				{
	    					
	    					if(i + 1 == itOrderLine.getSplitSeq())
	    					{
	    						String itemSeq = StringUtil.lpad((i+1)+"", 4, "0");
	    						tempOrd.addOrdLine(itOrderLine);
	    						tempOrd.setOrderNo(itOrd.getOrderNo() + "_" + i);
	    						tempOrd.setShipOrderKey(itOrd.getShipOrderKey());
		    					tempOrd.setWarehouseKey(itOrd.getWarehouseKey());

		    					tempOrd.setInvoiceNo(itOrd.getInvoiceNo() + "-" + itemSeq);
	    					}
					
	    				}
	    				
	    				if(tempOrd != null) {
	        			
    	    				//box 추천 실행
    	    				new BoxRecommendApp(tempOrd, boxMaster);

//    	    				log.info( "------------------------------------------" );
//    	    				log.info( "boxType : " + tempOrd.getBoxType()  );
//    	    				log.info( "------------------------------------------" );

    	    				String t_packBoxTypeRecom = tempOrd.getBoxType();
    	    				String t_packBoxSplitYn = KurlyConstants.STATUS_Y;
		    				//2020.11.19 invoiceNo 뒤에 4자리 순번 추가
		    				String t_invoiceNo = tempOrd.getInvoiceNo();
    	    				
    	    				if("NoBox".equals(t_packBoxTypeRecom)) {
    	    					t_packBoxTypeRecom = boxMaster.getMaxBox(boxTypeMaxList, itOrd.getWarehouseKey());
    	    				} 
    	    				//오더분할 된경우만 'Y'
    	    				if(splitSeqNum>1) {
    	    					t_packBoxSplitYn = KurlyConstants.STATUS_Y;
    	    				} else {
    	    					t_packBoxSplitYn = KurlyConstants.STATUS_N;
    	    				}

		    				
			    			WifShipmentVO wifShipmentVO = new WifShipmentVO();
			    			
		    				wifShipmentVO.setShipOrderKey(itOrd.getShipOrderKey());
		    				wifShipmentVO.setOrderNo(itOrd.getOrderNo());
		    				wifShipmentVO.setWarehouseKey(itOrd.getWarehouseKey());
		    				wifShipmentVO.setOwnerKey(itOrd.getOwnerKey());
		    				wifShipmentVO.setInvoiceNo(t_invoiceNo);

		    				wifShipmentVO.setPackBoxTypeRecom(t_packBoxTypeRecom);
		    				wifShipmentVO.setPackBoxSplitYn(t_packBoxSplitYn);
		    				wifShipmentVO.setShipmentCnclYn("N");
		    				wifShipmentVO.setOrderNoInvoiceCnt(splitSeqNum);

		    				this.hdrVO(selectList,  wifShipmentVO) ;
		    				
		    				wifShipmentVOList.add(wifShipmentVO);
		    				
		    				int kk =0;
		    				int f_shipUidItemSeq = 1;
		    				String v_shipUidItemSeq = "";
		    				
		    				for(OrdLineVO itOrderLine : tempOrd.getOrdList() )
		    				{
				    			WifShipmentDtlVO wifShipmentDtlVO = new WifShipmentDtlVO();
				    			
		    					if(splitSeqNum == 1) {
		    						v_shipUidItemSeq = itOrderLine.getShipOrderItemSeq();
		    					} else {
		    						v_shipUidItemSeq = StringUtil.lpad(f_shipUidItemSeq+"", 6, "0");
		    					}
		    					
		    					wifShipmentDtlVO.setShipOrderKey(itOrderLine.getShipOrderKey());
		    					wifShipmentDtlVO.setShipOrderItemSeq(itOrderLine.getShipOrderItemSeq());
		    					wifShipmentDtlVO.setSkuCode(itOrderLine.getSkuCode());
		    					wifShipmentDtlVO.setQtyQpsOrder(""+itOrderLine.getOrdQty());
		    					wifShipmentDtlVO.setOwner(itOrd.getOwnerKey());
		    					wifShipmentDtlVO.setShipUidItemSeq(v_shipUidItemSeq);
		    					wifShipmentDtlVO.setInvoiceNo(t_invoiceNo);
		    					
//		    					log.info(">>>itOrd.getSkuCode() : [M"+kk+"]" + itOrderLine.getSkuCode() );
		    					kk++;

		    					this.dtlVO(selectList, wifShipmentDtlVO);

			    				wifShipmentDtlVOList.add(wifShipmentDtlVO);
			    				f_shipUidItemSeq++;
		    				}

	    				} //if(tmpOrd) end
	    				
//	    	    		log.info(">>>itOrd.getShipOrderKey() : [M"+j+"]" + itOrd.getShipOrderKey() );
	    	    		j++;
	    			} //for end
		        	
    			} //else end
    			executeCount++;
        		
				//insertMaaxCount = 100 건 도달하면 insert
				if( (executeCount>2 && executeCount%insertMaaxCount == 0 ) 
						|| ( executeCount == ordList.getList().size() && wifShipmentVOList.size() > 0) ) {

					log.info(">>>executeCount : ["+executeCount+"]"  );
					if(wifShipmentVOList.size() > 0) {
						
						boxRecomService.insertOrdShipmentListType(wifShipmentVOList, wifShipmentDtlVOList);
						
//						try
//				    	{
//							boxRecomService.insertOrdShipmentListType(wifShipmentVOList, wifShipmentDtlVOList);
//						
//				    	} catch (Exception e) {
//				    		result = "error";
//							log.info( " === BoxRecomBatch 1 error >> " +e );
//							resultMessage = e.toString();
//							r_ifYn = KurlyConstants.STATUS_N;
//							    				
//							e.printStackTrace();
//							
//				    	} finally {
//				    		
//				    		//상태 업데이트
//				    		HashMap<String, Object> uParam = new HashMap<String, Object>();
//				    		uParam.put("hdList",wifShipmentVOList);
//				    		uParam.put("receiveIntfYn", r_ifYn);
//				    		if(KurlyConstants.STATUS_N.equals(r_ifYn)) {
//				    			uParam.put("receiveIntfCode", "");
//					    	} else {
//					    		uParam.put("receiveIntfCode", KurlyConstants.STATUS_OK);
//					    	}
//							uParam.put("receiveIntfMemo", "");
//
////							log.info( "----------uParam " + uParam + "------------------------ " );
//							boxRecomService.updateWifShipmentHdrList(uParam);
//
//				    	} //finally end
					}// wifShipmentVOList.size end
					
					//초기화
					wifShipmentVOList = new ArrayList<WifShipmentVO>();
					wifShipmentDtlVOList = new ArrayList<WifShipmentDtlVO>();
					
				}
    		}	//for end
    		
    		runTimeEnd1 = System.currentTimeMillis();
    		apiRunTime1 = StringUtil.formatInterval(runTimeStart1, runTimeEnd1) ;

        	log.info("========order=all======== apiRunTime1(ms) : "+ apiRunTime1);

    	
    	} catch (Exception e) {
    		result = "error";
			log.info( " === BoxRecomBatch  error >> " +e );
			resultMessage = e.toString();
			e.printStackTrace();
    	} finally {

    		apiRunTimeEnd = System.currentTimeMillis();
			apiRunTime = StringUtil.formatInterval(apiRunTimeStart, apiRunTimeEnd) ;

        	log.info("======= apiRunTime(ms) : "+ apiRunTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_BOXRECOM);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog(KurlyConstants.METHOD_BOXRECOM +" Sucess("+apiRunTime+"ms)");
        	} else {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
            	logBatchExec.setMessageLog(resultMessage);
        	}
        	logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
        	logBatchExec.setExecuteCount(executeCount);
        	logBatchExec.setStartDate(startDate);
        	
    		
	    	//로그정보 적재
        	logBatchExecService.createLogBatchExec(logBatchExec);
	    	
        	log.info("=======BoxRecomBatch  finally end======= ");    		
    	}
    	log.info("=======BoxRecomBatch end=======");
    	
    }

	/**
	 * 
	 * @Method Name : selectDate
	 * @작성일 : 2020. 07. 16.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 16. 최초작성
	 * @Method 설명 : TEST 용도의 시간쿼리 리턴
	 */
    public String selectDate() {
    	String resDate = "";
    	try
    	{
    		resDate = boxRecomService.selectDate();
	    } catch (Exception e) {
			e.printStackTrace();
		} 
    	return resDate;
    	
    }

	/**
	 * 
	 * @Method Name : hdrVO
	 * @작성일 : 2020. 12. 07.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 07. 최초작성
	 * @Method 설명 : header VO 에 필요한 값 설정
	 */
    public void hdrVO(List<SearchOrdInfoVO> selectList, WifShipmentVO wifShipmentVO) {

		
    	for(SearchOrdInfoVO searchOrdInfoVO : selectList)
		{
    		if(searchOrdInfoVO.getShipOrderKey().equals(wifShipmentVO.getShipOrderKey())) {

    			wifShipmentVO.setShipType(searchOrdInfoVO.getShipType());
    			wifShipmentVO.setDocStatus(searchOrdInfoVO.getDocStatus());
    			wifShipmentVO.setDocDate(searchOrdInfoVO.getDocDate());
    			wifShipmentVO.setMemberGrade(searchOrdInfoVO.getMemberGrade());
    			wifShipmentVO.setReqShipDate(searchOrdInfoVO.getReqShipDate());
    			wifShipmentVO.setSupplierCode(searchOrdInfoVO.getSupplierCode());
    			wifShipmentVO.setCustomerName(searchOrdInfoVO.getCustomerName());
    			wifShipmentVO.setOrderType(searchOrdInfoVO.getOrderType());
    			wifShipmentVO.setTransportType(searchOrdInfoVO.getTransportType());
    			wifShipmentVO.setCourierBoxPassword(searchOrdInfoVO.getCourierBoxPassword());
    			wifShipmentVO.setRecipientName(searchOrdInfoVO.getRecipientName());
    			wifShipmentVO.setRecipientTelNo(searchOrdInfoVO.getRecipientTelNo());
    			wifShipmentVO.setRoadAddr(searchOrdInfoVO.getRoadAddr());
    			wifShipmentVO.setRoadAddrDetail(searchOrdInfoVO.getRoadAddrDetail());
    			wifShipmentVO.setCustomerId(searchOrdInfoVO.getCustomerId());
				wifShipmentVO.setCustomerTelNo(searchOrdInfoVO.getCustomerTelNo());
				wifShipmentVO.setZipCode(searchOrdInfoVO.getZipCode());
				wifShipmentVO.setCustomerPhoneNo(searchOrdInfoVO.getCustomerPhoneNo());
				wifShipmentVO.setNewZipCode(searchOrdInfoVO.getNewZipCode());
				wifShipmentVO.setJibunBasicAddr(searchOrdInfoVO.getJibunBasicAddr());
				wifShipmentVO.setJibunDetailAddr(searchOrdInfoVO.getJibunDetailAddr());
				wifShipmentVO.setRemark(searchOrdInfoVO.getRemark());
				wifShipmentVO.setBaseAddrType(searchOrdInfoVO.getBaseAddrType());
				wifShipmentVO.setOrderAddrGugun(searchOrdInfoVO.getOrderAddrGugun());
				wifShipmentVO.setOrderAddrDong(searchOrdInfoVO.getOrderAddrDong());
				wifShipmentVO.setCourierCode(searchOrdInfoVO.getCourierCode());
				wifShipmentVO.setNote(searchOrdInfoVO.getNote());
				wifShipmentVO.setCsNotice(searchOrdInfoVO.getCsNotice());
				wifShipmentVO.setCustomerMessage(searchOrdInfoVO.getCustomerMessage());
				wifShipmentVO.setRegionGroupCode(searchOrdInfoVO.getRegionGroupCode());
				wifShipmentVO.setRegionCode(searchOrdInfoVO.getRegionCode());
				wifShipmentVO.setDeliveryRound(searchOrdInfoVO.getDeliveryRound());
				wifShipmentVO.setAllocType(searchOrdInfoVO.getAllocType());
				wifShipmentVO.setAllocSeq(searchOrdInfoVO.getAllocSeq());
				wifShipmentVO.setOriginInvoiceNo(searchOrdInfoVO.getOriginInvoiceNo());
				wifShipmentVO.setSpecialMgntCustYn(searchOrdInfoVO.getSpecialMgntCustYn());
				wifShipmentVO.setManualProcYn(searchOrdInfoVO.getManualProcYn());
				
    			if( StringUtil.isEmpty(wifShipmentVO.getShipType()) ) {
    				wifShipmentVO.setShipType(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getDocStatus()) ) {
					wifShipmentVO.setDocStatus(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getDocDate()) ) {
					wifShipmentVO.setDocDate(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getMemberGrade()) ) {
					wifShipmentVO.setMemberGrade(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getOwnerKey()) ) {
					wifShipmentVO.setOwnerKey(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getReqShipDate()) ) {
					wifShipmentVO.setReqShipDate("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getSupplierCode()) ) {
					wifShipmentVO.setSupplierCode(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCustomerName()) ) {
					wifShipmentVO.setCustomerName(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getOrderType()) ) {
					wifShipmentVO.setOrderType(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getTransportType()) ) {
					wifShipmentVO.setTransportType("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getOrderNo()) ) {
					wifShipmentVO.setOrderNo(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getInvoiceNo()) ) {
					wifShipmentVO.setInvoiceNo(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCourierBoxPassword()) ) {
					wifShipmentVO.setCourierBoxPassword("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getRecipientName()) ) {
					wifShipmentVO.setRecipientName(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getRecipientTelNo()) ) {
					wifShipmentVO.setRecipientTelNo(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getRoadAddr()) ) {
					wifShipmentVO.setRoadAddr(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getRoadAddrDetail()) ) {
					wifShipmentVO.setRoadAddrDetail(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCustomerId()) ) {
					wifShipmentVO.setCustomerId("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCustomerTelNo()) ) {
					wifShipmentVO.setCustomerTelNo("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getZipCode()) ) {
					wifShipmentVO.setZipCode(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCustomerPhoneNo()) ) {
					wifShipmentVO.setCustomerPhoneNo(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getNewZipCode()) ) {
					wifShipmentVO.setNewZipCode(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getJibunBasicAddr()) ) {
					wifShipmentVO.setJibunBasicAddr(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getJibunDetailAddr()) ) {
					wifShipmentVO.setJibunDetailAddr(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getRemark()) ) {
					wifShipmentVO.setRemark("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getBaseAddrType()) ) {
					wifShipmentVO.setBaseAddrType(" ");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getOrderAddrGugun()) ) {
					wifShipmentVO.setOrderAddrGugun("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getOrderAddrDong()) ) {
					wifShipmentVO.setOrderAddrDong("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCourierCode()) ) {
					wifShipmentVO.setCourierCode("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getNote()) ) {
					wifShipmentVO.setNote("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCsNotice()) ) {
					wifShipmentVO.setCsNotice("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getCustomerMessage()) ) {
					wifShipmentVO.setCustomerMessage("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getRegionGroupCode()) ) {
					wifShipmentVO.setRegionGroupCode("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getRegionCode()) ) {
					wifShipmentVO.setRegionCode("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getDeliveryRound()) ) {
					wifShipmentVO.setDeliveryRound("");
				}
				if( StringUtil.isEmpty(wifShipmentVO.getAllocSeq()) ) {
					wifShipmentVO.setAllocSeq("");
				}
				if( "null".equals(wifShipmentVO.getAllocSeq()) ) {
					wifShipmentVO.setAllocSeq("");
				}
		    	if( StringUtil.isEmpty(wifShipmentVO.getAllocType()) ) {
		    		wifShipmentVO.setAllocType("");  
				}
				
			} else {
				continue;
			}
		}
    }

	/**
	 * 
	 * @Method Name : dtlVO
	 * @작성일 : 2020. 12. 07.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 12. 07. 최초작성
	 * @Method 설명 : detail VO 에 필요한 값 설정
	 */
    public void dtlVO(List<SearchOrdInfoVO> selectList, WifShipmentDtlVO wifShipmentDtlVO) {

    	for(SearchOrdInfoVO searchOrdInfoVO : selectList)
		{
    		if( searchOrdInfoVO.getShipOrderKey().equals(wifShipmentDtlVO.getShipOrderKey())
					&& searchOrdInfoVO.getSkuCode().equals(wifShipmentDtlVO.getSkuCode()) ) {


    			wifShipmentDtlVO.setItemStatus(searchOrdInfoVO.getItemStatus());
    			wifShipmentDtlVO.setQtyOriginOrder(searchOrdInfoVO.getQtyOriginOrder());
    			wifShipmentDtlVO.setQtyShipOrder(searchOrdInfoVO.getQtyShipOrder());
    			wifShipmentDtlVO.setQtyPicking(searchOrdInfoVO.getQtyPicking());
    			wifShipmentDtlVO.setQtyAllocated(searchOrdInfoVO.getQtyAllocated());
    			wifShipmentDtlVO.setQtyPacking(searchOrdInfoVO.getQtyPacking());
    			wifShipmentDtlVO.setQtyShipCompleted(searchOrdInfoVO.getQtyShipCompleted());
    			wifShipmentDtlVO.setQtyShipCancelled(searchOrdInfoVO.getQtyShipCancelled());
    			wifShipmentDtlVO.setQtyByUom(searchOrdInfoVO.getQtyByUom());
    			wifShipmentDtlVO.setMeasureKey(searchOrdInfoVO.getMeasureKey());
    			wifShipmentDtlVO.setUomKey(searchOrdInfoVO.getUomKey());
    			wifShipmentDtlVO.setUomQty(searchOrdInfoVO.getUomQty());
    			wifShipmentDtlVO.setDefaultUomKey(searchOrdInfoVO.getDefaultUomKey());
    			wifShipmentDtlVO.setDefaultUomQty(searchOrdInfoVO.getDefaultUomQty());
    			wifShipmentDtlVO.setSkuName(searchOrdInfoVO.getSkuName());
    			wifShipmentDtlVO.setSkuSubName(searchOrdInfoVO.getSkuSubName());
    			wifShipmentDtlVO.setSkuAlterCode(searchOrdInfoVO.getSkuAlterCode());
    			wifShipmentDtlVO.setSkuGroup01(searchOrdInfoVO.getSkuGroup01());
    			wifShipmentDtlVO.setSkuGroup02(searchOrdInfoVO.getSkuGroup02());
    			wifShipmentDtlVO.setSkuGroup03(searchOrdInfoVO.getSkuGroup03());
    			wifShipmentDtlVO.setSkuGroup04(searchOrdInfoVO.getSkuGroup04());
    			wifShipmentDtlVO.setSellingType(searchOrdInfoVO.getSellingType());
    			wifShipmentDtlVO.setGrossWeight(searchOrdInfoVO.getGrossWeight());
    			wifShipmentDtlVO.setNetWeight(searchOrdInfoVO.getNetWeight());
    			wifShipmentDtlVO.setWeightUnit(searchOrdInfoVO.getWeightUnit());
    			wifShipmentDtlVO.setLength(searchOrdInfoVO.getSkuDepth());
    			wifShipmentDtlVO.setWidth(searchOrdInfoVO.getSkuWidth());
    			wifShipmentDtlVO.setHeight(searchOrdInfoVO.getSkuHeight());
    			wifShipmentDtlVO.setCbm(searchOrdInfoVO.getCbm());
    			wifShipmentDtlVO.setSkuOptionName(searchOrdInfoVO.getSkuOptionName());
    			wifShipmentDtlVO.setSkuShopmallName(searchOrdInfoVO.getSkuShopmallName());
    			wifShipmentDtlVO.setRemark(searchOrdInfoVO.getDtRemark());
    			wifShipmentDtlVO.setGroupNo(searchOrdInfoVO.getGroupNo());
    			wifShipmentDtlVO.setWorkBatchNo(searchOrdInfoVO.getWorkBatchNo());
    			wifShipmentDtlVO.setQpsNum(searchOrdInfoVO.getQpsNum());
    			wifShipmentDtlVO.setWmsBatchYmd(searchOrdInfoVO.getWmsBatchYmd());
    			
    			if( StringUtil.isEmpty(searchOrdInfoVO.getItemStatus()) ) {
					wifShipmentDtlVO.setItemStatus(" ");
				}
    			
				if( StringUtil.isEmpty(searchOrdInfoVO.getQtyByUom()) ) {
					wifShipmentDtlVO.setQtyByUom("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getMeasureKey()) ) {
					wifShipmentDtlVO.setMeasureKey("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getUomKey()) ) {
					wifShipmentDtlVO.setUomKey("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getDefaultUomKey()) ) {
					wifShipmentDtlVO.setDefaultUomKey("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuName()) ) {
					wifShipmentDtlVO.setSkuName(" ");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuSubName()) ) {
					wifShipmentDtlVO.setSkuSubName(" ");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuAlterCode()) ) {
					wifShipmentDtlVO.setSkuAlterCode(" ");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuGroup01()) ) {
					wifShipmentDtlVO.setSkuGroup01("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuGroup02()) ) {
					wifShipmentDtlVO.setSkuGroup02("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuGroup03()) ) {
					wifShipmentDtlVO.setSkuGroup03("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuGroup04()) ) {
					wifShipmentDtlVO.setSkuGroup04("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSellingType()) ) {
					wifShipmentDtlVO.setSellingType("");
				}
//				if( StringUtil.isEmpty(searchOrdInfoVO.getNetWeight()) ) {
//					wifShipmentDtlVO.setNetWeight("");
//				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getWeightUnit()) ) {
					wifShipmentDtlVO.setWeightUnit(" ");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuOptionName()) ) {
					wifShipmentDtlVO.setSkuOptionName("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getSkuShopmallName()) ) {
					wifShipmentDtlVO.setSkuShopmallName("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getDtRemark()) ) {
					wifShipmentDtlVO.setRemark("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getGroupNo()) ) {
					wifShipmentDtlVO.setGroupNo("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getWorkBatchNo()) ) {
					wifShipmentDtlVO.setWorkBatchNo("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getQpsNum()) ) {
					wifShipmentDtlVO.setQpsNum("");
				}
				if( StringUtil.isEmpty(searchOrdInfoVO.getWmsBatchYmd()) ) {
					wifShipmentDtlVO.setWmsBatchYmd("");
				}
    			
			} else {
				continue;
			}
		}
    }
}