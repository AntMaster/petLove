package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.UserPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserPetRepository extends JpaRepository<UserPet,Integer> {

        List<UserPet> findByUserIdOrderByCreateTime(String openId);



}
