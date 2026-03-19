package ucr.algoritmos.matryoshka;

import model.Recursion;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static model.Recursion.matryoshkaS;

class RecursionTest {
    @Test
    void factorialTest() {
        int n = 5;
        long t1 = System.nanoTime();
        long result = Recursion.factorial(n);
        long t2 = System.nanoTime();



        System.out.println("tn: " + util.Utility.format(t2 - t1));
        System.out.println("\nFactorial de:" + n + " es: " + result);
    }

    @Test
    void matryoskaTest() {
        System.out.println(matryoshkaS(5));
    }

    @Test
    void fibonacciTest() {
        int[] list = {5, 10, 12, 15,20};
        for (int n : list) {
            AtomicInteger counter = new AtomicInteger(0);

            long t1 = System.nanoTime();
            long result = Recursion.fibonacci(n, counter);
            long t2 = System.nanoTime();
            System.out.println("Fibonacci de:" + n + " es: " + util.Utility.format(result) + "\ntotal de llamadas recursivas: " + counter.get()+" T(n)" + util.Utility.format(t2 - t1)+ "ns");

        }
    }

    @Test
    void fibMemoTest() {
        int[] list = {5, 10, 12, 15,20};
        for (int n : list) {
            Map<Integer, Long> memo = new java.util.HashMap<>();
            AtomicInteger counter = new AtomicInteger(0);

            long t1 = System.nanoTime();
            long result = model.Recursion.fibMemo(n, memo,counter);
            long t2 = System.nanoTime();
            System.out.println("FibMemoHashMap:" + n + " is: " + util.Utility.format(result) + "\ntotal de llamadas recursivas: " + counter.get()+" T(n)" + util.Utility.format(t2 - t1)+ "ns");

        }

    }
    @Test
    void fibMemoArrayTest() {
        int[] list = {5, 10, 12, 15,20};
        for (int n : list) {
            long [] memo = new long[n+1];
            for (int i = 0; i < n+1; i++) {
                memo[i] = -1; // Inicializar con un valor
            }
            AtomicInteger counter = new AtomicInteger(0);

            long t1 = System.nanoTime();
            long result = Recursion.fibMemoArray(n, memo,counter);
            long t2 = System.nanoTime();
            System.out.println("FibMemoArray:" + n + " is: " + util.Utility.format(result) + "\ntotal de llamadas recursivas: " + counter.get()+" T(n)" + util.Utility.format(t2 - t1)+ "ns");

        }

    }


}