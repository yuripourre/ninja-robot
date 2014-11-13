package br.com.nrobot.fx;

import br.com.etyllica.effects.Effect;
import br.com.etyllica.layer.GeometricLayer;

public class Explosion extends Effect {

	public Explosion() {
		super(0, 0, 96, 96, "explosion.png");
		
		setSpeed(100);
		setFrames(8);		
	}

	public void explode(GeometricLayer layer) {
		
		int centerX = layer.getX()+layer.utilWidth()/2-utilWidth()/2;
		int centerY = layer.getY()+layer.utilHeight()/2;
		
		setCoordinates(centerX, centerY);
		startEffect();
	}	
	
}
