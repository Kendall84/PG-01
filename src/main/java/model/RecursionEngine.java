package model;


import java.util.*;


/**
 * motor de recursividad: implementa factorial y fibonacci en javaFX
 */
public class RecursionEngine {


    //noido para un arbol de llamadas recursivas
    public static class CallNode{

        public final  String label;
        public final int n;
        public final List<CallNode> children = new ArrayList<>();
        public long result = -1;
        public boolean fromMemo= false;//para fin con memorizacion
        public int depth;

        public CallNode(String label, int n, int depth){

            this.label = label;
            this.n = n;
            this.depth = depth;
        }
    }

    //datos de cada llamado recursivo - paso recursion
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

    //atributos del estado interno
    private final Map<Integer, Long> memo = new HashMap<>();
    private int callCount;
    private final List<Step> steps = new ArrayList<>();
    private CallNode treeRoot;//raiz del arbol de llamadas


    public void reset(){

        memo.clear();
        callCount = 0;
        steps.clear();
        treeRoot = null;

    }

    public long computeFactorial (int n){

        reset();
        treeRoot = new CallNode("fact("+ n + ")", n,0);
        long result = factorial(n, treeRoot,0);
        steps.add(new Step("Resultado final", n+ "! = "+ result,
                result, false, callCount));

        return result;
    }

    private long factorial(int n, CallNode parent, int depth){

        callCount++;
        String label = "fact("+ n + ")";
        steps .add(new Step("Llamada No.;"+callCount+ label
                , buildFactExp(n), -1, false, callCount));

        if (n<=1) {
            parent.result = 1;
            steps.add(new Step("Caso base: "+ label+ " = 1"
            , "Fact(1) = 1 ", 1, false, callCount));
            return  1;
        }

        CallNode child = new CallNode("fact("+(n-1)+")",n-1, depth+1);
        parent.children.add(child);
        long sub = factorial(n-1, child, depth+1);
        long result = (long) n * sub;
        parent.result = result;

        steps.add(new Step("Retorno: "+ label+ " = "+n+" * "+ sub+ " = "+ result,
                           label+ " = "+ result,
                           result, false, callCount));
        return result;
    }

    private String buildFactExp(int n) {

        if (n <= 1) return "1";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i >= 1; i-- ){
            sb.append(i);
            if (i>1) sb.append(" * ");
        }

        return sb.toString();

    }

    public List<Step> getSteps(){return steps;}
    public CallNode getTreeRoot(){return treeRoot;}
    public int getCallCount(){return callCount;}
    public Map<Integer, Long> getMemo(){return Collections.unmodifiableMap(memo);}


}
