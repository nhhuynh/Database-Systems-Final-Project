import java.sql.*;
import java.util.Scanner;

public class Lab4 {



    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = DriverManager.getConnection("jdbc:odbc:Project");
            stmt = con.createStatement();

            /*
            Add commands and text
             */

            /*String createTrip = "CREATE TABLE Trip " +
                    "( TripNumber char(50) not null," +
                    "StartLocationName varchar(50)," +
                    "DestinationName varchar(50)," +
                    "PRIMARY KEY (TripNumber))";

            String createTripOffer = "CREATE TABLE TripOffering" +
                    "( TripNumber char(50) not null," +
                    "Dates varchar(50) not null," +
                    "ScheduledStartTime varchar(50) not null," +
                    "ScheduledArrivalTime varchar(50)," +
                    "DriverName  varchar(50), " +
                    "BusID varchar(50)," +
                    "PRIMARY KEY (TripNumber, Dates, ScheduledStartTime)," +
                    "Foreign KEY (TripNumber) REFERENCES Trip(TripNumber)," +
                    "FOREIGN Key (DriverName) References Driver(DriverName)," +
                    "FOREIGN Key (BusID) REFERENCES Bus(BusID))";

            String createBus = "Create Table Bus" +
                    "(BusID VARCHAR(50) not null," +
                    "Model VARCHAR(50)," +
                    "Years VARCHAR(50)," +
                    "PRIMARY KEY (BusID))";

            String createDriver = "Create Table Driver" +
                    "(DriverName VARCHAR (50) not null," +
                    "DriverTelephoneNumber VARCHAR (50)," +
                    "PRIMARY KEY (DriverName))";

            String createStop = "Create Table Stop" +
                    "(StopNumber VARCHAR (50)," +
                    "StopAddress VARCHAR (50)," +
                    "PRIMARY KEY (StopNumber))";

            String createActualTripStopInfo = "Create Table ActualTripStopInfo" + "(TripNumber VARCHAR (50) not null," +
                    "Dates varchar(50) not null," +
                    "ScheduledStartTime varchar(50) not null," +
                    "StopNumber VARCHAR (50) not null," +
                    "ScheduledArrivalTime VARCHAR(50)," +
                    "ActualStartTime VARCHAR (50)," +
                    "ActualArrivalTime VARCHAR (50)," +
                    "NumberOfPassengerIn VARCHAR (50)," +
                    "NumberOfPassengerOut VARCHAR (50)," +
                    "Primary Key(TripNumber,Dates,ScheduledStartTime,StopNumber)," +
                    "FOREIGN Key(TripNumber) REFERENCES Trip(TripNumber)," +
                    "FOREIGN Key(StopNumber) References Stop(StopNumber))";

            String createTripStopInfo = "Create Table TripStopInfo" + "(TripNumber VARCHAR (50) not null," +
                    "StopNumber VARCHAR (50) not null," +
                    "SequenceNumber VARCHAR (50), " +
                    "DrivingTime VARCHAR (50)," +
                    "Primary Key (TripNumber, StopNumber)," +
                    "FOREIGN Key (TripNumber) References Trip(TripNumber)," +
                    "FOREIGN Key(StopNumber) REFERENCES Stop(StopNumber))";

            stmt.execute(createTrip);
            stmt.execute(createBus);
            stmt.execute(createDriver);
            stmt.execute(createTripOffer);
            stmt.execute(createStop);
            stmt.execute(createActualTripStopInfo);
            stmt.execute(createTripStopInfo);*/

