package jdbc.dbutils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class DBUtilsTest {
    private static final String URL = "jdbc:mysql://localhost:3306/test"; // 替换为实际数据库名
    private static final String USER = "root";
    private static final String PASSWORD = "mzyudada";

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
         try{
             conn = DriverManager.getConnection(URL,USER,PASSWORD);
             System.out.println("连接成功");
             conn.setAutoCommit(false);
             System.out.println("关闭自动事务提交");

             //创建一个查询或者更新实例
             QueryRunner runner = new QueryRunner();

             //查询单挑记录时候，直接创建对象存储
             String sql_one = "select * from Student where Sno=202015121";
             Student onestudent = runner.query(conn,sql_one, new BeanHandler<>(Student.class));
             System.out.println("单条查询" + onestudent);

             //需要输出多条查询记录时候，创建列表用来存放查询到的student对象
             String sql_all = "select * from Student";
             List<Student> students = runner.query(conn,sql_all,new BeanListHandler<Student>(Student.class));
            //遍历输出列表内容
             for(Student studentMin : students){
                 System.out.println("多条查询结果:"+studentMin);
             }

             //更新表格单挑记录
             String updateSql = "UPDATE student SET Sname = ? WHERE Sno = ?";
             int updateResult = runner.update(conn, updateSql, "Yushuishen2", 202015121);
             System.out.println("更新 " + updateResult + " row(s)");

             //删除表格记录
             String deleteSql = "DELETE FROM student WHERE Sno = ?";
             int deleteResult = runner.update(conn, deleteSql, 12);
             System.out.println("删除 " + deleteResult + " row(s)");

             //插入记录
             String insersql = "INSERT INTO student VALUES(?,?,?,?,?)";
             int INSERTstudent = runner.update(conn,insersql,3, "YSSYmMA","F",20,"CS");
             //输出插入数量
             System.out.println("插入 " + INSERTstudent + " row(s)");

             //提交修改
             DbUtils.commitAndClose(conn);
         }catch (SQLException sqlException) {
             if (conn != null) {
                 conn.setAutoCommit(true);
             }
             sqlException.printStackTrace();
             System.out.println("抛出sql异常");
             DbUtils.rollbackAndCloseQuietly(conn); // 回滚，关闭连接
             System.out.println("回滚和关闭sql连接");
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println("抛出java异常");
             DbUtils.closeQuietly(conn); // 关闭连接
             System.out.println("关闭连接");
         }
    }
}
