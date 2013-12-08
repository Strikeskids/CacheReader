public class ArchiveMeta {

	private final int cacheType;
	private final int fileId;
	private final int startSector;
	private final int fileSize;

	public ArchiveMeta(int cacheType, int archiveId, int archiveSize, int startSector) {
		this.cacheType = cacheType;
		this.fileId = archiveId;
		this.fileSize = archiveSize;
		this.startSector = startSector;
	}

	public int getCacheType() {
		return cacheType;
	}

	public int getFileId() {
		return fileId;
	}

	public int getStartSector() {
		return startSector;
	}

	public int getFileSize() {
		return fileSize;
	}

	public boolean checkSector(Sector sector) {
		return sector.getFileId() == this.getFileId() && sector.getCacheType() == this.getCacheType();
	}

}
