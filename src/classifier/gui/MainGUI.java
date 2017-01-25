package classifier.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


/**
 * Created by Wijtse on 25-1-2017.
 */
public class MainGUI extends Application {

    private Stage primaryStage;
    private Group root;
    private Scene scene;
    private FlowPane layout;
    private TextField tfCorpusLocation;
    private Button btnBrowse;
    private Button btnSettings;
    private Button btnStart;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.root = new Group();
        this.layout = new FlowPane();

        primaryStage.setTitle("NaiveBayesianClassifier");
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);

        tfCorpusLocation = new TextField();
        tfCorpusLocation.setTranslateX(10);
        tfCorpusLocation.setTranslateY(50);
        tfCorpusLocation.setPrefWidth(300);

        Text topText = new Text("Select corpus directory");

        btnBrowse = new Button("Browse");
        btnBrowse.setTranslateX(tfCorpusLocation.getTranslateX() + tfCorpusLocation.getWidth() + 5);
        btnBrowse.setTranslateY(tfCorpusLocation.getTranslateY());
        btnBrowse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dc = new DirectoryChooser();
                File selectedDirectory = dc.showDialog(primaryStage);

                if (selectedDirectory != null) {
                    tfCorpusLocation.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });

        btnSettings = new Button("Settings");
        btnSettings.setTranslateX(tfCorpusLocation.getTranslateX());
        btnSettings.setTranslateY(tfCorpusLocation.getTranslateY() + tfCorpusLocation.getHeight() + 10);
        btnSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new SettingsGUI().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnStart = new Button("Start");
        btnStart.setTranslateX(270);
        btnStart.setTranslateY(200);
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                topText.setText("Select a file to classify");
                tfCorpusLocation.setText("");
                btnStart.setVisible(false);
                btnSettings.setVisible(false);
                Button btnClassify = new Button("Classify");
                btnClassify.setTranslateX(-50);
                btnClassify.setTranslateY(100);
                layout.getChildren().add(btnClassify);
                btnBrowse.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fc = new FileChooser();
                        File selectedFile = fc.showOpenDialog(primaryStage);
                        if (selectedFile != null) {
                            tfCorpusLocation.setText(selectedFile.getAbsolutePath());
                        }
                    }
                });
            }
        });
//        btnStart.setTranslateX((primaryStage.getWidth() - btnStart.getWidth() - 10) - btnSettings.getWidth());
//        btnStart.setTranslateY((primaryStage.getHeight() - btnStart.getHeight() - 10) - btnSettings.getHeight() - tfCorpusLocation.getHeight());


        layout.getChildren().add(tfCorpusLocation);
        layout.getChildren().add(btnBrowse);
        layout.getChildren().add(btnSettings);
        layout.getChildren().add(btnStart);
        layout.getChildren().add(topText);
        root.getChildren().add(layout);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
