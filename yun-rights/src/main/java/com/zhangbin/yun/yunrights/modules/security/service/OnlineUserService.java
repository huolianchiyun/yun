package com.zhangbin.yun.yunrights.modules.security.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.zhangbin.yun.yunrights.modules.common.constant.Constants;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.security.cache.UserInfoCache;
import com.zhangbin.yun.yunrights.modules.security.config.bean.SecurityProperties;
import com.zhangbin.yun.yunrights.modules.security.model.dto.MyUserDetails;
import com.zhangbin.yun.yunrights.modules.security.model.dto.OnlineUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.zhangbin.yun.yunrights.modules.common.xcache.CacheKey.BIND_USER_HASH_KEY_PREFIX;

@Service
@Slf4j
public class OnlineUserService {

    private final SecurityProperties properties;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public OnlineUserService(SecurityProperties properties) {
        this.properties = properties;
    }

    /**
     * 保存在线用户信息
     *
     * @param myUserDetails /
     * @param token         /
     * @param request       /
     */
    public void save(MyUserDetails myUserDetails, String token, HttpServletRequest request) {
        GroupDO dept = myUserDetails.getUser().getDept();
        String deptStr = dept != null ? dept.getGroupName() : Constants.EMPTY_STR;
        String ip = IPUtil.getIp(request);
        String browser = IPUtil.getBrowser(request);
        String address = IPUtil.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(myUserDetails.getUsername(), deptStr, browser, ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        redisUtils.set(properties.getOnlineKey() + token, onlineUser, properties.getTokenValidityInSeconds() / 1000);
    }

    /**
     * 查询全部数据
     *
     * @param filter   /
     * @param pageInfo /
     * @return /
     */
    public PageInfo<List<OnlineUser>> getAllOnlineUsersWithPage(String filter, PageInfo<List<OnlineUser>> pageInfo) {
        List<OnlineUser> onlineUsers = getAllOnlineUsersWithNoPage(filter);
        pageInfo.setData(PageUtil.toPage(pageInfo.getPageNum(), pageInfo.getPageSize(), onlineUsers));
        pageInfo.setTotal(Integer.toUnsignedLong(onlineUsers.size()));
        return pageInfo;
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUser> getAllOnlineUsersWithNoPage(String filter) {
        List<String> keys = redisUtils.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = (OnlineUser) redisUtils.get(key);
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUser.toString().contains(filter)) {
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }

    /**
     * 踢出用户
     *
     * @param key /
     */
    public void kickOut(String key) {
        key = properties.getOnlineKey() + key;
        redisUtils.del(key);
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        String key = properties.getOnlineKey() + token;
        OnlineUser onlineUser = (OnlineUser) redisUtils.get(key);
        if (onlineUser != null && StringUtils.isNotEmpty(onlineUser.getUserName())) {
            UserInfoCache.cleanCacheFor(onlineUser.getUserName());
            redisUtils.del(BIND_USER_HASH_KEY_PREFIX + onlineUser.getUserName());
        }
        redisUtils.del(key);
    }

    /**
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws IOException /
     */
    public void download(List<OnlineUser> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUser user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("部门", user.getDept());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public OnlineUser getOne(String key) {
        return JSON.parseObject(JSON.toJSONString(redisUtils.get(key)), OnlineUser.class, Feature.IgnoreNotMatch, Feature.IgnoreAutoType);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String ignoreToken) {
        List<OnlineUser> onlineUsers = getAllOnlineUsersWithNoPage(userName);
        if (onlineUsers == null || onlineUsers.isEmpty()) {
            return;
        }
        for (OnlineUser onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(userName)) {
                try {
                    String token = EncryptUtils.desDecrypt(onlineUser.getKey());
                    if (StringUtils.isNotBlank(ignoreToken) && !ignoreToken.equals(token)) {
                        this.kickOut(token);
                    } else if (StringUtils.isBlank(ignoreToken)) {
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    log.error("checkUser is error", e);
                }
            }
        }
    }

}
