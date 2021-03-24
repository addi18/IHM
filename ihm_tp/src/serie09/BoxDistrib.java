package serie09;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class BoxDistrib extends Distrib {
	
	JButton orange;
	JButton chocolate;
	JButton coffee;
	JButton insert;
	JButton cancel;
	JTextField fInsert;
	JTextField fDrink;
	JTextField fMoney;
	
	BoxDistrib(String title) {
		super(title);
	}
	
	@Override
	protected void createView(String title) {
		super.createView(title);
		orange = BKey.ORANGE.jbutton();
		chocolate = BKey.CHOCO.jbutton();
		chocolate.setPreferredSize(new Dimension(orange.getMaximumSize().width,chocolate.getPreferredSize().height));
		coffee = BKey.COFFEE.jbutton();
		coffee.setPreferredSize(new Dimension(orange.getMaximumSize().width,coffee.getPreferredSize().height));

		
		insert = BKey.INS.jbutton();
		cancel = BKey.CANCEL.jbutton();
		int width = Math.max(insert.getMaximumSize().width, cancel.getMaximumSize().width);
		insert.setPreferredSize(new Dimension(width, insert.getMaximumSize().height));
		cancel.setPreferredSize(new Dimension(width, cancel.getMaximumSize().height));

		fInsert = FKey.INS.jtextfield();
		fInsert.setMaximumSize(new Dimension(fInsert.getMaximumSize().width, fInsert.getPreferredSize().height));
		fDrink = FKey.DRINK.jtextfield();
		fDrink.setMaximumSize(new Dimension(fDrink.getMaximumSize().width, fDrink.getPreferredSize().height));
		fMoney = FKey.MONEY.jtextfield();
		fMoney.setMaximumSize(new Dimension(fMoney.getMaximumSize().width, fMoney.getPreferredSize().height));
		

	}

	@Override
	protected void placeComponents() {
		JPanel p = new JPanel(null);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); {
			p.add(Box.createVerticalStrut(10));
			p.add(addComponentCenter(LKey.BACK.jlabel()));
			p.add(Box.createVerticalStrut(5));
			p.add(addComponentCenter(LKey.CREDIT.jlabel()));
			p.add(Box.createVerticalStrut(3));
			JPanel q = new JPanel(null);
			q.setLayout(new BoxLayout(q, BoxLayout.X_AXIS)); {
				q.add(Box.createHorizontalStrut(10));
				JPanel r = new JPanel(null);
				r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS)); {
					JPanel	s = new JPanel(null);
					s.setLayout(new BoxLayout(s, BoxLayout.X_AXIS)); {
						s.add(orange);
						s.add(Box.createHorizontalStrut(10));
						s.add(addLabel(PKey.ORANGE.jlabel()));
					}
					r.add(s);
					r.add(Box.createRigidArea(new Dimension(0,5)));
					s = new JPanel(null);
					s.setLayout(new BoxLayout(s, BoxLayout.X_AXIS)); {
						s.add(chocolate);
						s.add(Box.createHorizontalStrut(10));
						s.add(addLabel(PKey.CHOCO.jlabel()));
						
					}
					r.add(s);
					r.add(Box.createRigidArea(new Dimension(0,5)));
					s = new JPanel(null);
					s.setLayout(new BoxLayout(s, BoxLayout.X_AXIS)); {
						s.add(coffee);
						s.add(Box.createHorizontalStrut(10));
						s.add(addLabel(PKey.COFFEE.jlabel()));
						
					}
					r.add(s);
					r.add(Box.createRigidArea(new Dimension(0,5)));
					
					s = new JPanel(null);
					s.setLayout(new BoxLayout(s, BoxLayout.X_AXIS)); {
						s.add(LKey.DRINK.jlabel());
						s.add(Box.createHorizontalStrut(10));
						s.add(fDrink);
					}
					r.add(s); 
				}
				q.add(r);
				
				q.add(Box.createHorizontalStrut(10));
				
				r = new JPanel(null);
				r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS)); {
					//r.add(Box.createGlue());
					r.add(Box.createVerticalStrut(15));
					JPanel s = new JPanel(null);
					s.setLayout(new BoxLayout(s, BoxLayout.X_AXIS)); {
						s.add(insert);
						s.add(Box.createHorizontalStrut(5));
						s.add(fInsert);
						s.add(Box.createHorizontalStrut(10));
						s.add(PKey.INS.jlabel());
					}
					r.add(s);
					s = new JPanel(null);
					s.setLayout(new BoxLayout(s, BoxLayout.X_AXIS)); {
						s.add(cancel);
						r.add(Box.createVerticalStrut(5));
						s.add(Box.createGlue());
					}
					r.add(s);
					//r.add(Box.createGlue());
					r.add(Box.createVerticalStrut(20));
					s = new JPanel(null);
					s.setLayout(new BoxLayout(s, BoxLayout.X_AXIS)); {
						s.add(Box.createHorizontalStrut(5));
						s.add(LKey.MONEY.jlabel());
						s.add(Box.createHorizontalStrut(10));
						s.add(fMoney);
						s.add(Box.createHorizontalStrut(10));
						s.add(PKey.MONEY.jlabel());
					}
					r.add(s);
				}
				q.add(r);
				q.add(Box.createHorizontalStrut(10));
			}
			p.add(q);
			p.add(Box.createVerticalStrut(10));
			p.add(addComponentCenter(BKey.TAKE.jbutton()));
			p.add(Box.createVerticalStrut(10));
		}
		getFrame().add(p, BorderLayout.NORTH);
	}
	
	private JComponent addComponentCenter(JComponent l) {
	    l.setAlignmentX(0.5f);
	    l.setAlignmentY(0.5f);
	    return l;
	}
	
	private JLabel addLabel(JLabel l) {
	    l.setMaximumSize(new Dimension(Integer.MAX_VALUE, l.getMaximumSize().height));;
		l.setHorizontalAlignment(SwingConstants.LEFT);
	    return l;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BoxDistrib("BoxDistrib de boissons").display();
			}
		});
	}

}
