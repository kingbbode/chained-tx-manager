package com.github.kingbbode.chainedtxmanager.experiment;

import com.github.kingbbode.chainedtxmanager.data.a.ADataRepository;
import com.github.kingbbode.chainedtxmanager.data.a.ADataService;
import com.github.kingbbode.chainedtxmanager.data.b.BDataRepository;
import com.github.kingbbode.chainedtxmanager.data.b.BDataRequiredService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(ChainedTxServiceRequiredTest.TestConfig.class)
class ChainedTxServiceRequiredTest {

	static class TestConfig {
		@Bean
		public ChainedTxService chainedTxService(
			ADataService aDataService,
			BDataRequiredService bDataRequiredService
		) {
			return new ChainedTxService(aDataService, bDataRequiredService);
		}
	}

	@Autowired
	ADataRepository aDataRepository;

	@Autowired
	BDataRepository bDataRepository;

	@Autowired
	ChainedTxService chainedTxService;

	@AfterEach
	void tearDown() {
		aDataRepository.deleteAllInBatch();
		bDataRepository.deleteAllInBatch();
	}

	@Test
	void a1_ok_b1_ok() {
		chainedTxService.a1_ok_b1_ok();
		assertThat(aDataRepository.findByText("a1")).isPresent();
		assertThat(bDataRepository.findByText("b1")).isPresent();
	}

	@Test
	void a1_ok_b1_ok_a2_ok() {
		chainedTxService.a1_ok_b1_ok_a2_ok();
		assertThat(aDataRepository.findByText("a1")).isPresent();
		assertThat(bDataRepository.findByText("b1")).isPresent();
		assertThat(aDataRepository.findByText("a2")).isPresent();
	}

	@Test
	void a1_ok_b1_ok_a2_ok_b2_ok() {
		chainedTxService.a1_ok_b1_ok_a2_ok_b2_ok();
		assertThat(aDataRepository.findByText("a1")).isPresent();
		assertThat(bDataRepository.findByText("b1")).isPresent();
		assertThat(aDataRepository.findByText("a2")).isPresent();
		assertThat(bDataRepository.findByText("b2")).isPresent();
	}

	@Test
	void b1_ok_a1_ok_b2_ok() {
		chainedTxService.b1_ok_a1_ok_b2_ok();
		assertThat(bDataRepository.findByText("b1")).isPresent();
		assertThat(aDataRepository.findByText("a1")).isPresent();
		assertThat(bDataRepository.findByText("b2")).isPresent();
	}

	@Test
	void a1_ok_b1_rollback() {
		assertThatThrownBy(() -> chainedTxService.a1_ok_b1_rollback()).isExactlyInstanceOf(IllegalStateException.class);

		assertThat(aDataRepository.findByText("a1")).isEmpty();
		assertThat(bDataRepository.findByText("b1")).isEmpty();
	}

	@Test
	void a1_ok_b1_ok_a2_rollback() {
		assertThatThrownBy(() -> chainedTxService.a1_ok_b1_ok_a2_rollback()).isExactlyInstanceOf(IllegalStateException.class);

		assertThat(aDataRepository.findByText("a1")).isEmpty();
		assertThat(bDataRepository.findByText("b1")).isEmpty();
		assertThat(aDataRepository.findByText("a2")).isEmpty();
	}

	@Test
	void a1_ok_b1_ok_a2_ok_b2_rollback() {
		assertThatThrownBy(() -> chainedTxService.a1_ok_b1_ok_a2_ok_b2_rollback()).isExactlyInstanceOf(IllegalStateException.class);

		assertThat(aDataRepository.findByText("a1")).isEmpty();
		assertThat(bDataRepository.findByText("b1")).isEmpty();
		assertThat(aDataRepository.findByText("a2")).isEmpty();
		assertThat(bDataRepository.findByText("b2")).isEmpty();
	}

	@Test
	void b1_ok_a1_rollback() {
		assertThatThrownBy(() -> chainedTxService.b1_ok_a1_rollback()).isExactlyInstanceOf(IllegalStateException.class);

		assertThat(bDataRepository.findByText("b1")).isEmpty();
		assertThat(aDataRepository.findByText("a1")).isEmpty();
	}

	@Test
	void b1_ok_a1_ok_b2_rollback() {
		assertThatThrownBy(() -> chainedTxService.b1_ok_a1_ok_b2_rollback()).isExactlyInstanceOf(IllegalStateException.class);

		assertThat(bDataRepository.findByText("b1")).isEmpty();
		assertThat(aDataRepository.findByText("a1")).isEmpty();
		assertThat(bDataRepository.findByText("b2")).isEmpty();
	}

	@Test
	void b1_ok_a1_ok_b2_ok_a2_rollback() {
		assertThatThrownBy(() -> chainedTxService.b1_ok_a1_ok_b2_ok_a2_rollback()).isExactlyInstanceOf(IllegalStateException.class);

		assertThat(bDataRepository.findByText("b1")).isEmpty();
		assertThat(aDataRepository.findByText("a1")).isEmpty();
		assertThat(bDataRepository.findByText("b2")).isEmpty();
		assertThat(aDataRepository.findByText("a2")).isEmpty();
	}
}