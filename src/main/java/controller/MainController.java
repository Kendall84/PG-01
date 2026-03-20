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
import model.Recursion;
import model.RecursionEngine;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController implements Initializable {

    @FXML
    private Canvas canvasTree;
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
    private ListView listSteps;
    @FXML
    private Label lblFactResult;
    @FXML
    private Label lblFactCalls;

    // ----- UI DE FIBONACCI -----
    // Asigna estos fx:id en Scene Builder para la pestaña de Fibonacci:

    @FXML
    private Slider sliderFibN;      // fx:id="sliderFibN"
    @FXML
    private Label lblFibN;          // fx:id="lblFibN"
    @FXML
    private Button btnFibCalc;      // fx:id="btnFibCalc"
    @FXML
    private Button btnFibReset;     // fx:id="btnFibReset"
    @FXML
    private Label lblFibResult;     // fx:id="lblFibResult"
    @FXML
    private Label lblFibCalls;      // fx:id="lblFibCalls"
    @FXML
    private Label lblFibComplexity; // fx:id="lblFibComplexity"
    @FXML
    private ListView listFibSteps;  // fx:id="listFibSteps"
    @FXML
    private ToggleButton toggleFibNoMemo; // fx:id="toggleFibNoMemo"
    @FXML
    private ToggleButton toggleFibMemo;   // fx:id="toggleFibMemo"

    private ToggleGroup fibMemoGroup;


    //atributos internos de la clase controlller
    private final RecursionEngine engine = new RecursionEngine();
    private RecursionEngine.CallNode lastRoot;
    private List<RecursionEngine.CallNode> factBFS;


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
        // Configuracion de fibonacci
        // Asegúrate de que sliderFibN no sea nulo (esto pasará si no has asignado el fx:id en el FXML)
        if (sliderFibN != null) {
            sliderFibN.setMin(1); sliderFibN.setMax(15); sliderFibN.setValue(5);
            sliderFibN.setMajorTickUnit(1); sliderFibN.setSnapToTicks(true);
            sliderFibN.valueProperty().addListener((observable, oldValue, newValue) -> {
                lblFibN.setText(String.valueOf(newValue.intValue()));
            });
        }
        
        // Configurar ToggleButtons para memorizacion
        if (toggleFibNoMemo != null && toggleFibMemo != null) {
            fibMemoGroup = new ToggleGroup();
            toggleFibNoMemo.setToggleGroup(fibMemoGroup);
            toggleFibMemo.setToggleGroup(fibMemoGroup);
            toggleFibNoMemo.setSelected(true); // Por defecto sin memorizacion
        }

        if (btnFibCalc != null) btnFibCalc.setOnAction(event -> runFibonacci());
        if (btnFibReset != null) btnFibReset.setOnAction(e -> resetFibTab());
    }

    private void resetFactTab() {
        lblFactResult.setText("-");
        lblFactCalls.setText("-");
        lblComplexity.setText("-");
        listSteps.getItems().clear();

    }

    private void resetFibTab() {
        if (lblFibResult != null) lblFibResult.setText("-");
        if (lblFibCalls != null) lblFibCalls.setText("-");
        if (lblFibComplexity != null) lblFibComplexity.setText("-");
        if (listFibSteps != null) listFibSteps.getItems().clear();

    }

    private void runFactorial() {
        int n = (int) sliderFactN.getValue();
//        AtomicInteger counter = new AtomicInteger(0);
//        long result = Recursion.factorial(n,counter);
//        lblFactResult.setText(util.Utility.format(result));
//        lblFactCalls.setText(String.valueOf(counter.get()));

        engine.computeFactorial(n);
        lastRoot = engine.getTreeRoot();


        //llenamos la lista pasos
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i<n; i++){
            RecursionEngine.Step step = engine.getSteps().get(i);
            items.add(String.format("[%02d] %s", i+1, step.description));
        }
        listSteps.setItems(items);
        lblFactResult.setText(util.Utility.format(engine.getTreeRoot().result));
        lblFactCalls.setText(String.valueOf(engine.getCallCount()));
        lblComplexity.setText("O(n) = O(" + n + ") llamadas");
    }

    private void runFibonacci() {
        if (sliderFibN == null) return;
        int n = (int) sliderFibN.getValue();
        
        // Verificar si usamos memorizacion
        boolean useMemo = false;
        if (toggleFibMemo != null && toggleFibMemo.isSelected()) {
            useMemo = true;
        }

        if (useMemo) {
            engine.computeFibonacciMemo(n); // Asumimos que implementaste este metodo en RecursionEngine como sugerido
        } else {
            engine.computeFibonacci(n);
        }
        
        lastRoot = engine.getTreeRoot();

        //llenamos la lista pasos
        if (listFibSteps != null) {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i = 0; i < engine.getSteps().size(); i++) {
                RecursionEngine.Step step = engine.getSteps().get(i);
                items.add(String.format("[%02d] %s", i + 1, step.description));
            }
            listFibSteps.setItems(items);
        }
        
        if (lblFibResult != null) lblFibResult.setText(util.Utility.format(engine.getTreeRoot().result));
        if (lblFibCalls != null) lblFibCalls.setText(String.valueOf(engine.getCallCount()));
        
        if (lblFibComplexity != null) {
            if (useMemo) {
                lblFibComplexity.setText("O(n) aprox");
            } else {
                lblFibComplexity.setText("O(2^n)");
            }
        }
    }
}