package TChat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class Start {
    public JFrame Start_windows = new JFrame("TChat");
    public JButton  RegisterButton = new JButton("注册");
    public JButton  LoginButton = new JButton("登录");
    public JTextField UserID = new JTextField ();
    public JPasswordField UserPassword  = new JPasswordField();




    //定义程序开始的页面
    public void StartUI(){
        //设置开始页面UI

        Start_windows.setSize(500, 525);
        Start_windows.setResizable(false);
        Start_windows.setVisible(true);
        Start_windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Start_windows.setLocation(500,150);
        //设置开始页面的图标
        Start_windows.setIconImage(new ImageIcon("src/TChat/TChat.jpg").getImage());
        ImageIcon Backgroundimg = new ImageIcon("src/TChat/TChat.jpg");
        JLabel Backgroundpicture = new JLabel(Backgroundimg);
        JPanel Start = new JPanel();
        Start.add(Backgroundpicture);
        Start_windows.add(Start,BorderLayout.NORTH);
        UserID.setPreferredSize(new Dimension (250,25));
        UserPassword.setPreferredSize(new Dimension (250,25));
        Label UID = new Label("Account :  ");
        Label UPW = new Label("Password:");
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        jp1.add(UID);
        jp1.add(UserID);
        jp2.add(UPW);
        jp2.add(UserPassword);
        JPanel User = new JPanel();
        User.add(jp1);
        User.add(jp2);
        Panel RL = new Panel();

        RL.add(RegisterButton);
        RL.add(LoginButton);
        Start_windows.add(RL,BorderLayout.SOUTH);
        Start_windows.add(User,BorderLayout.CENTER);
    }


    //事件监控及响应
    public void StartEvent(){

        //按下注册按钮则打开注册页面
        RegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register Register1 = new Register();
                Register1.Register();
                Register1.RegisterEvent();
            }
        });

        //按下登录按钮则打开登录页面或者弹出登录失败
        //此函数调用登录类，由登录类处理所有登录成功之后的操作。
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{//调用登录操作
                    int PORT = 10000;
                    Socket Server = new Socket("localhost",PORT);
                    String Account = UserID.getText();
                    String Password = new String(UserPassword.getPassword());
                    System.out.println(Account+"+"+Password);
                    Client client = new Client(Server,Account,Password,PORT);//把参数传给Client类

                    if(client.isLoginSucessful()){//如果登录成功
                        JOptionPane.showMessageDialog(null, "登录成功！", "Login Successful！", JOptionPane.CANCEL_OPTION);
                        Start_windows.setVisible(false);//登录成功后设置开始页面不可见。
                        System.out.println("登录成功");
                        client.Event();

                    }else{
                        System.out.println(("登录失败"));
                        JOptionPane.showMessageDialog(null, "登录失败！请重新登录！", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }catch(IOException ex){
                    System.out.println(("登录失败"));
                    JOptionPane.showMessageDialog(null, "登录失败！请重新登录！", "Login Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }
}
