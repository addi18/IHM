package serie04.model;

import java.beans.PropertyChangeListener;

/**
 * @inv
 *     consumersNb() > 0
 *     producersNb() > 0
 *     box() != null
 *     forall i : consumer(i) != null
 *     forall i : producer(i) != null
 *     isRunning()
 *         <==> exists i,j : consumer(i).isAlive() || producer(j).isAlive()
 *     !isRunning() ==> !isFrozen()
 *
 * @cons
 *     $ARGS$ int prod, int cons, int iter
 *     $PRE$
 *         prod > 0
 *         cons > 0
 *         iter > 0
 *     $POST$
 *         producersNb() == prod
 *         consumersNb() == cons
 *         iter est le nombre de fois que chaque acteur utilisera la boite
 *         !isRunning()
 */
public interface ProdConsModel {

    // CONSTANTES

    String SENTENCE_NAME = "sentence";
    String RUNNING_NAME = "running";
    String FROZEN_NAME = "frozen";

    // REQUETES


    /**
     * La boite partagée entre tous les acteurs.
     */
    Box box();

    /**
     * Le i-ème consommateur.
     * @pre
     *     0 <= i < consumersNb()
     */
    Actor consumer(int i);

    /**
     * Le nombre de consommateurs.
     */
    int consumersNb();

    /**
     * Indique si tous les producteurs sont morts et si tous les consommateurs
     *  non morts (au moins un) sont bloqués, ou le contraire.
     */
    boolean isFrozen();

    /**
     * Indique si au moins un acteur est vivant.
     */
    boolean isRunning();

    /**
     * Le i-ème producteur.
     * @pre
     *     0 <= i < producersNb()
     */
    Actor producer(int i);

    /**
     * Le nombre de producteurs.
     */
    int producersNb();

    // COMMANDES

    /**
     * Ajoute un écouteur pour les changements de valeur de la propriété pName.
     * Si lst est null, ne fait rien.
     */
    void addPropertyChangeListener(String pName, PropertyChangeListener lst);

    /**
     * Termine tous les acteurs encore vivants.
     * @post
     *     !isRunning()
     */
    void stop();

    /**
     * Retire un écouteur pour les changements de valeur de la propriété pName.
     * Si lst est null, ne fait rien.
     */
    void removePropertyChangeListener(String pName, PropertyChangeListener lst);

    /**
     * Démarre (ou redémarre) tous les acteurs.
     * @pre
     *     !isRunning()
     * @post
     *     forall i : producer(i).isAlive()
     *     forall i : consumer(i).isAlive()
     */
    void start();
}
