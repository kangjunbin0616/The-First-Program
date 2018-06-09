import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Refresh {
    private static String Update = "UPDATE mytable SET HashPassword=? WHERE mytable.Username=?";
    private static String Password = "SELECT HashPassword FROM mytable WHERE Username=?";
    private  static Connection conn=null;
    private  static PreparedStatement preparedStatement=null;
    public String getRefresh(String username,String OldP, String NewP) {
        try {
            Refresh.conn=DBCon.getConnection();
             Refresh.preparedStatement = DBCon.conn.prepareStatement(Password);
            Refresh.preparedStatement.setString(1, username);
            ResultSet se = Refresh.preparedStatement.executeQuery();
            se.next();
            String s=se.getString("HashPassword");
            System.out.println(s);
            if (Client.Hash(OldP).equals(s)) {
                Refresh.preparedStatement = Refresh.conn.prepareStatement(Update);
                Refresh.preparedStatement.setString(1, Client.Hash(NewP));
                Refresh.preparedStatement.setString(2, username);
                Refresh.preparedStatement.executeUpdate();
                return "更改密码成功";
            } else if (!Client.Hash(OldP).equals(s)) return "密码错误";
            else return "用户不存在";
        } catch (SQLException e) {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
        return "用户不存在";
    }
}
