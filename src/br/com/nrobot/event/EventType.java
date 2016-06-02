package br.com.nrobot.event;

public enum EventType {
	RESSURRECT ("ress");
	
	public final String code;

	EventType(String code) {
		this.code = code;
	}
	
	public static EventType valueOfCode(String code) {
		for (EventType type:values()) {
			if(code.equals(type.code)) {
				return type;		
			}
		}
		return null;
	}
}
