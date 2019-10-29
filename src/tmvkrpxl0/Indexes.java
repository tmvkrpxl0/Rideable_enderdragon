package tmvkrpxl0;

enum Indexes {
	DESTROY_BLOCK(0),
	DAMAGE_ENTITY(1),
	MULTIPLY(2),
	LEVEL(3),
	EXP(4),
	COOLDOWN(5),
	FLYLEFT(6);
	private final int index;
	private Indexes(int r) {
		this.index = r;
	}
	protected int getIndex() {
		return index;
	}
}
