package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {
	
	@Autowired // cho phép spring tìm kiếm mấy cái này
	private BuildingService buildingService;
	
	@GetMapping(value = "/api/building/")
    public List<BuildingDTO> getBuilding(@RequestParam Map<String, Object> params,
                                         @RequestParam(name = "typeCode", required = false) List<String> typeCode) {
        List<BuildingDTO> result = buildingService.findAll(params, typeCode);
        return result;
    }

    @DeleteMapping(value = "/api/building/{id}/{name}/")
    public void deleteBuilding(@PathVariable Integer id,
                               @PathVariable String name,
                               @RequestParam(value = "ward", required = false) String ward) {
        System.out.print("Da xoa toa nha co id la " + id + " roi nhe!");
    }
	

}
