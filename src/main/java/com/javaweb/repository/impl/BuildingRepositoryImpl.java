package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.entity.BuildingEntity;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.utils.ConnectionJDBCUtils;
import com.javaweb.utils.NumberUntil;
import com.javaweb.utils.StringUtil;

@Repository // Đánh dấu đây là class làm việc với Database
public class BuildingRepositoryImpl implements BuildingRepository {



    public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
        String staffId = buildingSearchBuilder.getStaffId().toString();
        if (StringUtil.checkString(staffId)) {
            sql.append(" INNER JOIN assignmentbuilding ON b.id = assignmentbuilding.buildingid ");
        }
        List<String> typeCode = buildingSearchBuilder.getTypeCode();
        if (typeCode != null && typeCode.size() != 0) {
            sql.append(" INNER JOIN buildingrenttype ON b.id = buildingrenttype.buildingid ");
            sql.append(" INNER JOIN renttype ON renttype.id = buildingrenttype.renttypeid ");
        }
    }
     

    public static void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
//        for (Map.Entry<String, Object> it : params.entrySet()) {
//            if (!it.getKey().equals("staffId") && !it.getKey().equals("typeCode") &&
//                !it.getKey().startsWith("area") && !it.getKey().startsWith("rentPrice")) {
//                
//                String value = it.getValue().toString();
//                if (StringUtil.checkString(value)) {
//                    if (NumberUntil.isNumber(value)) {
//                        where.append(" AND b." + it.getKey() + " = " + value);
//                    } else {
//                        where.append(" AND b." + it.getKey() + " LIKE '%" + value + "%' ");
//                    }
//                }
//            }
//        }
    	try {
            Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
            for (Field item : fields) {
                item.setAccessible(true); // Cho phép truy cập các biến private
                String fieldName = item.getName(); // Lấy tên biến (vd: name, street, numberOfBasement...)
                
                // Bỏ qua các trường đặc biệt không nằm trực tiếp cột của bảng building hoặc đã xử lý ở querySpecial
                if (fieldName.equals("staffId") || fieldName.equals("typeCode") ||
                    fieldName.startsWith("area") || fieldName.startsWith("rentPrice")) {
                    continue;
                }
                
                Object value = item.get(buildingSearchBuilder); // Lấy giá trị của trường từ object buildingSearchBuilder
                if (value != null) {
                    if (item.getType().getName().equals("java.lang.Long") || 
                        item.getType().getName().equals("java.lang.Integer")) {
                        where.append(" AND b.").append(fieldName).append(" = ").append(value);
                    } else if (item.getType().getName().equals("java.lang.String")) {
                        String valueStr = (String) value;
                        if (StringUtil.checkString(valueStr)) {
                            where.append(" AND b.").append(fieldName).append(" LIKE '%").append(valueStr).append("%' ");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }

    public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
    	Long staffId = buildingSearchBuilder.getStaffId();
        if (staffId != null) {
            where.append(" AND assignmentbuilding.staffid = ").append(staffId);
        }
        
        Long rentAreaTo = buildingSearchBuilder.getAreaTo();
        Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
        if (rentAreaFrom != null || rentAreaTo != null) {
            if (rentAreaFrom != null) {
                where.append(" AND rentarea.value >= ").append(rentAreaFrom);
            }
            if (rentAreaTo != null) {
                where.append(" AND rentarea.value <= ").append(rentAreaTo);
            }
        }
        
        Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
        Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
        if (rentPriceFrom != null || rentPriceTo != null) {
            if (rentPriceFrom != null) {
                where.append(" AND b.rentprice >= ").append(rentPriceFrom);
            }
            if (rentPriceTo != null) {
                where.append(" AND b.rentprice <= ").append(rentPriceTo);
            }
        }

        // Xử lý typeCode bằng Java 8 Stream
        List<String> typeCode = buildingSearchBuilder.getTypeCode();
        if (typeCode != null && !typeCode.isEmpty()) {
            List<String> code = typeCode.stream().map(it -> "'" + it + "'").collect(Collectors.toList());
            where.append(" AND renttype.code IN(").append(String.join(",", code)).append(") ");
        }
    
//        // java 7
//        if (typeCode != null && typeCode.size() != 0) {
//        	List<String> code = new ArrayList<String>();
//        	for(String item: typeCode) {
//        		code.add("'" + item + "'");
//        	}
//            where.append(" AND renttype.code IN(" + String.join(",", typeCode) + ") ");
//        }
        // java 8
		/*
		 * if(typeCode != null && typeCode.size() != 0) { where.append(" AND("); String
		 * sqlString = typeCode.stream().map(it -> "renttype.code Like" + "'%" + it +
		 * "%'").collect(Collectors.joining(" or ")); where.append(" ) "); }
		 */
        
    }

    @Override
    public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {
        StringBuilder sql = new StringBuilder("SELECT b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee FROM building b ");
        joinTable(buildingSearchBuilder, sql);
        
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        queryNormal(buildingSearchBuilder, where);
        querySpecial(buildingSearchBuilder, where);
        
        where.append(" GROUP BY b.id;");
        sql.append(where);

        List<BuildingEntity> result = new ArrayList<>();
        try (Connection conn = ConnectionJDBCUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {
             
            while (rs.next()) {
                BuildingEntity buildingEntity = new BuildingEntity();
                buildingEntity.setId(rs.getLong("b.id"));
                buildingEntity.setName(rs.getString("b.name"));
                buildingEntity.setWard(rs.getString("b.ward"));
                buildingEntity.setDistrictid(rs.getLong("b.districtid"));
                buildingEntity.setStreet(rs.getString("b.street"));
                // TODO: set các field còn lại
                
                result.add(buildingEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
