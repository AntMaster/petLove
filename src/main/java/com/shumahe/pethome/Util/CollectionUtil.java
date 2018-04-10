package com.shumahe.pethome.Util;

import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static List<String> removeRepeatStringItem(List<String> strings) {
        return strings.stream()
                .collect(Collectors.toMap(e -> e, e -> 1, (a, b) -> a + b)) // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .entrySet().stream()                    // 所有 entry 对应的 Stream
                .filter(entry -> entry.getValue() > 1)  // 过滤出元素出现次数大于 1 的 entry
                .map(entry -> entry.getKey())           // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList());          // 转化为 List




    }



    public static List<Integer> distinctIntegerItems(List<Integer> integers) {

        return integers.stream().map(i -> i * i).distinct().collect(Collectors.toList());

    }


}
