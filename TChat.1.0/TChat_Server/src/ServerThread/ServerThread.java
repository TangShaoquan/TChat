package ServerThread;

import ServerThread.ServerThread;
import ServerUI.ServerUI;

import javax.swing.text.AbstractDocument;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.swing.UIManager.put;

//���������߳�
public class ServerThread implements Runnable{
    //�ͻ���Socket
    public Socket S;
    //������ServerSocket
    public ServerSocket SS;
    //�ͻ���IP
    public String IP;
    //�ͻ��˶˿�
    public int PORT;
    //�ͻ���UID
    public String UID;
    //�û��ǳ�
    String ThisNickname = null;
    //�û��˺�
    String ThisAccount = null;
    //�û�����
    String Password = null;

    //��̬ArrayList����UID��UID��IP�Ͷ˿�ƴ�Ӷ���
    static ArrayList<String> UID_Arr = new ArrayList<String>();
    //��̬ArrayList�����Ѿ���¼��UID��UID��IP�Ͷ˿�ƴ�Ӷ���
    public static ArrayList<String> Login_Arr = new ArrayList<>();
    //�����洢UID �� �˺�
    public static HashMap<String,String> UID_Account = new HashMap<String, String>();
    //HashMasp����UID �� ServerThread��ɵĶ�
    public static HashMap<String ,ServerThread> UID_ServerThread = new HashMap<String, ServerThread>();
    //��̬HashMap�����洢�û��˺ź��û�����
    public static HashMap<String ,String> Account_Password = new HashMap<String,String>();
    //��̬HashMap�����洢�û��˺ź��ǳƶ�
    public static HashMap<String,String> Account_Ncikname = new HashMap<String, String>();

    public  ServerThread(Socket s, ServerSocket ss, String ip, int port)
    {
        this.S = s;
        this.SS = ss;
        this.IP = ip;
        this.PORT = port;
        this.UID  = ip  + ":" + port;
    }

    @Override
    public void run() {
        //����ǰUID����ArrayList
        UID_Arr.add(UID);
        //����ǰUID��ServerThread����HashMap
        UID_ServerThread.put(UID,this);
        //ʱ����ʾ�ĸ�ʽ
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //�����̨��ӡ���ӵĿͻ���IP�Ͷ˿�
        System.out.println("Client connected: " + UID);
        try{
            //��ȡ������
            InputStream input = S.getInputStream();
            //��ȡ�����
            OutputStream output = S.getOutputStream();
            //��ӭ��Ϣ
            String Welcome = sdf.format(new Date())+ "\n" +"Link Server Successful......\nServerIP:"+SS.getInetAddress().getLocalHost().getHostAddress()+",PORT:10000\nClientIP:"+IP+",PORT:"+PORT+"\n";
            output.write(Welcome.getBytes());
            //׼��������
            byte[] buf = new byte[1024];
            int len = 0;
            while(true){

                input = S.getInputStream();
                len = input.read(buf);
                String Msg = new String(buf,0,len);
                //�����̨������Ϣ��֤׼ȷ��
                System.out.println("��Ϣ����:"+Msg);
                //��Ϣ���� ע����ߵ�¼���������
                String Type = Msg.substring(0,Msg.indexOf("/"));
                System.out.println("Type:"+Type);
                //��Ϣ����
                //�����Ϣ������ע���������Content
                String Content = Msg.substring(Msg.indexOf("/")+1);
                //�����̨�����Ϣ����֤׼ȷ��
                System.out.println("����Content"+Content);

                if(Type.equals("Register")){//����ע����Ϣ
                    Register(Msg,len);
                }
                else if(Type.equals("Login")){
                    //���ҵ�¼��ʱ��ͻ�ȡ��ǰ��¼�õ��ǳƺ��˻���
                    Login(output,Msg,len);
                }
                else if(Type.equals("Chat")){
                    toChat(Content,len);
                }
                else if(Type.equals("BroadCast")){//�㲥
                    Broadcast(Content,len);
                }
                else if(Type.equals("Exit")){
                    //ɾ����¼�ļ�¼��������ش���
                    UID_Arr.remove(UID);
                    UID_ServerThread.remove(UID);
                    Login_Arr.remove(UID);
                    //���ø����б���ͻ��˸���
                    UpdateOnlineList(output);
                    System.out.println("�˳��ɹ���");//��仰Ҫɾ��
                }
                else if(Type.equals("UpdateOnlineList")){//���������б�
                    UpdateOnlineList(output);//���ø����б���
                }
                else if(Type.equals("PersonalInformation")){
                    System.out.println("�յ�������Ϣ���󡭡�");
                    PersonalInformatin(output);
                }
            }
        }catch (Exception ex){}

    }



