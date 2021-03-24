package serie04.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingUtilities;

import serie04.event.SentenceListener;
import serie04.event.SentenceSupport;
import serie04.util.Formatter;
import util.Contract;

public abstract class AbstractActor implements Actor {
	//ATTRIBUTS 
	private Box box;
	private TaskCode taskCode;
	private Thread tWorker;
	private int maxIterNb;
	private final SentenceSupport ss;
	private PropertyChangeSupport pcs;

	//CONSTRUCTEUR
	AbstractActor(int maxIter, Box box) {
		Contract.checkCondition(maxIter > 0);
		Contract.checkCondition(box != null);

		this.box = box;
		maxIterNb = maxIter;

		ss = new SentenceSupport(this);
		pcs = new PropertyChangeSupport(this);
	}
	//REQUETES
	@Override
	public Box getBox() {
		return box;
	}

	@Override
	public int getMaxIterNb() {
		return maxIterNb;
	}

	@Override
	public SentenceListener[] getSentenceListeners() {
		return ss.getSentenceListeners();
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return pcs.getPropertyChangeListeners();
	}

	@Override
	public boolean isAlive() {
		return (tWorker == null) ? false : tWorker.isAlive();
	}

	@Override
	public boolean isActive() {
		return tWorker != null && this.isAlive() && !taskCode.isStopped;
	}

	//COMMANDES
	@Override
	public void addSentenceListener(SentenceListener listener) {
		Contract.checkCondition(listener != null);

		ss.addSentenceListener(listener);
	}

	@Override
	public void removeSentenceListener(SentenceListener listener) {
		Contract.checkCondition(listener != null);

		ss.removeSentenceListener(listener);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		Contract.checkCondition(listener != null);

		pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		Contract.checkCondition(listener != null);

		pcs.removePropertyChangeListener(listener);
	}

	@Override
	public void start() {
		Contract.checkCondition(!isAlive());

		boolean oldV = isActive();
		taskCode = new TaskCode();
		tWorker = new Thread(taskCode);
		//				new Thread(new Runnable() {
		//			@Override
		//			public void run() {
		//				if (taskCode != null) {
		//					taskCode.run();
		//				}
		//			}
		//		});
		tWorker.start();
		fireStateActiveChanged(oldV, isActive());
	}

	@Override
	public void interruptAndWaitForTermination() {
		Contract.checkCondition(isActive());

		boolean oldV = isActive();
		if (taskCode != null) {
			taskCode.stopped();
			tWorker.interrupt();
			try {
				tWorker.join();
			} catch (InterruptedException e) {
			}
			tWorker.interrupt();
			fireStateActiveChanged(oldV, isActive());
		}
	}

	protected void fireSentenceSaid(String string) {
		Contract.checkCondition(string != null);

		final String s = string;
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ss.fireSentenceSaid(s);
				}
			});
		} else {
			ss.fireSentenceSaid(s);
		}
		fireNewSentence(string);
	}
	protected void fireNewSentence(String string) {
		pcs.firePropertyChange("sentence", null, string);
	}
	protected void fireStateActiveChanged(final boolean oldV, final boolean newV) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					pcs.firePropertyChange(ACTIVE_NAME, oldV, newV);
				}
			});
		} else {
			pcs.firePropertyChange(ACTIVE_NAME, oldV, newV);
		}
	}

	//OUTILS
	private class TaskCode implements Runnable {
		//ATTRIBUTS
		private boolean isWaiting;
		private boolean isStopped;

		//CONSTRUCTEUR
		TaskCode() {
			isWaiting = false;
			isStopped = false;
		}
		boolean isWaiting() {
			return isWaiting;
		}
		//COMMANDE
		void stopped() {
			isStopped = true;
		}
		@Override
		public void run() {
			fireSentenceSaid("Naissance");
			int i = 0;
			while (!isStopped && i < getMaxIterNb()) {
				i += 1;
				fireSentenceSaid("Début de l'étape " + i);
				fireSentenceSaid("Demande le verrou");
				fireSentenceSaid("Acquiert le verrou");
				synchronized (box) {
					while (!isStopped && !canUseBox()) {
						fireSentenceSaid("Rentre dans la salle d'attente");
						try {
							isWaiting = true;
							box.wait();
						} catch (InterruptedException e) {
						}
						isWaiting = false;
						fireSentenceSaid("Sort de la salle d'attente");
					}

					if (canUseBox()) {
						useBox();
						box.notifyAll();
					}
					fireSentenceSaid("Libère le verrou");
				}
				fireSentenceSaid("Fin de l'étape " + i);
			}
			fireSentenceSaid(isStopped ? "Mort Subite" : "Mort naturelle");
		}
	}

	//REQUETES ABSTRAITES
	protected abstract boolean canUseBox();
	protected abstract void useBox();
	public abstract int number();
	public abstract Formatter getFormatter();
}
