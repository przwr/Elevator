package elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by przemek on 08.05.16.
 */
public class Elevator {


    public float speed = 0.01f;
    private float currentFloor;
    private int maxFloor;
    private int destination;
    private float openPercent = 0;
    private State stopOpen, ride, stopClosed;
    private State current;
    private ArrayList<Person> people = new ArrayList<>();


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

    public Elevator(int floors) {
        maxFloor = floors;
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
//                    TODO ADD PEOPLE
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

    public void update() {
        current.update();
    }

    public int getCurrentFloor() {
        return (int) currentFloor;
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
