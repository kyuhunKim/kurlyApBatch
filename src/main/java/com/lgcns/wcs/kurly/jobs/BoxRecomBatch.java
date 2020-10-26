package com.lgcns.wcs.kurly.jobs;

import java.time.LocalDateTime;
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
import com.lgcns.wcs.kurly.dto.box.SearchVO;
import com.lgcns.wcs.kurly.service.BoxRecomService;
import com.lgcns.wcs.kurly.service.LogApiStatusService;
import com.lgcns.wcs.kurly.service.LogBatchExecService;

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
    	log.info("=================BoxRecomBatch start===============");
    	log.info("The current date  : " + LocalDateTime.now());
    	long start = System.currentTimeMillis();
    	
		String result = "sucess";
		String resultMessage = "";
		int executeCount = 0;
    	Date startDate = Calendar.getInstance().getTime();
    	try
    	{
    		//상품정보 조회 -- 분할대상 오더들의 모든 상품을 조회
//    		List<SkuTypeVO> skuTypeList = boxRecomService.selectSkuMasterList();
//    		SkuTypeMap skuMaster = new SkuTypeMap();
//    		int skuCnt = skuMaster.getInit(skuTypeList);
//    		log.info("생성된 SKU 수: " + skuCnt);

    		//박스정보 조회
    		List<BoxTypeVO> boxTypeList = boxRecomService.selectBoxTypeList();
    		BoxTypeList boxMaster = new BoxTypeList();
    		int boxCnt = boxMaster.getInitBoxMaster(boxTypeList);
    		log.info("생성된 Box Type 수: " + boxCnt);
    		
    		// 온도대별 max box 조회
    		List<BoxTypeVO> boxTypeMaxList = boxRecomService.selectBoxTypeMaxList();
    		
    		//cell type 조회
    		List<CellTypeVO> cellTypeList = boxRecomService.selectCellTypeList();
    		CellTypeList cellList = new CellTypeList();
    		int cellCnt = cellList.getInitCellType(cellTypeList);
    		log.info("생성된 셀 종류 수: " + cellCnt);

    		//order 정보 조회
    		OrdInfoList ordList = new OrdInfoList();
    		
    		int totalRow = boxRecomService.selectOrdInfoSearchCount() ;
    		int startRowIndex = 0;
    		int endRowIndex = 0;
    		int pagePerRecord = 1000;
    		int pageCount = totalRow/pagePerRecord;
    		
    		if(totalRow > 0 && pageCount < pagePerRecord) {
    			pageCount = 1;
    		}
    		log.info(">>>totalRow : " + totalRow);
    		log.info(">>>pageCount : " + pageCount);
    		
    		SearchVO svo = new SearchVO();
    		for(int s=0; s<=pageCount;s++) 
    		{
    			svo = new SearchVO();
//    			startRowIndex = startRowIndex;
    			endRowIndex = pagePerRecord;

        		
        		log.info(">>>startRowIndex : " + startRowIndex);
        		log.info(">>>endRowIndex : " + endRowIndex);
        		
        		svo.setStartRowIndex(startRowIndex);
        		svo.setEndRowIndex(endRowIndex);
        	
	    		List<OrdInfoVO> selectList = boxRecomService.selectOrdInfoSearchList(svo);
	    		log.info(">>>selectList : " + selectList.size());
	    		
	//    		List<OrdInfoVO> selectList = boxRecomService.selectOrdInfoList();
	//    		log.info(">>>selectList : " + selectList.size());
	    		
	    		
	    		for(OrdInfoVO itOrd : selectList)
	    		{
	    			log.info(">>>itOrd.getShipOrderKey() : " + itOrd.getShipOrderKey());
//	    			
	    			OrdInfoVO ordVO = new OrdInfoVO();
	    			
	    			ordVO.setShipOrderKey(itOrd.getShipOrderKey());
	    			ordVO.setOrderNo(itOrd.getOrderNo());
	    			ordVO.setWarehouseKey(itOrd.getWarehouseKey());
	    			ordVO.setOwnerKey(itOrd.getOwnerKey());
	    			ordVO.setBoxSplitCheckYn(itOrd.getBoxSplitCheckYn());
	
	    			Map<String, String> param = new HashMap<String, String>();
	    			param.put("shipOrderKey", itOrd.getShipOrderKey());
	    			
	    			List<OrdLineVO> ordLines = boxRecomService.selectOrdLineList(param);
	    			
	    			for(OrdLineVO itOrdLine : ordLines)
					{
		    			ordVO.addOrdLine(itOrdLine.getSkuCode(), itOrdLine.getOrdQty(),
		    					itOrdLine.getShipOrderKey(), itOrdLine.getWarehouseKey(), 
		    					itOrdLine.getOwnerKey(), itOrdLine.getOrderNo(), itOrdLine.getShipOrderItemSeq(),
		    					itOrdLine.getSkuDepth(), itOrdLine.getSkuHeight(), itOrdLine.getSkuWidth());
					}
	    			ordList.addOrd(ordVO);	
	    			
	    		}
	    	
	    		String r_ifYn = KurlyConstants.STATUS_Y;
	    		String retMessage = "";
	    		
	    		OrdSplitApp app;
	    		for(OrdInfoVO itOrd : ordList.getList())
	    		{
	    			int rSplit = 0;
	    			
	    			try
	    	    	{
	    				//해당 shipOrderKey 가 TB_ORD_SHIPMENT_HDR 존재하는지 확인 , 있을경우 오류남
	    				Map<String, String> CntParam = new HashMap<String, String>();
	    				CntParam.put("shipOrderKey", itOrd.getShipOrderKey());
	    				
	    				int hdrCnt = boxRecomService.selectOrdShipmentCount(CntParam);
	    				if(hdrCnt>0) {
	        				log.info( " This shipOrderKey is existenceis TB_ORD_SHIPMENT_HDR Table ["+itOrd.getShipOrderKey()+"]");
	    					continue;
	    				}
	    				
	    				//오더박스분할여부확인(Y:오더분할가능, N:오더분할불가능)
	    				//오더분할부가능일 경우 오더분할 없이 처리
	    	    		if(KurlyConstants.STATUS_N.equals(itOrd.getBoxSplitCheckYn())) {
	        				
	    	    			int shipUidKey = 0;
		    				String t_packBoxTypeRecom = "";
		    				String t_packBoxSplitYn = KurlyConstants.STATUS_N;
		    				//박스추천없이 해당 온도대의 제일 큰 박스로 설정함
		    				t_packBoxTypeRecom = boxMaster.getMaxBox(boxTypeMaxList, itOrd.getWarehouseKey());
		    				
		    				Map<String, String> param = new HashMap<String, String>();
		    				param.put("shipOrderKey", itOrd.getShipOrderKey());
		    				param.put("packBoxTypeRecom", t_packBoxTypeRecom);  //추천패킹박스타입
		    				param.put("packBoxSplitYn", t_packBoxSplitYn);  
	
		    				log.info( "----------param " + param + "------------------------ " );
		    				shipUidKey = boxRecomService.insertOrdShipmentHdr(param);
		    				
	        				//split 안된값으로 셋팅
		    	    		Map<String, String> dParam = new HashMap<String, String>();
		    				dParam.put("shipOrderKey", itOrd.getShipOrderKey());
		    				dParam.put("shipUidKey", ""+shipUidKey);
		    				dParam.put("owner", ""+itOrd.getOwnerKey());
		
		    				log.info( "----------dParam " + dParam + "------------------------ " );
		    				boxRecomService.insertOrdShipmentDtlAll(dParam);
		        			
	        			} else {
	        				//order split
	            			app = new OrdSplitApp(itOrd, cellList);
	            			rSplit = app.runOrdSplit();
	            			
	            			//이형 상품일 경우
	            			//처리안함. update 확인필요
	            			if(rSplit == -1) {
	            				log.info( " runOrdSplit fail ["+itOrd.getShipOrderKey()+"]" );
		        				r_ifYn = KurlyConstants.STATUS_T;
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
	    	    						tempOrd.addOrdLine(itOrderLine);
	    	    						tempOrd.setOrderNo(itOrd.getOrderNo() + "_" + i);
	    	    						tempOrd.setShipOrderKey(itOrd.getShipOrderKey());
	    	    					}
	    	    				}
	    	    				
	    	    				if(tempOrd != null) {
	    	        			
		    	    				//box 추천 실행
		    	    				new BoxRecommendApp(tempOrd, boxMaster);
		
		    	    				log.info( "------------------------------------------" );
		    	    				log.info( "boxType : " + tempOrd.getBoxType()  );
		    	    				log.info( "------------------------------------------" );
		
		    	    				int shipUidKey = 0;
		    	    				String t_packBoxTypeRecom = tempOrd.getBoxType();
		    	    				String t_packBoxSplitYn = KurlyConstants.STATUS_N;
		    	    				if("NoBox".equals(t_packBoxTypeRecom)) {
		    	    					t_packBoxTypeRecom = boxMaster.getMaxBox(boxTypeMaxList, itOrd.getWarehouseKey());
		    	    				} 
		    	    				//오더분할 된경우만 'Y'
		    	    				if(splitSeqNum>1) {
		    	    					t_packBoxSplitYn = KurlyConstants.STATUS_Y;
		    	    				}
		    	    				
		    	    				Map<String, String> param = new HashMap<String, String>();
		    	    				param.put("shipOrderKey", itOrd.getShipOrderKey());
		    	    				param.put("packBoxTypeRecom", t_packBoxTypeRecom);  //추천패킹박스타입
		    	    				param.put("packBoxSplitYn", t_packBoxSplitYn);  
		
		    	    				log.info( "----------param " + param + "------------------------ " );
		    	    				shipUidKey = boxRecomService.insertOrdShipmentHdr(param);
		    	    				
		    	    				try
		    	        	    	{
		    	    					boxRecomService.insertOrdShipmentDtl(tempOrd, shipUidKey, splitSeqNum);
		    	        	    	} catch (Exception e) {
		    	        	    		log.info( " === insertOrdShipmentDtl  error >> " +e );
		    	        				e.printStackTrace();
//		    	        				
//		    	        				Map<String, String> dParam = new HashMap<String, String>();
//		    		    				dParam.put("shipOrderKey", itOrd.getShipOrderKey());
//		    		    				dParam.put("shipUidKey", ""+shipUidKey);
//		    		    				dParam.put("owner", ""+itOrd.getOwnerKey());
//		    		
//		    		    				log.info( "----------dParam " + dParam + "------------------------ " );
//		    		    				boxRecomService.insertOrdShipmentDtlAll(dParam);
		    	        	    	}
	
	    	    				} //if(tmpOrd) end
	    	    			} //for end
	        			} //else end
	    				
	    	    	} catch (Exception e) {
	    	    		result = "error";
	    				log.info( " === BoxRecomBatch  error >> " +e );
	    				retMessage = e.toString();
	    				r_ifYn = KurlyConstants.STATUS_N;
	    				    				
	    				e.printStackTrace();
	    				
	    	    	} finally {
	    	    		//상태 업데이트
	    				Map<String, String> uParam = new HashMap<String, String>();
	    				uParam.put("shipOrderKey", itOrd.getShipOrderKey());
	    				uParam.put("receiveIntfYn", r_ifYn);
	    				uParam.put("receiveIntfCode", "");
	    				uParam.put("receiveIntfMemo", "");
	
	    				log.info( "----------uParam " + uParam + "------------------------ " );
	        			boxRecomService.updateWifShipmentHdr(uParam);
	        			
	    	    		
	    	    	} //finally end
	    		}	//for end

        		executeCount++;
    		}	
    	
    	} catch (Exception e) {
    		result = "error";
			log.info( " === BoxRecomBatch  error >> " +e );
			resultMessage = e.toString();
			e.printStackTrace();
    	} finally {

        	long end = System.currentTimeMillis();
        	long diffTime = ( end - start );  //m

        	log.info("================= diffTime(ms) : "+ diffTime);

	    	//배치 로그 정보 insert
        	LogBatchExec logBatchExec = new LogBatchExec();
	    	
        	logBatchExec.setExecMethod(KurlyConstants.METHOD_BOXRECOM);
        	if("sucess".equals(result)) {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_Y);
            	logBatchExec.setMessageLog("");	
        	} else {
            	logBatchExec.setSuccessYn(KurlyConstants.STATUS_N);
            	logBatchExec.setMessageLog(resultMessage);
        	}
        	logBatchExec.setExecuteDirectYn(KurlyConstants.STATUS_N);
        	logBatchExec.setExecuteCount(executeCount);
        	logBatchExec.setStartDate(startDate);
        	
    		
	    	//로그정보 적재
        	logBatchExecService.createLogBatchExec(logBatchExec);
	    	
        	log.info("=================BoxRecomBatch  finally end=============== ");    		
    	}
    	log.info("=================BoxRecomBatch end===============");
    	
    }

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
}