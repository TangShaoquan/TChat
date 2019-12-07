package TChat;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static TChat.Client.UidRecerive;

public class ClientUI extends JFrame{



    //设置时间显示格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    JTabbedPane LoginTable = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);//标签栏放顶部，不足时折叠摆放标签

    //定义按钮
    JButton Sent = new JButton("发送");
    JButton ExitButton = new JButton("退出TChat");
    JButton GroupChat = new JButton("清屏");
    JButton BroadCast = new JButton("广播");
    JButton  ConstentButton = new JButton("我的好友");
    JButton GroupButton = new JButton("我的聊天");

    JPanel ChatPanel = new JPanel();
    JPanel ConstentPanel = new JPanel();
    JPanel PersonalinformationPanel = new JPanel();

    StringBuilder uidReceiver = null;
    String accountfile = new String();

    //文本输入框
    JTextArea Say = new JTextArea();
    //消息框
    JTextArea Chat = new JTextArea();
    //昵称框
    JTextArea NickNameArea = new JTextArea();
    //账号框
    JTextArea AccountArea = new JTextArea();
    //IP框
    JTextArea IPArea = new JTextArea();
    //端口框
    JTextArea PortArea = new JTextArea();
    //在线列表的标题
    String[] colTitles = {"昵称","IP","端口"};
    //在线列表的数据
    String[][] rowData = null;
    //在线列表
    JTable Online = new JTable(
            new DefaultTableModel(rowData,colTitles){
                //表格不可编辑，只可显示
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            }
    );
    //在线群列表的标题
    String[] GColTitles = {"昵称","IP","端口"};
    //在线群聊的数据
    String[][] GRowData = null;
    //在线群聊列表
    JTable GroupList = new JTable(
            new DefaultTableModel(GRowData,GColTitles){
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            }
    );
    //聊天消息滚动窗
    JScrollPane jspChat = new JScrollPane(Chat);
    //在线列表滚动窗
    JScrollPane jspOnline = new JScrollPane(Online);
    //在线群聊滚动窗
    JScrollPane jspGroup = new JScrollPane(GroupList);

    Label NA = new Label("     NickName     ");
    Label AA = new Label("      Account     ");
    Label IP = new Label("        IP        ");
    Label PR = new Label("       PORT       ");

