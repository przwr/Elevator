package elevator;

import java.util.*;

/**
 * Created by przemek on 08.05.16.
 */
public class Elevator {

    public float speed = 0.01f;
    public float openPercent = 0;
    public State stopOpen, ride, stopClosed;
    public State current;
    public boolean stopped = false;
    //    public boolean goingUp = true;
//    public boolean goingUp2 = true;
    public boolean queueDownEnded = false;
    public boolean queueUpEnded = false;
    public boolean queueDown2Ended = false;
    public boolean noDest = true;
    //public int a = -1;
    public ArrayList<Integer> aList = new ArrayList<>(20);
    // Wmapie trzymane tylko najwyższe piętro - begin
    public HashMap<Integer, Integer> aMap = new HashMap<>(20);
    public PriorityQueue<Integer> goingUpQueue = new PriorityQueue<>(20);
    public PriorityQueue<Integer> goingUpQueue2 = new PriorityQueue<>(20);
    //public PriorityQueue<Integer> goingUpQueue1 = new PriorityQueue<>(5, Collections.reverseOrder());
    public PriorityQueue<Integer> goingDownQueue = new PriorityQueue<>(20, Collections.reverseOrder());
    public PriorityQueue<Integer> goingDownQueue2 = new PriorityQueue<>(20, Collections.reverseOrder());
    // <--------------------------------------------------------------------->
    public PriorityQueue<Integer> upQueue = new PriorityQueue<>(60);
    public PriorityQueue<Integer> upQueue2 = new PriorityQueue<>(60);
    //public PriorityQueue<Integer> goingUpQueue1 = new PriorityQueue<>(5, Collections.reverseOrder());
    public PriorityQueue<Integer> downQueue = new PriorityQueue<>(60, Collections.reverseOrder());
    public PriorityQueue<Integer> downQueue2 = new PriorityQueue<>(60, Collections.reverseOrder());
    public boolean goingUp = true;
    public boolean goingUp2 = true;
    public boolean upQueueDone = false;
    //checked służy do sprawdzania czy lista była użyta - czy nie była pusta
    public boolean upQueueChecked = false;
    public boolean downQueueDone = false;
    public boolean downQueueChecked = false;
    public boolean upQueue2Done = false;
    public boolean upQueue2Checked = false;
    public boolean downQueue2Done = false;
    public boolean downQueue2Checked = false;
    public boolean ifNoDest = true;
    public boolean ifPreviousWasNoDest = true;
    public int stops = -1;
    public int floorsTraveled = 0;
    private float currentFloor;
    private int previous;
    private int maxFloor;
    private int destination;
    private int counter;
    private ArrayList<Person> people = new ArrayList<>();
    private ArrayList<Person>[] floorPeople;
    private ArrayList<Person> toRemove = new ArrayList<>();
    // trzyma wartość dest przed wymuszeniem otworzenia
    //private Controller controller = new Controller();
    //private Controller2 controller = new Controller2();
    private MainController controller;
    private int currentFloor2 = 0;
    private int previousFloor = 0;

    // <-------------------------------------------------------------------->
    public Elevator(int floors, ArrayList<Person>[] floorPeople, MainController controller) {
        this.controller = controller;
        maxFloor = floors;
        this.floorPeople = floorPeople;
        stopClosed = new State() {
            @Override
            public void update() {
//                goingUp2 = previous > getDestination() ? false : true;
                if (openPercent == 0) {
                    if (destination != getCurrentFloor()) {
                        current = ride;
                    }
                } else if (openPercent < 1) {
                    openPercent += 0.01f;
                } else {
                    current = stopOpen;
                    openPercent = 1;
                }
            }

            @Override
            public int getState() {
                return 0;
            }
        };
        stopOpen = new State() {
            @Override
            public void update() {
                if (openPercent == 1) {
                    stops++;
                    for (Person person : people) {
                        if (person.destination == getCurrentFloor()) {
                            toRemove.add(person);
                        }
                    }
                    if (!toRemove.isEmpty()) {
                        people.removeAll(toRemove);
                        for (Person person : toRemove) {
                            floorPeople[getCurrentFloor()].add(person);
                        }
                        toRemove.clear();

                    }
                    if (counter == 0) {
                        for (Person person : floorPeople[getCurrentFloor()]) {
                            if (person.destination != getCurrentFloor()) {
                                people.add(person);
                                person.setWaitTime(System.currentTimeMillis());
                                toRemove.add(person);
                            }
                        }
                        for (Person p : floorPeople[getCurrentFloor()]) {
                            if (p.destination == getCurrentFloor()) {
                                p.setEndTime(System.currentTimeMillis());
                                p.out = true;
                            }
                        }
                        floorPeople[getCurrentFloor()].removeAll(toRemove);
                        toRemove.clear();
                    } else {
                        counter--;
                    }
                    counter = 30;
                    closeDoor();
                } else if (openPercent > 0) {
                    openPercent -= 0.01f;
                } else {
                    current = stopClosed;
                    openPercent = 0;
                    //goingUp2 = previous > getDestination() ? false : true;
                }
            }

            @Override
            public int getState() {
                return 1;
            }
        };
        ride = new State() {
            @Override
            public void update() {
                if (previousFloor != getCurrentFloor()) {
                    previousFloor = getCurrentFloor();
                    floorsTraveled++;
                }
                if (destination != getCurrentFloor()) {
                    currentFloor += (Math.signum(destination - currentFloor)) * speed;
                } else {
                    currentFloor = destination;
                    current = stopClosed;
                    openDoor();
                }
            }

            @Override
            public int getState() {
                return 2;
            }
        };
        current = stopClosed;
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void openDoor() {
        if (current == stopOpen) {
            current = stopClosed;
        }
        if (current == stopClosed && openPercent == 0) {
            openPercent = 0.01f;
        }
        if (current == ride) {
            System.out.println("dest w current: " + getDestination());
        }
        //controller.updateOpen(this);
        counter = 0;
    }

    public void closeDoor2() {
        if (current == stopOpen && openPercent == 1) {
            openPercent = 0.99f;
        }
        if (current == stopClosed)
            current = stopOpen;
    }

    public void closeDoor() {
        if (current == stopOpen && openPercent == 1) {
            openPercent = 0.99f;
        }
        if (controller.algorithm != null) {
            controller.algorithm.update(this);
        }
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        if (destination > maxFloor) {
            destination = maxFloor;
        } else if (destination < 0) {
            destination = 0;
        }
        this.destination = destination;
    }

    public void update() {
        current.update();
    }

    //    public float getCurrentFloor3(){
//
//    }
    public int getCurrentFloor() {
        if (Math.abs(currentFloor - previous) >= 1) {
            previous = Math.round(currentFloor);
            return previous;
        } else {
            return previous;
        }
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentFloor2() {
        double val = currentFloor - (currentFloor % 1);
        if (goingUp2) {
            if (currentFloor % 1 > 0.00000000001)
                return (int) val + 1;
            else
                return (int) val;
        } else {
            return (int) val;
        }
    }

    public float getNextFloorPercent() {
        return currentFloor - getCurrentFloor();
    }

    public float getOpenPercent() {
        return openPercent;
    }

    public List<Person> getPeople() {
        return people;
    }


    private abstract class State {
        public abstract void update();

        public abstract int getState();
    }
}
