package com.itheima.Service;

import com.itheima.Dao.InformationDao;
import com.itheima.Dao.UserDao;
import com.itheima.Dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PurchaseService {
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;
    private InformationDao informationDao;

    @Autowired
    public PurchaseService(UserDao userDao, JdbcTemplate jdbcTemplate, InformationDao informationDao) {
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
        this.informationDao = informationDao;
    }

    public void purchaseItem(String username) {
        // 更新用户金币数量
        int goldDelta = -100; // 购买道具所需金币数量
        userDao.updateUserGold(username, goldDelta);

        // 将购买记录插入packbag表中
        jdbcTemplate.update("INSERT INTO packbag (username, name, num) VALUES (?, ?, 1)",
                username, "沙尘暴同花");

        // 随机产出道具
        String itemName = getRandomItemNameAndValue();
        int itemValue = getItemValue(itemName);

        // 更新用户金币数量
        User user = userDao.findByUsername(username);
        int currentGold = Integer.parseInt(user.getGold());
        int newGold = currentGold + itemValue;
        userDao.updateUserGold(username, newGold);

        // 保存开启道具信息
        String information = String.format("开启时间：%s，获得道具：%s", LocalDateTime.now(), itemName);
        informationDao.insertInformation(username, information);
    }

    private String getRandomItemNameAndValue() {
        Random random = new Random();
        double randomNumber = random.nextDouble(); // 生成0到1之间的随机数

        if (randomNumber < 0.7) {
            return "巨龙*25";
        } else if (randomNumber < 0.91899) {
            return "巨龙*150";
        } else if (randomNumber < 0.96899) {
            return "巨龙*200";
        } else if (randomNumber < 0.98899) {
            return "巨龙*250";
        } else if (randomNumber < 0.99899) {
            return "女祭司*5";
        } else if (randomNumber < 0.99999) {
            return "女祭司*10";
        } else {
            return "女祭司*20";
        }
    }

    private int getItemValue(String itemName) {
        if (itemName.startsWith("巨龙")) {
            return Integer.parseInt(itemName.substring(itemName.indexOf('*') + 1));
        } else {
            return Integer.parseInt(itemName.substring(itemName.indexOf('*') + 1)) * 100;
        }
    }
}
