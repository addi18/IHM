package serie08;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class Genealogy {
	//CONSTANTES
	private static final String NAME = "Toto";
	private static final int RANK = 3;
	
	//ATTRIBUTS
	private GenTree tree;
	private JFrame frame;
	private DefaultMutableTreeNode root;
	
	//CONSTRUCTEUR
	public Genealogy() {
		createView();
        placeComponents();
        createController();
	}
	//REQUETES
	//COMMANDES
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	//OUTILS
	private void createView() {
		tree = new GenTree();
		tree.setEditable(true);
		tree.setCellRenderer(new GenCellRenderer());
		tree.setCellEditor(new DefaultTreeCellEditor2(tree, new GenCellRenderer(), new GenCellEditor()));
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(500, 500));
	}

	private void placeComponents() {
		frame.add(new JScrollPane(tree));
	}

	private void createController() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				frame.setTitle(title(e.getPath()));
			}
		});
		
		tree.getModel().addTreeModelListener(new TreeModelListener() {
			public void treeStructureChanged(TreeModelEvent e) {
			}

			public void treeNodesRemoved(TreeModelEvent e) {
			}

			public void treeNodesInserted(TreeModelEvent e) {
			}
			
			public void treeNodesChanged(TreeModelEvent e) {
				frame.setTitle(title(tree.getSelectionPath()));
			}
		});
	}
	
	private String title(TreePath path) {
		String  s = ((DefaultMutableTreeNode) path.getPathComponent(path.getPathCount() - 1)).getUserObject().toString();
		for (int i = path.getPathCount() - 1; i > 0; --i) {
			s += ", " +((Person) ((DefaultMutableTreeNode) path.getPathComponent(i)).getUserObject()).getGender().getDesc() + " " + ((Person) ((DefaultMutableTreeNode) path.getPathComponent(i - 1)).getUserObject()).getName();
		}
		return s;
	}
	
	//POINT D'ENTREE
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Genealogy().display();
	        }
	    });
	}
}
