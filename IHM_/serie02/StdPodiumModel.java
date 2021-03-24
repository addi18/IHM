package serie02;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Implémentation standard de PodiumModel (SANS CHANGE LISTENER).
 */
public class StdPodiumModel<E> implements PodiumModel<E> {

    // ATTRIBUTS

    private List<E> data;
    private int capacity;
    private EventListenerList listeners;
    private ChangeEvent event;

    // CONSTRUCTEURS

    public StdPodiumModel(List<E> init, int capacity) {
        Contract.checkCondition(init != null);
        Contract.checkCondition(!containsNullValue(init));

        this.capacity = capacity;
        data = new ArrayList<E>(init);
        listeners = new EventListenerList();
    }

    public StdPodiumModel() {
        this(new ArrayList<E>(), 0);
    }

    // REQUETES

    @Override
    public E bottom() {
        Contract.checkCondition(size() > 0);

        return data.get(0);
    }

    @Override
    public E elementAt(int i) {
        Contract.checkCondition(0 <= i && i < capacity());

        if (i < size()) {
            return data.get(i);
        } else {
            return null;
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public boolean similar(PodiumModel<E> that) {
        Contract.checkCondition(that != null);

        int dataSize = data.size();
        boolean result = (that.capacity() == capacity
                && that.size() == dataSize);
        for (int i = 0; result && (i < dataSize); i++) {
            result = that.elementAt(i).equals(data.get(i));
        }
        return result;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public E top() {
        Contract.checkCondition(size() > 0);

        return data.get(data.size() - 1);
    }

    @Override
    public String toString() {
        String res = "[";
        int dataSize = data.size();
        for (int i = 0; i < dataSize; i++) {
            res += data.get(i) + "|";
        }
        return capacity + "/" + res + "]";
    }

    // COMMANDES

    @Override
    public void addTop(E elem) {
        Contract.checkCondition(elem != null);
        Contract.checkCondition(size() < capacity());

        data.add(elem);
        fireStateChanged();
    }

    @Override
    public void removeBottom() {
        Contract.checkCondition(size() > 0);

        data.remove(0);
        fireStateChanged();
    }

    @Override
    public void removeTop() {
        Contract.checkCondition(size() > 0);

        data.remove(size() - 1);
        fireStateChanged();
    }

    // OUTILS

    private boolean containsNullValue(List<E> list) {
        for (E e : list) {
            if (e == null) {
                return true;
            }
        }
        return false;
    }

    //AJOUT DE MOI
	@Override
	public void addChangeListener(ChangeListener cl) {
		assert cl != null;
		listeners.add(ChangeListener.class, cl);
	}

	@Override
	public void removeChangeListener(ChangeListener cl) {
		assert cl != null;
		listeners.remove(ChangeListener.class, cl);
	}
	
	public ChangeListener[] getChangeListeners() {
		return listeners.getListeners(ChangeListener.class);
	}
	
	protected void fireStateChanged() {
		Object[] lst = listeners.getListenerList();
		for (int i = lst.length - 2; i >= 0; i -=2) {
			if (lst[i] == ChangeListener.class) {
				if (event == null) {
					event = new ChangeEvent(this);
				}
				((ChangeListener) lst[i + 1]).stateChanged(event);
			}
		}
	}
	
}
