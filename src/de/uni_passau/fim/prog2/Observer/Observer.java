package de.uni_passau.fim.prog2.observer;

/**
 * Entspricht dem Interface {@code Observer} des Design-Patterns Observer.
 *
 * @version 25.01.20
 * @author -----
 */
public interface Observer {

    /**
     * Entspricht der Benachrichtigungsmethode des {@code Observer}.
     *
     * @param o             Entspricht dem zu beobachtenden Objekt, das den
     *                      {@code Observer} benachrichtigt hat.
     */
    void update(Observable o);
}
