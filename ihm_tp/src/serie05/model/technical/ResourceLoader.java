package serie05.model.technical;

import serie05.event.ResourceLoaderListener;
import serie05.event.ResourceLoaderSupport;

public abstract class ResourceLoader<R> {

    // ATTRIBUTS

    private final ResourceLoaderSupport<R> support;

    // CONSTRUCTEURS

    public ResourceLoader() {
        support = new ResourceLoaderSupport<R>(this);
    }

    // COMMANDES

    public void addResourceLoaderListener(ResourceLoaderListener<R> lst) {
        support.addResourceLoaderListener(lst);
    }

    public abstract void loadResource() throws Exception;

    public void removeResourceLoaderListener(ResourceLoaderListener<R> lst) {
        support.removeResourceLoaderListener(lst);
    }

    protected void fireResourceLoaded(R data) {
        support.fireResourceLoaded(data);
    }

    // OUTILS

    // cette méthode sert à ralentir les actions pour qu'on aie le temps
    // de voir que ça rame...
    protected void delayAction() {
        final int delay = 100;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
