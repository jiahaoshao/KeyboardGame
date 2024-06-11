import java.sql.*;

public class Test {

    // MySQL8.0以下版本 - JDBC驱动名及数据库URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // test为数据库名,?后面这句很重要,设置useSSl=false
    static final String DB_URL = "jdbc:mysql://localhost:3306/keyboardgame?characterEncoding=UTF8&autoReconnect=true&useSSL=false&&serverTimezone=GMT%2B8";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "fangyi";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动,加载驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接,连接数据库
            System.out.println("数据库连接中...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //System.out.println(conn);
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            //System.out.println(stmt);
            String sql;
            sql = "SELECT id, username, password FROM user";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                int id  = rs.getInt("id");
                String name = rs.getString("username");
                String url = rs.getString("password");

                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 用户名: " + name);
                System.out.print(", 密码: " + url);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("操作结束!");
    }
}