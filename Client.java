import javafx.scene.control.PasswordField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Surface surface=new Surface();

    }
    public static String Hash(String password) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(password.getBytes());
            byte[] bits = md.digest();
            for (int i = 0; i < bits.length; i++) {
                int a = bits[i];
                if (a < 0) a += 256;
                if (a < 16) buf.append("0");
                buf.append(Integer.toHexString(a));
            }
        }catch (Exception e){}
        return buf.toString();
    }
    public static String xor(String s1, String s2) {
        byte b1[] = s1.getBytes();
        byte b2[] = s2.getBytes();
        byte longbytes[],shortbytes[];
        if(b1.length>=b2.length){
            longbytes = b1;
            shortbytes = b2;
        }else{
            longbytes = b2;
            shortbytes = b1;
        }
        byte xorstr[] = new byte[longbytes.length];
        int i = 0;
        for (; i < shortbytes.length; i++) {
            xorstr[i] = (byte)(shortbytes[i]^longbytes[i]);
        }
        for (;i<longbytes.length;i++){
            xorstr[i] = longbytes[i];
        }
        String arg= new String(xorstr);
       return  arg;
    }
}

class LoginListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e) {
        ClientSocket socket = new ClientSocket(8998);
        socket.sendMessage("Login");
        socket.sendMessage(Surface.getUsername());
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowdate = new Date();
        String t1 = dataFormat.format(nowdate);
        socket.sendMessage(t1);
        String a = Client.Hash(Client.xor(Client.Hash(Surface.getpassword()), t1));
        socket.sendMessage(a);
        socket.close();
        String re=null;
        try {
            ServerSocket serversocket = new ServerSocket(8099);
            Socket socketl=serversocket.accept();
            BufferedReader reader=new BufferedReader(new InputStreamReader(socketl.getInputStream()));
            re=reader.readLine();
            String mau=reader.readLine();
            String t=reader.readLine();
            if(Client.Hash(Client.xor(mau,t)).equals(Client.Hash(Client.xor(Client.Hash(Client.xor(Client.Hash(Surface.getpassword()),a)),t))))
            System.out.println(re);
            reader.close();
            socketl.close();
        }catch (Exception f){
            f.printStackTrace();
        }
        Surface.Result.setText(re);
    }
}
class RegisterListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ClientSocket socket=new ClientSocket(8189);
        socket.sendMessage("Register");
        socket.sendMessage(Surface.getUsername());
        socket.sendMessage(Client.Hash(Surface.getpassword()));
        Surface.Result.setText(socket.getMessage());
        socket.close();
    }
}

class RefreshDialog extends JDialog{
    public static JTextField OldPassword;
    public static JTextField NewPassword;
    public static JTextField Message;
    public RefreshDialog(JFrame owner){
        super(owner,"重置密码",true);
        JPanel jPanelFirset=new JPanel();
        JPanel jPanelSecond=new JPanel();
        jPanelFirset.setLayout(new FlowLayout());
        add(jPanelFirset,BorderLayout.CENTER);
        add(jPanelSecond,BorderLayout.SOUTH);
        JLabel one=new JLabel("OldPassword",SwingConstants.RIGHT);
        JLabel two=new JLabel("Newpassword",SwingConstants.RIGHT);
        OldPassword=new JTextField(10);
        NewPassword=new JTextField(10);
        Message=new JTextField(10);
        JPanel Mpanel=new JPanel();
        Mpanel.add(Message);
        jPanelFirset.add(one);
        jPanelFirset.add(OldPassword);
        jPanelFirset.add(two);
        jPanelFirset.add(NewPassword);
        add(Mpanel,BorderLayout.NORTH);
        JButton confirm=new JButton("Confirm");
        JButton cancel=new JButton("Cancle");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientSocket socket=new ClientSocket(8189);
                socket.sendMessage("Refresh");
                socket.sendMessage(Surface.getUsername());
                socket.sendMessage(OldPassword.getText());
                socket.sendMessage(NewPassword.getText());
               Message.setText( socket.getMessage());
               socket.close();
            }
        });
        jPanelSecond.add(confirm);
        jPanelSecond.add(cancel);
        setSize(500,300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public  static String getOldPassword(){
        return OldPassword.getText();
    }
    public static  String getNewPassword(){
        return  NewPassword.getText();
    }

}