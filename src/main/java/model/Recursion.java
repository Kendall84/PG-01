package model;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Recursion {
    public static long factorial(int n, AtomicInteger counter){

        counter.incrementAndGet();
        if(n<=1)return 1;
        return n* factorial(n-1, counter);
    }

        public static void matryoshka(int n) {
            if (n <= 1) {
                System.out.println("Abriendo la muñeca mas pequeña:" + n);
                return;

            }
            System.out.println("Abriendo la muñeca mas pequeña:" + n);
            matryoshka(n - 1);
        }

        public static String matryoshkaS(int n) {
            if (n <= 1) {
                return "Abriendo la muñeca mas pequeña:" + n;
            }
            return "Abriendo la muñeca mas pequeña:" + n + "\n" + matryoshkaS(n - 1);
        }

        public static long fibonacci(int n, AtomicInteger counter) {
            counter.incrementAndGet();//Contar cada llamada recursiva
            if (n <= 1) {
                return n;
            } else {
                return fibonacci(n - 1,counter) + fibonacci(n - 2, counter);
            }
        }

        //Fibonacci con memoizacion
        public static long fibMemo(int n, Map<Integer, Long> memo, AtomicInteger counter) {
            counter.incrementAndGet();//Contar cada llamada recursiva

            if (n <= 1) return n;
            if (memo.containsKey(n)) return memo.get(n);
            long result = fibMemo(n - 1, memo, counter) + fibMemo(n - 2, memo, counter);
            memo.put(n, result);
            return result;
        }

        //fibonacci con arreglos
        public static long fibMemoArray(int n, long[] memo, AtomicInteger counter) {
            counter.incrementAndGet();//Contar cada llamada recursiva

            if (n <= 1) return n
                    ;
            if (memo[n] != 0) return memo[n];
            memo[n] = fibMemoArray(n - 1, memo, counter) + fibMemoArray(n - 2, memo, counter);
            return memo[n];
        }


    }


