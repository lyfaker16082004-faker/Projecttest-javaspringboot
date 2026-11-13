package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.entity.RentAreaEntity;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.utils.ConnectionJDBCUtils;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository {

	@Override
	public List<RentAreaEntity> getValueByBuildingId(Long id) {
		String sql = "select * from rentarea where rentarea.buildingid =" + id;

		List<RentAreaEntity> rentAreas = new ArrayList<>();
		try (Connection connection = ConnectionJDBCUtils.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rSet = statement.executeQuery(sql)) {
			while(rSet.next()) {
				RentAreaEntity areaEntity = new RentAreaEntity();
				areaEntity.setValue(rSet.getString("value"));
				rentAreas.add(areaEntity);
			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rentAreas;
	}

}
