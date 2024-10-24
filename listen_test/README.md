# Request Logging Listener 实现与测试说明

## 余水深 软件工程2203班 2100150182

## 简介

本文档介绍了如何在 Java Servlet 中实现并测试 HTTP 请求的日志记录功能。通过 `RequestLoggingListener` 监听器，可以记录每个请求的详细信息（如客户端 IP、请求方法、URI、User-Agent 等）及其处理时间。

## 实现步骤

### 1. 创建 `TestServlet`

该 `TestServlet` 用于测试请求监听器功能。其主要作用是通过一个简单的 GET 请求返回确认信息。

```java
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Request logging test successful!");
    }
}
```

### 2. 创建 `RequestLoggingListener`

`RequestLoggingListener` 实现了 `ServletRequestListener` 接口，用于监听和记录 HTTP 请求的详细信息。该监听器会在请求初始化时记录客户端 IP、请求方法、URI 等信息，并在请求销毁时记录请求处理时间。

```java
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@WebListener
public class RequestLoggingListener implements ServletRequestListener {

    private static final Logger logger = Logger.getLogger(RequestLoggingListener.class.getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        String clientIp = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString() != null ? request.getQueryString() : "";
        String userAgent = request.getHeader("User-Agent");

        logger.info(String.format("Request Initialized: IP=%s, Method=%s, URI=%s, Query=%s, User-Agent=%s, StartTime=%d",
                clientIp, method, uri, queryString, userAgent, startTime));
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info(String.format("Request Destroyed: URI=%s, Duration=%d ms", request.getRequestURI(), duration));
    }
}
```

## 测试步骤

1. **编写 Servlet 和 Listener：** 按照上文创建 `TestServlet` 和 `RequestLoggingListener`。

2. **部署到服务器：** 将编译好的代码部署到支持 Servlet 的服务器（如 Tomcat）。

3. **访问测试资源：** 在浏览器中访问 `/test` URL，例如：

   ```
   http://localhost:8080/your-app/test
   ```

   确认页面显示：`Request logging test successful!`

4. **查看日志：** 检查服务器日志文件，确认其中包含类似以下的请求记录信息：

   ```java
   Request Initialized: IP=127.0.0.1, Method=GET, URI=/test, Query=, User-Agent=Mozilla/5.0, StartTime=...
   Request Destroyed: URI=/test, Duration=20 ms
   ```

这样就完成了日志监听器的实现与测试。