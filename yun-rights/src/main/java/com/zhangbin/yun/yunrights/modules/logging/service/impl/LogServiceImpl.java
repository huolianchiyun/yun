package com.zhangbin.yun.yunrights.modules.logging.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.constant.Constants;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.FileUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.IPUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.ValidationUtil;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.logging.enums.LogLevel;
import com.zhangbin.yun.yunrights.modules.logging.mapper.LogDoMapper;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDo;
import com.zhangbin.yun.yunrights.modules.logging.model.dto.LogErrorDTO;
import com.zhangbin.yun.yunrights.modules.logging.model.dto.LogQueryCriteria;
import static com.zhangbin.yun.yunrights.modules.logging.model.dto.LogQueryCriteria.BlurryType.USER_NAME;
import com.zhangbin.yun.yunrights.modules.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    private final LogDoMapper logDoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, LogDo log) {
        fillSomeValues2LogDo(username, browser, ip, joinPoint, log);
        logDoMapper.insert(log);
    }


    @Override
    public Object findByErrDetail(Long id) {
        LogDo log = Optional.of(logDoMapper.selectByPrimaryKey(id)).orElseGet(LogDo::new);
        ValidationUtil.isNull(log.getId(), "Log", "id", id);
        byte[] details = log.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : Constants.EMPTY_STR.getBytes()));
    }


    @Override
    public PageInfo<Object> queryAll(LogQueryCriteria criteria) {
        Page<LogDo> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, logDoMapper);
        PageInfo<Object> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setData(page.getResult());
        if (LogLevel.ERROR.equals(criteria.getLogLevel())) {
            pageInfo.setData(page.getResult().stream().map(LogErrorDTO::new).collect(Collectors.toList()));
        }
        return pageInfo;
    }

    @Override
    public Object queryAllByUser(LogQueryCriteria criteria) {
        criteria.setBlurryType(USER_NAME);
        Page<LogDo> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, logDoMapper);
        PageInfo<Object> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setData(page.getResult());
        return pageInfo;
    }


    @Override
    public void download(LogQueryCriteria criteria, HttpServletResponse response) throws IOException {
        criteria.setPageNum(1);
        criteria.setPageSize(500);
        Page<LogDo> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, logDoMapper);
        String tempPath = FileUtil.SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        try (BigExcelWriter writer = ExcelUtil.getBigWriter(file); ServletOutputStream out = response.getOutputStream()) {
            writer.write(convertLogDoList2MapList(page.getResult()), true);
            int pages = page.getPages();
            for (int i = 2; i <= pages; i++) {
                criteria.setPageNum(i);
                page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, logDoMapper);
                writer.write(convertLogDoList2MapList(page.getResult()), true);
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
    public void delAllByError() {
        logDoMapper.deleteByLogLevel(LogLevel.ERROR.getLevel());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        logDoMapper.deleteByLogLevel(LogLevel.INFO.getLevel());
    }

    private void fillSomeValues2LogDo(String userName, String browser, String ip, ProceedingJoinPoint joinPoint, LogDo log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Logging aopLog = signature.getMethod().getAnnotation(Logging.class);
        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + Constants.POINT + signature.getName() + Constants.PARENTHESES;
        StringBuilder params = new StringBuilder(Constants.LEFT_BRACE);
        //参数值
        List<Object> argValues = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
        //参数名称
        for (Object argValue : argValues) {
            params.append(argValue).append(Constants.ONE_SPACE);
        }
        // 描述
        if (log != null) {
            log.setOperationDesc(aopLog.value());
        }
        assert log != null;
        log.setClientIp(ip);

        String loginPath = "login";
        if (loginPath.equals(signature.getName())) {
            try {
                userName = new JSONObject(argValues.get(0)).get("userName").toString();
            } catch (Exception e) {
                LogServiceImpl.log.error(e.getMessage(), e);
            }
        }
        log.setAddress(IPUtil.getCityInfo(log.getClientIp()));
        log.setRequestMethod(methodName);
        log.setUserName(userName);
        log.setRequestParams(params.toString() + Constants.ONE_SPACE + Constants.LEFT_BRACE);
        log.setBrowser(browser);
    }


    private List<Map<String, Object>> convertLogDoList2MapList(List<LogDo> logs) {
        return logs.stream().map(e -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", e.getUserName());
            map.put("IP", e.getClientIp());
            map.put("IP来源", e.getAddress());
            map.put("描述", e.getOperationDesc());
            map.put("浏览器", e.getBrowser());
            map.put("请求耗时/毫秒", e.getRequestTimeConsuming());
            map.put("异常详情", new String(ObjectUtil.isNotNull(e.getExceptionDetail()) ? e.getExceptionDetail() : Constants.EMPTY_STR.getBytes()));
            map.put("创建日期", e.getCreateTime());
            return map;
        }).collect(Collectors.toList());
    }

}
