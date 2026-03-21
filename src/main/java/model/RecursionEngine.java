package model;

import java.util.*;

/**
 * motor de recursividad: implementa factorial y fibonacci en javaFX
 */
public class RecursionEngine {


    public static class CallNode{
        public final String label;
        public final int n;
        public final List<CallNode> children = new ArrayList<>();
        public long result = -1;
        public boolean fromMemo = false;
        public int depth;

        public CallNode(String label, int n, int depth){
            this.label = label;
            this.n = n;
            this.depth = depth;
        }
    }


    public static class Step{
        public final String description;
        public final String expression;
        public final long partialResult;
        public final boolean isMemo;
        public final int callCount;

        public Step(String description, String expression, long partialResult, boolean Memo, int calls) {
            this.description = description;
            this.expression = expression;
            this.partialResult = partialResult;
            this.isMemo = Memo;
            this.callCount = calls;
        }
    }


    private final Map<Integer, Long> memo = new HashMap<>();
    private int callCount;
    private final List<Step> steps = new ArrayList<>();
    private CallNode treeRoot;

    public void reset(){
        memo.clear();
        callCount = 0;
        steps.clear();
        treeRoot = null;
    }

    public long computeFactorial (int n){
        reset();
        treeRoot = new CallNode("fact("+ n + ")", n, 0);
        long result = factorial(n, treeRoot, 0);
        steps.add(new Step("Resultado final", n + "! = " + result, result, false, callCount));
        return result;
    }

    private long factorial(int n, CallNode parent, int depth){
        callCount++;
        String label = "fact("+ n + ")";
        steps.add(new Step("Llamada No." + callCount + "; " + label, buildFactExp(n), -1, false, callCount));

        if (n <= 1) {
            parent.result = 1;
            steps.add(new Step("Caso base: " + label + " = 1", "Fact(1) = 1", 1, false, callCount));
            return 1;
        }

        CallNode child = new CallNode("fact(" + (n - 1) + ")", n - 1, depth + 1);
        parent.children.add(child);
        long sub = factorial(n - 1, child, depth + 1);
        long result = (long) n * sub;
        parent.result = result;

        steps.add(new Step("Retorno: " + label + " = " + n + " * " + sub + " = " + result, label + " = " + result, result, false, callCount));
        return result;
    }

    private String buildFactExp(int n) {
        if (n <= 1) return "1";
        StringBuilder sb = new StringBuilder();
        for (int i = n; i >= 1; i-- ){
            sb.append(i);
            if (i > 1) sb.append(" * ");
        }
        return sb.toString();
    }

    public long computeFibonacci(int n) {
        reset();
        treeRoot = new CallNode("fib(" + n + ")", n, 0);
        long result = fibonacci(n, treeRoot, 0);
        steps.add(new Step("Resultado final", "fib(" + n + ") = " + result, result, false, callCount));
        return result;
    }

    private long fibonacci(int n, CallNode parent, int depth) {
        callCount++;
        String label = "fib(" + n + ")";
        steps.add(new Step("Llamada No." + callCount + "; " + label, "Calculando " + label, -1, false, callCount));

        if (n <= 1) {
            parent.result = n;
            steps.add(new Step("Caso base: " + label + " = " + n, "fib(" + n + ") = " + n, n, false, callCount));
            return n;
        }

        CallNode child1 = new CallNode("fib(" + (n - 1) + ")", n - 1, depth + 1);
        parent.children.add(child1);
        long r1 = fibonacci(n - 1, child1, depth + 1);

        CallNode child2 = new CallNode("fib(" + (n - 2) + ")", n - 2, depth + 1);
        parent.children.add(child2);
        long r2 = fibonacci(n - 2, child2, depth + 1);

        long result = r1 + r2;
        parent.result = result;

        steps.add(new Step("Retorno: " + label + " = " + r1 + " + " + r2 + " = " + result, label + " = " + result, result, false, callCount));
        return result;
    }

    public long computeFibonacciMemo(int n) {
        reset();
        treeRoot = new CallNode("fib(" + n + ")", n, 0);
        long result = fibonacciMemo(n, treeRoot, 0);
        steps.add(new Step("Resultado final", "fib(" + n + ") = " + result, result, false, callCount));
        return result;
    }

    private long fibonacciMemo(int n, CallNode parent, int depth) {
        callCount++;
        String label = "fib(" + n + ")";
        steps.add(new Step("Llamada No." + callCount + "; " + label, "Calculando " + label, -1, false, callCount));


        if (memo.containsKey(n)) {
            long memoResult = memo.get(n);
            parent.result = memoResult;
            parent.fromMemo = true;
            steps.add(new Step("Resultado por Memo: " + label + " = " + memoResult, "fib(" + n + ") = " + memoResult, memoResult, true, callCount));
            return memoResult;
        }


        if (n <= 1) {
            parent.result = n;
            memo.put(n, (long) n);
            steps.add(new Step("Caso base: " + label + " = " + n, "fib(" + n + ") = " + n, n, false, callCount));
            return n;
        }


        CallNode child1 = new CallNode("fib(" + (n - 1) + ")", n - 1, depth + 1);
        parent.children.add(child1);
        long r1 = fibonacciMemo(n - 1, child1, depth + 1);

        CallNode child2 = new CallNode("fib(" + (n - 2) + ")", n - 2, depth + 1);
        parent.children.add(child2);
        long r2 = fibonacciMemo(n - 2, child2, depth + 1);

        long result = r1 + r2;
        parent.result = result;
        memo.put(n, result);

        steps.add(new Step("Retorno: " + label + " = " + r1 + " + " + r2 + " = " + result, label + " = " + result, result, false, callCount));
        return result;
    }

    public List<Step> getSteps(){ return steps; }
    public CallNode getTreeRoot(){ return treeRoot; }
    public int getCallCount(){ return callCount; }
    public Map<Integer, Long> getMemo(){ return Collections.unmodifiableMap(memo); }
}