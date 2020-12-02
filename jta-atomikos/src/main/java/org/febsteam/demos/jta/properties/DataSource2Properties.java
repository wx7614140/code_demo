package org.febsteam.demos.jta.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/11/26 18:02
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "spring.datasource.datasource2")
public class DataSource2Properties extends MultipleDataSourceProperties {
}
