import imports.example.*;

public class App {
    public static void main(String[] args) {
        try {
            for(String arg : args){
                System.out.println(arg);
            }
            System.out.println(module.times_str(50, "="));

            System.out.println("Hello, World!");
            String input = inputs.input("Enter your name: ");
            System.out.println("Hello, "+input+"!");
            System.out.println(module.times_str(50, "="));
            
            for(int i=1; i<=10; i++){
                System.out.print("loading:");
                System.out.print(i);
                System.out.print("/10");
                System.out.print(" ");
                String PackageName = random.randLetters((int)random.randLong(1, 5))+".";
                PackageName += random.randLetters((int)random.randLong(1, 5))+".";
                PackageName += random.randLetters((int)random.randLong(1, 5));
                System.out.print(PackageName);
                long Size = random.randLong(1_000_000, 5_000_000);
                System.out.print(" ");
                System.out.printf("%.2f", ((double)Size)/1_000_000.0);
                System.out.print(" MiB");
                module.sleep(Size/1_000_000);
                
                System.out.print("\r\033[2K");
            }
            System.out.println("Loading complete.");
            System.out.println(module.times_str(50, "="));

            System.out.println("Time: ");
            System.out.println((long)times.GetTime(1));
            var rand = random.randDouble(0, 1);
            System.out.println("Random double: "+rand);
            var rand2 = random.randLong(0, 100);
            System.out.println("Random long: "+rand2);
            System.out.println(module.times_str(50, "="));
            System.out.println(inputs.input("Enter some: "));
            System.out.println(maths.evaluate("2+3*4/5"));

            // String snbT = inputs.input("Enter a  SNBT:");
            // SNBT snbt = SNBT.ParseSNBT(snbT);
            // System.out.println(snbt.GetValue(inputs.input("Enter a path: ")));
            // snbt.SetValue(inputs.input("Enter a path: "), inputs.input("Enter a value: "));
            // System.out.println(snbt.GetValue(inputs.input("Enter a path: ")));           

        } finally{
            inputs.closeScanner();
        }
    }
}
