package imports.example;

public class times {
    public static double GetTime(double scalc) {
        // 返回时间戳（秒）
        return (System.currentTimeMillis() / 1000.0 * scalc);
    }
}
