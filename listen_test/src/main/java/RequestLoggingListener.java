import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * 该类实现了 ServletRequestListener 接口，用于监听和记录每个 HTTP 请求的详细信息，
 * 包括客户端 IP、请求方法、URI、查询字符串、User-Agent 以及请求的处理时间。
 */
@WebListener // 通过 @WebListener 注解自动将该类注册为 ServletRequestListener
public class RequestLoggingListener implements ServletRequestListener {

    // 使用 java.util.logging.Logger 来记录日志信息
    private static final Logger logger = Logger.getLogger(RequestLoggingListener.class.getName());

    /**
     * 当请求初始化（即客户端发送请求时）会调用此方法。
     * 该方法记录请求的开始时间、客户端 IP、请求方法、URI 等信息，并将开始时间存储到请求属性中。
     *
     * @param sre ServletRequestEvent 包含请求的详细信息
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        // 将 ServletRequest 转换为 HttpServletRequest，以便访问 HTTP 特有的信息
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        // 获取当前系统时间戳，表示请求开始的时间
        long startTime = System.currentTimeMillis();

        // 将请求开始时间存储到请求属性中，后续在 requestDestroyed 中用来计算处理时间
        request.setAttribute("startTime", startTime);

        // 获取客户端的 IP 地址
        String clientIp = request.getRemoteAddr();

        // 获取请求方法（例如：GET、POST）
        String method = request.getMethod();

        // 获取请求的 URI（例如：/api/resource）
        String uri = request.getRequestURI();

        // 获取查询字符串（如果有则记录，否则为空）
        String queryString = request.getQueryString() != null ? request.getQueryString() : "";

        // 获取 User-Agent 头信息（客户端浏览器或其他请求工具的标识）
        String userAgent = request.getHeader("User-Agent");

        // 记录请求初始化的日志，包括客户端 IP、请求方法、URI、查询字符串、User-Agent 和请求开始时间
        logger.info(String.format("Request Initialized: IP=%s, Method=%s, URI=%s, Query=%s, User-Agent=%s, StartTime=%d",
                clientIp, method, uri, queryString, userAgent, startTime));
    }

    /**
     * 当请求结束时会调用此方法。
     * 该方法计算并记录请求处理的总时长（从初始化到销毁），并记录请求 URI 和处理时间。
     *
     * @param sre ServletRequestEvent 包含请求的详细信息
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // 将 ServletRequest 转换为 HttpServletRequest，以便访问 HTTP 特有的信息
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        // 从请求属性中获取开始时间（在 requestInitialized 中存储的时间戳）
        long startTime = (long) request.getAttribute("startTime");

        // 获取当前系统时间戳，表示请求结束的时间
        long endTime = System.currentTimeMillis();

        // 计算请求的处理时长（结束时间减去开始时间）
        long duration = endTime - startTime;

        // 记录请求结束的日志，包含请求 URI 和处理时间（以毫秒为单位）
        logger.info(String.format("Request Destroyed: URI=%s, Duration=%d ms", request.getRequestURI(), duration));
    }
}
