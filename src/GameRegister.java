import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GameRegister extends JFrame{
    public JTextField userField;
    public JPasswordField passField1;
    public JPasswordField passField2;
    public JButton Login_Jb;
    public JButton Register_Jb;

    GameRegister(int x, int y, int h, int w) {
        super("注册界面");
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
        // 创建标签和文本字段
        JLabel userLabel = new JLabel("用户名:");
        userField = new JTextField(15);
        JLabel passLabel1 = new JLabel("密   码:");
        passField1 = new JPasswordField(15);
        JLabel passLabel2 = new JLabel("确认密码:");
        passField2 = new JPasswordField(15);
        Register_Jb = new JButton("注册");

        // 添加组件到窗口
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(userLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(userField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(passLabel1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        add(passField1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(passLabel2, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        add(passField2, constraints);


        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(Register_Jb, constraints);


        Register_Jb.addActionListener(e -> ClickRegister_Jb());
        //pack();
    }

    public void ClickRegister_Jb() {
        String username = userField.getText();
        String password1 = passField1.getText();
        String password2 = passField2.getText();
        if(GameSql.isexistusername(username)) {
            JOptionPane.showMessageDialog(null, "该用户名已存在", "错误", JOptionPane.ERROR_MESSAGE);
        }
        else{
            if(!Objects.equals(password1, password2))
            {
                JOptionPane.showMessageDialog(null, "两次输入的密码不一样", "错误", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                GameSql.add(username, password1);
                JOptionPane.showMessageDialog(null, "注册成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        System.out.println(username + "\n" + password1 + "\n" + password2);

    }
}

