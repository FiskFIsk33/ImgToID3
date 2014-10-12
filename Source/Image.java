import java.nio.file.Files;
import java.io.File;

public class Image
{
	private File imgFile;
	private String mimetype;

	/**
	*	Loads up the image
	**/
	public Image(String path)
	{
		mimetype = Files.probeContentType(path);
		imgFile = new File(path);
	}

	/**
	*	returns the image as byte[]
	**/
	public byte[] getImageData()
	{
		byte[] fileContent = Files.readAllBytes(imgFile.toPath());
		return fileContent;
	}

	public String getMimetype()
	{
		return mimetype;
	}



}

