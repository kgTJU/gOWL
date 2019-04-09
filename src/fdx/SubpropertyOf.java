package fdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.clarkparsia.sparqlowl.parser.antlr.SparqlOwlParser.booleanLiteral_return;

public class SubpropertyOf {
	/*
	 * dataMap存放subpropertyOf的二元关系？
	 * key：子关系
	 * value：父关系
	 */
	Map<String, String> dataMap =  new HashMap<String, String>();
	/*
	 * 
	 */
	//String pathIn = "D:\\workspace\\Ukmodel\\data\\resultsubpropertyof";
	String pathIn = "data/resultsubpropertyof";
	File fileIn=new File(pathIn);
	BufferedReader reader;
	/*
	 * 
	 */
	//String pathOut="D:\\workspace\\Ukmodel\\data\\resultFile\\output_ResultSubpropertyOf";
	String pathOut="data/resultFile/output_ResultSubpropertyOf";
	File fileOut=new File(pathOut);
	BufferedWriter writer;
	/**
	 * 
	 */
	/*public static void main(String[] args){
		SubpropertyOf subpropertyOf = new SubpropertyOf();
		subpropertyOf.readFromeFileAndWrite2File();
	}*/
	public void saveData2Map(String son,String father){
		dataMap.put(son, father);
	}
	/**
	 * 读取文件中的内容并标号
	 */
	public void readFromeFileAndWrite2File(){
		
		
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
			writer= new BufferedWriter(new FileWriter(fileOut));
			reader = new BufferedReader(new FileReader(fileIn));
			String str = null;
			while ((str = reader.readLine()) != null) {
				//System.out.println(str);
				if(str.trim()==""||"".equals(str.trim())){
					continue;
				}else{
					//System.out.println(str.charAt(0));
					if(str.charAt(0)=='<'){
						writer.write(str+"\r\n");
						/*
						 * 将读取的字符串存放起来
						 */
						String[] temp = str.split("\t");
						String son= "";
						String father ="";
						boolean flag = false;
						for(int i=0;i<temp[0].length();i++){
							if(temp[0].charAt(i)=='#'){
								flag=true;
								continue;
							}
							if(flag==true&&temp[0].charAt(i)!='>'){
								son+=temp[0].charAt(i);
							}
						}
						flag =false;
						for(int i=0;i<temp[1].length();i++){
							if(temp[1].charAt(i)=='#'){
								flag=true;
								continue;
							}
							if(flag==true&&temp[1].charAt(i)!='>'){
								father+=temp[1].charAt(i);
							}
						}
						//System.out.println(son+"   "+father);
						saveData2Map(son, father);
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
