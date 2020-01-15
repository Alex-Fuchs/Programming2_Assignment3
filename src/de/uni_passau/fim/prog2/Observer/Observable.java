package de.uni_passau.fim.prog2.Observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Entspricht der Klasse {@code Observable} des Design-Patterns Observer.
 *
 * @version 15.01.20
 * @author -----
 */
public class Observable {

    private Set<Observer> observers;

    /**
     * Entspricht {@code true}, falls sich der Zustand verändert hat und die
     * Observer benachrichtigt werden.
     */
    private boolean changed;

    /**
     * Kreiert ein {@code Observable} ohne {@code Observer}.
     */
    protected Observable() {
        observers = new HashSet<Observer>();
    }

    /**
     * Fügt einen {@code Observer} hinzu, wobei dieser jedoch nicht hinzugefügt
     * wird, falls dieser bereits registriert wurde.
     *
     * @param observer      Entspricht den zu hinzufügendem {@code Observer}.
     * @see                 Observer
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Löscht einen registrierten {@code Observer}.
     *
     * @param observer      Entspricht den zu löschenden Observer.
     * @see                 Observer
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Gibt zurück, ob sich der Zustand des {@code Observable} verändert hat.
     *
     * @return      Entspricht {@code true}, falls sich der Zustand verändert
     *              hat, und {@code false}, falls alle {@code Observer} bereits
     *              benachrichtigt wurden oder keine Zustandsveränderung
     *              stattfand.
     */
    public boolean hasChanged() {
        return changed;
    }

    /**
     * Benachrichtigt alle registrierten {@code Observer}, dass sich der
     * Zustand verändert hat.
     *
     * @see     Observer#update(Observable)
     */
    protected void notifyObserver() {
        for (Observer o: observers) {
            o.update(this);
        }
    }

    /**
     * Setzt den Zustand auf verändert, führt {@code notifyObserver()} aus
     * und setzt den Zustand wieder auf unverändert.
     *
     * @see     #notifyObserver()
     * @see     #clearChanged()
     */
    protected void setChanged() {
        changed = true;
        notifyObserver();
        clearChanged();
    }

    /**
     * Setzt den Zustand auf unverändert.
     */
    protected void clearChanged() {
        changed = false;
    }
}
