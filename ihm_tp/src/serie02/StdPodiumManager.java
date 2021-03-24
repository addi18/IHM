package serie02;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;


public class StdPodiumManager<E extends Drawable>
        implements PodiumManager<E> {

    // ATTRIBUTS

    private final Set<E> drawables;
    private int shots;
    private long time;
    private long delta;
    public static final String PROP_LAST_ORDER = "lastOrder";
    private PropertyChangeSupport pcs;
    private VetoableChangeSupport vcs;
    private Order lastOrder;
    private EnumMap<Rank, Podium<E>> podiums;

    // CONSTRUCTEURS

    /**
     * @pre <pre>
     *     drawables != null
     *     2 <= drawables.size() </pre>
     */
    public StdPodiumManager(Set<E> drawables) {
        Contract.checkCondition(drawables != null);
        Contract.checkCondition(drawables.size() >= 2);

        this.drawables = new HashSet<E>(drawables);
        podiums = new EnumMap<Rank, Podium<E>>(Rank.class);
        for (Rank r : Rank.values()) {
            podiums.put(r, new Podium<E>(new StdPodiumModel<E>()));
        }
        pcs = new PropertyChangeSupport(this);
        vcs = new VetoableChangeSupport(this);
        changePodiumModels();
        internalReinit();
        pcs.firePropertyChange(PROP_FINISHED, true, false);
    }

    // REQUETES

    @Override
    public Order getLastOrder() {
        return lastOrder;
    }

    @Override
    public Map<Rank, Podium<E>> getPodiums() {
        return podiums.clone();
    }

    @Override
    public int getShotsNb() {
        return shots;
    }

    @Override
    public long getTimeDelta() {
        return delta;
    }

    @Override
    public boolean isFinished() {
        PodiumModel<E> mIniLeft = podiums.get(Rank.WRK_LEFT).getModel();
        PodiumModel<E> mIniRight = podiums.get(Rank.WRK_RIGHT).getModel();
        PodiumModel<E> mObjLeft = podiums.get(Rank.OBJ_LEFT).getModel();
        PodiumModel<E> mObjRight = podiums.get(Rank.OBJ_RIGHT).getModel();
        return mIniLeft.similar(mObjLeft) && mIniRight.similar(mObjRight);
    }

    // COMMANDES

    @Override
    public void executeOrder(Order o) throws PropertyVetoException {
        Contract.checkCondition(o != null);

        boolean oldValue = isFinished();
        switch (o) {
            case LO:
                sendTop(Rank.WRK_LEFT, Rank.WRK_RIGHT);
                break;
            case KI:
                sendTop(Rank.WRK_RIGHT, Rank.WRK_LEFT);
                break;
            case MA:
                cycle(Rank.WRK_LEFT);
                break;
            case NI:
                cycle(Rank.WRK_RIGHT);
                break;
            case SO:
            	vcs.fireVetoableChange(PROP_LAST_ORDER, null, o);
                exchangeTops();
                break;
            default:
                throw new AssertionError();
        }
        update(oldValue, o);
    }

    @Override
    public void reinit() {
        changePodiumModels();
        internalReinit();
        pcs.firePropertyChange(PROP_FINISHED, true, false);

    }

    // OUTILS

    private void exchangeTops() {
        PodiumModel<E> left = podiums.get(Rank.WRK_LEFT).getModel();
        PodiumModel<E> right = podiums.get(Rank.WRK_RIGHT).getModel();
        if (left.size() > 0 && right.size() > 0) {
            E elem = left.top();
            left.removeTop();
            left.addTop(right.top());
            right.removeTop();
            right.addTop(elem);
        }
    }

    private void cycle(Rank r) {
        PodiumModel<E> m = podiums.get(r).getModel();
        if (m.size() > 0) {
            E elem = m.bottom();
            m.removeBottom();
            m.addTop(elem);
        }
    }

    private void sendTop(Rank from, Rank to) {
        PodiumModel<E> f = podiums.get(from).getModel();
        PodiumModel<E> t = podiums.get(to).getModel();
        if (f.size() > 0 && t.size() < t.capacity()) {
            E elem = f.top();
            f.removeTop();
            t.addTop(elem);
        }
    }

    private void update(boolean oldF, Order order) {
        shots += 1;
        boolean newF = isFinished();
        if (newF) {
            delta = System.currentTimeMillis() - time;
        }
        pcs.firePropertyChange(PROP_LAST_ORDER, null, order);
        pcs.firePropertyChange(PROP_FINISHED, oldF, newF);
    }

    private void internalReinit() {
        shots = 0;
        delta = 0;
        time = System.currentTimeMillis();
    }

    /**
     * Construit les 4 séquences d'éléments de E, puis les 4 modèles de podiums
     *  basés sur ces séquences.
     * La concaténation des deux premières séquences est une permutation des
     *  éléments de drawables.
     * La concaténation des deux dernières séquences est aussi une permutation
     *  des éléments de drawables.
     * Il se peut que les permutations soient identiques.
     */
    private void changePodiumModels() {
        List<List<E>> lst = createRandomElements();
        lst.addAll(createRandomElements());
        for (Rank r : Rank.values()) {
            podiums.get(r).setModel(
                    new StdPodiumModel<E>(
                            lst.get(r.ordinal()),
                            drawables.size()));
        }
    }

    /**
     * Construit une séquence de deux séquences aléatoires d'éléments de E, à
     *  partir de l'ensemble drawables, un peu comme on distribue des cartes :
     *  - on commence par mélanger les cartes,
     *  - puis on les distribue au hasard, une par une, en deux tas (de tailles
     *  pas forcément égales donc).
     */
    private List<List<E>> createRandomElements() {
        final double ratio = 0.5;
        List<E> elements = new LinkedList<E>(drawables);
        // on mélange les éléments de drawables dans list
        // c'est quadratique en temps mais on s'en fout
        List<E> list = new ArrayList<E>(drawables.size());
        for (int i = drawables.size(); i > 0; i--) {
            int k = ((int) (Math.random() * i));
            list.add(elements.get(k));
            elements.remove(k);
        }
        // on distribue au hasard les éléments de list dans elemsL (les éléments
        // de gauche) et dans elemsR (les éléments de droite)
        List<E> elemsL = new ArrayList<E>(drawables.size());
        List<E> elemsR = new ArrayList<E>(drawables.size());
        for (E e : list) {
            if (Math.random() < ratio) {
                elemsL.add(e);
            } else {
                elemsR.add(e);
            }
        }
        ArrayList<List<E>> result = new ArrayList<List<E>>(2);
        result.add(elemsL);
        result.add(elemsR);
        return result;
    }

    //AJOUT A MOI
    
	@Override
	public void addPropertyChangeListener(String propName, PropertyChangeListener lst) {
		pcs.addPropertyChangeListener(propName, lst);
	}

	@Override
	public void addVetoableLastOrderChangeListener(VetoableChangeListener lst) {
		vcs.addVetoableChangeListener(PROP_LAST_ORDER, lst);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener lst) {
		pcs.removePropertyChangeListener(lst);
	}

	@Override
	public void removeVetoableChangeListener(VetoableChangeListener lst) {
		vcs.addVetoableChangeListener(PROP_LAST_ORDER, lst);
	}
}
