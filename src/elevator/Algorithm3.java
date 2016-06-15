package elevator;

import java.util.List;

public class Algorithm3 extends Algorithm {

    @Override
    protected void update(Elevator elevator) {
        super.update(elevator);
        if (elevator.downQueue.isEmpty() && elevator.upQueue.isEmpty() && elevator.downQueue2.isEmpty() && elevator.upQueue2.isEmpty()) {
            elevator.upQueueDone = false;
            elevator.downQueueDone = false;
            elevator.upQueue2Done = false;
            elevator.downQueue2Done = false;
            elevator.ifNoDest = true;
            elevator.setDestination(5);
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
                            update(elevator);
                        }
                    }
                    if (!elevator.downQueue2.isEmpty()) {
                        elevator.setDestination(elevator.downQueue2.poll());
                        elevator.ifNoDest = false;
                        elevator.goingUp2 = false;

                    } else {
                        elevator.downQueue2Done = false;
                        elevator.upQueueDone = false;
                        elevator.downQueueDone = false;
                        elevator.upQueue2Done = false;
                        update(elevator);
                    }
                    elevator.upQueue2Checked = true;
                } else {
                    elevator.downQueue2Done = false;
                    elevator.upQueueDone = false;
                    elevator.downQueueDone = false;
                    elevator.upQueue2Done = false;
                    update(elevator);
                }
            }
        }
    }

    @Override
    protected void addPerson(int begin, int dest, Elevator elevator, List<Person>[]
            people) {
        int previousDestination = elevator.getDestination();
        if (begin != dest) {
            people[begin].add(new Person(begin, dest));
            // <--------------------------część gdy się błąka -------------------------->
            if (elevator.ifNoDest) {
                if (begin > elevator.getCurrentFloor2()) {
                    //elevator.upQueue.add(begin);
                    elevator.ifNoDest = false;
                    elevator.setDestination(begin);
                    elevator.goingUp2 = true;

                    if (begin > dest) {
                        elevator.upQueueDone = true;
                        elevator.goingUp2 = false;
                        if (!elevator.downQueue.contains(dest))
                            elevator.downQueue.add(dest);
                    } else {
                        if (!elevator.upQueue.contains(dest))
                            elevator.upQueue.add(dest);
                    }
                } else if (begin == elevator.getCurrentFloor2()) {
                    if (begin > dest) {

                        elevator.ifNoDest = false;
                        if (!elevator.downQueue.contains(dest))
                            elevator.downQueue.add(dest);
                    } else {
                        if (!elevator.upQueue.contains(dest))
                            elevator.upQueue.add(dest);
                    }

                    elevator.setDestination(begin);

                    elevator.ifNoDest = false;
                    elevator.openDoor();
                } else if (begin < elevator.getCurrentFloor2()) {
                    elevator.ifNoDest = false;
                    elevator.setDestination(begin);
                    elevator.goingUp2 = false;

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
                }


            } else {
                //<------------normalna część --------------------------->
                if (elevator.goingUp2 && !elevator.upQueueDone) {
                    //realizuje kolejkę upQueue
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
                        updateDestination(1, elevator, begin);
                    } else if (begin == elevator.getCurrentFloor2()) {
                        if (begin > dest) {
                            if (!elevator.downQueue.contains(dest))
                                elevator.downQueue.add(dest);
                        } else {
                            if (!elevator.upQueue.contains(dest))
                                elevator.upQueue.add(dest);
                        }

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

                        updateDestination(4, elevator, begin);
                    }
                }
            }
        }
    }


}
