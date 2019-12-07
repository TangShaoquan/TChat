package TChat_Server;

import java.net.ServerSocket;
import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import ServerThread.ServerThread;
import ServerUI.ServerUI;

public class TChat_Server
{
    public static void main(String[] args) throws Exception
    {
        //����������ServerSocket
        ServerSocket Server = new ServerSocket(10000);
        //��ʾServer�����ɹ�
        System.out.println("Server online...  " + Server.getInetAddress().getLocalHost().getHostAddress() +":"+ 10000);
        String ss = Server.getInetAddress().getLocalHost().getHostAddress();

        ServerUI serverUI = new ServerUI(ss);
        //�����˿ڣ��������Ӳ������µ�ServerThread�߳������������
        while(true)
        {
            //���տͻ���Socket
            Socket Client = Server.accept();
            //��ȡ�ͻ���IP�Ͷ˿�
            String Client_ip = Client.getInetAddress().getLocalHost().getHostAddress();
            int Client_port = Client.getPort();
            System.out.println(Client_ip+"+"+Client_port);
            //�����µķ������߳�, ����߳��ṩ������ServerSocket���ͻ���Socket���ͻ���IP�Ͷ˿�
            new Thread(new ServerThread(Client,Server,Client_ip,Client_port)).start();
        }
    }
}


