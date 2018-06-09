import javafx.scene.input.DataFormat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register {
  private  static final String UsernameQuery="SELECT mytable.Username FROM mytable";
    private  static final String UsernameInsert="INSERT INTO mytable(Username, HashPassword,TimeStart,TimeEnd) "+"VALUES(?,?,?,?)";
    private  static Connection conn=null;
    private  static PreparedStatement preparedStatement=null;
    public  String register(String username,String password) {
         String in=null;
          Register.conn=DBCon.getConnection();
          try{
              Register.preparedStatement=Register.conn.prepareStatement(UsernameQuery);
              ResultSet rs=Register.preparedStatement.executeQuery();
              while(rs.next()){
                  if(username.equals(rs.getString(1)))
                  return in="用户名已存在";
              }
              Register.preparedStatement=Register.conn.prepareStatement(UsernameInsert);
              Register.preparedStatement.setString(1,username);
              Register.preparedStatement.setString(2,password);
              SimpleDateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              Date startDate=new Date();
              long startTime=startDate.getTime();
              Date endDate=new Date();
              endDate.setTime(startTime+1000*60);
              Register.preparedStatement.setString(3,dataFormat.format(startDate));
              Register.preparedStatement.setString(4,dataFormat.format(endDate));
              Register.preparedStatement.executeUpdate();
              in="注册成功";
          }catch (SQLException e){
              for(Throwable t:e)
                  System.out.println(t.getMessage());
          }
          return in;
    }

}
