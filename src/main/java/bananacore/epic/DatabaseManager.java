package bananacore.epic;

import bananacore.epic.models.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class DatabaseManager {
    private static Logger logger = LoggerFactory.getLogger("DatabaseManager");
    public static SessionFactory sessionFactory;
    private static ListProperty<BrakeSession> brakeSessions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private static ListProperty<WrongGearSession> wrongGearSessions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private static ListProperty<SpeedSession> speedSessions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private static ListProperty<FuelSession> fuelSessions = new SimpleListProperty<>(FXCollections.observableArrayList());


    public static void connectToDB(){
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistryBuilder ssrb;
        try{
            ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(ssrb.build());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    public static void insertBrakeSession(BrakeSession brakeSession){
        Thread sessionThread = new Thread(()->{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(brakeSession);
            session.getTransaction().commit();
            session.close();
        });
        sessionThread.start();
    }

    public static void insertWrongGearSession(WrongGearSession wrongGearSession){
        Thread sessionThread = new Thread(()->{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(wrongGearSession);
            session.getTransaction().commit();
            session.close();
        });
        sessionThread.start();
    }

    public static void insertSpeedSession(SpeedSession speedSession){
        Thread sessionThread = new Thread(()->{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(speedSession);
            session.getTransaction().commit();
            session.close();
        });
        sessionThread.start();
    }

    public static void insertFuelSession(FuelSession fuelSession){
        Thread sessionThread = new Thread(()->{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(fuelSession);
            session.getTransaction().commit();
            session.close();
        });
        sessionThread.start();
    }

    public static void update(){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        brakeSessions.setAll(FXCollections.observableArrayList(session.createQuery("from BrakeSession order by starttime asc").list()));
        fuelSessions.setAll(FXCollections.observableArrayList(session.createQuery("from FuelSession order by starttime asc").list()));
        wrongGearSessions.setAll(FXCollections.observableArrayList(session.createQuery("from WrongGearSession order by starttime asc").list()));
        speedSessions.setAll(FXCollections.observableArrayList(session.createQuery("from SpeedSession order by starttime asc").list()));
        tx.commit();
        session.close();
    }

    public static void update(Timestamp fromDate, Timestamp toDate){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        brakeSessions.setAll(FXCollections.observableArrayList(session.createQuery("from BrakeSession where starttime between '" + fromDate + "' AND '" + toDate + "' order by starttime asc").list()));
        fuelSessions.setAll(FXCollections.observableArrayList(session.createQuery("from FuelSession where starttime between  '" + fromDate + "' AND '" + toDate + "' order by starttime asc").list()));
        wrongGearSessions.setAll(FXCollections.observableArrayList(session.createQuery("from WrongGearSession where starttime between  '" + fromDate + "' AND '" + toDate + "' order by starttime asc").list()));
        speedSessions.setAll(FXCollections.observableArrayList(session.createQuery("from SpeedSession where starttime between  '" + fromDate + "' AND '" + toDate + "' order by starttime asc").list()));
        tx.commit();
        session.close();
    }

    public static boolean brakeDataExistsBefore(Timestamp date){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from BrakeSession where starttime < '" + date + "'");
        query.setMaxResults(1);
        boolean exists = query.list().size() > 0;
        tx.commit();
        session.close();
        return exists;
    }

    public static boolean brakeDataExistsAfter(Timestamp date){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from BrakeSession where starttime > '" + date + "'");
        query.setMaxResults(1);
        boolean exists = query.list().size() > 0;
        tx.commit();
        session.close();
        return exists;
    }

    public static Timestamp getLatestData(){
        Timestamp newest = new Timestamp(0);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Timestamp brake = (Timestamp) session.createQuery(" select max(startTime) from BrakeSession").list().get(0);
        if(brake != null){
            newest = brake;
        }
        Timestamp fuel = (Timestamp) session.createQuery("select max(startTime) from FuelSession").list().get(0);
        if(fuel != null && (fuel.compareTo(newest) > 0)){
            newest = fuel;
        }
        Timestamp wrong = (Timestamp) session.createQuery("select max(startTime) from WrongGearSession").list().get(0);
        if(wrong != null && (wrong.compareTo(newest) > 0)){
            newest = wrong;
        }
        Timestamp speed = (Timestamp) session.createQuery("select max(startTime) from SpeedSession").list().get(0);
        if(speed != null && (speed.compareTo(newest) > 0)){
            newest = speed;
        }
        tx.commit();
        session.close();
        return newest;


    }

    public static ObservableList<BrakeSession> getBrakeSessions() {
        return brakeSessions.get();
    }

    public static ListProperty<BrakeSession> brakeSessionsProperty() {
        return brakeSessions;
    }

    public static ObservableList<WrongGearSession> getWrongGearSessions() {
        return wrongGearSessions.get();
    }

    public static ListProperty<WrongGearSession> wrongGearSessionsProperty() {
        return wrongGearSessions;
    }

    public static ObservableList<SpeedSession> getSpeedSessions() {
        return speedSessions.get();
    }

    public static ListProperty<SpeedSession> speedSessionsProperty() {
        return speedSessions;
    }

    public static ObservableList<FuelSession> getFuelSessions() {
        return fuelSessions.get();
    }

    public static ListProperty<FuelSession> fuelSessionsProperty() {
        return fuelSessions;
    }


    //setup
    public static void  updateSettings(SettingsEPIC settingsObject) {
        Thread sessionThread = new Thread(()->{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            try{
                getSettings();
                session.update(settingsObject);
            } catch (IndexOutOfBoundsException e){
                session.save(settingsObject);
            }
            session.getTransaction().commit();
            session.close();
        });
        sessionThread.start();
    }
    public static SettingsEPIC  getSettings() {
//        Thread sessionThread;
//        sessionThread = new Thread(()->{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
//            SettingsEPIC settingsObject =(SettingsEPIC) session.createQuery("from Settings").getFirstResult();
            ObservableList settingsList = FXCollections.observableArrayList(session.createQuery("from Settings").list());
            SettingsEPIC settingsObject=(SettingsEPIC) settingsList.get(0);

            session.getTransaction().commit();
            session.close();
//        });
//        sessionThread.start();

        return settingsObject;//
    }

}