    JLabel Receivelabel = new JLabel("点击清屏可清除当前窗口聊天记录并导出至文件");

///////////////////////////////////构造函数////////////////////////////////////////
    public ClientUI(OutputStream output,InputStream input){

        setIconImage(new ImageIcon("src/Start/TChat.jpg").getImage());
        setTitle("***TChat聊天室***");
        setResizable(false);
        setLocation(500,150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,602);
        //设定按钮的位置大小
        BroadCast.setBounds(50,505,100,30);
        Sent.setBounds(200,505,100,30);
        GroupChat.setBounds(350,505,100,30);
        ConstentButton.setBounds(73,0,100,30);
        GroupButton.setBounds(300,0,100,30);
        ExitButton.setBounds(100,500,300,25);
        //标签的大小和位置
        NA.setBounds(75,100,100,25);
        AA.setBounds(75,150,100,25);
        IP.setBounds(75,200,100,25);
        PR.setBounds(75,250,100,25);


        //设定标签容器的布局和大小位置
        ChatPanel.setLayout(null);
        ChatPanel.setPreferredSize(new Dimension(500,600));
        ChatPanel.setBackground(Color.LIGHT_GRAY);
        ConstentPanel.setLayout(null);
        ConstentPanel.setPreferredSize(new Dimension(500,600));
        ConstentPanel.setBackground(Color.LIGHT_GRAY);
        PersonalinformationPanel.setLayout(null);
        PersonalinformationPanel.setPreferredSize(new Dimension(500,600));
        PersonalinformationPanel.setBackground(Color.LIGHT_GRAY);

        //设定聊天框与输入框的大小和性质
        //聊天框的大小和位置
        jspChat.setBounds(0, 0, 500, 350);
        //位于中间的提示
        Receivelabel.setBounds(0,350,500,20);
        //输入框的大小和位置
        Say.setBounds(0,380,500,120);
        //在线列表
        jspOnline.setBounds(0,35,250,565);
        jspGroup.setBounds(230,35,250,565);
        //昵称框与账号框
        NickNameArea.setBounds(175,100,200,25);
        AccountArea.setBounds(175,150,200,25);
        IPArea.setBounds(175,200,200,25);
        PortArea.setBounds(175,250,200,25);


        //聊天框自动换行
        Chat.setLineWrap(true);
        //聊天框不可编辑，只能显示消息
        Chat.setEditable(false);
        //昵称框
        NickNameArea.setEditable(false);
        //账户框
        AccountArea.setEditable(false);

        //聊天框滚动条设置不出现
        jspChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //滚动条需要时出现
        jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //设置滚动条不出现
        jspOnline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置滚动条需要的时候出现
        jspOnline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //设置在线列表的大小与位置
        //在线群聊滚动条不出现
        jspGroup.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置滚动条需要的时候出现
        jspGroup.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        //添加标签和容器
        LoginTable.addTab("                 聊天                 ",ChatPanel);
        LoginTable.addTab("                联系人                ",ConstentPanel);
        LoginTable.addTab("               个人信息               ",PersonalinformationPanel);

        //加入标签
        ChatPanel.add(jspChat);
        ChatPanel.add(Receivelabel);
        ChatPanel.add(Say);
        ChatPanel.add(BroadCast);
        ChatPanel.add(Sent);
        ChatPanel.add(GroupChat);
        ConstentPanel.add(ConstentButton);
        ConstentPanel.add(GroupButton);
        ConstentPanel.add(jspOnline);
        ConstentPanel.add(jspGroup);
        PersonalinformationPanel.add(NA);
        PersonalinformationPanel.add(NickNameArea);
        PersonalinformationPanel.add(AA);
        PersonalinformationPanel.add(AccountArea);
        PersonalinformationPanel.add(IP);
        PersonalinformationPanel.add(IPArea);
        PersonalinformationPanel.add(PR);
        PersonalinformationPanel.add(PortArea);
        PersonalinformationPanel.add(ExitButton);
        getContentPane().add(LoginTable,BorderLayout.CENTER);

        //添加响应事件
        //广播
        BroadCast.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    OutputStream out = output;
                    String say = new String(Say.getText());
                    System.out.println("广播::"+Say.getText());
                    out.write(("BroadCast/"+say).getBytes());
                }catch(IOException ex){
                    System.out.println("广播功能出错");
                }finally {
                    //清屏
                    Say.setText(" ");
                    Receivelabel.setText("点击清屏可清除当前窗口聊天记录并导出至文件|广播全体在线成员");
                }
            }
        });
        //退出
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    OutputStream out = output;
                    out.write(("Exit").getBytes());
                    System.out.println("退出");
                    System.exit(0);
                }catch(Exception ex){
                }
            }
        });
        //点击我的好友--》更新在线列表
        ConstentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    OutputStream out = output;
                    out.write(("UpdateOnlineList/").getBytes());
                    System.out.println("输出更新信号");
                }catch (IOException ex){
                }

            }
        });
        //在线列表被选中的事件
        Online.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel OnlineTbm = (DefaultTableModel) Online.getModel();
                //提取鼠标选中的行作为信息接受者
                int[] selectedIndex = Online.getSelectedRows();
                //拼接UID
                UidRecerive = new StringBuilder(" ");
                for(int i = 0; i<selectedIndex.length;i++){
                    UidRecerive.append((String)OnlineTbm.getValueAt(selectedIndex[i],1));
                    UidRecerive.append(":");
                    UidRecerive.append((String)OnlineTbm.getValueAt(selectedIndex[i],2));
                    if(i!= selectedIndex.length-1)
                        UidRecerive.append(",");
                }
                System.out.println("选中列表："+UidRecerive.toString());
                //把UidRecerive加入到我的群聊表
                String Temp = new String(UidRecerive.toString());
                System.out.println("正在更新群聊列表"+Temp);
                DefaultTableModel GroupListTbm = (DefaultTableModel) GroupList.getModel();
                String[] Grouplist = Temp.split(",");
                for(String Member : Grouplist){
                    String[] tmp = new String[3];
                    tmp[0] = "昵称";
                    tmp[1] = Member.substring(0,Member.indexOf(":"));
                    tmp[2] = Member.substring(Member.indexOf(":")+1);
                    GroupListTbm.addRow(tmp);
                }
                Receivelabel.setText("发给："+UidRecerive.toString());
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //添加发送按钮的事件
        Sent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(UidRecerive.toString().equals(" ") == true)
                    JOptionPane.showMessageDialog(null,"请在好友列表选择接收消息的对象","No Receiver",JOptionPane.ERROR_MESSAGE);
                else
                {
                    Chat.setCaretPosition(Chat.getDocument().getLength());
                    try{
                        Chat.append(sdf.format(new Date())+"\n发往"+UidRecerive.toString()+":\n");
                        Chat.append(Say.getText() + "\n");
                        //发送聊天消息
                        output.write(("Chat/"+UidRecerive.toString()+"/"+Say.getText()).getBytes());
                    }catch (Exception ex){
                        System.out.println("发送错误");
                    }finally {
                        Say.setText(" ");
                    }
                }
            }
        });
        //为GroupButton设置事件
        GroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //清空
                ((DefaultTableModel) GroupList.getModel()).getDataVector().clear();
                //刷新
                GroupList.updateUI();
                Receivelabel.setText("已清空聊天对象！");
                JOptionPane.showMessageDialog(null,"清空群聊列表成功！请重新选择聊天对象！","Clear Sucessful!",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //把Group Chat按钮重新定义为清屏并且添加事件
        GroupChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //先导出到文件
                chattofile();
                //清空
                Chat.setText(" ");
                Receivelabel.setText("已清空并导入对应文件");
            }
        });
        //为个人信息面板添加事件，获取用户的个人信息并返回
        LoginTable.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //向服务器请求个人信息

                try{
                    OutputStream out = output;
                    System.out.println("正在请求个人信息……");
                    out.write(("PersonalInformation/").getBytes());
                    System.out.println("请求已发送……");
                }catch (Exception ex){
                    System.out.println("个人信息请求出错！");
                }
            }
        });
    }
    //导出聊天记录到文件
    public void chattofile(){
        String filename = accountfile;
        String path = "src/ChatFile/";
        String chatfilename = path+filename+".txt";
        String chat = Chat.getText();
        File ChatFile = new File(chatfilename);
        try{
            if(!ChatFile.exists()){
                //创建新文件
                ChatFile.createNewFile();
                OutputStream out = new FileOutputStream(ChatFile);
                out.write(chat.getBytes());
            }
            else{
                OutputStream out = new FileOutputStream(ChatFile,true);
                out.write(chat.getBytes());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
