package bananacore.epic;

import bananacore.epic.models.SettingsEPIC;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static double WEIGHT = 2000;
    public static OurParser PARSER=new OurParser();
    public static SettingsEPIC settingsEPIC= DatabaseManager.getSettings();


//    static {
//        PARSER = new OurParser();
////        settingsEPIC = DatabaseManager.getSettings();
//    }

}
