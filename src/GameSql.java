import java.sql.*;
import java.util.Objects;

public class GameSql {

    // MySQL8.0以下版本 - JDBC驱动名及数据库URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // test为数据库名,?后面这句很重要,设置useSSl=false
    static final String DB_URL = "jdbc:mysql://localhost:3306/keyboardgame?characterEncoding=UTF8&autoReconnect=true&useSSL=false&&serverTimezone=GMT%2B8";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "fangyi";

    public static Connection conn = null;
    public static Statement stmt = null;
    public static ResultSet rs = null;

    public static void init(){
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
                sql = "DROP TABLE IF EXISTS user";
                stmt.executeUpdate(sql);
                sql = "CREATE TABLE IF NOT EXISTS `user` (" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                        "  `username` char(20) NOT NULL DEFAULT ''," +
                        "  `password` char(20) NOT NULL DEFAULT ''," +
                        "  PRIMARY KEY (`id`)" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
                stmt.executeUpdate(sql);

                sql = "INSERT INTO user" +
                        "(id,username,password)" +
                        "VALUES(default, 'root','root');";
                int i = stmt.executeUpdate(sql);
                sql = "INSERT INTO user" +
                        "(id,username,password)" +
                        "VALUES(default, '123','123');";
                i = stmt.executeUpdate(sql);


                sql = "DROP TABLE IF EXISTS userinfo";
                stmt.executeUpdate(sql);
                sql = "CREATE TABLE IF NOT EXISTS userinfo (" +
                        "    id INT," +
                        "    practice_time TIME," +
                        "    practice_results VARCHAR(255)," +
                        "    game_time TIME," +
                        "    results VARCHAR(255)," +
                        "    opponent_results VARCHAR(255)" +
                        ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
                stmt.executeUpdate(sql);

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



    public static boolean isexist(String username, String password) {
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
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                // 通过字段检索
                String name = rs.getString("username");
                String psd = rs.getString("password");

                if(Objects.equals(username, name) && Objects.equals(password, psd))
                {
                    rs.close();
                    stmt.close();
                    conn.close();
                    return true;
                }
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
            return false;
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
        return false;
    }

    public static void add(String username, String password)
    {
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
            String sql;
            sql = "INSERT INTO user (id,username,password) VALUES(default,'" + username + "','"+ password + "');";
            System.out.println(sql);
            int i = stmt.executeUpdate(sql);
            System.out.println("受影响行数:" + i);
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

    public static boolean isexistusername(String username) {
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
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                // 通过字段检索
                String name = rs.getString("username");
                String psd = rs.getString("password");

                if(Objects.equals(username, name))
                {
                    rs.close();
                    stmt.close();
                    conn.close();
                    return true;
                }
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
            return false;
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
        return false;
    }
}