package fdx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GenerateSymmetric {
	
	MapTest mapTest = new MapTest();
	ArrayList<String> tboxsymmetricOfMap=new ArrayList<String>();
	ArrayList<String> aboxdata=new ArrayList<String>();
	ArrayList<String> writeall=new ArrayList<String>();
	//Map<String,ArrayList<String>> aboxMap = new HashMap<String,ArrayList<String>>();


	/**
	 * 获取取得的数据
	 */
	public void getDataMap(String path){
		/*subpropertyOf.readFromeFileAndWrite2File();
		dataMap = subpropertyOf.dataMap;*/
		mapTest.readTBox(path);
		tboxsymmetricOfMap = mapTest.tboxsymmetricOfMap;

	}
	
	public Map<String,ArrayList<String>> Generation(Map<String,ArrayList<String>> aboxMap,String pathABox,String path){
		
		getDataMap(path);
	
		//对于具有transitive的属性
		for(String s:tboxsymmetricOfMap){
			ArrayList<String>aboxMapnew = new ArrayList<String>();

			//从aboxmap中获得该属性的信息，aboxdata中是<主语 宾语，主语 宾语，...>
			if(aboxMap.containsKey(s)){
				aboxdata = aboxMap.get(s);
				//对于每一个“主语1 宾语1”，拆分后添加为“宾语1 主语1”；
				for(String tra:aboxdata){					
					String[] str = tra.split(" ");
					String transdomain = str[0];
					String transrange = str[1];	
					aboxMapnew.add(transrange+" "+transdomain);
					writeall.add(transrange+" "+s+" "+transdomain+" .");
				}
			}
			for(String stradd:aboxMapnew){
				aboxMap.get(s).add(stradd);
			}
		}
														
		writeFile(writeall,pathABox);
		return aboxMap;
	}
	
	public void writeFile(ArrayList<String> output,String pathABox){

		File file=new File(pathABox);
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
			for(String s:output){
				writer.write(s+"\r\n");
			}
			
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
