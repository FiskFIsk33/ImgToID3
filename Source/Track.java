import com.mpatric.mp3agic.*;
import java.io.IOException;
import java.io.File;


public class Track
{
	private Image img;
	private boolean hasImage;
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
		hasImage = false;
	}

	public String toString()
	{
		String string;
		string = "file: " + path;
		if (hasImage)
		{
			string = string + " image: " + img.getPath();
		}else
		{
			string = string + " No Image Found";
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

			//System.out.println("mp3 says Path path is: "+getPath());
			//System.out.println("looking in Path path: "+path+" for "+soughtName);

			File folder = new File(path);
			//System.out.println(path + " path");
			File[] listOfFiles = folder.listFiles();   //load all files in folder

			if(listOfFiles != null && listOfFiles.length != 0) //is the folder NOT empty?
			{

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
								//System.out.println(path + filename + " found for " + this.path);
								found = 1;
								loadImage(path + filename);
								hasImage = true;
							}
						}
					}
				}
			}else
			{
				//System.out.println("folder: "+path+" empty");
			}
		}
		if (found == 0)
		{
			//System.out.println(":( didnt find for: " + this.path);
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
		String tempName = path + "_temp_backup_ImgToID3";
		try
		{	
			System.out.println("saving to " + path);
			tag.setAlbumImage(img.getImageData(), img.getMimetype());		//save new file
			f.save(path + "_tagged_ImgToID3");



			if(!renameFile(path, tempName))				//move old file to temp name				
			{ System.out.println("oldfile Rename failed!"); }

			if(!renameFile(path + "_tagged_ImgToID3", path))				//move new file to orig filename				
			{ System.out.println("newfile Rename failed!"); }

			File backup = new File(tempName);
      		if(backup.delete())
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
			File backup = new File(tempName);
			File orig = new File(path);
			backup.renameTo(orig);
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