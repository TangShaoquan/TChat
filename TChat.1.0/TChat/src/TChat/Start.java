package TChat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class Start {
    public JFrame Start_windows = new JFrame("TChat");
    public JButton  RegisterButton = new JButton("ע��");
    public JButton  LoginButton = new JButton("��¼");
    public JTextField UserID = new JTextField ();
    public JPasswordField UserPassword  = new JPasswordField();




    //�������ʼ��ҳ��
    public void StartUI(){
        //���ÿ�ʼҳ��UI

        Start_windows.setSize(500, 525);
        Start_windows.setResizable(false);
        Start_windows.setVisible(true);
        Start_windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Start_windows.setLocation(500,150);
        //���ÿ�ʼҳ���ͼ��
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


    //�¼���ؼ���Ӧ
    public void StartEvent(){

        //����ע�ᰴť���ע��ҳ��
        RegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register Register1 = new Register();
                Register1.Register();
                Register1.RegisterEvent();
            }
        });

        //���µ�¼��ť��򿪵�¼ҳ����ߵ�����¼ʧ��
        //�˺������õ�¼�࣬�ɵ�¼�ദ�����е�¼�ɹ�֮��Ĳ�����
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{//���õ�¼����
                    int PORT = 10000;
                    Socket Server = new Socket("localhost",PORT);
                    String Account = UserID.getText();
                    String Password = new String(UserPassword.getPassword());
                    System.out.println(Account+"+"+Password);
                    Client client = new Client(Server,Account,Password,PORT);//�Ѳ�������Client��

                    if(client.isLoginSucessful()){//�����¼�ɹ�
                        JOptionPane.showMessageDialog(null, "��¼�ɹ���", "Login Successful��", JOptionPane.CANCEL_OPTION);
                        Start_windows.setVisible(false);//��¼�ɹ������ÿ�ʼҳ�治�ɼ���
                        System.out.println("��¼�ɹ�");
                        client.Event();

                    }else{
                        System.out.println(("��¼ʧ��"));
                        JOptionPane.showMessageDialog(null, "��¼ʧ�ܣ������µ�¼��", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }catch(IOException ex){
                    System.out.println(("��¼ʧ��"));
                    JOptionPane.showMessageDialog(null, "��¼ʧ�ܣ������µ�¼��", "Login Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }
}
