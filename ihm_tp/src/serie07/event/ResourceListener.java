package serie07.event;

import java.util.EventListener;

public interface ResourceListener extends EventListener {

    /**
     * Invoquée lorsque un enregistrement (une ligne de la table) vient d'être
     *  chargé, l'événement véhiculant cet enregistrement au format String.
     */
    void resourceLoaded(ResourceEvent<String> e);

    /**
     * Invoquée lorsque un enregistrement (une ligne de la table) vient d'être
     *  sauvegardé, l'événement véhiculant cet enregistrement au format String.
     */
    void resourceSaved(ResourceEvent<String> e);

    /**
     * Invoquée lorsque un enregistrement (une ligne de la table) vient d'être
     *  chargé ou sauvegardé, l'événement véhiculant le pourcentage de
     *  chargement ou de sauvegarde effectué.
     */
    void progressUpdated(ResourceEvent<Integer> e);

    /**
     * Invoquée lorsqu'une erreur survient pendant le chargement ou la
     *  sauvegarde d'un enregistrement (une ligne de la table), l'événement
     *  véhiculant l'exception survenue.
     */
    void errorOccured(ResourceEvent<Exception> e);
}
