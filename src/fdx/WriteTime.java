package fdx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WriteTime {
	public void write2File(String input){
		String path="time";
		File file=new File(path);
		/*
		 * 如果文件不存在创建文件
		 */
		if(!file.exists()){
		   try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		BufferedWriter writer = null;
		try{
			writer= new BufferedWriter(new FileWriter(file));
			writer.write(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
