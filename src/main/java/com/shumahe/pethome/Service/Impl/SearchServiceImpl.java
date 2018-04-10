package com.shumahe.pethome.Service.Impl;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.PetVariety;
import com.shumahe.pethome.Domain.UserBasic;
import com.shumahe.pethome.Enums.*;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Form.PetSearchForm;
import com.shumahe.pethome.Repository.PetPublishRepository;
import com.shumahe.pethome.Repository.PetVarietyRepository;
import com.shumahe.pethome.Repository.UserBasicRepository;
import com.shumahe.pethome.Service.BaseService.PublishBaseService;
import com.shumahe.pethome.Service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {


    @Autowired
    PetPublishRepository petPublishRepository;

    @Autowired
    PetVarietyRepository petVarietyRepository;

    @Autowired
    PublishBaseService publishBaseService;


    @Autowired
    UserBasicRepository userBasicRepository;

    /**
     * 搜索
     *
     * @param petSearchForm
     * @return
     */
    @Override
    public List<PublishDTO> petSearch(PetSearchForm petSearchForm) {


        /**
         * 未选择详细 默认根据关键字模糊查询所有(first:关键字匹配定义库--->second:数据库查询)
         *
         * 选择详细  根据详细改变查询SQL
         *
         */

        /**  define search parameters
         /**
         * 查询模糊查询 （宠物名称  like 宠物描述 like 丢失地点 like 发布人 in  发布类型 in  宠物性别 in 宠物分类  in 宠物品种  in 丢失状态 in ）
         * @param petName
         * @param petDescription
         * @param lostLocation
         * @param publisher
         * @param publishType
         * @param petSex
         * @param petClassify
         * @param petVariety
         * @param lostState
         * @param Pageable
         * @return List
         */

        /**
         *
         * final sort parameter
         */
        //String petName = petSearchForm.getKeyWord();
        //String petDescription = petSearchForm.getKeyWord();
        //String lostLocation = petSearchForm.getKeyWord();
        //List<String> publisher;
        List<Integer> publishType = new ArrayList<>();
        List<Integer> petSex = new ArrayList<>();
        List<Integer> petClassify = new ArrayList<>();
        List<Integer> petVariety = new ArrayList<>();
        //List<Integer> lostState = new ArrayList<>();

        /**
         *  if choose detail
         */

        Map<String, Boolean> checkChoose = new HashMap<>();
        checkChoose.put("chooseChannel", false);
        checkChoose.put("chooseClassify", false);
        checkChoose.put("chooseVariety", false);
        checkChoose.put("chooseSex", false);

        /********************************************* define keyword ***************************************************************************************

         /**
         * 频道关键字
         */
        List<String> keyWords_MASTER = Arrays.asList("主人", "寻主");
        List<String> keyWords_PET = Arrays.asList("宠物", "寻宠");

        /**
         * 类别关键字
         */
        List<String> keyWords_Cat = Arrays.asList("喵", "猫", "猫子", "猫猫", "小猫", "喵星人");
        List<String> keyWords_Dog = Arrays.asList("汪", "狗", "狗子", "狗狗", "小狗", "汪星人");
        List<String> keyWords_Rabbit = Arrays.asList("兔子", "小兔子", "兔兔", "大白兔", "小白兔");


        /**
         * 性别关键字
         */
        List<String> keyWords_Male = Arrays.asList("公");
        List<String> keyWords_Female = Arrays.asList("母");


        Map<Integer, List<String>> channelMap = new HashMap<>();
        channelMap.put(PublishTypeEnum.SEARCH_MASTER.getCode(), keyWords_MASTER);
        channelMap.put(PublishTypeEnum.SEARCH_PET.getCode(), keyWords_PET);


        Map<Integer, List<String>> classifyMap = new HashMap<>();
        classifyMap.put(PetClassifyEnum.CAT.getCode(), keyWords_Cat);
        classifyMap.put(PetClassifyEnum.DOG.getCode(), keyWords_Dog);
        classifyMap.put(PetClassifyEnum.RABBIT.getCode(), keyWords_Rabbit);


        Map<Integer, List<String>> SexMap = new HashMap<>();
        SexMap.put(PetSexEnum.MALE.getCode(), keyWords_Male);
        SexMap.put(PetSexEnum.FEMALE.getCode(), keyWords_Female);


        /**
         * 品种关键字(数据库查询)
         */
        Map<Integer, List<String>> VarietyMap = new HashMap<>();
        if (petSearchForm.getVarietyId() == SearchEnum.NONE_VALUE.getCode()) {

            List<String> keyWords_CatVariety = new ArrayList<>();
            List<String> keyWords_DogVariety = new ArrayList<>();
            List<String> keyWords_RabbitVariety = new ArrayList<>();
            List<String> keyWords_MouseVariety = new ArrayList<>();

            List<PetVariety> all = petVarietyRepository.findAll();
            all.forEach(variety -> {
                if (PetClassifyEnum.CAT.getCode() == variety.getClassifyId()) {

                    keyWords_CatVariety.add(variety.getName());

                } else if (PetClassifyEnum.DOG.getCode() == variety.getClassifyId()) {

                    keyWords_DogVariety.add(variety.getName());

                } else if (PetClassifyEnum.RABBIT.getCode() == variety.getClassifyId()) {

                    keyWords_RabbitVariety.add(variety.getName());

                } else if (PetClassifyEnum.MOUSE.getCode() == variety.getClassifyId()) {

                    keyWords_MouseVariety.add(variety.getName());

                }
            });

            VarietyMap.put(PetClassifyEnum.CAT.getCode(), keyWords_CatVariety);
            VarietyMap.put(PetClassifyEnum.DOG.getCode(), keyWords_DogVariety);
            VarietyMap.put(PetClassifyEnum.RABBIT.getCode(), keyWords_RabbitVariety);
            VarietyMap.put(PetClassifyEnum.MOUSE.getCode(), keyWords_MouseVariety);
        }

        /********************************************* match keyword ***************************************************************************************/


        if (petSearchForm.getPublishType() == SearchEnum.NONE_VALUE.getCode()) {

            /**
             * 匹配
             */
            channelMap.forEach((k, v) -> {

                if (v.stream().filter(e -> (petSearchForm.getKeyWord().contains(e))).count() > 0) {
                    publishType.add(k);
                }
            });

            if (publishType.isEmpty()) {
                publishType.add(SearchEnum.NONE_VALUE.getCode());
            }

        } else {

            publishType.add(petSearchForm.getPublishType());
            checkChoose.put("chooseChannel", true);


        }


        /**
         * 类别匹配
         */

        if (petSearchForm.getClassifyId() == SearchEnum.NONE_VALUE.getCode()) {

            classifyMap.forEach((k, v) -> {

                if (v.stream().filter(e -> (petSearchForm.getKeyWord()).contains(e)).count() > 0) {
                    petClassify.add(k);
                }
            });

            if (petClassify.isEmpty()) {
                petClassify.add(SearchEnum.NONE_VALUE.getCode());
            }

        } else {

            petClassify.add(petSearchForm.getClassifyId());
            checkChoose.put("chooseClassify", true);
        }

        /**
         * 性别匹配
         */
        if (petSearchForm.getPetSex() == SearchEnum.NONE_VALUE.getCode()) {


            SexMap.forEach((k, v) -> {

                if (v.stream().filter(e -> (petSearchForm.getKeyWord()).contains(e)).count() > 0) {
                    petSex.add(k);
                }
            });

            if (petSex.isEmpty()) {
                petSex.add(SearchEnum.NONE_VALUE.getCode());
            }
        } else {

            petSex.add(petSearchForm.getPetSex());
            checkChoose.put("chooseSex", true);
        }

        /**
         * 品种匹配
         */

        if (petSearchForm.getVarietyId() == SearchEnum.NONE_VALUE.getCode()) {

            classifyMap.forEach((k, v) -> {

                if (v.stream().filter(e -> (petSearchForm.getKeyWord()).contains(e)).count() > 0) {
                    petVariety.add(k);
                }
            });

            if (petVariety.isEmpty()) {
                petVariety.add(SearchEnum.NONE_VALUE.getCode());
            }

        } else {
            petVariety.add(petSearchForm.getVarietyId());
            checkChoose.put("chooseVariety", true);
        }


        //List<UserBasic> userBasics = userBasicRepository.findByNickNameContains(petSearchForm.getKeyWord());
        //publisher = userBasics.stream().map(userBasic -> userBasic.getOpenId()).collect(Collectors.toList());

        /***********************************************search result  by  choose state  use sorted parameters *********************************************************************************/


        /**
         * 改进查询
         */
        Specification<PetPublish> tSpecification = (Root<PetPublish> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            /**
             * basePredicate--->typePredicate---->sexPredicate---->classifyPredicate---->varietyPredicate（final）
             */

            // petName like "%%"    petDescription like "%%"   lostLocation like "%%"
            Predicate petNamePredicate = cb.like(root.get("petName").as(String.class), "%" + petSearchForm.getKeyWord().trim() + "%");
            Predicate descriPredicate = cb.like(root.get("petDescription").as(String.class), "%" + petSearchForm.getKeyWord().trim() + "%");
            Predicate lostPredicate = cb.like(root.get("lostLocation").as(String.class), "%" + petSearchForm.getKeyWord().trim() + "%");
            Predicate findPredicate = cb.equal(root.get("findState").as(Integer.class), 0);//未找到


            Predicate basePredicate = cb.or(petNamePredicate, descriPredicate, lostPredicate);

            Predicate typePredicate;
            Predicate sexPredicate;
            Predicate classifyPredicate;
            Predicate varietyPredicate;
            Predicate findStatePredicate;


            //publishType in (?,?,?)
            CriteriaBuilder.In<Integer> publishTypeIn = cb.in(root.get("publishType").as(Integer.class));
            publishType.stream().forEach(type -> publishTypeIn.value(type));


            //petSex in (?,?,?)
            CriteriaBuilder.In<Integer> petSexIn = cb.in(root.get("petSex").as(Integer.class));
            petSex.stream().forEach(sex -> petSexIn.value(sex));


            //classifyId in (?,?,?)
            CriteriaBuilder.In<Integer> classifyIdIn = cb.in(root.get("classifyId").as(Integer.class));
            petClassify.stream().forEach(classify -> classifyIdIn.value(classify));


            //varietyId in (?,?,?)
            CriteriaBuilder.In<Integer> varietyIdIn = cb.in(root.get("varietyId").as(Integer.class));
            petVariety.stream().forEach(variety -> varietyIdIn.value(variety));


            if (checkChoose.get("chooseChannel")) {

                typePredicate = cb.and(basePredicate, publishTypeIn);

            } else {
                typePredicate = cb.or(basePredicate, publishTypeIn);
            }


            if (checkChoose.get("chooseSex")) {
                sexPredicate = cb.and(typePredicate, petSexIn);
            } else {
                sexPredicate = cb.or(typePredicate, petSexIn);
            }


            if (checkChoose.get("chooseClassify")) {
                classifyPredicate = cb.and(sexPredicate, classifyIdIn);
            } else {
                classifyPredicate = cb.or(sexPredicate, classifyIdIn);
            }

            if (checkChoose.get("chooseVariety")) {
                varietyPredicate = cb.and(classifyPredicate, varietyIdIn);
            } else {
                varietyPredicate = cb.or(classifyPredicate, varietyIdIn);
            }

            findStatePredicate = cb.and(varietyPredicate, findPredicate);

            return findStatePredicate;
        };

        Pageable pageRequest = new PageRequest(0, 200);
        Page all = petPublishRepository.findAll(tSpecification, pageRequest);

        if (all.getContent().isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        /**
         * BaseService查发布关联信息
         */
        List<PublishDTO> petExtends = publishBaseService.findPetExtends(all.getContent());
        return petExtends;
    }


    /**
     * 后台查询
     *
     * @param keywords
     * @return
     */
    @Override
    public Map<String, Object> adminPetSearch(String keywords, Integer publishType) {

        Pageable pageable = new PageRequest(0, 20);
        Page<PetPublish> pets = petPublishRepository.findByPetNameContainsOrOwnerNameContainsOrOwnerContactContains(keywords, keywords, keywords, pageable);
        if (pets.getContent().isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        List<PetPublish> sortedPub = pets.getContent().stream().filter(e -> e.getPublishType() == publishType).collect(toList());
        List<PublishDTO> petsDetail = publishBaseService.findPetsDetail(sortedPub);


        Map<String, Object> res = new HashMap<>();
        res.put("total", pets.getTotalElements());
        res.put("pages", pets.getTotalPages());
        res.put("size", pets.getSize());
        res.put("page", pets.getNumber());
        res.put("data", petsDetail);

        return res;
    }

    /**
     * 查询初始化
     *
     * @return
     */
    @Override
    public List<PublishDTO> init() {

        Pageable pageable = new PageRequest(0, 100);
        Page<PetPublish> pets = petPublishRepository.findByPublishStateAndFindStateOrderByCreateTimeDesc(ShowStateEnum.SHOW.getCode(), PetFindStateEnum.NOT_FOUND.getCode(), pageable);
        List<PublishDTO> petExtends = publishBaseService.findPetExtends(pets.getContent());
        return petExtends;
    }
}