            Scanner kb = new Scanner(System.in);
            boolean user = false;
            while(!user)
            {
                System.out.println("Menu:\n1. Display Schedule\n2. Edit Schedule\n3. Display Stops\n4. Display Weekly Schedule\n5. Add a Drive\n6. Add a Bus\n7. Delete a Bus\n8. Insert Actual Trip Offering");
                int temp = kb.nextInt();
                if(temp == -1)
                {
                    user = true;
                    con.close();
                }
                else if (temp == 6)
                {
                    insertBus(con);
                }
                else if(temp == 5)
                {
                    insertDrive(con);
                }
                else if(temp == 7)
                {
                    deletebus(con);
                }
                else if(temp == 2)
                {
                    changetrip(con);
                }
                else if(temp == 8){
                    insertActualTrip(con);
                }
                else if(temp == 1)
                {
                    selectTrip(con);
                }
                else if (temp == 3)
                {
                    selectTripStopInfo(con);
                }
                else if(temp == 4)
                {
                    selectWeekTrip(con);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectWeekTrip(Connection con) throws SQLException {
        Scanner kb = new Scanner(System.in);
        String driver = kb.nextLine();

        PreparedStatement ps = con.prepareStatement("Select * " +
                "From TripOffering " +
                "Where DriverName = ?");

        ps.setString(1, driver);
        ResultSet rs = ps.executeQuery();
	System.out.println("TripNumber -> Scheduled Start Time -> Date -> Scheduled Arrival Time -> DriverName -> BusID");
        while(rs.next()){
            System.out.println(rs.getString("TripNumber") + " " + rs.getString("ScheduledStartTime") + " " + rs.getString("Dates") + " " + rs.getString("ScheduledArrivalTime")+ " " + rs.getString("DriverName") + " " + rs.getString("BusID"));
        }
    }

    private static void selectTripStopInfo(Connection con) throws SQLException {
        Scanner kb = new Scanner(System.in);
        String trip = kb.nextLine();

        PreparedStatement ps = con.prepareStatement("Select * " +
                "From TripStopInfo " +
                "Where TripNumber = ? ");
        ps.setString(1, trip);
        ResultSet rs = ps.executeQuery();
	System.out.println("TripNumber -> StopNumber -> SequenceNumber -> DrivingTime");
        while(rs.next()){
            System.out.println(rs.getString("TripNumber") + " " + rs.getString("StopNumber") + " " + rs.getString("SequenceNumber") + " " + rs.getString("DrivingTime"));
        }
    }

    private static void selectTrip(Connection con) throws SQLException {
        Scanner kb = new Scanner(System.in);
	System.out.println("Starting Location: ");
        String startloc = kb.nextLine();
	System.out.println("Destination Name: ");
        String destname = kb.nextLine();
	System.out.println("Date: ");
        String dates = kb.nextLine();

        String temp = "Select [T.StartLocationName], [T.DestinationName], [TT.Dates], [TT.ScheduledStartTime], [TT.ScheduledArrivalTime], [TT.DriverName], [TT.BusID] " +
                "From Trip T, TripOffering TT " +
                "Where [T.TripNumber] = [TT.TripNumber] AND T.StartLocationName = ? and T.DestinationName = ? and TT.Dates = ?";

        PreparedStatement ps = con.prepareStatement(temp);

        ps.setString(1, startloc);
        ps.setString(2, destname);
        ps.setString(3, dates);
        System.out.println(ps.toString());
        ResultSet rs = ps.executeQuery();
	System.out.println("Starting Location -> Destination Name -> Date -> Scheduled Start Time -> ScheduledArrivalTime -> DriverName -> BusID");
        while(rs.next())
        {
            System.out.println(rs.getString("StartLocationName") + " " + rs.getString("DestinationName") + " " + rs.getString("Dates") + " " + rs.getString("ScheduledStartTime") + " " + rs.getString("ScheduledArrivalTime") + " " + rs.getString("DriverName") + " " + rs.getString("BusID"));
        }
        ps.close();

    }

    private static void insertActualTrip(Connection con) throws SQLException {
        Scanner kb = new Scanner(System.in);
	System.out.println("Trip Number: ");
        String trip = kb.nextLine();
	System.out.println("Date: ");
        String dates = kb.nextLine();
	System.out.println("Scheduled Start Time: ");
        String schedule = kb.nextLine();
	System.out.println("Stop Number: ");
        String stop = kb.nextLine();
	System.out.println("Scheduled Arrival Time: ");
        String schedulea = kb.nextLine();
	System.out.println("Actual Start Time: ");
        String starta = kb.nextLine();
	System.out.println("Actual Arrival Time: ");
        String arrivala = kb.nextLine();
	System.out.println("Number Of Passengers In: ");
        String numpassin = kb.nextLine();
	System.out.println("Number of Passengers Out: ");
        String numpassout = kb.nextLine();

        PreparedStatement ps = con.prepareStatement("Insert into ActualTripStopInfo(TripNumber, Dates, ScheduledStartTime, StopNumber, ScheduledArrivalTime, ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, NumberOfPassengerOut) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        ps.setString(1,trip);
        ps.setString(2, dates);
        ps.setString(3,schedule);
        ps.setString(4, stop);
        ps.setString(5, schedulea);
        ps.setString(6, starta);
        ps.setString(7,arrivala);
        ps.setString(8,numpassin);
        ps.setString(9,numpassout);
        ps.executeUpdate();
        ps.close();
	System.out.println("Actual Trip Stop Info Added to Database!");
    }

    private static void changetrip(Connection con) throws SQLException {
        Scanner kb = new Scanner(System.in);
	System.out.println("1. Delete Trip Offering\n2. Add Trip Offerings\n3. Change Driver\n4. Change Bus");
        int temp = kb.nextInt();
        kb.nextLine();
        if(temp == 1)
        {
	    System.out.println("Trip Number: ");
            String trip = kb.nextLine();
	    System.out.println("Date: ");
            String dates = kb.nextLine();
	    System.out.println("Scheduled Start Time: ");
            String schedule = kb.nextLine();
            PreparedStatement ps = con.prepareStatement("Delete FROM TripOffering Where TripNumber = ? AND Dates = ? and ScheduledStartTime = ?");
            ps.setString(1,trip);
            ps.setString(2, dates);
            ps.setString(3, schedule);
            ps.executeUpdate();
            ps.close();
	System.out.println("Deleted from Database!");
        }
        else if (temp == 2)
        {
	int temp2 = 1;
	String trip;
	String dates;
	String schedule;
	String schedulea;
	String driver;
	String bus
	While (temp2 == 1){
	    System.out.println("Trip Number: ");
            trip = kb.nextLine();
	    System.out.println("Date: ");
            dates = kb.nextLine();
	    System.out.println("Scheduled Start Time: ");
            schedule = kb.nextLine();
	    System.out.println("Scheduled Arrival Time: ");
            schedulea = kb.nextLine();
	    System.out.println("DriverName: ");
            driver = kb.nextLine();
	    System.out.println("BusID: ");
            bus = kb.nextLine();

            PreparedStatement ps = con.prepareStatement("Insert into TripOffering(TripNumber, Dates, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID) values(?,?,?,?,?,?)");

            ps.setString(1, trip);
            ps.setString(2, dates);
            ps.setString(3, schedule);
            ps.setString(4, schedulea);
            ps.setString(5, driver);
            ps.setString(6, bus);
            ps.executeUpdate();
            ps.close();
	    System.out.println("Added To Database!");
	    System.out.println("More?\n1. Yes\n2. No");
	    temp2 = kb.nextInt();
	    kb.nextLine();
	}
        }
        else if (temp == 3)
        {
	    System.out.println("Trip Number: ");
            String trip = kb.nextLine();
	    System.out.println("Date: ");
            String dates = kb.nextLine();
	    System.out.println("Scheduled Start Time: ");
            String schedule = kb.nextLine();
	    System.out.println("New DriverName: ");
            String driver = kb.nextLine();

            PreparedStatement ps = con.prepareStatement("Update TripOffering Set DriverName = ? where TripNumber = ? and Dates = ? and ScheduledStartTime = ? ");
            ps.setString(1, driver);
            ps.setString(2, trip);
            ps.setString(3, dates);
            ps.setString(4, schedule);
            ps.executeUpdate();
            ps.close();
	    System.out.println("Changed Driver Name!");
        }
        else if(temp == 4)
        {
	    System.out.println("Trip Number: ");
            String trip = kb.nextLine();
	    System.out.println("Date: ");
            String dates = kb.nextLine();
	    System.out.println("Scheduled Start Time: ");
            String schedule = kb.nextLine();
	    System.out.println("New BusID: ");
            String bus = kb.nextLine();

            PreparedStatement ps = con.prepareStatement("Update TripOffering Set BusID = ? where TripNumber = ? and Dates = ? and ScheduledStartTime = ? ");
            ps.setString(1, bus);
            ps.setString(2, trip);
            ps.setString(3, dates);
            ps.setString(4, schedule);
            ps.executeUpdate();
            ps.close();
	    System.out.println("Changed BusID!");
        }
    }

    private static void deletebus(Connection c) throws SQLException {
        Scanner kb = new Scanner(System.in);
	System.out.println("Bus ID: ");
        String busid = kb.nextLine();
        PreparedStatement ps = c.prepareStatement("Delete FROM Bus Where BusID = ?");
        ps.setString(1,busid);
        ps.executeUpdate();
        ps.close();
	System.out.println("Bus Deleted From Database!");

    }

    private static void insertDrive(Connection con) throws SQLException {
        Scanner kb = new Scanner(System.in);
	System.out.println("Driver Name: ");
        String drivername = kb.nextLine();
	System.out.println("Telephone Number: ");
        String tele = kb.nextLine();
        PreparedStatement ps = con.prepareStatement("Insert into Driver(DriverName, DriverTelephoneNumber) values (?,?)");

        ps.setString(1, drivername);
        ps.setString(2, tele);
        ps.executeUpdate();
        ps.close();
	System.out.println("Added Driver To Database!");

    }

    private static void insertBus(Connection con) throws SQLException {
        Scanner kb = new Scanner(System.in);
	System.out.println("Bus ID: ");
        String busid = kb.nextLine();
	System.out.println("Bus Model: ");
        String model = kb.nextLine();
	System.out.println("Bus Year: ");
        String years = kb.nextLine();

        PreparedStatement ps = con.prepareStatement("Insert into Bus(BusID, Model, Years) values(?,?,?)");

        ps.setString(1, busid);
        ps.setString(2, model);
        ps.setString(3, years);
        ps.executeUpdate();
        ps.close();
	System.out.println("Added bus to the Database!");
    }


}
