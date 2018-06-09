import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

 public class Surface extends JFrame {
    private int screenHeight;
    private int screenWidth;
    private  JPanel panelone;
    private  JPanel paneltwo;
    public static JTextField Result=new JTextField(10);
    public  static  JTextArea Date=new JTextArea();
    public static JTextField usernameField=new JTextField(10);
    public static JPasswordField passwordField=new JPasswordField(10);
    private JButton LoginButton=new JButton("Login");
    private JButton RegisterButton=new JButton("Register");
    private JButton RefreshButton=new JButton("Refresh");
    public Surface(){
        Toolkit kit=Toolkit.getDefaultToolkit();
        Dimension screenSize=kit.getScreenSize();
        screenHeight =screenSize.height;
        screenWidth=screenSize.width;
        setSize(screenWidth/2,screenHeight/2);
        setLocationByPlatform(true);
        setTitle("界面");
        Container container=this.getContentPane();
        container.setLayout(new BorderLayout());
        panelone=new JPanel();
        paneltwo=new JPanel();
        container.add(panelone,BorderLayout.NORTH);
        container.add(paneltwo,BorderLayout.SOUTH);
        container.add(Date,BorderLayout.CENTER);
        JLabel Username=new JLabel("Username",SwingConstants.RIGHT);
        JLabel Password=new JLabel("Password",SwingConstants.RIGHT);
        panelone.add(Username);
        panelone.add(usernameField);
        panelone.add(Password);
        panelone.add(passwordField);
        panelone.add(Result);
        paneltwo.add(LoginButton);
        paneltwo.add(RegisterButton);
        paneltwo.add(RefreshButton);
        LoginButton.addActionListener(new LoginListener());
        RegisterButton.addActionListener(new RegisterListener());

        RefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RefreshDialog(Surface.this);
            }
        });
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static String getUsername(){
        return usernameField.getText();
    }
    public static String getpassword(){
        return new String(passwordField.getPassword());
    }
}
