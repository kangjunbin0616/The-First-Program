import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBCon {
    private static final String url="jdbc:mysql://localhost:3306/mydatabase";
    private static final String user="root";
    private static final String password="kjb123456";
    public static Connection conn=null;
    public static PreparedStatement preparedStatement=null;
    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection(url,user,password);
        }catch (Exception e){
        }
        return conn;
    }
}
