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

public class Controller2 {


    private ObservableList<Integer> options;
    private Button button = new Button("Dodaj osobę");
    private ComboBox start;
    private ComboBox destination;
    private int i = 0;
    //  TEST
    private Button door = new Button("Otwórz/Zamknij Drzwi");
    private ComboBox dest;

//    public void updateOpen(Elevator elevator) {
//        //przed otworzenie drzwi
//        if (elevator.goingUp2 && !elevator.noDest) {
//            for (int b : elevator.aList)
//                if (!elevator.goingDownQueue.contains(b))
//                    elevator.goingDownQueue.add(b);
//            elevator.aList.clear();
////            if (elevator.a != -1) {
////                if (!elevator.goingDownQueue.contains(elevator.a))
////                    elevator.goingDownQueue.add(elevator.a);
////                elevator.a = -1;
////            }
//        }
//    }


    public void update(int state, Elevator elevator) {
        //po otworzeniu drzwi
        // 0- otwarte drzwi, 1 - zamkniete drzwi
        //goingUp = !goingUp;
        // jeżeli jedzie teraz do góry
        System.out.println("update na początku");

        //goingUpQueue- kolejka gdy winda jedzie na dół i nie zatrzymuje się na wyższych niż obecne piętro
        //queueUpCopied - flaga czy goingUpQueue została już skopiowana

        //<---------------------------------------------------------------------------------------->

        int previousDestination = elevator.getDestination();
        if (elevator.downQueue.isEmpty() && elevator.upQueue.isEmpty() && elevator.downQueue2.isEmpty() && elevator.upQueue2.isEmpty()) {
            elevator.upQueueDone = false;
            elevator.downQueueDone = false;
            elevator.upQueue2Done = false;
            elevator.downQueue2Done = false;
            elevator.ifNoDest = true;
            if (elevator.goingUp2) {
                if (elevator.getCurrentFloor2() < 5)
                    elevator.setDestination(5);
                else {
                    elevator.setDestination(0);
                    elevator.goingUp2 = false;
                }
            } else {
                if (elevator.getCurrentFloor2() > 0)
                    elevator.setDestination(0);
                else {
                    elevator.setDestination(5);
                    elevator.goingUp2 = true;
                }
            }
        } else {
            if (!elevator.upQueueDone) {
                if (!elevator.upQueue.isEmpty()) {
                    if (elevator.upQueue.peek() == elevator.getCurrentFloor2() && elevator.ifNoDest) {
                        elevator.upQueue.poll();
                        if (elevator.upQueue.isEmpty())
                            elevator.upQueueDone = true;
                    }
                    if (!elevator.upQueue.isEmpty()) {
                        elevator.setDestination(elevator.upQueue.poll());
                        elevator.ifNoDest = false;
                        //elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
                        elevator.goingUp2 = true;
                    } else {
                        elevator.upQueueDone = true;
                    }
                    elevator.upQueueChecked = true;
                } else {
                    elevator.upQueueDone = true;
                }
            }
            if (!elevator.downQueueDone && elevator.upQueueDone) {
                if (!elevator.downQueue.isEmpty()) {
                    if (elevator.downQueue.peek() == elevator.getCurrentFloor2() && elevator.ifNoDest) {
                        elevator.downQueue.poll();
                        if (elevator.downQueue.isEmpty())
                            elevator.downQueueDone = true;
                    }
                    if (!elevator.downQueue.isEmpty()) {
                        elevator.setDestination(elevator.downQueue.poll());
                        elevator.ifNoDest = false;
                        elevator.goingUp2 = false;
                        //elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
                    } else {
                        elevator.downQueueDone = true;
                    }
                    elevator.downQueueChecked = true;
                } else {
                    elevator.downQueueDone = true;
                }
            }
            if (!elevator.upQueue2Done && elevator.downQueueDone) {
                if (!elevator.upQueue2.isEmpty()) {
                    if (elevator.upQueue2.peek() == elevator.getCurrentFloor2() && elevator.ifNoDest) {
                        elevator.upQueue2.poll();
                        if (elevator.upQueue2.isEmpty())
                            elevator.upQueue2Done = true;
                    }
                    if (!elevator.upQueue2.isEmpty()) {
                        elevator.setDestination(elevator.upQueue2.poll());
                        elevator.ifNoDest = false;
                        elevator.goingUp2 = true;
                        //elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
                    } else {
                        elevator.upQueue2Done = true;
                    }
                    elevator.upQueue2Checked = true;
                } else {
                    elevator.upQueue2Done = true;
                }
            }
            if (!elevator.downQueue2Done && elevator.upQueue2Done) {
                if (!elevator.downQueue2.isEmpty()) {
                    if (elevator.downQueue2.peek() == elevator.getCurrentFloor2() && elevator.ifNoDest) {
                        elevator.downQueue2.poll();
                        if (elevator.downQueue2.isEmpty()) {
                            elevator.downQueue2Done = false;
                            elevator.upQueueDone = false;
                            elevator.downQueueDone = false;
                            elevator.upQueue2Done = false;
                            update(1, elevator);
                        }
                    }
                    if (!elevator.downQueue2.isEmpty()) {
                        elevator.setDestination(elevator.downQueue2.poll());
                        elevator.ifNoDest = false;
                        elevator.goingUp2 = false;
                        //elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
                    } else {
                        elevator.downQueue2Done = false;
                        elevator.upQueueDone = false;
                        elevator.downQueueDone = false;
                        elevator.upQueue2Done = false;
                        update(1, elevator);
                    }
                    elevator.upQueue2Checked = true;
                } else {
                    elevator.downQueue2Done = false;
                    elevator.upQueueDone = false;
                    elevator.downQueueDone = false;
                    elevator.upQueue2Done = false;
                    update(1, elevator);
                }
            }
        }


//
//        int previousDestination = elevator.getDestination();
//        if (elevator.goingDownQueue.isEmpty() && elevator.goingUpQueue.isEmpty() && elevator.goingDownQueue2.isEmpty() && elevator.goingUpQueue2.isEmpty()) {
//            elevator.noDest = true;
//            //elevator.a = -1;
//            elevator.aList.clear();
//            elevator.queueUpEnded = false;
//            elevator.queueDownEnded = false;
//        }
//        if (!elevator.goingDownQueue.isEmpty() && !elevator.queueDownEnded) {
//            if (elevator.goingDownQueue.peek() == elevator.getCurrentFloor2())
//                elevator.goingDownQueue.poll();
//        }
//        if (!elevator.queueDownEnded && !elevator.goingDownQueue.isEmpty()) {
//
//            if (!elevator.goingDownQueue.isEmpty()) {
//
//                elevator.setDestination(elevator.goingDownQueue.poll());
//                elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//                elevator.goingUp = false;
//            }
//
//        } else if (elevator.noDest == true) {
//            if (elevator.getCurrentFloor() > 0) {
//                elevator.goingUp = true;
//                elevator.setDestination(elevator.getCurrentFloor() - 1);
//                elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//            } else {
//                elevator.goingUp = true;
//                elevator.setDestination(elevator.getCurrentFloor() + 1);
//                elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//            }
//        } else {
//            elevator.queueDownEnded = true;
//            if (!elevator.goingUpQueue.isEmpty()) {
//                elevator.goingUp = true;
//                if (elevator.goingUpQueue.peek() == elevator.getCurrentFloor2())
//                    elevator.goingUpQueue.poll();
//                if (!elevator.goingUpQueue.isEmpty()) {
//                    elevator.setDestination(elevator.goingUpQueue.poll());
//                    elevator.queueUpEnded = true;
//                    elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//                }
//            } else {
//                if (elevator.queueUpEnded == true) {
//                    elevator.goingUp = false;
//                    if (!elevator.goingDownQueue2.isEmpty()) {
//                        if (elevator.goingDownQueue2.peek() == elevator.getCurrentFloor2())
//                            elevator.goingDownQueue2.poll();
//                        if (!elevator.goingDownQueue2.isEmpty()) {
//                            elevator.setDestination(elevator.goingDownQueue2.poll());
//                            elevator.queueDown2Ended = true;
//                            elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//                        }
//                    } else {
////                        elevator.queueUpEnded = false;
////                        elevator.queueDownEnded = false;
////                        update(1, elevator);
//                        if (elevator.queueDown2Ended == true) {
//                            if (!elevator.goingUpQueue2.isEmpty()) {
//                                if (elevator.goingUpQueue2.peek() == elevator.getCurrentFloor2())
//                                    elevator.goingUpQueue2.poll();
//                                if (!elevator.goingUpQueue2.isEmpty()) {
//                                    elevator.setDestination(elevator.goingUpQueue2.poll());
//                                    elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//                                }
//                            } else {
//                                elevator.queueDown2Ended = false;
//                                elevator.queueUpEnded = false;
//                                elevator.queueDownEnded = false;
//                                update(1, elevator);
//                            }
//                        } else {
//                            elevator.queueUpEnded = false;
//                            elevator.queueDownEnded = false;
//                            update(1, elevator);
//                        }
//                    }
//                } else {
//                    elevator.queueDownEnded = false;
//                    update(1, elevator);
//                }
//
////        elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//            }
//            //elevator.goingUp = !elevator.goingUp;
//            //System.out.println("update na końcu");
//
//        }
    }

