package serie08;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class GenTree extends JTree {
	//CONSTANTE
	private static final long serialVersionUID = 1L;
	
	//ATTRIBUT
	private DefaultMutableTreeNode root;
	
	//CONSTRUCTEUR
	public GenTree() {
		super();
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		model.setRoot(null);
		setEditable(true);
		setCellRenderer(new GenCellRenderer());
		setCellEditor(new DefaultTreeCellEditor2(this, new GenCellRenderer(), new GenCellEditor()));
		root = null;
		setComponentPopupMenu(createPopupMenu());
		createController();
	}
	
	//OUTILS
	private JPopupMenu createPopupMenu() {
		final GenTree self = this;
		@SuppressWarnings("serial")
		JPopupMenu jPopupMenu = new JPopupMenu() {
		    public void show(Component invoker, int x, int y) {
		        super.show(invoker, x, y);
		    }
		};
		
		JMenuItem createRoot = new JMenuItem("create root");
		createRoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (root == null) {
					root = new DefaultMutableTreeNode(new Person(Gender.MALE));
					DefaultTreeModel model = (DefaultTreeModel) self.getModel();
					model.setRoot(root);
					self.addSelectionPath(new TreePath(root));
				}
			}
		});				
		jPopupMenu.add(createRoot);
		
		JMenuItem createBrotherBefore = new JMenuItem("create Brother Before");
		createBrotherBefore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((DefaultMutableTreeNode)self.getLastSelectedPathComponent()).getParent();
				if (node == null) {
					return;
				}
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Person(Gender.MALE));
				DefaultTreeModel model = (DefaultTreeModel) self.getModel();
				model.insertNodeInto(newNode, node, 1);
				//node.insert(newNode, 0);
				self.removeSelectionPath(new TreePath(node));
				self.addSelectionPath(new TreePath(newNode));
			}
		});				
		jPopupMenu.add(createBrotherBefore);
		
		JMenuItem createBrotherAfter = new JMenuItem("create Brother After");
		createBrotherAfter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((DefaultMutableTreeNode)self.getLastSelectedPathComponent()).getParent();
				if (node == null) {
					return;
				}
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Person(Gender.MALE));
				node.add(newNode);
				self.removeSelectionPath(new TreePath(node));
				self.addSelectionPath(new TreePath(newNode));
			}
		});				
		jPopupMenu.add(createBrotherAfter);
		
		JMenuItem createFirstSon = new JMenuItem("create first son");
		createFirstSon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)self.getLastSelectedPathComponent();
				if (node == null) {
					return;
				}
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Person(Gender.FEMALE));
				//node.add(newNode);
				DefaultTreeModel model = (DefaultTreeModel) self.getModel();
				model.insertNodeInto(newNode, node, 0);
				setSelectionPath(new TreePath(newNode));
				//((DefaultTreeModel) self.getModel()).reload();
				//TreePath path = new TreePath(newNode);
				//self.setSelectionPath(path);
			}
		});				
		jPopupMenu.add(createFirstSon);
		
		JMenuItem deleteSelection = new JMenuItem("delete Selection");
		deleteSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node;
				DefaultTreeModel model = (DefaultTreeModel) (self.getModel());
				TreePath[] paths = self.getSelectionPaths();
				if (paths != null) {
					for (int i = 0; i < paths.length; i++) {
						node = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
						if (model.getRoot() != node) {
							model.removeNodeFromParent(node);
						}
					}
				}
			}
		});				
		jPopupMenu.add(deleteSelection);
		
		return jPopupMenu;
	}
	
	private void createController() {
		getModel().addTreeModelListener(new TreeModelListener() {
			public void treeStructureChanged(TreeModelEvent e) {
				System.out.println("Bonjour");
			}
			
			@Override
			public void treeNodesRemoved(TreeModelEvent e) {
				System.out.println("Bonjour");
			}
			
			@Override
			public void treeNodesInserted(TreeModelEvent e) {
				TreePath path = new TreePath(((DefaultMutableTreeNode) e.getChildren()[0]));
				System.out.println(path.toString());
				expandPath(e.getTreePath());
				addSelectionPath(new TreePath((DefaultMutableTreeNode)e.getChildren()[0]));
				//setSelectionPath(e.getTreePath());
				//setSelectionPath(new TreePath((DefaultMutableTreeNode)e.getChildren()[0]));
				System.out.println(getExpandsSelectedPaths());
			}
			
			@Override
			public void treeNodesChanged(TreeModelEvent e) {
				System.out.println("Bonjour");
			}
		});
	}

}
