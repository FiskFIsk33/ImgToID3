import com.mpatric.mp3agic.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TracksList
{
	private static List<Track> tracks = new ArrayList<Track>();

	/**
	*	Load Tracks Into List
	**/
	public static void loadFiles(String path)
	{
		System.out.println("looking for files in "+path);
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();  //load all files in folder
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile())			//is file? (not folder)
			{
				String filename = listOfFiles[i]+"";
				if (Config.isAudio(filename))		//is audio file?
				{
					System.out.println("found: " + filename);
					tracks.add(new Track(filename));	//add audio file to the list
				}
			}else if (listOfFiles[i].isDirectory()) //found folder, check it too
			{
				System.out.println("checking: " + listOfFiles[i]);
				loadFiles(listOfFiles[i] + "");
			}
		}
	}
	public static void loadImages() throws IOException
	{
		for(int i = 0; i < tracks.size(); i++)
		{
			tracks.get(i).findImage();
		}
	}

	public static void statusToTxt()
	{
		//ChangesFile txt = new ChangesFile();
		for(Track track : tracks)
		{
			ChangesFile.writeLine(track.toString());
		}
		ChangesFile.open();
	}
}