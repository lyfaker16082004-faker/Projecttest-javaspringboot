package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.repository.BuildingRepository;

@Repository // Đánh dấu đây là class làm việc với Database
public class BuildingRepositoryImpl implements BuildingRepository {

	static final String url = "jdbc:mysql://localhost:3306/estatebasic";
	static final String username = "root";
	static final String password = "123456";
	
	@Override
	public List<BuildingEntity> findAll() {
		String sql = "select * from building";
		List<BuildingEntity> resuList = new ArrayList<>();
		try(Connection connection = DriverManager.getConnection(url,username,password);
				Statement statement = connection.createStatement();
				ResultSet rSet = statement.executeQuery(sql);
				){
			while(rSet.next()) {
				BuildingEntity building = new BuildingEntity();
				building.setName(rSet.getString("name"));
				building.setStreet(rSet.getString("street"));
				building.setWard(rSet.getString("ward"));
				building.setNumber(rSet.getInt("numberofbasement"));
				resuList.add(building);
			}
			 
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("connected database failed...");
		}
		return resuList;
	}

	

}
