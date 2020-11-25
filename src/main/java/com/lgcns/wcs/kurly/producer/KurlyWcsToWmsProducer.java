package com.lgcns.wcs.kurly.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.lgcns.wcs.kurly.dto.DasNumUseCellData;
import com.lgcns.wcs.kurly.dto.InvoicePrintCompletData;
import com.lgcns.wcs.kurly.dto.InvoiceSortCompletData;
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyData;
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplayData;
import com.lgcns.wcs.kurly.dto.PackQpsCompletData;
import com.lgcns.wcs.kurly.dto.PickQpsCompletData;
import com.lgcns.wcs.kurly.dto.QpsNumUseCellData;
import com.lgcns.wcs.kurly.dto.ResponseMesssage;
import com.lgcns.wcs.kurly.dto.ToteCellExceptTxnData;
import com.lgcns.wcs.kurly.dto.ToteReleaseParamData;
import com.lgcns.wcs.kurly.dto.ToteScanData;
import com.lgcns.wcs.kurly.service.ToteReleaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KurlyWcsToWmsProducer {

	//토트 마스트 초기화 연계 
	@Qualifier("toteReleaseKafkaTemplate")
    private KafkaTemplate<String, ToteReleaseParamData> toteReleaseKafkaTemplate;
	
	@Qualifier("toteScanKafkaTemplate")
    private KafkaTemplate<String, ToteScanData> toteScanKafkaTemplate;

	@Qualifier("toteCellExceptTxnKafkaTemplate")
    private KafkaTemplate<String, ToteCellExceptTxnData> toteCellExceptTxnKafkaTemplate;

    @Qualifier("ordmadeNotfullyReplayKafkaTemplate")
    private KafkaTemplate<String, OrdmadeNotfullyReplayData> ordmadeNotfullyReplayKafkaTemplate;
    @Qualifier("ordmadeNotfullyKafkaTemplate")
    private KafkaTemplate<String, OrdmadeNotfullyData> ordmadeNotfullyKafkaTemplate;
    @Qualifier("pickQpsCompletKafkaTemplate")
    private KafkaTemplate<String, PickQpsCompletData> pickQpsCompletKafkaTemplate;
    @Qualifier("packQpsCompletKafkaTemplate")
    private KafkaTemplate<String, PackQpsCompletData> packQpsCompletKafkaTemplate;
    @Qualifier("invoicePrintCompletKafkaTemplate")
    private KafkaTemplate<String, InvoicePrintCompletData> invoicePrintCompletKafkaTemplate;
    @Qualifier("invoiceSortCompletKafkaTemplate")
    private KafkaTemplate<String, InvoiceSortCompletData> invoiceSortCompletKafkaTemplate;
    @Qualifier("qpsNumUseCellKafkaTemplate")
    private KafkaTemplate<String, QpsNumUseCellData> qpsNumUseCellKafkaTemplate;
    @Qualifier("dasNumUseCellKafkaTemplate")
    private KafkaTemplate<String, DasNumUseCellData> dasNumUseCellKafkaTemplate;
    
    @Value("${spring.kafka.topics.wcs-out.destination.tote-release}")
    private String WMS_TOTE_RELEASE;
    @Value("${spring.kafka.topics.wcs-out.destination.tote-scan}")
    private String WMS_TOTE_SCAN;
    @Value("${spring.kafka.topics.wcs-out.destination.tote-cellExceptTxn}")
    private String WMS_TOTE_CELLEXCEPTTXN;
    @Value("${spring.kafka.topics.wcs-out.destination.ordmadeNotfullyReplay}")
    private String WMS_ORDMADE_NOTFULLYREPLAY;
    @Value("${spring.kafka.topics.wcs-out.destination.ordmadeNotfully}")
    private String WMS_ORDMADE_NOTFULLY;
    @Value("${spring.kafka.topics.wcs-out.destination.pickQpsComplet}")
    private String WMS_PICK_QPSCOMPLET;
    @Value("${spring.kafka.topics.wcs-out.destination.packQpsComplet}")
    private String WMS_PACK_QPSCOMPLET;
    @Value("${spring.kafka.topics.wcs-out.destination.invoicePrintComplet}")
    private String WMS_INVOICE_PRINTCOMPLET;
    @Value("${spring.kafka.topics.wcs-out.destination.invoiceSortComplet}")
    private String WMS_INVOICE_SORTCOMPLET;
    @Value("${spring.kafka.topics.wcs-out.destination.qpsNumUseCell}")
    private String WMS_QPSNUMUSECELL;

    @Value("${spring.kafka.topics.wcs-out.destination.dasNumUseCell}")
    private String WMS_DASNUMUSECELL;
    
    @Autowired
    ToteReleaseService toteReleaseService;

    public KurlyWcsToWmsProducer(
    		KafkaTemplate<String, ToteReleaseParamData> toteReleaseKafkaTemplate,
    		KafkaTemplate<String, ToteScanData> toteScanKafkaTemplate,
    		KafkaTemplate<String, ToteCellExceptTxnData> toteCellExceptTxnKafkaTemplate,
    	    KafkaTemplate<String, OrdmadeNotfullyReplayData> ordmadeNotfullyReplayKafkaTemplate,
    	    KafkaTemplate<String, OrdmadeNotfullyData> ordmadeNotfullyKafkaTemplate,
    	    KafkaTemplate<String, PickQpsCompletData> pickQpsCompletKafkaTemplate,
    	    KafkaTemplate<String, PackQpsCompletData> packQpsCompletKafkaTemplate,
    	    KafkaTemplate<String, InvoicePrintCompletData> invoicePrintCompletKafkaTemplate,
    	    KafkaTemplate<String, InvoiceSortCompletData> invoiceSortCompletKafkaTemplate,
    	    KafkaTemplate<String, QpsNumUseCellData> qpsNumUseCellKafkaTemplate,
    	    KafkaTemplate<String, DasNumUseCellData> dasNumUseCellKafkaTemplate
           ){
      this.toteReleaseKafkaTemplate = toteReleaseKafkaTemplate;
      this.toteScanKafkaTemplate = toteScanKafkaTemplate;
      this.toteCellExceptTxnKafkaTemplate = toteCellExceptTxnKafkaTemplate;
      this.ordmadeNotfullyReplayKafkaTemplate = ordmadeNotfullyReplayKafkaTemplate;
      this.ordmadeNotfullyKafkaTemplate = ordmadeNotfullyKafkaTemplate;
      this.pickQpsCompletKafkaTemplate = pickQpsCompletKafkaTemplate;
      this.packQpsCompletKafkaTemplate = packQpsCompletKafkaTemplate;
      this.invoicePrintCompletKafkaTemplate = invoicePrintCompletKafkaTemplate;
      this.invoiceSortCompletKafkaTemplate = invoiceSortCompletKafkaTemplate;
      this.qpsNumUseCellKafkaTemplate = qpsNumUseCellKafkaTemplate;
      this.dasNumUseCellKafkaTemplate = dasNumUseCellKafkaTemplate;
    }

    /**
	 * 
	 * @Method Name : sendToteReleaseObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 토트 마스트 초기화 연계  (WCS => WMS)
	 */
    public DeferredResult<ResponseEntity<?>> sendToteReleaseObject(ToteReleaseParamData toteReleaseParamData){
    	log.info("=================sendToteReleaseObject start===============");
    	log.info("================="+toteReleaseParamData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, ToteReleaseParamData>> future =  toteReleaseKafkaTemplate.send(WMS_TOTE_RELEASE, toteReleaseParamData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, ToteReleaseParamData> result) {
//        	    	log.info("sendToteReleaseObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendToteReleaseObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, ToteReleaseParamData> result =  toteReleaseKafkaTemplate.send(WMS_TOTE_RELEASE,  toteReleaseParamData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_TOTE_RELEASE,  toteReleaseParamData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendToteReleaseObject end===============");
    	return deferredResult;
    }
    /**
	 * 
	 * @Method Name : sendToteScanObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 :WCS 토트 자동화 설비 투입 정보 (마스터)  (WCS => WMS)
	 */
    public DeferredResult<ResponseEntity<?>> sendToteScanObject(ToteScanData toteScanData){
    	log.info("=================sendToteScanObject start===============");

		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//	    	ListenableFuture<SendResult<String, ToteScanData>> future =  toteScanKafkaTemplate.send(WMS_TOTE_SCAN, toteScanData);
//	       
//	  	    future.addCallback(new ListenableFutureCallback<>() {
//	    	    @Override
//	        	public void onSuccess(SendResult<String, ToteScanData> result) {
//	    	    	log.info("sendToteScanObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//	        	}
//	    	    @Override
//	        	public void onFailure(Throwable ex) {
//	    	    	log.info("sendToteScanObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//	        	}
//	        });
        	SendResult<String, ToteScanData> result =  toteScanKafkaTemplate.send(WMS_TOTE_SCAN,  toteScanData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_TOTE_SCAN,  toteScanData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	log.info("=================sendToteScanObject end===============");
    	return deferredResult;
    }
    
    /**
	 * 
	 * @Method Name : sendToteCellExceptTxnObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : 토트 문제 처리용 피킹정보 연계  (WCS => WMS)
	 */
    public DeferredResult<ResponseEntity<?>> sendToteCellExceptTxnObject(ToteCellExceptTxnData toteCellExceptTxnData){
    	log.info("=================sendToteCellExceptTxnObject start===============");
    	log.info("================="+toteCellExceptTxnData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, ToteCellExceptTxnData>> future =  toteCellExceptTxnKafkaTemplate.send(WMS_TOTE_CELLEXCEPTTXN, toteCellExceptTxnData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, ToteCellExceptTxnData> result) {
//        	    	log.info("sendToteCellExceptTxnObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendToteCellExceptTxnObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, ToteCellExceptTxnData> result =  toteCellExceptTxnKafkaTemplate.send(WMS_TOTE_CELLEXCEPTTXN,  toteCellExceptTxnData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_TOTE_CELLEXCEPTTXN,  toteCellExceptTxnData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendToteCellExceptTxnObject end===============");
    	return deferredResult;
    }
    

    /**
	 * 
	 * @Method Name : sendOrdmadeNotfullyReplayObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 미출오더 상품보충용 추가피킹정보 연계  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendOrdmadeNotfullyReplayObject(OrdmadeNotfullyReplayData ordmadeNotfullyReplayData){
    	log.info("=================sendOrdmadeNotfullyReplayObject start===============");
    	log.info("================="+ordmadeNotfullyReplayData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, OrdmadeNotfullyReplayData>> future =  ordmadeNotfullyReplayKafkaTemplate.send(WMS_ORDMADE_NOTFULLYREPLAY, ordmadeNotfullyReplayData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, OrdmadeNotfullyReplayData> result) {
//        	    	log.info("sendOrdmadeNotfullyReplayObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendOrdmadeNotfullyReplayObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, OrdmadeNotfullyReplayData> result =  ordmadeNotfullyReplayKafkaTemplate.send(WMS_ORDMADE_NOTFULLYREPLAY,  ordmadeNotfullyReplayData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_ORDMADE_NOTFULLYREPLAY,  ordmadeNotfullyReplayData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendOrdmadeNotfullyReplayObject end===============");
    	return deferredResult;
    }

    /**
	 * 
	 * @Method Name : sendOrdmadeNotfullyReplayObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendOrdmadeNotfullyObject(OrdmadeNotfullyData ordmadeNotfullyData){
    	log.info("=================sendOrdmadeNotfullyObject start===============");
    	log.info("================="+ordmadeNotfullyData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, OrdmadeNotfullyData>> future =  ordmadeNotfullyKafkaTemplate.send(WMS_ORDMADE_NOTFULLY, ordmadeNotfullyData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, OrdmadeNotfullyData> result) {
//        	    	log.info("sendOrdmadeNotfullyObject onSuccess Sent message : " + result.toString() );
////        	    	ResponseEntity<String> responseEntity = new ResponseEntity<>("SUCCESS", HttpStatus.OK);
////                    deferredResult.setResult(responseEntity);
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendOrdmadeNotfullyObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, OrdmadeNotfullyData> result =  ordmadeNotfullyKafkaTemplate.send(WMS_ORDMADE_NOTFULLY,  ordmadeNotfullyData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_ORDMADE_NOTFULLY,  ordmadeNotfullyData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendOrdmadeNotfullyObject end===============");
    	return deferredResult;
    }
    /**
	 * 
	 * @Method Name : sendPackQpsCompletObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 오더 패킹 완료 정보  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendPackQpsCompletObject(PackQpsCompletData packQpsCompletData){
    	log.info("=================sendPackQpsCompletObject start===============");
    	log.info("================="+packQpsCompletData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, PackQpsCompletData>> future =  packQpsCompletKafkaTemplate.send(WMS_PACK_QPSCOMPLET, packQpsCompletData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, PackQpsCompletData> result) {
//        	    	log.info("sendPackQpsCompletObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendPackQpsCompletObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, PackQpsCompletData> result =  packQpsCompletKafkaTemplate.send(WMS_PACK_QPSCOMPLET,  packQpsCompletData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_PACK_QPSCOMPLET,  packQpsCompletData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendPackQpsCompletObject end===============");
    	return deferredResult;
    }

    /**
	 * 
	 * @Method Name : sendPickQpsCompletObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 오더 피킹 완료 정보  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendPickQpsCompletObject(PickQpsCompletData pickQpsCompletData){
    	log.info("=================sendPickQpsCompletObject start===============");
    	log.info("================="+pickQpsCompletData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, PickQpsCompletData>> future =  pickQpsCompletKafkaTemplate.send(WMS_PICK_QPSCOMPLET, pickQpsCompletData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, PickQpsCompletData> result) {
//        	    	log.info("sendPickQpsCompletObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendPickQpsCompletObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, PickQpsCompletData> result =  pickQpsCompletKafkaTemplate.send(WMS_PICK_QPSCOMPLET,  pickQpsCompletData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_PICK_QPSCOMPLET,  pickQpsCompletData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendPickQpsCompletObject end===============");
    	return deferredResult;
    }
    /**
	 * 
	 * @Method Name : sendInvoicePrintCompletObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 운송장 발행 정보  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendInvoicePrintCompletObject(InvoicePrintCompletData invoicePrintCompletData){
    	log.info("=================sendInvoicePrintCompletObject start===============");
    	log.info("================="+invoicePrintCompletData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, InvoicePrintCompletData>> future =  invoicePrintCompletKafkaTemplate.send(WMS_INVOICE_PRINTCOMPLET, invoicePrintCompletData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, InvoicePrintCompletData> result) {
//        	    	log.info("sendInvoicePrintCompletObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendInvoicePrintCompletObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, InvoicePrintCompletData> result =  invoicePrintCompletKafkaTemplate.send(WMS_INVOICE_PRINTCOMPLET,  invoicePrintCompletData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_INVOICE_PRINTCOMPLET,  invoicePrintCompletData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendInvoicePrintCompletObject end===============");
    	return deferredResult;
    }

    /**
	 * 
	 * @Method Name : sendInvoiceSortCompletObject
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 방면 분류 완료 정보  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendInvoiceSortCompletObject(InvoiceSortCompletData invoiceSortCompletData){
    	log.info("=================sendInvoiceSortCompletObject start===============");
    	log.info("================="+invoiceSortCompletData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, InvoiceSortCompletData>> future =  invoiceSortCompletKafkaTemplate.send(WMS_INVOICE_SORTCOMPLET, invoiceSortCompletData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, InvoiceSortCompletData> result) {
//        	    	log.info("sendInvoiceSortCompletObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendInvoiceSortCompletObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, InvoiceSortCompletData> result =  invoiceSortCompletKafkaTemplate.send(WMS_INVOICE_SORTCOMPLET,  invoiceSortCompletData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_INVOICE_SORTCOMPLET,  invoiceSortCompletData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendInvoiceSortCompletObject end===============");
    	return deferredResult;
    }

    /**
	 * 
	 * @Method Name : sendQpsNumUseCellObject
	 * @작성일 : 2020. 08. 25.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 08. 25. 최초작성
	 * @Method 설명 : QPS 호기별 가용셀 정보  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendQpsNumUseCellObject(QpsNumUseCellData qpsNumUseCellData){
    	log.info("=================sendQpsNumUseCellObject start===============");
    	log.info("================="+qpsNumUseCellData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, QpsNumUseCellData>> future =  qpsNumUseCellKafkaTemplate.send(WMS_QPSNUMUSECELL, qpsNumUseCellData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, InvoiceSortCompletData> result) {
//        	    	log.info("sendInvoiceSortCompletObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendInvoiceSortCompletObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, QpsNumUseCellData> result =  qpsNumUseCellKafkaTemplate.send(WMS_QPSNUMUSECELL,  qpsNumUseCellData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_QPSNUMUSECELL,  qpsNumUseCellData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendQpsNumUseCellObject end===============");
    	return deferredResult;
    }


    /**
	 * 
	 * @Method Name : sendDasNumUseCellObject
	 * @작성일 : 2020. 11. 24.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 11. 24. 최초작성
	 * @Method 설명 : DAS 호기별 가용셀 정보  (WCS => WMS)
     */
    public DeferredResult<ResponseEntity<?>> sendDasNumUseCellObject(DasNumUseCellData dasNumUseCellData){
    	log.info("=================sendDasNumUseCellObject start===============");
    	log.info("================="+dasNumUseCellData.toString());
    		
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        try {
//        	ListenableFuture<SendResult<String, DasNumUseCellData>> future =  qpsNumUseCellKafkaTemplate.send(WMS_QPSNUMUSECELL, dasNumUseCellData);
//        	
//        	future.addCallback(new ListenableFutureCallback<>() {
//        	    @Override
//            	public void onSuccess(SendResult<String, InvoiceSortCompletData> result) {
//        	    	log.info("sendInvoiceSortCompletObject onSuccess Sent message : " + result.toString() );
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("SUCCESS");
//        	    	resMessage.setMessage("");
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
//                    deferredResult.setResult(responseEntity);
//            	}
//        	    @Override
//            	public void onFailure(Throwable ex) {
//        	    	log.info("sendInvoiceSortCompletObject onFailure  Unable to send message due to : " + ex.getMessage());
//
//        	    	ResponseMesssage resMessage = new ResponseMesssage();
//        	    	resMessage.setStatus("FAILURE");
//        	    	resMessage.setMessage(ex.getMessage());
//                    ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//                    deferredResult.setResult(responseEntity);
//            	}
//            });

        	SendResult<String, DasNumUseCellData> result =  dasNumUseCellKafkaTemplate.send(WMS_DASNUMUSECELL,  dasNumUseCellData).get();
        	log.info("=================Producer send result [key = {}, value = {}]", result.getProducerRecord().key(),
                    result.getProducerRecord().value());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("SUCCESS");
	    	resMessage.setMessage("");
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("================= Kafka  [topic = {}, value = {}] Exception: {}",
            		WMS_DASNUMUSECELL,  dasNumUseCellData, e.getMessage());

	    	ResponseMesssage resMessage = new ResponseMesssage();
	    	resMessage.setStatus("FAILURE");
	    	resMessage.setMessage(e.getMessage());
            ResponseEntity<ResponseMesssage> responseEntity = new ResponseEntity<>(resMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            deferredResult.setResult(responseEntity);
        }
    	
    	log.info("=================sendQpsNumUseCellObject end===============");
    	return deferredResult;
    }
}