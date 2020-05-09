/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author kingsley Osemwenkhae D00215130
 */
public class Server
{
    private Set registration = new HashSet();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    public Server()
    {
        this.registration = this.processRegisteredVehicleNumberFromFile();
    }
    
    public void start()
    {
        try
        {
            ServerSocket ss = new ServerSocket(50000);  // set up ServerSocket to listen for connections on port 8080

            System.out.println("Server: Server started. Listening for connections on port 50000...");
            
            int clientNumber = 0;  // a number for clients that the server allocates as clients connect
            
            while (true)    // loop continuously to accept new client connections
            {
                Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection, 
                                                // and open a new socket to communicate with the client
                clientNumber++;
                
                System.out.println("Server: Client " + clientNumber + " has connected.");
                
                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());
 
                Thread thread = new Thread(new ClientHandler(socket, this.registration, clientNumber)); // create a new ClientHandler for the client,
                thread.start();                                             // and run it in its own thread
                
                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        }
        catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }
    
    
    private Set<String> processRegisteredVehicleNumberFromFile()
    {
        String fileName = Configuration.VEHICLES_READ_FILE;
        
        Set<String> numbers = new HashSet();
        
        try (Scanner in = new Scanner(new java.io.File(fileName)))
        {           
            in.useDelimiter("[,\n;\t\r]+");
            while( in.hasNextLine() )
            { 
                in.next();
                String vehicleRegistration = in.next();
                numbers.add(vehicleRegistration);
                in.nextLine();               
            }
            System.out.println("\nPrcessing Finished For Registered Vehicles Numbers File.\n");
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
        return numbers;
    }
    
    
    public Set getRegistration()
    {
        return this.registration;
    }
    
}
