package com.seciii.prism030.core.config.poolconfig.factory;

import com.alibaba.dashscope.aigc.generation.Generation;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 池化对象工厂类
 *
 * @author xueruichen
 * @date 2024.05.28
 */
public class PooledDashScopeObjectFactory extends BasePooledObjectFactory<Generation> {
    @Override
    public Generation create() {
        return new Generation();
    }

    @Override
    public PooledObject<Generation> wrap(Generation generation) {
        return new DefaultPooledObject<>(generation);
    }
}
