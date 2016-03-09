package bananacore.epic;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static double WEIGHT = 2000;
    public static OurParser PARSER;

    public static Boolean auto;
    public static int numberOfGears;
    public static int weight;
    public static Boolean gassoline;
    public static int fuelsize;

    static {
        PARSER = new OurParser();
    }

}
