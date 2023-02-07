package com.wowmate.server.user.repository;

import com.wowmate.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // findBy??? => ??? 는 엔티티의 속성명을 첫글자 대문자로하여 입력 = 해당 속성값으로 탐색하여 DB 에서 엔티티 반환
    // JpaRepository 의 편의 기능
    Optional<User> findByEmail(@Param("email") String email);

}
