import java.util.LinkedList;
import java.util.List;

/**
 * Generates a total flight path from one airport to another
 * @author megangibbons
 *
 */
public class Path implements Comparable<Path> {
    public String startTime; // start time of the entire path
    public Flight start; // first flight
    public int totalTime; // total time for flights and layovers
    public List<Flight> dataSequence; // list of flights in order
    public Flight end; // last flight
    public String endTime; // end time of entire flight sequence

    /**
     * Creates a path object
     * @param start: the first flight of the path
     */
    public Path(Flight start) {
        this.start = start; 
        // set the path time to be the time of the first flight
        this.totalTime = start.getFlightDuration();
        // add the first flight to the sequence of flights
        this.dataSequence = new LinkedList<>();
        this.dataSequence.add(start);
        // set the end at the first flight, too
        this.end = start;
        // copy over the arrival and departures times from the first flight
        this.startTime = start.getDepartureTime();
        this.endTime = start.getArrivalTime();
    }


    /**
     * Updates an existing path object by adding a new flight
     * @param copyPath: the path to copy
     * @param extendBy: the flight by which to extend the path
     */
    public Path(Path copyPath, Flight extendBy) {
        // add the new flight's in air time to the total path duration
        this.totalTime = copyPath.totalTime + extendBy.getFlightDuration();
        // add the layover time between the current end flight and the new flight
        // to the total path time
        String[] pathTime = copyPath.endTime.split(":");
        String[] flightTime = extendBy.getDepartureTime().split(":");
        int pathHour = Integer.parseInt(pathTime[0]);
        int pathMin = Integer.parseInt(pathTime[1]);
        int flightHour = Integer.parseInt(flightTime[0]);
        int flightMin = Integer.parseInt(flightTime[1]);
        int layover = ((flightHour - pathHour) * 60) + flightMin - pathMin;
        this.totalTime += layover;
        // copy over the existing path's sequence of flights to the new path
        this.start = copyPath.start;
        this.dataSequence = new LinkedList<>();
        for (int i = 0; i < copyPath.dataSequence.size(); i++) {
            this.dataSequence.add(i, copyPath.dataSequence.get(i));
        }
        // add the new flight to the end of the path
        this.dataSequence.add(copyPath.dataSequence.size(), extendBy);
        this.end = extendBy;
        // copy forward the path's departure time
        this.startTime = copyPath.startTime;
        // update the path's arrival time to be the new flight's arrival time
        this.endTime = extendBy.getArrivalTime();
    }

    /**
     * Compares path's to one another. Allows the natural ordering of paths to 
     * be increasing with path duration.
     * @param other is the other path that is being compared to this one
         * @return -1 when this path has a smaller duration than the other,
         *         +1 when this path has a larger duration that the other
     */
    public int compareTo(Path other) {
        int cmp = this.totalTime - other.totalTime;
        return cmp;
    }

    /**
     * Returns a String representation of the path
     * @return information about the path in a String
     */
    @Override
    public String toString() {
        String path = "";
        for (int i = 0; i < dataSequence.size(); i++) {
            path += (dataSequence.get(i).toString() + "\n");
        }
        path += "Total time traveling: " + totalTime/60 + " hours and " + totalTime%60 + " minutes.\n";
        return path;
    }
}
