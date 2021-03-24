package serie07.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import serie07.model.application.DefaultNoteSheetModel;
import serie07.model.application.NoteSheetModel;
import serie07.model.gui.ColumnFeatures;
import serie07.model.gui.DefaultNoteTableModel;
import serie07.model.gui.NoteTableModel;
import util.Contract;

public class NoteSheet extends JPanel {
	//ATTRIBUTS
	private JTable table;
	private NoteTableModel jTableModel;
	private JScrollPane ntmScroll;
	private NoteSheetModel appliModel;
	protected EventListenerList listenerList;
	protected ChangeEvent changeEvent;
	private int progress;
	
	//CONSTRUCTEUR
	public NoteSheet() {
		createModel();
		createView();
		placeComponent();
		createController();
	}
	
	//REQUETES
	/*Permet d'accéder au modèle de la JTable associée.*/
	public NoteTableModel getTableModel() {
		return jTableModel;
	}
	
	/*Donne la moyenne correspondant aux notes stockées 
	* dans le modèle de la JTable.*/
	public double getMean() {
		return appliModel.getMean(jTableModel);
	}
	
	/*Donne le nombre de points correspondant aux notes stockées 
	 * dans le modèle de la JTable.*/
	public double getPoints() {
		return appliModel.getPoints(jTableModel);
	}
	/*Donne le niveau de progression de la tâche de chargement ou de sauvegarde 
	 * en cours (un entier compris entre 0 et 100).*/
	public int getProgress() {
		return progress;
	}
	
	//COMMANDES
	/*Provoque le chargement du contenu d'un fichier de notes dans le modèle de 
	 * la JTable*/
	public void loadFile(File f) {
		Contract.checkCondition(f != null);
		
		jTableModel.clearRows();
		appliModel.loadFile(f);
	}
	
	/*provoque l'enregistrement de l'état du modèle de la JTable dans un 
	 * fichier de notes*/
	public void saveFile(File f) {
		Contract.checkCondition(f != null);
		
		appliModel.saveFile(f, getTableModel());
	}
	/*Installe un ChangeListener qui sera notifié (par des notifications de type pull) 
	 * de tout changement dans la valeur de la propriété progress du modèle du bean.*/
	public void addProgressListener(ChangeListener cl) {
		Contract.checkCondition(cl != null);
		
		listenerList.add(ChangeListener.class, cl);
	}
	
	/*désinstalle un ChangeListener préalablement installé sur le bean en tant que 
	 * « progress listener. »*/
	public void removeProgressListener(ChangeListener cl) {
		Contract.checkCondition(cl != null);
		
		listenerList.remove(ChangeListener.class, cl);
	}

	//OUTILS
	private void createModel() {
		jTableModel = new DefaultNoteTableModel();
		appliModel = new DefaultNoteSheetModel();
		listenerList = new EventListenerList();
		changeEvent = new ChangeEvent(this);
	}
	
	private void placeComponent() {
		setHeader();
		add(ntmScroll);
	}

