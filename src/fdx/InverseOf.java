package fdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InverseOf {
	/*
	 * dataMap存放InverseOf的逆关系？
	 * key：互为逆关系A
	 * value：互为逆关系B
	 */
	Map<String, String> dataMap =  new HashMap<String, String>();
	/*
	 * 
	 */
	//String pathIn = "D:\\workspace\\Ukmodel\\data\\resultinverseof";
	String pathIn = "data/resultinverseof";
	File fileIn=new File(pathIn);
	BufferedReader reader;
	/*
	 * 
	 */
	//String pathOut="D:\\workspace\\Ukmodel\\data\\resultFile\\output_ResultInverseOf";
	String pathOut="data/resultFile/output_ResultInverseOf";
	File fileOut=new File(pathOut);
	BufferedWriter writer;
	/**
	 * 
	 */
	/*public static void main(String[] args){
		InverseOf inverseOf = new InverseOf();
		inverseOf.readFromFileAndWrite2File();
	}*/
	/**
	 * 
	 * @param son
	 * @param father
	 */
	public void saveData2Map(String reverseA,String reverseB){
		dataMap.put(reverseA, reverseB);
	}
	/**
	 * 读取文件中的内容并标号
	 */
	public void readFromFileAndWrite2File(){

		if(!fileIn.exists()||fileIn.isDirectory())
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if(!fileOut.exists()){
				try {
					fileOut.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		try {
			writer = new BufferedWriter(new FileWriter(fileOut));
			reader = new BufferedReader(new FileReader(fileIn));
			String str = null;
			while ((str = reader.readLine()) != null) {
				
				if(str.trim()==""||"".equals(str.trim())){
					continue;
				}else{
					if(str.charAt(0)=='<'&&str.charAt(1)=='h'){
						//System.out.println(str);
						writer.write(str+"\r\n");
						String[] temp = str.split("\t");
						String reverseA= "";
						String reverseB ="";
						boolean flag = false;
						for(int i=0;i<temp[0].length();i++){
							if(temp[0].charAt(i)=='#'){
								flag=true;
								continue;
							}
							if(flag==true&&temp[0].charAt(i)!='>'){
								reverseA+=temp[0].charAt(i);
							}
						}
						flag =false;
						for(int i=0;i<temp[1].length();i++){
							if(temp[1].charAt(i)=='#'){
								flag=true;
								continue;
							}
							if(flag==true&&temp[1].charAt(i)!='>'){
								reverseB+=temp[1].charAt(i);
							}
						}
						//System.out.println(reverseA+"   "+reverseB);
						saveData2Map(reverseA, reverseB);
						saveData2Map(reverseB, reverseA);
						
					}else {
						continue;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
