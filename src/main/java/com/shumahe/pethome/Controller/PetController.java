package com.shumahe.pethome.Controller;


import com.shumahe.pethome.DTO.UserPetAlbumDTO;
import com.shumahe.pethome.DTO.UserPetDTO;
import com.shumahe.pethome.Domain.PetVariety;
import com.shumahe.pethome.Domain.UserPet;
import com.shumahe.pethome.Domain.UserPetAlbum;
import com.shumahe.pethome.Domain.UserPetPhoto;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Form.UserPetAlbumForm;
import com.shumahe.pethome.Form.UserPetForm;
import com.shumahe.pethome.Form.UserPetPhotoForm;
import com.shumahe.pethome.Repository.PetVarietyRepository;
import com.shumahe.pethome.Repository.UserPetAlbumRepository;
import com.shumahe.pethome.Repository.UserPetPhotoRepository;
import com.shumahe.pethome.Repository.UserPetRepository;
import com.shumahe.pethome.Service.PetService;
import com.shumahe.pethome.Service.UserService;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.*;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/pet")
public class PetController {



    @Autowired
    private PetService petService;


    @Autowired
    private UserPetRepository userPetRepository;


    @Autowired
    private UserPetAlbumRepository userPetAlbumRepository;


    @Autowired
    private UserPetPhotoRepository userPetPhotoRepository;


    @Autowired
    private PetVarietyRepository petVarietyRepository;


    @Autowired
    private UserService userService;

    /**
     * 新增宠卡1 2
     *
     * @param petForm
     * @param bindingResult
     * @return
     */
    @PutMapping("/{openId}")
    public ResultVO petAdd(@PathVariable("openId") String openId, @Valid UserPetForm petForm, BindingResult bindingResult) {

        //验证表单数据是否正确
        if (bindingResult.hasErrors()) {
            log.info("【添加宠卡】参数不正确,petForm={}", petForm);
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        petForm.setUserId(openId);
        UserPet pet = petService.petAdd(petForm);
        return ResultVOUtil.success(pet);
    }

    /**
     * 宠物修改
     *
     * @param openId
     * @param
     * @param
     * @return
     */
    @PostMapping("/{openId}")
    public ResultVO petModify(@PathVariable("openId") String openId, @RequestBody UserPet userPet) {

        UserPet pet = userPetRepository.findOne(userPet.getId());
        if (pet == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }
        UserPet save = userPetRepository.save(userPet);
        return ResultVOUtil.success(save);
    }

    /**
     * 新增相册
     *
     * @param albumForm
     * @param bindingResult
     * @return
     */
    @PutMapping("/album/{openId}")
    public ResultVO albumAdd(@Valid UserPetAlbumForm albumForm, BindingResult bindingResult) {

        //验证表单数据是否正确
        if (bindingResult.hasErrors()) {
            log.error("【添加宠卡相册】参数不正确,albumForm={}", albumForm);
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }


        UserPetAlbum album = petService.albumAdd(albumForm);
        return ResultVOUtil.success(album);
    }

    /**
     * 修改相册
     */
    @PostMapping("/album/{albumId}")
    public ResultVO albumModify(@PathVariable("albumId") Integer albumId,
                                @RequestParam(value = "name", defaultValue = "EMPTY") String name) {


        if (name.equals("EMPTY")) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "相册名称不能为空");
        }

        UserPetAlbum album = userPetAlbumRepository.findOne(albumId);
        if (album == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        if (!name.equals("EMPTY"))
            album.setName(name);

        UserPetAlbum save = userPetAlbumRepository.save(album);
        return ResultVOUtil.success(save);
    }


    /**
     * 设为封面
     */
    @PostMapping(value = "/album/{albumId}/{photoId}")
    public ResultVO albumCover(@PathVariable("albumId") Integer albumId, @PathVariable("photoId") Integer photoId) {

        if (photoId == null || albumId == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }
        boolean res = petService.albumCover(albumId, photoId);
        return ResultVOUtil.success(res);
    }

