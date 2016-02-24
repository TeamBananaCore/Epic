package bananacore.epic;


import java.util.ArrayList;

public class CarController {

    public void addObserver (String observername, OurParser ourParser){
        ourParser.addObserver(observername, this);

    }
    public void reciveData(){

    }


}
