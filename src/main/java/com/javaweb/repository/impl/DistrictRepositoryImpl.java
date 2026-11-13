package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.javaweb.entity.DistrictEntity;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.utils.ConnectionJDBCUtils;
@Repository
public class DistrictRepositoryImpl implements DistrictRepository {

	
	@Override
	public DistrictEntity findNameById(Long id) {
		String sql = "SELECT d.name FROM district d WHERE d.id = " + id + ";";
        DistrictEntity districtEntity = new DistrictEntity();
        try (Connection conn = ConnectionJDBCUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                districtEntity.setName(rs.getString("name"));
            }
            return districtEntity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

}
