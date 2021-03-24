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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;

public class EventsTester {
	//ATTRIBUTS
	private JFrame mainFrame;
	private JFrame testFrame;
	private JButton btnNewTestFrame;
	private JButton razCompteur;
	private Map<String, Integer> data;
	private Map<String, JTextArea> eventTextArea;
	private int counter;
	private int lastNum;

	//CONSTRUCTEUR
	public EventsTester() {
		createView();
		placeComponents();
		createController();
	}
	
	//COMMANDE
	public void display() {
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	//OUTILS
	private void placeComponents() {
		JPanel p = new JPanel(); {
			p.add(btnNewTestFrame);
			p.add(razCompteur);
		}
		mainFrame.add(p, BorderLayout.NORTH);
		p = new JPanel(new GridLayout(3, 3)); {
			String[] allEvents = getAllEvents();
			for (String key : allEvents) {
				JTextArea text = new JTextArea();
				text.setEditable(false);
				eventTextArea.put(key, text);
				JScrollPane q = new JScrollPane(text); {
					q.setBorder(BorderFactory.createTitledBorder(
							new LineBorder(Color.black), key));
				}
				p.add(q);
			}
		}
		p.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(7, 7, 7, 7), 
				BorderFactory.createLineBorder(null)));
		mainFrame.add(p, BorderLayout.CENTER);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnNewTestFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (testFrame == null) {
					try {
						createNewTestFrame();
					} catch (BadLocationException e1) {
						messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
					}
				} else {
					int reponse = JOptionPane.showConfirmDialog(mainFrame,
							"Vous voulez detruire la fenêtre de test en cours "
							+ "d'utilisation?",
							"Confirmation",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (reponse == JOptionPane.YES_OPTION) {
						testFrame.dispose();
						Set<String> keys = eventTextArea.keySet();
						for (String val : keys) {
							eventTextArea.get(val).setText(null);
						}
						try {
							createNewTestFrame();
						} catch (BadLocationException e1) {
							messageWarning("Attention : BAD LOCATION "
									+ "EXCEPTION!!!");
						}
						lastNum = 0;
						counter = 0;
					}
				}
			}
		});
		razCompteur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				++counter;
				Set<String> keys = eventTextArea.keySet();
				for (String val : keys) {
					JTextArea jta = eventTextArea.get(val);
					jta.append("---  RAZ" + counter + "  ---\n");
				}
				lastNum = 0;
			}
		});
	}

	private void createView() {
		final int mainFrameWidth = 900;
		final int mainFrameHeight = 800;
		
		mainFrame = new JFrame("Tests sur les événements - Zone d'AFFICHAGE");
		mainFrame.setPreferredSize(new Dimension(
				mainFrameWidth, mainFrameHeight));

		btnNewTestFrame = new JButton("Nouvelle Fenetre");
		razCompteur = new JButton("RAZ Compteur");
		data = new HashMap<String, Integer>();
		eventTextArea = new HashMap<String, JTextArea>();
	    counter = 0;
	    lastNum = 0;
	}
	
	private void createNewTestFrame() throws BadLocationException {
		final int testFrameWidth = 200;
		final int testFrameHeight = 100;
		
		testFrame = new JFrame("Zone de test");
		testFrame.setPreferredSize(new Dimension(testFrameWidth, 
				testFrameHeight));
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.pack();
		testFrame.setLocationRelativeTo(null);
		testFrame.setVisible(true);
		
		testFrame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void windowIconified(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(
						testFrame, 
						"Vous allez fermer cette fenêtre de test!",
						"Inforrmation",
						JOptionPane.INFORMATION_MESSAGE
					);
				testFrame.dispose();
				testFrame = null;
			}
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void windowActivated(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
		});
		testFrame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
		});
		testFrame.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
		});
		testFrame.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
		});
		testFrame.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
		});
		testFrame.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
		});
		testFrame.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				try {
					eventHandle(e);
				} catch (BadLocationException e1) {
					messageWarning("Attention : BAD LOCATION EXCEPTION!!!");
				}
			}
		});
	}
	private void eventHandle(AWTEvent e) throws BadLocationException {
		String eventName = e.paramString();
        eventName = eventName.substring(0, eventName.indexOf(","));
        ++lastNum;
		if (data.containsKey(eventName)) {
			data.put(eventName, lastNum);
		} else {
			data.put(eventName, lastNum);
		}
		String event = e.toString();
		event = event.substring("java.awt.event.".length(), event.indexOf("["));
		event = event.replaceAll("Event", "Listener");
		if (eventName.contains("FOCUS")) {
			event = event.replaceAll("Window", "WindowFocus");
		}
		if (eventName.contains("MOVED") || eventName.contains("DRAGGED")) {
			event = event.replaceAll("Mouse", "MouseMotion");
		}
		if (eventName.contains("STATE")) {
			event = event.replaceAll("Window", "WindowState");
		}
		eventTextArea.get(event).append(data.get(eventName) + " " 
				+ eventName + "\n");
	}
	private void messageWarning(String string) {
		JOptionPane.showMessageDialog(
			testFrame, 
			string,
			"Erreur !",
			JOptionPane.WARNING_MESSAGE
		);
	}
	private String[] getAllEvents() {
		return  new String[]{"MouseListener", "WindowFocusListener", 
				"WindowListener", "KeyListener", "WindowStateListener",
				 "MouseWheelListener", "MouseMotionListener"};
	}
	
	//POINT D'ENTREE
	public static void main(String[] arg) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new EventsTester().display();
			}
		});
	}
}
