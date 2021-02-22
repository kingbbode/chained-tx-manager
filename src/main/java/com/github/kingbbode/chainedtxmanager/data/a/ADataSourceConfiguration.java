package com.github.kingbbode.chainedtxmanager.data.a;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(basePackages = ADataSourceConfiguration.JPA_PACKAGE, entityManagerFactoryRef = ADataSourceConfiguration.ENTITY_MANAGER, transactionManagerRef = ADataSourceConfiguration.TX_MANAGER)
public class ADataSourceConfiguration {

	static final String JPA_PACKAGE = "com.github.kingbbode.chainedtxmanager.data.a";
	static final String ENTITY_MANAGER = "aEntityManager";
	public static final String TX_MANAGER = "aTransactionManager";
	private static final String UNIT_NAME = "aPersistenceUnit";
	private static final String DATA_SOURCE = "aDataSource";
	private static final String DATA_SOURCE_PROPERTIES = "aDataSourceProperties";
	private static final String JPA_ENTITY_FACTORY_BUILDER = "aEntityFactoryBuilder";
	private static final String JPA_VENDOR_ADAPTER = "aJpaVendorAdapter";

	@Primary
	@Bean(DATA_SOURCE_PROPERTIES)
	@ConfigurationProperties(prefix = "datasource.a")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(DATA_SOURCE)
	@ConfigurationProperties("datasource.a.hikari")
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
