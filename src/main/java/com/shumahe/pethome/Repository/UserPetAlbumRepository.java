package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.UserPetAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserPetAlbumRepository extends JpaRepository<UserPetAlbum,Integer> {

    List<UserPetAlbum> findByPetIdAndShowOrderByCreateTime(Integer petId, Integer show);


    List<UserPetAlbum> findByIdIn(List<Integer> ids);

}

