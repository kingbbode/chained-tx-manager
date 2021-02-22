package com.github.kingbbode.chainedtxmanager.experiment;

import com.github.kingbbode.chainedtxmanager.data.a.ADataService;
import com.github.kingbbode.chainedtxmanager.data.b.BDataService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ChainedTxService {
	private final ADataService aDataService;
	private final BDataService bDataService;

	public ChainedTxService(ADataService aDataService, BDataService bDataService) {
		this.aDataService = aDataService;
		this.bDataService = bDataService;
	}

	public void a1_ok_b1_ok() {
		aDataService.save("a1");
		bDataService.save("b1");
	}

	public void a1_ok_b1_ok_a2_ok() {
		aDataService.save("a1");
		bDataService.save("b1");
		aDataService.save("a2");
	}

	public void a1_ok_b1_ok_a2_ok_b2_ok() {
		aDataService.save("a1");
		bDataService.save("b1");
		aDataService.save("a2");
		bDataService.save("b2");
	}

	public void b1_ok_a1_ok_b2_ok() {
		bDataService.save("b1");
		aDataService.save("a1");
		bDataService.save("b2");
	}

	public void a1_ok_b1_rollback() {
		aDataService.save("a1");
		bDataService.saveWithException("b1");
	}

	public void a1_ok_b1_ok_a2_rollback() {
		aDataService.save("a1");
		bDataService.save("b1");
		aDataService.saveWithException("a2");
	}

	public void a1_ok_b1_ok_a2_ok_b2_rollback() {
		aDataService.save("a1");
		bDataService.save("b1");
		aDataService.save("a2");
		bDataService.saveWithException("b2");
	}

	public void b1_ok_a1_rollback() {
		bDataService.save("b1");
		aDataService.saveWithException("a1");
	}

	public void b1_ok_a1_ok_b2_rollback() {
		bDataService.save("b1");
		aDataService.save("a1");
		bDataService.saveWithException("b2");
	}

	public void b1_ok_a1_ok_b2_ok_a2_rollback() {
		bDataService.save("b1");
		aDataService.save("a1");
		bDataService.save("b2");
		aDataService.saveWithException("a2");
	}
}
