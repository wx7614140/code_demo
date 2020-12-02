package org.febsteam.demos.jta.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import com.p6spy.engine.spy.P6DataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.febsteam.demos.jta.properties.MultipleDataSourceProperties;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/2 15:51
 */
public abstract class MultipleDataSourceConfig<T extends MultipleDataSourceProperties> implements InitializingBean {
    protected static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    /**
     * 替代 MybatisPlusProperties
     */
    protected final T dataSourceProperties;

    protected final MybatisPlusProperties properties;

    protected final Interceptor[] interceptors;

    protected final TypeHandler[] typeHandlers;

    protected final LanguageDriver[] languageDrivers;

    protected final ResourceLoader resourceLoader;

    protected final DatabaseIdProvider databaseIdProvider;

    protected final List<ConfigurationCustomizer> configurationCustomizers;

    protected final List<MybatisPlusPropertiesCustomizer> mybatisPlusPropertiesCustomizers;

    protected final ApplicationContext applicationContext;

    public MultipleDataSourceConfig(T dataSourceProperties,
                             ObjectProvider<Interceptor[]> interceptorsProvider,
                             ObjectProvider<TypeHandler[]> typeHandlersProvider,
                             ObjectProvider<LanguageDriver[]> languageDriversProvider,
                             ResourceLoader resourceLoader,
                             ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                             ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
                             ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider,
                             ApplicationContext applicationContext) {
        this.dataSourceProperties = dataSourceProperties;
        this.properties = dataSourceProperties.convert();
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.typeHandlers = typeHandlersProvider.getIfAvailable();
        this.languageDrivers = languageDriversProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
        this.mybatisPlusPropertiesCustomizers = mybatisPlusPropertiesCustomizerProvider.getIfAvailable();
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(mybatisPlusPropertiesCustomizers)) {
            mybatisPlusPropertiesCustomizers.forEach(i -> i.customize(properties));
        }
        checkConfigFileExists();
    }

    protected void checkConfigFileExists() {
        if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
            Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
            Assert.state(resource.exists(),
                    "Cannot find config location: " + resource + " (please add config file or check your Mybatis configuration)");
        }
    }
    /**
     * 检查spring容器里是否有对应的bean,有则进行消费
     *
     * @param clazz    class
     * @param consumer 消费
     * @param <T>      泛型
     */
    protected <T> void getBeanThen(Class<T> clazz, Consumer<T> consumer) {
        if (this.applicationContext.getBeanNamesForType(clazz, false, false).length > 0) {
            consumer.accept(this.applicationContext.getBean(clazz));
        }
    }

    // TODO 入参使用 MybatisSqlSessionFactoryBean
    protected void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
        // TODO 使用 MybatisConfiguration
        MybatisConfiguration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new MybatisConfiguration();
        }
        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factory.setConfiguration(configuration);
    }

    /**
     * 数据源
     * @return
     */
    protected DataSource dataSource() throws SQLException {
        //创建xa datasource
        MysqlXADataSource mysqlXADataSource=new MysqlXADataSource();
        mysqlXADataSource.setUrl(dataSourceProperties.getUrl());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXADataSource.setPassword(dataSourceProperties.getPassword());
        mysqlXADataSource.setUser(dataSourceProperties.getUsername());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);

        //2.注册到我们全局事务上
        AtomikosDataSourceBean xaDataSource =new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXADataSource);
        xaDataSource.setUniqueResourceName(dataSourceProperties.getUniqueResourceName());
        xaDataSource.setMinPoolSize(dataSourceProperties.getMinPoolSize());
        xaDataSource.setMaxPoolSize(dataSourceProperties.getMaxPoolSize());
        xaDataSource.setBorrowConnectionTimeout(dataSourceProperties.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(dataSourceProperties.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(dataSourceProperties.getMaintenanceInterval());
        xaDataSource.setMaxIdleTime(dataSourceProperties.getMaxIdleTime());
        xaDataSource.setTestQuery(dataSourceProperties.getTestQuery());
        xaDataSource.setXaDataSourceClassName(dataSourceProperties.getType());
        P6DataSource dataSource = new P6DataSource(xaDataSource);
        return dataSource;
    }

    /**
     * sqlSessionFactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        applyConfiguration(factory);
        if (this.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (this.properties.getTypeAliasesSuperType() != null) {
            factory.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.typeHandlers)) {
            factory.setTypeHandlers(this.typeHandlers);
        }
        Resource[] mapperLocations = this.properties.resolveMapperLocations();
        if (!ObjectUtils.isEmpty(mapperLocations)) {
            factory.setMapperLocations(mapperLocations);
        }
        // TODO 修改源码支持定义 TransactionFactory
        this.getBeanThen(TransactionFactory.class, factory::setTransactionFactory);

        // TODO 对源码做了一定的修改(因为源码适配了老旧的mybatis版本,但我们不需要适配)
        Class<? extends LanguageDriver> defaultLanguageDriver = this.properties.getDefaultScriptingLanguageDriver();
        if (!ObjectUtils.isEmpty(this.languageDrivers)) {
            factory.setScriptingLanguageDrivers(this.languageDrivers);
        }
        Optional.ofNullable(defaultLanguageDriver).ifPresent(factory::setDefaultScriptingLanguageDriver);

        // TODO 自定义枚举包
        if (StringUtils.hasLength(this.properties.getTypeEnumsPackage())) {
            factory.setTypeEnumsPackage(this.properties.getTypeEnumsPackage());
        }
        // TODO 此处必为非 NULL
        GlobalConfig globalConfig = this.properties.getGlobalConfig();
        // TODO 注入填充器
        this.getBeanThen(MetaObjectHandler.class, globalConfig::setMetaObjectHandler);
        // TODO 注入主键生成器
        this.getBeanThen(IKeyGenerator.class, i -> globalConfig.getDbConfig().setKeyGenerator(i));
        // TODO 注入sql注入器
        this.getBeanThen(ISqlInjector.class, globalConfig::setSqlInjector);
        // TODO 注入ID生成器
        this.getBeanThen(IdentifierGenerator.class, globalConfig::setIdentifierGenerator);
        // TODO 设置 GlobalConfig 到 MybatisSqlSessionFactoryBean
        factory.setGlobalConfig(globalConfig);
        return factory.getObject();
    }

    /**
     * sqlsessionTemplate
     * @param sqlSessionFactory
     * @return
     */
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = this.properties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }
}
