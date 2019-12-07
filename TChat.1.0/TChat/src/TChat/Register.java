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
    JButton  RegisterButton = new JButton("ע��");
    //ע���ı���
    JTextField Nickname = new JTextField();
    JTextField UserAccount =new JTextField();
    JPasswordField UserPassword = new JPasswordField();

    //ע���¼�
    public void RegisterEvent(){
        RegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //�½����ӷ���ע����Ϣ
                //���÷�����
                try(Socket Socket_Register = new Socket("localhost",10000)){
                    System.out.println("ע��ϵͳ���ӳɹ�");

                    //��ȡ������
                    InputStream input = Socket_Register.getInputStream();
                    //��ȡ�����
                    OutputStream output = Socket_Register.getOutputStream();

                    //�����̨�����ӭ��Ϣ���������ע���Ƿ�ɹ���
                    byte[] buf = new byte[2048];
                    int len = input.read(buf);
                    String Welcome =new String(buf,0,len);
                    System.out.println(Welcome);


                    //����ע����Ϣ
                    // ��**��ʶ�ǳ�,##��ʶ�û��˻���$$��ʶ�û�����
                    String password = new String(UserPassword.getPassword());
                    output.write(("Register/"+ Nickname.getText() +"#"+ UserAccount.getText() +"$"+password+"%").getBytes());
                    JOptionPane.showMessageDialog(null, "ע��ɹ���", "Register Successful��", JOptionPane.CANCEL_OPTION);
                    Register_windows.setVisible(false);

                }catch(IOException ex){
                    System.out.println(("����ʧ��"));
                    JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ�������ע�ᣡ", "Register Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    //����UI
    public void Register(){

        Register_windows.setIconImage(new ImageIcon("src/Start/TChat.jpg").getImage());

        Register_windows.setSize(300, 200);
        Register_windows.setResizable(false);
        Register_windows.setVisible(true);

        //�����ǳ��ı���UI
        Nickname.setPreferredSize(new Dimension (200,20));
        Label NickLable = new Label("NickName:");

        //�����˻��ı���UI
        UserAccount.setPreferredSize(new Dimension (200,20));
        Label UseraccountLabel = new Label("   Account:");

        //���������ı���UI
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

