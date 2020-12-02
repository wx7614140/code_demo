package org.febsteam.demos.jta;

import org.febsteam.demos.jta.properties.DataSource1Properties;
import org.febsteam.demos.jta.properties.DataSource2Properties;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
/**
 * 多数据源配置
 */
@MapperScans(value = {
		@MapperScan(basePackages = "org.febsteam.demos.jta.dao.datasource1", sqlSessionTemplateRef = "datasource1SqlSessionTemplate",
				sqlSessionFactoryRef = "datasource1SqlSessionFactory"),
		@MapperScan(basePackages = "org.febsteam.demos.jta.dao.datasource2", sqlSessionTemplateRef = "datasource2SqlSessionTemplate",
				sqlSessionFactoryRef = "datasource2SqlSessionFactory")
})
@EnableConfigurationProperties({DataSource1Properties.class, DataSource2Properties.class})
public class JtaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JtaApplication.class, args);
	}

}
