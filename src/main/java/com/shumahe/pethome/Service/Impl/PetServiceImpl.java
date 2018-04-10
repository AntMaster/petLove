package com.shumahe.pethome.Service.Impl;


import com.shumahe.pethome.Controller.AdminController;
import com.shumahe.pethome.DTO.UserPetAlbumDTO;
import com.shumahe.pethome.DTO.UserPetDTO;
import com.shumahe.pethome.DTO.UserPetPhotoDTO;
import com.shumahe.pethome.Domain.*;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Enums.ShowStateEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Form.UserPetAlbumForm;
import com.shumahe.pethome.Form.UserPetForm;
import com.shumahe.pethome.Form.UserPetPhotoForm;
import com.shumahe.pethome.Repository.PetPublishRepository;
import com.shumahe.pethome.Repository.UserPetAlbumRepository;
import com.shumahe.pethome.Repository.UserPetPhotoRepository;
import com.shumahe.pethome.Repository.UserPetRepository;
import com.shumahe.pethome.Service.PetService;
import com.shumahe.pethome.VO.ResultVO;
import javafx.util.converter.DateStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PetServiceImpl implements PetService {


    @Autowired
    UserPetRepository userPetRepository;

    @Autowired
    UserPetAlbumRepository userPetAlbumRepository;


    @Autowired
    UserPetPhotoRepository userPetPhotoRepository;

    @Autowired
    PetPublishRepository petPublishRepository;

    /**
     * 新增   宠卡
     *
     * @param petForm
     * @return
     */
    @Override
    public UserPet petAdd(UserPetForm petForm) {

        UserPet userPet = new UserPet();
        BeanUtils.copyProperties(petForm, userPet);
        userPet.setBirthday(new DateStringConverter().fromString(petForm.getBirthday()));
        UserPet save = userPetRepository.save(userPet);
        return save;
    }

    /**
     * 新增   宠卡  相册
     *
     * @param albumForm
     * @return
     */
    @Override
    public UserPetAlbum albumAdd(UserPetAlbumForm albumForm) {

        UserPet pet = userPetRepository.findOne(albumForm.getPetId());
        if (pet == null) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "宠物不存在");
        }


        UserPetAlbum petAlbum = new UserPetAlbum();
        BeanUtils.copyProperties(albumForm, petAlbum);

        UserPetAlbum save = userPetAlbumRepository.save(petAlbum);
        return save;

    }

    /**
     * 新增   宠卡  相册  相片
     *
     * @param photoForm
     * @return
     */
    @Override
    public UserPetPhoto photoAdd(UserPetPhotoForm photoForm) {


        UserPetAlbum album = userPetAlbumRepository.findOne(photoForm.getAlbumId());
        if (album == null) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "宠物相册不存在");
        }


        UserPetPhoto petPhoto = new UserPetPhoto();
        BeanUtils.copyProperties(photoForm, petPhoto);
        petPhoto.setPetId(album.getPetId());

        UserPetPhoto save = userPetPhotoRepository.save(petPhoto);
        return save;
    }

    /**
     * 宠卡 列表
     *
     * @param openId
     * @return
     */
    @Autowired
    private AdminController adminController;

    @Override
    public List<UserPetDTO> petList(String openId) {

        List<UserPet> pets = userPetRepository.findByUserIdOrderByCreateTime(openId);
        if (pets.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }


        Map<Integer, PetVariety> varietyMap = adminController.petVariety();

        List<UserPetDTO> collect = pets.stream().map(e -> {
            UserPetDTO userPetDTO = new UserPetDTO();
            BeanUtils.copyProperties(e, userPetDTO);
            if (e.getVarietyId() != null)
                userPetDTO.setVarietyName(varietyMap.get(e.getVarietyId()).getName());
            return userPetDTO;
        }).collect(Collectors.toList());

        return collect;
    }


    /**
     * 宠物相册 列表
     *
     * @param petId
     * @return
     */
    @Override
    public UserPetDTO albumList(Integer petId) {

        UserPet pet = userPetRepository.findOne(petId);
        if (pet == null) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "宠物不存在");
        }


        List<UserPetAlbum> albums = userPetAlbumRepository.findByPetIdAndShowOrderByCreateTime(petId, ShowStateEnum.SHOW.getCode());
        if (albums == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        //相册数量
        int[] albumIds = albums.stream().mapToInt(e -> e.getId()).toArray();
        List<UserPetPhoto> photos = userPetPhotoRepository.findByAlbumIdInAndShowOrderByPetId(albumIds, ShowStateEnum.SHOW.getCode());
        Map<Integer, List<UserPetPhoto>> albumGroup = photos.stream().collect(Collectors.groupingBy(photo -> photo.getAlbumId()));//根据相册ID对照片分组


        UserPetDTO petDTO = new UserPetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setAlbumCount(albums.size());

        Map<Integer, PetVariety> varietyMap = adminController.petVariety();
        if (pet.getVarietyId() != null)
            petDTO.setVarietyName(varietyMap.get(pet.getVarietyId()).getName());


        List<UserPetAlbumDTO> albumDTOS = albums.stream().map(e -> {

            UserPetAlbumDTO albumDTO = new UserPetAlbumDTO();
            BeanUtils.copyProperties(e, albumDTO);
            if (albumGroup.get(e.getId()) != null)
                albumDTO.setPhotoCount(albumGroup.get(e.getId()).size());//当前相册照片数量

            return albumDTO;
        }).collect(Collectors.toList());

        petDTO.setPetAlbumDTOS(albumDTOS);

        return petDTO;
    }

    /**
     * 宠物相册相片 列表
     *
     * @param albumId
     * @return
     */
    @Override
    public UserPetAlbumDTO photoList(Integer albumId) {

        UserPetAlbum album = userPetAlbumRepository.findOne(albumId);
        if (album == null) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "相册不存在");
        }

        List<UserPetPhoto> photos = userPetPhotoRepository.findByAlbumIdAndShowOrderById(albumId, ShowStateEnum.SHOW.getCode());

        if (photos == null) {
            throw new PetHomeException(ResultEnum.FAILURE.getCode(), "该相册中不存在相片");
        }

        List<UserPetPhotoDTO> collect = photos.stream().map(e -> {

            UserPetPhotoDTO photoDTO = new UserPetPhotoDTO();
            BeanUtils.copyProperties(e, photoDTO);
            return photoDTO;

        }).collect(Collectors.toList());

        UserPetAlbumDTO albumDTO = new UserPetAlbumDTO();
        BeanUtils.copyProperties(album, albumDTO);
        albumDTO.setPhotoCount(photos.size());
        albumDTO.setPetPhotoDTOS(collect);
        return albumDTO;
    }

    /**
     * 删除相册
     *
     * @param albumId
     * @return
     */
    @Override
    @Transactional
    public boolean albumDelete(List<Integer> albumId) {


        //UserPetAlbum album = userPetAlbumRepository.findOne(albumId);

        List<UserPetAlbum> albums = userPetAlbumRepository.findByIdIn(albumId);

        if (albums == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }
        albums.forEach(e -> e.setShow(ShowStateEnum.HIDE.getCode()));

        userPetAlbumRepository.save(albums);

        List<UserPetPhoto> photos = userPetPhotoRepository.findByAlbumIdIn(albumId);
        if (photos == null)
            return true;

        photos.stream().forEach(e -> e.setShow(ShowStateEnum.HIDE.getCode()));

        userPetPhotoRepository.save(photos);

        return true;
    }

    /**
     * 删除相片
     *
     * @param photoIds
     * @return
     */
    @Override
    public boolean photoDelete(List<Integer> photoIds) {

        List<UserPetPhoto> photos = userPetPhotoRepository.findByIdIn(photoIds);
        if (photos == null) {
            return true;
        }

        photos.forEach(e -> e.setShow(ShowStateEnum.HIDE.getCode()));

        userPetPhotoRepository.save(photos);

        return true;
    }

    @Override
    public boolean albumCover(Integer albumId, Integer photoId) {

        UserPetPhoto photo = userPetPhotoRepository.findOne(photoId);
        if (photo == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        UserPetAlbum album = userPetAlbumRepository.findOne(albumId);
        if (album == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        album.setCoverPath(photo.getPath());
        userPetAlbumRepository.save(album);
        return true;
    }
}
