package com.zhangbin.yun.yunrights.modules.logging.service;

import com.zhangbin.yun.common.page.PageInfo;
import com.zhangbin.yun.common.utils.download.DownLoadSupport;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDO;
import com.zhangbin.yun.yunrights.modules.logging.model.criteria.LogQueryCriteria;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface LogService extends DownLoadSupport {

    /**
     * 分页查询满足条件的操作日志
     *
     * @param criteria 查询条件
     * @return /
     */
    PageInfo<List<LogDO>> queryByCriteria(LogQueryCriteria criteria);

    /**
     * 查询异常详情
     *
     * @param id 日志ID
     * @return /
     */
    Object queryExceptionalDetailById(Long id);

    /**
     * 保存日志数据
     *
     * @param browser   浏览器
     * @param ip        请求IP
     * @param joinPoint
     * @param log       日志实体
     */
    @Async
    void saveLog(String browser, String ip, ProceedingJoinPoint joinPoint, LogDO log);

    /**
     * 导出日志
     *
     * @param criteria 查询条件
     * @param response /
     */
    void download(LogQueryCriteria criteria, HttpServletResponse response);

    /**
     * 删除所有错误日志
     */
    void deleteAllLogsByErrorLevel();

    /**
     * 删除所有INFO日志
     */
    void delAllLogsByInfoLevel();
}
