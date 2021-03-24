package serie08;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class GenCellRenderer extends DefaultTreeCellRenderer {
	//CONSTANTE
	private static final long serialVersionUID = 1L;
	
	public Component  getTreeCellRendererComponent(
			JTree tree, Object value, boolean sel,
			boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Gender newVal = ((Person)((DefaultMutableTreeNode) value).getUserObject()).getGender();
		ImageIcon img = newVal.getImage();
		if (leaf) {
			setLeafIcon(img);
		} else if (expanded) {
			setOpenIcon(img);
		} else {
			setClosedIcon(img);
		}
		
		if (sel) {
			setBackground(newVal.getBorderSelectionColor());
		}
		
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	}

}
