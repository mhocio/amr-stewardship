package com.top.antibiotic.unit.functions;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class MathFunctionsTests {

    @Test
    public void test() {
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(5, 2);
        while (iterator.hasNext()) {
            final int[] combination = iterator.next();
            System.out.println(Arrays.toString(combination));
        }
    }

    @Test
    public void test2() {
        HashSet<String> set1 = new HashSet();
        set1.add("One");
        set1.add("Two");

        HashSet<String> set2 = new HashSet();
        set2.add("Two");
        set2.add("One");

        Assert.assertEquals(set1, set2);
    }
}
