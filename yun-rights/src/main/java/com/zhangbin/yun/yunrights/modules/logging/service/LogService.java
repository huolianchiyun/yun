package com.zhangbin.yun.yunrights.modules.logging.service;

import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDo;
import com.zhangbin.yun.yunrights.modules.logging.model.dto.LogQueryCriteria;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface LogService {

    /**
     * 分页查询
     *
     * @param criteria 查询条件
     * @return /
     */
    PageInfo<Object> queryAll(LogQueryCriteria criteria);


    /**
     * 查询分页用户日志
     *
     * @param criteria 查询条件
     * @return -
     */
    Object queryAllByUser(LogQueryCriteria criteria);

    /**
     * 保存日志数据
     *
     * @param username  用户
     * @param browser   浏览器
     * @param ip        请求IP
     * @param joinPoint
     * @param log       日志实体
     */
    @Async
    void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, LogDo log);

    /**
     * 查询异常详情
     *
     * @param id 日志ID
     * @return Object
     */
    Object findByErrDetail(Long id);


    /**
     * 导出日志
     *
     * @param criteria   查询条件
     * @param response /
     * @throws IOException /
     */
    void download(LogQueryCriteria criteria, HttpServletResponse response) throws IOException;

    /**
     * 删除所有错误日志
     */
    void delAllByError();

    /**
     * 删除所有INFO日志
     */
    void delAllByInfo();
}
