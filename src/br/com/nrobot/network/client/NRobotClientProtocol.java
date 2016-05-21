package br.com.nrobot.network.client;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.midnight.model.Peer;
import br.com.midnight.protocol.common.StringClientProtocol;
import br.com.nrobot.config.Config;

public class NRobotClientProtocol extends StringClientProtocol {
	
	private NRobotClientListener listener;
	
	public static final String PREFIX_NINJA_ROBOT = "nr";
	
	public static final String PREFIX_COMMAND = "c";
	public static final String PREFIX_CONFIG = "f";
	public static final String PREFIX_POSITIONS = "p";
	public static final String PREFIX_MESSAGE = "m";
	public static final String PREFIX_CHEAT_CODE = "d";
	
	public static final String CHEAT_RESSURRECT = "ress";
	
	public static final String STATE_PRESS = "h";
	public static final String STATE_RELEASE = "r";
	public static final String KEY_RIGHT = ">";
	public static final String KEY_LEFT = "<";
	public static final String KEY_JUMP = "J";
	public static final String KEY_ITEM = "I";
	
	public static final String PREFIX_INIT = "i";
	public static final String PREFIX_JOIN = "j";
	public static final String PREFIX_EXIT = "q";
	
	public static final String CONFIG_START = "st";
	public static final String CONFIG_NAME = "cn";
	public static final String CONFIG_SPRITE = "ci";
	
	public NRobotClientProtocol(NRobotClientListener listener) {
		super(PREFIX_NINJA_ROBOT);
		this.listener = listener;
	}
	
	public void sendPressKey(String key) {
		sendTCP(PREFIX_COMMAND+" "+STATE_PRESS+" "+key);
	}
	
	public void sendReleaseKey(String key) {
		sendTCP(PREFIX_COMMAND+" "+STATE_RELEASE+" "+key);
	}
	
	public void sendName(String name) {
		sendTCP(PREFIX_CONFIG+" "+CONFIG_NAME+" "+name);
	}
	
	public void sendSprite(String sprite) {
		sendTCP(PREFIX_CONFIG+" "+CONFIG_SPRITE+" "+sprite);
	}
	
	public void sendStart() {
		sendTCP(PREFIX_CONFIG+" "+CONFIG_START);
	}
	
	public void sendRessurrect() {
		sendTCP(PREFIX_CHEAT_CODE+" "+CHEAT_RESSURRECT);
	}
	
	public void sendMessage(String message) {
		sendTCP(PREFIX_MESSAGE+" "+message);
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
			
			Config config = listener.getConfig();
			sendName(config.getName());
		} else if(msg.startsWith(PREFIX_JOIN)) {
			String id = msg.split(" ")[1];
			String name = msg.split(" ")[2];
			listener.joinedClient(id, name);
		} else if(msg.startsWith(PREFIX_EXIT)) {
			String id = msg.split(" ")[1];
			listener.exitClient(id);
		} else if(msg.startsWith(PREFIX_POSITIONS)) {
			String crop = msg.substring((PREFIX_POSITIONS+" ").length());
			listener.updatePositions(crop);
		} else if(msg.startsWith(PREFIX_CONFIG)) {
			String crop = msg.substring((PREFIX_CONFIG+" ").length());
			
			String[] parts = crop.split(" ");
			String id = parts[0];
			String config = parts[1];
			String value = parts[2];
			
			if(CONFIG_NAME.equals(config)) {
				listener.updateName(id, value);	
			} else if(CONFIG_SPRITE.equals(config)) {
				listener.updateSprite(id, value);	
			} else if(CONFIG_START.equals(config)) {
				listener.startGame();	
			}
			
		} else if(msg.startsWith(PREFIX_MESSAGE)) {
			String id = msg.split(" ")[0];
			String message = msg.substring((PREFIX_MESSAGE+" ").length());
			listener.receiveMessage(id, message);
		}
	}

	@Override
	public void receiveUDP(Peer peer, String msg) {
		// TODO Auto-generated method stub
		
	}

	public NRobotClientListener getListener() {
		return listener;
	}

	public void setListener(NRobotClientListener listener) {
		this.listener = listener;
	}

}
