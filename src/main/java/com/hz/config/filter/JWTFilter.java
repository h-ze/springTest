package com.hz.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.hz.utils.JWTToken;
import com.hz.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends BasicHttpAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    private JWTUtil jwtUtil;
    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            logger.info("登录");
            try {
                executeLogin(request, response);
            } catch (ExpiredJwtException e){
                responseError(response, "token已过期,请重新登录");
                return false;
            } catch (MalformedJwtException e){
                responseError(response, "token错误,请输入正确的token");
                return false;
            } catch (Exception e){
                e.printStackTrace();
                responseError(response, e.getMessage());
                return false;
            }//token 错误

        } else {
            logger.info("token为空");
            responseError(response, "参数错误,请输入用户token");
            return false;
        }
        return true;
        //logger.info("请求进来");
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        //return true;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getParameter("token");
        //String token = req.getHeader("token");
        return token != null;
    }

    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getParameter("token");
        //String token = httpServletRequest.getHeader("token");
        Claims claims = JWTUtil.parseJWT(token);

        JWTToken jwtToken = new JWTToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获

        //UsernamePasswordToken jwtToken = new UsernamePasswordToken("test","123456");
        getSubject(request, response).login(jwtToken);
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        logger.info("进入jwtFilter");
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /unauthorized/**
     */
    private void responseError(ServletResponse response, String message) {
        logger.info("抛异常:"+message);
        //HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //设置编码，否则中文字符在重定向时会变为空字符串
        //message = URLEncoder.encode(message, "UTF-8");
        //httpServletResponse.sendRedirect("/unauthorized/" + message);

        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("state",false);
        jsonObject.put("msg",message);
        response.setContentType("application/json;charset=UTF-8");
        try {
            response.getWriter().println(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果这个Filter在之前isAccessAllowed（）方法中返回false,则会进入这个方法。我们这里直接返回错误的response
     *
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     * onAccessDenied是否执行取决于isAccessAllowed的值，如果返回true则onAccessDenied不会执行；如果返回false，执行onAccessDenied
     * 如果onAccessDenied也返回false，则直接返回，不会进入请求的方法（只有isAccessAllowed和onAccessDenied的情况下）
     * */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        //httpResponse.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        //fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }


}
