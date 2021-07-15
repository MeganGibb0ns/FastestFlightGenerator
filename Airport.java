import java.util.LinkedList;

/**
 * Airport objects contain information about the airports
 * @author megangibbons
 *
 */
public class Airport {
    private String acronym; // three letter unique airport acronym
    private String airportName; // full airport name
    private String city; // city the airport is located in
    private String state; // state the airport is located in
    private LinkedList<Flight> flightsLeaving; // all the flights leaving from the airport

    /**
     * Creates the airport object
     * @param acro: three letter airport acronym
     * @param airport: full name of the airport
     * @param city: city the airport is located in
     * @param state: state the airport is located in
     */
    public Airport(String acro, String airport, String city, String state) {
        // throw an exception if the data includes nulls
        if (acro == null || airport == null || city == null || state == null) {
            throw new IllegalArgumentException();
        }
        this.flightsLeaving = new LinkedList<Flight>();
        this.acronym = acro;
        this.airportName = airport;
        this.city = city;
        this.state = state;
    }
    
    /**
     * Get the three letter acronym for the airport
     * @return the acronym
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * Get the full airport name
     * @return the airport name
     */
    public String getAirport() {
        return this.airportName;
    }

    /**
     * Gets the airport's city
     * @return the airport's city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Gets the airport's state
     * @return the airport's state
     */
    public String getState() {
        return this.state;
    }

    /**
     * Gets all the flights leaving the airport
     * @return list of flights leaving the airport
     */
    public LinkedList<Flight> getFlightsLeaving() {
        return this.flightsLeaving;
    }

    /**
     * Adds a new flight to the linked list of flights leaving the airport
     * @param f: the flight leaving the airport
     */
    public void addNewFlightLeaving(Flight f) {
        this.flightsLeaving.add(f);
    }

    /**
     * Returns a string of airport information
     * @return airport information
     */
    @Override
    public String toString() {
        return "Acronym: " + this.acronym + ", Name: " + this.airportName + ", Location: "
            + this.city + ", " + this.state;
    }
}
