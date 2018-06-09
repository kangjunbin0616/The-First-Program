import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private BufferedReader reader;
    private PrintWriter writer;
    private ServerSocket server;
    private Socket socket;
    private Socket socketplus;
    public void getServer(){
        try{
            server=new ServerSocket(8189);
                System.out.println("等待连接");
                while(true) {
                    socket = server.accept();
                    System.out.println("连接成功");
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    String flag = reader.readLine();
                    System.out.println(flag);
                    if (flag.equals("Register")) {
                        String username = reader.readLine();
                        System.out.println(username);
                        String password = reader.readLine();
                        System.out.println(password);
                        Register r = new Register();
                        String result = r.register(username, password);
                        writer.println(result);
                        reader.close();
                        writer.close();
                        socket.close();
                        if(result.equals("注册成功")) {
                            ConnetLS();
                            writer.println("Register");
                            writer.println(username);
                            writer.println(password);
                            writer.close();
                            socketplus.close();
                        }
                    }
                 else  if(flag.equals("Refresh")){
                        Refresh refresh=new Refresh();
                        String username = reader.readLine();
                        String oldpassword = reader.readLine();
                        String newpassword=reader.readLine();
                        String result=refresh.getRefresh(username,oldpassword,newpassword);
                        writer.println(result);
                        writer.println(result);
                        reader.close();
                        writer.close();
                        socket.close();
                    }
                     else if(flag.equals("验证成功")){
                        DateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String s="xidianbishe";
                        String name=reader.readLine();
                        String c=reader.readLine();
                        String d=reader.readLine();
                        String logintime=reader.readLine();
                        String t2=reader.readLine();
                        Date t3=new Date();
                        if(t3.getTime()-dataFormat.parse(t2).getTime()>2000) continue;
                        String d2=Client.Hash(Client.xor(Client.xor(name,s),c));
                        if(!d.equals(d2)) continue;
                        String a2=Login.Servergeta(name,logintime);
                        String c2=Client.Hash(Client.xor(a2,t2));
                        String t4=dataFormat.format(new Date());
                        String MAsn=Client.Hash(Client.xor( Client.xor(a2,s),t4));
                        String MAu=Client.Hash(Client.xor(a2,Login.getPassword(name)));
                        reader.close();
                        writer.close();
                        if(!c.equals(c2)){
                         ConnetLS();
                         writer.println("拒绝登录");
                        writer.println(MAu);
                        writer.println(MAsn);
                        writer.println(t4);
                        writer.close();
                        reader.close();
                        socketplus.close();
                        }
                        else if (c.equals(c2)){
                            Login.ServerReflash(name,logintime);
                            ConnetLS();
                            writer.println("登录成功");
                            String g=Client.xor(MAsn,MAu);
                            String hexString="0123456789ABCDEF";
                            byte[] bytes=g.getBytes();
                            StringBuilder sb=new StringBuilder(bytes.length*2);
                            for(int j=0;j<bytes.length;j++)
                            {
                                sb.append(hexString.charAt((bytes[j]&0xf0)>>4));
                                sb.append(hexString.charAt((bytes[j]&0x0f)>>0));
                            }
                            g= sb.toString();
                            writer.println(g);
                            writer.println(t4);
                            writer.println(Client.Hash(MAu));
                            System.out.println(MAu);
                            System.out.println(g);
                            System.out.println(Client.Hash(MAu));
                            System.out.println(t4);
                            writer.println(MAu);
                            writer.close();
                            reader.close();
                            socketplus.close();
                        }
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ConnetLS() {
        try {
            socketplus = new Socket("127.0.0.1", 8998);
            writer = new PrintWriter(new OutputStreamWriter(socketplus.getOutputStream()),true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Server myserver=new Server();
        myserver.getServer();
    }
}
