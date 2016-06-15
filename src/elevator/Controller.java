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
    private int i = 0;
    //  TEST
    private Button door = new Button("Otwórz/Zamknij Drzwi");
    private ComboBox dest;

    public void updateOpen(Elevator elevator) {
        //przed otworzeniem drzwi
        if (elevator.goingUp2 && !elevator.noDest) {
            for (int b : elevator.aList)
                if (!elevator.goingDownQueue.contains(b))
                    elevator.goingDownQueue.add(b);
            elevator.aList.clear();
//            if (elevator.a != -1) {
//                if (!elevator.goingDownQueue.contains(elevator.a))
//                    elevator.goingDownQueue.add(elevator.a);
//                elevator.a = -1;
//            }
        }
    }

    public void update(int state, Elevator elevator) {
        //po otworzeniu drzwi
        // 0- otwarte drzwi, 1 - zamkniete drzwi
        //goingUp = !goingUp;
        // jeżeli jedzie teraz do góry
        System.out.println("update na początku");

        //goingUpQueue- kolejka gdy winda jedzie na dół i nie zatrzymuje się na wyższych niż obecne piętro
        //queueUpCopied - flaga czy goingUpQueue została już skopiowana
        int previousDestination = elevator.getDestination();
        if (elevator.goingDownQueue.isEmpty() && elevator.goingUpQueue.isEmpty() && elevator.goingDownQueue2.isEmpty() && elevator.goingUpQueue2.isEmpty()) {
            elevator.noDest = true;
            //elevator.a = -1;
            elevator.aList.clear();
            elevator.queueUpEnded = false;
            elevator.queueDownEnded = false;
        }
        if (!elevator.goingDownQueue.isEmpty() && !elevator.queueDownEnded) {
            if (elevator.goingDownQueue.peek() == elevator.getCurrentFloor2())
                elevator.goingDownQueue.poll();
        }
        if (!elevator.queueDownEnded && !elevator.goingDownQueue.isEmpty()) {
            if (!elevator.goingDownQueue.isEmpty()) {
                elevator.setDestination(elevator.goingDownQueue.poll());
                elevator.goingUp2 = previousDestination <= elevator.getDestination();
                elevator.goingUp = false;
            }

        } else if (elevator.noDest) {
            if (elevator.getCurrentFloor() > 0) {
                elevator.goingUp = true;
                elevator.setDestination(elevator.getCurrentFloor() - 1);
                elevator.goingUp2 = previousDestination <= elevator.getDestination();
            } else {
                elevator.goingUp = true;
                elevator.setDestination(elevator.getCurrentFloor() + 1);
                elevator.goingUp2 = previousDestination <= elevator.getDestination();
            }
        } else {
            elevator.queueDownEnded = true;
            if (!elevator.goingUpQueue.isEmpty()) {
                elevator.goingUp = true;
                if (elevator.goingUpQueue.peek() == elevator.getCurrentFloor2())
                    elevator.goingUpQueue.poll();
                if (!elevator.goingUpQueue.isEmpty()) {
                    elevator.setDestination(elevator.goingUpQueue.poll());
                    elevator.queueUpEnded = true;
                    elevator.goingUp2 = previousDestination <= elevator.getDestination();
                }
            } else {
                if (elevator.queueUpEnded) {
                    elevator.goingUp = false;
                    if (!elevator.goingDownQueue2.isEmpty()) {
                        if (elevator.goingDownQueue2.peek() == elevator.getCurrentFloor2())
                            elevator.goingDownQueue2.poll();
                        if (!elevator.goingDownQueue2.isEmpty()) {
                            elevator.setDestination(elevator.goingDownQueue2.poll());
                            elevator.queueDown2Ended = true;
                            elevator.goingUp2 = previousDestination <= elevator.getDestination();
                        }
                    } else {
//                        elevator.queueUpEnded = false;
//                        elevator.queueDownEnded = false;
//                        update(1, elevator);
                        if (elevator.queueDown2Ended) {
                            if (!elevator.goingUpQueue2.isEmpty()) {
                                if (elevator.goingUpQueue2.peek() == elevator.getCurrentFloor2())
                                    elevator.goingUpQueue2.poll();
                                if (!elevator.goingUpQueue2.isEmpty()) {
                                    elevator.setDestination(elevator.goingUpQueue2.poll());
                                    elevator.goingUp2 = previousDestination <= elevator.getDestination();
                                }
                            } else {
                                elevator.queueDown2Ended = false;
                                elevator.queueUpEnded = false;
                                elevator.queueDownEnded = false;
                                update(1, elevator);
                            }
                        } else {
                            elevator.queueUpEnded = false;
                            elevator.queueDownEnded = false;
                            update(1, elevator);
                        }
                    }
                } else {
                    elevator.queueDownEnded = false;
                    update(1, elevator);
                }

//        elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
            }
            //elevator.goingUp = !elevator.goingUp;
            //System.out.println("update na końcu");

        }
    }

    public void printQueues(Elevator elevator) {
        System.out.println("GoingUpQueue: " + elevator.goingUpQueue);
        System.out.println("GoingDownQueue: " + elevator.goingDownQueue);
        System.out.println("GoingDownQueue2: " + elevator.goingDownQueue2);
        //System.out.println("queueUpCopied: " + elevator.queueUpCopied);
        System.out.println("goingUp: " + elevator.goingUp);
        System.out.println("destination: " + elevator.getDestination());
    }

    public void setButtons(ObservableList<Node> children, int floorCount, Elevator elevator, List<Person>[]
            people) {
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
                    System.out.println("On action na początku");
                    printQueues(elevator);
                    int begin = (int) start.getSelectionModel().getSelectedItem();
                    int dest = (int) destination.getSelectionModel().getSelectedItem();
                    int previousDestination = elevator.getDestination();
                    if (begin != dest) {
                        people[begin].add(new Person(begin, dest));
                        // kiedy nie było zlecenia
                        if (elevator.noDest) {
                            elevator.noDest = false;
                            if (begin > dest) {
                                if (elevator.getCurrentFloor2() != begin) {
                                    elevator.setDestination(begin);
                                    elevator.goingUp2 = previousDestination <= elevator.getDestination();
                                    //elevator.a = dest;
                                    elevator.aList.add(dest);
                                } else {
                                    //elevator.a = dest;
                                    elevator.aList.add(dest);
                                    elevator.openDoor();
                                }
//                                if (!elevator.goingDownQueue.contains(elevator.getDestination()))
//                                    elevator.goingDownQueue.add(elevator.getDestination());
//                                elevator.setDestination(begin);
//                                if (elevator.a != -1 && !elevator.goingDownQueue.contains(elevator.a))
//                                    elevator.goingUpQueue.add(elevator.a);
                            } else {
                                if (elevator.getCurrentFloor2() != begin) {
                                    elevator.setDestination(begin);
                                    elevator.goingUp2 = previousDestination <= elevator.getDestination();
                                    // a do dest bo może się zmienic jeszce najwyśzy
                                    //elevator.a = dest;
                                    elevator.aList.add(dest);
                                } else {
                                    //elevator.a = dest;
                                    elevator.aList.add(dest);
                                    elevator.openDoor();
                                }
                            }
                        } else if (elevator.goingUp2) {
                            // przyjmuje tylko tych którzy jadą z wyższych pięter
                            if (!elevator.queueDownEnded) {
                                if (begin > elevator.getDestination()) {
                                    elevator.noDest = false;
                                    if (!elevator.goingDownQueue.contains(elevator.getDestination()))
                                        elevator.goingDownQueue.add(elevator.getDestination());
                                    if (elevator.getCurrentFloor2() != begin) {
//                                    if (elevator.a != -1 && !elevator.goingDownQueue.contains(elevator.a))
//                                        elevator.goingDownQueue.add(elevator.getDestination());
                                        if (!elevator.goingDownQueue.contains(elevator.getDestination()))
                                            elevator.goingDownQueue.add(elevator.getDestination());
                                        elevator.setDestination(begin);
                                        elevator.goingUp2 = previousDestination <= elevator.getDestination();

//                                    if (elevator.a != -1 && !elevator.goingUpQueue.contains(elevator.a))
//                                        elevator.goingUpQueue.add(elevator.a);
//                                    elevator.a = dest;
                                        for (int b : elevator.aList) {
                                            if (!elevator.goingUpQueue.contains(b)) {
                                                elevator.goingUpQueue.add(b);
                                            }
                                            elevator.aList.remove(b);
                                        }
                                        elevator.aList.add(dest);
                                    } else {
                                        if (!elevator.goingDownQueue.contains(elevator.getDestination()))
                                            elevator.goingDownQueue.add(elevator.getDestination());
                                        for (int b : elevator.aList) {
                                            if (!elevator.goingUpQueue.contains(b)) {
                                                elevator.goingUpQueue.add(b);
                                            }
                                            elevator.aList.remove(b);
                                        }
                                        elevator.aList.add(dest);
//                                    if (elevator.a != -1 && !elevator.goingUpQueue.contains(elevator.a))
//                                        elevator.goingUpQueue.add(elevator.a);
//                                    elevator.a = dest;
                                        elevator.openDoor();
                                    }


                            /*if (!elevator.goingDownQueue.contains(dest))
                                elevator.goingDownQueue.add(dest);*/

                                } else if (begin == elevator.getDestination()) {
                                /*if (!elevator.goingDownQueue.contains(dest))
                                    elevator.goingDownQueue.add(dest);
                                if (begin == elevator.getCurrentFloor2())
                                    elevator.openDoor();*/
                                    if (!elevator.aList.contains(dest))
                                        elevator.aList.add(dest);
                                    if (begin == elevator.getCurrentFloor2()) {
                                        if (!elevator.goingDownQueue.contains(elevator.getDestination()))
                                            elevator.goingDownQueue.add(elevator.getDestination());
                                        elevator.openDoor();
                                    }
                                } else {
                                    if (dest < begin) {
                                        if (!elevator.goingDownQueue.contains(begin))
                                            elevator.goingDownQueue.add(begin);
                                        if (!elevator.goingDownQueue.contains(dest))
                                            elevator.goingDownQueue.add(dest);
                                    } else {
                                        if (!elevator.goingDownQueue.contains(begin))
                                            elevator.goingDownQueue.add(begin);
                                        if (!elevator.goingUpQueue.contains(dest))
                                            elevator.goingUpQueue.add(dest);
                                    }
                                }
                            } else {
                                // jedzie w górę zgodnie z goingUpQueue
                                if (begin >= elevator.getCurrentFloor2()) {
                                    if (dest > begin) {
                                        if (!elevator.goingUpQueue.contains(dest))
                                            elevator.goingUpQueue.add(dest);
                                        if (begin == elevator.getCurrentFloor2()) {
                                            if (!elevator.goingUpQueue.contains(elevator.getDestination()))
                                                elevator.goingUpQueue.add(elevator.getDestination());
                                            elevator.openDoor();
                                        } else if (!elevator.goingUpQueue.contains(begin))
                                            elevator.goingUpQueue.add(begin);
                                    } else {
                                        if (begin == elevator.getCurrentFloor2()) {
                                            if (!elevator.goingDownQueue.contains(elevator.getDestination()))
                                                elevator.goingDownQueue.add(elevator.getDestination());
                                            elevator.openDoor();
                                        } else if (!elevator.goingUpQueue.contains(begin))
                                            elevator.goingUpQueue.add(begin);
                                        if (!elevator.goingDownQueue2.contains(dest))
                                            elevator.goingDownQueue2.add(dest);
                                    }

                                } else {
                                    if (dest < begin) {
                                        if (!elevator.goingDownQueue2.contains(dest))
                                            elevator.goingDownQueue2.add(dest);
                                        if (!elevator.goingDownQueue2.contains(begin))
                                            elevator.goingDownQueue2.add(begin);
                                    } else {
                                        if (!elevator.goingDownQueue2.contains(begin))
                                            elevator.goingDownQueue2.add(begin);
                                        if (!elevator.goingUpQueue2.contains(dest))
                                            elevator.goingUpQueue2.add(dest);
                                    }
                                }
                            }
                        } else {
                            if (begin <= elevator.getCurrentFloor2()) {
                                if (dest < begin) {
                                    if (!elevator.goingDownQueue.contains(dest))
                                        elevator.goingDownQueue.add(dest);
                                    if (begin == elevator.getCurrentFloor2()) {
                                        if (!elevator.goingDownQueue.contains(elevator.getDestination()))
                                            elevator.goingDownQueue.add(elevator.getDestination());
                                        elevator.openDoor();
                                    } else if (!elevator.goingDownQueue.contains(begin))
                                        elevator.goingDownQueue.add(begin);
                                } else {
                                    if (!elevator.goingUpQueue.contains(dest))
                                        elevator.goingUpQueue.add(dest);
                                    if (begin == elevator.getCurrentFloor2()) {
                                        if (!elevator.goingDownQueue.contains(elevator.getDestination()))
                                            elevator.goingDownQueue.add(elevator.getDestination());
                                        elevator.openDoor();
                                    } else if (!elevator.goingDownQueue.contains(begin))
                                        elevator.goingDownQueue.add(begin);
                                }
                            } else {
                                // tutaj może potrzebny if
                                if (!elevator.goingUpQueue.contains(begin))
                                    elevator.goingUpQueue.add(begin);
                                if (!elevator.goingDownQueue2.contains(dest))
                                    elevator.goingDownQueue2.add(dest);
                            }
                        }

                        System.out.println("On action na końcu");
                        printQueues(elevator);
                    }


//                    elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
                }

        );
        children.add(new

                Label("Wybierz piętra:")

        );
        children.add(layout);
        children.add(button);

//        TEST
        children.add(new

                Label("")

        );
        children.add(new

                Label("Przyciski do testowania:")

        );
        dest = new ComboBox<>(options);
        dest.getSelectionModel().

                select(0);

        children.add(new

                Label("Jedź na piętro:")

        );
        children.add(dest);
        children.add(door);
        dest.setOnAction(event -> elevator.setDestination((int) dest.getSelectionModel().

                getSelectedItem()

        ));
        door.setOnAction(event ->

                {
                    elevator.openDoor();
                    elevator.closeDoor();
                }

        );

    }


}
