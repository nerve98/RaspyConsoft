package sample;

import java.util.*;

public class Lista{
    private static Map<String,Utente> mappa;
    private static ArrayList<String> lista;
    static UID lastUID;

    static{
        lastUID= new UID();
        mappa=new HashMap<>();
        mappa.put("9d61107c", new Utente( "Gianluca", "Canova"));
        mappa.put("04b22632535380", new Utente( "Francesco", "Baldan"));
        mappa.put("904263b32c95581", new Utente( "Valerio", "Ly"));
        mappa.put("bd60107c", new Utente( "Giacomo", "Caviola"));
        mappa.put("044c3a32c95581", new Utente( "Luca", "Bonin"));
        mappa.put("04db2b32535380", new Utente( "Matteo", "Marchi"));
        mappa.put("04ea3632535380", new Utente( "Luca", "Ambrosi"));
        mappa.put("04bc0d32535380", new Utente( "Alessio", "Lasta"));
        mappa.put("04d36232535380", new Utente( "Daniele", "Faccin"));
        lista=new ArrayList<>();
    }

    public static void add(String uid){
        lista.add(uid);
    }

    public static String remove(){
        return lista.remove(0);
    }

    public static String getUser(String uid){
        return mappa.get(uid).toString();
    }
    /*public static void addLast(String last){
        lastUID=last;
    }*/

    /*public static String getUtente(String uid){
        return lista.get(lista.size()).toString();
    }*/

    public static long size(){
        return lista.size();
    }

    static class UID extends Observable {
        private String someVariable = null;

        public void setSomeVariable(String someVariable) {
            synchronized (this) {
                this.someVariable = someVariable;
            }
            setChanged();
            notifyObservers();
        }

        public synchronized String getSomeVariable() {
            return someVariable;
        }
    }
}
