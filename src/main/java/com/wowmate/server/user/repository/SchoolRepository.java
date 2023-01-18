package com.wowmate.server.user.repository;

import com.wowmate.server.user.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
