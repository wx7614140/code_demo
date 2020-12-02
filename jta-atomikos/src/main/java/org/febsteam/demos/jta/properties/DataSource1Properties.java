package org.febsteam.demos.jta.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/11/26 18:02
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource.datasource1")
public class DataSource1Properties extends MultipleDataSourceProperties {
}
