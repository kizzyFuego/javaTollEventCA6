/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

/**
 *
 * @author kingsley Osemwenkhae D00215130
 */

public class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
    {
        Socket socket;
        Set<String> vehicleRegistration = new HashSet();
        int clientNumber;
        BufferedReader socketReader;
        PrintWriter socketWriter;
        ArrayList<TollEvent> invalidTollEventsList = new ArrayList<>();
        
        

        public ClientHandler(Socket clientSocket, Set registration, int clientNumber)
        {
            try
            {
                this.socket = clientSocket;  // store socket ref for closing 
                
                this.vehicleRegistration = registration;
                
                this.clientNumber = clientNumber;  // ID number that we are assigning to this client
                
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);              
                
                OutputStream os = clientSocket.getOutputStream();     
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer
               
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            JsonReader reader = Json.createReader(this.socketReader);
            JsonObject object = reader.readObject();
            String packetType = object.getString("PacketType");
            
            try
            {
                while ( !(packetType.equals("Close")) )
                {
                   
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + packetType);
                    
                    
                    if (packetType.equals("GetRegisteredVehicles"))
                    {
                        JsonBuilderFactory factory = Json.createBuilderFactory(null);
                        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
                        
                        for( String s : this.vehicleRegistration )
                        {
                            arrayBuilder.add(s);                           
                        }
                                               
                        // build the JsonArray
                        JsonArray jsonArray = arrayBuilder.build();
                        
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", "GetRegisteredVehicles" )
                            .add("vehicles", jsonArray)
                            .build();                     
                        socketWriter.println(jsonRootObject.toString());
                                         
                    } 
                    else if (packetType.equals("Heartbeat"))
                    {
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", "Heartbeat")
                            .add("response", "Heartbeat response")
                            .build();               
                        socketWriter.println(jsonRootObject.toString());  // send message to client
                       
                    }
                    else if (packetType.equals("RegisterValidTollEvent"))
                    {
                        //send decoded message to server
                        String boothId = object.getString("TollBoothID");
                        String registration = object.getString("Vehicle Registration");
                        long imageId = object.getInt("Vehicle Image ID");
                        
                        Timestamp time = Timestamp.from(Instant.parse( object.getString("LocalDateTime") ));
                        
                        MySqlTollEventDAO dao = new MySqlTollEventDAO();
                        dao.addTollEvent(boothId, registration, imageId, time);                                                                                                               
                        
                        //send back reply
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", "RegisteredValidTollEvent")
                            .build();               
                        socketWriter.println(jsonRootObject.toString());  // send message to client
                       
                    }
                    
                    else if( packetType.equals("RegisterInValidTollEvent") )
                    {
                        String boothId = object.getString("TollBoothID");
                        String registration = object.getString("Vehicle Registration");
                        long imageId = object.getInt("Vehicle Image ID");
                        
                        Timestamp time = Timestamp.from(Instant.parse( object.getString("LocalDateTime") ));
                        
                        //add Tollevent to invalid Tollevent ArrayList
                        this.invalidTollEventsList.add( new TollEvent(boothId, registration, imageId, time)  );
                        
                        //then print out
                        System.out.println("--- Invalid TollEvent List ---");
                        for( TollEvent tollEvent : this.invalidTollEventsList )
                        {
                            tollEvent.toString();
                        }
                        
                        
                        //send back reply
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", "RegisteredInValidTollEvent")
                            .build();               
                        socketWriter.println(jsonRootObject.toString());
                    }
                    
                    else if( packetType.equals("GetCustomerTotalBillByName") )
                    {
                        String name = object.getString("name");
                        MySqlTollEventDAO dao = new MySqlTollEventDAO();
                        double cost = dao.getBillByPersonName(name);
                        //marks the tollevent as processed
                        dao.markTollEventAsProcessedTypeNameAndRegistration("name", name);
                        
                        //send back reply
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", "GetCustomerTotalBillByName")
                            .add("cost", cost)                               
                            .build();               
                        socketWriter.println(jsonRootObject.toString());
                    }
                    else if( packetType.equals("GetTotalBillForOnlyOneRegistrationNumber") ) //GetTotalBillForOnlyOneRegistrationNumber
                    {
                        String registration = object.getString("registration");
                        MySqlTollEventDAO dao = new MySqlTollEventDAO();
                        double cost = dao.getBillByVehicleRegistrationNumber(registration);
                        
                        
                        
                        //send back reply
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", "GetTotalBillForOnlyOneRegistrationNumber")
                            .add("cost", cost)                               
                            .build();               
                        socketWriter.println(jsonRootObject.toString());
                    }
                    else if( packetType.equals("GetAllBills") ) //GetTotalBillForOnlyOneRegistrationNumber
                    {
                        //String registration = object.getString("registration");
                        MySqlTollEventDAO dao = new MySqlTollEventDAO();
                        ArrayList answer = dao.getAllBillsByName();
                        
                        JsonBuilderFactory factory = Json.createBuilderFactory(null);
                        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();                        
                        
                        for( int i = 0; i < answer.size(); i = i+2 )
                        {
                            arrayBuilder.add(answer.get(i).toString());
                            arrayBuilder.add(answer.get(i+1).toString());
                            /*arrayBuilder.add(Json.createObjectBuilder()
                                    .add()
                                    .add(answer.get(i+1).toString())                                    
                                    .build()
                            );*/
                        }
                        //send back reply
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", "GetAllBills")
                            .add("result", arrayBuilder)                               
                            .build();        
                        socketWriter.println(jsonRootObject.toString());
                    }
                    
                    else
                    {
                        JsonObject jsonRootObject = Json.createObjectBuilder()
                            .add("PacketType", packetType )
                            .add("response", "Invalid Information Was Queried.")
                            .build();                     
                        socketWriter.println(jsonRootObject.toString());  // send message to client
                    }
                    
                    //waiting for further intrution from the connected client
                    reader = Json.createReader(this.socketReader);
                    object = reader.readObject();
                    packetType = object.getString("PacketType");
                }
                
                socketReader.close();
                socketWriter.close();
                socket.close();
                
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            catch( JsonParsingException e )
            {
                System.out.println("Server Message: Json Parsing Exception: "+ e);
            }
            catch( Exceptions e )
            {
                System.out.println("Excepption Ocurred.");
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }