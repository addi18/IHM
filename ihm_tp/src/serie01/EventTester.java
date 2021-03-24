package serie01;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class EventTester {

	private JFrame mainFrame;
	private JFrame testFrame;
	private JButton btnNewTestFrame;
	private JButton btnRAZ;
	private Map<Integer, JTextArea> zonesTexte;
	private int numEvent;
	private int numRAZ;

	public EventTester() {
		createModel();
		createView();
		placeComponents();
		createController();
	}

	public void display() {
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	private void createModel() {

	}

	private void createView() {
		mainFrame = new JFrame("Tests sur les évènements - Zone d'Affichage");		
		testFrame = null;
		btnNewTestFrame = new JButton("Nouvelle fenêtre");
		btnRAZ = new JButton("RAZ Compteur");
		zonesTexte = new HashMap<Integer, JTextArea>();
		for (int i = 0; i < 7; i++) {
			JTextArea jta = new JTextArea(12, 20);
			jta.setEditable(false);
			zonesTexte.put(i, jta);
		}
	}

	private void placeComponents() {
		JPanel p = new JPanel(); {
			p.add(btnNewTestFrame);
			p.add(btnRAZ);
		}
		mainFrame.add(p, BorderLayout.NORTH);
		p = new JPanel(new GridLayout(0,3)); {
			List<String> noms = new ArrayList<String>();
			noms.add("MouseListener");
			noms.add("MouseWheelListener");
			noms.add("MouseMotionListener");
			noms.add("WindowListener");
			noms.add("WindowFocusListener");
			noms.add("WindowStateListener");
			noms.add("KeyListener");
			for (int i = 0; i < 7; i++) {
				JScrollPane q = new JScrollPane(); {
					q.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), noms.get(i)));
					q.setViewportView(zonesTexte.get(i));
				}
				p.add(q);				
			}
		}
		mainFrame.add(p, BorderLayout.CENTER);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		btnNewTestFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (testFrame != null) {
					int input = JOptionPane.showConfirmDialog(
							null,
							"Supprimer la fenêtre actuelle ?",
							"Confirmation de suppression",
							JOptionPane.YES_NO_OPTION);
					if (input == 0) {
						createNewTestFrame();
					}
				} else {
					createNewTestFrame();
				}
			}
		});
		btnRAZ.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				numEvent = 0;
				numRAZ ++;
				for(JTextArea jta : zonesTexte.values()) {
					jta.append("--- RAZ " + numRAZ + " ---\n");
				}
			}
		});

	}

	/*
	private void refresh() {

	}
	 */
	private void createNewTestFrame() {
		if (testFrame != null) {
			testFrame.dispose();
		}
		for (JTextArea jta : zonesTexte.values()) {
			jta.setText("");
		}
		numEvent = 0;
		numRAZ = 0;
		testFrame = new JFrame("Zone de test");
		testFrame.setPreferredSize(new Dimension(200, 100));
		testFrame.pack();
		testFrame.setLocationRelativeTo(null);
		testFrame.setVisible(true);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addListeners();
	}

	private void addListeners() {
		testFrame.addWindowListener(new WindowListener() {
			private final int index = 3;
			@Override
			public void windowOpened(WindowEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				printEvent(arg0, index);
			}
		});
		testFrame.addWindowFocusListener(new WindowFocusListener() {
			private final int index = 4;
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				printEvent(arg0, index);		
			}

			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				printEvent(arg0, index);
			}
		});
		testFrame.addWindowStateListener(new WindowStateListener() {
			private final int index = 5;
			@Override
			public void windowStateChanged(WindowEvent arg0) {
				printEvent(arg0, index);
			}
		});
		testFrame.addMouseListener(new MouseListener() {
			private int index = 0;
			@Override
			public void mouseReleased(MouseEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				printEvent(arg0, index);
			}
		});
		testFrame.addMouseMotionListener(new MouseMotionListener() {
			private final int index = 2;
			@Override
			public void mouseMoved(MouseEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				printEvent(arg0, index);
			}
		});
		testFrame.addMouseWheelListener(new MouseWheelListener() {
			private final int index = 1;
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				printEvent(arg0, index);
			}
		});
		testFrame.addKeyListener(new KeyListener() {
			private final int index = 6;
			@Override
			public void keyTyped(KeyEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				printEvent(arg0, index);
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				printEvent(arg0, index);			
			}
		});
	}

	private void printEvent(AWTEvent arg0, int index) {
		numEvent ++;
		String str = arg0.paramString().split(",")[0];
		System.out.println(str);
		zonesTexte.get(index).append(numEvent + " " + str + "\n");		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new EventTester().display();
			}
		});
	}
}
