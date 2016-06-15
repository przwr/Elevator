package elevator;

import java.util.List;

/**
 * Created by Samsung on 2016-06-14.
 */
public abstract class Algorithm {
    protected abstract void addPerson(int begin, int dest, Elevator elevator, List<Person>[] people);


    protected void update(Elevator elevator) {
        Main.updateStats();
    }

    protected void updateDestination(int i, Elevator elevator, int begin) {
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
                update(elevator);
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
                update(elevator);
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
                update(elevator);
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
                update(elevator);
                break;
        }
    }
}
