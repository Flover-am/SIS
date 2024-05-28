package com.seciii.prism030.core.config.poolconfig.pool;

import com.alibaba.dashscope.aigc.generation.Generation;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 实例GenerationPool
 *
 * @author xueruichen
 * @date 2024.05.28
 */
public class GenerationPool extends GenericObjectPool<Generation> {
    public GenerationPool(PooledObjectFactory<Generation> factory) {
        super(factory);
    }

    public GenerationPool(PooledObjectFactory<Generation> factory, GenericObjectPoolConfig<Generation> config) {
        super(factory, config);
    }

    public GenerationPool(PooledObjectFactory<Generation> factory, GenericObjectPoolConfig<Generation> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
