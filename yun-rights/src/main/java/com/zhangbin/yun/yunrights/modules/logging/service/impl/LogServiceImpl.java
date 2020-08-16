package com.zhangbin.yun.yunrights.modules.logging.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.constant.Constants;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.FileUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.IPUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.ValidationUtil;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.logging.enums.LogLevel;
import com.zhangbin.yun.yunrights.modules.logging.mapper.LogMapper;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDO;
import com.zhangbin.yun.yunrights.modules.logging.model.criteria.LogQueryCriteria;
import com.zhangbin.yun.yunrights.modules.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogMapper logMapper;

    @Override
    public PageInfo<List<LogDO>> queryAllByCriteria(LogQueryCriteria criteria) {
        Page<LogDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, logMapper);
        PageInfo<List<LogDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<LogDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public Object queryExceptionalDetailById(Long id) {
        LogDO log = Optional.of(logMapper.selectByPrimaryKey(id)).orElseGet(LogDO::new);
        ValidationUtil.isNull(log.getId(), "Log", "id", id);
        byte[] details = log.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : Constants.EMPTY_STR.getBytes()));
    }

    @Override
    public void saveLog(String username, String browser, String ip, ProceedingJoinPoint joinPoint, LogDO log) {
        fillSomeValues2LogDo(username, browser, ip, joinPoint, log);
        logMapper.insert(log);
    }

    @Override
    public void download(LogQueryCriteria criteria, HttpServletResponse response) throws IOException {
        criteria.setPageNum(1);
        criteria.setPageSize(500);
        Page<LogDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, logMapper);
        String tempPath = FileUtil.SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        try (BigExcelWriter writer = ExcelUtil.getBigWriter(file); ServletOutputStream out = response.getOutputStream()) {
            writer.write(convertLogDOList2MapList(page.getResult()), true);
            int pages = page.getPages();
            for (int i = 2; i <= pages; i++) {
                criteria.setPageNum(i);
                page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, logMapper);
                writer.write(convertLogDOList2MapList(page.getResult()), true);
            }
            // 一次性写出内容，使用默认样式，强制输出标题
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
            writer.flush(out, true);
        } finally {
            // 终止后删除临时文件
            file.deleteOnExit();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllLogsByErrorLevel() {
        logMapper.deleteByLogLevel(LogLevel.ERROR.getLevel());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllLogsByInfoLevel() {
        logMapper.deleteByLogLevel(LogLevel.INFO.getLevel());
    }

    private void fillSomeValues2LogDo(String userName, String browser, String ip, ProceedingJoinPoint joinPoint, LogDO log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Logging aopLog = signature.getMethod().getAnnotation(Logging.class);
        // 描述
        if (log != null) {
            log.setOperationDesc(aopLog.value());
        }
        assert log != null;
        log.setClientIp(ip);
        //参数值
        String params = JSON.toJSONString(joinPoint.getArgs()[0], SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.IgnoreNonFieldGetter);
        String loginPath = "login";
        if (loginPath.equals(signature.getName())) {
            try {
                userName = new JSONObject(params).getStr("userName");
            } catch (Exception e) {
                LogServiceImpl.log.error(e.getMessage(), e);
            }
        }
        log.setAddress(IPUtil.getCityInfo(log.getClientIp()));
        String methodName = joinPoint.getTarget().getClass().getName() + Constants.POINT + signature.getName() + Constants.PARENTHESES;
        log.setRequestMethod(methodName);
        log.setOperator(userName);
        log.setRequestParams(params);
        log.setBrowser(browser);
    }

    private List<Map<String, Object>> convertLogDOList2MapList(List<LogDO> logs) {
        return Optional.of(logs).orElseGet(ArrayList::new).stream().map(LogDO::toLinkedMap).collect(Collectors.toList());
    }

}
