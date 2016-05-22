package br.com.nrobot.ui;

import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.etyllica.layer.StaticLayer;

public class SelectionButton {

	private int x, y;
	
	private StaticLayer slot;
	private StaticLayer slotActive;
	
	private ImageLayer label;
	private ImageLayer layer;
	
	public SelectionButton(int x, int y, String path) {
		super();
		this.x = x;
		this.y = y;
		
		slot = new StaticLayer("ui/slot.png");
		slotActive = new StaticLayer("ui/slot_active.png");
		
		layer = new ImageLayer(x, y);
		layer.cloneLayer(slot);
		
		label = new ImageLayer(x, y, 64, 64, path);
		label.centralize(x, y, slot.getW(), slot.getH());
	}
	
	public void draw(Graphic g) {
		layer.simpleDraw(g, x, y);
		label.draw(g);
	}
	
	public boolean updateMouse(PointerEvent event) {
		if(layer.onMouse(event)) {
			active();
			if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
				return true;
			}
		} else {
			unactive();
		}
		return false;
	}
	
	public void active() {
		layer.cloneLayer(slotActive);
	}
	
	public void unactive() {
		layer.cloneLayer(slot);
	}
	
}
