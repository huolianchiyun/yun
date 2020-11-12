package com.yun.sys.modules.rights.service;

import com.yun.sys.modules.rights.model.$do.DictTypeDO;
import com.yun.sys.modules.rights.model.common.NameValue;
import com.yun.common.page.PageInfo;
import com.yun.sys.modules.rights.model.criteria.DictTypeQueryCriteria;

import java.util.List;
import java.util.Set;

public interface DictTypeService {
    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    DictTypeDO queryById(Long id);

    /**
     * 查询字典类型
     *
     * @return 子类类型集合
     */
    List<NameValue> queryDictType();

    /**
     * 分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    PageInfo<List<DictTypeDO>> queryByCriteria(DictTypeQueryCriteria criteria);
    /**
     * 新增
     *
     * @param dictType /
     */
    void create(DictTypeDO dictType);

    /**
     * 编辑
     *
     * @param dictType /
     */
    void update(DictTypeDO dictType);

    /**
     * 删除
     *
     * @param ids
     */
    void deleteByIds(Set<Long> ids);

}
