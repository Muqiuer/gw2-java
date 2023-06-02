import com.itheima.Dao.UserDao;
import com.itheima.Dao.Packbag;
import com.itheima.Dao.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main extends Component {
    private static UserDao userDao;
    private static User currentUser;
    private static JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        userDao = context.getBean(UserDao.class);
        jdbcTemplate = context.getBean(JdbcTemplate.class);

        JFrame loginFrame = createLoginFrame();
        loginFrame.setVisible(true);
    }

    private static JFrame createLoginFrame() {
        JFrame frame = new JFrame("登录窗口");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("用户名:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("密码:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(registerButton);

        // 登录按钮点击事件处理
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                User user = userDao.findByUsernameAndPassword(username, password);
                if (user != null) {
                    currentUser = user;
                    frame.dispose(); // 关闭登录窗口
                    showItemShopPanel(); // 显示道具商城面板
                } else {
                    JOptionPane.showMessageDialog(frame, "用户名或密码错误！");
                }
            }
        });

        // 注册按钮点击事件处理
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                User user = new User(username, password);
                userDao.createUser(user);

                JOptionPane.showMessageDialog(frame, "注册成功！");
            }
        });

        return frame;
    }

    private static void showItemShopPanel() {
        JTextArea resultTextArea = new JTextArea();
        JTextField searchName = new JTextField(50); // 设置搜索框长度为50
        JFrame shopFrame = new JFrame("道具商城");
        shopFrame.setLocationRelativeTo(null);
        shopFrame.setSize(400, 300);
        shopFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

// 使用GridBagLayout作为布局管理器
        shopFrame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel nameLabel = new JLabel("道具名称：沙尘暴同花");
        JLabel priceLabel = new JLabel("价格：100金币");

        JButton buyButton = new JButton("购买");
        JButton packbagButton = new JButton("背包");
        JButton selectButton = new JButton("查询");

// 设置searchName的位置和大小
        constraints.gridx = 0; // 列索引为0
        constraints.gridy = 0; // 行索引为0
        constraints.gridwidth = 2; // 占据2列
        constraints.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        shopFrame.add(searchName, constraints);

// 设置selectButton的位置和大小
        constraints.gridx = 2; // 列索引为2
        constraints.gridy = 0; // 行索引为0
        constraints.gridwidth = 1; // 占据1列
        constraints.fill = GridBagConstraints.NONE; // 不填充
        shopFrame.add(selectButton, constraints);

// 设置其他组件的位置和大小
        constraints.gridx = 0; // 列索引为0
        constraints.gridy = 1; // 行索引为1
        constraints.gridwidth = 3; // 占据3列
        constraints.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        shopFrame.add(nameLabel, constraints);

        constraints.gridx = 0; // 列索引为0
        constraints.gridy = 2; // 行索引为2
        constraints.gridwidth = 3; // 占据3列
        constraints.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        shopFrame.add(priceLabel, constraints);

        constraints.gridx = 0; // 列索引为0
        constraints.gridy = 3; // 行索引为3
        constraints.gridwidth = 1; // 占据1列
        constraints.fill = GridBagConstraints.NONE; // 不填充
        shopFrame.add(buyButton, constraints);

        constraints.gridx = 1; // 列索引为1
        constraints.gridy = 3; // 行索引为3
        constraints.gridwidth = 1; // 占据1列
        constraints.fill = GridBagConstraints.NONE; // 不填充
        shopFrame.add(packbagButton, constraints);
        packbagButton.addActionListener(e -> showPackbagItems());
        selectButton.addActionListener(e -> {
                String username = searchName.getText(); // 获取searchName的文本内容作为username
                searchUsername(resultTextArea, username); // 将resultTextArea和username作为参数传递给searchUsername方法
        });


        // 购买按钮点击事件处理
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
                UserDao userDao = context.getBean(UserDao.class);
                buyItem(userDao);
            }
        });

        shopFrame.setVisible(true);
    }
    private static void searchUsername(JTextArea resultTextArea, String username) {
        // 使用username进行查询操作
        String goldValue = getGoldValueFromDatabase(username);
        List<String> timeInfoList = getTimeInfoFromDatabase(username);
        List<String> informationList = getInformationFromDatabase(username);

        // 创建新窗口
        JFrame resultFrame = new JFrame("查询结果");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 创建文本区域
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.append("Gold: " + goldValue + "\n");
        resultTextArea.append("Time: " + String.join(", ", timeInfoList) + "\n");
        resultTextArea.append("Information: " + String.join(", ", informationList) + "\n");

        // 将文本区域添加到窗口中
        resultFrame.getContentPane().add(new JScrollPane(resultTextArea));

        // 设置窗口大小并显示
        resultFrame.setSize(400, 300);
        resultFrame.setVisible(true);
    }

    private static String getGoldValueFromDatabase(String username) {
        String goldValue = "N/A";
        String query = "SELECT gold FROM user WHERE username = ?";
        Object[] params = {username};

        try {
            goldValue = jdbcTemplate.queryForObject(query, params, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return goldValue;
    }

    private static List<String> getTimeInfoFromDatabase(String username) {
        String sql = "SELECT time FROM information WHERE username = ?";
        List<String> timeInfoList = jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> rs.getString("time"));
        return timeInfoList;
    }

    private static List<String> getInformationFromDatabase(String username) {
        String sql = "SELECT time, information FROM information WHERE username = ?";
        List<String> informationList = jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> {
            String time = rs.getString("time");
            String information = rs.getString("information");
            return "Time: " + time + ", Information: " + information + '\n';
        });
        return informationList;
    }

    private static void showPackbagItems() {
        String username = currentUser.getUsername(); // 替换为实际的用户名

        List<Packbag> packbag = getPackbag(username);

        // 创建新窗口并显示数据
        JFrame frame = new JFrame("Packbag Items");
        JTextArea textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton useButton = new JButton("使用");

        // 设置布局管理器
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(useButton, BorderLayout.SOUTH);
        useButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 调用使用物品的方法
                useItem("沙尘暴同花");
            }
        });


        StringBuilder sb = new StringBuilder();
        for (Packbag item : packbag) {
            sb.append(item).append("\n");
        }
        textArea.setText(sb.toString());

        frame.getContentPane().add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



    private static void buyItem(UserDao userDao) {
        // 执行购买逻辑
        addToPackbag();
        showConfirmationDialog("购买成功！");
    }

    public static List<Packbag> getPackbag(String username) {
        return jdbcTemplate.query("SELECT name, SUM(num) FROM packbag WHERE username = ? GROUP BY name",
                (rs, rowNum) -> {
                    Packbag item = new Packbag();
                    item.setName(rs.getString("name"));
                    item.setSum(rs.getInt("SUM(num)"));
                    return item;
                },
                username);
    }

    private static void addToPackbag() {
        // 将购买的道具添加到 packbag 表中
        String username = currentUser.getUsername();
        String itemName = "沙尘暴同花";
        int quantity = 1;

        jdbcTemplate.update("INSERT INTO packbag (username, name, num) VALUES (?, ?, ?)",
                username, itemName, quantity);
        int goldDelta = -100;
        userDao.updateUserGold(username, goldDelta);
    }

    private static void showConfirmationDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    private static void showRewardPopup(String reward, double probability, int rewardValue) {
        JFrame parentFrame = new JFrame();
        String message = "恭喜您获得奖励：" + reward + "\n概率：" + probability + "\n奖励总价值：" + rewardValue;
        JOptionPane.showMessageDialog(parentFrame, message, "获得奖励", JOptionPane.INFORMATION_MESSAGE);
    }




    private static double[] calculateWeights(double[] probabilities) {
        // 计算概率对应的权重（概率总和为1）
        double[] weights = new double[probabilities.length];
        double sum = 0.0;
        for (double probability : probabilities) {
            sum += probability;
        }
        for (int i = 0; i < probabilities.length; i++) {
            weights[i] = probabilities[i] / sum;
        }
        return weights;
    }

    private static int selectRewardIndex(double[] weights, double randomProbability) {
        // 根据权重随机选择奖励的索引
        double cumulativeWeight = 0.0;
        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (randomProbability <= cumulativeWeight) {
                return i;
            }
        }
        return -1; // 如果出现意外情况，返回-1表示未能选择奖励
    }


    private static double getRandomProbability() {
        // 随机生成概率
        return Math.random();
    }

    // 在 Main 类中声明静态变量
    private static String[] rewards = {
            "巨龙*25", "巨龙*150", "巨龙*200", "巨龙*250",
            "女祭司*5", "女祭司*10", "女祭司*20"
    };
    private static double[] probabilities = {0.7, 0.21899, 0.05, 0.02, 0.01, 0.001, 0.00001};

    // 在 useItem 方法中使用概率选择奖励
    private static void useItem(String itemName) {
        //更新背包
        try {
            deleteName("沙尘暴同花");
            // 继续执行后续方法
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // 停止后续方法的执行
        }

        // 计算总概率
        double totalProbability = 0;
        for (double probability : probabilities) {
            totalProbability += probability;
        }

        // 生成随机概率
        double randomProbability = Math.random() * totalProbability;

        // 根据随机概率选择奖励
        double cumulativeProbability = 0;
        int rewardIndex = -1;
        for (int i = 0; i < rewards.length; i++) {
            cumulativeProbability += probabilities[i];
            if (randomProbability <= cumulativeProbability) {
                rewardIndex = i;
                break;
            }
        }

        // 获取选择的奖励
        String reward = rewards[rewardIndex];

        //更新information
        insertInformation(reward);

        // 随机获得的奖励的总价值
        int rewardValue = calculateRewardValue(reward);

        // 更新用户的金币值
        updateGold(rewardValue);

        // 显示弹窗提示
        showRewardPopup(reward, probabilities[rewardIndex], rewardValue);
    }
    private static void  deleteName(String name) throws Exception {
        String queryCount = "SELECT COUNT(*) FROM packbag WHERE name = ?";
        String queryDelete = "DELETE FROM packbag WHERE name = ? LIMIT 1";
        Object[] params = {name};

        try {
            int rowCount = jdbcTemplate.queryForObject(queryCount, params, Integer.class);
            if (rowCount < 1) {
                String query = "UPDATE user SET gold = gold - ? WHERE username = ?";
                Object[] sql = {100, name};

                try {
                    int rowsAffected = jdbcTemplate.update(query, sql);
                    if (rowsAffected > 0) {
                        System.out.println("Gold decreased successfully.");
                    } else {
                        System.out.println("Failed to decrease gold.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                throw new Exception("No record found with the given name.");
            }

            int rowsAffected = jdbcTemplate.update(queryDelete, params);
            if (rowsAffected > 0) {
                System.out.println("Successfully deleted the record.");
            } else {
                System.out.println("Failed to delete the record.");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void insertInformation(String reward) {
        String username = currentUser.getUsername();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        jdbcTemplate.update("INSERT INTO information (username, time, information) VALUES (?, ?, ?)",
                username, timestamp, reward);
    }

    private static int calculateRewardValue(String reward) {
        if (reward.startsWith("巨龙")) {
            int quantity = Integer.parseInt(reward.substring(reward.indexOf("*") + 1));
            int price = 1; // 假设巨龙的价格为100
            return quantity * price;
        } else if (reward.startsWith("女祭司")) {
            int quantity = Integer.parseInt(reward.substring(reward.indexOf("*") + 1));
            int price = 100; // 假设女祭司的价格为50
            return quantity * price;
        }
        return 0;
    }

    private static void updateGold(int rewardValue) {
        String username = currentUser.getUsername();

        // 查询当前用户的金币值
        int currentGold = jdbcTemplate.queryForObject("SELECT gold FROM user WHERE username = ?",
                Integer.class, username);

        // 计算新的金币值
        int newGold = currentGold + rewardValue;

        // 更新用户的金币值
        jdbcTemplate.update("UPDATE user SET gold = ? WHERE username = ?", newGold, username);
    }


    private static double selectByUserName(double[] probabilities) {
        Random random = new Random();
        int index = random.nextInt(probabilities.length);
        return probabilities[index];
    }




}
