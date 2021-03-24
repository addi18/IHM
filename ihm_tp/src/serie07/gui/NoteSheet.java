package serie07.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import serie07.model.application.DefaultNoteSheetModel;
import serie07.model.application.NoteSheetModel;
import serie07.model.gui.DefaultNoteTableModel;
import serie07.model.gui.NoteTableModel;

@SuppressWarnings("serial")
public class NoteSheet extends JPanel {

	private JTable table;
	private NoteSheetModel model;
	private int progress;
	private EventListenerList listeners;
	private ChangeEvent event;
	private JPopupMenu menu;
	private JScrollPane scrollPane;
	
	public NoteSheet() {
		progress = 0;
		table = new JTable(new DefaultNoteTableModel());
		scrollPane = new JScrollPane();
		listeners = new EventListenerList();
		model = new DefaultNoteSheetModel();
		placeComponents();
		createController();
	}
	
	public NoteTableModel getTableModel() {
		return (NoteTableModel) table.getModel();
	}
	
	public double getMean() {
		return model.getMean(getTableModel());
	}
	
	public double getPoints() {
		return model.getPoints(getTableModel());
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void loadFile(File f) {
		getTableModel().clearRows();
		model.loadFile(f);
	}
	
	public void saveFile(File f) {
		model.saveFile(f, getTableModel());
	}
	
	public void addProgressListener(ChangeListener cl) {
		listeners.add(ChangeListener.class, cl);
	}
	
	public void removeProgressListener(ChangeListener cl) {
		listeners.remove(ChangeListener.class, cl);
	}
	
	public ChangeListener[] getChangeListeners() {
		return listeners.getListeners(ChangeListener.class);
	}
	
	protected void fireStateChanged() {
		Object[] lst = listeners.getListenerList();
		for (int i = lst.length - 2; i >= 0; i -= 2) {
			if (lst[i] == ChangeListener.class) {
				if (event == null) {
					event = new ChangeEvent(this);
				}
				((ChangeListener) lst[i + 1]).stateChanged(event);
			}
		}
	}
	
	private void placeComponents() {
		scrollPane.setViewportView(table);
		this.add(scrollPane);
	}
	
	private void createController() {
		model.addPropertyChangeListener(DefaultNoteSheetModel.ROW, new PropertyChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				getTableModel().addRow((List<Object>) arg0.getNewValue());
			}
		});
		model.addPropertyChangeListener(DefaultNoteSheetModel.PROGRESS, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				progress = (Integer) arg0.getNewValue();
				fireStateChanged();
			}
		});
		model.addPropertyChangeListener(DefaultNoteSheetModel.SAVED, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				JOptionPane.showMessageDialog(null, "Sauvegardé dans " + arg0.getNewValue(), "Succès !", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		model.addPropertyChangeListener(DefaultNoteSheetModel.ERROR, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				Exception err = ((Exception) arg0.getNewValue());
				JOptionPane.showMessageDialog(null, err.getMessage(), "Erreur !", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		JMenuItem insertBefore = new JMenuItem("Insérer avant cette ligne");
		insertBefore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getTableModel().insertRow(getTableModel().getEmptyDataRow(), table.getSelectedRow());				
			}
		});
		JMenuItem insertAfter = new JMenuItem("Insérer après cette ligne");
		insertAfter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getTableModel().insertRow(getTableModel().getEmptyDataRow(), table.getSelectedRow() + 1);
			}
		});
		JMenuItem insertEnd = new JMenuItem("Insérer à la fin");
		insertEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getTableModel().addRow(getTableModel().getEmptyDataRow());
			}
		});
		JMenuItem create = new JMenuItem("Insérer une ligne");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getTableModel().addRow(getTableModel().getEmptyDataRow());
			}
		});
		JMenuItem deleteRow = new JMenuItem("Supprimer cette ligne");
		deleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getTableModel().removeRow(table.getSelectedRow());
			}
		});
		JMenuItem deleteAll = new JMenuItem("Supprimer toutes les lignes");
		deleteAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getTableModel().clearRows();
			}
		});
		menu = new JPopupMenu() {
			@Override
			public void show(Component invoker, int x, int y) {
				int row = table.rowAtPoint(new Point(x, y));
				if (row != -1) {
					table.setRowSelectionInterval(row, row);
				}
				super.show(invoker, x, y);
			}
		}; {
			menu.add(insertBefore);
			menu.add(insertAfter);
			menu.add(insertEnd);
			menu.addSeparator();
			menu.add(deleteRow);
			menu.add(deleteAll);
		}
		table.setComponentPopupMenu(menu);
		final JPopupMenu preMenu = new JPopupMenu();
		preMenu.add(create);
		scrollPane.setComponentPopupMenu(preMenu);
		getTableModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent arg0) {
				if (((NoteTableModel)arg0.getSource()).getRowCount() == 0) {
					scrollPane.setComponentPopupMenu(preMenu);
				} else {
					scrollPane.setComponentPopupMenu(null);
				}
			}
		});


	}
}
