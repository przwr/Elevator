package elevator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    protected static ObservableList<Stat> data;
    static Elevator elevator;
    static int height = 140;
    static int floorsCount = 5;
    static ArrayList<Person>[] people = new ArrayList[floorsCount + 1];
    static TableView table;
    static Label avgTravel;
    static Label avgWaiting;
    static Label traveledFloors;
    static Label stopsCount;
    static int TIME_FACTOR = 2;

    static {
        for (int i = 0; i < people.length; i++) {
            people[i] = new ArrayList<>();
        }
    }

    MainController controller = new MainController();
    GraphicsContext gc;
    Animation animation;

    public static void main(String[] args) {
        launch(args);
    }

    public static void updateStats() {
        data.clear();
        ArrayList<Person> floor;
        Integer counter = 1;
        float avgTime = 0;
        float avgWait = 0;
        for (int i = 0; i < people.length; i++) {
            floor = people[i];
            for (Person person : floor) {
                data.add(new Stat(counter, person.source, person.destination, person.getRunTimeNice(), person.getWaitTimeNice()));
                avgTime += person.getRunTime();
                avgWait += person.getWaitTime();
                counter++;
            }
        }
        avgTime /= (counter - 1);
        avgWait /= (counter - 1);
        avgTime = ((int) (avgTime * 100)) / 100f;
        avgWait = ((int) (avgWait * 100)) / 100f;
        if (avgTime <= 0) {
            avgTime = 0;
        }
        if (avgWait <= 0) {
            avgWait = 0;
        }
        avgTravel.setText("Średni czas podróży [s]: " + avgTime * TIME_FACTOR);
        avgWaiting.setText("Średni czas podróży [s]: " + avgWait * TIME_FACTOR);
        stopsCount.setText("Ilość postojów: " + (elevator.stops >= 0 ? elevator.stops : 0));
        traveledFloors.setText("Ilość przejechanych pięter: " + elevator.floorsTraveled);
        table.setItems(data);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        elevator = new Elevator(floorsCount, people, controller);

        primaryStage.setTitle("Elevator");
        BorderPane main = new BorderPane();
        Pane info = new StackPane();
        Label label = new Label("Symulator Windy");
        label.setPrefHeight(50);
        label.setFont(Font.font(24));
        info.getChildren().add(label);
        main.setTop(info);

        VBox buttons = new VBox();
        buttons.setPrefWidth(500);
        main.setLeft(buttons);
        buttons.setPadding(new Insets(10, 10, 10, 10));

        Canvas canvas = new Canvas(600, (floorsCount + 1) * (height + 26));
        gc = canvas.getGraphicsContext2D();
        ScrollPane sp = new ScrollPane();
        sp.setContent(canvas);
        sp.setPadding(new Insets(0, 0, 0, 0));
        main.setCenter(sp);

        controller.setButtons(buttons.getChildren(), floorsCount, people);
        primaryStage.setScene(new Scene(main, 600 + 520, (floorsCount + 1) * (height + 26) + 70));
        primaryStage.show();
        animation = new Animation();
        animation.start();

        table = new TableView();
        data = FXCollections.observableArrayList();

        TableColumn lp = new TableColumn("Lp.");
        lp.setCellValueFactory(new PropertyValueFactory<Stat, Integer>("lp"));
        lp.setStyle("-fx-alignment: CENTER;");

        TableColumn from = new TableColumn("Z");
        from.setCellValueFactory(new PropertyValueFactory<Stat, Integer>("source"));
        from.setStyle("-fx-alignment: CENTER;");

        TableColumn to = new TableColumn("Do");
        to.setCellValueFactory(new PropertyValueFactory<Stat, Integer>("destination"));
        to.setStyle("-fx-alignment: CENTER;");

        TableColumn total = new TableColumn("Podróż (s)");
        total.setCellValueFactory(new PropertyValueFactory<Stat, Integer>("timeTotal"));
        total.setStyle("-fx-alignment: CENTER;");

        TableColumn inElevator = new TableColumn("Oczekiwanie (s)");
        inElevator.setCellValueFactory(new PropertyValueFactory<Stat, Integer>("timeWaiting"));
        inElevator.setStyle("-fx-alignment: CENTER;");

        lp.setPrefWidth(50);
        from.setPrefWidth(80);
        to.setPrefWidth(80);
        total.setPrefWidth(130);
        inElevator.setPrefWidth(130);
        table.setPrefWidth(500);
        table.setPrefHeight((floorsCount + 1) * 170 - 250);
        table.setFixedCellSize(24);
        table.getColumns().addAll(lp, from, to, total, inElevator);


        avgTravel = new Label("Średni czas podróży: ");
        avgWaiting = new Label("Średni czas oczekiwania: ");
        traveledFloors = new Label("Ilość przejechanych pięter: ");
        stopsCount = new Label("Ilość postojów: ");


        table.setItems(data);
        buttons.getChildren().add(new Label(""));
        buttons.getChildren().add(avgTravel);
        buttons.getChildren().add(avgWaiting);
        buttons.getChildren().add(traveledFloors);
        buttons.getChildren().add(stopsCount);
        buttons.getChildren().add(new Label(" "));
        buttons.getChildren().add(table);

    }

    private void drawElevators(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        int yTranslate = 0, xTranslate;
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(491, 1, 491, (floorsCount) * 200);
        gc.strokeLine(599, 1, 599, (floorsCount) * 200);
        gc.setLineWidth(6);
        for (int i = 0; i <= floorsCount; i++) {
            gc.strokeLine(0, 3, i == 0 ? 600 : 489, 3);
            float cf = elevator.getNextFloorPercent() * (height + 25);
            gc.translate(0, 20);
            yTranslate += 20;
            if (elevator.getCurrentFloor() == floorsCount - i) {
                gc.fillPolygon(new double[]{534, 544, 539},
                        new double[]{-8 - cf, -8 - cf, 2 - cf}, 3);
                gc.fillPolygon(new double[]{545, 555, 550},
                        new double[]{2 - cf, 2 - cf, -8 - cf}, 3);
                gc.strokeRoundRect(495, 8 - cf, 100, height, 6, 6);
                int shift = 12;
                for (int s = 2; s < elevator.getPeople().size(); s++) {
                    shift /= 3;
                }
                if (elevator.getPeople().size() >= 6) {
                    shift = -1;
                }
                gc.translate(498 + shift, 0);
                xTranslate = 498 + shift;
                for (Person person : elevator.getPeople()) {
                    gc.setFont(Font.font(16));
                    gc.fillText(" " + person.destination, 3, 64 - cf);
                    gc.fillOval(7, 75 - cf, 13, 13);
                    gc.fillRoundRect(0, 90 - cf, 26, 30, 10, 10);
                    gc.fillRoundRect(4, 115 - cf, 8, 30, 7, 7);
                    gc.fillRoundRect(14, 115 - cf, 8, 30, 7, 7);
                    if (elevator.getPeople().size() < 5) {
                        gc.translate(90 / elevator.getPeople().size(), 0);
                        xTranslate += 90 / elevator.getPeople().size();
                    } else {
                        gc.translate(85 / elevator.getPeople().size(), 0);
                        xTranslate += 85 / elevator.getPeople().size();
                    }
                }
                gc.translate(-xTranslate, 0);
                gc.setLineWidth(2);
                gc.strokeLine(544 - elevator.getOpenPercent() * 50, 10 - cf, 544 - elevator.getOpenPercent() * 50, height + 6 - cf);
                gc.strokeLine(546 + elevator.getOpenPercent() * 50, 10 - cf, 546 + elevator.getOpenPercent() * 50, height + 6 - cf);
                gc.setGlobalAlpha(0.5);
                gc.setFill(Color.GRAY);
                gc.fillRect(498, 11 - cf, (1 - elevator.getOpenPercent()) * 50 - 5, height - 6);
                gc.fillRect(547 + elevator.getOpenPercent() * 50, 11 - cf, (1 - elevator.getOpenPercent()) * 50 - 5, height - 6);
                gc.setFill(Color.BLACK);
                gc.setGlobalAlpha(1);
                gc.setLineWidth(6);
            }
            gc.translate(454, 0);
            xTranslate = 454;
            for (Person person : people[floorsCount - i]) {
                if (person.destination == floorsCount - i) {
                    gc.setFill(Color.GRAY);
                    gc.setFont(Font.font(16));
                    gc.fillText(" " + person.getRunTimeLabel(), -3, 64);
                } else {
                    gc.setFont(Font.font(16));
                    gc.fillText(" " + person.destination, 3, 64);
                }
                gc.fillOval(7, 75, 13, 13);
                gc.fillRoundRect(0, 90, 26, 30, 10, 10);
                gc.fillRoundRect(4, 115, 8, 30, 7, 7);
                gc.fillRoundRect(14, 115, 8, 30, 7, 7);
                gc.translate(-30, 0);
                xTranslate -= 30;
                gc.setFill(Color.BLACK);
            }
            gc.setFont(Font.font(24));
            gc.translate(-xTranslate, 0);
            gc.fillText("P" + (floorsCount - i), 2, 8);
            gc.translate(0, height + 5);
            yTranslate += height + 5;
        }
        gc.strokeLine(0, 3, 600, 3);
        gc.translate(0, -yTranslate);
    }

    public static class Stat {
        private final SimpleIntegerProperty lp;
        private final SimpleIntegerProperty source;
        private final SimpleIntegerProperty destination;
        private final SimpleStringProperty timeTotal;
        private final SimpleStringProperty timeWaiting;

        public Stat(int fName, int source, int destination, String timeTotal, String timeWaiting) {
            this.lp = new SimpleIntegerProperty(fName);
            this.source = new SimpleIntegerProperty(source);
            this.destination = new SimpleIntegerProperty(destination);
            this.timeTotal = new SimpleStringProperty(timeTotal);
            this.timeWaiting = new SimpleStringProperty(timeWaiting);
        }

        public int getLp() {
            return lp.get();
        }

        public void setLp(int lp) {
            this.lp.set(lp);
        }

        public int getSource() {
            return source.get();
        }

        public void setSource(int source) {
            this.source.set(source);
        }

        public int getDestination() {
            return destination.get();
        }

        public void setDestination(int destination) {
            this.destination.set(destination);
        }

        public String getTimeTotal() {
            return timeTotal.get();
        }

        public void setTimeTotal(String timeTotal) {
            this.timeTotal.set(timeTotal);
        }

        public String getTimeWaiting() {
            return timeWaiting.get();
        }

        public void setTimeWaiting(String timeWaiting) {
            this.timeWaiting.set(timeWaiting);
        }
    }

    class Animation extends AnimationTimer {

        @Override
        public void handle(long now) {
            elevator.update();
            drawElevators(gc);
        }
    }
}
