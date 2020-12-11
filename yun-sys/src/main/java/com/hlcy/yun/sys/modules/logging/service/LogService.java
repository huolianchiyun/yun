package com.hlcy.yun.sys.modules.logging.service;

import com.hlcy.yun.sys.modules.logging.model.criteria.LogQueryCriteria;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.sys.modules.logging.enums.LogLevel;
import com.hlcy.yun.sys.modules.logging.model.$do.LogDO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LogService extends DownLoadSupport {

    /**
     * 导出日志
     *
     * @param criteria 查询条件
     * @param response /
     */
    void download(LogQueryCriteria criteria, HttpServletResponse response);

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
     * 根据日志等级删除日志
     * @param logLevel 日志等级
     */
    void delLogsByLevel(LogLevel logLevel);

}
