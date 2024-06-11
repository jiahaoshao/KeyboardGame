import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class GameClient1 {
    public static void main(String[] args) throws Exception{
        //1.客户端请求于服务端的socket管道连接
        Socket socket = new Socket("127.0.0.1", 8888);
        //2.从socket通信管道中得到一个字节输入流
        OutputStream os = socket.getOutputStream();
        //3.包装为高级打印流
        PrintStream ps = new PrintStream(os);
        //4.开始发送消息出去
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.print("请输入消息：");
            ps.println(sc.nextLine());
            ps.flush();
        }
    }
}
