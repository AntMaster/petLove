package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.UserPetAlbum;
import com.shumahe.pethome.Domain.UserPetPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPetPhotoRepository  extends JpaRepository<UserPetPhoto,Integer>{


    List<UserPetPhoto>  findByIdIn(List<Integer> photoIds);

    List<UserPetPhoto> findByAlbumIdAndShowOrderById(Integer albumId,Integer show);

    List<UserPetPhoto> findByAlbumIdInAndShowOrderByPetId(int[] albumId,Integer show);

    List<UserPetPhoto> findByAlbumId(Integer albumId);

    List<UserPetPhoto> findByAlbumIdIn(List<Integer> albumId);
}
