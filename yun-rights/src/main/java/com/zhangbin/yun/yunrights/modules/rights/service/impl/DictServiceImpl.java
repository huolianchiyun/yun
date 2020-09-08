package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.mapper.DictionaryMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictionaryDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DictQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dict")
public class DictServiceImpl implements DictService {

    private final DictionaryMapper dictionaryMapper;

    @Override
    @Cacheable(key = "'id:' + #p0")
    public DictionaryDO queryById(Long id) {
        return dictionaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<List<DictionaryDO>> queryAllByCriteria(DictQueryCriteria criteria) {
        Page<DictionaryDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, dictionaryMapper);
        PageInfo<List<DictionaryDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<DictionaryDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<DictionaryDO> queryAllByCriteriaWithNoPage(DictQueryCriteria criteria) {
        return CollectionUtil.list(false, dictionaryMapper.selectAllByCriteria(criteria));
    }

    @Override
    public void create(DictionaryDO dict) {
        dictionaryMapper.insert(dict);
    }

    @Override
    public void update(DictionaryDO dict) {
        dictionaryMapper.updateByPrimaryKeySelective(dict);
    }


    @Override
    public void deleteById(Long id) {
        dictionaryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void download(List<DictionaryDO> userDOList, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(Optional.ofNullable(userDOList).orElseGet(ArrayList::new).stream().map(DictionaryDO::toLinkedMap).collect(Collectors.toList()), response);
    }
}
