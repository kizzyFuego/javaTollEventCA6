/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;

import java.util.Scanner;

/**
 *
 * @author kizzy
 */
public class Configuration
{
    public static final String START_UP_READ_FILE = "";
    public static final String TOLLEVENT_READ_FILE = "Toll-Events.csv";
    public static final String VEHICLES_READ_FILE = "vehicles.csv";
    public static final String EXIT_WRITE_FILE = "";
    
    //Database Configuration
    public static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";   
    public static final String DATABASE_URL = "jdbc:mysql://localhost:8889/";
    public static final String DATABASE_NAME = "tollSystemUpgrade";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "root";
}
