/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author kingsley Osemwenkhae D00215130
 */


public class MySqlTollEventDAO 
{
    private Connection connection;
    
    
    public MySqlTollEventDAO()
    {
        try{
            this.connection = this.getConnection();
        }
        catch( Exceptions e ){
            System.out.println("Connection failed " + e.getMessage());
            System.exit(2);
        }     
    }
    
    public Connection getConnection() throws Exceptions 
    {
        
        String driver = Configuration.DATABASE_DRIVER;
        String url = Configuration.DATABASE_URL + Configuration.DATABASE_NAME;
        String username = Configuration.DATABASE_USERNAME;
        String password = Configuration.DATABASE_PASSWORD;
        Connection con = null;
        
        try 
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } 
        catch (ClassNotFoundException ex1) 
        {
            System.out.println("Failed to find driver class " + ex1.getMessage());
            System.exit(1);
            
        } 
        catch (SQLException ex2) 
        {
            System.out.println("Connection failed " + ex2.getMessage());
            System.exit(2);
        }
        return con;
    }
    

    public void closeConnection()
    {
        try 
        {
            if (this.connection != null) 
            {
                this.connection.close();
                this.connection = null;                
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Failed to close connection: " + e.getMessage());
            System.exit(1);
        }
    }
    
    //String query = "INSERT INTO tollEvents (vehicleRegistration, imageId, time) VALUES (?, ?, ?)";
    
    public void markTollEventAsProcessedTypeNameAndRegistration( String type, String object )
    {
        
        ArrayList<Integer> list = new ArrayList<>();
        
        if( type.equalsIgnoreCase("name") )
        {
            list = this.getTollEventIdListFromName(object);
        }
        else
        {
            list = this.getTollEventIdListFromRegistration(object);
        }
        
        //Connection connection = null;
        String query = "INSERT INTO tollevents (processed) VALUES ('Yes')";
        
        try
        {           
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            for( int i = 0; i < list.size(); i++ )
            {                
                preparedStmt.setString(1, object);
                preparedStmt.addBatch();
            }
            preparedStmt.executeBatch();
            
            System.out.println("-- Tollevents Marked As Processed ---");         

        }
        catch (SQLException se)
        {
            System.out.println("issue with the sql query");
            System.out.println(se.getMessage());
        }      
    }
    
    
    public ArrayList<Integer> getTollEventIdListFromName( String name )
    {
        ArrayList<Integer> list = new ArrayList<>();
        
        String query = "SELECT toll_id FROM tollevents, vehicles, customers WHERE customer_name=? AND customers.customer_id=vehicles.vehicle_id AND vehicles.vehicle_id=tollevents.vehicle_id";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setString(1, name);
            
            ResultSet result = preparedStmt.executeQuery();
            
            while (result.next()) 
            {   
                list.add( result.getInt("toll_id") );              
            }                            
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return list;
    }
    
    public ArrayList<Integer> getTollEventIdListFromRegistration( String registration )
    {
        ArrayList<Integer> list = new ArrayList<>();
        String query = "SELECT toll_id FROM tollevents, vehicles WHERE vehicle_registration=? AND  tollevents.vehicle_id=vehicles.vehicle_id";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setString(1, registration);
            
            ResultSet result = preparedStmt.executeQuery();
            
            while (result.next()) 
            {   
                list.add( result.getInt("toll_id") );              
            }                            
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return list;
    }
    
    
    public void addTollEvent(String tollBoothID, String vehicleRegistration, long imageId, Timestamp time ) throws Exceptions
    {
        //use the registration number to find the vehicle id
        int vehicleId = this.getVehicleIdFromRegistationNumber(vehicleRegistration);
               
        String query = "INSERT INTO tollevents (toll_booth_id, vehicle_id, image_id, time) VALUES (?, ?, ?, ?)";
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
           
            preparedStmt.setString(1, tollBoothID);
            preparedStmt.setInt(2, vehicleId);
            preparedStmt.setLong(3, imageId);
            preparedStmt.setTimestamp(4, time);
            
            
            if( preparedStmt.execute() )
                System.out.println("Isert Done Successfully.");
            

        }
        catch (SQLException se)
        {
            System.out.println("issue with the sql query");
            System.out.println(se.getMessage());
        }      
    }
    
    
    public  int getVehicleIdFromRegistationNumber( String registrationNumber )
    {
        int id = 0;
        String query = "SELECT vehicle_id FROM vehicles WHERE vehicle_registration=? LIMIT 1";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setString(1, registrationNumber);
            
            ResultSet result = preparedStmt.executeQuery();
            
            while (result.next()) 
            {
                id = result.getInt("vehicle_id");
                break;
            }
                           
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return id;
    }
    
    
    public double getBillByPersonName( String name )
    {
        double cost = 0;
        String query = "SELECT customer_name,  (Sum(vehicle_cost)) AS cost FROM tollevents NATURAL JOIN customers NATURAL JOIN customer_vehicle NATURAL JOIN vehicles NATURAL JOIN vehicle_type_cost WHERE customer_name = ?";
        //String query = "SELECT vehicle_id FROM vehicles WHERE vehicle_registration=? LIMIT 1";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setString(1, name);
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {
                cost = cost + result.getDouble("cost");
                //break;
            }
                             
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
            System.out.println(error.getLocalizedMessage());
            error.printStackTrace();
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return cost;
    }
    
    public ArrayList getAllBillsByName( )
    {
        
        ArrayList<Object> answer = new ArrayList<>();
        String query = "SELECT customer_name,  (Sum(vehicle_cost)) AS cost FROM tollevents NATURAL JOIN customers NATURAL JOIN customer_vehicle NATURAL JOIN vehicles NATURAL JOIN vehicle_type_cost GROUP BY customer_name";
        //String query = "SELECT vehicle_id FROM vehicles WHERE vehicle_registration=? LIMIT 1";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);                      
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {             
                answer.add( result.getString("customer_name") );
                answer.add( result.getDouble("cost") );              
                
            }
                             
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
            System.out.println(error.getLocalizedMessage());
            error.printStackTrace();
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return answer;
    }
    
    public double getBillByVehicleRegistrationNumber( String registration )
    {
        double cost = 0;
        System.out.println(registration);
        
        String query = "SELECT (SELECT customer_name FROM customers, vehicles WHERE vehicle_registration=? AND customer_id = vehicle_id) as name, vehicle_registration,  (Sum(vehicle_cost)) as cost FROM tollevents NATURAL JOIN customers NATURAL JOIN customer_vehicle NATURAL JOIN vehicles NATURAL JOIN vehicle_type_cost WHERE vehicle_registration = ?";      
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setString(1, registration);
            preparedStmt.setString(2, registration);
            
            ResultSet result = preparedStmt.executeQuery();
            
            while (result.next()) 
            {
                cost = result.getDouble("cost");
                System.out.println(cost);
                break;
            }
                             
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage() );
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return cost;
    }
    
}