package elevator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;

public class Controller {


    private ObservableList<Integer> options;
    private Button button = new Button("Dodaj osobę");
    private ComboBox start;
    private ComboBox destination;

    //  TEST
    private Button door = new Button("Otwórz/Zamknij Drzwi");
    private ComboBox dest;

    public void setButtons(ObservableList<Node> children, int floorCount, Elevator elevator, List<Person>[] people) {
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
        layout.getChildren().add(start);
        layout.getChildren().add(label);
        layout.getChildren().add(destination);

        button.setOnAction(event -> {
            int begin = (int) start.getSelectionModel().getSelectedItem();
            int dest = (int) destination.getSelectionModel().getSelectedItem();
            if (begin != dest) {
                people[begin].add(new Person(dest));
            }
        });
        children.add(new Label("Wybierz piętra:"));
        children.add(layout);
        children.add(button);

//        TEST
        children.add(new Label(""));
        children.add(new Label("Przyciski do testowania:"));
        dest = new ComboBox<>(options);
        dest.getSelectionModel().select(0);
        children.add(new Label("Jedź na piętro:"));
        children.add(dest);
        children.add(door);
        dest.setOnAction(event -> elevator.setDestination((int) dest.getSelectionModel().getSelectedItem()));
        door.setOnAction(event -> {
            elevator.openDoor();
            elevator.closeDoor();
        });

    }


}
