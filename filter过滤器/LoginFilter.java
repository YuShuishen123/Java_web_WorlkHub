package sgg.demo2.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//通过注释的方法免去对web.xml文件的繁琐配置
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    //直接放行的静态请求资源列表
    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
            ".css", ".js", ".jpg", ".png", ".gif", ".ico", ".jsp"
    );

    // 定义排除列表，不需要登录访问的路径
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/login.html", "/register.html", "/public", "/index.html"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();

        if ((isStaticResource(requestURI)) || (isExcludedPath(requestURI))){
            // 请求的为静态资源或者排除列表内的路径则直接通过
            chain.doFilter(request, response);
        }else if (requestURI.endsWith("/welcome.html")) {
            // welcome.html 请求检查登录状态
            String username = (String) req.getSession().getAttribute("user");
            if ("".equals(username) || username == null) {
                // 未登录用户重定向到登录页面
                resp.sendRedirect(req.getContextPath() + "/login.html");
            } else {
                // 已登录用户放行请求
                chain.doFilter(request, response);
            }
        } else {
            // 其他请求直接通过
            chain.doFilter(request, response);
        }
    }
    // 判断是否为静态资源请求
    private boolean isStaticResource(String requestURI) {
        return STATIC_EXTENSIONS.stream().anyMatch(requestURI::contains);
    }

    // 判断当前请求路径是否在排除列表中
    private boolean isExcludedPath(String requestURI) {
        return EXCLUDED_PATHS.stream().anyMatch(requestURI::contains);
    }

}