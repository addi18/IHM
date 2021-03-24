package serie09;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GridBagDistrib extends Distrib {
	
	GridBagDistrib(String title) {
		super(title);
	}


	@Override
	protected void placeComponents() {
		JPanel p = new JPanel(new GridBagLayout()); {
			p.add(LKey.BACK.jlabel(), new GBC(0, 0, 7, 1).insets(15, 5, 0, 5));
			p.add(LKey.CREDIT.jlabel(), new GBC(0, 1, 7, 1).insets(5, 5, 0, 5));
			p.add(BKey.ORANGE.jbutton(), new GBC(0, 2, 2, 2).fill(GBC.HORIZONTAL).insets(0, 5, 0, 5));
			p.add(PKey.ORANGE.jlabel(), new GBC(2, 2, 1, 2).fill(GBC.BOTH));
			p.add(BKey.CHOCO.jbutton(), new GBC(0, 4, 2, 2).fill(GBC.HORIZONTAL).insets(0, 5, 0, 5));
			p.add(PKey.CHOCO.jlabel(), new GBC(2, 4, 1, 2).fill(GBC.BOTH));
			p.add(BKey.COFFEE.jbutton(), new GBC(0, 6, 2, 2).fill(GBC.HORIZONTAL).insets(0, 5, 0, 5));
			p.add(PKey.COFFEE.jlabel(), new GBC(2, 6, 1, 2).fill(GBC.BOTH).weight(1,  1));
			p.add(new JLabel(" "), new GBC(4, 2, 1, 1));
			p.add(BKey.INS.jbutton(), new GBC(3, 3, 2, 2).fill(GBC.HORIZONTAL).insets(0, 5, 5, 5));
			p.add(FKey.INS.jtextfield(), new GBC(5, 3, 1, 2).fill(GBC.HORIZONTAL).weight(1,  1));
			p.add(PKey.INS.jlabel(), new GBC(6, 3, 1, 2).fill(GBC.BOTH).insets(2, 2, 2, 10));
			p.add(BKey.CANCEL.jbutton(), new GBC(3, 5, 2, 2).fill(GBC.HORIZONTAL).insets(0, 5, 5, 5));
			p.add(LKey.DRINK.jlabel(), new GBC(0, 8, 1, 1).insets(5));
			p.add(FKey.DRINK.jtextfield(), new GBC(1, 8, 2, 1).fill(GBC.HORIZONTAL));
			p.add(LKey.MONEY.jlabel(), new GBC(3, 8, 1, 1).insets(5));
			p.add(FKey.MONEY.jtextfield(), new GBC(4, 8, 2, 1).fill(GBC.HORIZONTAL));
			p.add(PKey.MONEY.jlabel(), new GBC(6, 8, 1, 1).insets(2, 2, 2, 10));
			p.add(BKey.TAKE.jbutton(), new GBC(0, 9, 7, 1).insets(5, 5, 5, 15));
		}
		getFrame().add(p, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GridBagDistrib("GridBagDistrib de boissons").display();
			}
		});
	}

}
