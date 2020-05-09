/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tolleventsystem;
import java.util.Scanner;

/**
 *
 * @author Kingsley Osemwenkhae D00215130
 */
public class Keyboard
{
    public static String getStringInput(String displayInstruction)
    {
        Scanner keyboard = new Scanner(System.in);
        String input = null;
        System.out.print(displayInstruction);

        if (keyboard.hasNextLine())
        {
            input = keyboard.nextLine();
        }
        return input;
    }
    
}

