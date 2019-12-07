package TChat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import javax.swing.SwingUtilities;

public class Client{

    public Socket Server = new Socket();
    public String Account = new String();
    public String Password = new String();
    //接收者
    static StringBuilder UidRecerive = null;

    int Port = 0;
    public static boolean LoginFlag;


    public Client(Socket server, String account, String password, int port) {
        this.Server = server;
        this.Account = account;
        this.Password = password;
        this.Port = port;
    }

    public void Event(){

        if(LoginFlag){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                        try{
                            //Server.setSoTimeout(3000);
                            System.out.println(LoginFlag);
                            OutputStream output = Server.getOutputStream();
                            InputStream input = Server.getInputStream();
                            ClientUI Clientui = new ClientUI(output,input);
                            Clientui.setVisible(true);
                            new Thread(new ClientThread(input,output,Clientui)).start();
                        }catch(IOException ex){
                        }

            };
            });
        }
    }

    //检测登录
    public boolean isLoginSucessful() {
        try {
            InputStream input = Server.getInputStream();
            OutputStream output = Server.getOutputStream();

            byte[] buf = new byte[2048];
            int len = input.read(buf);
            String Welcome = new String(buf, 0, len);
            System.out.println(Welcome);

            System.out.println("正在发送账户密码……");
            System.out.println("账户:" + Account + "密码:" + Password);
            output.write(("Login/" + Account + "$" + Password + "%").getBytes());
            System.out.println("账户密码发送成功……\n正在验证登录信息……\n");

            len = input.read(buf);
            String Msg = new String(buf, 0, len);
            String Type = Msg.substring(0, Msg.indexOf("/"));
            System.out.println(Msg + Type);

            if (Type.equals("LoginSucessful"))
                LoginFlag = true;
            else if (Type.equals("LoginFailed"))
                LoginFlag = false;
            else {
                System.out.println("登录遇上未知错误");
                LoginFlag = false;
            }
        } catch (IOException ex) {
        } finally {
            System.out.println("登录是否成功："+LoginFlag);
            return LoginFlag;
        }


    }


}