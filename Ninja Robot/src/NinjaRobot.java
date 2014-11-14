import br.com.etyllica.EtyllicaFrame;
import br.com.etyllica.context.Application;
import br.com.etyllica.util.PathHelper;
import br.com.nrobot.MainMenu;


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
		
		String s = PathHelper.currentDirectory();
		setPath(s+"../");
				
		return new MainMenu(w, h);
	}

}
