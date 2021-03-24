package serie03;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MobileComponent extends JComponent implements AnimableComponent {
	
	private Mobile model;
	private Point oldPoint;

	public MobileComponent(int width, int height, int ray) {
		assert width > 0 && height > 0;
		assert ray > 0 && ray <= Math.min(width, height) / 2;
		
		setPreferredSize(new Dimension(width, height));
		final Rectangle sr = new Rectangle(width, height);
		final Rectangle mr = new Rectangle(2*ray, 2*ray);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(!model.isMovable()) {
					model.setHorizontalShift(arg0.getPoint().x - oldPoint.x);
					model.setVerticalShift(arg0.getPoint().y - oldPoint.y);
					model.setMovable(true);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(mr.contains(arg0.getPoint())) {
					model.setMovable(false);
					oldPoint = arg0.getPoint();
					if(model.isValidCenterPosition(oldPoint)) {
						model.setCenter(oldPoint);
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {				
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if(!model.isMovable()) {
					oldPoint = model.getCenter();
					if(model.isValidCenterPosition(arg0.getPoint()) ) {
						model.setCenter(arg0.getPoint());
					}
				}
			}
		});
		
		model = new StdMobile(sr, mr);
		
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				repaint();
			}
		});
	}

	@Override
	public void animate() {
		if (model.isMovable()) {
			model.move();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.setColor(STAT_COLOR);
		g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
		g.setColor(MOV_COLOR);
		int x = model.getCenter().x;
		int y = model.getCenter().y;
		int ray = model.getMovingRect().height;
		g.fillOval(x, y, ray, ray);
		Toolkit.getDefaultToolkit();
	}

	@Override
	public Point getDiscCenter() {
		return model.getCenter();
	}

	@Override
	public int getHorizontalShift() {
		return model.getHorizontalShift();
	}

	@Override
	public Mobile getModel() {
		return model;
	}

	@Override
	public int getVerticalShift() {
		return model.getVerticalShift();
	}

	@Override
	public boolean isDiscCaught() {
		return model.isMovable();
	}

	@Override
	public void setDiscCenter(Point p) {
		model.setCenter(p);
	}

	@Override
	public void setDiscShift(int dx, int dy) {
		model.setHorizontalShift(dx);
		model.setHorizontalShift(dy);
	}

}
