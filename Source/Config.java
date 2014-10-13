import java.util.*;



public class Config
{
	private static List<String> audioFileTypes;
	private static List<String> imageFileTypes;
	private static List<String> imagePaths;
	
	/*
	* initialize values
	*/
	public static void init()
	{
		audioFileTypes = new ArrayList<String>();
		audioFileTypes.add("mp3");
		System.out.println(""+audioFileTypes);

		imageFileTypes = new ArrayList<String>();
		imageFileTypes.add("png");
		imageFileTypes.add("jpg");
		System.out.println(""+imageFileTypes);

		imagePaths = new ArrayList<String>();
		imagePaths.add("test/Folder");
		imagePaths.add("Folder");
		imagePaths.add("");
	}

	/*
	*	Check if provided file is Defined as allowed Audio Filetype
	*/
	public static boolean isAudio(String filename)
	{
		boolean ret;
		String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		if(audioFileTypes.contains(extension))
		{
			ret = true;
		}else
		{
			ret = false;
		}
		return ret;

	}

	/*
	*	Check if provided file is Defined as allowed Image Filetype
	*/
	public static boolean isImage(String filename)
	{
		boolean ret;
		String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		if(imageFileTypes.contains(extension))
		{
			ret = true;
		}else
		{
			ret = false;
		}
		return ret;

	}

	public static int getImgPathSize()
	{
		return imagePaths.size();
	}

	public static String getImgPath(int index)
	{
		return imagePaths.get(index);
	}
}