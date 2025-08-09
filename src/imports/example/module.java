package imports.example;

public class module {
    public static void sleep(double second){
        try {
            Thread.sleep((long)(second*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String times_str(int n, String s){
        String result = "";
        for(int i=0; i<n; i++){
            result += s;
        }
        return result;
    }
}
