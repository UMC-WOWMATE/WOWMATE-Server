package com.wowmate.server.user.repository;

import com.wowmate.server.user.domain.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
