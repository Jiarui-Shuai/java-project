package imports.example;

public class random {
    public static double randDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static long randLong(long min, long max) {
        return (long) (Math.random() * (max - min + 1) + min);
    }

    public static float randFloat(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    public static char randChar() {
        String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"; 
        return chars.charAt((int) (Math.random() * chars.length()));
    }

    public static String randString(int length) {
        String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";  // Deflat ASCII characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    public static String randLetters(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }
}
