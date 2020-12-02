package org.febsteam.demos.jta.properties;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import lombok.Data;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/11/30 18:27
 */
@Data
public abstract class MultipleDataSourceProperties {

    /**
     * Name of the datasource. Default to "testdb" when using an embedded database.
     */
    protected String name;

    /**
     * Whether to generate a random datasource name.
     */
    protected boolean generateUniqueName = true;

    /**
     * Fully qualified name of the connection pool implementation to use. By default, it
     * is auto-detected from the classpath.
     */
    protected String type;

    /**
     * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
     */
    protected String driverClassName;

    /**
     * JDBC URL of the database.
     */
    protected String url;

    /**
     * Login username of the database.
     */
    protected String username;

    /**
     * Login password of the database.
     */
    protected String password;

    /**
     * JNDI location of the datasource. Class, url, username and password are ignored when
     * set.
     */
    protected String jndiName;

    /**
     * Initialize the datasource with available DDL and DML scripts.
     */
    protected DataSourceInitializationMode initializationMode = DataSourceInitializationMode.EMBEDDED;

    /**
     * Platform to use in the DDL or DML scripts (such as schema-${platform}.sql or
     * data-${platform}.sql).
     */
    protected String platform = "all";

    /**
     * Schema (DDL) script resource references.
     */
    protected List<String> schema;

    /**
     * Username of the database to execute DDL scripts (if different).
     */
    protected String schemaUsername;

    /**
     * Password of the database to execute DDL scripts (if different).
     */
    protected String schemaPassword;

    /**
     * Data (DML) script resource references.
     */
    protected List<String> data;

    /**
     * Username of the database to execute DML scripts (if different).
     */
    protected String dataUsername;

    /**
     * Password of the database to execute DML scripts (if different).
     */
    protected String dataPassword;

    /**
     * Whether to stop if an error occurs while initializing the database.
     */
    protected boolean continueOnError = false;

    /**
     * Statement separator in SQL initialization scripts.
     */
    protected String separator = ";";

    /**
     * SQL scripts encoding.
     */
    protected Charset sqlScriptEncoding;

    protected DataSourceProperties.Xa xa = new DataSourceProperties.Xa();

    protected String uniqueName;

    protected final MybatisPlusProperties mybatisPlusProperties = new MybatisPlusProperties();
    protected int minPoolSize;
    protected int maxPoolSize;
    protected int maxLifetime;
    protected int borrowConnectionTimeout;
    protected int loginTimeout;
    protected int maintenanceInterval;
    protected int maxIdleTime;
    protected String testQuery;
    protected String uniqueResourceName;

    /**
     * Location of MyBatis xml config file.
     */
    protected String configLocation;

    /**
     * Locations of MyBatis mapper files.
     *
     * @since 3.1.2 add default value
     */
    protected String[] mapperLocations = new String[]{"classpath*:/mapper/**/*.xml"};

    /**
     * Packages to search type aliases. (Package delimiters are ",; \t\n")
     */
    protected String typeAliasesPackage;

    /**
     * The super class for filtering type alias.
     * If this not specifies, the MyBatis deal as type alias all classes that searched from typeAliasesPackage.
     */
    protected Class<?> typeAliasesSuperType;

    /**
     * Packages to search for type handlers. (Package delimiters are ",; \t\n")
     */
    protected String typeHandlersPackage;

    /**
     * Indicates whether perform presence check of the MyBatis xml config file.
     */
    protected boolean checkConfigLocation = false;

    /**
     * Execution mode for {@link org.mybatis.spring.SqlSessionTemplate}.
     */
    protected ExecutorType executorType;

    /**
     * The default scripting language driver class. (Available when use together with mybatis-spring 2.0.2+)
     * <p>
     * 如果设置了这个,你会至少失去几乎所有 mp 提供的功能
     */
    protected Class<? extends LanguageDriver> defaultScriptingLanguageDriver;

    /**
     * Externalized properties for MyBatis configuration.
     */
    protected Properties configurationProperties;

    /**
     * A Configuration object for customize default settings. If {@link #configLocation}
     * is specified, this property is not used.
     * TODO 使用 MybatisConfiguration
     */
    @NestedConfigurationProperty
    protected MybatisConfiguration configuration;

    /**
     * TODO 枚举包扫描
     */
    protected String typeEnumsPackage;

    /**
     * TODO 全局配置
     */
    @NestedConfigurationProperty
    protected GlobalConfig globalConfig = GlobalConfigUtils.defaults();

    public MybatisPlusProperties convert(){
        mybatisPlusProperties.setCheckConfigLocation(this.checkConfigLocation);
        mybatisPlusProperties.setConfigLocation(this.configLocation);
        mybatisPlusProperties.setConfiguration(this.configuration);
        mybatisPlusProperties.setConfigurationProperties(this.configurationProperties);
        mybatisPlusProperties.setDefaultScriptingLanguageDriver(this.defaultScriptingLanguageDriver);
        mybatisPlusProperties.setExecutorType(this.executorType);
        mybatisPlusProperties.setGlobalConfig(this.globalConfig);
        mybatisPlusProperties.setMapperLocations(this.mapperLocations);
        mybatisPlusProperties.setTypeAliasesPackage(this.typeAliasesPackage);
        mybatisPlusProperties.setTypeAliasesSuperType(this.typeAliasesSuperType);
        mybatisPlusProperties.setTypeEnumsPackage(this.typeEnumsPackage);
        mybatisPlusProperties.setTypeHandlersPackage(this.typeHandlersPackage);
        return mybatisPlusProperties;
    }
}
