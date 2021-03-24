package serie05.model.filters;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import util.Contract;


/**
 * Implantation partielle des critères de filtrage.
 */
public abstract class AbstractFilter<E extends Filterable<V>, V>
        implements Filter<E, V> {

    // ATTRIBUTS

    private final PropertyChangeSupport propSupport;
    private V value;

    // CONSTRUCTEURS

    protected AbstractFilter(V initValue) {
        Contract.checkCondition(initValue != null);

        value = initValue;
        propSupport = new PropertyChangeSupport(this);
    }

    // REQUETES

    @Override
    public List<E> filter(List<E> list) {
        Contract.checkCondition(list != null);

        List<E> result = new ArrayList<E>();
        for (E e : list) {
            if (isValid(e)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public PropertyChangeListener[] getValueChangeListeners() {
        return propSupport.getPropertyChangeListeners("value");
    }

    // COMMANDES

    @Override
    public void addValueChangeListener(PropertyChangeListener lst) {
        if (lst == null) {
            return;
        }
        propSupport.addPropertyChangeListener("value", lst);
    }

    @Override
    public void removeValueChangeListener(PropertyChangeListener lst) {
        if (lst == null) {
            return;
        }
        propSupport.removePropertyChangeListener("value", lst);
    }

    @Override
    public void setValue(V val) {
        Contract.checkCondition(val != null);
        V oldValue = value;
        value = val;
        propSupport.firePropertyChange("value", oldValue, value);
    }
}
