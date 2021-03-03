package com.lgcns.wcs.kurly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lgcns.wcs.kurly.jobs.BoxRecomBatch;
import com.lgcns.wcs.kurly.jobs.DasNumUseCellBatch;
import com.lgcns.wcs.kurly.jobs.InvoicePrintCompletBatch;
import com.lgcns.wcs.kurly.jobs.InvoiceSortCompletBatch;
import com.lgcns.wcs.kurly.jobs.OrdmadeNotfullyBatch;
import com.lgcns.wcs.kurly.jobs.OrdmadeNotfullyReplayBatch;
import com.lgcns.wcs.kurly.jobs.PackQpsCompletBatch;
import com.lgcns.wcs.kurly.jobs.PickQpsCompletBatch;
import com.lgcns.wcs.kurly.jobs.QpsNumUseCellBatch;
import com.lgcns.wcs.kurly.jobs.RegionMasterBatch;
import com.lgcns.wcs.kurly.jobs.ReplayOptimizBatch;
import com.lgcns.wcs.kurly.jobs.ToteCellExceptTxnBatch;
import com.lgcns.wcs.kurly.jobs.ToteReleaseBatch;
import com.lgcns.wcs.kurly.jobs.ToteScanBatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduleTask {
	
	@Autowired
	ToteReleaseBatch tote;
	
	@Autowired
	RegionMasterBatch regionMaster;
	
	@Autowired
	BoxRecomBatch boxRecom;
	
	@Autowired
	ToteScanBatch toteScan;
	
	@Autowired
	InvoicePrintCompletBatch invoicePrintComplet;
	
	@Autowired
	InvoiceSortCompletBatch invoiceSortComplet;
	
	@Autowired
	OrdmadeNotfullyBatch ordmadeNotfully;
	
	@Autowired
	OrdmadeNotfullyReplayBatch ordmadeNotfullyReplay;
	
	@Autowired
	PackQpsCompletBatch packQpsComplet;
	
	@Autowired
	PickQpsCompletBatch pickQpsComplet;
	
	@Autowired
	ToteCellExceptTxnBatch toteCellExceptTxn;
	
	@Autowired
	QpsNumUseCellBatch qpsNumUseCell;
	
	@Autowired
	DasNumUseCellBatch dasNumUseCell;
	
	@Autowired
	ReplayOptimizBatch replayOptimizBatch;
	
//	//TEST
	@Scheduled(fixedDelay = 600000)
	public void testTask() {
//		log.info("The current date (1) testTask: " + LocalDateTime.now());
		String resDate = boxRecom.selectDate();
		log.info("The current date (1) resDate: " + resDate);

//		log.info("Current Thread : {}", Thread.currentThread().getName());
	}
	//오더분할 배치 - 10초
	@Scheduled(fixedDelay = 10000)
	public void BoxRecom() {
//		log.info("The current date (2) BoxRecom : " + LocalDateTime.now());
//		log.info("Current Thread : {}", Thread.currentThread().getName());
		boxRecom.BoxRecomBatchTask();
	}
	//토트 마스터 초기화(Release) 정보 연계 - 10초
	@Scheduled(fixedDelay = 10000)
	public void ToteRelease() {
//		System.out.println("The current date (3) ToteRelease : " + LocalDateTime.now());
		tote.ToteReleaseTask();
	}
	//분류 권역 정보  - 확인필요
	@Scheduled(fixedDelay = 3600000)
	public void RegionMasterTask() {
//		System.out.println("The current date (4) RegionMasterTask : " + LocalDateTime.now());
		regionMaster.RegionMasterTask();
	}
	//WCS 토트 자동화 설비 투입 정보 (토트 기준) - 10초
	@Scheduled(fixedDelay = 10000)
	public void ToteScan() {
//		System.out.println("The current date (5) ToteRelease : " + LocalDateTime.now());
		toteScan.ToteScanTask();
	}
	//WCS 문제처리용 피킹정보 연계 - 10초
	@Scheduled(fixedDelay = 10000)
	public void ToteCellExceptTxn() {
//		System.out.println("The current date (6) ToteRelease : " + LocalDateTime.now());
		toteCellExceptTxn.ToteCellExceptTxnTask();
	}
	//WCS 미출오더 처리용 WMS 피킹지시금지 정보 연계 - 10초
	@Scheduled(fixedDelay = 10000)
	public void OrdmadeNotfully() {
//		System.out.println("The current date (7) ToteRelease : " + LocalDateTime.now());
		ordmadeNotfully.OrdmadeNotfullyTask();
	}
	
	//WCS 미출오더 상품보충용 추가피킹정보 연계 - 10초
	@Scheduled(fixedDelay = 10000)
	public void OrdmadeNotfullyReplay() {
//		System.out.println("The current date (8) ToteRelease : " + LocalDateTime.now());
		ordmadeNotfullyReplay.OrdmadeNotfullyReplayTask();
	}
	//WCS 오더 피킹 완료 정보 - 1분
	@Scheduled(fixedDelay = 60000)
	public void PickQpsComplet() {
//		System.out.println("The current date (9) PickQpsComplet : " + LocalDateTime.now());
		pickQpsComplet.PickQpsCompletTask();
	}
	//WCS 오더 패킹 완료 정보 - 1분
	@Scheduled(fixedDelay = 60000)
	public void PackQpsComplet() {
//		System.out.println("The current date (10) PackQpsComplet : " + LocalDateTime.now());
		 packQpsComplet.PackQpsCompletTask();
	}
	
	//WCS 운송장 발행 정보 - 1분
	@Scheduled(fixedDelay = 60000)
	public void InvoicePrintComplet() {
//		System.out.println("The current date (11) InvoicePrintComplet : " + LocalDateTime.now());
		invoicePrintComplet.InvoicePrintCompletTask();
	}
	//WCS 방면 분류 완료 정보 - 1분
	@Scheduled(fixedDelay = 60000) 
	public void InvoiceSortComplet() {
//		System.out.println("The current date (12) InvoiceSortComplet : " + LocalDateTime.now());
		invoiceSortComplet.InvoiceSortCompletTask();
	}
	//QPS 호기별 가용셀 정보 - 10초
	@Scheduled(fixedDelay = 10000) 
	public void QpsNumUseCell() {
//		System.out.println("The current date (13) QpsNumUseCell : " + LocalDateTime.now());
		qpsNumUseCell.QpsNumUseCellTask();
	}
	//DAS 그룹번호별 가용셀 정보 - 10초
	@Scheduled(fixedDelay = 10000) 
	public void DasNumUseCell() {
//		System.out.println("The current date (14) DasNumUseCell : " + LocalDateTime.now());
		dasNumUseCell.DasNumUseCellTask();
	}

	//최적화 배치 미처리분 재작업 - 3분
	@Scheduled(fixedDelay = 180000) 
	public void ReplayOptimizBatch() {
//		System.out.println("The current date (15) InvoiceSortComplet : " + LocalDateTime.now());
		replayOptimizBatch.ReplayOptimizBatchTask();
	}
}