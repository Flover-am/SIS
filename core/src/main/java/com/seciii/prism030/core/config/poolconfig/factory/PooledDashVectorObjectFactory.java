package com.seciii.prism030.core.config.poolconfig.factory;

import com.aliyun.dashvector.DashVectorClient;
import com.seciii.prism030.core.config.DashVectorConfig;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author xueruichen
 * @date 2024.05.28
 */
public class PooledDashVectorObjectFactory extends BasePooledObjectFactory<DashVectorClient> {
    private final DashVectorConfig config;

    public PooledDashVectorObjectFactory(DashVectorConfig config) {
        this.config = config;
    }
    @Override
    public DashVectorClient create() {
        return new DashVectorClient(config.getApiKey(), config.getEndpoint());
    }

    @Override
    public PooledObject<DashVectorClient> wrap(DashVectorClient dashVectorClient) {
        return new DefaultPooledObject<>(dashVectorClient);
    }
}
