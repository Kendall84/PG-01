package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import model.RecursionEngine;
import model.TreePainter;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // ----- UI GENERAL -----
    @FXML
    private Canvas canvasTree; // fx:id="canvasTree" (Factorial)
    @FXML
    private Button btnFactReset;
    @FXML
    private Button btnFactCalc;
    @FXML
    private Label lblComplexity;
    @FXML
    private Slider sliderFactN;
    @FXML
    private Label lblFactN;
    @FXML
    private ListView<String> listSteps;
    @FXML
    private Label lblFactResult;
    @FXML
    private Label lblFactCalls;

    // ----- UI DE FIBONACCI -----
    @FXML
    private Canvas canvasFibTree;   // fx:id="canvasFibTree" (Fibonacci)
    @FXML
    private Slider sliderFibN;
    @FXML
    private Label lblFibN;
    @FXML
    private Button btnFibCalc;
    @FXML
    private Button btnFibReset;
    @FXML
    private Label lblFibResult;
    @FXML
    private Label lblFibCalls;
    @FXML
    private Label lblFibComplexity;
    @FXML
    private ListView<String> listFibSteps;
    @FXML
    private ToggleButton toggleFibNoMemo;
    @FXML
    private ToggleButton toggleFibMemo;

    private ToggleGroup fibMemoGroup;

    // ----- ATRIBUTOS INTERNOS -----
    private final RecursionEngine engine = new RecursionEngine();
    private final TreePainter painter = new TreePainter(); // Instancia del pintor
    private RecursionEngine.CallNode lastRoot;
    private List<RecursionEngine.CallNode> bfsOrder;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupFactTab();
        setupFibTab();
    }

    private void setupFactTab() {
        sliderFactN.setMin(1); sliderFactN.setMax(12); sliderFactN.setValue(5);
        sliderFactN.setMajorTickUnit(1); sliderFactN.setSnapToTicks(true);
        sliderFactN.valueProperty().addListener((observable, oldValue, newValue) -> {
            lblFactN.setText(String.valueOf(newValue.intValue()));
        });
        btnFactCalc.setOnAction(event -> runFactorial());
        btnFactReset.setOnAction(e -> resetFactTab());
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
            toggleFibNoMemo.setSelected(true);
        }

        if (btnFibCalc != null) btnFibCalc.setOnAction(event -> runFibonacci());
        if (btnFibReset != null) btnFibReset.setOnAction(e -> resetFibTab());
    }

    private void resetFactTab() {
        lblFactResult.setText("-");
        lblFactCalls.setText("-");
        lblComplexity.setText("-");
        listSteps.getItems().clear();
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
        int n = (int) sliderFactN.getValue();
        engine.computeFactorial(n);
        lastRoot = engine.getTreeRoot();
        bfsOrder = TreePainter.collectBFS(lastRoot);

        // Llenar lista de pasos
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < engine.getSteps().size(); i++){
            RecursionEngine.Step step = engine.getSteps().get(i);
            items.add(String.format("[%02d] %s", i + 1, step.description));
        }
        listSteps.setItems(items);
        
        lblFactResult.setText(String.valueOf(engine.getTreeRoot().result));
        lblFactCalls.setText(String.valueOf(engine.getCallCount()));
        lblComplexity.setText("O(n) = O(" + n + ") llamadas");

        // Dibujar el árbol en el canvas de Factorial (canvasTree)
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

        // Llenar lista de pasos
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

        // Dibujar el árbol en el canvas de Fibonacci (canvasFibTree)
        drawTree(canvasFibTree, lastRoot, bfsOrder);
    }

    /**
     * Dibuja el árbol de llamadas en el canvas especificado.
     * @param canvas El canvas sobre el que se va a dibujar.
     * @param root La raíz del árbol a dibujar.
     * @param bfsOrder La lista de nodos en orden BFS para determinar qué dibujar.
     */
    private void drawTree(Canvas canvas, RecursionEngine.CallNode root, List<RecursionEngine.CallNode> bfsOrder) {
        if (canvas == null || root == null) return;
        painter.paint(canvas, root, bfsOrder.size(), bfsOrder);
    }
}