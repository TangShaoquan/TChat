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



    //����ʱ����ʾ��ʽ
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    JTabbedPane LoginTable = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);//��ǩ���Ŷ���������ʱ�۵��ڷű�ǩ

    //���尴ť
    JButton Sent = new JButton("����");
    JButton ExitButton = new JButton("�˳�TChat");
    JButton GroupChat = new JButton("����");
    JButton BroadCast = new JButton("�㲥");
    JButton  ConstentButton = new JButton("�ҵĺ���");
    JButton GroupButton = new JButton("�ҵ�����");

    JPanel ChatPanel = new JPanel();
    JPanel ConstentPanel = new JPanel();
    JPanel PersonalinformationPanel = new JPanel();

    StringBuilder uidReceiver = null;
    String accountfile = new String();

    //�ı������
    JTextArea Say = new JTextArea();
    //��Ϣ��
    JTextArea Chat = new JTextArea();
    //�ǳƿ�
    JTextArea NickNameArea = new JTextArea();
    //�˺ſ�
    JTextArea AccountArea = new JTextArea();
    //IP��
    JTextArea IPArea = new JTextArea();
    //�˿ڿ�
    JTextArea PortArea = new JTextArea();
    //�����б�ı���
    String[] colTitles = {"�ǳ�","IP","�˿�"};
    //�����б������
    String[][] rowData = null;
    //�����б�
    JTable Online = new JTable(
            new DefaultTableModel(rowData,colTitles){
                //��񲻿ɱ༭��ֻ����ʾ
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            }
    );
    //����Ⱥ�б�ı���
    String[] GColTitles = {"�ǳ�","IP","�˿�"};
    //����Ⱥ�ĵ�����
    String[][] GRowData = null;
    //����Ⱥ���б�
    JTable GroupList = new JTable(
            new DefaultTableModel(GRowData,GColTitles){
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            }
    );
    //������Ϣ������
    JScrollPane jspChat = new JScrollPane(Chat);
    //�����б������
    JScrollPane jspOnline = new JScrollPane(Online);
    //����Ⱥ�Ĺ�����
    JScrollPane jspGroup = new JScrollPane(GroupList);

    Label NA = new Label("     NickName     ");
    Label AA = new Label("      Account     ");
    Label IP = new Label("        IP        ");
    Label PR = new Label("       PORT       ");

    JLabel Receivelabel = new JLabel("��������������ǰ���������¼���������ļ�");

