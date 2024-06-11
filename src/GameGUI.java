import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {

    public JLabel userLabel;
    public JButton Singleplayer = new JButton("单人模式");
    public JButton Multiplayer = new JButton("多人模式");
    public JButton Result_inquiry = new JButton("成绩查询");


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

        String user = "用户名：" + username;
        userLabel = new JLabel(user);
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

    }
    public void Click_Multiplayer()
    {

    }
    public void Click_Result_inquiry()
    {

    }

}
