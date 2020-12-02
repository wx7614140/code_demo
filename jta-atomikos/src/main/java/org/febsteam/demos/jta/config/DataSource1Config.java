package org.febsteam.demos.jta.config;

import com.baomidou.mybatisplus.autoconfigure.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.febsteam.demos.jta.properties.DataSource1Properties;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/11/26 18:11
 */
@Configuration
@Slf4j
@ConditionalOnClass({DataSource1Properties.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
public class DataSource1Config extends MultipleDataSourceConfig<DataSource1Properties> {
    private final DataSource1Properties dataSource1Properties;
    public DataSource1Config(DataSource1Properties dataSource1Properties, ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider, ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider, ApplicationContext applicationContext) {
        super(dataSource1Properties, interceptorsProvider, typeHandlersProvider, languageDriversProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider, mybatisPlusPropertiesCustomizerProvider, applicationContext);
        this.dataSource1Properties = dataSource1Properties;
    }

    /**
     * 数据源
     * @return
     */
    @Bean("dataSource1")
    @Override
    public DataSource dataSource() throws SQLException {
        return super.dataSource();
    }

    /**
     * sqlSessionFactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean("datasource1SqlSessionFactory")
    @Override
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource1") DataSource dataSource) throws Exception {
        return super.sqlSessionFactory(dataSource);
    }

    /**
     * sqlsessionTemplate
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "datasource1SqlSessionTemplate")
    @Override
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("datasource1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return super.sqlSessionTemplate(sqlSessionFactory);
    }

}
