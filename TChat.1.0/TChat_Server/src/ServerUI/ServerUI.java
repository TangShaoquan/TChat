package ServerUI;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SelectableChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import ServerThread.ServerThread;


public class ServerUI extends JFrame {

    public  static StringBuilder SelectedUID = null;
    public  static String UIDSELECTED = new String();
    String ss;

    //设置时间显示格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    //标签
    JTabbedPane LoginTable = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);

    //定义按钮
    JButton BroadCast = new JButton("广播");
    JButton LogoutButton = new JButton("强行下线");
    JButton Logout = new JButton("退出并关闭服务器");

    //定义容器
    JPanel BroadCastPanel = new JPanel();
    JPanel LoginListPanel = new JPanel();
    JPanel ServerInformation = new JPanel();

    //定义聊天框和输入框
    JTextArea Chat = new JTextArea();
    JTextArea Say = new JTextArea();
    JScrollPane jspChat = new JScrollPane(Chat);

    //定义服务器信息框
    JTextArea IP = new JTextArea();
    JTextArea Port = new JTextArea();
    JLabel IPLabel = new JLabel("服务器IP");
    JLabel PortLabel = new JLabel("服务器PORT");

    //定义表格
    String[] colTitles = {"昵称","IP","端口"};
    String[][] rowData = null;
    JTable Online = new JTable(
            new DefaultTableModel(rowData,colTitles){
                //表格不可编辑，只可显示
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            }
    );

    JScrollPane jspOnline = new JScrollPane(Online);
    public ServerUI(String s){
        ss = s;
        setTitle("***TChat服务器管理页面***");
        setResizable(false);
        setSize(500,600);
        setVisible(true);
        setLocation(500,150);

        //设置容器大小
        BroadCastPanel.setLayout(null);
        BroadCastPanel.setPreferredSize(new Dimension(500,600));
        BroadCastPanel.setBackground(Color.GRAY);
        LoginListPanel.setLayout(null);
        LoginListPanel.setPreferredSize(new Dimension(500,600));
        LoginListPanel.setBackground(Color.GRAY);
        ServerInformation.setLayout(null);
        ServerInformation.setPreferredSize(new Dimension(500,600));
        ServerInformation.setBackground(Color.LIGHT_GRAY);

        //添加页
        LoginTable.addTab("                 广播                 ",BroadCastPanel);
        LoginTable.addTab("               在线列表               ",LoginListPanel);
        LoginTable.addTab("                服务器                ",ServerInformation);
        add(LoginTable);

        //获取服务器的地址及端口
        IP.setText(ss);
        Port.setText("1000");

        //聊天
        //聊天框自动换行
        Chat.setLineWrap(true);
        //聊天框不可编辑，只能显示消息
        Chat.setEditable(false);
        jspChat.setBounds(0,0,500,350);
        Say.setBounds(0,355,500,145);
        BroadCast.setBounds(100,505,300,25);
        BroadCastPanel.add(jspChat);
        BroadCastPanel.add(Say);
        BroadCastPanel.add(BroadCast);

        //列表管理
        jspOnline.setBounds(0,0,500,500);
        LogoutButton.setBounds(100,505,300,25);
        LoginListPanel.add(LogoutButton);
        LoginListPanel.add(jspOnline);

        IPLabel.setBounds(100,190,50,50);
        PortLabel.setBounds(100,240,70,50);
        IP.setBounds(175,200,200,25);
        Port.setBounds(175,250,200,25);
        Logout.setBounds(100,500,300,25);
        ServerInformation.add(IPLabel);
        ServerInformation.add(PortLabel);
        ServerInformation.add(Port);
        ServerInformation.add(IP);
        ServerInformation.add(Logout);

        //添加广播功能
        BroadCast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Msg = new String(Say.getText());
                try{
                    for(String tmp_id : ServerThread.Login_Arr){
                        OutputStream output = ServerThread.UID_ServerThread.get(tmp_id).S.getOutputStream();
                        output.write(("BroadCast/"+"192.168.232.1:10000/"+Msg).getBytes());
                    }
                }catch(IOException ex){
                }finally {
                    System.out.println("广播成功！\n广播消息："+Msg);
                    Say.setText(" ");
                    Chat.append(sdf.format(new Date())+"\n发往全部在线用户：\n"+Msg);
                    Chat.setCaretPosition(Chat.getDocument().getLength());
                }
            }
        });
        //退出并关闭服务器
        Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"成功关闭服务器！","Logout Server",JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        //每次切换Tab的时候就刷新用户列表
        LoginTable.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //当切换到登录列表的时候
                if(LoginTable.getSelectedIndex()==1)
                    add_loginlist_to_table();
            }
        });
        //添加选中的事件
        Online.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel OnlineTbm = (DefaultTableModel) Online.getModel();
                //提取鼠标选中的行作为信息接受者
                int[] selectedIndex = Online.getSelectedRows();
                //拼接UID
                SelectedUID = new StringBuilder(" ");
                for(int i = 0; i<selectedIndex.length;i++){
                    SelectedUID.append((String)OnlineTbm.getValueAt(selectedIndex[i],1));
                    SelectedUID.append(":");
                    SelectedUID.append((String)OnlineTbm.getValueAt(selectedIndex[i],2));
                    if(i!= selectedIndex.length-1)
                        SelectedUID.append(",");
                }
                UIDSELECTED = SelectedUID.toString().substring(SelectedUID.toString().indexOf(" ")+1);
                System.out.println("选中列表:"+UIDSELECTED);
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
        //添加强制下线功能
        LogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Temp = SelectedUID.toString().trim().trim();
                    String[] Delect = Temp.split(",");
                    System.out.println("即将下线:"+Temp+"数组"+Delect[0]);
                    try{
                        for(int i = 0 ; i < Delect.length ; i++){
                            System.out.println(Delect[i]);
                            OutputStream out = ServerThread.UID_ServerThread.get(Delect[i]).S.getOutputStream();
                            out.write(("Logout/").getBytes());
                            ServerThread.UID_ServerThread.get(Delect[i]).S.close();
                            ServerThread.Login_Arr.remove(Delect[i]);
                        }
                    }catch (IOException ex){
                    }finally {
                        DefaultTableModel OnlineTbm = (DefaultTableModel) Online.getModel();
                        for(int i =0 ; i < OnlineTbm.getRowCount();i++){
                            OnlineTbm.removeRow(i);
                        }
                        add_loginlist_to_table();
                    }
            }
        });
    }
    //把登录用户添加到服务器表格中
    public void add_loginlist_to_table(){
        if(ServerThread.Login_Arr.isEmpty()==false){
            System.out.println(ServerThread.Login_Arr.isEmpty());
            DefaultTableModel Tbm = (DefaultTableModel) Online.getModel();
            //清空列表
            Tbm.setRowCount(0);
            String loginlist = " ";
            for(String list : ServerThread.Login_Arr){
                loginlist = loginlist + list+",";
            }
            String[] Onlinelist = loginlist.split(",");
            //添加在线列表
            for(String member : Onlinelist){
                //处理空格
                String Member = member.trim();
                //获取account
                String account = ServerThread.UID_Account.get(Member);
                System.out.println("账号："+account);
                String[] tmp = new String[3];
                //获取昵称
                tmp[0] = ServerThread.Account_Ncikname.get(account);
                tmp[1] = Member.substring(0,Member.indexOf(":"));//显示IP
                tmp[2] = Member.substring(Member.indexOf(":")+1);//端口
                //添加列表
                Tbm.addRow(tmp);
            }
        }
        else
            JOptionPane.showMessageDialog(null,"无已登录用户","No Login",JOptionPane.INFORMATION_MESSAGE);
    }
}
