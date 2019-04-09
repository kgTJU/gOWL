package fdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Partition {
	public void partitionModel(int length){
		String path = "data/resultFile/result";
		File file = new File(path);
		if(!file.exists()||file.isDirectory())
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String str = null;
			int count=0;
			while ((str = reader.readLine()) != null) {
				//System.out.println(str);
				//a.add(str);
				//s1.add(str);	
				//A[count]=str;
				//count++;
				String[] strSpace = str.trim().split(" ");
				if(strSpace.length!=4)
				System.out.println("str size "+strSpace.length);
				int tmp1,tmp2;
				//转义字符需要用[]括起来
				tmp1=(strSpace[0].split("[*]").length)-1;
				tmp2=(strSpace[2].split("[*]").length)-1;
				if(tmp1<=length && tmp2<=length){
					write2File(str,length);
				}
				else{
					count++;
					System.out.println(count+str);
				}
			}

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
	}
	
	public static void write2File(String input,int length){
		String path="data/resultFile/Uk"+length;
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
			writer= new BufferedWriter(new FileWriter(file,true));
			writer.write(input+"\r\n");
				
			
//			System.out.println("a.size="+a.size());
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
