import imports.example.inputs;
import imports.example.module;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        String input = inputs.input("Enter your name: ");
        System.out.println("Hello, "+input+"!");
        System.out.println(module.times_str(3, "="));
        for(int i=0; i<10000; i++){
            System.out.print("loading: |"+module.times_str(i/10000*10, "=")+module.times_str((10-i/10000*10)," ")+"|\r");
            module.sleep(0.001);
        }
        System.out.println("");
    }
}
