package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

public class Main extends Application implements Observer {

    //Controller controller;

    public static LabelTesto testoInput;
    private static Thread threadNFC;
    private static Thread threadSocket;

    public Main(){
        testoInput=new LabelTesto();
        //observe(Lista.lastUID);
        //controller= new Controller();
    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        observe(Lista.lastUID);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Image image = new Image(getClass().getResource("921.png").toExternalForm());

        primaryStage.getIcons().add(image);
        primaryStage.setTitle("Badge");
        primaryStage.setScene(new Scene(root, 600, 360));
        primaryStage.setFullScreen(true);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if(threadNFC!=null){
                    threadNFC.stop();

                }
                if(threadSocket!=null){
                    while(!threadSocket.getState().equals(Thread.State.TIMED_WAITING));

                    threadSocket.stop();

                }
                Platform.exit();
                System.exit(0);
            }
        });

    }


    public static void main(String[] args) {

        threadNFC=new Thread(new NFC());
        threadNFC.start();
        System.out.println("Thread NFC fatto!");
        threadSocket=new Thread(new Socket());
        threadSocket.start();
        System.out.println("Thread Socket fatto!");
        System.out.println("lauch(args) inizio!");
        launch(args);
        System.out.println("lauch(args) fatto!");
        //System.out.println(Sqlite.querySocket("044c3a32c95581"));

    }


    public void observe(Observable o) {
        o.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        String someVariable = ((Lista.UID) o).getSomeVariable();
        Sqlite.addMovementQuery(someVariable);
        User rs = Sqlite.getUserQuery(someVariable);
        //if(rs!=null)
        //Lista.add(someVariable);
        String name = rs.getName();
        String surname = rs.getSurname();

        System.out.println("Somevariable: "+someVariable);
        //System.out.println("Testo: " + Lista.getUser(someVariable));
        testoInput.setTestoInput(name+" "+surname);




        //Controller.setTex(Lista.getUser(someVariable));

    }

    class LabelTesto extends Observable {
        private String testoInput = Controller.DEFAULT_MESSAGE;

        public void setTestoInput(String someVariable) {
            synchronized (this) {
                this.testoInput = someVariable;
            }
            setChanged();
            notifyObservers();
        }

        public synchronized String getTestoInput() {
            return testoInput;
        }
    }
}
