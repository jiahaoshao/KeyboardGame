import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) throws Exception{
        System.out.println("———————服务端启动———————");
        //1.注册端口：public ServerSocket(int port)
        ServerSocket serverSocket = new ServerSocket(8888);
        //2.定义一个循环不断接受客户端的连接请求
        while (true) {
            //3.开始等待接受客户端的Socket管道连接
            Socket accept = serverSocket.accept();
            new ServerReaderThread(accept).start();
        }
    }
}

class ServerReaderThread extends Thread{
    private Socket socket;

    public ServerReaderThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //3.从socket通信管道中得到一个字节输入流
            InputStream is = socket.getInputStream();
            //4.把字节输入流转换成字符输入流
            InputStreamReader isr = new InputStreamReader(is);
            //5.把字符输入流包装为缓冲字符输入流
            BufferedReader br = new BufferedReader(isr);
            //6.按照行读取消息
            String line;
            while ((line = br.readLine())!= null){
                System.out.println(socket.getRemoteSocketAddress()+"说："+line);
            }
        }catch (Exception e){
            System.out.println("客户端"+socket.getRemoteSocketAddress()+"下线了。");
        }
    }
}
