package org.febsteam.demos.jta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.febsteam.demos.jta.dao.datasource1.Test1Mapper;
import org.febsteam.demos.jta.dao.datasource2.Test2Mapper;
import org.febsteam.demos.jta.entity.datasource1.Test1;
import org.febsteam.demos.jta.entity.datasource2.Test2;
import org.febsteam.demos.jta.service.ITest1Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/2 17:26
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Test1ServiceImpl extends ServiceImpl<Test1Mapper, Test1> implements ITest1Service {
    private final Test1Mapper test1Mapper;
    private final Test2Mapper test2Mapper;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveTest1Test2() {
        Test1 test1 = new Test1();
        test1.setName("name"+System.currentTimeMillis());
        test1.setValue("value"+System.currentTimeMillis());
        test1Mapper.insert(test1);
        Test2 test2 = new Test2();
        test2.setName("name"+System.currentTimeMillis());
        test2.setValue("value"+System.currentTimeMillis());
        test2Mapper.insert(test2);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveTest1Test2WithException() throws Exception {
        Test1 test1 = new Test1();
        test1.setName("name"+System.currentTimeMillis());
        test1.setValue("value"+System.currentTimeMillis());
        test1Mapper.insert(test1);
        Test2 test2 = new Test2();
        test2.setId(test1.getId());
        test2.setName("name"+System.currentTimeMillis());
        test2.setValue("value"+System.currentTimeMillis());
        test2Mapper.insert(test2);
        throw new RuntimeException("手动抛出异常！");
    }

}
