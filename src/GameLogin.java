import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class GameLogin {
    static class gui extends JFrame {
        public JTextField userField;
        public JPasswordField passField;
        public JButton Login_Jb;
        public JButton Register_Jb;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int x = (int)width / 2 - 150;
        int y = (int)height / 2 - 150;

        gui(int x, int y, int h, int w) {
            super("登陆界面");
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

            setDefaultCloseOperation(EXIT_ON_CLOSE);
            // 创建标签和文本字段
            JLabel userLabel = new JLabel("用户名:");
            userField = new JTextField(15);
            JLabel passLabel = new JLabel("密   码:");
            passField = new JPasswordField(15);

            Login_Jb = new JButton("登录");
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
            add(passLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            add(passField, constraints);

            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            add(Login_Jb, constraints);

            constraints.gridx = 0;
            constraints.gridy = 3;
            add(Register_Jb, constraints);


            Login_Jb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        ClickLogin_Jb();
                    } catch (SQLException | IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            Register_Jb.addActionListener(e -> ClickRegister_Jb());
            //pack();
        }

        public void ClickLogin_Jb() throws Exception {
            String username = userField.getText();
            String password = passField.getText();
            if(username.equals("root") && password.equals("root"))
            {
                //new GameServer();
                //new GameClient1();
            }
            else
            {
                if(!GameSql.isexist(username, password))
                {
                    JOptionPane.showMessageDialog(null, "用户名或密码错误，请重新输入！", "错误", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "登录成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    new GameGUI(x, y, 300, 300, username);
                }
            }

            System.out.println(username + "\n" + password + "\n");
        }

        public void ClickRegister_Jb()
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            int x = (int)width / 2 - 150;
            int y = (int)height / 2 - 150;
            //this.setVisible(false);
            new GameRegister(x, y, 300, 300);
        }
    }

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int x = (int)width / 2 - 150;
        int y = (int)height / 2 - 150;

        new gui(x, y, 300, 300);

        GameSql.init();
    }
}
