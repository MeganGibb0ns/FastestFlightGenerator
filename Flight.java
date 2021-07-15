/**
 * The flight class creates a flight object which stores information on each
 * individual flight
 * @author megangibbons
 *
 */
public class Flight {
    private int flightID; // unique flight ID
    private Airport departure; // airport object from which the flight leaves
    private Airport arrival; // airport object to which the flight arrives
    private int flightDuration; // total flight time in minutes
    private String departureTime; // local time the flight departs
    private String arrivalTime; // local time the flight arrives
    
    /**
     * Creates a Flight object
     * @param id: internal flight ID
     * @param departure: departure airport
     * @param arrival: arrival airport
     * @param duration: total flight time in minutes
     * @param departureTime: time the flight departs
     * @param arrivalTime: time the flight arrives
     */
    public Flight(int id, Airport departure, Airport arrival, int duration, String departureTime,
        String arrivalTime) {
        // throw an exception if the inputs are invalid
        if (departure == null || arrival == null || departureTime == null || arrivalTime == null) {
            throw new IllegalArgumentException();
        }
        this.flightID = id;
        this.departure = departure;
        this.arrival = arrival;
        this.flightDuration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    /**
     * Gets the flight's ID
     * @return the flight's ID
     */
    public int getID() {
        return this.flightID;
    }

    /**
     * Gets the departure airport for the flight
     * @return the flight's departure airport
     */
    public Airport getDepartureAirport() {
        return this.departure;
    }

    /**
     * Gets the arrival airport for the flight
     * @return the flight's arrival airport
     */
    public Airport getArrivalAirport() {
        return this.arrival;
    }

    /**
     * Gets the flight's duration in minutes
     * @return the flight duration
     */
    public int getFlightDuration() {
        return this.flightDuration;
    }

    /**
     * Gets the flight's departure time
     * @return the flight's departure time
     */
    public String getDepartureTime() {
        return this.departureTime;
    }

    /**
     * Gets the flight's arrival time
     * @return the flight's arrival time
     */
    public String getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Generates a string of information about the flight
     * @return a String of flight information
     */
    @Override
    public String toString() {
        return "Flight " + this.flightID + " departs from " + this.departure.getAcronym() + " at "
            + this.departureTime + " and arrives in " + this.arrival.getAcronym() + " at "
            + arrivalTime;
    }
}
