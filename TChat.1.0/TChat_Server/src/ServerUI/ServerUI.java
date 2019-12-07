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

    //����ʱ����ʾ��ʽ
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    //��ǩ
    JTabbedPane LoginTable = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);

    //���尴ť
    JButton BroadCast = new JButton("�㲥");
    JButton LogoutButton = new JButton("ǿ������");
    JButton Logout = new JButton("�˳����رշ�����");

    //��������
    JPanel BroadCastPanel = new JPanel();
    JPanel LoginListPanel = new JPanel();
    JPanel ServerInformation = new JPanel();

    //���������������
    JTextArea Chat = new JTextArea();
    JTextArea Say = new JTextArea();
    JScrollPane jspChat = new JScrollPane(Chat);

    //�����������Ϣ��
    JTextArea IP = new JTextArea();
    JTextArea Port = new JTextArea();
    JLabel IPLabel = new JLabel("������IP");
    JLabel PortLabel = new JLabel("������PORT");

    //������
    String[] colTitles = {"�ǳ�","IP","�˿�"};
    String[][] rowData = null;
    JTable Online = new JTable(
            new DefaultTableModel(rowData,colTitles){
                //��񲻿ɱ༭��ֻ����ʾ
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            }
    );

    JScrollPane jspOnline = new JScrollPane(Online);
    public ServerUI(String s){
        ss = s;
        setTitle("***TChat����������ҳ��***");
        setResizable(false);
        setSize(500,600);
        setVisible(true);
        setLocation(500,150);

        //����������С
        BroadCastPanel.setLayout(null);
        BroadCastPanel.setPreferredSize(new Dimension(500,600));
        BroadCastPanel.setBackground(Color.GRAY);
        LoginListPanel.setLayout(null);
        LoginListPanel.setPreferredSize(new Dimension(500,600));
        LoginListPanel.setBackground(Color.GRAY);
        ServerInformation.setLayout(null);
        ServerInformation.setPreferredSize(new Dimension(500,600));
        ServerInformation.setBackground(Color.LIGHT_GRAY);

        //���ҳ
        LoginTable.addTab("                 �㲥                 ",BroadCastPanel);
        LoginTable.addTab("               �����б�               ",LoginListPanel);
        LoginTable.addTab("                ������                ",ServerInformation);
        add(LoginTable);

        //��ȡ�������ĵ�ַ���˿�
        IP.setText(ss);
        Port.setText("1000");

        //����
        //������Զ�����
        Chat.setLineWrap(true);
        //����򲻿ɱ༭��ֻ����ʾ��Ϣ
        Chat.setEditable(false);
        jspChat.setBounds(0,0,500,350);
        Say.setBounds(0,355,500,145);
        BroadCast.setBounds(100,505,300,25);
        BroadCastPanel.add(jspChat);
        BroadCastPanel.add(Say);
        BroadCastPanel.add(BroadCast);

        //�б����
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

        //��ӹ㲥����
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
                    System.out.println("�㲥�ɹ���\n�㲥��Ϣ��"+Msg);
                    Say.setText(" ");
                    Chat.append(sdf.format(new Date())+"\n����ȫ�������û���\n"+Msg);
                    Chat.setCaretPosition(Chat.getDocument().getLength());
                }
            }
        });
        //�˳����رշ�����
        Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"�ɹ��رշ�������","Logout Server",JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        //ÿ���л�Tab��ʱ���ˢ���û��б�
        LoginTable.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //���л�����¼�б��ʱ��
                if(LoginTable.getSelectedIndex()==1)
                    add_loginlist_to_table();
            }
        });
        //���ѡ�е��¼�
        Online.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel OnlineTbm = (DefaultTableModel) Online.getModel();
                //��ȡ���ѡ�е�����Ϊ��Ϣ������
                int[] selectedIndex = Online.getSelectedRows();
                //ƴ��UID
                SelectedUID = new StringBuilder(" ");
                for(int i = 0; i<selectedIndex.length;i++){
                    SelectedUID.append((String)OnlineTbm.getValueAt(selectedIndex[i],1));
                    SelectedUID.append(":");
                    SelectedUID.append((String)OnlineTbm.getValueAt(selectedIndex[i],2));
                    if(i!= selectedIndex.length-1)
                        SelectedUID.append(",");
                }
                UIDSELECTED = SelectedUID.toString().substring(SelectedUID.toString().indexOf(" ")+1);
                System.out.println("ѡ���б�:"+UIDSELECTED);
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
        //���ǿ�����߹���
        LogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Temp = SelectedUID.toString().trim().trim();
                    String[] Delect = Temp.split(",");
                    System.out.println("��������:"+Temp+"����"+Delect[0]);
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
    //�ѵ�¼�û���ӵ������������
    public void add_loginlist_to_table(){
        if(ServerThread.Login_Arr.isEmpty()==false){
            System.out.println(ServerThread.Login_Arr.isEmpty());
            DefaultTableModel Tbm = (DefaultTableModel) Online.getModel();
            //����б�
            Tbm.setRowCount(0);
            String loginlist = " ";
            for(String list : ServerThread.Login_Arr){
                loginlist = loginlist + list+",";
            }
            String[] Onlinelist = loginlist.split(",");
            //��������б�
            for(String member : Onlinelist){
                //����ո�
                String Member = member.trim();
                //��ȡaccount
                String account = ServerThread.UID_Account.get(Member);
                System.out.println("�˺ţ�"+account);
                String[] tmp = new String[3];
                //��ȡ�ǳ�
                tmp[0] = ServerThread.Account_Ncikname.get(account);
                tmp[1] = Member.substring(0,Member.indexOf(":"));//��ʾIP
                tmp[2] = Member.substring(Member.indexOf(":")+1);//�˿�
                //����б�
                Tbm.addRow(tmp);
            }
        }
        else
            JOptionPane.showMessageDialog(null,"���ѵ�¼�û�","No Login",JOptionPane.INFORMATION_MESSAGE);
    }
}
