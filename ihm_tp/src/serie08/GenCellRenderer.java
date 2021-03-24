package serie08;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class GenCellRenderer extends DefaultTreeCellRenderer{
	public static final String FONT_NAME = "Verdana";
	public static final int FONT_SIZE = 14;
	
	public GenCellRenderer() {
		setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject() != null) {
			Gender gender = ((Person) node.getUserObject()).getGender();
			if (leaf) {
				setLeafIcon(gender.getImage());
			} else if (expanded) {
				setOpenIcon(gender.getImage());
			} else {
				setClosedIcon(gender.getImage());
			}
			if (hasFocus) {
				setBackgroundSelectionColor(gender.getBackgroundSelectionColor());
				setBorderSelectionColor(gender.getBorderSelectionColor());
			}
		}
		return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	}
}
