package br.com.nrobot.network.client;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.midnight.model.Peer;
import br.com.midnight.protocol.common.StringClientProtocol;

public class ActionClientProtocol extends StringClientProtocol {
	
	private ActionClientListener listener;
	
	public static final String PREFIX_ACTION = "/a";
	
	public static final String PREFIX_INIT = "i";
	public static final String PREFIX_JOIN = "j";
	public static final String PREFIX_EXIT = "q";
	
	public ActionClientProtocol(ActionClientListener listener) {
		super(PREFIX_ACTION);
		this.listener = listener;
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
