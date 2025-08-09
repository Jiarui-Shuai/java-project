package imports.example;

import java.util.Scanner;

public class inputs {
       public static String input(String message){
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        String input = scanner.nextLine();
        scanner.close();
        return input;
    } 

    
}
