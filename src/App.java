import imports.example.inputs;
import imports.example.module;
import imports.example.times;;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        String input = inputs.input("Enter your name: ");
        System.out.println("Hello, "+input+"!");
        System.out.println(module.times_str(3, "="));
        double time = times.GetTime(1);
        for(int i=0; i<=10000; i++){
            // System.out.print("loading:");
            // System.out.print(i);
            // System.out.print("/10000\r");
            module.sleep(0.001);
        }
        System.out.println("\nTime: ");
        System.out.println(times.GetTime(1)-time);
        System.out.println("");
    }
}
