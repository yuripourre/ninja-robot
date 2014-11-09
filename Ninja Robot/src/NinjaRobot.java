import br.com.etyllica.Etyllica;
import br.com.etyllica.context.Application;
import br.com.etyllica.util.PathHelper;
import br.com.nrobot.MainMenu;


public class NinjaRobot extends Etyllica {

	private static final long serialVersionUID = 787422233224705125L;

	public NinjaRobot() {
		super(800, 600);
	}

	@Override
	public Application startApplication() {
		
		String s = PathHelper.currentDirectory();
		setPath(s+"../");
		
		return new MainMenu(w, h);
	}

}
