package fdx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenerateTransitive {
	
	MapTest mapTest = new MapTest();
	ArrayList<String> tboxtransitiveOfMap=new ArrayList<String>();
	ArrayList<String> aboxdata=new ArrayList<String>();
	
	//Map<String,ArrayList<String>> aboxMap = new HashMap<String,ArrayList<String>>();
	

	/**
	 * 获取取得的数据
	 */
	public void getDataMap(String path){
		/*subpropertyOf.readFromeFileAndWrite2File();
		dataMap = subpropertyOf.dataMap;*/
		mapTest.readTBox(path);
		tboxtransitiveOfMap = mapTest.tboxtransitiveOfMap;

	}
	
	public Map<String,ArrayList<String>> Generation(Map<String,ArrayList<String>> aboxMap,String pathABox,String path){
		
		getDataMap(path);
	
		//对于具有transitive的属性
		for(String s:tboxtransitiveOfMap){
			int sizebefore = 0;
			int sizeafter = 10;
			ArrayList<String>aboxMapnew = new ArrayList<String>();
			ArrayList<String> writeall=new ArrayList<String>();
			
			while(sizebefore!=sizeafter){
				sizebefore = aboxMapnew.size();
			//从aboxmap中获得该属性的信息，aboxdata中是<主语 宾语，主语 宾语，...>
			if(aboxMap.containsKey(s)){
				aboxdata = aboxMap.get(s);
				//对于每一个“主语1 宾语1”，拆分后再从该aboxdata中找有没有“主语2=宾语1”的情况，有的话就加入
				for(String tra:aboxdata){					
					String[] str = tra.split(" ");
					String transdomain = str[0];
					String transmid1 = str[1];
					
					for(String trb:aboxdata){
						
						String[] strb = trb.split(" ");
						String transmid2 = strb[0];
						String transrange = strb[1];
						
						if(transmid1.equals(transmid2) && !(aboxMapnew.contains(transdomain+" "+transrange))){
							aboxMapnew.add(transdomain+" "+transrange);
							writeall.add(transdomain+" "+s+" "+transrange+" .");
							continue;
						}
						else{continue;}
						
					}										

				}
				
				
			}
			sizeafter = aboxMapnew.size();	
			}
			for(String stradd:aboxMapnew){
				aboxMap.get(s).add(stradd);
			}
			writeFile(writeall,pathABox);
		}
		
		
		
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
