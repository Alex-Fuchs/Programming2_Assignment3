package de.uni_passau.fim.prog2.observer;

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
        observers = new HashSet<>();
    }

    /**
     * Fügt einen {@code Observer} hinzu, wobei dieser jedoch nicht hinzugefügt
     * wird, falls dieser bereits registriert wurde.
     *
     * @param observer      Entspricht den zu hinzufügendem {@code Observer},
     *                      der nicht {@code null} sein darf.
     * @see                 Observer
     */
    public void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer);
        } else {
            throw new IllegalArgumentException("Observer cannot be null!");
        }
    }

    /**
     * Löscht einen registrierten {@code Observer}.
     *
     * @param observer      Entspricht den zu löschenden {@code Observer}, der
     *                      nicht {@code null} sein darf.
     * @see                 Observer
     */
    public void removeObserver(Observer observer) {
        if (observer != null) {
            observers.remove(observer);
        } else {
            throw new IllegalArgumentException("Observer cannot be null");
        }
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
     * Zustand verändert hat und setzt den Zustand auf unverändert, wobei den
     * {@code Observer} ein zusätliches {@code Object} mit übergeben wird.
     *
     * @param arg           Entspricht zusätzlichen Informationen, die den
     *                      {@code Observer} gegeben werden kann.
     * @see                 #hasChanged()
     * @see                 #clearChanged()
     * @see                 Observer#update(Observable, Object)
     */
    protected void notifyObserver(Object arg) {
        if (hasChanged()) {
            for (Observer o : observers) {
                o.update(this, arg);
            }
            clearChanged();
        }
    }

    /**
     * Setzt den Zustand auf verändert.
     */
    protected void setChanged() {
        changed = true;
    }

    /**
     * Setzt den Zustand auf unverändert.
     */
    protected void clearChanged() {
        changed = false;
    }
}
