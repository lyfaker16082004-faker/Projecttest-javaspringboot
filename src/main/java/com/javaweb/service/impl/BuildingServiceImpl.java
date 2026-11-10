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
        List<BuildingDTO> result = new ArrayList<BuildingDTO>();

        for (BuildingEntity item : buildingEntities) {
            BuildingDTO building = new BuildingDTO();
            building.setName(item.getName());
            
            // Map các trường còn lại từ Entity sang DTO
            building.setManagerName(item.getManagerName());
            building.setManagerPhoneNumber(item.getManagerPhoneNumber());
            building.setFloorArea(item.getFloorArea());
            building.setRentPrice(item.getRentPrice());
            building.setServiceFee(item.getServiceFee());
            building.setBrokerageFee(item.getBrokerageFee());

            // Tìm DistrictEntity qua id để lấy tên quận ghép vào địa chỉ
            DistrictEntity districtEntity = districtRepository.findNameById(item.getDistrictid());
            String districtName = (districtEntity != null && districtEntity.getName() != null) ? districtEntity.getName() : "";

            building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtName);
            result.add(building);
        }

        return result;

	}
}
