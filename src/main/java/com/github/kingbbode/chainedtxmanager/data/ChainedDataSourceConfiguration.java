package com.github.kingbbode.chainedtxmanager.data;

import com.github.kingbbode.chainedtxmanager.data.a.ADataSourceConfiguration;
import com.github.kingbbode.chainedtxmanager.data.b.BDataSourceConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChainedDataSourceConfiguration {

	@Bean
	@Primary
	public PlatformTransactionManager primaryTransactionManager(
		@Qualifier(ADataSourceConfiguration.TX_MANAGER) PlatformTransactionManager aTxManager,
		@Qualifier(BDataSourceConfiguration.TX_MANAGER) PlatformTransactionManager bTxManager
	) {
		return new ChainedTransactionManager(bTxManager, aTxManager);
	}
}
