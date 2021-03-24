package serie05.event;

import javax.swing.event.EventListenerList;

public class ResourceLoaderSupport<R> {

    // CONSTANTES

    @SuppressWarnings("rawtypes")
    private static final ResourceLoaderListener[] EMPTY_TAB =
            new ResourceLoaderListener[0];

    // ATTRIBUTS

    private EventListenerList listenersList;
    private final Object owner;

    // CONSTRUCTEURS

    public ResourceLoaderSupport(Object owner) {
        this.owner = owner;
    }

    // REQUETES

    @SuppressWarnings("unchecked")
    public synchronized ResourceLoaderListener<R>[]
            getResourceLoaderListeners() {
        if (listenersList == null) {
            return (ResourceLoaderListener<R>[]) EMPTY_TAB;
        }
        return listenersList.getListeners(ResourceLoaderListener.class);
    }

    public synchronized void addResourceLoaderListener(
            ResourceLoaderListener<R> listener) {
        if (listener == null) {
            return;
        }
        if (listenersList == null) {
            listenersList = new EventListenerList();
        }
        listenersList.add(ResourceLoaderListener.class, listener);
    }

    public synchronized void removeResourceLoaderListener(
            ResourceLoaderListener<R> listener) {
        if (listener == null || listenersList == null) {
            return;
        }
        listenersList.remove(ResourceLoaderListener.class, listener);
    }

    // COMMANDES

    @SuppressWarnings("unchecked")
    public void fireResourceLoaded(R d) {
        Object[] list = listenersList.getListenerList();
        ResourceLoaderEvent<R> e = new ResourceLoaderEvent<R>(owner, d);
        for (int i = list.length - 2; i >= 0; i--) {
            ((ResourceLoaderListener<R>) list[i + 1]).resourceLoaded(e);
        }
    }
}
