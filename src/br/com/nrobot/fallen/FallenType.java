package br.com.nrobot.fallen;

public enum FallenType {
	BOMB("B") {
		public Fallen create(int x, int y) {
			return new Bomb(x, y);
		}
	},
	GLUE("G") {
		public Fallen create(int x, int y) {
			return new Glue(x, y);
		}
	},
	HIVE("H") {
		public Fallen create(int x, int y) {
			return new Hive(x, y);
		}
	},
	LEAF("L") {
		public Fallen create(int x, int y) {
			return new Leaf(x, y);
		}
	};

	public final String prefix;

	FallenType(String prefix) {
		this.prefix = prefix;
	}

	public static FallenType fromParam(String prefix) {
		for (FallenType type : values()) {
			if (type.prefix.equals(prefix)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown FallenType: " + prefix);
	}

	public abstract Fallen create(int x, int y);
}
