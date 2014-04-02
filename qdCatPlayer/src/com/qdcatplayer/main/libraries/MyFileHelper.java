package com.qdcatplayer.main.Libraries;

import java.io.File;

public class MyFileHelper {
	public static final String[] soundExtention =
		{
			"MP3", "MP4", "WAV", "WMA", "OOG", "AAC", "3GP", "AMR", "M4A"
		};
	public static String getFileExtension(String fullFilename, Boolean withDot)
	{
		String ext = fullFilename.substring(
				fullFilename.lastIndexOf(".") + (withDot?0:1),
				fullFilename.length()
				);
        return ext.toUpperCase();
	}
	public static String getFileName(String fullFilename, Boolean withExtension)
	{
		if(fullFilename==null)
		{
			return null;
		}
		String filename = fullFilename.substring(
				fullFilename.lastIndexOf("/")+1,
				fullFilename.length()
				);
		filename=filename.substring(0, filename.lastIndexOf("."));
		if(withExtension)
		{
			filename+=MyFileHelper.getFileExtension(fullFilename, false);
		}
        return filename;
	}
	public static String getFolderName(String fullFilename)
	{
		if(fullFilename==null)
		{
			return null;
		}
		String filename = fullFilename.substring(
				fullFilename.lastIndexOf("/")+1,
				fullFilename.length()
				);
        return filename;
	}
	public static Boolean isSoundFile(String fullFilename)
	{
		String ext = MyFileHelper.getFileExtension(fullFilename, false);
		for(String item:MyFileHelper.soundExtention)
		{
			if(item.toUpperCase().equals(ext.toUpperCase()))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Xoa 1 file khoi he thong
	 * @param absPath
	 * @return
	 */
	public static Boolean removeFile(String absPath)
	{
		if(absPath==null || absPath.equals(""))
		{
			return false;
		}
		File f = new File(absPath);
		if(f.isFile())
		{
			return f.delete();
		}
		return false;
	}
	public static Boolean isExist(String absPath)
	{
		File f = new File(absPath);
		return f.exists();
	}
	/**
	 * Delete File or Folder recursively
	 * @param root
	 * @return
	 */
	public static Boolean delete(String root) {
		if(root==null)
		{
			return false;
		}
		File f = new File(root);
		if(f.isDirectory())
		{
			for(File tmp:f.listFiles())
			{
				delete(tmp.getAbsolutePath());
			}
		}
		return f.delete();
	}
}
