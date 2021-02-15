package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.common.spring.SpringContextHolder;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.command.query.RecordInfoQueryCmd;
import com.hlcy.yun.gb28181.service.params.query.CatalogQueryParams;
import com.hlcy.yun.gb28181.service.params.query.DeviceInfoQueryParams;
import com.hlcy.yun.gb28181.service.params.query.QueryParams;
import com.hlcy.yun.gb28181.service.command.query.AbstractQueryCmd;
import com.hlcy.yun.gb28181.service.command.query.CatalogQueryCmd;
import com.hlcy.yun.gb28181.service.command.query.DeviceInfoQueryCmd;
import com.hlcy.yun.gb28181.service.params.query.RecordInfoQueryParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备查询操作器
 */
@Service
@RequiredArgsConstructor
public class QueryOperator<T extends QueryParams> implements InitializingBean {
    private Map<Class, AbstractQueryCmd> cmdFactory = new HashMap<>(2);
    private final GB28181Properties properties;

    public static QueryOperator getInstance() {
        return SpringContextHolder.getBean(QueryOperator.class);
    }

    @SuppressWarnings("unchecked")
    public void operate(T operateParam) {
        final AbstractQueryCmd cmd = cmdFactory.get(operateParam.getClass());
        cmd.execute(operateParam);
    }

    @Override
    public void afterPropertiesSet() {
        cmdFactory.put(CatalogQueryParams.class, new CatalogQueryCmd(properties));
        cmdFactory.put(DeviceInfoQueryParams.class, new DeviceInfoQueryCmd(properties));
        cmdFactory.put(RecordInfoQueryParams.class, new RecordInfoQueryCmd(properties));
    }
}
