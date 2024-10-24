##  作业一：Filter练习

姓名：余水深    学号：2100150182  班级：软件工程2203

**题目:** 实现一个登录验证过滤器

**目标:** 创建一个 Servlet的 过滤器,用于验证用户是否已登录。对于未登录的用户,将其**重定向**到登录页面。

##### 实现情况:

1. 检查当前请求是否是对登录页面、注册页面或公共资源的请求。如果是,则允许请求通过。 

2. 如果不是上述情况,检查用户的 session 中是否存在表示已登录的属性(如 "user" 属性)。

3. 如果用户已登录,允许请求继续。 

4. 如果用户未登录,将请求重定向到登录页面。

##### 项目结构：

<img src="D:\PixPin\Temp\Clip_2024-10-09_23-12-12.png" alt="Clip_2024-10-09_23-12-12" style="zoom:33%;" />

##### 实现一个方法来检查当前请求路径是否在排除列表中，方法和排除列表的定义如下：

​	该方法用于判断请求资源路径是否包含在排除列表中

```java
private static final List<String> EXCLUDED_PATHS = Arrays.asList(
         "/login.html", "/index.html"，"/error.html"
 );
private boolean isExcludedPath(String requestURI) {
    return EXCLUDED_PATHS.stream().anyMatch(requestURI::contains);
}
```

​	调用该方法进行判断，如果请求资源的路径不在排除列表中或者为不为静态资源，则进行登陆检测，

如果提取不到user信息，说明用户未进行登陆，则跳转到登陆界面，反之则放行，通过 login.servelet 的控制跳转到welcome.html界面

```java
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
```