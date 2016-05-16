package elevator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Controller {


    private ObservableList<Integer> options;
    private Button button = new Button("Dodaj osobę");
    private ComboBox start;
    private ComboBox destination;

    public void setButtons(ObservableList<Node> children, int floorCount, Elevator elevator) {
        HBox layout = new HBox();
        layout.setAlignment(Pos.BASELINE_CENTER);
        options = FXCollections.observableArrayList();
        for (int i = 0; i <= floorCount; i++)
            options.add(i);

        start = new ComboBox<>(options);
        Label label = new Label(" -> ");
        destination = new ComboBox<>(options);
        layout.getChildren().add(start);
        layout.getChildren().add(label);
        layout.getChildren().add(destination);

        button.setOnAction(event -> {
            elevator.openDoor();
            elevator.closeDoor();

        });
        children.add(new Label("Wybierz piętra:"));
        children.add(layout);
        children.add(button);
    }


}
