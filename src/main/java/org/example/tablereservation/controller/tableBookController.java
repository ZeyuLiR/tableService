package org.example.tablereservation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
public class tableBookController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/api/tables/booked")
    public ResponseEntity<?> getBookedTables(@RequestHeader(value = "userId", required = false) String userId) {
        String sql = "SELECT * FROM tableReservation WHERE userId = ?";
        List<Map<String, Object>> reservations = jdbcTemplate.queryForList(sql, userId);
        String jsonStr = JSON.toJSONString(reservations);
        return ResponseEntity.ok(jsonStr);
    }

    @PostMapping("/api/tables/book")
    public ResponseEntity<?> bookTours(@RequestBody Map<String, Object> jsonObj, @RequestHeader(value = "userId", required = false) String userId) {
        System.out.println(jsonObj.get("guestNumber"));
        int guestNumber = Integer.parseInt((String) jsonObj.get("guestNumber"));
        String customerName = (String) jsonObj.get("customerName");
        String contactNumber = (String) jsonObj.get("contactNumber");
        String reservationData = (String) jsonObj.get("reservationData");
        String specialRequest = (String) jsonObj.get("specialRequest");
        System.out.println(jsonObj.get("reservationDate"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime reservationDate = LocalDateTime.parse((String)jsonObj.get("reservationDate"), formatter);
        String insertSql = "INSERT INTO tableReservation (userId, guestNumber,customerName,contactNumber,reservationData,specialRequest" +
                ",reservationDate) VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(insertSql, userId, guestNumber,customerName,contactNumber,reservationData,specialRequest,reservationDate);
        JSONObject obj = new JSONObject();
        obj.put("message", "success");
        return ResponseEntity.ok(obj);
    }
}
