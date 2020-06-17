package it.myrent.ee.api.utils;



import java.util.List;

/**
 *
 * @author jamess
 */
public class DataManipulation {


    /**
     * Aggiunge un oggetto nella lista di oggetti. Gli oggetti esistenti non vengono inseriti.
     * L'esistenza e' verificata usando equals, l'inserimento ordinato e' basato su compareTo.
     * @return l'indice dell'oggetto inserito o -1 se l'oggetto esiste gia'.
     */
    public static int inserimentoOrdinato(List comparableObjects, Comparable candidate) {
        if(!comparableObjects.contains(candidate)) {
            for(int i = 0; i < comparableObjects.size(); i++) {
                if(candidate.compareTo(comparableObjects.get(i)) < 0) {
                    comparableObjects.add(i, candidate);
                    return i;
                }
            }

            // Il candidato e' aggiunto qui in 2 casi:
            //     1. la lista non ha elementi
            //     2. il candidato e' maggiore di tutti gli oggetti nella lista
            comparableObjects.add(candidate);
            return comparableObjects.size() - 1;
        }
        return -1;
    }

}
