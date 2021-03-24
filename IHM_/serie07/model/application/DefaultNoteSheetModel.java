package serie07.model.application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import serie07.event.ResourceEvent;
import serie07.event.ResourceListener;
import serie07.model.gui.NoteTableModel;
import serie07.model.technical.ResourceManager;

public class DefaultNoteSheetModel implements NoteSheetModel {
	
	public final static String ROW = "row";
	public final static String SAVED = "saved";
	public final static String PROGRESS = "progress";
	public final static String ERROR = "error";
	private PropertyChangeSupport pcs;
	private ResourceManager rm;
	
	public DefaultNoteSheetModel() {
		pcs = new PropertyChangeSupport(this);
		rm = new ResourceManager();
		rm.addResourceListener(new ResourceListener() {
			
			@Override
			public void resourceSaved(final ResourceEvent<String> e) {
				if(!SwingUtilities.isEventDispatchThread()) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							pcs.firePropertyChange(SAVED, null, e.getResource());
						}
					});
				} else {
					pcs.firePropertyChange(SAVED, null, e.getResource());
				}
			}
			
			@Override
			public void resourceLoaded(final ResourceEvent<String> e) {
				if(!SwingUtilities.isEventDispatchThread()) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							processData(e.getResource());
						}
					});
				} else {
					processData(e.getResource());
				}
			}
			
			@Override
			public void progressUpdated(final ResourceEvent<Integer> e) {
				if(!SwingUtilities.isEventDispatchThread()) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							pcs.firePropertyChange(PROGRESS, null, e.getResource());
						}
					});
				} else {
					pcs.firePropertyChange(PROGRESS, null, e.getResource());
				}
			}
			
			@Override
			public void errorOccured(final ResourceEvent<Exception> e) {
				if(!SwingUtilities.isEventDispatchThread()) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							pcs.firePropertyChange(ERROR, null, e.getResource());
						}
					});
				} else {
					pcs.firePropertyChange(ERROR, null, e.getResource());
				}
			}
			
			private void processData(String str) {
				String[] parts = str.split("\t");
				String sub = parts[0];
				Double coef = Double.parseDouble(parts[1]);
				Double mark = Double.parseDouble(parts[2]);
				ArrayList<Object> ligne = new ArrayList<Object>();
				ligne.add(sub);
				ligne.add(coef);
				ligne.add(mark);
				pcs.firePropertyChange(ROW, null, ligne);
			}
		});
	}

	@Override
	public double getMean(NoteTableModel m) {
		assert m != null;
		if (m.getRowCount() == 0) {
			return Double.NaN;
		}
		double sum = 0;
		for (int i = 0; i < m.getRowCount(); i++) {
			sum += (Double)m.getValueAt(i, NoteTableModel.COEF);
		}
		return getPoints(m) / sum;
	}

	@Override
	public double getPoints(NoteTableModel m) {
		assert m != null;
		double sum = 0;
		for (int i = 0; i < m.getRowCount(); i++) {
			sum += (Double)m.getValueAt(i, NoteTableModel.POINTS);
		}
		return sum;
	}

	@Override
	public void addPropertyChangeListener(String prop, PropertyChangeListener lst) {
		pcs.addPropertyChangeListener(prop, lst);
	}

	@Override
	public void removePropertyChangeListener(String prop, PropertyChangeListener lst) {
		pcs.removePropertyChangeListener(prop, lst);
	}

	@Override
	public void loadFile(final File f) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				rm.loadResource(f);
			}
		}).start();
	}

	@Override
	public void saveFile(final File f,final NoteTableModel m) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<String> lignes = new ArrayList<String>();
				for (int i = 0; i < m.getRowCount(); i++) {
					String str = m.getValueAt(i, 0).toString() + "\t" + m.getValueAt(i, 1) + "\t" + m.getValueAt(i, 2);
					lignes.add(str);
				}
				rm.saveResource(f, lignes);
			}
		}).start();
	}

}
