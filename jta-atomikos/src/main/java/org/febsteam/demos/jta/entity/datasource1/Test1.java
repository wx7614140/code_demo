package org.febsteam.demos.jta.entity.datasource1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/9/16 11:47
 */

@Data
@TableName("test1")
public class Test1 implements Serializable {

    private static final long serialVersionUID = 4169021429269000516L;

    /**
     * id,主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * name
     */
    @TableField("name")
    private String name;

    /**
     * 密码
     */
    @TableField("value")
    private String value;
}
