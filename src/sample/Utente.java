package sample;

public class Utente {
    String nome;
    String cognome;

    public Utente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return nome +" "+ cognome;
    }
}
