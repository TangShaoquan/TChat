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
        //建立服务器ServerSocket
        ServerSocket Server = new ServerSocket(10000);
        //提示Server建立成功
        System.out.println("Server online...  " + Server.getInetAddress().getLocalHost().getHostAddress() +":"+ 10000);
        String ss = Server.getInetAddress().getLocalHost().getHostAddress();

        ServerUI serverUI = new ServerUI(ss);
        //监听端口，建立连接并开启新的ServerThread线程来服务此连接
        while(true)
        {
            //接收客户端Socket
            Socket Client = Server.accept();
            //提取客户端IP和端口
            String Client_ip = Client.getInetAddress().getLocalHost().getHostAddress();
            int Client_port = Client.getPort();
            System.out.println(Client_ip+"+"+Client_port);
            //建立新的服务器线程, 向该线程提供服务器ServerSocket，客户端Socket，客户端IP和端口
            new Thread(new ServerThread(Client,Server,Client_ip,Client_port)).start();
        }
    }
}


