/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;

/**
 *
 * @author kizzy
 */
public class Test
{
    public static void main(String[] args)
    {
        TollEvent tollEvent = new TollEvent("boothID", "vehReg", 2333);
        
        System.out.println(tollEvent.getTime().toInstant().toString());
        //2020-05-08 18:45:21.383
        //2020-05-08T16:48:31.416Z
        //Timestamp time = Timestamp.from(Instant.parse( in.next() ));
    }
}
