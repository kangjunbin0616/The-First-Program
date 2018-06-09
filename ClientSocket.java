import java.io.*;
import java.net.Socket;

public class ClientSocket {
    private PrintWriter bufferedWriter;
    private BufferedReader bufferedReader;
    Socket socket;
    public  ClientSocket(int port){
        try{
            socket=new Socket("127.0.0.1",port);
            System.out.println("用户连接登录节点成功");
            bufferedWriter=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void sendMessage(String s){
        try{
            bufferedWriter.println(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getMessage(){
        String s=null;
        try{
            s=bufferedReader.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  s;
    }
    public void close()  {
        try {
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
