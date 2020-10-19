package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import com.github.pagehelper.Page;
import com.zhangbin.yun.common.page.PageInfo;
import com.zhangbin.yun.common.mybatis.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.DictTypeMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictTypeDO;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DictTypeQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dict_type")
public class DictTypeServiceImpl implements DictTypeService {

    private final DictTypeMapper dictTypeMapper;

    @Override
    @Cacheable(key = "'id:' + #p0")
    public DictTypeDO queryById(Long id) {
        return dictTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(key = "'type'")
    public List<NameValue> queryDictType() {
        return dictTypeMapper.selectAll()
                .stream()
                .sorted((Comparator.comparing(DictTypeDO::getSort)))
                .map(DictTypeDO::toNameValue)
                .collect(Collectors.toList());
    }

    @Override
    public PageInfo<List<DictTypeDO>> queryAllByCriteria(DictTypeQueryCriteria criteria) {
        Page<DictTypeDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, dictTypeMapper);
        PageInfo<List<DictTypeDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<DictTypeDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    @CacheEvict(key = "'type'")
    public void create(DictTypeDO dictType) {
        dictTypeMapper.insert(dictType);
    }

    @Override
    @CacheEvict(key = "'type'")
    public void update(DictTypeDO dictType) {
        DictTypeDO dictTypeDB = dictTypeMapper.selectByPrimaryKey(dictType.getId());
        Assert.isTrue(!(dictTypeMapper.selectUsedCount(dictType.getId()) > 0 && !dictType.getCode().equals(dictTypeDB.getCode())),
                "该字典类型已经被使用，不能删除！");
        dictTypeMapper.updateByPrimaryKeySelective(dictType);
    }

    @Override
    public void deleteByIds(Set<Long> ids) {
        for (Long id : ids) {
            Assert.isTrue(dictTypeMapper.selectUsedCount(id) <= 0, "该字典类型已经被使用，不能删除！");
        }
        dictTypeMapper.deleteByIds(ids);
    }
}
