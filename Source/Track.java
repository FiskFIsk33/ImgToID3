import com.mpatric.mp3agic.*;
import java.io.IOException;


public class Track
{
	private Image img;
	private Mp3File f;

	/**
	*	This constructor loads up the specified mp3 file
	*	and adds an id3v2 tag if nonexistent
	**/
	public Track(String path)
	{
		try
		{
		f = new Mp3File(path);
		}catch(IOException e){}catch(UnsupportedTagException e){}catch(InvalidDataException e){}
		ID3v2 id3v2Tag;
		if (f.hasId3v2Tag())
		{
			id3v2Tag = f.getId3v2Tag();
		} else
		{
			// mp3 does not have an ID3v2 tag, let's create one..
			id3v2Tag = new ID3v24Tag();
			f.setId3v2Tag(id3v2Tag);
		}
	}

	/**
	*	Loads up the image
	**/
	public void loadImage(String path)
	{
		img = new Image(path);
		System.out.println(img.getMimetype());

	}
}