package com.zhangbin.yun.sys.modules.rights.service;

import com.zhangbin.yun.common.utils.download.DownLoadSupport;
import com.zhangbin.yun.sys.modules.rights.model.$do.DictDO;
import com.zhangbin.yun.sys.modules.rights.model.criteria.DictQueryCriteria;
import com.zhangbin.yun.sys.modules.rights.model.common.NameValue;

import java.util.List;
import java.util.Set;

public interface DictService extends PageService<DictQueryCriteria, DictDO>, DownLoadSupport<DictDO> {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    DictDO queryById(Long id);

    /**
     * 根据字典类型查询
     * @param typeCode /
     * @return 子类类型集合
     */
    List<NameValue> queryByDictType(String typeCode);

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
     * @param ids
     */
    void deleteByIds(Set<Long> ids);
}
