import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameClient extends JFrame {
    // 将isRunning变量改为ThreadLocal，确保每个线程（游戏实例）都有自己的副本
    //private ThreadLocal<Boolean> isRunning = ThreadLocal.withInitial(() -> true); // 控制游戏运行的标志
    private Boolean isRunning = true;
    private ConcurrentLinkedQueue<FallingWord> fallingWords;
    private int score = 100;
    private int score1 = 0;
    private int score2 = 0;
    private GamePanel gamePanel;
    private JPanel defenseWall;
    private Random random;
    private final int wallWidth = 800;
    private final int wallHeight = 10;
    private final int wordFallInterval = 30; // 单词下落的时间间隔（秒）
    private ArrayList<String> words = new ArrayList<>();
    private String user;
    private String opponent = "2";
    private JButton startButton;
    private JLabel opponenstate;
    private double fallingspeed = 1;//下落速度
    private TCPClient client1;
    private TCPClient client2;
    private String host = "localhost";//连接地址
    private String state1 = "0";
    private String state2 = "0";


    public GameClient(String username) {
        super("打字游戏(当前用户：" + username + ")");
        user = username;
        fallingWords = new ConcurrentLinkedQueue<>();
        score = 100;
        score1 = 0;
        random = new Random();
        promptForOpponentInfo();
        readfile();
        TCP();
        initializeStartButton();
        //initializeGame();
    }

    private void promptForOpponentInfo() {
        JTextField usernameField = new JTextField();
        JTextField hostField = new JTextField();
        Object[] message = {
                "对手用户名:", usernameField,
                "连接地址:", hostField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "输入对手信息", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            opponent = usernameField.getText();
            host = hostField.getText();
            // 现在您可以使用这些信息来设置TCP连接或其他操作
        } else {
            System.out.println("取消输入");
            // 处理取消操作
        }
    }


    private void initializeStartButton() {
        pack();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        startButton = new JButton("准备");
        startButton.addActionListener(e -> startGame());
        opponenstate = new JLabel("对手状态：未连接");
        opponenstate.setBounds(650, 0, 150, 30);
        this.add(opponenstate, BorderLayout.NORTH);
        this.add(startButton, BorderLayout.SOUTH);
        new Thread(this::Sendinfo).start();
        new Thread(this::getopponentstate).start();
    }

    private void updateOpponentState()
    {
        String st = "未连接";
        if(Objects.equals(state2, "0"))
            st = "未连接";
        if(Objects.equals(state2, "1"))
            st = "已准备";
        if(Objects.equals(state2, "2"))
            st = "对战中";
        opponenstate.setText("对手状态：" + st);
    }

    private void startGame() {
        // 重新绘制界面
        state1 = "1";
        startButton.setText("准备成功");
    }

    private void getopponentstate() {
        while (isRunning) {
            try {
                Thread.sleep(1);
                state2 = client1.getstate();
                updateOpponentState();
                if(Objects.equals(state2, "4")) GameOver();
                //score2 = client1.getscore();
                if(state1.equals("1") && Objects.equals(state2, "1"))
                {
                    client1.sendinfo("state " + state1 + " score " + score1);
                    state1 = "2";
                    // 移除开始按钮
                    this.remove(startButton);
                    initializeGame();
                    this.revalidate();
                    this.repaint();
                    this.remove(opponenstate);
                    new Thread(this::getopponentscore).start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void getopponentscore() {
        while (isRunning) {
            try {
                Thread.sleep(500);
                score2 = client1.getscore();
                fallingspeed = Math.max(1,1 + (score2 - score1) / 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void initializeGame() {
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(wallWidth, 600));
        add(gamePanel);
        pack();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //setLocationRelativeTo(null);
        setVisible(true);
        //setResizable(false);

        // 添加键盘监听器
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                Iterator<FallingWord> iterator = fallingWords.iterator();
                while (iterator.hasNext()) {
                    FallingWord word = iterator.next();
                    if (word.matchKeyChar(keyChar)) {
                        iterator.remove(); // 移除单词
                        updateScore(word.word.length());
                        break; // 匹配到单词后退出循环
                    }
                }
            }
        });

        new Thread(this::generateWords).start();
        new Thread(this::fallWords).start();
        //new Thread(this::Sendscore).start();
    }

    private void SendSate()
    {
        while (isRunning) {
            try {
                Thread.sleep(100);
                client1.sendinfo("state " + state1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void Sendscore()
    {
        while (isRunning) {
            try {
                Thread.sleep(1000);
                client1.sendinfo("score " + score1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void Sendinfo()
    {
        while (isRunning) {
            try {
                Thread.sleep(100);
                client1.sendinfo("state " + state1 + " score " + score1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generateWords() {
        while (isRunning) {
            try {
                //Thread.sleep(0000);
                String word = generateRandomWord();
                FallingWord fallingWord = new FallingWord(word, random.nextInt(wallWidth - 100), 0);
                fallingWords.add(fallingWord);
                Thread.sleep(5000); // 每5秒生成一个新单词
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fallWords() {
        while (isRunning) {
            try {
                for (FallingWord fallingWord : fallingWords) {
                    fallingWord.y += fallingspeed; // 控制下落速度
                    if (fallingWord.y >= getHeight() - wallHeight) {
                        fallingWords.remove(fallingWord);
                        updateDefenseWall(fallingWord.word.length());
                    }
                    //System.out.println(score);
                }
                repaint();
                Thread.sleep(50); // 控制刷新频率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateScore(int wordLength) {
        score1 += wordLength;
    }

    private void updateDefenseWall(int wordLength) {
        // 当单词到达防守墙时，更新分数并可能减少防守墙的宽度
        score -= wordLength;
        score = Math.min(100, score);
        if(score <= 0)
        {
            score = 0;
            GameOver();
        }
        else
        {
            //System.out.println(score);
            // 这里可以添加逻辑来减少防守墙的宽度
            SwingUtilities.invokeLater(() -> gamePanel.updateWallLength(score));
        }

    }

    private String generateRandomWord() {
        // 这里应该实现一个方法来随机生成单词
        return words.get(random.nextInt(words.size()));
    }

    private class GamePanel extends JPanel {
        private int currentWallLength;

        public GamePanel() {
            currentWallLength = wallWidth; // 初始长度等于最大长度
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 绘制下落的单词
            Font wordFont = new Font("Arial", Font.BOLD, 12);
            g.setFont(wordFont);
            for (FallingWord fallingWord : fallingWords) {
                g.drawString(fallingWord.word, fallingWord.x, fallingWord.y);
            }
            // 绘制防守墙
            g.setColor(Color.GREEN);
            g.fillRect(0, getHeight() - wallHeight, currentWallLength, wallHeight);
            drawScore(g);
            drawState(g);
        }

        private void drawScore(Graphics g) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("SimSun", Font.BOLD, 18));
            g.drawString("我的得分: " + score1, 10, 20);
            g.drawString("对手得分: " + score2, 10, 40);

        }

        private void drawState(Graphics g){
            g.setColor(Color.BLACK);
            g.setFont(new Font("SimSun", Font.BOLD, 18));
            if(Objects.equals(state2, "0"))
            {
                g.drawString("对手状态: 掉线" , 630, 20);
            }
            if(Objects.equals(state2, "1"))
            {
                g.drawString("对手状态: 已准备" , 630, 20);
            }
            if(Objects.equals(state2, "2"))
            {
                g.drawString("对手状态: 对战中" , 630, 20);
            }
        }

        public void updateWallLength(int score) {
            // 根据分数计算防守墙的长度
            currentWallLength = Math.max(0, score * 8);
            repaint();
        }
    }

    public void GameOver() {
        if(isRunning)
        {
            isRunning = false; // 停止游戏循环
            // 停止生成新单词和下落的单词
            // 显示游戏结束的消息
            //GameSql.add(user, score1);
            state1 = "4";
            JOptionPane.showMessageDialog(this, "游戏结束！您的最终得分是：" + score1 +"\n对手得分是：" + score2, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            GameSql.add(user, score1, opponent, score2);
            //System.exit(0); // 或者您可以选择其他方式来结束游戏，比如返回主菜单
            this.dispose();
        }

    }

    private class FallingWord {
        String word;
        int x, y;
        int matchedIndex = 0; // 用于跟踪已匹配的字符索引

        FallingWord(String word, int x, int y) {
            this.word = word;
            this.x = x;
            this.y = y;
        }

        // 检查输入的字符是否匹配单词的下一个字符
        public boolean matchKeyChar(char keyChar) {
            if (matchedIndex < word.length() && word.charAt(matchedIndex) == keyChar) {
                matchedIndex++;
                if (matchedIndex == word.length()) {
                    // 如果所有字符都匹配，则返回true
                    return true;
                }
            }
            return false;
        }
    }

    public void TCP(){
        //client2 =  new TCPClient(host, opponent,user);
        client1 =  new TCPClient(host,user,opponent);
    }

    public void readfile()
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/res/words.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
