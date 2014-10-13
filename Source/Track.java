import com.mpatric.mp3agic.*;
import java.io.IOException;
import java.io.File;


public class Track
{
	private Image img;
	private Mp3File f;
	private String path;

	/**
	*	This constructor loads up the specified mp3 file
	*	and adds an id3v2 tag if nonexistent
	**/
	public Track(String path)
	{
		loadFile(path);
		this.path = path;
	}

	public String getPath()
	{
		String retpath;
		if (path.lastIndexOf("/") != -1)		//if contains /, split folder name from file name
		{
			retpath = path.substring(0, path.lastIndexOf("/"));
		} else if (path.lastIndexOf("\\") != -1)
		{
			retpath = path.substring(0, path.lastIndexOf("\\"));
		}else
		{										//else, path is "here"
			retpath = "./";
		}
		return retpath;
	}

	/**
	*	Loads up the image
	**/
	public void loadFile(String path)
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

	/*
	*	Uses the predefined search rules (in Config) to find a suitable image
	*/
	public void findImage() throws IOException
	{
		
		int found = 0;
		//String soughtName;
		for(int i=0; (i < Config.getImgPathSize()) && (found == 0); i++) //iterate through Config image paths til image found
		{
			String path = Config.getImgPath(i);
			String soughtName = Config.getImgName(i);
			if (path == "./")
			{
				path = getPath()+"/";
			}else
			{
				path = getPath()+path;
			}

			System.out.println("mp3 says Path path is: "+getPath());
			System.out.println("looking in Path path: "+path+" for "+soughtName);

			File folder = new File(path);
			//System.out.println(path + " path");
			File[] listOfFiles = folder.listFiles();   //load all files in folder
			//System.out.println("files: " + listOfFiles.length);

			for (int j = 0; j < listOfFiles.length && found == 0; j++) {
				if (listOfFiles[j].isFile())			//is file? (not folder)
				{
					//System.out.println("file: " + listOfFiles[j].getName()+ " sought: " + soughtName);
					String filename = listOfFiles[j].getName();
					if (filename.matches("(?i).*" + soughtName +".*"))//(regex) is this the file We're seeking (minus filetype)?
					{
						//System.out.println("file matched: " + listOfFiles[j].getName());
						String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length()); //is the file an image?
						if (Config.isImage(extension))
						{
							System.out.println(path + filename + " found for " + this.path);
							found = 1;
							loadImage(path + filename);
							setImage();
						}
					}
				}
			}
		}
	}

	/**
	*	Loads the image
	**/
	public void loadImage(String path) throws IOException
	{
		img = new Image(path);
		System.out.println(img.getMimetype());
	}

	/**
	*	writes image to file
	**/
	public void setImage()
	{
		ID3v2 tag = f.getId3v2Tag();
		try
		{	
			System.out.println("saving to " + path);
			tag.setAlbumImage(img.getImageData(), img.getMimetype());		//save new file
			f.save(path + "_tagged_ImgToID3");

      		File ooldName = new File(path);									//move old file to temp name
			File onewName = new File(path + "_temp_backup_ImgToID3");
			if(ooldName.renameTo(onewName))				
			{
				System.out.println("oldfile Renamed");
			}else {
         		System.out.println("oldfile RenameError");
      		}	

			File noldName = new File(path + "_tagged_ImgToID3");			//move new file to orig filename
			File nnewName = new File(path);
			if(noldName.renameTo(nnewName))
			{
				System.out.println("newfile Renamed");
			}else {
         		System.out.println("newfile RenameError");
      		}

      		if(onewName.delete())
			{
				System.out.println("tmp Deleted");
			}else {
         		System.out.println("tmpDeletionError");
      		}




		}catch(IOException e)
		{	System.out.println("IOException: " + e.getMessage());	//couldn't save new mp3
		}catch(NotSupportedException e)
		{	System.out.println("NotSupportedException: " + e.getMessage()); //couldn't save new mp3
		}finally
		{
			File backup = new File(path + "_temp_backup_ImgToID3");
			File orig = new File(path);
			backup.renameTo(orig);
		}
	}
}