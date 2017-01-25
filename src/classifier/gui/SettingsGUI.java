package classifier.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Wijtse on 25-1-2017.
 */
public class SettingsGUI extends Application {

    public static int MAX_FEATURES = 2000;
    public static int SMOOTHING_CONSTANT = 1;
    public static int MIN_FREQUENCY = 1;
    public static int MAX_FREQUENCY = 6000;

    private Stage primaryStage;
    private Group root;
    private Scene scene;
    private GridPane layout;
    private NumberTextField tfMaxFeatures;
    private NumberTextField tfSmoothingConstant;
    private NumberTextField tfMinFrequency;
    private NumberTextField tfMaxFrequency;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.root = new Group();
        this.layout = new GridPane();

        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(5, 10, 5, 10));

        Text txtMaxFeatures = new Text("Max features: ");
        Text txtSmoothingConstant = new Text(("Smoothing constant: "));
        Text txtMinFrequency = new Text("Min frequency: ");
        Text txtMaxFrequency = new Text("Max frequency: ");

        tfMaxFeatures = new NumberTextField();
        tfMaxFeatures.setText("" + MAX_FEATURES);
        tfMaxFeatures.setMaxWidth(100);
        tfSmoothingConstant = new NumberTextField();
        tfSmoothingConstant.setText("" + SMOOTHING_CONSTANT);
        tfSmoothingConstant.setMaxWidth(100);
        tfMinFrequency = new NumberTextField();
        tfMinFrequency.setText("" + MIN_FREQUENCY);
        tfMinFrequency.setMaxWidth(100);
        tfMaxFrequency = new NumberTextField();
        tfMaxFrequency.setText("" + MAX_FREQUENCY);
        tfMaxFrequency.setMaxWidth(100);

        layout.add(txtMaxFeatures, 0, 0);
        layout.add(txtSmoothingConstant, 0, 1);
        layout.add(txtMinFrequency, 0, 2);
        layout.add(txtMaxFrequency, 0, 3);

        layout.add(tfMaxFeatures, 1, 0);
        layout.add(tfSmoothingConstant, 1, 1);
        layout.add(tfMinFrequency, 1, 2);
        layout.add(tfMaxFrequency, 1, 3);

        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");
        btnCancel.setTranslateX(7);
        FlowPane fp = new FlowPane();
        fp.getChildren().add(btnSave);
        fp.getChildren().add(btnCancel);

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateVariables();
                primaryStage.close();
            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });

        layout.add(fp, 1, 4);

        primaryStage.setTitle("Settings");
        primaryStage.setWidth(260);

        root.getChildren().add(layout);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateVariables() {
        MAX_FEATURES = Integer.parseInt(tfMaxFeatures.getText());
        SMOOTHING_CONSTANT = Integer.parseInt(tfSmoothingConstant.getText());
        MIN_FREQUENCY = Integer.parseInt(tfMinFrequency.getText());
        MAX_FREQUENCY = Integer.parseInt(tfMaxFrequency.getText());
    }

}
