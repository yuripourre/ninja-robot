package br.com.nrobot.network.client;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.midnight.model.Peer;
import br.com.midnight.protocol.common.StringClientProtocol;

public class NRobotClientProtocol extends StringClientProtocol {
	
	private NRobotClientListener listener;
	
	public static final String PREFIX_NINJA_ROBOT = "nr";
	
	public static final String PREFIX_COMMAND = "c";
	public static final String PREFIX_POSITIONS = "p";
	
	public static final String STATE_PRESS = "h";
	public static final String STATE_RELEASE = "r";
	public static final String KEY_RIGHT = ">";
	public static final String KEY_LEFT = "<";
	
	public static final String PREFIX_INIT = "i";
	public static final String PREFIX_JOIN = "j";
	public static final String PREFIX_EXIT = "q";
	
	public NRobotClientProtocol(NRobotClientListener listener) {
		super(PREFIX_NINJA_ROBOT);
		this.listener = listener;
	}

	public void sendPressKeyLeft() {
		sendTCP(PREFIX_COMMAND+" "+STATE_PRESS+" "+KEY_LEFT);
	}
	
	public void sendReleaseKeyLeft() {
		sendTCP(PREFIX_COMMAND+" "+STATE_RELEASE+" "+KEY_LEFT);
	}
	
	public void sendPressKeyRight() {
		sendTCP(PREFIX_COMMAND+" "+STATE_PRESS+" "+KEY_RIGHT);
	}
	
	public void sendReleaseKeyRight() {
		sendTCP(PREFIX_COMMAND+" "+STATE_RELEASE+" "+KEY_RIGHT);
	}
	
	public void sendKeyEvent(KeyEvent event) {
		String message = event.getState().toString()
				+" "+Integer.toString(event.getKey());
		
		sendTCP(message);
	}
	
	public void sendPointerEvent(PointerEvent event) {
		String message = event.getState().toString()
				+" "+Integer.toString(event.getX())
				+" "+Integer.toString(event.getY());
		
		sendTCP(message);
	}
	
	@Override
	public void receiveTCP(Peer peer, String msg) {
		
		if(msg.startsWith(PREFIX_INIT)) {
			//HandShake message
			String crop = msg.substring((PREFIX_INIT+" ").length());
			String[] ids = crop.split(" "); 
			listener.init(ids);
		} else if(msg.startsWith(PREFIX_JOIN)) {
			String id = msg.split(" ")[1];
			listener.joinedClient(id);
		} else if(msg.startsWith(PREFIX_EXIT)) {
			String id = msg.split(" ")[1];
			listener.exitClient(id);
		} else if(msg.startsWith(PREFIX_POSITIONS)) {
			String crop = msg.substring((PREFIX_POSITIONS+" ").length());
			listener.updatePositions(crop);
		} else {
			String id = msg.split(" ")[0];
			String message = msg.split(" ")[1];
			listener.receiveMessage(id, message);
		}
	}

	@Override
	public void receiveUDP(Peer peer, String msg) {
		// TODO Auto-generated method stub
		
	}
			
}
