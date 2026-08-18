package com.unifiedhome.api.repository;

import com.unifiedhome.api.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
}