package serie08;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class GenTree extends JTree {
	
	private GenCellRenderer renderer;
	private DefaultTreeCellEditor editor;
	private JPopupMenu menu;
	
	public GenTree() {
		super(new DefaultMutableTreeNode());
		renderer = new GenCellRenderer();
		setCellRenderer(renderer);
		editor = new DefaultTreeCellEditor2(this, renderer, new GenCellEditor());	
		setCellEditor(editor);
		super.setEditable(true);
		setRootVisible(false);
		createMenu();
	}
	
	private void createMenu() {
		final JMenuItem newRoot = new JMenuItem("Create root");
		final DefaultTreeModel model = (DefaultTreeModel) getModel();
		newRoot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!isRootVisible()) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(new Person(Gender.MALE), true);
					
					model.setRoot(node);
					setRootVisible(true);
					addSelectionRow(0);
				}
			}
		});
		final JMenuItem createBroBef = new JMenuItem("Create brother before");
		createBroBef.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Person(Gender.MALE), true);
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
				int index = parent.getIndex(node);
				parent.insert(newNode, index);
				model.reload();
				TreePath t = new TreePath(parent.getPath());
				setSelectionPath(t.pathByAddingChild(newNode));
			}
		});
		final JMenuItem createBroAf = new JMenuItem("Create brother after");
		createBroAf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Person(Gender.MALE), true);
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
				int index = parent.getIndex(node);
				parent.insert(newNode, index + 1);
				model.reload();
				TreePath t = new TreePath(parent.getPath());
				setSelectionPath(t.pathByAddingChild(newNode));
			}
		});
		final JMenuItem createSon = new JMenuItem("Create first son");
		createSon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Person(Gender.MALE), true);
				node.insert(newNode, 0);
				expandPath(getSelectionPath());
				setSelectionPath(getSelectionPath().pathByAddingChild(newNode));
			}
		});
		final JMenuItem deleteSel = new JMenuItem("Delete selections");
		deleteSel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreePath[] paths = getSelectionPaths();
				for (int i = paths.length - 1; i >= 0; i--) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
					node.removeFromParent();
					if (paths[i].equals(new TreePath(((DefaultMutableTreeNode) model.getRoot()).getPath()))) {
						model.setRoot(null);
						setRootVisible(false);
					}
				}
				model.reload();
				if (model.getRoot() != null) {
					setSelectionPath(new TreePath(((DefaultMutableTreeNode) model.getRoot()).getPath()));
				}
			}
		});
		
		menu = new JPopupMenu(); {
			menu.add(newRoot);
			menu.add(createBroBef);
			menu.add(createBroAf);
			menu.add(createSon);
			menu.add(deleteSel);
		}
		menu.addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				if (!isRootVisible()) {
					newRoot.setEnabled(true);
					createBroBef.setEnabled(false);
					createBroAf.setEnabled(false);
					createSon.setEnabled(false);
					deleteSel.setEnabled(false);
				} else {
					newRoot.setEnabled(false);
					deleteSel.setEnabled(true);
					createBroBef.setEnabled(true);
					createBroAf.setEnabled(true);
					createSon.setEnabled(true);

					if (getSelectionPath().equals(new TreePath(((DefaultMutableTreeNode) model.getRoot()).getPath()))) {
						createBroBef.setEnabled(false);
						createBroAf.setEnabled(false);
					} else if (!((DefaultMutableTreeNode) getSelectionPath().getLastPathComponent()).isLeaf()) {
						createSon.setEnabled(false);
					}
				}
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {				
			}
		});
		setComponentPopupMenu(menu);
	}

}
