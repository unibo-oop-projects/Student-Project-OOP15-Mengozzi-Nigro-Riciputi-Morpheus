package morpheus.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author jacopo
 *
 */
public class Ranking extends Storable {
    /**
     * 
     */
    private static final long serialVersionUID = 3005073739818117637L;
    /**
     * 
     */
    private static final String FILE = "res/Ranking.dat";
    /**
     * 
     */
    private static final int MAX_SIZE = 10;
    private final List<Element> values;
    private final List<String> app;
    private boolean toSort;

    /**
     * Legge la classifica e la carica in memoria.
     * 
     * @throws IOException
     *             In caso il file non esistesse
     */
    public Ranking() {
        super(FILE);
        boolean avvert = false;
        Ranking rank = null;
        try {
            rank = (Ranking) this.readObject();
        } catch (IOException e) {
            new File(FILE);
            avvert = true;
        }
        if (avvert) { 
            this.values = new ArrayList<>();
            this.app = new ArrayList<>();
        } else {
            this.values = rank.getRanking();
            this.app = rank.getPlayers();
        }
    }


    /**
     * Aggiunge un elemento alla classifica.
     *
     *!!!! Appunto da togliere !!!
     * per mengo se non mi ricordo di dirtelo, questo metodo lancia eccezione perchè nel caso un elemento con quel nome
     * si già presente avevo pensato che era carino che dalla view si lanciasse una finestra (tipo JDialogo) che chieda se si
     * è sicuro di aggiornare il risultato, nel caso si risponda di aggiornare il risultato a quel punto devi chiamare il metodo forceAdd
     * 
     * 
     * @param el
     *            Elemento da aggiungere alla classifica: chiave-> il nome;
     *            valore-> il punteggio.
     * @throws IllegalArgumentException
     *             se contiene già un elemento con quel nome.
     *             !!! Appunto da togliere !!!
     *             Leggere descrizione sopra per il lancio dell'eccezione
     */
    public void add(final Element el) throws IllegalArgumentException {
        if (app.contains(el.getName())) {
            throw new IllegalArgumentException();
        }
        app.add(el.getName());
        values.add(el);
        toSort = true;
        if (values.size() > MAX_SIZE) {
           remove(); 
        }
    }

    /**
     * Forza l'aggiornamento dello score di un Risultato già inserito.
     * 
     * @param el
     *            Elemento da aggiornare: chiave-> il nome; valore-> il
     *            punteggio
     */
    public void forceAdd(final Pair<String, Integer> el) {
        for (final Element e : values) {
            if (e.getName().equals(el.getKey())) {
                e.setScore(el.getValue());
                toSort = true;
            }
        }
    }
    
    /**
     * Stampa la classifica su terminale.
     */
    public void getRankingOnTerm() {
        if (toSort) {
            Collections.sort(values, new Element()::compare);
            toSort = false;
        }

        for (final Element e : values) {
            System.out.println(e.getName() + "\t" + e.getScore());
        }
    }

    /**
     * Salva i cambiamenti alla classifica su file.
     * 
     * @throws IOException
     *             In caso il file non esistesse
     */
    public void close() throws IOException {
        if (toSort) {
            Collections.sort(values, new Element()::compare);
            toSort = false;
        }
        this.writeObject(this);
    }

    /**
     * Ritorna una Lista con i primi x elementi desiderati, se x è più grande
     * del numero degli elementi disponibili verranno riportati tutti gli
     * elementi.
     * 
     * @param x
     *            il numero degli elementi da ritornare
     * @return una Lista, già ordinato con i primi x elementi della classifica
     * 
     */
    public List<Element> getPartOfRanking(final int x) {
        if (toSort) {
            Collections.sort(values, new Element()::compare);
            toSort = false;
        }
        return new ArrayList<>(values.subList(0, x - 1));
    }

    /**
     * Ritorna la classifica per intero.
     * 
     * @return una Lista con l'intera classifica
     */
    public List<Element> getRanking() {
        if (toSort) {
            Collections.sort(values, new Element()::compare);
            toSort = false;
        }
        return new ArrayList<>(values);
    }

    /**
     * La lista dei nomi del giocatori presenti nella classifica.
     * 
     * @return La lista dei nomi del giocatori presenti nella classifica
     */
    public List<String> getPlayers() {
        return new ArrayList<>(app);
    }

    /**
     * Get the Element at the x position.
     * @param x
     *          rank of the element
     * @return
     *          the desidered element
     */
    public Element getPosition(final int x) {
        if (toSort) {
            Collections.sort(values, new Element()::compare);
            toSort = false;
        }
        return values.get(x - 1);
    }

    /**
     * Fornisce il punteggio massimo e il giocatore che l'ha fatto.
     * 
     * @return un Pair composto da: -Nome giocatore; -Punteggio.
     */
    public Pair<String, Integer> getRecord() {
        return values.get(0).getAsPair();
    }

    /**
     * @return the toSort
     */
    public boolean isToSort() {
        return toSort;
    }
    
    private void remove() {
        Collections.sort(values, new Element() :: compare);
        toSort = false;
        values.remove(MAX_SIZE);
    }

}