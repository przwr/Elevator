package elevator;

import java.util.List;

public class Algorithm1 extends Algorithm {

    @Override
    protected void update(Elevator elevator) {
        super.update(elevator);
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
                            update(elevator);
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
    }


}
