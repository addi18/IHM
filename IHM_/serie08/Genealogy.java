package serie08;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class Genealogy {

	private JFrame frame;
	private JTree tree;
//	private DefaultMutableTreeNode root;
	
	public Genealogy () {
		createModel();
		createView();
		placeComponents();
		createController();
	}
	
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void createModel() {
//		root = createNode(3, Gender.FEMALE);
	}
	
//	private DefaultMutableTreeNode createNode (int stage, Gender g) {
//		if (stage == 0) {
//			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new Person(g));
//			return node;
//		}
//		else {
//			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new Person(g), true);
//			for (int i = 0; i < 3; i++) {
//				node.add(createNode(stage - 1, i%2 == 0 ? Gender.MALE : Gender.FEMALE));
//			}
//			return node;
//		}
//	}
	
	private void createView() {
		frame = new JFrame("Genealogy");
		frame.setPreferredSize(new Dimension(500, 500));
		tree = new GenTree();
//		GenCellRenderer r = new GenCellRenderer();
//		tree.setCellRenderer(r);
//		tree.setCellEditor(new DefaultTreeCellEditor2(tree, r, new GenCellEditor()));
//		tree.setEditable(true);
	}
	
	private void placeComponents() {
		JScrollPane p = new JScrollPane(); {
			p.setViewportView(tree);
		}
		frame.add(p);
	}
	
	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				String str = "";
				Object[] path = arg0.getPath().getPath();
				for (int i = path.length - 1; i > 0; i--) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path[i];
					str += node.toString() +", " +((Person) node.getUserObject()).getGender().getDesc() + " de ";  
				}
				str += path[0].toString();
				frame.setTitle(str);
			}
		});
		
		tree.getModel().addTreeModelListener(new TreeModelListener() {
			
			@Override
			public void treeStructureChanged(TreeModelEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void treeNodesRemoved(TreeModelEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void treeNodesInserted(TreeModelEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void treeNodesChanged(TreeModelEvent arg0) {
				Object[] path = arg0.getTreePath().getPath();
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) arg0.getTreePath().getLastPathComponent();
				String str = "";
				if (arg0.getChildIndices() != null) {
					int index = arg0.getChildIndices()[0];
					DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(index);
					str = child.toString() + ", " +((Person) child.getUserObject()).getGender().getDesc() + " de ";
					for (int i = path.length - 1; i > 0; i--) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path[i];
						str += node.toString() +", " +((Person) node.getUserObject()).getGender().getDesc() + " de "; 
					}
				}

				str += path[0].toString();
				frame.setTitle(str);
			}
		});
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Genealogy().display();
			}
		});
	}
}
