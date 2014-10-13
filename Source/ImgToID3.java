import com.mpatric.mp3agic.*;
import java.io.IOException;

class ImgToID3
{
	public static void main(String[] args) throws IOException, NotSupportedException
	{
		
		String teststring = "Uoååegjtj";
		if (teststring.matches("(?i).*" + "" +".*"))
		{
			System.out.println("matched");
		}else{
			System.out.println("no match");
		}


		System.out.println("test");
		Config.init();
		//Track testtrack = new Track("Thrones.mp3");
		//testtrack.loadImage("Folder.jpg");
		TracksList.loadFiles("./");
		TracksList.loadImages();
	}
}