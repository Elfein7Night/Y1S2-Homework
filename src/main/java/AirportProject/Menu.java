package AirportProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

public class Menu {
    private static Airport airport;

    public static void showMenu(String filePathArg) {
        if (filePathArg == null)
            airport = new Airport();
        else
            initAirportFromFile(filePathArg);

        Scanner s = new Scanner(System.in);
        int choice = -1;
        while (choice != 0) try {
            System.out.println("Menu:");
            System.out.println("1) Create outgoing Flight");
            System.out.println("2) Create incoming Flight");
            System.out.println("3) Show all outgoing flights");
            System.out.println("4) Show all incoming flights");
            System.out.println("5) Save airport to file");
            System.out.println("6) Load airport from file");
            System.out.println("7) Show Custom Selected flights");
            System.out.println("8) Show all flights");
            System.out.println("9) Remove Flight");
            System.out.println("\n0) To exit");
            choice = s.nextInt();
            switch (choice) {
                case 1 -> airport.getFlightFromUser(s, true);
                case 2 -> airport.getFlightFromUser(s, false);
                case 3 -> {
                    System.out.println("Outgoing flights: ");
                    airport.showOutgoingFlights();
                }
                case 4 -> {
                    System.out.println("Incoming flights: ");
                    airport.showIncomingFlights();
                }
                case 5 -> {
                    System.out.println("Please enter file name or file path to create");
                    airport.save(s.next());
                }
                case 6 -> {
                    System.out.println("Choose: ");
                    System.out.println("1) enter file name/path yourself");
                    System.out.println("2) load default file");
                    switch (s.nextInt()) {
                        case 1 -> {
                            System.out.println("Please enter file name or file path to read from");
                            initAirportFromFile(s.next());
                        }
                        case 2 -> {
                            airport = new Airport(new File("airport"));
                            System.out.println("Loaded default file!");
                        }
                        default -> System.out.println("Invalid Input");
                    }
                }
                case 7 -> showCustomRangeFlights(s);
                case 8 -> System.out.println(airport);
                case 9 -> {
                    System.out.println("Choose num of flight to remove");
                    System.out.println(airport);
                    if (airport.removeFlight(s.nextInt()))
                        System.out.println("Removed");
                    else System.out.println("Error Removing Flight");
                }
                case 0 -> System.out.println("~~~end of program~~~");
                default -> System.out.println("invalid input");
            }
        } catch (Exception e) {
            if (e instanceof MyException)
                System.out.println(e.getMessage());
            else
                System.out.println("oops! error: " + e.getClass().getSimpleName());
            System.out.println("please try again");
            s.nextLine(); //clean buffer
        }
    }


    public static void showCustomRangeFlights(Scanner s) throws MyException {
        List<Flight> result = new ArrayList<>(airport.getFlights());
        var oc = new Object() {
            String str;
            int num;
        };
        System.out.println("Do you want to filter by Direction?");
        if (scanBoolean(s)) {
            System.out.println("Please Select direction: ");
            System.out.println("1) Outgoing");
            System.out.println("2) Incoming");
            switch (s.nextInt()) {
                case 1 -> result.removeAll(airport.getIncomingFlights());
                case 2 -> result.removeAll(airport.getOutgoingFlights());
                default -> throw new MyException("Unexpected Value!");
            }
        }
        System.out.println("Do you want to filter by country?");
        if (scanBoolean(s)) {
            System.out.println("Please Enter country:");
            oc.str = s.next();
            Airport.filterByCountry(result, oc.str);
        }
        System.out.println("Do you want to filter by city?");
        if (scanBoolean(s)) {
            System.out.println("Please Enter city:");
            oc.str = s.next();
            Airport.filterByCity(result, oc.str);
        }
        System.out.println("Do you want to filter by airport?");
        if (scanBoolean(s)) {
            System.out.println("Please Enter airport name:");
            oc.str = s.next();
            Airport.filterByAirport(result, oc.str);
        }
        System.out.println("Do you want to filter by company?");
        if (scanBoolean(s)) {
            System.out.println("Please Enter company name:");
            oc.str = s.next();
            Airport.filterByCompany(result, oc.str);
        }
        System.out.println("Do you want to filter by terminal num?");
        if (scanBoolean(s)) {
            System.out.println("Please Enter terminal number:");
            oc.num = s.nextInt();
            Airport.filterByTerminal(result, oc.num);
        }
        System.out.println("Do you want to filter by days of the week?");
        if (scanBoolean(s)) {
            System.out.println("Please enter days of the week by name [space separated]");
            System.out.println("Example: 'sunday friday monday'");
            Airport.filterByWeekDays(result, s.next().toLowerCase());
        }
        System.out.println("Do you want to filter by date and time range?");
        if (scanBoolean(s)) {
            System.out.println("Please enter start date");
            LocalDateTime start = Flight.getDateTimeFromUser(s);
            System.out.println("Please enter end date");
            LocalDateTime end = Flight.getDateTimeFromUser(s);
            Airport.filterByDateRange(result, start, end);
        }
        result.forEach(System.out::println);
    }

    private static void initAirportFromFile(String filepath) {
        File f = new File(filepath);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("Error locating the specified file!");
            return;
        }
        try {
            airport = new Airport(f);
            System.out.println("\n~~~Successfully Loaded Argument File~~~\n");
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File Not Found!");
        }
    }

    public static boolean scanBoolean(Scanner scanner) {
        boolean input = false, gotInput = false;
        String scanned;
        do {
            System.out.println("Enter 'y' for yes, 'n' for no");
            scanned = scanner.next().toLowerCase();
            if (scanned.equals("y")) {
                input = true;
                gotInput = true;
            } else if (scanned.equals("n")) {
                gotInput = true;
            } else
                System.out.println("invalid input!");
        } while (!gotInput);
        return input;
    }
}
