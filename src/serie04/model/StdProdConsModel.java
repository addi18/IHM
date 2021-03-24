package serie04.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import serie04.event.SentenceEvent;
import serie04.event.SentenceListener;
import serie04.util.Formatter;
import util.Contract;

public class StdProdConsModel implements ProdConsModel {

    // ATTRIBUTS

    private static final int MAX_VALUE = 100;

    private final Actor[] actors;
    private final Box box;
    private final int prodNumber;
    private final int consNumber;
    private final PropertyChangeSupport support;

    private boolean running;
    private boolean frozen;
    private int numberOfActiveActors;

    // CONSTRUCTEURS

    public StdProdConsModel(int prod, int cons, int iter) {
        Contract.checkCondition(prod > 0 && cons > 0 && iter > 0);

        box = new UnsafeBox();
        prodNumber = prod;
        consNumber = cons;
        SentenceListener sl = new SentenceListener() {
            @Override
            public void sentenceSaid(SentenceEvent e) {
                fireNewSentence(e.getSentence());
            }
        };
        PropertyChangeListener pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(Actor.ACTIVE_NAME)
                        && evt.getNewValue() == Boolean.TRUE) {
                    numberOfActiveActors += 1;
                } else {
                    numberOfActiveActors -= 1;
                }
                setFrozen();
            }
        };
        actors = new Actor[prodNumber + consNumber];
        for (int i = 0; i < prodNumber; i++) {
            actors[i] = new StdProducer(iter, MAX_VALUE, box);
            actors[i].addSentenceListener(sl);
            actors[i].addPropertyChangeListener(pcl);
        }
        for (int i = prodNumber; i < prodNumber + consNumber; i++) {
            actors[i] = new StdConsumer(iter, box);
            actors[i].addSentenceListener(sl);
            actors[i].addPropertyChangeListener(pcl);
        }
        support = new PropertyChangeSupport(this);
    }

    // REQUETES

    @Override
    public Box box() {
        return box;
    }

    @Override
    public Actor consumer(int i) {
        Contract.checkCondition(0 <= i && i < consNumber);

        return actors[prodNumber + i];
    }

    @Override
    public int consumersNb() {
        return consNumber;
    }

    @Override
    public boolean isFrozen() {
        if (!isRunning()) {
            return false;
        } else {
            return frozen;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Actor producer(int i) {
        Contract.checkCondition(0 <= i && i < prodNumber);

        return actors[i];
    }

    @Override
    public int producersNb() {
        return prodNumber;
    }

    @Override
    public void addPropertyChangeListener(String pName,
                PropertyChangeListener lst) {
        if (lst == null) {
            return;
        }
        support.addPropertyChangeListener(pName, lst);
    }

    @Override
    public void stop() {
        for (int j = 0; j < actors.length; j++) {
            if (actors[j].isAlive()) {
                actors[j].interruptAndWaitForTermination();
            }
        }
        boolean oldRunning = running;
        running = false;
        fireRunningStateChanged(oldRunning, running);
    }

    @Override
    public void removePropertyChangeListener(String pName,
                PropertyChangeListener lst) {
        if (lst == null) {
            return;
        }
        support.removePropertyChangeListener(pName, lst);
    }

    @Override
    public void start() {
        Contract.checkCondition(!isRunning());

        box.dump();
        Formatter.resetTime();
        boolean oldRunning = running;
        running = true;
        fireRunningStateChanged(oldRunning, running);
        for (int i = 0; i < actors.length; i++) {
            actors[i].start();
        }
    }

    // OUTILS

    private void setFrozen() {
        boolean oldFrozen = isFrozen();
        frozen = (prodAreDead() || consAreDead()) && numberOfActiveActors == 0;
        fireFrozenStateChanged(oldFrozen, frozen);
    }

    private boolean prodAreDead() {
        return actorsAreDead(0, prodNumber);
    }

    private boolean consAreDead() {
        return actorsAreDead(prodNumber, prodNumber + consNumber);
    }

    private boolean actorsAreDead(int begin, int end) {
        for (int i = begin; i < end; i++) {
            if (actors[i].isAlive()) {
                return false;
            }
        }
        return true;
    }

    private void fireRunningStateChanged(boolean oldValue, boolean newValue) {
        support.firePropertyChange(RUNNING_NAME, oldValue, newValue);
    }

    private void fireFrozenStateChanged(boolean oldValue, boolean newValue) {
        support.firePropertyChange(FROZEN_NAME, oldValue, newValue);
    }

    private void fireNewSentence(String sentence) {
        support.firePropertyChange(SENTENCE_NAME, null, sentence);
    }
}
