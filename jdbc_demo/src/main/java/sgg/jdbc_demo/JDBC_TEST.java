package sgg.jdbc_demo;

import java.sql.*;

public class JDBC_TEST {
    private static final String URL = "jdbc:mysql://localhost:3306/test"; // 替换为实际数据库名
    private static final String USER = "root";
    private static final String PASSWORD = "mzyudada";
    static String UpdateSql = "INSERT INTO sc1 (Sno, Cno, Grade) VALUES (?, ?, ?)";
    static String SelectSql = "SELECT * FROM student1";

    public static void main(String[] args) throws SQLException {
        // 直接在main方法中创建连接，后续调用个子方法都使用该连接
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("数据库连接成功！");
        try{
            // 示例调用插入方法
            System.out.println("示例调用插入方法");

            insertRecord(202115121, 9, 99, conn);
            // 示例查询方法
            System.out.println("示例调用查询方法");
            selectFunction(conn);

        }finally {
            if(conn != null){
                conn.close();
                System.out.println("连接关闭");
            }
        }

    }

    // 数据库查询方法
    public static void selectFunction(Connection conn) throws SQLException {
        // 建立查询
        PreparedStatement stmt = conn.prepareStatement(SelectSql);
        // 启动查询
        ResultSet rs = stmt.executeQuery();
        // 遍历查询结果
        while(rs.next()){
            System.out.println(rs.getObject(1)+" " + rs.getObject(2)+" "+rs.getObject(3)+" "+rs.getObject(4));
        }
        rs.close();
        System.out.println("rs关闭");
        stmt.close();
        System.out.println("stmt关闭");
    }

    // 数据库插入方法
    public static void insertRecord(int sno, int cno, int grade, Connection conn) throws SQLException {
        try{
            // 2. 关闭自动提交
            conn.setAutoCommit(false);

            SQLException ex = null;

            // 创建 statement 对象
            PreparedStatement stmt = conn.prepareStatement(UpdateSql);
            stmt.setInt(1, sno);
            stmt.setInt(2, cno);
            stmt.setInt(3, grade);
            stmt.executeUpdate();

            // 3. 提交事务
            conn.commit();
            System.out.println("数据插入成功！");
            stmt.close();
            System.out.println("stmt关闭");
        }catch (SQLException e){
            try {
                conn.rollback();
                System.out.println("事务回滚成功！");
            }finally {
                conn.setAutoCommit(true);
                System.out.println("设置自动提交");
                e.printStackTrace();
                System.out.println("输出错误信息");
            }
        }
    }
    }


//package sgg.jdbc_demo;
//
//import java.sql.*;
//
//public class JDBC_TEST {
//    private static final String URL = "jdbc:mysql://localhost:3306/test"; // 替换为实际数据库名
//    private static final String USER = "root";
//    private static final String PASSWORD = "mzyudada";
//    static String sql = "insert into sc1(Sno, Cno, Grade) values(?,?,?)";
//
//    public static void main(String[] args) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        // 测试是否连接成功
//        try {
//            // 1. 创建连接
//            conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("数据库连接成功！");
//            //创建statement对象
//            try{
//                stmt = conn.prepareStatement(sql);
//                stmt.setInt(1,202115123);
//                stmt.setInt(2,4);
//                stmt.setInt(3,99);
//                stmt.executeUpdate();
//                conn.setAutoCommit(false);
//                conn.commit();
//            }catch (SQLException e){
//                conn.rollback();
//                e.printStackTrace();
//            }finally {
//                conn.setAutoCommit(true);
//            }
//        } catch (SQLException se) {
//            // 处理 JDBC 错误
//            se.printStackTrace();
//        } catch (Exception e) {
//            // 处理 Class.forName 错误
//            e.printStackTrace();
//        } finally {
//            // 释放资源
//            if (rs != null) {  //关闭查询得到的返回结果
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (stmt != null) {  //关闭创建的查询
//                try {
//                    stmt.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (conn != null) {  //关闭数据库连接
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}