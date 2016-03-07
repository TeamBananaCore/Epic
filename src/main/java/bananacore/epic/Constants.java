package bananacore.epic;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static double WEIGHT = 2000;
    public static OurParser PARSER;
    public static double OPTIMAL_SPEED_REDUCTION = 10.0 / (50.0 * Constants.WEIGHT);

    static {
        PARSER = new OurParser();
    }

    public static double calculateBrakePerformance(int startSpeed, int endSpeed, long duration){
        return 100*(duration / ((double)(startSpeed - endSpeed) * WEIGHT))/OPTIMAL_SPEED_REDUCTION;
    }

}
