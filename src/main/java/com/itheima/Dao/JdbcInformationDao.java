package com.itheima.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class JdbcInformationDao implements InformationDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcInformationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertInformation(String username, String information) {
        LocalDateTime currentTime = LocalDateTime.now();
        jdbcTemplate.update("INSERT INTO information (username, time, information) VALUES (?, ?, ?)",
                username, currentTime, information);
    }

}


