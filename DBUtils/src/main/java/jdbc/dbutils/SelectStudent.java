package jdbc.dbutils;

import org.apache.commons.dbutils.DbUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "selectStudent",value = "/students")
public class  SelectStudent extends HttpServlet {
    // 数据库数据
    // 替换为实际数据库名
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASSWORD = "mzyudada";
    private ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //查询部分
        try{
            Class.forName(JDBC_DRIVER); // 注册驱动程序
            //创建连接
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
            //执行查询
            String sql = "select * from student"; // 查询语句
            pstmt = conn.prepareStatement(sql); // 执行查询
            rs = pstmt.executeQuery(); // 查询结果
            List<Student> students = new ArrayList<>(); // 列表储存查询结果

            while (rs.next()){
                Student student = new Student();
                student.setSno(rs.getInt("Sno"));
                student.setSname(rs.getString("Sname"));
                student.setSage(rs.getInt("Sage"));
                student.setSsex(rs.getString("Ssex"));
                student.setSdept(rs.getString("Sdept"));
                students.add(student);
            }
            response.getWriter().write(objectMapper.writeValueAsString(students));
        } catch (SQLException e) {
            //创建一个错误类用于返回
            ErrorObject errorObject = new ErrorObject();
            errorObject.setErrorCode(666);
            errorObject.setErrorMessage("查询失败");
            response.getWriter().write(objectMapper.writeValueAsString(errorObject));
            e.printStackTrace();
        }catch (Exception e){
                ErrorObject errorObject = new ErrorObject();
                errorObject.setErrorCode(555);
                errorObject.setErrorMessage("服务器问题");
                out.print(objectMapper.writeValueAsString(errorObject));
                e.printStackTrace();
        }finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(conn);
            System.out.println("关闭连接");
        }
    }
}
