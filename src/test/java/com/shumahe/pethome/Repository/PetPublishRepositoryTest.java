package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.UserBasic;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetPublishRepositoryTest {

    @Autowired
    PetPublishRepository petPublishRepository;

    @Autowired
    UserBasicRepository userBasicRepository;

    @Test
    public void findByOrderByIdDesc() {
    }

    @Test
    public void findByPublishTypeOrderByIdDesc() {
    }

    @Test
    public void findByPublishStateOrderById() {


        Specification<PetPublish> specification = new Specification<PetPublish>() {


            /**
             * @param *root: 代表查询的实体类.
             * @param query: 可以从中可到 Root 对象, 即告知 JPA Criteria 查询要查询哪一个实体类. 还可以
             * 来添加查询条件, 还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象.
             * @param *cb: CriteriaBuilder 对象. 用于创建 Criteria 相关对象的工厂. 当然可以从中获取到 Predicate 对象
             * @return: *Predicate 类型, 代表一个查询条件.
             */

            @Override
            public Predicate toPredicate(Root<PetPublish> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                query.from(PetPublish.class);
                Path<String> nameExp = root.get("petName");


            /*    Predicate p1=cb.like(root.get("petName").as(String.class), uqm.getName());
                Predicate p2=cb.equal(root.get("uuid").as(Integer.class), uqm.getUuid());
                Predicate p3=cb.gt(root.get("age").as(Integer.class), uqm.getAge());
*/
                return cb.like(nameExp, "%用户2的狐狸%");

            }
        };


        List all = petPublishRepository.findAll(specification);
        System.out.println(all.toArray());
    }

    @Test
    public void findByPublishStateOrderById1() {

        PetPublish petPublish = new PetPublish();
        petPublish.setPetName("用户1的狗子");


        PageRequest pageRequest = new PageRequest(0, 10);

        /**
         * 查询模糊查询 （宠物名称 like 宠物描述 like 丢失地点 like 发布人  in 发布类型  in 宠物性别 in 宠物分类 in  宠物品种 in  丢失状态 in）
         * @param petName
         * @param petDescription
         * @param lostLocation
         * @param publisher
         * @param publishType
         * @param petSex
         * @param petClassify
         * @param petVariety
         * @param lostState
         * @param pageRequest
         * @return List
         */

        String petName = "用户1的狗子";
        String petDescription = "aa";
        String lostLocation = "aa";
        List<String> publisher = new ArrayList<>();
        publisher.add("213");
        List<Integer> publishType = Arrays.asList(1, 2);
        List<Integer> petSex = Arrays.asList(1, 2);
        List<Integer> petClassify = Arrays.asList(1, 2);
        List<Integer> petVariety = Arrays.asList(1, 2);
        List<Integer> findState = Arrays.asList(1, 2);





    }


}