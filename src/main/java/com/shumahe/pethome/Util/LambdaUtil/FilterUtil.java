package com.shumahe.pethome.Util.LambdaUtil;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterUtil {

    public static void filterAndPredicate() {

        List<String> strings = Arrays.asList("zhangsan", "lisi", "wangwu", "zhaoliu", "qumazi");


        //单个条件进行过滤
        strings.stream().filter(e -> e.contains("123")).collect(Collectors.toList());
        //多个条件进行过滤
        Predicate<String> str = s -> s.contains("213");
        Predicate<String> str2 = s -> s.contains("213");
        strings.stream().filter(str.and(str2));


        //过滤思路1 直接拿一个变量去过滤
        PetPublish pet = new PetPublish();
        PetPublish pet2 = new PetPublish();
        PetPublish pet3 = new PetPublish();
        List<PetPublish> petList = Arrays.asList(pet, pet2, pet3);
        Predicate<PetPublish> pre1 = e -> e.getPublisherId().equals(213);
        Predicate<PetPublish> pre2 = e -> e.getPublisherId().equals(456);
        petList.stream().filter(pre1.and(pre2)).collect(Collectors.toList());

        //过滤思路2 搞个临时变量去过滤 另起一个做集合做参数
        List<String> someValue = Arrays.asList("1","2","3");
        Predicate<PetPublish> pre3 = e -> someValue.contains(e.getPublisherId());
        petList.stream().filter(pre3).collect(Collectors.toList());


        List<String> collect = petList.stream().map(PetPublish::getPublisherId).collect(Collectors.toList());
        Map<String, List<String>> map = new HashMap<>();
        Predicate<Map<String, List<String>>> mapPre = m -> m.containsValue("zhangsan");
        Predicate<Map<String, List<String>>> mapPre2 = m -> m.containsValue("lisi");


        //对Map进行Filter;
        //1.先对list进行Filter
        //2.list 转 map
        petList.stream().filter(e -> e.getPublisherId().equals("aaa"));


    }
}
