package ucr.lab.pg01;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        start1(stage);
        start2(stage);
    }

    private void start2(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        Scene scene = new Scene(fxmlLoader.load(), 1100, 720);

        scene.getStylesheets().add(HelloApplication.class.getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void start1(Stage stage){
        TextField name = new TextField("1000000");
        Label result = new Label ("");
        Button button = new Button("Medir 0(n) vs 0(n2)");
        button.setOnAction(ActionEvent ->{
            int n = Integer.parseInt(name.getText());

        });


        long n = Integer.parseInt(name.getText());


        long t1 = System.nanoTime();
        long sum=0;
        for (int i=0;i<n;i++) {
            sum += i;
        }
        long t2=System.nanoTime();
        result.setText("0(n):"+ util.Utility.format(t2-t1) +" ns");

    }

}