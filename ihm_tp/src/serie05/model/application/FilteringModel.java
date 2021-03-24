package serie05.model.application;

import java.util.List;

import javax.swing.SwingUtilities;

import serie05.event.ResourceLoaderEvent;
import serie05.event.ResourceLoaderListener;
import serie05.event.ResourceLoaderSupport;
import serie05.model.filters.Filter;
import serie05.model.filters.Filterable;
import serie05.model.technical.ResourceLoader;

public class FilteringModel<E extends Filterable<V>, V> {

    // ATTRIBUTS

    private final ResourceLoaderSupport<E> support;
    private final ResourceLoader<E> loader;
    private final List<Filter<E, V>> filters;

    // CONSTRUCTEURS

    public FilteringModel(List<Filter<E, V>> filters, ResourceLoader<E> rl) {
        this.filters = filters;
        loader = rl;
        loader.addResourceLoaderListener(new ResourceLoaderListener<E>() {
            @Override
            public void resourceLoaded(final ResourceLoaderEvent<E> e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        support.fireResourceLoaded(e.getResource());
                    }
                });
            }
        });
        support = new ResourceLoaderSupport<E>(this);
    }

    // REQUETES

    public List<Filter<E, V>> getFilters() {
        return filters;
    }

    // COMMANDES

    public void addResourceLoaderListener(ResourceLoaderListener<E> lst) {
        support.addResourceLoaderListener(lst);
    }

    public void removeResourceLoaderListener(ResourceLoaderListener<E> lst) {
        support.removeResourceLoaderListener(lst);
    }

    public void populate() {
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loader.loadResource();
                } catch (Exception e) {
                    // TODO: il faudrait faire mieux que ça...
                    System.err.println(e.getMessage());
                }
            }
        });
        worker.start();
    }
}
