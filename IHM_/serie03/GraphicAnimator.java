package serie03;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class GraphicAnimator {
	
	//VARIABLES
	
	private EventListenerList listenerList;
	private ChangeEvent changeEvent;
	private Thread animThread;
	private final int maxSpeed;
	private SharedState state;
	private Animable component;
	
	//CONSTRUCTEURS
	
	public GraphicAnimator(Animable c, int s) {
		maxSpeed = s;
		listenerList = new EventListenerList();
		state = new SharedState();
		component = c;
		animThread = new Thread(new AnimationLoop());
	}
	
	//REQUETES
	
	public boolean isAnimationPaused() {
		return state.isPaused();
	}
	
	public boolean isAnimationRunning() {
		return state.isRunning();
	}
	
	public boolean isAnimationStopped() {
		return state.isStopped();
	}
	
	public boolean isAnimationStarted() {
		return state.isStarted();
	}
	
	public int getSpeed() {
		return state.getSpeed();
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	//COMMANDES

	public void startAnimation() {
		state.setStarted(true);
		animThread.start();
		fireStateChanged();
	}
	
	public void pauseAnimation() {
		state.setPaused(true);
		fireStateChanged();
	}
	
	public void resumeAnimation() {
		state.setPaused(false);
		synchronized (state) {
			state.notify();
		}
		fireStateChanged();
	}
	
	public void stopAnimation() {
		state.setStopped(true);
		synchronized (state) {
			state.notify();
		}
		fireStateChanged();
	}
	
	public void setSpeed(int d) {
		state.setSpeed(d);
		fireStateChanged();
	}
	
	//OUTILS
	
	public void addChangeListener(ChangeListener listener) {
		listenerList.add(ChangeListener.class, listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		listenerList.remove(ChangeListener.class, listener);
	}
	
	public ChangeListener[] getChangeListeners() {
		return listenerList.getListeners(ChangeListener.class);
	}
	
	public void fireStateChanged() {
		Object[] lst = listenerList.getListenerList();
		for (int i = lst.length - 2; i >= 0; i -=2) {
			if (lst[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) lst[i + 1]).stateChanged(changeEvent);
			}
		}
	}
	
	private class SharedState {
		private boolean started;
		private boolean stopped;
		private boolean paused;
		private volatile int speed;
		
		synchronized boolean isStarted() {
			return started;
		}

		synchronized boolean isRunning() {
			return started && !stopped;
		}

		synchronized boolean isStopped() {
			return stopped && started;
		}
		
		synchronized boolean isPaused() {
			return started && !stopped && paused;
		}
		
		synchronized int getSpeed() {
			return speed;
		}
		
		synchronized void setSpeed(int d) {
			speed = d;
		}
		
		synchronized void setStarted(boolean b) {
			started = b;
		}
		
		synchronized void setStopped(boolean b) {
			stopped = b;
		}
		
		synchronized void setPaused(boolean b) {
			paused = b;
		}
		
	}
	
	private class AnimationLoop implements Runnable{
		@Override
		public void run() {
			while (!state.isStopped()) {
				while(state.isPaused()) {
					try {
						synchronized(state) {
							state.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000/(state.getSpeed() + 1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				component.animate();
			}
			System.out.println("Fin du thread");
		}
	}
 }
