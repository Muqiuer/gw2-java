package com.itheima.Service;

import com.itheima.Dao.Information;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class InformationService {
    private JdbcTemplate jdbcTemplate;

    // 构造函数或依赖注入方法省略

    public List<Information> getUserInformationByUsername(String username) {
        String informationSql = "SELECT i.time, i.information " +
                "FROM information i " +
                "WHERE i.username = ?";
        List<Information> informationList = jdbcTemplate.query(informationSql, new Object[]{username}, (resultSet, rowNum) -> {
            java.util.Date utilDate = resultSet.getObject("time", java.util.Date.class);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            String information = resultSet.getString("information");
            return new Information(sqlDate, information);
        });

        return informationList;
    }



    public int getUserGoldByUsername(String username) {
        String goldSql = "SELECT gold FROM user WHERE username = ?";
        Integer gold = jdbcTemplate.queryForObject(goldSql, Integer.class, username);
        return gold != null ? gold : 0;
    }
}

