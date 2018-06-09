import javax.xml.crypto.Data;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login {
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private  static final String UsernameQuery="SELECT TimeStart FROM mytable WHERE Username=?";
    private static String in=null;
    public static String getlogin(String username, String t1) {
        conn = DBCon.getConnection();
        try {
            preparedStatement = conn.prepareStatement(UsernameQuery);
            preparedStatement.setString(1,username);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs==null)return "用户不存在";
            rs.next();
            String startTime=rs.getString("TimeStart");
            DateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date startdata = dataFormat.parse(startTime);
                        Date enddata=dataFormat.parse(t1);
                     long  t2 = enddata.getTime()-startdata.getTime();
                     if(t2>2000){
                         Date l=new Date();
                         if(l.getTime()-enddata.getTime()<2000)
                         return "验证成功";
                         else return "登录超时";
                     }
                     else return "登录过于频繁";
                    }catch ( ParseException e){
                        e.printStackTrace();
                    }
                }
         catch (SQLException e) {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
        return in="用户不存在";
    }
    public static void ServerReflash(String name ,String t) {
        try {
            conn = DBCon.getConnection();
            preparedStatement = conn.prepareStatement("UPDATE mytable SET TimeStart=? WHERE mytable.Username=?");
            preparedStatement.setString(1, t);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            for (Throwable k : e)
                System.out.println(k.getMessage());
        }
    }
    public static  String Servergeta(String username,String t1) {
        String a=null;
        try {
            String password=getPassword(username);
             a=Client.Hash(Client.xor(password,t1));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  a;
    }
    public static  String  getPassword(String username){
        String password=null;
        try{
            conn = DBCon.getConnection();
            preparedStatement = conn.prepareStatement("SELECT HashPassword FROM mytable WHERE Username=?");
            preparedStatement.setString(1,username );
            ResultSet re=preparedStatement.executeQuery();
            re.next();
            password=re.getString("HashPassword");
        }catch (SQLException e) {
            for (Throwable k : e)
                System.out.println(k.getMessage());
        }
      return  password;
    }
}
