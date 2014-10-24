import java.awt.Desktop;
import java.io.*;

/**
*	This class creates and opens the "preview changes"
*	text file.
**/
public class ChangesFile
{	
	private static BufferedWriter out;
	private static String path;
	private static Boolean closed;

	static
	{
		path = "ChangesPreview.txt";
		try{
			out = new BufferedWriter(new FileWriter(path));
			closed = false;
			out.write("List of changes:");
			out.newLine();
			out.newLine();
		}catch(IOException e){
			System.out.println("ChangesFile.initializer IOException: " + e.toString());
		}catch(ExceptionInInitializerError e){
			System.out.println("ChangesFile.initializer ExceptionInInitializerError: " + e.toString());
		}
	}

	public static void writeLine(String text)
	{
		try{
			out.write(text);
			out.newLine();

		}catch(IOException e){
			System.out.println("ChangesFile.writeLine IOException: " + e.toString());
		}

	}

	public static void open(boolean startnew)
	{
		if(closed == false)
		{
			try{
				out.close();
				closed = true;
			}catch(IOException e){
				System.out.println("ChangesFile.open IOException: " + e.toString());
			}
		}else{System.out.println("changesfile error");}
		try{
		File file = new File(path);
		java.awt.Desktop.getDesktop().edit(file);
		}catch(IOException e){
			System.out.println("ChangesFile.open IOException: " + e.toString());
		}
		if(startnew)
		{
			path = "Changes.txt";
			try{
				out = new BufferedWriter(new FileWriter(path));
				closed = false;
				out.write("List of changes made:");
				out.newLine();
				out.newLine();
			}catch(IOException e){
				System.out.println("ChangesFile.initializer IOException: " + e.toString());
			}catch(ExceptionInInitializerError e){
				System.out.println("ChangesFile.initializer ExceptionInInitializerError: " + e.toString());
			}
		}
	}
}