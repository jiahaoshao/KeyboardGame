import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Userinfo extends JFrame {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    int x = (int)width / 2 - 150;
    int y = (int)height / 2 - 150;

    public JTable table;
    public String user;

    Userinfo(int x, int y, int h, int w, String username) {
        super("成绩");
        user = username;
        setLocation(x, y);
        setSize(h, w);
        setResizable(false);
        setVisible(true);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        //pack();
        String[][] tableDate = GameSql.findscord(username);
        String[] name = {"用户名", "练习时间", "练习成绩", "比赛时间", "成绩", "对手","对手成绩"};
        table = new JTable(Objects.requireNonNull(tableDate), name);
        // 设置 constraints 来铺满整个画布
        constraints.fill = GridBagConstraints.BOTH; // 让组件完全填充其显示区域
        constraints.weightx = 1.0; // 设置横向权重
        constraints.weighty = 1.0; // 设置纵向权重
        constraints.gridx = 0; // 设置组件的 GridX
        constraints.gridy = 0; // 设置组件的 GridY
        add(new JScrollPane(table), constraints);
    }
}
