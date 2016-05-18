package br.com.nrobot.network.client;

public class NinjaRobotClient implements ActionClientListener {

	public static final int PORT = 9967;
	private static final int CLIENT_DELAY = 100;
	
	private ActionClientProtocol protocol;
	
	private ActionClientListener listener;
	
	public NinjaRobotClient(String ip) {
		super();
		
		ActionClient client = new ActionClient(ip, PORT, this);
		client.start(CLIENT_DELAY);
		protocol = client.getActionProtocol();
	}
	
	@Override
	public void exitClient(String id) {
		if(listener != null) {
			return;
		}
		
		listener.exitClient(id);
	}

	@Override
	public void joinedClient(String id) {
		if(listener != null) {
			return;
		}
		
		listener.joinedClient(id);
	}

	@Override
	public void receiveMessage(String id, String message) {
		if(listener != null) {
			return;
		}
		
		listener.receiveMessage(id, message);
	}

	@Override
	public void init(String[] ids) {
		if(listener != null) {
			return;
		}
		
		listener.init(ids);
	}

	public void setListener(ActionClientListener listener) {
		this.listener = listener;
	}

	public ActionClientProtocol getProtocol() {
		return protocol;
	}
	
}