	private void createController() {
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				table.addRowSelectionInterval(row, row);
			}
		});
		
		table.setComponentPopupMenu(createPopMenu());
		
		JPopupMenu popUpMenu = new JPopupMenu();
		JMenuItem insertStart = new JMenuItem("Insérer une ligne");
		insertStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Object> l = addLine();
				if (l.size() == 3) {
					jTableModel.addRow(l);
				}
			}
		});
		popUpMenu.add(insertStart);
		ntmScroll.setComponentPopupMenu(popUpMenu);
		
		jTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				JPopupMenu jPopupMenu = new JPopupMenu();
				if (jTableModel.getRowCount() == 0) {
					JMenuItem insertBefore = new JMenuItem("Inserer une ligne");
					insertBefore.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							jTableModel.insertRow(addLine(), 0);
						}
					});
					jPopupMenu.add(insertBefore);
					ntmScroll.setComponentPopupMenu(jPopupMenu);
				} else {
					if (e.getType() == TableModelEvent.INSERT && jTableModel.getRowCount() == 1) {
						ntmScroll.remove(jPopupMenu);
					}
				}
			}
		});
		
		appliModel.addPropertyChangeListener("progress", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				progress = (Integer) evt.getNewValue();
				fireStateChanged();
			}
		});
		
		appliModel.addPropertyChangeListener("row", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				jTableModel.addRow((List<Object>) evt.getNewValue());
			}
		});
		
		appliModel.addPropertyChangeListener("error", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				JOptionPane.showMessageDialog(ntmScroll, "Une erreur s'est produite, Veuillez réessayer s'il vous plait!!",
					      "Erreur!!",
					      JOptionPane.ERROR_MESSAGE);
				jTableModel.clearRows();
			}
		});
	}

	private void createView() {
		table = new JTable();
		table.setModel(jTableModel);
		ntmScroll = new JScrollPane(table);
	}
	
	protected void fireStateChanged() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
		    	for (Object cl : listeners) {
		    		if (changeEvent == null) {
		    			changeEvent = new ChangeEvent(this);
		    		}
		    		((ChangeListener) cl).stateChanged(changeEvent);
		    	}
			}
		});
    }
	
	private void setHeader() {
		TableColumn names;
		for (ColumnFeatures cf : ColumnFeatures.values()) {
			names = table.getColumnModel().getColumn(cf.ordinal());
			names.setHeaderValue(cf.header());
		}
	}
	private List<Object> addLine() {
		Object[][] message = new Object[table.getColumnCount()][2];
		List<Object> result = new ArrayList<Object>();
		JPanel myPanel = new JPanel();
		for (int i = 0; i < table.getColumnCount() - 1; ++i) {
			Object[] test = {table.getColumnModel().getColumn(i).getHeaderValue(), new JTextField(5)};
			message[i] = test;
		    myPanel.add(new JLabel((String) message[i][0]));
		    myPanel.add((Component) message[i][1]);
		    myPanel.add(Box.createHorizontalStrut(15));
		}
		int option = JOptionPane.showConfirmDialog(null, myPanel, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			for (int i = 0; i < table.getColumnCount() - 1; ++i) {
				result.add(ColumnFeatures.values()[i].value(((JTextField)message[i][1]).getText().trim()));
			}
		}
		return result;
	}
	
	private JPopupMenu createPopMenu() {
		JPopupMenu jPopupMenu = new JPopupMenu() {
		    public void show(Component invoker, int x, int y) {
		        int row = table.rowAtPoint(new Point(x, y));
		        if (row != -1) {
		        	table.addRowSelectionInterval(row, row);
		        }
		        super.show(invoker, x, y);
		    }
		};
		
		JMenuItem insertBefore = new JMenuItem("Insérer avant cette ligne");
		insertBefore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					List<Object> l = addLine();
					if (l.size() == 3) {
						jTableModel.insertRow(l, row);
					}
				}
			}
		});				
		jPopupMenu.add(insertBefore);
		
		JMenuItem insertAfter = new JMenuItem("Insérer après cette ligne");
		insertAfter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					List<Object> l = addLine();
					if (l.size() == 3) {
						jTableModel.insertRow(l, row + 1);
					}
				}
			}
		});
		jPopupMenu.add(insertAfter);
		
		JMenuItem insertEnd = new JMenuItem("Insérer à la fin");
		insertEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					List<Object> l = addLine();
					if (l.size() == 3) {
						jTableModel.addRow(l);
					}
				}
			}
		});
		jPopupMenu.add(insertEnd);
		
		JMenuItem delete = new JMenuItem("Supprimer cette ligne");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					jTableModel.removeRow(row);
				}
			}
		});
		jPopupMenu.add(delete);

		JMenuItem deleteAll = new JMenuItem("Supprimer toutes les lignes");
		deleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					jTableModel.clearRows();
				}
			}
		});
		jPopupMenu.add(deleteAll);
		return jPopupMenu;
	}
}
