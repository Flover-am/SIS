package com.seciii.prism030.core.config.poolconfig.pool;

import com.aliyun.dashvector.DashVectorClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author xueruichen
 * @date 2024.05.28
 */
public class DashVectorClientPool extends GenericObjectPool<DashVectorClient> {
    public DashVectorClientPool(PooledObjectFactory<DashVectorClient> factory) {
        super(factory);
    }

    public DashVectorClientPool(PooledObjectFactory<DashVectorClient> factory, GenericObjectPoolConfig<DashVectorClient> config) {
        super(factory, config);
    }

    public DashVectorClientPool(PooledObjectFactory<DashVectorClient> factory, GenericObjectPoolConfig<DashVectorClient> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
