package org.febsteam.demos.jta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.febsteam.demos.jta.entity.datasource1.Test1;

public interface ITest1Service extends IService<Test1> {
    void saveTest1Test2();
    void saveTest1Test2WithException() throws Exception;
}
