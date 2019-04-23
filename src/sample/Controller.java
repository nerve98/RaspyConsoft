package sample;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;


import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    @FXML
    private Label time;
    @FXML
    private GridPane grid;

    @FXML
    public Label testo;

    private Date data;
    private int minute;
    private int hour;
    private int second;
    private String text;
    private Date dataName;
    public final static String DEFAULT_MESSAGE="Passa il Badge";
    private static String changedText=DEFAULT_MESSAGE;
    public final static long ANIMATION_TEXT=4000;
    public final static long ANIMATION_DEFAULT_TEXT=50;
    private static boolean youCanPassBadge=true;
    /*public static void setTesto() {
        if (this.testo instanceof Label){
            String object = (String) obj;
        }
    }  */
    @FXML
    public void initialize() {

        observe(Main.testoInput);
        //testo.setText("Ah");
        //dataName= new Date();
        testo.setText(changedText);


        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data = new Date();
                second = data.getSeconds();
                minute = data.getMinutes();
                hour = data.getHours();



                if(changedText!=testo.getText()){
                    //testo.setText(changedText);
                    //dataName=data;
                        youCanPassBadge=false;
                        testo.setText(changedText);
                        FadeTransition ft = new FadeTransition(Duration.millis(ANIMATION_TEXT), testo);

                        ft.setFromValue(1);
                        ft.setToValue(0);
                        ft.setCycleCount(1);
                        ft.setAutoReverse(false);
                        ft.play();
                        ft.setOnFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                changedText = DEFAULT_MESSAGE;
                                testo.setText(DEFAULT_MESSAGE);
                                FadeTransition ftd = new FadeTransition(Duration.millis(ANIMATION_DEFAULT_TEXT), testo);

                                ftd.setToValue(1);
                                ftd.setCycleCount(1);
                                ftd.setAutoReverse(false);
                                ftd.play();
                                ftd.setOnFinished(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        youCanPassBadge=true;
                                    }
                                });
                            }
                        });

                }
                /*if((data.getTime()-dataName.getTime())>5){
                    setChangedText(DEFAULT_MESSAGE);
                }*/

                time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
                /*FadeTransition ft = new FadeTransition(Duration.millis(500), testo);

                ft.setFromValue(1);
                ft.setToValue(0.7);
                ft.setCycleCount(Timeline.INDEFINITE);
                ft.setAutoReverse(true);
                ft.play();*/

            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }




    /*public void setTex(String text) {
        this.text = text;
    }

    public void setTesto(String testo) {
        this.testo.setText(testo);
    }*/

    public void observe(Observable o) {
        o.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChangedText(Main.testoInput.getTestoInput());

    }

    public synchronized void setChangedText(String s){
        changedText=s;
    }

    public static boolean isYouCanPassBadge(){
        return youCanPassBadge;
    }
}

