import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;



public class Config
{
	private static String musicPath;
	private static List<String> audioFileTypes;
	private static List<String> imageFileTypes;
	private static List<String> imagePaths;
	private static boolean replace;
	
	/*
	* initialize values
	*/
	static
	{
		Properties prop = new Properties();
		InputStream input = null;

		try
		{
			input = new FileInputStream("config.cfg");
			prop.load(input);

			boolean done;

			musicPath = prop.getProperty("music_path");

			replace = Boolean.parseBoolean(prop.getProperty("replace_if_present"));

			audioFileTypes = new ArrayList<String>();
			String audiotypes = prop.getProperty("audio_filetypes");
			done = false;
			while(!done)
			{
				////System.out.println("ln 34: " +audiotypes.indexOf(";"));
				audioFileTypes.add(audiotypes.substring(0, audiotypes.indexOf(";")));
				audiotypes = audiotypes.substring(audiotypes.indexOf(";") + 1, audiotypes.length());
				if(audiotypes.indexOf(";")<0)
				{
					done = true;
				}
			}


			imageFileTypes = new ArrayList<String>();
			String imgtypes = prop.getProperty("image_filetypes");
			done = false;
			while(!done)
			{
				//System.out.println("ln 49: " +imgtypes.indexOf(";"));
				imageFileTypes.add(imgtypes.substring(0, imgtypes.indexOf(";")));
				imgtypes = imgtypes.substring(imgtypes.indexOf(";") + 1, imgtypes.length());
				if(imgtypes.indexOf(";")<0)
				{
					done = true;
				}
			}

			imagePaths = new ArrayList<String>();
			String imgpaths = prop.getProperty("image_paths");
			done = false;
			while(!done)
			{
				////System.out.println("ln 34: " +imgpaths.indexOf(";"));
				imagePaths.add(imgpaths.substring(0, imgpaths.indexOf(";")));
				imgpaths = imgpaths.substring(imgpaths.indexOf(";") + 1, imgpaths.length());
				if(imgpaths.indexOf(";")<0)
				{
					done = true;
				}
			}
			
			//System.out.println("replace = "+replace);
			//System.out.println(""+musicPath);
			//System.out.println(""+audioFileTypes);
			//System.out.println(""+imageFileTypes);
			//System.out.println(""+imagePaths);
		
		}catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		/*
		audioFileTypes = new ArrayList<String>();
		audioFileTypes.add("mp3");
		//System.out.println(""+audioFileTypes);

		imageFileTypes = new ArrayList<String>();
		imageFileTypes.add("png");
		imageFileTypes.add("jpg");
		//System.out.println(""+imageFileTypes);

		imagePaths = new ArrayList<String>();
		//imagePaths.add("test/Folder");
		imagePaths.add("Folfder");
		imagePaths.add("");
		*/
	}

	public static String getMusicPath()
	{
		return musicPath;
	}

	public static boolean getReplace()
	{
		return replace;
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
		String path = imagePaths.get(index);
		if (path.lastIndexOf("/") != -1)		//if contains /, split folder name from file name
		{
			path = path.substring(0, path.lastIndexOf("/")+1);

		}else									//else, path is "here"
		{
			path = "./";
		}
		return path;
	}

	public static String getImgName(int index)
	{
		String path = imagePaths.get(index);
		String name;
		if (path.lastIndexOf("/") != -1)		//if contains /, split folder name from file name
		{
			name = path.substring(path.lastIndexOf("/") + 1, path.length());

		}else
		{
			name = path;					//else, path is ""
		}
		return name;
	}
}