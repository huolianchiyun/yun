package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictionaryDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DictQueryCriteria;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DictService {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    DictionaryDO queryById(Long id);

    /**
     * 分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    PageInfo<List<DictionaryDO>> queryAllByCriteria(DictQueryCriteria criteria);

    /**
     * 不分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    List<DictionaryDO> queryAllByCriteriaWithNoPage(DictQueryCriteria criteria);

    /**
     * 新增
     *
     * @param dict /
     */
    void create(DictionaryDO dict);

    /**
     * 编辑
     *
     * @param dict /
     */
    void update(DictionaryDO dict);

    /**
     * 删除
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 导出数据
     *
     * @param dictionaryList 待导出的数据
     * @param response   /
     * @throws IOException /
     */
    void download(List<DictionaryDO> dictionaryList, HttpServletResponse response) throws IOException;

}
