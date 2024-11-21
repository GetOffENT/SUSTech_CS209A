package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static application.MyServer.socketList;

public class LoginService implements Runnable{
    private Socket s;
    private Scanner in;
    private PrintWriter out;

//    public  ArrayList<Socket> socketList=new ArrayList<>();

    public LoginService(Socket aSocket){
        this.s=aSocket;

    }

    @Override
    public void run() {

        InputStream inputStream= null;
        try {
            inputStream = s.getInputStream();
            OutputStream outputStream=s.getOutputStream();
            in=new Scanner(inputStream);
            out=new PrintWriter(outputStream);
            doSomeService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //针对未匹配的玩家
    public void doSomeService(){
        while (true){
            if(!in.hasNext())
                break;
            String command=in.next();
            System.out.println("Received command: "+command);
            executeCommand(command);
//            if(executeCommand(command)){ //登录成功
////                int id1=MyServer.onlineList.get(0),id2=MyServer.onlineList.get(1);
//                if(MyServer.socketList.size()<2){
//                    continue;
//                }
//                MyService aService=new MyService(MyServer.socketList.get(0),MyServer.socketList.get(1));
//                Thread t2=new Thread(aService);
//                t2.start();
//            }
        }
    }
    public boolean executeCommand(String command){
        boolean succeed=true;
        if("Login".equals(command)){
            String username=in.next();
            String password=in.next();
            System.out.println(command+username+password);
            succeed=false;
            for(int i=0;i<MyServer.playerList.size();++i)
                if(username.equals(MyServer.playerList.get(i).username) && password.equals(MyServer.playerList.get(i).password)){
                    out.println("succeed "+MyServer.playerList.get(i).totalGame+" "+MyServer.playerList.get(i).wonGame);
                    MyServer.onlineList.add(i);
                    socketList.add(s);
                    succeed=true;
                    break;
                }
            if(!succeed)
                out.println("fail");
        }else if ("Signup".equals(command)){
            String username=in.next();
            String password=in.next();
            System.out.println(command+username+password);
            succeed=true;
            for(int i=0;i<MyServer.playerList.size();++i)
                if(username.equals(MyServer.playerList.get(i).username)){
                    out.println("fail");
                    succeed=false;
                    break;
                }
            if(succeed){
                out.println("succeed");
                MyServer.playerList.add(new Player(username,password,0,0));
                MyServer.onlineList.add(MyServer.playerList.size());
                socketList.add(s);
            }
        }else if("Prepared".equals(command)){
            socketList.add(s);
            if((socketList.size()/2)%2==1){
                System.out.println("Please wait for another player...请等待对手上线...");
                out.println("Please wait for another player...请等待对手上线...");
                out.flush();
                return false;
                }
            else {
                System.out.println("SocketList size:"+socketList.size());
                System.out.println("GameBegin");
                out.println("GameBegin");
                out.flush();
                try {
                    PrintWriter out2=new PrintWriter(socketList.get(0).getOutputStream());
                    out2.println("GameBegin");
                    out2.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MyService aService=new MyService(s,socketList.get(0));
                Thread t2=new Thread(aService);
                t2.start();
                in.close();//停止接收命令
            }
        }
        out.flush();
        if(succeed)
            return true;
        else return false;
    }
}
