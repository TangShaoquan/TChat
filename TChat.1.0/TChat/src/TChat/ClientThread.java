package TChat;
import javax.imageio.IIOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class ClientThread implements Runnable {
    InputStream input = null;
    OutputStream output = null;
    ClientUI Clientui;
    byte[] buf = new byte[1024];
    int len = 0;

    public ClientThread(InputStream in,OutputStream out,ClientUI ui){
        this.input = in;
        this.output = out;
        this.Clientui = ui;
    }

    @Override
    public void run() {
        try{
            while(true){
                System.out.println("Link Flag");
                len = input.read(buf);
                System.out.println("消息Len："+len);
                //消息Msg
                String Msg = new String(buf,0,len);
                System.out.println("消息Msg："+Msg);
                //消息类型
                String Type = Msg.substring(0,Msg.indexOf("/"));
                System.out.println("消息Type:"+Type);
                //消息本体，更新后的名单或者聊天内容
                String Content = Msg.substring(Msg.indexOf("/")+1);

                //根据类型处理
                //更新
                if(Type.equals("UpdateOnlineList")){
                    //接受到更新消息的结果
                    System.out.println("已接受到更新列表的消息");
                    //获取昵称流
                    String nick = Content.substring(Content.indexOf("&")+1);
                    System.out.println("昵称流"+nick);
                    String Onlist = Content.substring(0,Content.indexOf("&"));
                    DefaultTableModel Tbm = (DefaultTableModel) Clientui.Online.getModel();
                    //先清空列表
                    Tbm.setRowCount(0);
                    String[] OnlineList = Onlist.split(",");
                    String[] OnlineNick = nick.split(",");
                    //添加在线列表
                    for(String  Member : OnlineList){
                        String[]  tmp = new String[3];
                        tmp[0] = nick.substring(0,nick.indexOf(","));
                        tmp[1] = Member.substring(0,Member.indexOf(":"));//显示IP
                        tmp[2] = Member.substring(Member.indexOf(":")+1);//端口
                        //添加列表
                        Tbm.addRow(tmp);
                        //删除对应的昵称
                        nick = nick.substring(nick.indexOf(",")+1);
                    }
                }
                //广播
                else if(Type.equals("BroadCast")){
                    //打印信息
                    String sender = Content.substring(0,Content.indexOf("/"));
                    String word = Content.substring(Content.indexOf("/")+1);
                    //打印聊天信息
                    Clientui.Chat.append(Clientui.sdf.format(new Date())+"\n来自"+sender+":\n"+word+"\n\n");
                    //显示最新消息
                    Clientui.Chat.setCaretPosition(Clientui.Chat.getDocument().getLength());
                }
                //聊天
                else if(Type.equals("Chat")){
                    System.out.println("接受到的消息内容"+Content);
                    String senders = Content.substring(0,Content.indexOf("/"));
                    String words = Content.substring(Content.indexOf("/")+1);
                    Clientui.Chat.append(Clientui.sdf.format(new Date())+"\n来自"+senders+":\n"+words+"\n\n");
                    Clientui.Chat.setCaretPosition(Clientui.Chat.getDocument().getLength());
                }
                //获取个人信息
                else if(Type.equals("PersonalInformation")){
                    String Nkname = Content.substring(0,Content.indexOf("&"));
                    String ACount = Content.substring(Content.indexOf("&")+1,Content.indexOf("@"));
                    String IPnum  = Content.substring(Content.indexOf("@")+1,Content.indexOf(":"));
                    String POrt   = Content.substring(Content.indexOf(":")+1);
                    System.out.println("已获取到个人信息：\n");
                    System.out.println("昵称:"+Nkname+"\n账户:"+ACount+"\nIP"+IPnum+"\nPort"+POrt);
                    Clientui.accountfile = Nkname;
                    Clientui.NickNameArea.setText("          "+Nkname);
                    Clientui.AccountArea.setText("          "+ACount);
                    Clientui.IPArea.setText("          "+IPnum);
                    Clientui.PortArea.setText("          "+POrt);
                }
                //若是被服务器下线
                else if(Type.equals("Logout")){
                    JOptionPane.showMessageDialog(null,"您已被服务器强制下线！请重新登录！","Logout",JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        }catch(IOException ex){
            System.out.println("错误！");
            JOptionPane.showMessageDialog(null,"服务器已下线！","WRONG",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
