package elevator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

/**
 * Created by Samsung on 2016-06-14.
 */
public class MainController {

    protected ObservableList<Integer> options;
    protected Button button = new Button("Dodaj osobę");
    protected Button button2 = new Button("Resetuj");
    protected ComboBox start;
    protected ComboBox destination;
    protected int i = 0;
    protected ComboBox choosenAlgorithm;
    protected ObservableList<Integer> algorithmOptions;
    protected HBox layout = new HBox();

    protected Algorithm algorithm;

    public MainController() {
    }

    public void setButtons(ObservableList<Node> children, int floorCount, ArrayList<Person>[] people) {
        HBox layout = new HBox();
        layout.setAlignment(Pos.BASELINE_CENTER);
        options = FXCollections.observableArrayList();
        for (int i = 0; i <= floorCount; i++)
            options.add(i);
        start = new ComboBox<>(options);
        start.getSelectionModel().select(0);
        Label label = new Label(" -> ");
        destination = new ComboBox<>(options);
        destination.getSelectionModel().select(0);


        algorithmOptions = FXCollections.observableArrayList();
        for (int j = 1; j <= 4; j++)
            algorithmOptions.add(j);
        choosenAlgorithm = new ComboBox<>(algorithmOptions);
        choosenAlgorithm.getSelectionModel().select(0);
        choosenAlgorithm.setOnAction((event) -> {
        });

        layout.getChildren().add(start);
        layout.getChildren().add(label);
        layout.getChildren().add(destination);
        layout.getChildren().add(new Label("                 "));
        layout.getChildren().add(choosenAlgorithm);

        HBox layout2 = new HBox();
        layout2.setAlignment(Pos.BASELINE_CENTER);

        layout2.getChildren().add(button);
        layout2.getChildren().add(new Label("               "));
        layout2.getChildren().add(button2);

        children.add(new Label("                               Wybierz piętra:              Wybierz algorytm:"));
        children.add(new Label(""));
        children.add(layout);
        children.add(new Label(""));
        children.add(layout2);


        button.setOnAction(event -> {
            int begin = (int) start.getSelectionModel().getSelectedItem();
            int dest = (int) destination.getSelectionModel().getSelectedItem();
            if (algorithm == null) {
                setAlgorithm();
            }
            algorithm.addPerson(begin, dest, Main.elevator, people);
        });

        button2.setOnAction(event -> {
            for (ArrayList<Person> aPeople : people) {
                aPeople.clear();
            }
            setAlgorithm();
            algorithm = null;
            Main.elevator = new Elevator(Main.floorsCount, people, this);
            Main.updateStats();
        });
    }


    private void setAlgorithm() {
        int algorithmNumber = choosenAlgorithm.getSelectionModel().getSelectedIndex();
        switch (algorithmNumber) {
            case 0:
                algorithm = new Algorithm1();
                break;
            case 1:
                algorithm = new Algorithm2();
                break;
            case 2:
                algorithm = new Algorithm3();
                break;
            case 3:
                algorithm = new Algorithm4();
                break;
        }
    }
}
