package com.imsapp.ims.repository.resources;

import com.imsapp.ims.entity.resources.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resources, Long> {
}

