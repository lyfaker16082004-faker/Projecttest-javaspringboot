package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.entity.DistrictEntity;
import com.javaweb.entity.RentAreaEntity;
import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;

@Component
public class BuildingDTOConverter {
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private RentAreaRepository rentAreaRepository;
	@Autowired
	private ModelMapper modelMapper;
	public BuildingDTO toBuildingDTO(BuildingEntity item) {
		  BuildingDTO building = modelMapper.map(item, BuildingDTO.class); // phải trùng tên bên class
           // building.setName(item.getName()); dùng mapper ở trên thay cho set bằng tay khi số lượng lớn
          
          // Tìm tên quận từ DistrictRepository dựa vào districtid
          DistrictEntity districtEntity = districtRepository.findNameById(item.getDistrictid());
          
          // Ghép chuỗi địa chỉ: street + ward + districtName
          building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());
          List<RentAreaEntity> rentAreas = rentAreaRepository.getValueByBuildingId(item.getId());
          String listRentAreas = rentAreas.stream().map(it -> it.getValue().toString()).collect(Collectors.joining(","));
          building.setRentArea(listRentAreas);
          return building;
	}

}
