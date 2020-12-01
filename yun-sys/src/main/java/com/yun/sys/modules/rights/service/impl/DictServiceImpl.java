package com.yun.sys.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.yun.sys.modules.rights.model.common.NameValue;
import com.yun.common.page.PageInfo;
import com.yun.common.mybatis.page.PageQueryHelper;
import com.yun.sys.modules.rights.mapper.DictMapper;
import com.yun.sys.modules.rights.model.$do.DictDO;
import com.yun.sys.modules.rights.model.criteria.DictQueryCriteria;
import com.yun.sys.modules.rights.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dict")
class DictServiceImpl implements DictService {

    private final DictMapper dictMapper;

    @Override
    @Cacheable(key = "'id:' + #p0")
    public DictDO queryById(Long id) {
        return dictMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<NameValue> queryByDictType(String typeCode) {
        return dictMapper.selectByTypeCode(typeCode)
                .stream()
                .sorted((Comparator.comparing(DictDO::getSort)))
                .map(DictDO::toNameValue)
                .collect(Collectors.toList());
    }

    @Override
    public PageInfo<List<DictDO>> queryByCriteria(DictQueryCriteria criteria) {
        Page<DictDO> page = PageQueryHelper.queryByCriteriaWithPage(criteria, dictMapper);
        PageInfo<List<DictDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<DictDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<DictDO> queryAllByCriteriaWithNoPage(DictQueryCriteria criteria) {
        return CollectionUtil.list(false, dictMapper.selectByCriteria(criteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DictDO dict) {
        dictMapper.insert(dict);
        dictMapper.updateByPrimaryKeySelective(new DictDO(dict.getId(), dict.getCode() + ":" + dict.getId()));
    }

    @Override
    public void update(DictDO dict) {
        DictDO dictDB = dictMapper.selectByPrimaryKey(dict.getId());
        if (dictDB.getStatus()) {
            Assert.isTrue(dict.getCode().equals(dictDB.getCode()), "该字典已经激活，不能修改其编码！");
        }
        dictMapper.updateByPrimaryKeySelective(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Set<Long> ids) {
        for (Long id : ids) {
            Assert.isTrue(!dictMapper.selectStatusById(id), "该字典已经激活，不能删除！");
        }
        dictMapper.deleteByIds(ids);
    }
}
