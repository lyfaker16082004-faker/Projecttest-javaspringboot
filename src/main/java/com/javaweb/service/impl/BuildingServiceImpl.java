package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {
	// Tiêm Repository vào Service
    @Autowired
    private BuildingRepository buildingRepository;

	@Override
	public List<BuildingDTO> findAll() {
		// TODO Auto-generated method stub
		List<BuildingEntity> buildingEntities = buildingRepository.findAll();
		
		List<BuildingDTO> resultBuildingDTOs = new ArrayList<BuildingDTO>();
		for(BuildingEntity item : buildingEntities) {
			BuildingDTO buildingDTO = new BuildingDTO();
			buildingDTO.setName(item.getName());
			buildingDTO.setAddress(item.getStreet() + "," + item.getWard());
			buildingDTO.setNumber(item.getNumber());
			resultBuildingDTOs.add(buildingDTO);
		}
		return resultBuildingDTOs;
	}


}
