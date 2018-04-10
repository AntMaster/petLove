package com.shumahe.pethome.Util.LambdaUtil;

import com.shumahe.pethome.Domain.PetPublish;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaInterfacePractise {


    void  practiseFunction(){


        //Function<T, R>

        //定义一个function 输入是String类型，输出是 PetPublish 类型，  PetPublish是一个类。
        Function<String, PetPublish> times2 = fun -> {
            PetPublish a = new PetPublish();
            a.setPetName(fun);
            return a;
        };

        String[] testIntStrings = {"1", "2", "3", "4"};
        //将String 的Array转换成map,调用times2函数进行转换
        Map<String, PetPublish> eventmap1 = Stream.of(testIntStrings).collect(Collectors.toMap(e -> e, e -> times2.apply(e)));


        //如果Collectors.toMap的转换过程很简单，比如输入和输出类型相同，则不需要另外定义Function,例如
        Map<String, String> eventmap2 = Stream.of(testIntStrings).collect(Collectors.toMap(e -> e, e -> (e + "a")));





    }


    void studyPredicateInterFace(){

    }






}
