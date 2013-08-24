
public class MetaChildEntry {

	private int identifier = -1;
	
	private final int id;
	
	public MetaChildEntry(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

}
