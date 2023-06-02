package com.itheima.Dao;

import java.util.List;

public interface UserDao {
    User findByUsernameAndPassword(String username, String password);
    void createUser(User user);
    void updateUserGold(String username, int gold);

    User findByUsername(String username);

    public List<String> getPackbagItems(String username);
}
