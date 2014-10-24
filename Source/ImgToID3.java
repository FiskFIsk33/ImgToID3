import com.mpatric.mp3agic.*;
import java.io.IOException;
import java.io.Console;

class ImgToID3
{
	public static void main(String[] args) throws IOException, NotSupportedException
	{
		
		String teststring = "Uoååegjtj";
		if (teststring.matches("(?i).*" + "" +".*"))
		{
			//System.out.println("matched");
		}else{
			//System.out.println("no match");
		}


		//System.out.println("test");
		//Track testtrack = new Track("Thrones.mp3");
		//testtrack.loadImage("Folder.jpg");
		TracksList.loadFiles(Config.getMusicPath());
		TracksList.loadImages();
		TracksList.previewToTxt();

		System.out.println("Opening ChangesPreview.txt");

		yesOrNo();


	}

	public static void yesOrNo()
	{
		Console console = System.console();
		String input = console.readLine("type Y to confirm or N to exit:");
		if(input.contains("Y")||input.contains("y"))
		{
			TracksList.writeImages();
		}else if(input.contains("N")||input.contains("n"))
		{
			System.exit(0);
		}else
		{
			System.out.println("invalid choice." + input);
			yesOrNo();
		}

	}
}