package com.itheima.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return jdbcTemplate.queryForObject("SELECT * FROM user WHERE username = ? AND password = ?",
                (rs, rowNum) -> new User(rs.getString("username"), rs.getString("password")),
                username, password);
    }

    @Override
    public void createUser(User user) {
        jdbcTemplate.update("INSERT INTO user (username, password) VALUES (?, ?)",
                user.getUsername(), user.getPassword());
    }


    @Override
    public void updateUserGold(String username, int gold) {
        jdbcTemplate.update("UPDATE user SET gold = gold - ? WHERE username = ?", 100, username);
    }
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }
    public List<String> getPackbagItems(String username) {
        return jdbcTemplate.queryForList("SELECT name, SUM(num) FROM packbag WHERE username = ? GROUP BY name", String.class, username);
    }


}
