package imports.example;

import java.util.Scanner;

public class inputs {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static String input(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
    
    // 可选：添加关闭方法供程序结束时调用
    public static void closeScanner() {
        scanner.close();
    }
}