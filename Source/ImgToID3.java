import com.mpatric.mp3agic.*;
import java.io.IOException;

class ImgToID3
{
	public static void main(String[] args) throws IOException, NotSupportedException
	{
		System.out.println("test");
		Track testtrack = new Track("Thrones.mp3");
		testtrack.loadImage("Folder.jpg");
	}
}