package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.entity.DistrictEntity;
import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {
	// Tiêm Repository vào Service
	@Autowired
    private BuildingRepository buildingRepository;
    
    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<BuildingDTO> findAll(Map<String, Object> params, List<String> typeCode) {
        List<BuildingEntity> buildingEntities = buildingRepository.findAll(params, typeCode);
        List<BuildingDTO> result = new ArrayList<>();
        
        for (BuildingEntity item : buildingEntities) {
            BuildingDTO building = new BuildingDTO();
            building.setName(item.getName());
            
            // Tìm tên quận từ DistrictRepository dựa vào districtid
            DistrictEntity districtEntity = districtRepository.findNameById(item.getDistrictid());
            
            // Ghép chuỗi địa chỉ: street + ward + districtName
            building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());
            
            result.add(building);
        }
        return result;
    }

}
