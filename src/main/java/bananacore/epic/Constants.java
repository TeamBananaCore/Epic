package bananacore.epic;

import bananacore.epic.models.SettingsEPIC;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static double WEIGHT = 2000;
    public static OurParser PARSER;//=new OurParser();
    public static SettingsEPIC settingsEPIC;//= DatabaseManager.getSettings();


    static {
        PARSER = new OurParser();
        try{
            settingsEPIC = DatabaseManager.getSettings();
        } catch (IndexOutOfBoundsException e){
            settingsEPIC = new SettingsEPIC(false,6,900,true,50,1,1,30);
        }
    }

}
