package com.github.kingbbode.chainedtxmanager.data.b;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = BDataSourceConfiguration.JPA_PACKAGE, entityManagerFactoryRef = BDataSourceConfiguration.ENTITY_MANAGER, transactionManagerRef = BDataSourceConfiguration.TX_MANAGER)
public class BDataSourceConfiguration {

	static final String JPA_PACKAGE = "com.github.kingbbode.chainedtxmanager.data.b";
	public static final String TX_MANAGER = "bTransactionManager";
	static final String ENTITY_MANAGER = "bEntityManager";
	private static final String UNIT_NAME = "bPersistenceUnit";
	private static final String DATA_SOURCE = "bDataSource";
	private static final String DATA_SOURCE_PROPERTIES = "bDataSourceProperties";
	private static final String JPA_ENTITY_FACTORY_BUILDER = "bEntityFactoryBuilder";
	private static final String JPA_VENDOR_ADAPTER = "bJpaVendorAdapter";

	@Bean(DATA_SOURCE_PROPERTIES)
	@ConfigurationProperties(prefix = "datasource.b")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(DATA_SOURCE)
	@ConfigurationProperties("datasource.b.hikari")
	public DataSource dataSource(@Qualifier(DATA_SOURCE_PROPERTIES) DataSourceProperties dataSourceProperties) {
		return new LazyConnectionDataSourceProxy(dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build());
	}

	@Bean(name = TX_MANAGER)
	public PlatformTransactionManager jpaSessionTxManager(@Qualifier(ENTITY_MANAGER) LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
		return transactionManager;
	}



	@Bean(JPA_VENDOR_ADAPTER)
	public JpaVendorAdapter jpaVendorAdapter(JpaProperties properties) {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(properties.isShowSql());
		if (properties.getDatabase() != null) {
			adapter.setDatabase(properties.getDatabase());
		}
		if (properties.getDatabasePlatform() != null) {
			adapter.setDatabasePlatform(properties.getDatabasePlatform());
		}
		adapter.setGenerateDdl(properties.isGenerateDdl());
		return adapter;
	}

	@Bean(JPA_ENTITY_FACTORY_BUILDER)
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
		@Qualifier(JPA_VENDOR_ADAPTER) JpaVendorAdapter jpaVendorAdapter,
		ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
		JpaProperties properties
	) {
		return new EntityManagerFactoryBuilder(
			jpaVendorAdapter, properties.getProperties(),
			persistenceUnitManager.getIfAvailable());
	}

	@Bean(ENTITY_MANAGER)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		@Qualifier(JPA_ENTITY_FACTORY_BUILDER) EntityManagerFactoryBuilder factoryBuilder,
		@Qualifier(DATA_SOURCE) DataSource dataSource
	) {

		return factoryBuilder
			.dataSource(dataSource)
			.packages(JPA_PACKAGE)
			.persistenceUnit(UNIT_NAME)
			.build();
	}
}
