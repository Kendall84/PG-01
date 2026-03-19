package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecursionTest {

    @Test
    void factorialTest(){
        int n = 5;
        long t1 = System.nanoTime();
        long result = Recursion.factorial(n);
        long t2 = System.nanoTime();
        System.out.println("Factorial de "+n + "es:"+ result
        + "\nFactorial de" + util.Utility.format(t2-t1)+ "ns");
    }


}