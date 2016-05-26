package br.com.nrobot.ui;

import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.GeometricLayer;
import br.com.etyllica.layer.ImageLayer;
import br.com.etyllica.layer.StaticLayer;

public class NRButton extends GeometricLayer {
	
	private StaticLayer boardNormal;
	
	private StaticLayer boardActive;
		
	private ImageLayer board;
	
	private ImageLayer label;
	
	private boolean onMouse = false;
	
	public NRButton(int x, int y, String path) {
		super(x, y, 340, 90);
		
		boardNormal = new StaticLayer("buttons/board.png");
		
		boardActive = new StaticLayer("buttons/board_onm.png");
		
		board = new ImageLayer(x, y+15);
		board.cloneLayer(boardNormal);		
				
		label = new ImageLayer(x, y, "buttons/"+path);
		label.centralizeX(board);
		
	}
	
	public void activate() {
		board.cloneLayer(boardActive);
	}
	
	public void unactivate() {
		board.cloneLayer(boardNormal);
	}
	
	public void draw(Graphics g) {
		board.draw(g);
		label.draw(g);
		
		/*g.setAlpha(50);
		g.setColor(Color.BLACK);
		g.fillRect(this);
		g.resetOpacity();*/
	}
	
	public void handleEvent(PointerEvent event) {
		
		if(colideRectPoint(event.getX(), event.getY())) {
			activate();
			onMouse = true;
		} else {
			unactivate();
			onMouse = false;
		}
		
	}

	public boolean isOnMouse() {
		return onMouse;
	}
	
}
