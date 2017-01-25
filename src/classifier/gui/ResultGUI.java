package classifier.gui;

import classifier.controller.NaiveBayesianClassifier;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Created by Wijtse on 25-1-2017
 */
public class ResultGUI extends Application {
    private GridPane layout;
    private Stage primaryStage;
    private Group root;
    private Scene scene;

    private Text resultText;
    private Text isThisCorrect;
    private Button btnYes;
    private Button btnNo;

    private String result;
    private String assignedClass;
    private Object monitor;

    public ResultGUI(String result, Object monitor) {
        this.result = "bleh";
        this.monitor = monitor;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.root = new Group();
        this.layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(5, 10, 5, 10));

        primaryStage.setTitle("Result");

        resultText = new Text("The file is classified as: " + result);
        isThisCorrect = new Text("Is this correct?");
        btnYes = new Button("Yes");
        btnYes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                assignedClass = result;
                primaryStage.close();
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        });
        btnNo = new Button("No");
        btnNo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchToSelection();
            }
        });

        layout.add(resultText, 0, 0, 2, 1);
        layout.add(isThisCorrect, 0, 1);
        ComboBox cb = new ComboBox<String>();
        cb.setVisible(false);
        layout.add(cb, 1, 1);
        layout.add(btnYes, 0, 2);
        layout.add(btnNo, 1, 2);

        root.getChildren().add(layout);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void switchToSelection() {
        layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text("Select correct class");
        ComboBox<String> cb = new ComboBox<String>();
        cb.getItems().addAll(NaiveBayesianClassifier.getTrainer().getVocabularyBuilder().getClasses());
        Button btnApply = new Button("Apply");
        btnApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                assignedClass = cb.getSelectionModel().getSelectedItem();
                primaryStage.close();
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        });
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        });

        layout.add(text, 0, 0, 2, 1);
        layout.add(cb, 0, 1);
        layout.add(btnApply, 0, 2);
        layout.add(btnCancel, 1, 2);

        root.getChildren().clear();
        root.getChildren().add(layout);
    }

    public String getAssignedClass() {
        return assignedClass;
    }
}
