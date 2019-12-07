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
                System.out.println("��ϢLen��"+len);
                //��ϢMsg
                String Msg = new String(buf,0,len);
                System.out.println("��ϢMsg��"+Msg);
                //��Ϣ����
                String Type = Msg.substring(0,Msg.indexOf("/"));
                System.out.println("��ϢType:"+Type);
                //��Ϣ���壬���º������������������
                String Content = Msg.substring(Msg.indexOf("/")+1);

                //�������ʹ���
                //����
                if(Type.equals("UpdateOnlineList")){
                    //���ܵ�������Ϣ�Ľ��
                    System.out.println("�ѽ��ܵ������б����Ϣ");
                    //��ȡ�ǳ���
                    String nick = Content.substring(Content.indexOf("&")+1);
                    System.out.println("�ǳ���"+nick);
                    String Onlist = Content.substring(0,Content.indexOf("&"));
                    DefaultTableModel Tbm = (DefaultTableModel) Clientui.Online.getModel();
                    //������б�
                    Tbm.setRowCount(0);
                    String[] OnlineList = Onlist.split(",");
                    String[] OnlineNick = nick.split(",");
                    //��������б�
                    for(String  Member : OnlineList){
                        String[]  tmp = new String[3];
                        tmp[0] = nick.substring(0,nick.indexOf(","));
                        tmp[1] = Member.substring(0,Member.indexOf(":"));//��ʾIP
                        tmp[2] = Member.substring(Member.indexOf(":")+1);//�˿�
                        //����б�
                        Tbm.addRow(tmp);
                        //ɾ����Ӧ���ǳ�
                        nick = nick.substring(nick.indexOf(",")+1);
                    }
                }
                //�㲥
                else if(Type.equals("BroadCast")){
                    //��ӡ��Ϣ
                    String sender = Content.substring(0,Content.indexOf("/"));
                    String word = Content.substring(Content.indexOf("/")+1);
                    //��ӡ������Ϣ
                    Clientui.Chat.append(Clientui.sdf.format(new Date())+"\n����"+sender+":\n"+word+"\n\n");
                    //��ʾ������Ϣ
                    Clientui.Chat.setCaretPosition(Clientui.Chat.getDocument().getLength());
                }
                //����
                else if(Type.equals("Chat")){
                    System.out.println("���ܵ�����Ϣ����"+Content);
                    String senders = Content.substring(0,Content.indexOf("/"));
                    String words = Content.substring(Content.indexOf("/")+1);
                    Clientui.Chat.append(Clientui.sdf.format(new Date())+"\n����"+senders+":\n"+words+"\n\n");
                    Clientui.Chat.setCaretPosition(Clientui.Chat.getDocument().getLength());
                }
                //��ȡ������Ϣ
                else if(Type.equals("PersonalInformation")){
                    String Nkname = Content.substring(0,Content.indexOf("&"));
                    String ACount = Content.substring(Content.indexOf("&")+1,Content.indexOf("@"));
                    String IPnum  = Content.substring(Content.indexOf("@")+1,Content.indexOf(":"));
                    String POrt   = Content.substring(Content.indexOf(":")+1);
                    System.out.println("�ѻ�ȡ��������Ϣ��\n");
                    System.out.println("�ǳ�:"+Nkname+"\n�˻�:"+ACount+"\nIP"+IPnum+"\nPort"+POrt);
                    Clientui.accountfile = Nkname;
                    Clientui.NickNameArea.setText("          "+Nkname);
                    Clientui.AccountArea.setText("          "+ACount);
                    Clientui.IPArea.setText("          "+IPnum);
                    Clientui.PortArea.setText("          "+POrt);
                }
                //���Ǳ�����������
                else if(Type.equals("Logout")){
                    JOptionPane.showMessageDialog(null,"���ѱ�������ǿ�����ߣ������µ�¼��","Logout",JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        }catch(IOException ex){
            System.out.println("����");
            JOptionPane.showMessageDialog(null,"�����������ߣ�","WRONG",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
