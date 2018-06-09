
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginServer {
    private BufferedReader reader;
    private PrintWriter writer;
    private ServerSocket serverSocket;
    private Socket socket;
    private  static Connection conn=null;
    private  static PreparedStatement preparedStatement=null;
    private  static String  a=null;
     private  static  String MAu=null;
    private  static  String s="xidianbishe";
    public void startServer() {
        try {
         serverSocket=new ServerSocket(8998);
         while(true){
             socket=serverSocket.accept();
             System.out.println("登录节点被连接成功");
             reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             String flag=reader.readLine();
             if(flag.equals("Register")){
              LoginServer.conn=DBCon.getConnection();
              LoginServer.preparedStatement=LoginServer.conn.prepareStatement("INSERT INTO mytabletwo(Username, TimeStart,TimeEnd) "+"VALUES(?,?,?)");
                 LoginServer.preparedStatement.setString(1,reader.readLine());
                 SimpleDateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 Date startDate=new Date();
                 long startTime=startDate.getTime();
                 Date endDate=new Date();
                 endDate.setTime(startTime+1000*60);
                 LoginServer.preparedStatement.setString(2,dataFormat.format(startDate));
                 LoginServer.preparedStatement.setString(3,dataFormat.format(endDate));
                 LoginServer.preparedStatement.executeUpdate();
             }
             if (flag.equals("Login")){
                String username=reader.readLine();
                String logintime=reader.readLine();
                 a=reader.readLine();
                String re= Login.getlogin(username,logintime);
                System.out.println(re);
                 Surface.Result.setText(re);
                if(!re.equals("验证成功")) {
                    writer.println(re);
                }
                 reader.close();
                 writer.close();
                 socket.close();
                 if(re.equals("验证成功")){
                    LoginServer.conn=DBCon.getConnection();
                    LoginServer.preparedStatement=LoginServer.conn.prepareStatement("UPDATE mytabletwo SET TimeStart=? WHERE Username=?");
                    LoginServer.preparedStatement.setString(1,logintime);
                    LoginServer.preparedStatement.setString(2,username);
                    LoginServer.preparedStatement.executeUpdate();
                    DateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date t2=new Date();
                    String c=Client.Hash(Client.xor(a,dataFormat.format(t2)));
                    String d=Client.Hash(Client.xor(Client.xor(username,s),c));
                    Socket lsocket=new Socket("127.0.0.1",8189);
                    reader = new BufferedReader(new InputStreamReader(lsocket.getInputStream()));
                    writer = new PrintWriter(new OutputStreamWriter(lsocket.getOutputStream()), true);
                    writer.println("验证成功");
                    writer.println(username);
                    writer.println(c);
                    writer.println(d);
                    writer.println(logintime);
                    writer.println(dataFormat.format(t2));
                    reader.close();
                    writer.close();
                    lsocket.close();
                }
             }
             if (flag.equals("拒绝登录")){
                  MAu=reader.readLine();
                 String MAsn=reader.readLine();
                 String t4=reader.readLine();
                 Date t5=new Date();
                 DateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 if(t5.getTime()-dataFormat.parse(t4).getTime()>2000) continue;
                 String MAsn2=Client.Hash(Client.xor(Client.xor(a,s),t4));
                 if(!Client.Hash(MAsn).equals(Client.Hash(MAsn2))) continue;
                 Socket socketd=new Socket("127.0.0.1",8099);
                 PrintWriter writerd=new PrintWriter(new OutputStreamWriter(socketd.getOutputStream()));
                 writerd.println("拒绝登录");
                 writerd.println(Client.Hash(Client.xor(MAu,dataFormat.format(t5))));
                 writerd.println(t5);
                 writerd.close();
                 socketd.close();
             }
             if(flag.equals("登录成功")){
                 String sb=reader.readLine();
                 System.out.println(sb);
                 byte[] baKeyword = new byte[sb.length()/2];
                 for(int i = 0; i < baKeyword.length; i++)
                 {
                     try
                     {
                         baKeyword[i] = (byte)(0xff & Integer.parseInt(sb.substring(i*2, i*2+2),16));
                     }
                     catch(Exception e)
                     {
                         e.printStackTrace();
                     }
                 }
                 try
                 {
                     sb = new String(baKeyword, "utf-8");//UTF-16le:Not
                 }
                 catch (Exception e1)
                 {
                     e1.printStackTrace();
                 }
                 String t4=reader.readLine();
                 String HM=reader.readLine();
                 String mau=reader.readLine();
                 System.out.println(sb);
                 System.out.println(HM);
                 System.out.println(t4);
                 Date t6=new Date();
                 DateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 if(t6.getTime()-dataFormat.parse(t4).getTime()>2000) continue;
                 String MAsn2=Client.Hash(Client.xor(Client.xor(a,s),t4));
                 String MAu2=Client.xor(MAsn2,sb);
                 System.out.println(MAu2);
                 if(!HM.equals(Client.Hash(MAu2))) continue;
                 System.out.println("登录成功");
                 socket.close();
                 writer.close();
                 reader.close();
                 Socket socketd=new Socket("127.0.0.1",8099);
                 PrintWriter writerd=new PrintWriter(new OutputStreamWriter(socketd.getOutputStream()));
                 writerd.println("登录成功");
                 writerd.println(Client.Hash(Client.xor(mau,dataFormat.format(t6))));
                 writerd.println(dataFormat.format(t6));
                 writerd.close();
                 socketd.close();
             }
         }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginServer loginServer=new LoginServer();
        loginServer.startServer();
    }
}