    //ע�ắ��
    public void Register(String msg,int Len){
        String Msg = msg;
        int len = Len;
        //��ȡ�ǳ��˺�����
        String Nickname = Msg.substring(Msg.indexOf("/")+1,Msg.indexOf("#"));
        String Account = Msg.substring(Msg.indexOf("#")+1,Msg.indexOf("$"));
        String Password = Msg.substring(Msg.indexOf("$")+1,Msg.indexOf("%"));
        //�����˺������ǳ�
        //Ҫʵ�ּ���˺Ų��ظ�������
        System.out.println("ע�����ܶ�"+Account+"+"+Password);
        Account_Ncikname.put(Account,Nickname);
        Account_Password.put(Account,Password);
    }
    //��¼����
    public void Login(OutputStream out,String msg,int Len){
        //��ȡ������˻�
        String Msg = msg;
        int len = Len;
        OutputStream output = out;
        String Account = Msg.substring(Msg.indexOf("/")+1,Msg.indexOf("$"));
        /**********************************************/
        ThisAccount = Account;
        ThisNickname = Account_Ncikname.get(Account);
        /*************************************************/
        String Password = Msg.substring(Msg.indexOf("$")+1,Msg.indexOf("%"));
        System.out.println("��¼�˻�"+Account+"    ��¼����"+Password);

        //��ѯAccount_Password�Ƿ����Ҫ��¼���û���
        if(Account_Password.containsKey(Account)&&Account_Password.containsValue(Password)){
                try{
                    out = S.getOutputStream();
                    out.write(("LoginSucessful/").getBytes());
                    //�ѵ�¼�ɹ��Ĵ��浽Login_Arr��
                    System.out.println("�Ѿ����͵�¼�ɹ�Flag");
                    Login_Arr.add(UID);
                    //���UID���˻�����������Account��ȡ���ǳ�
                    UID_Account.put(UID,Account);
                    System.out.println("��¼��Ϣ��"+UID+Account);
                    Iterator loginlist = Login_Arr.iterator();
                    while (loginlist.hasNext()){
                        System.out.println("�ѵ�¼�û���"+loginlist.next());
                    }
                }catch(IOException ex){
                }
            }
        else{
            try{
                out.write(("LoginFailed/").getBytes());
            }catch(IOException ex){}
        }
    }
    //���������б�
    public void UpdateOnlineList(OutputStream out)throws Exception{
        OutputStream output = out;
        StringBuilder Sb = new StringBuilder("UpdateOnlineList/");
        StringBuilder Sn = new StringBuilder("&");
        //��һ��ѭ���н�����ַ���������һ��ʱ��ʹ�� StringBuilder������������ܡ�
        for(String OnlineMember : Login_Arr){
            System.out.println(OnlineMember+"����");
            //׷���ǳ�
            Sn.append(ServerThread.Account_Ncikname.get(ServerThread.UID_Account.get(OnlineMember)));
            Sn.append(",");
            //��UpdateOnlineList/��׷��Onlinemember��ֵ�����ڱ�ʶ�Ǹ����б���Ϣ
            Sb.append(OnlineMember);
            Sb.append(",");
        }
        //���ǳ�׷�ӵ������б��������
        Sb.append(Sn);
         for(String tem_id : Login_Arr){

            output = UID_ServerThread.get(tem_id).S.getOutputStream();
            output.write(Sb.toString().getBytes());
        }
        System.out.println("���OnlineList:"+Sb);
    }
    //�㲥����
    public void Broadcast(String msg,int Len) throws Exception{
        String Content = msg;
        for(String tmp_id : Login_Arr){
            OutputStream output = UID_ServerThread.get(tmp_id).S.getOutputStream();
            output.write(("BroadCast/"+UID+"/"+ Content).getBytes());
        }
        System.out.println("�㲥��Ϣ��"+Content+"\n�㲥�ɹ���");
    }
    //���캯��
    public void toChat(String con,int Len) throws Exception{
        String Content = con;
        int len = Len;
         String[] Receiver_Arr = Content.substring(1,Content.indexOf("/")).split(",");
         String Msg = Content.substring(Content.indexOf("/")+1);
         Msg.trim();
         for(String tmp_id : Receiver_Arr){
             System.out.println(tmp_id);
             OutputStream output = UID_ServerThread.get(tmp_id).S.getOutputStream();
             System.out.println();
             output.write(("Chat/"+UID+"/"+Msg).getBytes());
             System.out.println("����Chat/"+UID+ Msg);
         }
    }
    //��ȡ������Ϣ
    public void PersonalInformatin(OutputStream out)throws Exception{
        //PersonalInformation��ΪFlag��&����ǳƣ�@����˺ţ�UIDΪIP+Port ���ã������
        String nickname = ThisNickname;
        String account = ThisAccount;
        OutputStream output = out;
        System.out.println("\n���ڷ��ظ�����Ϣ����");
        output.write(("PersonalInformation/"+nickname+"&"+account+"@"+UID).getBytes());
        System.out.println("������Ϣ�ѷ���,���͵���Ϣ�ǣ�\n");
        System.out.println("PersonalInformation/"+nickname+"&"+account+"@"+UID);

    }
}
