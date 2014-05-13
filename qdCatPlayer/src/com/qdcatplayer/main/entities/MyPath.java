package com.qdcatplayer.main.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyPathDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.Libraries.MyFileHelper;
import com.qdcatplayer.main.Libraries.MyStringHelper;

@DatabaseTable(tableName = "MyPaths")
public class MyPath extends _MyEntityAbstract<MyPathDAO, MyPath> {
	public static final String ABSPATH_F = "absPath";
	public static final String P_FOLDER_ID = "parentFolder_id";
	// because file/folder has no parent will got null too
	// so, we need to declare new Boolean varible to separate meaning
	// of 2 concept: "no parent" and "not ready yet"
	private Boolean parentFolder_ready = false;
	/**
	 * Mặc định absPath khi lưu vào CSDL, ký tự ' sẽ bị thay thế
	 * Xem MyStringHelper để biết thêm
	 */
	@DatabaseField(unique = true, canBeNull = false)
	private String absPath = null;
	@DatabaseField(canBeNull = false)
	protected String fileName = null;
	protected String fileExtension = null;

	@DatabaseField(canBeNull = true, foreign = true)
	private MyFolder parentFolder = null;

	/**
	 * Khi insert Path thi do Song van chua duoc insert nen tam thoi song=null,
	 * sau khi MyPath duoc insert thanh cong thi Song se co nhiem vu gan nguoc
	 * id cua minh qua chp Path.Song
	 * 
	 * @DatabaseField(foreign = true, canBeNull = true) private MySong song =
	 *                        null;
	 */

	public MyPath() {

	}

	public MyPath(String absPath_) {
		setAbsPath(absPath_);
	}
	/**
	 * Sử dụng khi làm việc ngoài CSDL, có ký tự '
	 * @return
	 */
	public String getAbsPath() {
		if (getGlobalDAO().getSource() != MySource.DISK_SOURCE)// very
																// importance
		{
			super.load();
		}
		return MyStringHelper.filterSQLSpecialAbsPath(absPath,UNKNOWN_VALUE, false);
	}
	/**
	 * Sử dụng khi truy vấn CSDL, do không được sử dụng ký tự '
	 * @return
	 */
	public String getAbsPathForSQL() {
		if (getGlobalDAO().getSource() != MySource.DISK_SOURCE)// very
																// importance
		{
			super.load();
		}
		return MyStringHelper.filterNullOrBlank(absPath, UNKNOWN_VALUE);
	}
	/**
	 * Ham On The Fly, khong ho tro Cache
	 * 
	 * @param withDot
	 * @return
	 */
	public String getFileExtension(Boolean withDot) {
		super.load();
		return withDot ? "."
				+ MyStringHelper
						.filterNullOrBlank(fileExtension, UNKNOWN_VALUE)
				: MyStringHelper
						.filterNullOrBlank(fileExtension, UNKNOWN_VALUE);
	}

	/**
	 * Ham On The Fly, phu thuoc vao absPath Cung trang thai voi loaded
	 * 
	 * @return
	 */
	public String getFileName() {
		super.load();
		return MyStringHelper.filterNullOrBlank(fileName, UNKNOWN_VALUE);
	}

	public MyFolder getParentFolder() {
		// parent folder co bien Boolean rieng de load
		// khong dung chung loaded
		if (parentFolder_ready == true || parentFolder != null) {
			return parentFolder;
		}
		// do not know DAO !
		if (getDao() == null) {
			return null;
		}
		// get through DAO is recommended, DAO will look SOURCE to choose
		// where to get Parent Obj
		parentFolder = getDao().getParentFolder(this);
		// DAO was already set by above call
		parentFolder_ready = true;
		return parentFolder;
	}

	@Override
	public void reset() {
		super.reset();
		// clear member
		parentFolder = null;
		fileName = null;
		// do not reset absPath
		// set lazy state
		parentFolder_ready = false;

	}

	public void setAbsPath(String path) {
		absPath = MyStringHelper.filterSQLSpecialAbsPath(path,UNKNOWN_VALUE, true);
	}

	public void setFileName(String fileName) {
		this.fileName = MyStringHelper
				.filterSQLSpecial(fileName, UNKNOWN_VALUE);
	}

	public void setParentFolder(MyFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public Boolean isOnDisk() {
		if (getAbsPath() == null) {
			return false;
		}
		return MyFileHelper.isExist(getAbsPath());
	}

	public void setFileExtension(String fileExtension_) {
		this.fileExtension = MyStringHelper.filterSQLSpecial(fileExtension_,
				UNKNOWN_VALUE);
	}

}
