import br.com.etyllica.EtyllicaFrame;
import br.com.etyllica.core.context.Application;
import br.com.nrobot.MainMenu;
import br.com.nrobot.config.ConfigLoader;


public class NinjaRobot extends EtyllicaFrame {

	private static final long serialVersionUID = 787422233224705125L;

	public NinjaRobot() {
		super(800, 600);
	}

	public static void main(String[] args){
		NinjaRobot game = new NinjaRobot();
		game.init();
	}
	
	@Override
	public Application startApplication() {
		initialSetup(ConfigLoader.PATH_PREFIX);
		
		return new MainMenu(w, h);
	}

}
