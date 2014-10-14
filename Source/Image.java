import java.nio.file.*;
import java.io.File;
import java.io.IOException;

public class Image
{
	private File imgFile;
	private String mimetype;

	/**
	*	Loads up the image
	**/
	public Image(String sPath) throws IOException
	{

			Path path = Paths.get(sPath);
			mimetype = Files.probeContentType(path);
			imgFile = new File(sPath);

	}

	/**
	*	returns the image as byte[]
	**/
	public byte[] getImageData() throws IOException
	{

		byte[] fileContent = Files.readAllBytes(imgFile.toPath());
		return fileContent;
	}

	public String getPath()
	{
		return imgFile.getPath();
	}

	public String getMimetype()
	{
		return mimetype;
	}



}

