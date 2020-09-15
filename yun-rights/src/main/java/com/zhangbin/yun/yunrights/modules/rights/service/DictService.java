package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DictQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;

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
    DictDO queryById(Long id);

    /**
     * 查询字典类型
     *
     * @return 子类类型集合
     */
    List<NameValue> queryDictType();

    /**
     * 根据字典类型查询
     * @param typeCode /
     * @return 子类类型集合
     */
    List<NameValue> queryByDictType(String typeCode);

    /**
     * 分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    PageInfo<List<DictDO>> queryAllByCriteria(DictQueryCriteria criteria);

    /**
     * 不分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    List<DictDO> queryAllByCriteriaWithNoPage(DictQueryCriteria criteria);

    /**
     * 新增
     *
     * @param dict /
     */
    void create(DictDO dict);

    /**
     * 编辑
     *
     * @param dict /
     */
    void update(DictDO dict);

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
     * @param response       /
     * @throws IOException /
     */
    void download(List<DictDO> dictionaryList, HttpServletResponse response) throws IOException;

}
