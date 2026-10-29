package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {
	
	@Autowired // cho phép spring tìm kiếm mấy cái này
	private BuildingService buildingService;
	
	@GetMapping(value = "api/building/")
	public List<BuildingDTO> getBuilding(){
		List<BuildingDTO> resultBuildingDTOs = buildingService.findAll();
		return  resultBuildingDTOs;
	}
	

}
