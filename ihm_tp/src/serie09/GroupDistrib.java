package serie09;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GroupDistrib extends Distrib {

	GroupDistrib(String title) {
		super(title);
	}

	@Override
	protected void placeComponents() {
		JPanel p = new JPanel(null);
		GroupLayout lmp = new GroupLayout(p);
		lmp.setHorizontalGroup(
				lmp.createParallelGroup(GroupLayout.Alignment.CENTER, true)
					.addComponent(LKey.BACK.jlabel())
					.addComponent(LKey.CREDIT.jlabel())
					.addGroup(
							lmp.createSequentialGroup()
								
								.addGroup(
										lmp.createParallelGroup(Alignment.LEADING, true)
											.addGroup(
													lmp.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																lmp.createParallelGroup(Alignment.LEADING, false)
																	.addComponent(BKey.ORANGE.jbutton(), 0, BKey.ORANGE.jbutton().getPreferredSize().width, BKey.ORANGE.jbutton().getMaximumSize().width)
																	.addComponent(BKey.CHOCO.jbutton(), 0, BKey.ORANGE.jbutton().getPreferredSize().width, BKey.ORANGE.jbutton().getMaximumSize().width)
																	.addComponent(BKey.COFFEE.jbutton(), 0, BKey.ORANGE.jbutton().getPreferredSize().width, BKey.ORANGE.jbutton().getMaximumSize().width)
																)
														.addGap(5)
														.addGroup(
																lmp.createParallelGroup(Alignment.LEADING, true)
																.addComponent(PKey.ORANGE.jlabel())
																.addComponent(PKey.CHOCO.jlabel())
																.addComponent(PKey.COFFEE.jlabel())
																)
														.addGap(10)
													)
											.addGroup(
													lmp.createSequentialGroup()
														.addContainerGap()
														.addComponent(LKey.DRINK.jlabel())
														.addGap(5)
														.addComponent(FKey.DRINK.jtextfield())
													)
										)
								.addGroup(
										lmp.createParallelGroup(Alignment.LEADING, true)
											.addGroup(
													lmp.createSequentialGroup()
														.addGroup(
																lmp.createParallelGroup(Alignment.LEADING, false)
																	.addComponent(BKey.INS.jbutton(), 0, BKey.CANCEL.jbutton().getPreferredSize().width, BKey.CANCEL.jbutton().getMaximumSize().width)
																	.addComponent(BKey.CANCEL.jbutton(), 0, BKey.CANCEL.jbutton().getPreferredSize().width, BKey.CANCEL.jbutton().getMaximumSize().width)
																)
														.addGap(5)
														.addComponent(FKey.INS.jtextfield())
														.addGap(5)
														.addComponent(PKey.INS.jlabel())
														
													)
											
											.addGroup(
													lmp.createSequentialGroup()
														.addGap(5)
														.addComponent(LKey.MONEY.jlabel())
														.addGap(5)
														.addComponent(FKey.MONEY.jtextfield())
														.addGap(5)
														.addComponent(PKey.MONEY.jlabel())
													)
										)
											.addContainerGap()
							)
					.addComponent(BKey.TAKE.jbutton())
				);
		lmp.setVerticalGroup(
				lmp.createSequentialGroup()
					.addContainerGap()
					.addComponent(LKey.BACK.jlabel())
					.addGap(5)
					.addComponent(LKey.CREDIT.jlabel())
					.addGap(5)
					.addGroup(
							lmp.createParallelGroup(Alignment.LEADING, false)
								.addGroup(
										lmp.createSequentialGroup()
											.addGroup(
													lmp.createParallelGroup(Alignment.BASELINE, false)
														.addComponent(BKey.ORANGE.jbutton())
														.addComponent(PKey.ORANGE.jlabel())
													)
											.addGap(5)
											.addGroup(
													lmp.createParallelGroup(Alignment.BASELINE, false)
														.addComponent(BKey.CHOCO.jbutton())
														.addComponent(PKey.CHOCO.jlabel())
													)
											.addGap(5)
											.addGroup(
													lmp.createParallelGroup(Alignment.BASELINE, false)
														.addComponent(BKey.COFFEE.jbutton())
														.addComponent(PKey.COFFEE.jlabel())
													)
											.addGap(5)
										)
								.addGroup(
										lmp.createSequentialGroup()
											.addGap(15)
											.addGroup(
													lmp.createParallelGroup(Alignment.BASELINE, false)
														.addComponent(BKey.INS.jbutton())
														.addComponent(FKey.INS.jtextfield())
														.addComponent(PKey.INS.jlabel())
													)
											.addGap(5)
											.addComponent(BKey.CANCEL.jbutton())
										)
							)
					.addGroup(
							lmp.createParallelGroup(Alignment.LEADING, true)
								.addGroup(
										lmp.createParallelGroup(Alignment.BASELINE, false)
											.addComponent(LKey.DRINK.jlabel())
											.addComponent(FKey.DRINK.jtextfield())
										)
								.addGroup(
										lmp.createParallelGroup(Alignment.BASELINE, false)
											.addComponent(LKey.MONEY.jlabel())
											.addComponent(FKey.MONEY.jtextfield())
											.addComponent(PKey.MONEY.jlabel())
										)
							)
					.addGap(5)
					.addComponent(BKey.TAKE.jbutton())
					.addContainerGap()
				);
		p.setLayout(lmp);
		getFrame().add(p, BorderLayout.NORTH);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GroupDistrib("GroupDistrib de boissons").display();
			}
		});
	}
	
}
