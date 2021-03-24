package serie04.event;

import javax.swing.event.EventListenerList;

public class SentenceSupport {

    private final Object owner;
    private EventListenerList listenersList;

    public SentenceSupport(Object owner) {
        this.owner = owner;
        listenersList = new EventListenerList();
    }

    public synchronized SentenceListener[] getSentenceListeners() {
        if (listenersList == null) {
            return new SentenceListener[0];
        }
        return listenersList.getListeners(SentenceListener.class);
    }

    public synchronized void addSentenceListener(SentenceListener listener) {
        if (listener == null) {
            return;
        }
        if (listenersList == null) {
            listenersList = new EventListenerList();
        }
        listenersList.add(SentenceListener.class, listener);
    }

    public synchronized void removeSentenceListener(SentenceListener listener) {
        if (listener == null || listenersList == null) {
            return;
        }
        listenersList.remove(SentenceListener.class, listener);
    }

    public void fireSentenceSaid(String sentence) {
        Object[] list = listenersList.getListenerList();
        SentenceEvent e = new SentenceEvent(owner, sentence);
        for (int i = list.length - 2; i >= 0; i--) {
            ((SentenceListener) list[i + 1]).sentenceSaid(e);
        }
    }
}
