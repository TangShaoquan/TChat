package TChat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Register extends JFrame{

    JFrame Register_windows = new JFrame("Register");
    JButton  RegisterButton = new JButton("注册");
    //注册文本框
    JTextField Nickname = new JTextField();
    JTextField UserAccount =new JTextField();
    JPasswordField UserPassword = new JPasswordField();

    //注册事件
    public void RegisterEvent(){
        RegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //新建链接发送注册信息
                //设置服务器
                try(Socket Socket_Register = new Socket("localhost",10000)){
                    System.out.println("注册系统链接成功");

                    //获取输入流
                    InputStream input = Socket_Register.getInputStream();
                    //获取输出流
                    OutputStream output = Socket_Register.getOutputStream();

                    //向控制台输出欢迎信息，检测链接注册是否成功。
                    byte[] buf = new byte[2048];
                    int len = input.read(buf);
                    String Welcome =new String(buf,0,len);
                    System.out.println(Welcome);


                    //发送注册信息
                    // 用**标识昵称,##标识用户账户，$$标识用户密码
                    String password = new String(UserPassword.getPassword());
                    output.write(("Register/"+ Nickname.getText() +"#"+ UserAccount.getText() +"$"+password+"%").getBytes());
                    JOptionPane.showMessageDialog(null, "注册成功！", "Register Successful！", JOptionPane.CANCEL_OPTION);
                    Register_windows.setVisible(false);

                }catch(IOException ex){
                    System.out.println(("链接失败"));
                    JOptionPane.showMessageDialog(null, "注册失败！请重新注册！", "Register Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    //设置UI
    public void Register(){

        Register_windows.setIconImage(new ImageIcon("src/Start/TChat.jpg").getImage());

        Register_windows.setSize(300, 200);
        Register_windows.setResizable(false);
        Register_windows.setVisible(true);

        //设置昵称文本框UI
        Nickname.setPreferredSize(new Dimension (200,20));
        Label NickLable = new Label("NickName:");

        //设置账户文本框UI
        UserAccount.setPreferredSize(new Dimension (200,20));
        Label UseraccountLabel = new Label("   Account:");

        //设置密码文本框UI
        UserPassword.setPreferredSize(new Dimension(200,20));
        Label UserPasswordLabel = new Label("Password:");

        JPanel register = new JPanel();
        register.add(NickLable);
        register.add(Nickname);
        register.add(UseraccountLabel);
        register.add(UserAccount);
        register.add(UserPasswordLabel);
        register.add(UserPassword);

        JPanel registerButton = new JPanel();
        registerButton.add(RegisterButton);

        Register_windows.add(register,BorderLayout.CENTER);
        Register_windows.add(registerButton,BorderLayout.SOUTH);
    }
}

