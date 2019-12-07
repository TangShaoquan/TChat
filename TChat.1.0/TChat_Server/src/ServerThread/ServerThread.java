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

//服务器多线程
public class ServerThread implements Runnable{
    //客户端Socket
    public Socket S;
    //服务器ServerSocket
    public ServerSocket SS;
    //客户端IP
    public String IP;
    //客户端端口
    public int PORT;
    //客户端UID
    public String UID;
    //用户昵称
    String ThisNickname = null;
    //用户账号
    String ThisAccount = null;
    //用户密码
    String Password = null;

    //静态ArrayList储存UID，UID由IP和端口拼接而成
    static ArrayList<String> UID_Arr = new ArrayList<String>();
    //静态ArrayList储存已经登录的UID，UID由IP和端口拼接而成
    public static ArrayList<String> Login_Arr = new ArrayList<>();
    //用来存储UID 和 账号
    public static HashMap<String,String> UID_Account = new HashMap<String, String>();
    //HashMasp储存UID 和 ServerThread组成的对
    public static HashMap<String ,ServerThread> UID_ServerThread = new HashMap<String, ServerThread>();
    //静态HashMap用来存储用户账号和用户密码
    public static HashMap<String ,String> Account_Password = new HashMap<String,String>();
    //静态HashMap用来存储用户账号和昵称对
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
        //将当前UID存入ArrayList
        UID_Arr.add(UID);
        //将当前UID和ServerThread存入HashMap
        UID_ServerThread.put(UID,this);
        //时间显示的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //向控制台打印链接的客户端IP和端口
        System.out.println("Client connected: " + UID);
        try{
            //获取输入流
            InputStream input = S.getInputStream();
            //获取输出流
            OutputStream output = S.getOutputStream();
            //欢迎信息
            String Welcome = sdf.format(new Date())+ "\n" +"Link Server Successful......\nServerIP:"+SS.getInetAddress().getLocalHost().getHostAddress()+",PORT:10000\nClientIP:"+IP+",PORT:"+PORT+"\n";
            output.write(Welcome.getBytes());
            //准备缓冲区
            byte[] buf = new byte[1024];
            int len = 0;
            while(true){

                input = S.getInputStream();
                len = input.read(buf);
                String Msg = new String(buf,0,len);
                //向控制台发送消息验证准确性
                System.out.println("消息整体:"+Msg);
                //消息类型 注册或者登录或者聊天等
                String Type = Msg.substring(0,Msg.indexOf("/"));
                System.out.println("Type:"+Type);
                //消息本体
                //如果消息类型是注册则不用输出Content
                String Content = Msg.substring(Msg.indexOf("/")+1);
                //向控制台输出消息以验证准确性
                System.out.println("这是Content"+Content);

                if(Type.equals("Register")){//处理注册消息
                    Register(Msg,len);
                }
                else if(Type.equals("Login")){
                    //并且登录的时候就获取当前登录用的昵称和账户名
                    Login(output,Msg,len);
                }
                else if(Type.equals("Chat")){
                    toChat(Content,len);
                }
                else if(Type.equals("BroadCast")){//广播
                    Broadcast(Content,len);
                }
                else if(Type.equals("Exit")){
                    //删除登录的记录并且做相关处理
                    UID_Arr.remove(UID);
                    UID_ServerThread.remove(UID);
                    Login_Arr.remove(UID);
                    //调用更新列表向客户端更新
                    UpdateOnlineList(output);
                    System.out.println("退出成功！");//这句话要删除
                }
                else if(Type.equals("UpdateOnlineList")){//更新在线列表
                    UpdateOnlineList(output);//调用更新列表函数
                }
                else if(Type.equals("PersonalInformation")){
                    System.out.println("收到个人信息请求……");
                    PersonalInformatin(output);
                }
            }
        }catch (Exception ex){}

    }



    //注册函数
    public void Register(String msg,int Len){
        String Msg = msg;
        int len = Len;
        //提取昵称账号密码
        String Nickname = Msg.substring(Msg.indexOf("/")+1,Msg.indexOf("#"));
        String Account = Msg.substring(Msg.indexOf("#")+1,Msg.indexOf("$"));
        String Password = Msg.substring(Msg.indexOf("$")+1,Msg.indexOf("%"));
        //储存账号密码昵称
        //要实现检测账号不重复的问题
        System.out.println("注册账密对"+Account+"+"+Password);
        Account_Ncikname.put(Account,Nickname);
        Account_Password.put(Account,Password);
    }
    //登录函数
    public void Login(OutputStream out,String msg,int Len){
        //提取密码和账户
        String Msg = msg;
        int len = Len;
        OutputStream output = out;
        String Account = Msg.substring(Msg.indexOf("/")+1,Msg.indexOf("$"));
        /**********************************************/
        ThisAccount = Account;
        ThisNickname = Account_Ncikname.get(Account);
        /*************************************************/
        String Password = Msg.substring(Msg.indexOf("$")+1,Msg.indexOf("%"));
        System.out.println("登录账户"+Account+"    登录密码"+Password);

        //查询Account_Password是否包含要登录的用户名
        if(Account_Password.containsKey(Account)&&Account_Password.containsValue(Password)){
                try{
                    out = S.getOutputStream();
                    out.write(("LoginSucessful/").getBytes());
                    //把登录成功的储存到Login_Arr到
                    System.out.println("已经发送登录成功Flag");
                    Login_Arr.add(UID);
                    //添加UID和账户，方便后面从Account获取到昵称
                    UID_Account.put(UID,Account);
                    System.out.println("登录信息："+UID+Account);
                    Iterator loginlist = Login_Arr.iterator();
                    while (loginlist.hasNext()){
                        System.out.println("已登录用户："+loginlist.next());
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
    //更新在线列表
    public void UpdateOnlineList(OutputStream out)throws Exception{
        OutputStream output = out;
        StringBuilder Sb = new StringBuilder("UpdateOnlineList/");
        StringBuilder Sn = new StringBuilder("&");
        //在一个循环中将许多字符串连接在一起时，使用 StringBuilder类可以提升性能。
        for(String OnlineMember : Login_Arr){
            System.out.println(OnlineMember+"在线");
            //追加昵称
            Sn.append(ServerThread.Account_Ncikname.get(ServerThread.UID_Account.get(OnlineMember)));
            Sn.append(",");
            //在UpdateOnlineList/后追加Onlinemember的值，用于标识是更新列表消息
            Sb.append(OnlineMember);
            Sb.append(",");
        }
        //把昵称追加到更新列表的流上面
        Sb.append(Sn);
         for(String tem_id : Login_Arr){

            output = UID_ServerThread.get(tem_id).S.getOutputStream();
            output.write(Sb.toString().getBytes());
        }
        System.out.println("输出OnlineList:"+Sb);
    }
    //广播函数
    public void Broadcast(String msg,int Len) throws Exception{
        String Content = msg;
        for(String tmp_id : Login_Arr){
            OutputStream output = UID_ServerThread.get(tmp_id).S.getOutputStream();
            output.write(("BroadCast/"+UID+"/"+ Content).getBytes());
        }
        System.out.println("广播信息："+Content+"\n广播成功！");
    }
    //聊天函数
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
             System.out.println("聊天Chat/"+UID+ Msg);
         }
    }
    //获取个人信息
    public void PersonalInformatin(OutputStream out)throws Exception{
        //PersonalInformation作为Flag，&间隔昵称，@间隔账号，UID为IP+Port ，用：做间隔
        String nickname = ThisNickname;
        String account = ThisAccount;
        OutputStream output = out;
        System.out.println("\n正在返回个人信息……");
        output.write(("PersonalInformation/"+nickname+"&"+account+"@"+UID).getBytes());
        System.out.println("个人信息已发送,发送的信息是：\n");
        System.out.println("PersonalInformation/"+nickname+"&"+account+"@"+UID);

    }
}
