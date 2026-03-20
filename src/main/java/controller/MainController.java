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


    //atributos internos de la clase controlller
    private final RecursionEngine engine = new RecursionEngine();
    private RecursionEngine.CallNode lastRoot;
    private List<RecursionEngine.CallNode> factBFS;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupFactTab();
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

    private void resetFactTab() {
        lblFactResult.setText("-");
        lblFactCalls.setText("-");
        lblComplexity.setText("-");
        listSteps.getItems().clear();

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
}