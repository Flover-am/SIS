package com.seciii.prism030.core.config.poolconfig.pool;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.seciii.prism030.common.exception.LLMException;
import com.seciii.prism030.common.exception.error.ErrorType;
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
public class GenerationPool {
    private final GenericObjectPool<Generation> pool;

    public GenerationPool(PooledObjectFactory<Generation> factory) {
        pool = new GenericObjectPool<>(factory);
    }

    public GenerationPool(PooledObjectFactory<Generation> factory, GenericObjectPoolConfig<Generation> config) {
        pool = new GenericObjectPool<>(factory, config);
    }

    public GenerationPool(PooledObjectFactory<Generation> factory, GenericObjectPoolConfig<Generation> config, AbandonedConfig abandonedConfig) {
        pool = new GenericObjectPool<>(factory, config, abandonedConfig);
    }

    public Generation borrowObject() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new LLMException(ErrorType.LLM_RESULT_ERROR);
        }
    }

    public void returnObject(Generation client) {
        pool.returnObject(client);
    }

    public void close() {
        pool.close();
    }
}
