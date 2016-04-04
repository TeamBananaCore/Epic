package bananacore.epic;

import bananacore.epic.models.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
        brakeSessions.setAll(FXCollections.observableArrayList(session.createQuery("from BrakeSession").list()));
        fuelSessions.setAll(FXCollections.observableArrayList(session.createQuery("from FuelSession").list()));
        wrongGearSessions.setAll(FXCollections.observableArrayList(session.createQuery("from WrongGearSession").list()));
        speedSessions.setAll(FXCollections.observableArrayList(session.createQuery("from SpeedSession").list()));
        tx.commit();
        session.close();
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
            session.saveOrUpdate(settingsObject);
            session.getTransaction().commit();
            session.close();
        });
        sessionThread.start();
    }
    public static SettingsEPIC  getSettings() {
        final SettingsEPIC[] settingsObject2 = {null};

        Thread sessionThread = new Thread(()->{
            System.out.println("test");
            Session session = sessionFactory.openSession();
            session.beginTransaction();
//            SettingsEPIC settingsEPIC=(SettingsEPIC) session.createQuery("from Settings").getFirstResult();
            ObservableList settingsList = FXCollections.observableArrayList(session.createQuery("from SettingsEPIC").list());
            SettingsEPIC settingsObject=(SettingsEPIC) settingsList.get(0);
            settingsObject2[0] =settingsObject;

            session.getTransaction().commit();
            session.close();
        });
        sessionThread.start();
        return settingsObject2[0];
    }

}
