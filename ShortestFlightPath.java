import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * The shortest flight path class provides the user with the fastest flight path between two cities
 * 
 * @author megangibbons
 *
 */
public class ShortestFlightPath {
    // stores all the airports and their information
    public static Hashtable<String, Airport> airports;
    // stores all the direct flights
    public static ArrayList<Flight> flights;
    
    /**
     * Reads in all the airport information from a .csv and creates airport objects
     * @param fileName: the name of the file
     */
    public static void readAirportsFromCSV(String fileName) {
        airports = new Hashtable<String, Airport>(); // initialize the hash table
        // read in each line of the file
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                // use the comma to split each data element
                String[] info = line.split(",");
                // create airport objects and add them to the hash table
                Airport airport = new Airport(info[0], info[1], info[2], info[3]);
                airports.put(info[0], airport);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads in all the flight information from a .csv and creates flight objects
     * @param fileName: the name of the file
     */
    public static void readFlightsFromCSV(String fileName) {
        flights = new ArrayList<Flight>(); // initialize flight array list
        int id = 1001; // using internal IDs for troubleshooting loading the .csv
        // read each line of the file
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                String[] info = line.split(",");
                // finds the appropriate airport object corresponding to the flight
                // in the hash table
                Airport departure = airports.get(info[0]);
                Airport arrival = airports.get(info[1]);
                int duration = Integer.parseInt(info[4]);
                // prevents creating flight objects for airports that aren't
                // in the hash table
                if (departure != null && arrival != null) {
                    // create a new flight object and add it to the array list
                    Flight newFlight =
                        new Flight(id, departure, arrival, duration, info[2], info[3]);
                    flights.add(newFlight);
                    // add the flight to a linked list of flights leaving each airport object
                    departure.addNewFlightLeaving(newFlight);
                }
                line = br.readLine();
                id += 1;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints all the airports for troubleshooting and so that the user knows
     * which airports are available to search from
     */
    public static void printAllAirports() {
        airports.forEach((k, v) -> System.out.println(v.toString()));
    }

    /**
     * Prints all flights stored in the flights array list for troubleshooting and 
     * so that the user can view all of the flight data
     */
    public static void printAllFlights() {
        for (int i = 0; i < flights.size(); i++) {
            System.out.println(flights.get(i).toString());
        }
    }

    /**
     * Prints all the flights departing from a specific airport
     * @param airport: the String acronym of the departure airport
     */
    public static void printFlightsFromAirport(String airport) {
        for (int i = 0; i < flights.size(); i++) {
            Flight curr = flights.get(i);
            if (airport.equals(curr.getDepartureAirport().getAcronym())) {
                System.out.println(curr.toString());
            }
        }
    }

    /**
     * Prints all the flights arriving to a specific airport
     * @param airport: the String acronym of the arrival airport
     */
    public static void printFlightsToAirport(String airport) {
        for (int i = 0; i < flights.size(); i++) {
            Flight curr = flights.get(i);
            if (airport.equals(curr.getArrivalAirport().getAcronym())) {
                System.out.println(curr.toString());
            }
        }
    }

    /**
     * Uses Dijkstra's shortest path algorithm to find the fastest travel route 
     * in minutes spent traveling between two airports
     * @param start: the acronym for the departure airport
     * @param end: the acronym for the arrival airport
     * @return the shortest path from the departure to the arrival airport
     */
    public static Path shortestFlight(String start, String end) {
        // if the start and end airport are the same or the airports aren't
        // in the hash table, return null
        if (start.equals(end) || !airports.containsKey(start) || !airports.containsKey(end)) {
            return null;
        }
        PriorityQueue<Path> paths = new PriorityQueue<Path>();
        // tracks the airports to which the path has been explored already
        Hashtable<String, Airport> explored = new Hashtable<>();
        // tracks all the airports whose shortest path hasn't already been explored
        ArrayList<Airport> unexplored = new ArrayList<>();
        // add the starting/departure airport to the explored list and explore
        // the paths from that airport
        explored.put(start, airports.get(start));
        Airport curr = airports.get(start);
        // continue the algorithm while all paths haven't been explored
        while (explored.size() != airports.size()) {
            // get all of the flights/edges leaving from the current airport
            LinkedList<Flight> flightsLeaving = curr.getFlightsLeaving();
            // stores the paths that will be updated to include the current airport
            // being explored to avoid a ConcurrentModificationException
            PriorityQueue<Path> intermediatePaths = new PriorityQueue<Path>();
            // check each flight/edge leaving the current airport
            for (int i = 0; i < flightsLeaving.size(); i++) {
                Flight currFlight = flightsLeaving.get(i);
                // when exploring the flights from the starting airport, add them
                // all to the paths priority queue
                if (curr.getAcronym().equals(start)) {
                    paths.add(new Path(currFlight));
                }
                // only check flights whose path to the arrival airport hasn't been
                // explored
                if (!explored.containsKey(currFlight.getArrivalAirport().getAcronym())) {
                    // add the arrival airport of the current flight/edge to the
                    // unexplored list if it's not already there
                    if (!unexplored.contains(currFlight.getArrivalAirport())) {
                        unexplored.add(currFlight.getArrivalAirport());
                    }
                    // iterate through the existing paths and add the current
                    // flight to the end of the path if the path ends at the
                    // departing airport of the current flight
                    Iterator<Path> itr = paths.iterator();
                    while (itr.hasNext()) {
                        Path p = itr.next();
                        if (p.end.getArrivalAirport().equals(curr)) {
                            // parse the time the path ends and when the 
                            // current flight takes off
                            String[] pathTime = p.endTime.split(":");
                            String[] flightTime = currFlight.getDepartureTime().split(":");
                            int pathHour = Integer.parseInt(pathTime[0]);
                            int pathMin = Integer.parseInt(pathTime[1]);
                            int flightHour = Integer.parseInt(flightTime[0]);
                            int flightMin = Integer.parseInt(flightTime[1]);
                            // make sure the new flight leaves at a time after
                            // the path's last arrival flight
                            if (pathHour <= flightHour && flightMin > pathMin) {
                                intermediatePaths.add(new Path(p, currFlight));
                            }
                        }
                    }
                }
            }
            // add all the intermediate paths updated to the paths queue
            paths.addAll(intermediatePaths);
            // iterate through the paths to find the next airport to examine (i.e. the one
            // with the shortest distance from the starting airport)
            Iterator<Path> itr = paths.iterator();
            int minLength = Integer.MAX_VALUE; // tracks the shortest time of all the paths
            Path shortestPath = null; // tracks the shortest path
            while (itr.hasNext()) {
                Path currPath = itr.next();
                // update minLength and shortestPath if the current path has a 
                // shorter distance than the others and the last arrival airport isn't in explored
                if (!explored.containsKey(currPath.end.getArrivalAirport().getAcronym())
                    && currPath.totalTime < minLength) {
                    shortestPath = currPath;
                    minLength = currPath.totalTime;
                }
            }
            // if no next shortest path is found (i.e. you can't get from the 
            // starting airport to another one), leave the loop
            if (shortestPath == null) {
                break;
            }
            // update the current airport, explored list, and unexplored list
            curr = shortestPath.end.getArrivalAirport();
            explored.put(curr.getAcronym(), curr);
            unexplored.remove(curr);
        }
        // remove paths that don't start and end at the starting and ending airports
        paths.removeIf(path -> !path.end.getArrivalAirport().getAcronym().equals(end));
        paths.removeIf(path -> !path.start.getDepartureAirport().getAcronym().equals(start));
        // return the path with the highest priority in the queue
        return paths.peek();
    }

    /**
     * Prints all the menu options for the user's interaction with the program
     */
    public static void printMenu() {
        System.out.println("Choose an option from the list below:");
        System.out.println("1) Display all of the airport information.");
        System.out.println("2) Display all of the available flights.");
        System.out.println("3) Display all the flights leaving from a specific airport.");
        System.out.println("4) Display all the flights arriving to a specific airport.");
        System.out.println("5) Find the shortest flight path between two airports.");
        System.out.println("6) Quit.");
    }
    
    /**
     * The main method continually prompts the user for input to run the other 
     * methods, such as printing the airports, finding the shortest flight,
     * and printing the flights
     */
    public static void main(String[] args) {
        // load in the data for both the flights and airports
        readAirportsFromCSV(
            "airports.csv");
        readFlightsFromCSV(
            "flights.csv");
        Scanner scnr = new Scanner(System.in);
        String userIn = "";
        boolean search = true;
        System.out.println("Welcome to the shortest flight path generator!");
        while (search) {
            printMenu();
            // get user input to determine which method to call
            userIn = scnr.next();
            userIn.strip();
            switch (userIn) {
                case "1":
                    printAllAirports();
                    break;
                case "2":
                    printAllFlights();
                    break;
                case "3":
                    // get which airport to evaluate
                    System.out.println("Which airport?");
                    String from = scnr.next().toUpperCase();
                    from.strip();
                    printFlightsFromAirport(from);
                    break;
                case "4":
                    // get which airport to evaluate
                    System.out.println("Which airport?");
                    String to = scnr.next().toUpperCase();
                    to.strip();
                    printFlightsToAirport(to);
                    break;
                case "5":
                    // get the departure airport
                    System.out.println("From which airport?");
                    from = scnr.next().toUpperCase();
                    from.strip();
                    // get the arrival airport
                    System.out.println("To which airport?");
                    to = scnr.next().toUpperCase();
                    to.strip();
                    // find the shortest path
                    Path path = shortestFlight(from, to);
                    // if no path is found, let the user know
                    if (path == null) {
                        System.out.println(
                            "No possible flight paths between these airports. Please try again.");
                        break;
                    }
                    // otherwise, print out the shortest path
                    System.out.print(path.toString());
                    break;
                case "6":
                    System.out.println("Thank you for using the fastest flight generator!");
                    search = false;
                    break;
                // handle invalid user inputs
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
        scnr.close();
    }
}
