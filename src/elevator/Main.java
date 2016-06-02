package elevator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    Elevator elevator;
    Controller controller = new Controller();
    GraphicsContext gc;
    Animation animation;
    int height = 140;
    private int floorsCount = 4;
    private ArrayList<Person>[] people = new ArrayList[floorsCount + 1];

    {
        for (int i = 0; i < people.length; i++) {
            people[i] = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        elevator = new Elevator(floorsCount, people);

        primaryStage.setTitle("Elevator");
        BorderPane main = new BorderPane();
        Pane info = new StackPane();
        Label label = new Label("Symulator Windy");
        label.setPrefHeight(50);
        label.setFont(Font.font(24));
        info.getChildren().add(label);
        main.setTop(info);

        VBox buttons = new VBox();
        buttons.setPrefWidth(200);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        main.setLeft(buttons);
        buttons.setPadding(new Insets(10, 10, 10, 10));

        Canvas canvas = new Canvas(600, (floorsCount + 1) * (height + 26));
        gc = canvas.getGraphicsContext2D();
        ScrollPane sp = new ScrollPane();
        sp.setContent(canvas);
        sp.setPadding(new Insets(0, 0, 0, 0));
        main.setCenter(sp);

        controller.setButtons(buttons.getChildren(), floorsCount, elevator, people);
        primaryStage.setScene(new Scene(main, 600 + 300, (floorsCount + 1) * (height + 26) + 70));
        primaryStage.show();
        animation = new Animation();
        animation.start();

//        people[0].add(new Person(1));
//        elevator.addPerson(new Person(1));
//        elevator.addPerson(new Person(1));
//        elevator.addPerson(new Person(1));
    }


    private void drawElevators(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        int yTranslate = 0, xTranslate;
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(491, 1, 491, (floorsCount + 1) * 165);
        gc.strokeLine(599, 1, 599, (floorsCount + 1) * 165);
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
//        gc.fillOval(10, 60, 30, 30);
//        gc.strokeOval(60, 60, 30, 30);
//        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
//        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
//        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
//        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
//        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
//        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
//        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
//        gc.fillPolygon(new double[]{10, 40, 10, 40},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolygon(new double[]{60, 90, 60, 90},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolyline(new double[]{110, 140, 110, 140},
//                new double[]{210, 210, 240, 240}, 4);
    }


    class Animation extends AnimationTimer {

        @Override
        public void handle(long now) {
            elevator.update();
            drawElevators(gc);
        }
    }

}
