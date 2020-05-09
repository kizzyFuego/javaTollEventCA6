/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kizzy
 */
public class MySqlTollEventDAOIT
{
    
    public MySqlTollEventDAOIT()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }


    /**
     * TESTING OF THE BILLING FUNCTIONALITY
     * Test of getBillByPersonName method, of class MySqlTollEventDAO.
     */
    @Test
    public void testGetBillByPersonName()
    {
        System.out.println("getBillByPersonName");
        String name = "Frank";
        MySqlTollEventDAO instance = new MySqlTollEventDAO();
        double expResult = 12.0;
        double result = instance.getBillByPersonName(name);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    

   
    
}
