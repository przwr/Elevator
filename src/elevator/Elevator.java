package elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by przemek on 08.05.16.
 */
public class Elevator {

    public float speed = 0.01f;
    private float currentFloor;
    private int previous;
    private int maxFloor;
    private int destination;
    private int counter;
    private float openPercent = 0;
    private State stopOpen, ride, stopClosed;
    private State current;
    private ArrayList<Person> people = new ArrayList<>();
    private ArrayList<Person>[] floorPeople;
    private ArrayList<Person> toRemove = new ArrayList<>();


    public Elevator(int floors, ArrayList<Person>[] floorPeople) {
        maxFloor = floors;
        this.floorPeople = floorPeople;
        stopClosed = new State() {
            @Override
            public void update() {
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
        };
        stopOpen = new State() {
            @Override
            public void update() {
                if (openPercent == 1) {
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
                        counter = 30;
                    }
                    if (counter == 0) {
                        for (Person person : floorPeople[getCurrentFloor()]) {
                            if (person.destination != getCurrentFloor()) {
                                people.add(person);
                                toRemove.add(person);
                            }
                        }
                        floorPeople[getCurrentFloor()].removeAll(toRemove);
                        toRemove.clear();
                    } else {
                        counter--;
                    }
                } else if (openPercent > 0) {
                    openPercent -= 0.01f;
                } else {
                    current = stopClosed;
                    openPercent = 0;
                }
            }
        };
        ride = new State() {
            @Override
            public void update() {
                if (destination != getCurrentFloor()) {
                    currentFloor += (Math.signum(destination - currentFloor)) * speed;
                } else {
                    currentFloor = destination;
                    current = stopClosed;
                }
            }
        };
        current = stopClosed;
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void openDoor() {
        if (current == stopClosed && openPercent == 0) {
            openPercent = 0.01f;
        }
    }

    public void closeDoor() {
        if (current == stopOpen && openPercent == 1) {
            openPercent = 0.99f;
        }
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

    public int getCurrentFloor() {
        if (Math.abs(currentFloor - previous) >= 1) {
            previous = Math.round(currentFloor);
            return previous;
        } else {
            return previous;
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
    }
}
