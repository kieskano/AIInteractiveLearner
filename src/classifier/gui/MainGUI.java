package classifier.gui;

import classifier.controller.Classifier;
import classifier.controller.NaiveBayesianClassifier;
import classifier.controller.Trainer;
import classifier.controller.Updater;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;


/**
 * Created by Wijtse on 25-1-2017.
 */
public class MainGUI extends Application {

    public enum State {
        UNTRAINED, TRAINING, TRAINED, CLASSIFYING;

        @Override
        public String toString() {
            switch (this) {
                case UNTRAINED:
                    return "State: Untrained  ";
                case TRAINING:
                    return "State: Training     ";
                case TRAINED:
                    return "State: Trained         ";
                case CLASSIFYING:
                    return "State: Classifying";
                default:
                    return "";
            }
        }
    }

    private Stage primaryStage;
    private Group root;
    private Scene scene;
    private FlowPane layout;
    private TextField tfCorpusLocation;
    private Button btnBrowse;
    private Button btnSettings;
    private Button btnStart;
    private Button btnClassify;
    private Button btnBack;
    private Text topText;
    private Text statusText;
    private String corpusDirPath;
    private String filePath;
    private State state;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.root = new Group();
        this.layout = new FlowPane();
        state = State.UNTRAINED;


        primaryStage.setTitle("NaiveBayesianClassifier");
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);

        tfCorpusLocation = new TextField();//new TextField("C:\\Users\\Wijtse\\IdeaProjects\\AIInteractiveLearner\\blogs");
        tfCorpusLocation.setTranslateX(10);
        tfCorpusLocation.setTranslateY(50);
        tfCorpusLocation.setPrefWidth(300);

        topText = new Text("Select corpus directory");

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
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(primaryStage.getScene().getWindow());
                    new SettingsGUI().start(stage);
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
                File file = new File(tfCorpusLocation.getText());
                if (file.exists() && file.isDirectory()) {
                    corpusDirPath = tfCorpusLocation.getText();
                    state = State.TRAINING;
                    statusText.setText(state.toString());
                    disableEverything();
                    new Thread(){
                        @Override
                        public void run() {
                            train();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    enableEveryThing();
                                    switchToClassifyScreen();
                                }
                            });
                        }
                    }.start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Could not load directory");
                    alert.setContentText("Make sure the given path is a correct directory!");
                    alert.show();
                }
            }
        });

        btnClassify = new Button("Classify");
        btnClassify.setTranslateX(-50);
        btnClassify.setTranslateY(100);
        btnClassify.setVisible(false);
        btnClassify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = new File(tfCorpusLocation.getText());
                if (file.exists() && file.isFile()) {
                    filePath = tfCorpusLocation.getText();
                    state = State.CLASSIFYING;
                    statusText.setText(state.toString());
                    disableEverything();
                    new Thread() {
                        public void run() {
                            String result = classify();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    enableEveryThing();
                                }
                            });
                            Object monitor = new Object();
                            ResultGUI rg = new ResultGUI(result, monitor);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        rg.start(new Stage());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            synchronized (monitor) {
                                try {
                                    monitor.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (rg.getAssignedClass() != null) {
                                NaiveBayesianClassifier.getUpdater().copyToTrainingSet(new File(filePath), rg.getAssignedClass());
                            }
                        }
                    }.start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Could not load file");
                    alert.setContentText("Make sure the given path is a correct file!");
                    alert.show();
                }
            }
        });

        btnBack = new Button("Back");
        btnBack.setTranslateX(-262);
        btnBack.setTranslateY(200);
        btnBack.setVisible(false);
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchToStartScreen();
            }
        });

        statusText = new Text(state.toString());
        statusText.setTranslateX(150);
        statusText.setTranslateY(180);
        statusText.minWidth(300);

        layout.getChildren().add(tfCorpusLocation);
        layout.getChildren().add(btnBrowse);
        layout.getChildren().add(btnSettings);
        layout.getChildren().add(btnStart);
        layout.getChildren().add(topText);
        layout.getChildren().add(btnClassify);
        layout.getChildren().add(btnBack);
        layout.getChildren().add(statusText);
        root.getChildren().add(layout);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String classify() {
        String result = NaiveBayesianClassifier.getClassifier().classify(new File(filePath));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                state = State.TRAINED;
                statusText.setText(state.toString());
            }
        });
        return result;
    }

    private void train() {
        NaiveBayesianClassifier.setDirectory(null);
        NaiveBayesianClassifier.setAmountOfFeatures(SettingsGUI.MAX_FEATURES);
        NaiveBayesianClassifier.setSmoothingConstant(SettingsGUI.SMOOTHING_CONSTANT);
        NaiveBayesianClassifier.setMinFreq(SettingsGUI.MIN_FREQUENCY);
        NaiveBayesianClassifier.setMaxFreq(SettingsGUI.MAX_FREQUENCY);

        NaiveBayesianClassifier.setTrainer(new Trainer());
        NaiveBayesianClassifier.setClassifier(new Classifier());
        NaiveBayesianClassifier.setUpdater(new Updater(corpusDirPath));

        NaiveBayesianClassifier.getTrainer().train(corpusDirPath);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                state = State.TRAINED;
                statusText.setText(state.toString());
            }
        });
    }

    private void disableEverything() {
        scene.setCursor(Cursor.WAIT);
        for(Node node : layout.getChildren()) {
            if (!(node instanceof Text)) {
                node.setDisable(true);
            }
        }
    }

    private void enableEveryThing() {
        scene.setCursor(Cursor.DEFAULT);
        for(Node node : layout.getChildren()) {
            if (!(node instanceof Text)) {
                node.setDisable(false);
            }
        }
    }

    public void switchToClassifyScreen() {
        topText.setText("Select a file to classify");
        tfCorpusLocation.setText("");
        btnStart.setVisible(false);
        btnBack.setVisible(true);
        btnSettings.setVisible(false);
        btnClassify.setVisible(true);
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

    public void switchToStartScreen() {
        topText.setText("Select corpus directory");
        tfCorpusLocation.setText(corpusDirPath);
        btnStart.setVisible(true);
        btnBack.setVisible(false);
        btnSettings.setVisible(true);
        btnClassify.setVisible(false);
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
    }

    public static void main(String[] args) {
        launch();
    }
}