    /**
     * 删除相册
     */
    @DeleteMapping("/album/{openId}")
    public ResultVO albumDelete(@RequestBody List<Integer> albumId) {

        boolean delete = petService.albumDelete(albumId);
        return ResultVOUtil.success(delete);
    }

    /**
     * 新增相片
     *
     * @param photoForm
     * @param bindingResult
     * @return
     */
    @PutMapping("/photo/{openId}")
    public ResultVO photoAdd(@Valid UserPetPhotoForm photoForm, BindingResult bindingResult) {

        //验证表单数据是否正确
        if (bindingResult.hasErrors()) {
            log.error("【添加照片】参数不正确,petForm={}", photoForm);
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }


        UserPetPhoto photo = petService.photoAdd(photoForm);
        return ResultVOUtil.success(photo);
    }

    /**
     * 修改相片
     */
    @PostMapping("/photo/{albumId}")
    public ResultVO photoModify(@RequestParam("albumId") Integer albumId,
                                @RequestParam(value = "name", defaultValue = "EMPTY") String name) {

        if (name.equals("EMPTY")) {

            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "相册名称不能为空");
        }

        UserPetPhoto photo = userPetPhotoRepository.findOne(albumId);
        if (photo == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        if (!name.equals("EMPTY"))
            photo.setName(name);

        UserPetPhoto save = userPetPhotoRepository.save(photo);
        return ResultVOUtil.success(save);
    }

    /**
     * 删除相片
     */
    @DeleteMapping(value = "/photo/{openId}")
    public ResultVO photoDelete(@RequestBody List<Integer> photoIds) {

        if (photoIds.isEmpty()) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }
        boolean delete = petService.photoDelete(photoIds);
        return ResultVOUtil.success(delete);
    }


    /**
     * 宠卡 列表
     *
     * @param openId
     * @return
     */
    @GetMapping("/list/{openId}")
    public ResultVO petFindList(@PathVariable("openId") String openId) {

        if (StringUtils.isEmpty(openId) || openId.equals("null")) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }
        List<UserPetDTO> userPets = petService.petList(openId);

        Map<String,Object> res = new HashMap<>();
        res.put("list",userPets);
        res.put("showMsg",userService.isShowMsgPoint(openId));

        return ResultVOUtil.success(res);
    }

    /**
     * 一个宠物
     *
     * @param openId
     * @return
     */
    /**
     *
     * @param openId
     * @param petId
     * @exception
     * @return
     *
     */
    @GetMapping("/one/{openId}")
    public ResultVO petFindOne(@PathVariable("openId") String openId, @RequestParam("petId") Integer petId) {

        if (StringUtils.isEmpty(openId) || openId.equals("null") || petId == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }

        UserPet pet = userPetRepository.findOne(petId);
        return ResultVOUtil.success(pet);
    }

    /**
     * 宠卡相册 列表
     *
     * @param petId
     * @return
     */
    @GetMapping("/album/{openId}")
    public ResultVO albumList(@RequestParam("petId") Integer petId) {

        UserPetDTO userPets = petService.albumList(petId);
        return ResultVOUtil.success(userPets);
    }

    /**
     * 宠卡相册相片 列表
     *
     * @param albumId
     * @return
     */
    @GetMapping("/photo/{openId}")
    public ResultVO photoList(@RequestParam("albumId") Integer albumId) {

        UserPetAlbumDTO photoDTO = petService.photoList(albumId);
        return ResultVOUtil.success(photoDTO);
    }

    /**
     * 获取宠物品种
     *
     * @return
     */
    @GetMapping("/variety")
    public ResultVO petVariety() {

        List<PetVariety> petVarieties = petVarietyRepository.findAll();
        if (petVarieties.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        Map<Integer, List<PetVariety>> varietyMap = petVarieties.stream().collect(Collectors.groupingBy(variety -> variety.getClassifyId()));
        return ResultVOUtil.success(varietyMap);

    }

}
