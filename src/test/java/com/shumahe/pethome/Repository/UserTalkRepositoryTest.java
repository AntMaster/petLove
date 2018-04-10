package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.UserTalk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Arrays.*;


public class UserTalkRepositoryTest {


    public static  void  main(String[] args){

        Map<Integer,List<String>> map = new HashMap<>();

        map.put(1,asList("1","2","3"));
        map.put(2,asList("1"));
        map.put(3,asList("1","2","3"));

        System.out.println(map.get(4));

    }

}