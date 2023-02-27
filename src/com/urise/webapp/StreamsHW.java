package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamsHW {

    public static void main(String[] args) {

        final int[] testArray1 = {1, 2, 3, 3, 2, 3};
        final int[] testArray2 = {9, 8};

        System.out.println(minValue(testArray1));
        System.out.println(minValue(testArray2));

        List<Integer> oddList = Arrays.asList(1, 2, 3, 3);
        List<Integer> evenList = Arrays.asList(1, 2, 3, 4);
        System.out.println("oddOrEven:");
        System.out.println("Array with odd sum: " + oddOrEven(oddList).toString());
        System.out.println("Array with even sum: " + oddOrEven(evenList).toString());
        System.out.println("oddOrEven2:");
        System.out.println("Array with odd sum: " + oddOrEven2(oddList).toString());
        System.out.println("Array with even sum: " + oddOrEven2(evenList).toString());
        System.out.println("oddOrEven3:");
        System.out.println("Array with odd sum: " + oddOrEven3(oddList).toString());
        System.out.println("Array with even sum: " + oddOrEven3(evenList).toString());
    }

    public static int minValue(int[] values) {
        return Arrays.stream(values)
                .sorted()
                .distinct()
                .reduce(0, (acc, element) -> acc * 10 + element);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        List<Integer> oddList = new ArrayList<Integer>();
        List<Integer> evenList = new ArrayList<Integer>();
        long sumAll = integers.stream().peek(i -> {
            if ((i % 2) == 0) {
                evenList.add(i);
            } else {
                oddList.add(i);
            }
        }).mapToInt(i -> i).sum();

        return ((sumAll % 2) != 0) ? evenList : oddList;
    }

    public static List<Integer> oddOrEven2(List<Integer> integers) {
        List<Integer> oddList = integers.stream().filter(i -> i.intValue() % 2 != 0).collect(Collectors.toList());

        List<Integer> evenList = integers.stream().filter(i -> i.intValue() % 2 == 0).collect(Collectors.toList());

        return ((oddList.size() % 2) != 0) ? evenList : oddList;
    }

    public static List<Integer> oddOrEven3(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream().collect(Collectors.partitioningBy(i -> i.intValue() % 2 == 0));
        return ((map.get(false).size() % 2) != 0) ? map.get(true) : map.get(false);
    }


}
