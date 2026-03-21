package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import model.Recursion;
import model.RecursionEngine;
import model.TreePainter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController implements Initializable {

    // ----- UI GENERAL -----
    @FXML private TabPane mainTabPane;
    @FXML private Tab tabGraph;

    // ----- UI PESTAÑA ANTIGUA (Fact-Fib) -----
    @FXML private TextField tfOldN;
    @FXML private RadioButton radioOldFib;
    @FXML private RadioButton radioOldFact;
    @FXML private ToggleGroup oldGroup;
    @FXML private Button btnOldCalc;
    @FXML private Button btnOldClean;
    @FXML private Label lblOldResult;
    @FXML private Label lblOldTime;
    @FXML private TreeView<String> treeViewOld;

    // ----- UI FACTORIAL -----
    @FXML private Canvas canvasTree;
    @FXML private Button btnFactReset;
    @FXML private Button btnFactCalc;
    @FXML private Label lblComplexity;
    @FXML private Slider sliderFactN;
    @FXML private Label lblFactN;
    @FXML private ListView<String> listSteps;
    @FXML private Label lblFactResult;
    @FXML private Label lblFactCalls;

    // ----- UI FIBONACCI -----
    @FXML private Canvas canvasFibTree;
    @FXML private Slider sliderFibN;
    @FXML private Label lblFibN;
    @FXML private Button btnFibCalc;
    @FXML private Button btnFibReset;
    @FXML private Label lblFibResult;
    @FXML private Label lblFibCalls;
    @FXML private Label lblFibComplexity;
    @FXML private ListView<String> listFibSteps;
    @FXML private ToggleButton toggleFibNoMemo;
    @FXML private ToggleButton toggleFibMemo;

    private ToggleGroup fibMemoGroup;

    // ----- UI GRAFICO -----
    @FXML private BarChart<String, Number> barChartTime;
    @FXML private BarChart<String, Number> barChartCalls;
    @FXML private NumberAxis timeAxis;
    @FXML private NumberAxis callsAxis;
    @FXML private CategoryAxis xAxisTime;
    @FXML private CategoryAxis xAxisCalls;

    // ----- ATRIBUTOS INTERNOS -----
    private final RecursionEngine engine = new RecursionEngine();
    private final TreePainter painter = new TreePainter();
    private RecursionEngine.CallNode lastRoot;
    private List<RecursionEngine.CallNode> bfsOrder;
    private boolean benchmarkDone = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupOldTab();
        setupFactTab();
        setupFibTab();
        setupGraphTab();
    }

    private void setupOldTab() {
        if (btnOldCalc != null) btnOldCalc.setOnAction(e -> runOldCalc());
        if (btnOldClean != null) btnOldClean.setOnAction(e -> resetOldTab());
    }

    private void resetOldTab() {
        if (tfOldN != null) tfOldN.setText("");
        if (lblOldResult != null) lblOldResult.setText("--");
        if (lblOldTime != null) lblOldTime.setText("--");
        if (treeViewOld != null) treeViewOld.setRoot(null);
    }

    private void runOldCalc() {
        if (tfOldN == null || tfOldN.getText().isEmpty()) return;
        try {
            int n = Integer.parseInt(tfOldN.getText());
            long t1 = System.nanoTime();
            long result;
            String prefix;

            if (radioOldFib != null && radioOldFib.isSelected()) {
                result = engine.computeFibonacci(n);
                prefix = "fib";
            } else {
                result = engine.computeFactorial(n);
                prefix = "fact";
            }
            long t2 = System.nanoTime();

            if (lblOldResult != null) lblOldResult.setText(String.valueOf(result));
            if (lblOldTime != null) lblOldTime.setText(util.Utility.format(t2 - t1) + " ns");

            RecursionEngine.CallNode rootNode = engine.getTreeRoot();
            if (treeViewOld != null && rootNode != null) {
                TreeItem<String> superRoot = new TreeItem<>("Arbol de llamadas recursivos");
                treeViewOld.setRoot(superRoot);

                List<TreeItem<String>> flatList = new ArrayList<>();
                flattenTree(rootNode, flatList, new AtomicInteger(1), prefix);
                
                superRoot.getChildren().addAll(flatList);
                superRoot.setExpanded(true);
            }

        } catch (NumberFormatException e) {
            if (lblOldResult != null) lblOldResult.setText("Error: N inválido");
        }
    }

    private void flattenTree(RecursionEngine.CallNode node, List<TreeItem<String>> list, AtomicInteger counter, String prefix) {
        if (node == null) return;

        String text = String.format("[%02d] LLamada No.: %s [%d]", counter.getAndIncrement(), prefix, node.n);
        TreeItem<String> item = new TreeItem<>(text);
        list.add(item);

        for (RecursionEngine.CallNode child : node.children) {
            flattenTree(child, list, counter, prefix);
        }
    }

    private void setupFactTab() {
        if (sliderFactN != null) {
            sliderFactN.setMin(1); sliderFactN.setMax(12); sliderFactN.setValue(5);
            sliderFactN.setMajorTickUnit(1); sliderFactN.setSnapToTicks(true);
            sliderFactN.valueProperty().addListener((observable, oldValue, newValue) -> {
                lblFactN.setText(String.valueOf(newValue.intValue()));
            });
        }
        if (btnFactCalc != null) btnFactCalc.setOnAction(event -> runFactorial());
        if (btnFactReset != null) btnFactReset.setOnAction(e -> resetFactTab());
    }

    private void setupFibTab() {
        if (sliderFibN != null) {
            sliderFibN.setMin(1); sliderFibN.setMax(15); sliderFibN.setValue(5);
            sliderFibN.setMajorTickUnit(1); sliderFibN.setSnapToTicks(true);
            sliderFibN.valueProperty().addListener((observable, oldValue, newValue) -> {
                lblFibN.setText(String.valueOf(newValue.intValue()));
            });
        }
        
        if (toggleFibNoMemo != null && toggleFibMemo != null) {
            fibMemoGroup = new ToggleGroup();
            toggleFibNoMemo.setToggleGroup(fibMemoGroup);
            toggleFibMemo.setToggleGroup(fibMemoGroup);
            
            fibMemoGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == toggleFibNoMemo) {
                    setBtnStyle(toggleFibNoMemo, true);
                    setBtnStyle(toggleFibMemo, false);
                } else if (newVal == toggleFibMemo) {
                    setBtnStyle(toggleFibNoMemo, false);
                    setBtnStyle(toggleFibMemo, true);
                }
            });
            
            toggleFibNoMemo.setSelected(true);
        }

        if (btnFibCalc != null) btnFibCalc.setOnAction(event -> runFibonacci());
        if (btnFibReset != null) btnFibReset.setOnAction(e -> resetFibTab());
    }
    
    private void setBtnStyle(Node node, boolean isPrimary) {
        node.getStyleClass().remove("btn-primary");
        node.getStyleClass().remove("btn-ghost");
        if (isPrimary) {
            node.getStyleClass().add("btn-primary");
        } else {
            node.getStyleClass().add("btn-ghost");
        }
    }
    
    private void setupGraphTab() {
        // Configurar ejes Y
        if (timeAxis != null) {
            timeAxis.setAutoRanging(false);
            timeAxis.setLowerBound(0);
            timeAxis.setUpperBound(100000);
            timeAxis.setTickUnit(10000);
        }
        if (callsAxis != null) {
            callsAxis.setAutoRanging(false);
            callsAxis.setLowerBound(0);
            callsAxis.setUpperBound(200);
            callsAxis.setTickUnit(25);
        }

        // Configurar categorías del eje X para evitar desorden
        if (xAxisTime != null) {
            xAxisTime.setCategories(FXCollections.observableArrayList(Arrays.asList("10", "15", "25", "50")));
        }
        if (xAxisCalls != null) {
            xAxisCalls.setCategories(FXCollections.observableArrayList(Arrays.asList("5", "10", "15", "25", "50")));
        }
        
        // Ejecutar benchmark al seleccionar la pestaña por primera vez
        if (tabGraph != null) {
            tabGraph.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
                if (isSelected && !benchmarkDone) {
                    runBenchmark();
                    benchmarkDone = true;
                }
            });
        }
    }

    private void resetFactTab() {
        if (lblFactResult != null) lblFactResult.setText("-");
        if (lblFactCalls != null) lblFactCalls.setText("-");
        if (lblComplexity != null) lblComplexity.setText("-");
        if (listSteps != null) listSteps.getItems().clear();
        if (canvasTree != null) {
            canvasTree.getGraphicsContext2D().clearRect(0, 0, canvasTree.getWidth(), canvasTree.getHeight());
        }
    }

    private void resetFibTab() {
        if (lblFibResult != null) lblFibResult.setText("-");
        if (lblFibCalls != null) lblFibCalls.setText("-");
        if (lblFibComplexity != null) lblFibComplexity.setText("-");
        if (listFibSteps != null) listFibSteps.getItems().clear();
        if (canvasFibTree != null) {
            canvasFibTree.getGraphicsContext2D().clearRect(0, 0, canvasFibTree.getWidth(), canvasFibTree.getHeight());
        }
    }

    private void runFactorial() {
        if (sliderFactN == null) return;
        int n = (int) sliderFactN.getValue();
        engine.computeFactorial(n);
        lastRoot = engine.getTreeRoot();
        bfsOrder = TreePainter.collectBFS(lastRoot);

        if (listSteps != null) {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i = 0; i < engine.getSteps().size(); i++){
                RecursionEngine.Step step = engine.getSteps().get(i);
                items.add(String.format("[%02d] %s", i + 1, step.description));
            }
            listSteps.setItems(items);
        }
        
        if (lblFactResult != null) lblFactResult.setText(String.valueOf(engine.getTreeRoot().result));
        if (lblFactCalls != null) lblFactCalls.setText(String.valueOf(engine.getCallCount()));
        if (lblComplexity != null) lblComplexity.setText("O(n) = O(" + n + ") llamadas");

        drawTree(canvasTree, lastRoot, bfsOrder);
    }

    private void runFibonacci() {
        if (sliderFibN == null) return;
        int n = (int) sliderFibN.getValue();
        
        boolean useMemo = toggleFibMemo != null && toggleFibMemo.isSelected();

        if (useMemo) {
            engine.computeFibonacciMemo(n);
        } else {
            engine.computeFibonacci(n);
        }
        
        lastRoot = engine.getTreeRoot();
        bfsOrder = TreePainter.collectBFS(lastRoot);

        if (listFibSteps != null) {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i = 0; i < engine.getSteps().size(); i++) {
                RecursionEngine.Step step = engine.getSteps().get(i);
                items.add(String.format("[%02d] %s", i + 1, step.description));
            }
            listFibSteps.setItems(items);
        }
        
        if (lblFibResult != null) lblFibResult.setText(String.valueOf(engine.getTreeRoot().result));
        if (lblFibCalls != null) lblFibCalls.setText(String.valueOf(engine.getCallCount()));
        
        if (lblFibComplexity != null) {
            lblFibComplexity.setText(useMemo ? "O(n) aprox" : "O(2^n)");
        }

        drawTree(canvasFibTree, lastRoot, bfsOrder);
    }

    private void runBenchmark() {
        if (barChartTime == null || barChartCalls == null) return;
        barChartTime.getData().clear();
        barChartCalls.getData().clear();

        // --- Gráfico de Tiempos ---
        XYChart.Series<String, Number> timeSeriesArray = new XYChart.Series<>();
        timeSeriesArray.setName("Fib memo array");
        XYChart.Series<String, Number> timeSeriesRecursive = new XYChart.Series<>();
        timeSeriesRecursive.setName("Fib Recursiva");
        XYChart.Series<String, Number> timeSeriesHashMap = new XYChart.Series<>();
        timeSeriesHashMap.setName("Fib Memo HashMap");
        
        int[] timeTestValues = {10, 15, 25, 50};
        for (int n : timeTestValues) {
            String label = String.valueOf(n);
            timeSeriesArray.getData().add(new XYChart.Data<>(label, (long)Recursion.fibMemoArray(n, new long[n + 1], new AtomicInteger(0))));
            timeSeriesHashMap.getData().add(new XYChart.Data<>(label, (long)Recursion.fibMemo(n, new HashMap<>(), new AtomicInteger(0))));
            if (n <= 25) {
                timeSeriesRecursive.getData().add(new XYChart.Data<>(label, (long)Recursion.fibonacci(n, new AtomicInteger(0))));
            }
        }
        barChartTime.getData().addAll(timeSeriesArray, timeSeriesHashMap, timeSeriesRecursive);

        // --- Gráfico de Llamadas ---
        XYChart.Series<String, Number> callsSeriesArray = new XYChart.Series<>();
        callsSeriesArray.setName("Fib memo array");
        XYChart.Series<String, Number> callsSeriesRecursive = new XYChart.Series<>();
        callsSeriesRecursive.setName("Fib Recursiva");
        XYChart.Series<String, Number> callsSeriesHashMap = new XYChart.Series<>();
        callsSeriesHashMap.setName("Fib Memo HashMap");

        int[] callsTestValues = {5, 10, 15, 25, 50};
        for (int n : callsTestValues) {
            String label = String.valueOf(n);
            AtomicInteger countArr = new AtomicInteger(0);
            Recursion.fibMemoArray(n, new long[n + 1], countArr);
            callsSeriesArray.getData().add(new XYChart.Data<>(label, countArr.get()));

            AtomicInteger countMap = new AtomicInteger(0);
            Recursion.fibMemo(n, new HashMap<>(), countMap);
            callsSeriesHashMap.getData().add(new XYChart.Data<>(label, countMap.get()));
            
            AtomicInteger countSimple = new AtomicInteger(0);
            if (n <= 25) {
                Recursion.fibonacci(n, countSimple);
            }
            callsSeriesRecursive.getData().add(new XYChart.Data<>(label, countSimple.get()));
        }
        barChartCalls.getData().addAll(callsSeriesArray, callsSeriesHashMap, callsSeriesRecursive);
    }

    private void drawTree(Canvas canvas, RecursionEngine.CallNode root, List<RecursionEngine.CallNode> bfsOrder) {
        if (canvas == null || root == null) return;
        painter.paint(canvas, root, bfsOrder.size(), bfsOrder);
    }
}