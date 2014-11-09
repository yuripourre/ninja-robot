package br.com.nrobot;

import java.awt.Color;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.graphics.Graphic;

public class MainMenu extends Application {

	public MainMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void draw(Graphic g) {
		g.setColor(Color.BLUE);
		g.fillRect(this);		
	}

	@Override
	public void load() {
		loading = 100;
	}

}
