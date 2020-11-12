package com.yun.common.utils.download;

import com.yun.common.mybatis.page.PageQuery;
import com.yun.common.utils.download.excel.ExcelSupport;
import com.yun.common.model.BaseDO;
import com.yun.common.mybatis.page.AbstractQueryPage;
import com.yun.common.utils.io.FileUtil;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface DownLoadSupport<T extends ExcelSupport> {
    /**
     * 以 Excel形式导出数据
     *
     * @param collection 待导出的数据
     * @param response   请求响应对象
     */
    default void downloadExcel(Collection<T> collection, HttpServletResponse response) {
        FileUtil.downloadExcel(Optional.ofNullable(collection).orElseGet(ArrayList::new).stream().map(ExcelSupport::toLinkedMap).collect(Collectors.toList()), response);
    }

    /**T
     * 以 Excel形式导出数据且以分页方式
     * @param pageQuery   提供分页查询功能
     * @param response   请求响应对象
     */
    default void downloadExcelWithPage(PageQuery<AbstractQueryPage, BaseDO> pageQuery, HttpServletResponse response) {
        FileUtil.downloadExcelWithPage(response, pageQuery);
    }
}
