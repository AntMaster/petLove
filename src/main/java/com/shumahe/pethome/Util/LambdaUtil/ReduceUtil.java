package com.shumahe.pethome.Util.LambdaUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  reduce() 函数可以将所有值合并成一个
 */
public class ReduceUtil {


    public  static  void main(String[] arg){

        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().distinct().collect(Collectors.toList());
        System.out.println(distinct);

        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining("----"));
        System.out.println(G7Countries);

    }
    public  static  void reduce(){
        // 为每个订单加上12%的税
        // 老方法：
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double total = 0;
        for (Integer cost : costBeforeTax) {
            double price = cost + .12*cost;
            total = total + price;
        }
        System.out.println("Total : " + total);

        // 新方法：
        double bill = costBeforeTax.stream().map((cost) -> cost + .12 * cost).reduce((x,v)->x+v).get();
                //reduce((sum, cost) -> sum + cost).get();
        System.out.println("Total : " + bill);

    }

}
