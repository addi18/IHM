package serie04.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.Thread.State;

import javax.swing.SwingUtilities;

import serie04.event.SentenceListener;
import serie04.event.SentenceSupport;
import serie04.util.Formatter;

public abstract class AbstractActor implements Actor {
	
	protected Box box;
	private final int maxIterNb;
	private Thread workerThread;
	private final TaskCode task;
	protected final SentenceSupport sentSup;
	private final PropertyChangeSupport pcs;
	protected final Formatter format;
	private static final Object lock = new Object();
	private static final Object pptLock = new Object();

	public AbstractActor(int maxIter, Box box, Formatter f) {
		assert box != null && maxIter > 0;
		this.box = box;
		this.maxIterNb = maxIter;
		format = f;
		sentSup = new SentenceSupport(this);
		task = new TaskCode();
		workerThread = new Thread(task);
		pcs = new PropertyChangeSupport(this);
		task.stopped = false;
	}

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
		return sentSup.getSentenceListeners();
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return pcs.getPropertyChangeListeners();
	}

	@Override
	public boolean isAlive() {
		return workerThread.isAlive();
	}

	@Override
	public boolean isActive() {
		return task.isActive();
	}

	@Override
	public void addSentenceListener(SentenceListener listener) {
		sentSup.addSentenceListener(listener);
	}

	@Override
	public void removeSentenceListener(SentenceListener listener) {
		sentSup.removeSentenceListener(listener);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	@Override
	public void start() {
		assert !isAlive();
		if(workerThread.getState() == State.TERMINATED) {
			workerThread = new Thread(task);
		}
		workerThread.start();
	}

	@Override
	public void interruptAndWaitForTermination() {
		System.out.println("interruptAndWaitForTermination");

		task.setStopped(true);

		workerThread.interrupt();
		/*
		System.out.println(t.getName() + " isInterrupted() : " + t.isInterrupted());
		System.out.println(t.getName() + " isActive() : " + isActive());
		System.out.println(t.getName() + " getState() : " + t.getState());
		*/
		while (workerThread.isAlive()) {
			try {
				workerThread.join();
			} catch (InterruptedException e) {
				
			}
		
		}

		
	}
	
	protected void writeMsg(String str) {
		task.fireSentenceSaid(str);
	}
	
	protected abstract void useBox();
	protected abstract boolean canUseBox();
	
	private class TaskCode implements Runnable {
		
		private volatile boolean stopped;
		private boolean isActive;
		private boolean active;

		private synchronized void setStopped(boolean b) {
			stopped = b; 
			//System.out.println("Stopped : " + workerThread.getName() + " / stopped : " + stopped + " / alive : " + isAlive());

		}
		
		private synchronized boolean isActive() {
			return active;
		}
		
		private synchronized void setActive() {
			boolean oldValue = isActive();
			active = isActive && Thread.currentThread().isAlive();
			fireActiveStateChanged(oldValue, active);
			
		}
		
		private void fireActiveStateChanged(final boolean oldValue, final boolean newValue) {
			if(!SwingUtilities.isEventDispatchThread()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						pcs.firePropertyChange(ACTIVE_NAME, oldValue, newValue);
					}
				});
			} else {
				pcs.firePropertyChange(ACTIVE_NAME, oldValue, newValue);
			}
		}
		
		private void fireSentenceSaid(final String str) {
			if(!SwingUtilities.isEventDispatchThread()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						sentSup.fireSentenceSaid(format.format(str));
					}
				});
			} else {
				sentSup.fireSentenceSaid(format.format(str));
			}
		}
		
		@Override
		public void run() {
			isActive = true;
			setActive();
			fireSentenceSaid("Naissance");
			setStopped(false);
			int i = 0;
			while (!stopped && i < getMaxIterNb()) {
				i++;
				fireSentenceSaid("Début de l'étape " + i);
				fireSentenceSaid("Demande le verrou ");
				synchronized (box) {
					fireSentenceSaid("Acquiert le verrou");
					while (!stopped && !canUseBox()) {
						fireSentenceSaid("Rentre dans la salle d'attente");
						isActive = false;
						setActive();
						try {
							box.wait();
						} catch (InterruptedException e) {
							System.out.println("Interruption détectée");
						}
						isActive = true;
						setActive();
						fireSentenceSaid("Sort de la salle d'attente");
					}
					if (!stopped) {
						useBox();
					}
					box.notifyAll();

				}
				fireSentenceSaid("Libère le verrou");
				fireSentenceSaid("Fin de l'étape " + i);
			}
			if (stopped) {
				fireSentenceSaid("Mort subite");
			} else {
				fireSentenceSaid("Mort naturelle");
			}
			isActive = false;
			setActive();
		}
		
	}

}
