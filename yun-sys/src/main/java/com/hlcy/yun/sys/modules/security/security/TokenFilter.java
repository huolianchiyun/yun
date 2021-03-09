package com.hlcy.yun.sys.modules.security.security;

import cn.hutool.core.util.StrUtil;
import com.hlcy.yun.sys.modules.security.cache.UserInfoCache;
import com.hlcy.yun.sys.modules.security.config.bean.SecurityProperties;
import com.hlcy.yun.sys.modules.security.service.OnlineUserService;
import com.hlcy.yun.sys.modules.security.model.dto.OnlineUser;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {
    private static final String SEC_WEBSOCKET_PROTOCOL = "sec-websocket-protocol";
    private static final String WEBSOCKET_PROTOCOL_REGEX = "^/wss?/.+";
    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final UserInfoCache userInfoCache;
    private final OnlineUserService onlineUserService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        handleSecWebsocketProtocol(httpServletRequest, httpServletResponse);

        String token = getTokenFrom(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUser onlineUser = null;
            boolean cleanUserCache = false;
            try {
                onlineUser = onlineUserService.getOne(properties.getOnlineKey() + token);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUser)) {
                    userInfoCache.cleanCacheByUsername(String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY)));
                }
            }
            if (onlineUser != null && StringUtils.hasText(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void handleSecWebsocketProtocol(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (httpServletRequest.getRequestURI().matches(WEBSOCKET_PROTOCOL_REGEX)) {
            httpServletResponse.setHeader(SEC_WEBSOCKET_PROTOCOL, httpServletRequest.getHeader(SEC_WEBSOCKET_PROTOCOL));
        }
    }

    private String getTokenFrom(HttpServletRequest httpServletRequest) {
        return resolveToken(httpServletRequest);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = null;
        try {
            bearerToken = request.getRequestURI().matches(WEBSOCKET_PROTOCOL_REGEX)
                    ? URLDecoder.decode(request.getHeader(SEC_WEBSOCKET_PROTOCOL), "UTF-8")
                    : request.getHeader(properties.getHeader());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
