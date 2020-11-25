package com.lgcns.wcs.kurly;

import java.time.LocalDateTime;

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
	

	@Scheduled(fixedDelay = 600000)
	public void testTask() {
		log.info("The current date (1) testTask: " + LocalDateTime.now());
		String resDate = boxRecom.selectDate();
		log.info("The current date (1) resDate: " + resDate);

		log.info("Current Thread : {}", Thread.currentThread().getName());
	}
	
//	@Scheduled(cron = "0 0/5 * * * *")
//	@Scheduled(fixedDelay = 600000, initialDelay = 10000)
//	public void BoxRecom() {
//		log.info("The current date (2) BoxRecom : " + LocalDateTime.now());
//		log.info("Current Thread : {}", Thread.currentThread().getName());
//		boxRecom.BoxRecomBatchTask();
//	}
//  //ok
//	@Scheduled(fixedDelay = 100000)
//	public void ToteRelease() {
//		System.out.println("The current date (3) ToteRelease : " + LocalDateTime.now());
//		tote.ToteReleaseTask();
//	}
//  //ok
//	@Scheduled(fixedDelay = 100000)
//	public void RegionMasterTask() {
//		System.out.println("The current date (4) RegionMasterTask : " + LocalDateTime.now());
//		regionMaster.RegionMasterTask();
//	}
//  //ok
//	@Scheduled(fixedDelay = 100000)
//	public void ToteScan() {
//		System.out.println("The current date (5) ToteRelease : " + LocalDateTime.now());
//		toteScan.ToteScanTask();
//	}
	
//	@Scheduled(fixedDelay = 100000)
//	public void ToteCellExceptTxn() {
//		System.out.println("The current date (6) ToteRelease : " + LocalDateTime.now());
//		toteCellExceptTxn.ToteCellExceptTxnTask();
//	}
	

//	@Scheduled(fixedDelay = 100000)
//	public void OrdmadeNotfully() {
//		System.out.println("The current date (7) ToteRelease : " + LocalDateTime.now());
//		ordmadeNotfully.OrdmadeNotfullyTask();
//	}
//	

//  //ok
//	@Scheduled(fixedDelay = 100000)
//	public void OrdmadeNotfullyReplay() {
//		System.out.println("The current date (8) ToteRelease : " + LocalDateTime.now());
//		ordmadeNotfullyReplay.OrdmadeNotfullyReplayTask();
//	}
//  //ok
//	@Scheduled(fixedDelay = 100000)
//	public void PickQpsComplet() {
//		System.out.println("The current date (10) ToteRelease : " + LocalDateTime.now());
//		pickQpsComplet.PickQpsCompletTask();
//	}
//  //ok
//	@Scheduled(fixedDelay = 600000)
//	public void PackQpsComplet() {
//		System.out.println("The current date (9) ToteRelease : " + LocalDateTime.now());
//		 packQpsComplet.PackQpsCompletTask();
//	}
	
//  //ok
//	@Scheduled(fixedDelay = 100000)
//	public void InvoicePrintComplet() {
//		System.out.println("The current date (11) ToteRelease : " + LocalDateTime.now());
//		invoicePrintComplet.InvoicePrintCompletTask();
//	}
//  //ok
//	@Scheduled(fixedDelay = 100000) 
//	public void InvoiceSortComplet() {
//		System.out.println("The current date (12) ToteRelease : " + LocalDateTime.now());
//		invoiceSortComplet.InvoiceSortCompletTask();
//	}
//	@Scheduled(fixedDelay = 600000) 
//	public void QpsNumUseCell() {
//		System.out.println("The current date (13) ToteRelease : " + LocalDateTime.now());
//		qpsNumUseCell.QpsNumUseCellTask();
//	}
//	@Scheduled(fixedDelay = 600000) 
//	public void DasNumUseCell() {
//		System.out.println("The current date (14) ToteRelease : " + LocalDateTime.now());
//		dasNumUseCell.DasNumUseCellTask();
//	}
}