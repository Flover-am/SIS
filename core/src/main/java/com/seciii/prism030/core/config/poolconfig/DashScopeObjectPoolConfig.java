package com.seciii.prism030.core.config.poolconfig;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.aliyun.dashvector.DashVectorClient;
import com.seciii.prism030.core.config.DashVectorConfig;
import com.seciii.prism030.core.config.poolconfig.factory.PooledDashScopeObjectFactory;
import com.seciii.prism030.core.config.poolconfig.factory.PooledDashVectorObjectFactory;
import com.seciii.prism030.core.config.poolconfig.pool.DashVectorClientPool;
import com.seciii.prism030.core.config.poolconfig.pool.GenerationPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.annotation.PreDestroy;

/**
 * DashScope对象池配置对象
 *
 * @author xueruichen
 * @date 2024.05.28
 */
@Configuration
public class DashScopeObjectPoolConfig {
    private GenerationPool generationPool;

    private DashVectorClientPool dashVectorClientPool;

    @Autowired
    private DashVectorConfig dashVectorConfig;

    @Bean
    protected GenerationPool generationPool() {
        PooledDashScopeObjectFactory pooledDashScopeObjectFactory = new PooledDashScopeObjectFactory();
        GenericObjectPoolConfig<Generation> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        generationPool = new GenerationPool(pooledDashScopeObjectFactory, config);
        return generationPool;
    }

    @Bean
    protected DashVectorClientPool dashVectorClientPool() {
        PooledDashVectorObjectFactory pooledDashVectorObjectFactory = new PooledDashVectorObjectFactory(dashVectorConfig);
        GenericObjectPoolConfig<DashVectorClient> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        dashVectorClientPool = new DashVectorClientPool(pooledDashVectorObjectFactory, config);
        return dashVectorClientPool;
    }

    @PreDestroy
    public void destroy() {
        if (generationPool != null) {
            generationPool.close();
        }

        if (dashVectorClientPool != null) {
            dashVectorClientPool.close();
        }
    }
}
