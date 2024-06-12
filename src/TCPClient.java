import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPClient{
    private  ObjectOutputStream oOut;
    private ObjectInputStream oIn;
    private  Message message;
    private String host;
    private  String name;
    private  String to;
    private readInfoThread reader;

    TCPClient(String host1, String name1, String to1){
        name = name1;
        to = to1;
        host = host1;
        //单线程池
        ExecutorService es = Executors.newSingleThreadExecutor();
        try {
            //创建客户端
            Socket socket = new Socket(host, 8888);
            System.out.println("服务器连接成功！");
            //构建输出输入流
            oOut = new ObjectOutputStream(socket.getOutputStream());
            oIn = new ObjectInputStream(socket.getInputStream());
            //1、客户端登录处理
            //向服务器发送登录信息（名字和消息类型）
//            System.out.println("请输入名称：");
//            String name = input.nextLine();
            //登录时，只自己的名字和消息类型为登录
            message = new Message(name, null, MessageType.TYPE_LOGIN, null);
            //发送给服务器
            oOut.writeObject(message);
            //服务器返回 欢迎信息
            message = (Message) oIn.readObject();
            //打印服务器返回的信息+当前客户端的名字
            System.out.println(message.getInfo() + message.getFrom());
            //2、启动读取消息的线程
            reader = new readInfoThread(oIn);
            es.execute(reader);  //读取线程完成
            //3、发送消息
            //使用主线程来发送消息

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendinfo(String info) throws IOException {
        message = new Message();
        //发给谁
//                System.out.println("To：");
//                message.setTo(input.nextLine());
        message.setTo(to);
        //谁发的，从自己这发
        message.setFrom(name);
        //类型 发送消息
        message.setType(MessageType.TYPE_SEND);
        //发送的内容
        System.out.println(name + " SendInfo：" + info);
        message.setInfo(info);
        /*----到此需要发送的消息 对象 封装完毕----*/
        //发送给服务器
        oOut.writeObject(message);
    }

    public String getstate()
    {
        Message readerMessage1 = reader.getMessage1();
        if(readerMessage1 == null || readerMessage1.getInfo() == null) return "0";
        String state = readerMessage1.getInfo();
        String[] state1 = state.split(" ");
        if(Objects.equals(state1[0], "state"))
            return state1[1];
        return "0";
    }

    public int getscore()
    {
        Message readerMessage1 = reader.getMessage1();
        if(readerMessage1 == null || readerMessage1.getInfo() == null) return 0;
        String state = readerMessage1.getInfo();
        String[] state1 = state.split(" ");
        if(Objects.equals(state1[2], "score"))
        {
            return Integer.parseInt(state1[3]);
        }
        return 0;
    }
}

/**
 * 读取其他客户端发来消息
 */
class readInfoThread implements Runnable {
    private ObjectInputStream oIn; //输入流 用来读操作
    private boolean flag = true; //标记
    private List<Message> messageList = Collections.synchronizedList(new ArrayList<>());
    private Message message1;

    public readInfoThread(ObjectInputStream oIn) {
        this.oIn = oIn;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {

        try {
            //循环 不断读取消息
            while (flag) {
                //读取信息
                Message message = (Message) oIn.readObject();
                messageList.add(message);
                message1 = message;
                //输出用户名+内容
                System.out.println("[" + message.getFrom() + "]对[" + message.getTo() + "]说：" + message.getInfo());
            }
            //没有数据就关闭
            if(oIn != null){
                oIn.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 公共方法来获取消息列表
    public List<Message> getMessageList() {
        synchronized (messageList) {
            return new ArrayList<>(messageList); // 返回消息列表的副本
        }
    }

    public Message getMessage1() {
        return message1;
    }
}
