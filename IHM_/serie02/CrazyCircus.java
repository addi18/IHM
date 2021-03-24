package serie02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import serie02.PodiumManager.Rank;

public class CrazyCircus<E extends Drawable> {

    // ATTRIBUTS

    private JFrame frame;
    private PodiumManager<E> manager;
    private Map<Order, JButton> cmds;
    private JButton restart;
    private JTextArea logs;
    private JCheckBox soCheck;
    // CONSTRUCTEURS

    public CrazyCircus(Set<E> drawables) {
        manager = new StdPodiumManager<E>(drawables);
        createView();
        placeComponents();
        createController();
    }

    // COMMANDES

    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // OUTILS

    private void createView() {
        frame = new JFrame("Crazy Circus");
        cmds = new EnumMap<Order, JButton>(Order.class);
        for (Order o : Order.values()) {
            cmds.put(o, new JButton(o.toString()));
            cmds.get(o).setName(o.name());
        }
        restart = new JButton("Nouvelle Partie");
        
        logs = new JTextArea(5, 30);
        
        soCheck = new JCheckBox("SO activé", true);
    }

    private void placeComponents() {
        Map<Rank, Podium<E>> podiums = manager.getPodiums();

        JPanel p = new JPanel(new BorderLayout()); {
            JPanel q = new JPanel(new GridLayout(0, 1)); {
                for (Order o : Order.values()) {
                    q.add(cmds.get(o));
                }
                q.add(restart);
                q.add(soCheck);
            }
            p.add(q, BorderLayout.NORTH);
        }
        frame.add(p, BorderLayout.EAST);

        p = new JPanel(new BorderLayout()); {
            JPanel q = new JPanel(new GridLayout(1, 0)); {
                for (Rank r : Rank.values()) {
                    q.add(podiums.get(r));
                    if (r == Rank.WRK_RIGHT) {
                        q.add(new JLabel(""));
                    }
                }
            }
            p.add(q, BorderLayout.CENTER);
            q = new JPanel(new GridLayout(1, 0)); {
                q.add(createLabel("Départ"));
                q.add(new JLabel(""));
                q.add(createLabel("Objectif"));
            }
            p.add(q, BorderLayout.SOUTH);
        }
        frame.add(p, BorderLayout.WEST);
        p = new JPanel(); {
        	p.add(logs);
        }
        frame.add(p, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel result = new JLabel(text);
        result.setHorizontalAlignment(SwingConstants.CENTER);
        return result;
    }

    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b = ((JButton) e.getSource());
                Order o = Order.valueOf(b.getName());
                if (o != null) {
                    try {
						manager.executeOrder(o);
					} catch (PropertyVetoException e1) {
						System.err.println("Action SO bloquée");
					}
                }
            }
        };
        for (Order o : Order.values()) {
            cmds.get(o).addActionListener(al);
        }
        
        manager.addVetoableLastOrderChangeListener(new VetoableChangeListener() {
			@Override
			public void vetoableChange(PropertyChangeEvent arg0) throws PropertyVetoException {
				Order newOrder = (Order)arg0.getNewValue();
				if (newOrder == Order.SO) {
					if (soCheck.isSelected()) {
						return;
					} else {
						throw new PropertyVetoException("Veto SO", arg0);
					}
				}
				return;
			}
		});
        
        manager.addPropertyChangeListener(StdPodiumManager.PROP_FINISHED, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				boolean isFinished = (Boolean) arg0.getNewValue();
				boolean wasFinished = (Boolean) arg0.getOldValue();
				if (wasFinished && !isFinished) {
					//Début de partie
					logs.setText("");

				} else if (!wasFinished && isFinished) {
					//Fin de partie
					logs.append("\nFin de partie !\n");;
				}
				if (!isFinished) {
					for (JButton btn : cmds.values()) {
						btn.setEnabled(true);
					}
				} else {
					for (JButton btn : cmds.values()) {
						btn.setEnabled(false);
					}
					logs.append("Gagné en " + manager.getShotsNb() + " et " + Math.round(manager.getTimeDelta() / 1000) + "s\n");
				}
			}
		});

        manager.addPropertyChangeListener(StdPodiumManager.PROP_LAST_ORDER, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				Order newOrder = (Order)arg0.getNewValue();
				logs.append(newOrder + " ");
			}
		});
        
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.reinit();
            }
        });
    }
}
