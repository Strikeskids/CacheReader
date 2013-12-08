
public class FileData {
	private final int id;	
	private final Archive archive;
	
	private final int identifier;
	
	private final byte[] data;
		
	public FileData(Archive archive, int id, int identifier, byte[] data) {
		this.archive = archive;
		this.id = id;
		this.identifier = identifier;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public Archive getArchive() {
		return archive;
	}

	public byte[] getData() {
		return data;
	}
	
	public Stream getDataAsStream() {
		return new ByteStream(data);
	}

	public int getIdentifier() {
		return identifier;
	}
}
