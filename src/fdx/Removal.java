package fdx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class Removal {
	public void cancelRepeat(String pathABox) throws IOException{
		//String path = "D:\\workspace\\Ukmodel\\data\\resultFile\\result";
		//String path = "LUBM10.nt";
		Set<String> s = new HashSet();
		int count=0;
		//int count1=0;
		File file=new File(pathABox);
		File file2= new File(pathABox+".tmp");
		PrintWriter pw = new PrintWriter(new FileWriter(file2));
		if(!file.exists()||file.isDirectory())
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		BufferedReader reader = null;
		int size=s.size();
		try {
			reader = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = reader.readLine()) != null) {
				//System.out.println(str);
				count++;
				s.add(str);
				//pw.println(str);
				//System.out.println(str);
				if(size==s.size()){
					//System.out.println(str);
					
				}
				else{
					size=s.size();
					pw.println(str);
					pw.flush();
				}
			}
			pw.close();
			reader.close();
			//Delete the original file
		      if (!file.delete()) {
		        System.out.println("Could not delete file");
		        return;
		      } 

		      //Rename the new file to the filename the original file had.
		      if (!file2.renameTo(file))
		        System.out.println("Could not rename file");
		      
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ABox size is: "+s.size()+"count = "+count);
	}
}
