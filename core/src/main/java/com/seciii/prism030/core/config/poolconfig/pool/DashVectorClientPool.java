package com.seciii.prism030.core.config.poolconfig.pool;

import com.aliyun.dashvector.DashVectorClient;
import com.seciii.prism030.common.exception.LLMException;
import com.seciii.prism030.common.exception.error.ErrorType;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author xueruichen
 * @date 2024.05.28
 */
public class DashVectorClientPool {
    private final GenericObjectPool<DashVectorClient> pool;

    public DashVectorClientPool(PooledObjectFactory<DashVectorClient> factory) {
        pool = new GenericObjectPool<>(factory);
    }

    public DashVectorClientPool(PooledObjectFactory<DashVectorClient> factory, GenericObjectPoolConfig<DashVectorClient> config) {
        pool = new GenericObjectPool<>(factory, config);
    }

    public DashVectorClientPool(PooledObjectFactory<DashVectorClient> factory, GenericObjectPoolConfig<DashVectorClient> config, AbandonedConfig abandonedConfig) {
        pool = new GenericObjectPool<>(factory, config, abandonedConfig);
    }

    public DashVectorClient borrowObject() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new LLMException(ErrorType.LLM_REQUEST_ERROR);
        }
    }

    public void returnObject(DashVectorClient client) {
        pool.returnObject(client);
    }

    public void close() {
        pool.close();
    }
}
