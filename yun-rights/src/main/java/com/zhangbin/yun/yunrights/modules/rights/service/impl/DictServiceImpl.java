package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.mapper.DictMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DictQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;
import com.zhangbin.yun.yunrights.modules.rights.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dict")
public class DictServiceImpl implements DictService {

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
    public PageInfo<List<DictDO>> queryAllByCriteria(DictQueryCriteria criteria) {
        Page<DictDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, dictMapper);
        PageInfo<List<DictDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<DictDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<DictDO> queryAllByCriteriaWithNoPage(DictQueryCriteria criteria) {
        return CollectionUtil.list(false, dictMapper.selectAllByCriteria(criteria));
    }

    @Override
    public void create(DictDO dict) {
        dictMapper.insert(dict);
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
    public void deleteByIds(Set<Long> ids) {
        for (Long id : ids) {
            Assert.isTrue(!dictMapper.selectStatusById(id), "该字典已经激活，不能删除！");
        }
        dictMapper.deleteByIds(ids);
    }

    @Override
    public void download(List<DictDO> userDOList, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(Optional.ofNullable(userDOList).orElseGet(ArrayList::new).stream().map(DictDO::toLinkedMap).collect(Collectors.toList()), response);
    }
}