///////////////////////////////////���캯��////////////////////////////////////////
    public ClientUI(OutputStream output,InputStream input){

        setIconImage(new ImageIcon("src/Start/TChat.jpg").getImage());
        setTitle("***TChat������***");
        setResizable(false);
        setLocation(500,150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,602);
        //�趨��ť��λ�ô�С
        BroadCast.setBounds(50,505,100,30);
        Sent.setBounds(200,505,100,30);
        GroupChat.setBounds(350,505,100,30);
        ConstentButton.setBounds(73,0,100,30);
        GroupButton.setBounds(300,0,100,30);
        ExitButton.setBounds(100,500,300,25);
        //��ǩ�Ĵ�С��λ��
        NA.setBounds(75,100,100,25);
        AA.setBounds(75,150,100,25);
        IP.setBounds(75,200,100,25);
        PR.setBounds(75,250,100,25);


        //�趨��ǩ�����Ĳ��ֺʹ�Сλ��
        ChatPanel.setLayout(null);
        ChatPanel.setPreferredSize(new Dimension(500,600));
        ChatPanel.setBackground(Color.LIGHT_GRAY);
        ConstentPanel.setLayout(null);
        ConstentPanel.setPreferredSize(new Dimension(500,600));
        ConstentPanel.setBackground(Color.LIGHT_GRAY);
        PersonalinformationPanel.setLayout(null);
        PersonalinformationPanel.setPreferredSize(new Dimension(500,600));
        PersonalinformationPanel.setBackground(Color.LIGHT_GRAY);

        //�趨������������Ĵ�С������
        //�����Ĵ�С��λ��
        jspChat.setBounds(0, 0, 500, 350);
        //λ���м����ʾ
        Receivelabel.setBounds(0,350,500,20);
        //�����Ĵ�С��λ��
        Say.setBounds(0,380,500,120);
        //�����б�
        jspOnline.setBounds(0,35,250,565);
        jspGroup.setBounds(230,35,250,565);
        //�ǳƿ����˺ſ�
        NickNameArea.setBounds(175,100,200,25);
        AccountArea.setBounds(175,150,200,25);
        IPArea.setBounds(175,200,200,25);
        PortArea.setBounds(175,250,200,25);


        //������Զ�����
        Chat.setLineWrap(true);
        //����򲻿ɱ༭��ֻ����ʾ��Ϣ
        Chat.setEditable(false);
        //�ǳƿ�
        NickNameArea.setEditable(false);
        //�˻���
        AccountArea.setEditable(false);

        //�������������ò�����
        jspChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //��������Ҫʱ����
        jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //���ù�����������
        jspOnline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //���ù�������Ҫ��ʱ�����
        jspOnline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //���������б�Ĵ�С��λ��
        //����Ⱥ�Ĺ�����������
        jspGroup.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //���ù�������Ҫ��ʱ�����
        jspGroup.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        //��ӱ�ǩ������
        LoginTable.addTab("                 ����                 ",ChatPanel);
        LoginTable.addTab("                ��ϵ��                ",ConstentPanel);
        LoginTable.addTab("               ������Ϣ               ",PersonalinformationPanel);

        //�����ǩ
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

        //�����Ӧ�¼�
        //�㲥
        BroadCast.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    OutputStream out = output;
                    String say = new String(Say.getText());
                    System.out.println("�㲥::"+Say.getText());
                    out.write(("BroadCast/"+say).getBytes());
                }catch(IOException ex){
                    System.out.println("�㲥���ܳ���");
                }finally {
                    //����
                    Say.setText(" ");
                    Receivelabel.setText("��������������ǰ���������¼���������ļ�|�㲥ȫ�����߳�Ա");
                }
            }
        });
        //�˳�
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    OutputStream out = output;
                    out.write(("Exit").getBytes());
                    System.out.println("�˳�");
                    System.exit(0);
                }catch(Exception ex){
                }
            }
        });
        //����ҵĺ���--�����������б�
        ConstentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    OutputStream out = output;
                    out.write(("UpdateOnlineList/").getBytes());
                    System.out.println("��������ź�");
                }catch (IOException ex){
                }

            }
        });
        //�����б�ѡ�е��¼�
        Online.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel OnlineTbm = (DefaultTableModel) Online.getModel();
                //��ȡ���ѡ�е�����Ϊ��Ϣ������
                int[] selectedIndex = Online.getSelectedRows();
                //ƴ��UID
                UidRecerive = new StringBuilder(" ");
                for(int i = 0; i<selectedIndex.length;i++){
                    UidRecerive.append((String)OnlineTbm.getValueAt(selectedIndex[i],1));
                    UidRecerive.append(":");
                    UidRecerive.append((String)OnlineTbm.getValueAt(selectedIndex[i],2));
                    if(i!= selectedIndex.length-1)
                        UidRecerive.append(",");
                }
                System.out.println("ѡ���б�"+UidRecerive.toString());
                //��UidRecerive���뵽�ҵ�Ⱥ�ı�
                String Temp = new String(UidRecerive.toString());
                System.out.println("���ڸ���Ⱥ���б�"+Temp);
                DefaultTableModel GroupListTbm = (DefaultTableModel) GroupList.getModel();
                String[] Grouplist = Temp.split(",");
                for(String Member : Grouplist){
                    String[] tmp = new String[3];
                    tmp[0] = "�ǳ�";
                    tmp[1] = Member.substring(0,Member.indexOf(":"));
                    tmp[2] = Member.substring(Member.indexOf(":")+1);
                    GroupListTbm.addRow(tmp);
                }
                Receivelabel.setText("������"+UidRecerive.toString());
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
        //��ӷ��Ͱ�ť���¼�
        Sent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(UidRecerive.toString().equals(" ") == true)
                    JOptionPane.showMessageDialog(null,"���ں����б�ѡ�������Ϣ�Ķ���","No Receiver",JOptionPane.ERROR_MESSAGE);
                else
                {
                    Chat.setCaretPosition(Chat.getDocument().getLength());
                    try{
                        Chat.append(sdf.format(new Date())+"\n����"+UidRecerive.toString()+":\n");
                        Chat.append(Say.getText() + "\n");
                        //����������Ϣ
                        output.write(("Chat/"+UidRecerive.toString()+"/"+Say.getText()).getBytes());
                    }catch (Exception ex){
                        System.out.println("���ʹ���");
                    }finally {
                        Say.setText(" ");
                    }
                }
            }
        });
        //ΪGroupButton�����¼�
        GroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //���
                ((DefaultTableModel) GroupList.getModel()).getDataVector().clear();
                //ˢ��
                GroupList.updateUI();
                Receivelabel.setText("������������");
                JOptionPane.showMessageDialog(null,"���Ⱥ���б�ɹ���������ѡ���������","Clear Sucessful!",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //��Group Chat��ť���¶���Ϊ������������¼�
        GroupChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //�ȵ������ļ�
                chattofile();
                //���
                Chat.setText(" ");
                Receivelabel.setText("����ղ������Ӧ�ļ�");
            }
        });
        //Ϊ������Ϣ�������¼�����ȡ�û��ĸ�����Ϣ������
        LoginTable.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //����������������Ϣ

                try{
                    OutputStream out = output;
                    System.out.println("�������������Ϣ����");
                    out.write(("PersonalInformation/").getBytes());
                    System.out.println("�����ѷ��͡���");
                }catch (Exception ex){
                    System.out.println("������Ϣ�������");
                }
            }
        });
    }
    //���������¼���ļ�
    public void chattofile(){
        String filename = accountfile;
        String path = "src/ChatFile/";
        String chatfilename = path+filename+".txt";
        String chat = Chat.getText();
        File ChatFile = new File(chatfilename);
        try{
            if(!ChatFile.exists()){
                //�������ļ�
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
