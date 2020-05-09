/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;

/**
 *
 * @author Kingsley Osemwenkhae D00215130
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

public class Client
{
    private ArrayList<TollEvent> generatedTollEvents = new ArrayList<>();
         
    private BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket socket;
    Set<String> registration = new HashSet();
    
    
    
    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }
    
    public Client()
    {
         
        try{
            //socket
            this.socket = new Socket("localhost", 50000);  // connect to server socket, and open new socket
            
            //reader
            InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
            this.socketReader = new BufferedReader(isReader);

            //socket writer
            this.socketWriter= new PrintWriter(socket.getOutputStream(), true);// true=> auto flush buffers
            
            System.out.println("Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Client: Port# of Server :" + socket.getPort() ); 

        }
        catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        }
        
    }
    

    public void start()
    {
        try {
            
            
            //Json 
            JsonObject jsonRootObject;
            JsonReader jsonReader;
            JsonObject object;
            
            System.out.println("Client: This Client is running and has connected to the server\n");
            
            this.showMenu();
            String option = Keyboard.getStringInput("\nPlease Choose An Option: ");
            while( !option.equals("0") )
            {
                switch(option)
                {
                    case("1"):
                        System.out.println("\n");                       
                        this.getRegisteredVehicles();
                        System.out.println("\n");
                        break;
                        
                    case("2"):
                        System.out.println("\n");                       
                        this.heartbeat();
                        System.out.println("\n");
                        break;                
                        
                    case("3"):
                        System.out.println("\n");                       
                        this.generateTollEventsFromFile();
                        this.registerGeneratedTollEvents();
                        System.out.println("\n");
                        break; 
                        
                    case("4"):
                        System.out.println("\n");
                        this.billProcessing();
                        System.out.println("\n");
                        break;
                    default:                   
                        System.out.println("\nInvalid Option.\n");
                        break;                
                }
                this.showMenu();
                option = Keyboard.getStringInput("\nPlease Choose An Option: ");              
            }
            
            if(option.equalsIgnoreCase("0"))
            {
                this.closeSession();
            }
            
            socketReader.close();
            socketWriter.close();
            socket.close();      

        } 
        catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        }
        catch( JsonParsingException e )
        {
            System.out.println("Client Message: Json Parsing Exception: "+ e);
        }
        
    }
    
    
    
    public void showMenu()
    {
        System.out.println("========== MENU OPTION ==========");
        System.out.println("Option 0: Close Server Session");
        System.out.println("Option 1: Request And Save Registration Numbers From Server");
        System.out.println("Option 2: Send heartbeat");
        System.out.println("Option 3: Generate And Register Toll Events (Both Valid And Invalid)");
        System.out.println("Option 4: Bill Procesing");
    }
    
    
    
    public void getRegisteredVehicles()
    {
        //this.registration = new HashSet();
        
        //Json 
        JsonObject jsonRootObject;
        JsonReader jsonReader;
        JsonObject object;
            
        //form the json object
        jsonRootObject = Json.createObjectBuilder()
                        .add("PacketType", "GetRegisteredVehicles")
                        .build(); 

        this.socketWriter.println(jsonRootObject.toString());  // write command to socket

        //decode the response and print it out
        jsonReader = Json.createReader(this.socketReader);
        object = jsonReader.readObject();
        System.out.println("== Registration ==");
        for( int i = 0; i < object.getJsonArray("vehicles").size(); i++ )
        {
            this.registration.add( object.getJsonArray("vehicles").getString(i) );
            System.out.println(object.getJsonArray("vehicles").getString(i));
        }
        
        System.out.println("=== Server Response ===");
        System.out.println("PacketType: "+object.getString("PacketType"));
        System.out.println("Vehicle Registration has been gotten and saved successfully.");
    }
    
    
    public void heartbeat()
    {
        //Json Variables
        JsonObject jsonRootObject;
        JsonReader jsonReader;
        JsonObject object;
        
        //form the json object
        jsonRootObject = Json.createObjectBuilder()
                        .add("PacketType", "Heartbeat")
                        .build(); 

        this.socketWriter.println(jsonRootObject.toString());  // write command to socket

        //decode the response and print it out
        jsonReader = Json.createReader(this.socketReader);
        object = jsonReader.readObject();
        System.out.println("=== Server Response ===");
        System.out.println("PacketType: "+object.getString("PacketType"));
        System.out.println("Response: "+object.getString("response"));
    }
    
    
    public void generateTollEventsFromFile()
    {
        String fileName = Configuration.TOLLEVENT_READ_FILE;
        
        try (Scanner in = new Scanner(new java.io.File(fileName)))
        {
            //in.useDelimiter("[^A-Za-z0-9_]+");
             //in.useDelimiter(" ,");
              in.useDelimiter("[,\n;\t\r]+");
                        
            //process it line by line
            while( in.hasNextLine() )
            {
                String tollBoothId = in.next(); 
                String vehicleRegistration = in.next();                            
                long imageId = in.nextLong();    
                Timestamp time = Timestamp.from(Instant.parse( in.next() ));                                         
               
                TollEvent tollEvent = new TollEvent(tollBoothId, vehicleRegistration, imageId, time);
                
                if( !this.generatedTollEvents.contains(tollEvent) )
                {
                    this.generatedTollEvents.add(tollEvent);
                }
                if( in.hasNextLine() )
                {
                    in.nextLine();
                }
                
            }
            System.out.println("--- tollevents has been generated from file ---");
        }
        
        catch (FileNotFoundException exception)
        {
            System.out.println("File Was Not Found.");
            System.out.println("Ensure The Source File Is In The Correct Location And Also Ensure That The File Name Matches.");
        }
        catch (IllegalStateException e)
        {
            System.out.println("Scanner Is Closed.");
        }
    }
    
    
    public void registerGeneratedTollEvents()
    {
        if( this.registration.size() == 0 )
        {
            System.out.println("=== NOTE: All Vehicles Registration Will Be Marked as Invalid If Valid List Has Not Be Gotten From the Server First ===");
        }
        
        //Json Variables
        JsonObject jsonRootObject;
        JsonReader jsonReader;
        JsonObject object;
        
        for( int i = 0; i < this.generatedTollEvents.size(); i++ )
        {
            TollEvent tollEvent = this.generatedTollEvents.get(i);
            
            /*
            boolean isValid;
            for(String s : this.registration)
            {
                if( s.equalsIgnoreCase(tollEvent.getVehicleRegistration()) )
                {
                }
            }*/
            
            
            if(this.registration.contains(tollEvent.getVehicleRegistration()))
            {
                //form the json object
                jsonRootObject = Json.createObjectBuilder()
                                .add("PacketType", "RegisterValidTollEvent")
                                .add("TollBoothID", tollEvent.getTollBoothId())
                                .add("Vehicle Registration", tollEvent.getVehicleRegistration())
                                .add("Vehicle Image ID", tollEvent.getImageId())
                                .add("LocalDateTime", tollEvent.getTime().toInstant().toString())
                                .build(); 

                this.socketWriter.println(jsonRootObject.toString());  // write command to socket

                //decode the response and print it out
                jsonReader = Json.createReader(this.socketReader);
                object = jsonReader.readObject();
                System.out.println("=== Server Response ===");
                System.out.println("PacketType: "+object.getString("PacketType"));
                
                //delete tollEvent from client after registration
                System.out.println("--- deleting registered tollEvent from client side");
                this.generatedTollEvents.remove(tollEvent);
            }
            else
            {
                //form the json object
                jsonRootObject = Json.createObjectBuilder()
                                .add("PacketType", "RegisterInValidTollEvent")
                                .add("TollBoothID", tollEvent.getTollBoothId())
                                .add("Vehicle Registration", tollEvent.getVehicleRegistration())
                                .add("Vehicle Image ID", tollEvent.getImageId())
                                .add("LocalDateTime", tollEvent.getTime().toInstant().toString())
                                .build(); 

                this.socketWriter.println(jsonRootObject.toString());  // write command to socket

                //decode the response and print it out
                jsonReader = Json.createReader(this.socketReader);
                object = jsonReader.readObject();
                System.out.println("=== Server Response ===");
                System.out.println("PacketType: "+object.getString("PacketType"));
                
                //delete tollEvent from client after registration
                System.out.println("--- deleting registered tollEvent from client side");
                this.generatedTollEvents.remove(tollEvent);
            } 
        }
    }
    
    
    public void closeSession()
    {
        JsonObject jsonRootObject = Json.createObjectBuilder()
                        .add("PacketType", "Close")
                        .build(); 

        this.socketWriter.println(jsonRootObject.toString());
    }
    
    
    public void billProcessingSubMenu()
    {
        System.out.println("*************************************************");
        System.out.println("************ BIll Processing Submenu ************");
        System.out.println("Option 0: Back To Main Menu");
        System.out.println("Option 1: Find a Customer By Name And Show Total Tollevent BIll");
        System.out.println("Option 2: Show All Bills");
    }
    
    
    public void showAllBills()
    {
        //Json Variables
        JsonObject jsonRootObject;
        JsonReader jsonReader;
        JsonObject object;
        
        //form the json object
        jsonRootObject = Json.createObjectBuilder()
                        .add("PacketType", "GetAllBills")
                        .build(); 

        this.socketWriter.println(jsonRootObject.toString());  // write command to socket

        //decode the response and print it out
        jsonReader = Json.createReader(this.socketReader);
        object = jsonReader.readObject();
        System.out.println("====== Server Response =====");
        System.out.println("=== List Of Bill For All People ===");
        for( int i = 0; i < object.getJsonArray("result").size(); i = i+2 )
        {
            String name = object.getJsonArray("result").get(i).toString();
            String cost = object.getJsonArray("result").get(i+1).toString();
            System.out.println( "Name : "+name);
            System.out.println("Bill: "+cost);
            System.out.println("\n\n");
        }
    }
    
    public void showTotalTolleventBillByName()
    {
        String name = Keyboard.getStringInput("\nPlease Enter Name: ");
       
        //Json Variables
        JsonObject jsonRootObject;
        JsonReader jsonReader;
        JsonObject object;
        
        //form the json object
        jsonRootObject = Json.createObjectBuilder()
                        .add("PacketType", "GetCustomerTotalBillByName")
                        .add("name", name)
                        .build(); 

        this.socketWriter.println(jsonRootObject.toString());  // write command to socket

        //decode the response and print it out
        jsonReader = Json.createReader(this.socketReader);
        object = jsonReader.readObject();
        System.out.println("=== Server Response ===");
        System.out.println("PacketType: "+object.getString("PacketType"));
        System.out.println("Name: "+name); 
        System.out.println("Bill: "+object.get("cost"));  
        
    }
    
    
    public void showOnlyRegistrationTolleventBill ()
    {
        String registration = Keyboard.getStringInput("\nPlease Enter Vehicle Registration: ");
        //Json Variables
        JsonObject jsonRootObject;
        JsonReader jsonReader;
        JsonObject object;
        
        //form the json object
        jsonRootObject = Json.createObjectBuilder()
                        .add("PacketType", "GetTotalBillForOnlyOneRegistrationNumber")
                        .add("registration", registration)
                        .build(); 

        this.socketWriter.println(jsonRootObject.toString());  // write command to socket

        //decode the response and print it out
        jsonReader = Json.createReader(this.socketReader);
        object = jsonReader.readObject();
        System.out.println("===== Server Response =====");
        System.out.println("PacketType: "+object.getString("PacketType"));
        System.out.println("Vehicle Registration: "+registration);
        System.out.println("Bill: "+object.get("cost"));      
        
    }
    
    
    
    public void billProcessing()
    {              
        this.billProcessingSubMenu();
        String option = Keyboard.getStringInput("\nPlease Choose An Option: ");
        while( !option.equals("0") )
        {
            switch(option)
            {
                case("1"):
                    System.out.println("\n");                       
                    this.showTotalTolleventBillByName();
                    System.out.println("\n");
                    break;

                case("2"):
                    System.out.println("\n");                       
                    //this.showOnlyRegistrationTolleventBill();
                    this.showAllBills();
                    System.out.println("\n");
                    break;                
                default:                   
                    System.out.println("\nInvalid Option.\n");
                    break;                
            }
            this.billProcessingSubMenu();
            option = Keyboard.getStringInput("\nPlease Choose An Option: ");              
        }
    }
    
}