    public void printQueues(Elevator elevator) {
        System.out.println("GoingUpQueue: " + elevator.goingUpQueue);
        System.out.println("GoingDownQueue: " + elevator.goingDownQueue);
        System.out.println("GoingDownQueue2: " + elevator.goingDownQueue2);
        //System.out.println("queueUpCopied: " + elevator.queueUpCopied);
        System.out.println("goingUp: " + elevator.goingUp);
        System.out.println("destination: " + elevator.getDestination());
    }

    public void updateDestination(int i, Elevator elevator, int begin) {
        switch (i) {
            case 1:
                if (elevator.ifNoDest) {
                    elevator.ifNoDest = false;
                } else {
                    if (!elevator.upQueue.contains(elevator.getDestination()) && !elevator.ifNoDest)
                        elevator.upQueue.add(elevator.getDestination());
                }
                if (!elevator.upQueue.contains(begin))
                    elevator.upQueue.add(begin);
                //if(elevator.openPercent<0.02 )
                update(1, elevator);
                break;
            case 2:
                if (elevator.ifNoDest) {
                    elevator.ifNoDest = false;
                } else {
                    if (!elevator.downQueue.contains(elevator.getDestination()) && !elevator.ifNoDest)
                        elevator.downQueue.add(elevator.getDestination());
                }
                if (!elevator.downQueue.contains(begin))
                    elevator.downQueue.add(begin);
                //if(elevator.openPercent>0.02 )
                update(1, elevator);
                break;
            case 3:
                if (elevator.ifNoDest) {
                    elevator.ifNoDest = false;
                } else {
                    if (!elevator.upQueue2.contains(elevator.getDestination()) && !elevator.ifNoDest)
                        elevator.upQueue2.add(elevator.getDestination());
                }
                if (!elevator.upQueue2.contains(begin))
                    elevator.upQueue2.add(begin);
                //if(elevator.openPercent>0.02  )
                update(1, elevator);
                break;
            case 4:
                if (elevator.ifNoDest) {
                    elevator.ifNoDest = false;
                } else {
                    if (!elevator.downQueue2.contains(elevator.getDestination()) && !elevator.ifNoDest)
                        elevator.downQueue2.add(elevator.getDestination());
                }
                if (!elevator.downQueue2.contains(begin))
                    elevator.downQueue2.add(begin);
                //if(elevator.openPercent>0.02 )
                update(1, elevator);
                break;
        }
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
                // <--------------------------część gdy się błąka -------------------------->
                if (elevator.ifNoDest) {
                    if (elevator.goingUp2) {
                        if (begin > elevator.getCurrentFloor2()) {
                            //elevator.upQueue.add(begin);
                            elevator.ifNoDest = false;
                            elevator.setDestination(begin);
                            elevator.goingUp2 = true;
//                        if (elevator.getDestination()> begin)
//                            elevator.upQueue.add(begin);
                            if (begin > dest) {
                                elevator.upQueueDone = true;
                                elevator.goingUp2 = false;
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
                            //updateDestination(1,elevator,begin);
                        } else if (begin == elevator.getCurrentFloor2()) {
                            if (begin > dest) {
//                                elevator.upQueueDone = true;
//                                elevator.goingUp2 = false;
                                elevator.ifNoDest = false;
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
//                        if (!elevator.upQueue.contains(elevator.getDestination()))
//                            elevator.upQueue.add(elevator.getDestination());
                            //elevator.upQueue.add(begin);
                            elevator.setDestination(begin);
                            //elevator.ifNoDest = false;
                            //updateDestination(1,elevator,begin);
                            elevator.ifNoDest = false;
                            elevator.openDoor();
                        } else if (begin < elevator.getCurrentFloor2()) {
                            elevator.ifNoDest = false;
                            elevator.setDestination(begin);
                            elevator.goingUp2 = false;
                            //elevator.ifNoDest = false;
                            //elevator.goingUp2 = false;
                            if (begin > dest) {
                                elevator.upQueueDone = true;
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                elevator.upQueueDone = true;
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }

                            elevator.closeDoor2();
                            //elevator.downQueue.add(begin);
                            //updateDestination(2,elevator,begin);
                        }
                    } else {
                        elevator.upQueueDone = true;
                        //elevator.ifNoDest = false;
                        if (begin > elevator.getCurrentFloor2()) {
                            elevator.ifNoDest = false;
                            elevator.setDestination(begin);
                            elevator.goingUp2 = true;
                            //elevator.upQueue2.add(begin);
                            //elevator.goingUp2 = true;
                            if (begin > dest) {
                                elevator.downQueueDone = true;
                                if (!elevator.downQueue2.contains(dest))
                                    elevator.downQueue2.add(dest);
                            } else {
                                elevator.downQueueDone = true;
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
                            elevator.closeDoor2();
                            //updateDestination(3,elevator,begin);
                        } else if (begin == elevator.getCurrentFloor2()) {
                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                //elevator.downQueueDone = true;
                                elevator.goingUp2 = true;
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
                            elevator.setDestination(begin);
                            elevator.ifNoDest = false;
                            //updateDestination(2,elevator,begin);
                            elevator.openDoor();
                        } else if (begin < elevator.getCurrentFloor2()) {

                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                elevator.downQueueDone = true;
                                elevator.goingUp2 = true;
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
                            elevator.setDestination(begin);
                            elevator.goingUp2 = false;
                            elevator.ifNoDest = false;
                            //updateDestination(2,elevator,begin);
//                        if (!elevator.downQueue.contains(elevator.getDestination()))
//                            elevator.downQueue.add(elevator.getDestination());

                        }

                    }
                } else {
                    //<------------normalna część --------------------------->
                    if (elevator.goingUp2 && !elevator.upQueueDone) {
                        //realizuje kolejkę upQueue
                        if (begin > elevator.getCurrentFloor2()) {
                            if (!elevator.upQueue.contains(begin))
                                elevator.upQueue.add(begin);
//                        if (elevator.getDestination()> begin)
//                            elevator.upQueue.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
                            updateDestination(1, elevator, begin);
                        } else if (begin == elevator.getCurrentFloor2()) {
                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
//                        if (!elevator.upQueue.contains(elevator.getDestination()))
//                            elevator.upQueue.add(elevator.getDestination());
                            updateDestination(1, elevator, begin);
                            elevator.openDoor();
                        } else if (begin < elevator.getCurrentFloor2()) {
                            if (!elevator.downQueue.contains(begin))
                                elevator.downQueue.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
                        }
                    } else if (!elevator.goingUp2 && !elevator.downQueueDone) {
                        //relizuje kolejkę downQueue
                        if (begin > elevator.getCurrentFloor2()) {
                            if (!elevator.upQueue2.contains(begin))
                                elevator.upQueue2.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue2.contains(dest))
                                    elevator.downQueue2.add(dest);
                            } else {
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
                        } else if (begin == elevator.getCurrentFloor2()) {
                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
                            updateDestination(2, elevator, begin);
                            elevator.openDoor();
                        } else if (begin < elevator.getCurrentFloor2()) {
                            if (!elevator.downQueue.contains(begin))
                                elevator.downQueue.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
//                        if (!elevator.downQueue.contains(elevator.getDestination()))
//                            elevator.downQueue.add(elevator.getDestination());
                            updateDestination(2, elevator, begin);
                        }
                    } else if (elevator.goingUp2 && elevator.downQueueDone) {
                        // realizuje upQueue2
                        if (begin > elevator.getCurrentFloor2()) {
                            if (!elevator.upQueue2.contains(begin))
                                elevator.upQueue2.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue2.contains(dest))
                                    elevator.downQueue2.add(dest);
                            } else {
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
//                        if (!elevator.upQueue2.contains(elevator.getDestination()))
//                            elevator.upQueue2.add(elevator.getDestination());
                            updateDestination(3, elevator, begin);
                        } else if (begin == elevator.getCurrentFloor2()) {
                            if (begin > dest) {
                                if (!elevator.downQueue2.contains(dest))
                                    elevator.downQueue2.add(dest);
                            } else {
                                if (!elevator.upQueue2.contains(dest))
                                    elevator.upQueue2.add(dest);
                            }
                            updateDestination(3, elevator, begin);
                            elevator.openDoor();
                        } else if (begin < elevator.getCurrentFloor2()) {
                            if (!elevator.downQueue2.contains(begin))
                                elevator.downQueue2.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue2.contains(dest))
                                    elevator.downQueue2.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
                        }
                    } else if (!elevator.goingUp2 && elevator.upQueue2Done) {
                        // realizuje kolejkę downQueue2
                        if (begin > elevator.getCurrentFloor2()) {
                            if (!elevator.upQueue.contains(begin))
                                elevator.upQueue.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue.contains(dest))
                                    elevator.downQueue.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
                        } else if (begin == elevator.getCurrentFloor2()) {
                            if (begin > dest) {
                                if (!elevator.downQueue2.contains(dest))
                                    elevator.downQueue2.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
                            updateDestination(4, elevator, begin);
                            elevator.openDoor();
                        } else if (begin < elevator.getCurrentFloor2()) {
                            if (!elevator.downQueue2.contains(begin))
                                elevator.downQueue2.add(begin);
                            if (begin > dest) {
                                if (!elevator.downQueue2.contains(dest))
                                    elevator.downQueue2.add(dest);
                            } else {
                                if (!elevator.upQueue.contains(dest))
                                    elevator.upQueue.add(dest);
                            }
//                        if (!elevator.downQueue2.contains(elevator.getDestination()))
//                            elevator.downQueue2.add(elevator.getDestination());
                            updateDestination(4, elevator, begin);
                        }
                    }
                }
            }
        });


        // <------------------------------------------------------------------------------------------>
        // kiedy nie było zlecenia
