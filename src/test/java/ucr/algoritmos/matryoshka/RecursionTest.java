package ucr.algoritmos.matryoshka;

import model.Recursion;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecursionTest {

    @Test
    void factorialTest() {
        System.out.println("--- Factorial Test ---");
        for (int i = 6; i <= 12; i++) {
            AtomicInteger counter = new AtomicInteger(0);
            long result = Recursion.factorial(i, counter);
            System.out.println("Factorial de " + i + " es: " + result + ", Llamadas: " + counter.get());
        }
    }

    @Test
    void fibonacciComparisonTest() {
        System.out.println("\n--- Fibonacci Comparison ---");
        int[] values = {5, 10, 15};
        for (int n : values) {
            System.out.println("\n--- N = " + n + " ---");

            // Sin memorización
            AtomicInteger counter1 = new AtomicInteger(0);
            long t1 = System.nanoTime();
            long result1 = Recursion.fibonacci(n, counter1);
            long t2 = System.nanoTime();
            System.out.printf("Fibonacci Simple:      Resultado=%-5d Llamadas=%-5d Tiempo=%-10s ns%n", result1, counter1.get(), util.Utility.format(t2 - t1));

            // Con memorización (HashMap)
            Map<Integer, Long> memoMap = new HashMap<>();
            AtomicInteger counter2 = new AtomicInteger(0);
            long t3 = System.nanoTime();
            long result2 = Recursion.fibMemo(n, memoMap, counter2);
            long t4 = System.nanoTime();
            System.out.printf("Fibonacci con HashMap: Resultado=%-5d Llamadas=%-5d Tiempo=%-10s ns%n", result2, counter2.get(), util.Utility.format(t4 - t3));
        }
    }

    @Test
    void fibonacciAdvancedComparisonTest() {
        System.out.println("\n--- Advanced Fibonacci Comparison ---");
        int[] values = {20, 25, 50}; // 50 es muy grande para el simple
        for (int n : values) {
            System.out.println("\n--- N = " + n + " ---");

            // Con memorización (HashMap)
            Map<Integer, Long> memoMap = new HashMap<>();
            AtomicInteger counterMap = new AtomicInteger(0);
            long t1 = System.nanoTime();
            long resultMap = Recursion.fibMemo(n, memoMap, counterMap);
            long t2 = System.nanoTime();
            System.out.printf("Fibonacci con HashMap: Resultado=%-20d Llamadas=%-5d Tiempo=%-10s ns%n", resultMap, counterMap.get(), util.Utility.format(t2 - t1));

            // Con memorización (Arreglo)
            long[] memoArray = new long[n + 1]; // Se inicializa en 0 por defecto
            AtomicInteger counterArray = new AtomicInteger(0);
            long t3 = System.nanoTime();
            long resultArray = Recursion.fibMemoArray(n, memoArray, counterArray);
            long t4 = System.nanoTime();
            System.out.printf("Fibonacci con Arreglo: Resultado=%-20d Llamadas=%-5d Tiempo=%-10s ns%n", resultArray, counterArray.get(), util.Utility.format(t4 - t3));

            // Fibonacci simple (solo para valores pequeños si es necesario, se omite para 50)
            if (n <= 25) {
                AtomicInteger counterSimple = new AtomicInteger(0);
                long t5 = System.nanoTime();
                long resultSimple = Recursion.fibonacci(n, counterSimple);
                long t6 = System.nanoTime();
                System.out.printf("Fibonacci Simple:      Resultado=%-20d Llamadas=%-5d Tiempo=%-10s ns%n", resultSimple, counterSimple.get(), util.Utility.format(t6 - t5));
            } else {
                System.out.println("Fibonacci Simple:      Omitido para n=" + n + " (demasiado lento)");
            }
        }
    }

    @Test
    void sumaDigitosTest() {
        System.out.println("\n--- Suma de Dígitos Test ---");
        int[] values = {20, 100, 1000, 5000};
        for (int n : values) {
            int result = Recursion.sumaDigitos(n);
            System.out.println("Suma de dígitos de " + n + " es: " + result);
            assertEquals(String.valueOf(n).chars().map(Character::getNumericValue).sum(), result);
        }

        // Prueba de StackOverflowError
        System.out.println("Probando StackOverflowError para sumaDigitos...");
        assertThrows(StackOverflowError.class, () -> Recursion.sumaDigitos(100000)); // Un número muy grande para causar el error
    }
}