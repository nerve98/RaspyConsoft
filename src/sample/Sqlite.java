package sample;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Sqlite extends Thread{

    private final static String GET_USER_QUERY = "select name, surname from BADGES where id='%s'";
    private final static String ADD_MOVEMENT_QUERY = "insert into MOVEMENTS(badge, date_time) values('%s', '%s')";
    //private final static String GET_MOVEMENT_QUERY = "select id, date_outgo from MOVEMENTS where badge='%s' ORDER BY id DESC LIMIT 1";
    //private final static String UPDATE_MOVEMENT_QUERY = "update MOVEMENTS set date_outgo = '%s' where id = '%s'";
    private final static String GET_BADGE_QUERY="select badge, date_time from MOVEMENTS where date_time > '%s'";
    private static String currentQuery = "";
    private static ResultSet resultQuery;
    //private static boolean running=false;
    private static Connection conn;
    private static Thread thread;
    static {
        try{
            // db parameters
            File database = new File("sqlite/db.db");
            String url = "jdbc:sqlite:"+database.getAbsolutePath();
            System.out.println(url);
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*public static void connection() {

        conn = null;
        /*File database = new File("sqlite/db.db");
        System.out.println(database.getAbsolutePath());
        String url = "jdbc:sqlite:"+database.getAbsolutePath();
        System.out.println("Il file non esiste: " + url);
        if(database.exists())
            System.out.println("il file esiste");
        else
            System.out.println("Il file non esiste: " + url);*/
        /*try{
            // db parameters
            File database = new File("sqlite/db.db");
            String url = "jdbc:sqlite:"+database.getAbsolutePath();
            System.out.println(url);
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*//*finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
    //}*/


    @Override
    public void run() {
        try {
            //while(true) {
                Statement statement = conn.createStatement();
                boolean hasData = statement.execute(currentQuery);
                if (hasData) {
                    resultQuery=statement.getResultSet();
                }
                System.out.println("Il thread ha finito il suo compito");
                //running=false;


            //}
            //System.out.println(resultQuery.toString());

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public static synchronized ResultSet query(){
        //running=true;
        System.out.println("Il thread sta per partire");

        //if(thread==null) {
            thread = new Sqlite();
            thread.start();
        /*}
        else
            thread.resume();*/

        System.out.println("E' partito");
        //while(!thread.isInterrupted());
        while(thread.isAlive()) {
            //System.out.println("RUNNING");
        }
        System.out.println("fuori dal while... nessuna pietà");
        
        return resultQuery;
    }

    public static User getUserQuery(String badge){



        //connection();

        currentQuery=String.format(GET_USER_QUERY, badge);

        ResultSet result=query();
        String name="Utente", surname="sconosciuto";
        if(result!=null) {
            try {
                name = result.getString("name");
                surname = result.getString("surname");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        User user = new User(name,surname);
        System.out.println("Sqlite query user fatta!");
        return user;
    }

    public static ArrayList<Movement> getBadgeQuery(String date_hour){

        currentQuery=String.format(GET_BADGE_QUERY, date_hour);

        ResultSet result=query();
        ArrayList<Movement> movements = new ArrayList<>();

        if(result!= null){

            while(true) {
                try {
                    if (!result.next()) {
                        break;
                    }
                    else {
                        String badge, data;

                        try {
                            badge = result.getString("badge");
                            data = result.getString("date_time");
                            //data2= result.getString("date_outgo");
                            //System.out.println(badge+" "+data+" "+data2);
                            movements.add(new Movement(badge, data));

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

        System.out.println("Sqlite query badge fatta!");
        return movements;
    }

    public static void addMovementQuery(String badge){

        //connection();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);

        currentQuery=String.format(ADD_MOVEMENT_QUERY, badge, ts);
        query();

        /*currentQuery=String.format(GET_MOVEMENT_QUERY, badge);
        ResultSet result= query();
        String id, date_outgo;

        try {
            if(result.next()) {
                id = result.getString("id");
                date_outgo = result.getString("date_outgo");

                if (date_outgo == null) {
                    currentQuery = String.format(UPDATE_MOVEMENT_QUERY, ts, id);
                    query();

                } else {

                    currentQuery = String.format(ADD_MOVEMENT_QUERY, badge, ts);
                    query();
                }
            }
            else{
                currentQuery = String.format(ADD_MOVEMENT_QUERY, badge, ts);
                query();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        System.out.println("Sqlite insert fatto");
    }
}
