package com.hlcy.yun.sys.modules.logging.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.hlcy.yun.sys.modules.logging.model.criteria.LogQueryCriteria;
import com.hlcy.yun.common.constant.Constants;
import com.hlcy.yun.common.mybatis.page.AbstractQueryPage;
import com.hlcy.yun.common.mybatis.page.PageMapper;
import com.hlcy.yun.common.mybatis.page.PageQuery;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.mybatis.page.PageQueryHelper;
import com.hlcy.yun.common.utils.ip.IPUtil;
import com.hlcy.yun.common.utils.validator.ValidationUtil;
import com.hlcy.yun.sys.modules.logging.annotation.Logging;
import com.hlcy.yun.sys.modules.logging.enums.LogLevel;
import com.hlcy.yun.sys.modules.logging.mapper.LogMapper;
import com.hlcy.yun.sys.modules.logging.model.$do.LogDO;
import com.hlcy.yun.sys.modules.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogMapper logMapper;

    @Override
    public PageInfo<List<LogDO>> queryByCriteria(LogQueryCriteria criteria) {
        Page<LogDO> page = PageQueryHelper.queryByCriteriaWithPage(criteria, logMapper);
        PageInfo<List<LogDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<LogDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public Object queryExceptionalDetailById(Long id) {
        LogDO log = Optional.ofNullable(logMapper.selectByPrimaryKey(id)).orElseGet(LogDO::new);
        ValidationUtil.isNull(log.getId(), "Log", "id", id);
        byte[] details = log.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : Constants.EMPTY_STR.getBytes()));
    }

    @Override
    public void saveLog(String browser, String ip, ProceedingJoinPoint joinPoint, LogDO log) {
        fillSomeValues2LogDo(browser, ip, joinPoint, log);
        logMapper.insert(log);
    }

    @Override
    public void download(LogQueryCriteria criteria, HttpServletResponse response) {
        downloadExcelWithPage(new PageQuery<LogQueryCriteria, LogDO>(criteria, logMapper) {
            @Override
            public Page<LogDO> queryByCriteriaWithPage(AbstractQueryPage queryPage, PageMapper<LogDO> mapper) {
                return PageQueryHelper.queryByCriteriaWithPage(queryPage, mapper);
            }
        }, response);
    }

    @Override
    public void delLogsByLevel(LogLevel logLevel) {
        logMapper.deleteByLogLevel(logLevel.getLevel());
    }

    private void fillSomeValues2LogDo(String browser, String ip, ProceedingJoinPoint joinPoint, LogDO log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Logging aopLog = signature.getMethod().getAnnotation(Logging.class);
        // 描述
        if (log != null) {
            log.setOperationDesc(aopLog.value());
        }
        assert log != null;
        log.setClientIp(ip);
        //参数值
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length >= 1) {
            String params = JSON.toJSONString(args[0], SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.IgnoreNonFieldGetter);
            log.setRequestParams(params);
        }
        log.setAddress(IPUtil.getCityInfo(log.getClientIp()));
        String methodName = joinPoint.getTarget().getClass().getName() + Constants.POINT + signature.getName() + Constants.PARENTHESES;
        log.setRequestMethod(methodName);
        log.setBrowser(browser);
    }
}
