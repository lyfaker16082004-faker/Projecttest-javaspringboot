package com.javaweb.repository;

import com.javaweb.entity.DistrictEntity;

public interface DistrictRepository {
	DistrictEntity findNameById(Long id);

}