//                    if (elevator.noDest == true) {
//                        elevator.noDest = false;
//                        if (begin > dest) {
//                            if (elevator.getCurrentFloor2() != begin) {
//                                elevator.setDestination(begin);
//                                elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//                                //elevator.a = dest;
//                                elevator.aList.add(dest);
//                            } else {
//                                //elevator.a = dest;
//                                elevator.aList.add(dest);
//                                elevator.openDoor();
//                            }
////                                if (!elevator.goingDownQueue.contains(elevator.getDestination()))
////                                    elevator.goingDownQueue.add(elevator.getDestination());
////                                elevator.setDestination(begin);
////                                if (elevator.a != -1 && !elevator.goingDownQueue.contains(elevator.a))
////                                    elevator.goingUpQueue.add(elevator.a);
//                        } else {
//                            if (elevator.getCurrentFloor2() != begin) {
//                                elevator.setDestination(begin);
//                                elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//                                // a do dest bo może się zmienic jeszce najwyśzy
//                                //elevator.a = dest;
//                                elevator.aList.add(dest);
//                            } else {
//                                //elevator.a = dest;
//                                elevator.aList.add(dest);
//                                elevator.openDoor();
//                            }
//                        }
//                    } else if (elevator.goingUp2) {
//                        // przyjmuje tylko tych którzy jadą z wyższych pięter
//                        if (!elevator.queueDownEnded) {
//                            if (begin > elevator.getDestination()) {
//                                elevator.noDest = false;
//                                if (!elevator.goingDownQueue.contains(elevator.getDestination()))
//                                    elevator.goingDownQueue.add(elevator.getDestination());
//                                if (elevator.getCurrentFloor2() != begin) {
////                                    if (elevator.a != -1 && !elevator.goingDownQueue.contains(elevator.a))
////                                        elevator.goingDownQueue.add(elevator.getDestination());
//                                    if (!elevator.goingDownQueue.contains(elevator.getDestination()))
//                                        elevator.goingDownQueue.add(elevator.getDestination());
//                                    elevator.setDestination(begin);
//                                    elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//
////                                    if (elevator.a != -1 && !elevator.goingUpQueue.contains(elevator.a))
////                                        elevator.goingUpQueue.add(elevator.a);
////                                    elevator.a = dest;
//                                    for (int b : elevator.aList) {
//                                        if (!elevator.goingUpQueue.contains(b)) {
//                                            elevator.goingUpQueue.add(b);
//                                        }
//                                        elevator.aList.remove(b);
//                                    }
//                                    elevator.aList.add(dest);
//                                } else {
//                                    if (!elevator.goingDownQueue.contains(elevator.getDestination()))
//                                        elevator.goingDownQueue.add(elevator.getDestination());
//                                    for (int b : elevator.aList) {
//                                        if (!elevator.goingUpQueue.contains(b)) {
//                                            elevator.goingUpQueue.add(b);
//                                        }
//                                        elevator.aList.remove(b);
//                                    }
//                                    elevator.aList.add(dest);
////                                    if (elevator.a != -1 && !elevator.goingUpQueue.contains(elevator.a))
////                                        elevator.goingUpQueue.add(elevator.a);
////                                    elevator.a = dest;
//                                    elevator.openDoor();
//                                }
//
//
//                            /*if (!elevator.goingDownQueue.contains(dest))
//                                elevator.goingDownQueue.add(dest);*/
//
//                            } else if (begin == elevator.getDestination()) {
//                                /*if (!elevator.goingDownQueue.contains(dest))
//                                    elevator.goingDownQueue.add(dest);
//                                if (begin == elevator.getCurrentFloor2())
//                                    elevator.openDoor();*/
//                                if (!elevator.aList.contains(dest))
//                                    elevator.aList.add(dest);
//                                if (begin == elevator.getCurrentFloor2())
//                                    elevator.openDoor();
//                            } else {
//                                if (dest < begin) {
//                                    if (!elevator.goingDownQueue.contains(begin))
//                                        elevator.goingDownQueue.add(begin);
//                                    if (!elevator.goingDownQueue.contains(dest))
//                                        elevator.goingDownQueue.add(dest);
//                                } else {
//                                    if (!elevator.goingDownQueue.contains(begin))
//                                        elevator.goingDownQueue.add(begin);
//                                    if (!elevator.goingUpQueue.contains(dest))
//                                        elevator.goingUpQueue.add(dest);
//                                }
//                            }
//                        } else {
//                            // jedzie w górę zgodnie z goingUpQueue
//                            if (begin >= elevator.getCurrentFloor2()) {
//                                if (dest > begin) {
//                                    if (!elevator.goingUpQueue.contains(dest))
//                                        elevator.goingUpQueue.add(dest);
//                                    if (begin == elevator.getCurrentFloor2())
//                                        elevator.openDoor();
//                                    else if (!elevator.goingUpQueue.contains(begin))
//                                        elevator.goingUpQueue.add(begin);
//                                } else {
//                                    if (begin == elevator.getCurrentFloor2())
//                                        elevator.openDoor();
//                                    else if (!elevator.goingUpQueue.contains(begin))
//                                        elevator.goingUpQueue.add(begin);
//                                    if (!elevator.goingDownQueue2.contains(dest))
//                                        elevator.goingDownQueue2.add(dest);
//                                }
//
//                            } else {
//                                if (dest < begin) {
//                                    if (!elevator.goingDownQueue2.contains(dest))
//                                        elevator.goingDownQueue2.add(dest);
//                                    if (!elevator.goingDownQueue2.contains(begin))
//                                        elevator.goingDownQueue2.add(begin);
//                                } else {
//                                    if (!elevator.goingDownQueue2.contains(begin))
//                                        elevator.goingDownQueue2.add(begin);
//                                    if (!elevator.goingUpQueue2.contains(dest))
//                                        elevator.goingUpQueue2.add(dest);
//                                }
//                            }
//                        }
//                    } else {
//                        if (begin <= elevator.getCurrentFloor2()) {
//                            if (dest < begin) {
//                                if (!elevator.goingDownQueue.contains(dest))
//                                    elevator.goingDownQueue.add(dest);
//                                if (begin == elevator.getCurrentFloor2())
//                                    elevator.openDoor();
//                                else if (!elevator.goingDownQueue.contains(begin))
//                                    elevator.goingDownQueue.add(begin);
//                            } else {
//                                if (!elevator.goingUpQueue.contains(dest))
//                                    elevator.goingUpQueue.add(dest);
//                                if (begin == elevator.getCurrentFloor2())
//                                    elevator.openDoor();
//                                else if (!elevator.goingDownQueue.contains(begin))
//                                    elevator.goingDownQueue.add(begin);
//                            }
//                        } else {
//                            // tutaj może potrzebny if
//                            if (!elevator.goingUpQueue.contains(begin))
//                                elevator.goingUpQueue.add(begin);
//                            if (!elevator.goingDownQueue2.contains(dest))
//                                elevator.goingDownQueue2.add(dest);
//                        }
//                    }
//
//                    System.out.println("On action na końcu");
//                    printQueues(elevator);
//                }
//
//
////                    elevator.goingUp2 = previousDestination > elevator.getDestination() ? false : true;
//            }
//
//            );
        children.add(new Label("Wybierz piętra:")

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
