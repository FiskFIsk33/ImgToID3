import com.mpatric.mp3agic.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TracksList
{
	private static List<Track> tracks = new LinkedList<Track>();
	private static int fileNumber = 0;

	/**
	*	Load Tracks Into List
	**/
	public static void loadFiles(String path)
	{
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();  //load all files in folder

		if (listOfFiles != null && listOfFiles.length > 0)
		{
			for(File file:listOfFiles)
			{
				//if (file.isFile())			//is file? (not folder)
				//{
				String filename = file+"";
				if (Config.isAudio(filename))		//is audio file?
				{
					try{
						tracks.add(new Track(filename));	//add audio file to the list
						System.out.println("loaded: " + ++fileNumber + " files");
					}catch(Exception e){
						System.out.println("Error, not loaded: " + filename );
					}
				}
				//}
				else if (file.isDirectory()) //found folder, check it too
				{
					loadFiles(file + "");
				}
			}
		}
	}
	public static void loadImages() throws IOException
	{
		int fileProcessing = 0;
		for(Track track : tracks)
		{
			System.out.println("finding image for file: " + ++fileProcessing + " of " + fileNumber);
			track.findImage();
		}
	}

	public static void writeImages()
	{
		int fileProcessing = 0;
		for(Track track : tracks)
		{
			System.out.println("Processing: " + ++fileProcessing + " of " + fileNumber);
			track.writeImage();
		}
		ChangesFile.open(false);
	}

	public static void previewToTxt()
	{
		//ChangesFile txt = new ChangesFile();
		for(Track track : tracks)
		{
			ChangesFile.writeLine(track.toString());
		}
		ChangesFile.open(true);
	}
}