package MDVRP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.lang.Math;

import Utils.Euclidian;


public class Manager {
    private List<Customer> customers = new ArrayList<>();
    private List<Depot> depots = new ArrayList<>();
    private double borderlineThreshold;


    public Manager(String problemFilepath, double borderlineThreshold) {
        this.borderlineThreshold = borderlineThreshold;
        // read data and initialize customers and depots
        this.readData(problemFilepath);
    }


    public List<Customer> getCustomers() { return this.customers; }
    public List<Depot> getDepots()       { return this.depots; }


    private void readData(String problemFilepath) {
        try {
            File myObj = new File(problemFilepath);
            Scanner fileReader = new Scanner(myObj);

            // Read the first line to get number of vehicles, number of customers, and number of depots
            String firstLine = fileReader.nextLine().trim();
            String[] firstLineArrSplit = firstLine.split("\\s+");

            int nVehicles = Integer.parseInt(firstLineArrSplit[0]);
            int nCustomers = Integer.parseInt(firstLineArrSplit[1]);
            int nDepots = Integer.parseInt(firstLineArrSplit[2]);

            // read each depots maximum specifications
            List<Integer> maxRouteDurationsPerDepot = new ArrayList<>();
            List<Integer> maxLoadEachVehiclePerDepot = new ArrayList<>();
            int lineCounter = 0;
            while (fileReader.hasNextLine() && lineCounter < nDepots) {
                String line = fileReader.nextLine().trim();
                String[] lineArrSplit = line.split("\\s+");

                int maxDuration = Integer.parseInt(lineArrSplit[0]);
                int maxVehicleLoad = Integer.parseInt(lineArrSplit[1]);
                maxRouteDurationsPerDepot.add(maxDuration);
                maxLoadEachVehiclePerDepot.add(maxVehicleLoad);
                lineCounter++;
            }

            // then read customer data and create customers
            lineCounter = 0;
            while (fileReader.hasNextLine() && lineCounter < nCustomers) {
                String line = fileReader.nextLine().trim();
                String[] lineArrSplit = line.split("\\s+");

                int customerId = Integer.parseInt(lineArrSplit[0]);
                int x = Integer.parseInt(lineArrSplit[1]);
                int y = Integer.parseInt(lineArrSplit[2]);
                int duration = Integer.parseInt(lineArrSplit[3]);
                int demand = Integer.parseInt(lineArrSplit[4]);

                // create and add customer
                Customer customer = new Customer(customerId, x, y, duration, demand);
                this.customers.add(customer);
                lineCounter++;
            }

            // at last, read depots coordinates and create depots
            lineCounter = 0;
            while (fileReader.hasNextLine() && lineCounter < nDepots) {
                String line = fileReader.nextLine().trim();
                String[] lineArrSplit = line.split("\\s+");
                //////System.out.println(Arrays.toString(lineArrSplit));
                int x = Integer.parseInt(lineArrSplit[1]);
                int y = Integer.parseInt(lineArrSplit[2]);
                int maxDuration = maxRouteDurationsPerDepot.get(lineCounter);
                int maxVehicleLoad = maxLoadEachVehiclePerDepot.get(lineCounter);

                // create and add depot
                Depot depot = new Depot(lineCounter + 1, x, y, nVehicles, maxDuration, maxVehicleLoad);
                this.depots.add(depot);
                lineCounter++;
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file...");
            e.printStackTrace();
        }

    }


    public void assignCustomersToDepots() {
        for (Customer customer: this.customers) {
            boolean isBorderlineCustomer = false;
            int[] customerCoordinates = new int[]{customer.getX(), customer.getY()};

            // initial values
            Depot firstDepot = this.depots.get(0);
            double shortestDistance = 1000000000;
            Depot currentShortestDepot = firstDepot;
            double shortestOtherDistance = 1000000000;

            // loop through depots to find the nearest depot and the distance to that depot,
            // and also the distance to second nearest depot (to check if on borderline)
            for (Depot depot: this.depots) {
                int[] depotCoordinates = new int[]{depot.getX(), depot.getY()};
                double distance = Euclidian.distance(customerCoordinates, depotCoordinates);
                if (distance < shortestDistance) {
                    if (distance < shortestOtherDistance) {
                        shortestOtherDistance = shortestDistance;
                    }
                    shortestDistance = distance;
                    currentShortestDepot = depot;
                }
                else if (distance < shortestOtherDistance) {
                    shortestOtherDistance = distance;
                }
            }

            // check if customer is on borderline between nearest and second nearest depot, with margin = threshold
            if (Math.abs(shortestDistance - shortestOtherDistance) < this.borderlineThreshold) {
                System.out.println(currentShortestDepot);
                isBorderlineCustomer = true;
            }

            // add customer to the depot closest to the customer
            currentShortestDepot.addCustomer(customer, isBorderlineCustomer);
        }
    }

}
