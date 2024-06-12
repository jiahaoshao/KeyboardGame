import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    int x = (int)width / 2 - 150;
    int y = (int)height / 2 - 150;

    public JLabel userLabel;
    public JButton Singleplayer = new JButton("单人模式");
    public JButton Multiplayer = new JButton("多人模式");
    public JButton Result_inquiry = new JButton("成绩查询");
    public String user;
    private GameClient gameClient = null;
    private Game game = null;

    GameGUI(int x, int y, int h, int w, String username) {
        super("打字游戏");
        setLocation(x, y);
        //setLocationRelativeTo(null);
        setSize(h, w);
        setResizable(false);
        setVisible(true);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // 设置组件之间的间距
        int verticalSpace = 10; // 垂直间距的大小
        constraints.insets = new Insets(verticalSpace, 0, verticalSpace, 0);

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        user = "用户名：" + username;
        userLabel = new JLabel(user);
        user = username;
        // 添加组件到窗口
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(userLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(Singleplayer, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(Multiplayer, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        add(Result_inquiry, constraints);

        Singleplayer.addActionListener(e -> Click_Singleplayer());
        Multiplayer.addActionListener(e -> Click_Multiplayer());
        Result_inquiry.addActionListener(e -> Click_Result_inquiry());
    }

    public void Click_Singleplayer()
    {
        if(game == null)
            game = new Game(user);
        else
            game.setVisible(true);

    }
    public void Click_Multiplayer()
    {
//        if(gameClient == null)
//            gameClient = new GameClient(user);
//        else
//            gameClient.setVisible(true);
        gameClient = new GameClient(user);
    }
    public void Click_Result_inquiry()
    {
        new Userinfo(x, y, 800, 600, user);
    }
}
