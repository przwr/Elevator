package elevator;

/**
 * Created by przemek on 15.05.16.
 */
public class Person {
    public int source;
    public int destination;
    long startTime;
    long endTime;
    float runTime;
    float waitTime;
    boolean out = false;

    public Person(int source, int destination) {
        startTime = System.currentTimeMillis();
        this.destination = destination;
        this.source = source;
    }

    public void setEndTime(long time) {
        if (!out) {
            endTime = time;
            runTime = ((endTime - startTime) / 1000f);
            out = true;
        }
    }

    public String getRunTimeLabel() {
        if (runTime > 10) {
            return "" + Math.round(runTime);
        } else {
            return " " + Math.round(runTime);
        }
    }

    public String getWaitTimeLabel() {
        if (waitTime > 10) {
            return "" + Math.round(waitTime);
        } else {
            return " " + Math.round(waitTime);
        }
    }

    public float getRunTime() {
        return runTime;
    }

    public float getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long time) {
        waitTime = (time - startTime) / 1000f;
    }

    public String getRunTimeNice() {
        return "" + ((int) (runTime * 10)) / 10f;
    }

    public String getWaitTimeNice() {
        return "" + ((int) (waitTime * 10)) / 10f;
    }
}
