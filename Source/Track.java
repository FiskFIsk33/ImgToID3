import com.mpatric.mp3agic.*;
import java.io.IOException;
import java.lang.Throwable;
import java.io.File;


public class Track
{
	private Image img;
	private int hasImage; //0 for none found, 1 for found, 2 for already had
	//private Mp3File f;
	private ID3v2 tag;
	private String path;

	/**
	*	This constructor loads up the specified mp3 file
	*	and adds an id3v2 tag if nonexistent
	**/
	public Track(String path)
	{
		loadFile(path);
		this.path = path;
		hasImage = 0;
	}

	public String toString()
	{
		String string;
		string = "file:  " + path + System.getProperty("line.separator");
		if (hasImage==1)
		{
			string = string + "image: " + img.getPath();
		}else if(hasImage==0)
		{
			string = string + "No Image Found!";
		}else if(hasImage==2)
		{
			string = string + "Image ignored, file already tagged (change settings in config.cfg)";
		}
		return string;
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
		if (hasImageTag())
		{
			hasImage = 2;
		}else
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
	
				////System.out.println("mp3 says Path path is: "+getPath());
				////System.out.println("looking in Path path: "+path+" for "+soughtName);
	
				File folder = new File(path);
				////System.out.println(path + " path");
				File[] listOfFiles = folder.listFiles();   //load all files in folder
	
				if(listOfFiles != null && listOfFiles.length != 0) //is the folder NOT empty?
				{
	
					for (int j = 0; j < listOfFiles.length && found == 0; j++) {
						if (listOfFiles[j].isFile())			//is file? (not folder)
						{
							////System.out.println("file: " + listOfFiles[j].getName()+ " sought: " + soughtName);
							String filename = listOfFiles[j].getName();
							if (filename.matches("(?i).*" + soughtName +".*"))//(regex) is this the file We're seeking (minus filetype)?
							{
								////System.out.println("file matched: " + listOfFiles[j].getName());
								String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length()); //is the file an image?
								if (Config.isImage(extension))
								{
									////System.out.println(path + filename + " found for " + this.path);
									found = 1;
									loadImage(path + filename);
								}
							}
						}
					}
				}else
				{
					////System.out.println("folder: "+path+" empty");
				}
			}
			if (found == 0)
			{
				////System.out.println(":( didnt find for: " + this.path);
			}
		}
	}

	public boolean hasImageTag()
	{
		boolean bool = false;
		ID3v2 tag = f.getId3v2Tag();
		byte[] tagimage = tag.getAlbumImage();    //if replace false, image exists
		if(tagimage != null && !Config.getReplace())
		{
			//System.out.println("image exists");
			bool = true;
		}
		return bool;
	}

	/**
	*	Loads the image
	**/
	public void loadImage(String path) throws IOException
	{
		img = new Image(path);
		hasImage = 1;
	}

	/**
	*	writes image to file
	**/
	public void writeImage()
	{
		ID3v2 tag = f.getId3v2Tag();
		if (hasImage == 1)
		{
			String tempName = path + "_temp_backup_ImgToID3";
			try
			{	
				//System.out.println("saving to " + path);
				tag.setAlbumImage(img.getImageData(), img.getMimetype());		//save new file
				f.save(path + "_tagged_ImgToID3");
	
	
	
				if(!renameFile(path, tempName))				//move old file to temp name				
				{ System.out.println("oldfile Rename failed!: " + path); 
				}
	
				if(!renameFile(path + "_tagged_ImgToID3", path))				//move new file to orig filename				
				{ System.out.println("newfile Rename failed!: " + path + "_tagged_ImgToID3"); }
	
				File backup = new File(tempName);
	      		if(backup.delete())
				{
					//System.out.println("tmp Deleted");
					ChangesFile.writeLine("Success: " + path);
				}else {
	         		System.out.println("tmpDeletionError: " + tempName);
	      		}	
			}catch(Throwable e)
			{	System.out.println("Exception: " + e.getMessage());	//couldn't save new mp3
				ChangesFile.writeLine("Failed:  " + path + " ERROR: " + e.getMessage());
			}finally
			{
				File backup = new File(tempName);
				File orig = new File(path);
				backup.renameTo(orig);
				File written = new File(path + "_tagged_ImgToID3");
				written.delete();

			}
		}else
		{
			//System.out.println("Skipping: " + path);
			ChangesFile.writeLine("Skipped: " + path);
		}
	}

	/*
	*	Takes file at path source and renames to path dest.
	*/
	private boolean renameFile(String source, String dest)
	{
		boolean success = false;
		File oldName = new File(source);								//move old file to temp name
		File newName = new File(dest);
		if(oldName.renameTo(newName))				
		{
			success = true;
		}	
		return success;
	}
}