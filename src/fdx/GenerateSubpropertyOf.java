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
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.Templates;

public class GenerateSubpropertyOf {

	
	/*
	 * dataMap存放subpropertyOf的二元关系？
	 * key：子关系
	 * value：父关系
	 */
	/*Map<String, String> dataMap =  new HashMap<String, String>();
	SubpropertyOf subpropertyOf = new SubpropertyOf();*/
	
	MapTest mapTest = new MapTest();
	Map<String,String> tboxsubPropertyOfMap=new HashMap<String,String>();
	ArrayList<String> writeall = new ArrayList<String>();
	

	/**
	 * 获取取得的数据
	 */
	public void getDataMap(String path){
		/*subpropertyOf.readFromeFileAndWrite2File();
		dataMap = subpropertyOf.dataMap;*/
		mapTest.readTBox(path);
		tboxsubPropertyOfMap = mapTest.tboxsubPropertyOfMap;
	}
	/**
	 * 测试返回的该类father是否正确
	 */
	public void checkFatherArrayList(String son){
		ArrayList<String> father=getFather(son);
		for(String s:father){
			System.out.println("son:"+son+"  father:"+s);
		}
	}
	/**
	 * 根据抽取出来的类，求解所有父类并返回
	 * @param son:子类名
	 * @return
	 */
	public ArrayList<String> getFather(String son){
		ArrayList<String> father = new ArrayList<String>();
		boolean flag=false;
		while(tboxsubPropertyOfMap.containsKey(son)){
            //为了处理循环问题，加入一个判断，如果father已经在列表中，就表示已经出现循环，跳出while
			for(String fa:father){
            	if(fa==(tboxsubPropertyOfMap.get(son))){
            		flag=true;
        			break;       			
            	}else{
            		flag=false;
            		continue;
            	}
            		
            }//说明father列表中没有
			if(flag==false){
				father.add(tboxsubPropertyOfMap.get(son));
				son=tboxsubPropertyOfMap.get(son);
			}//说明已经存在，就直接结束
			else{
				break;
			}
				
			
		}
		return father;
	}
	
	
	/*public void getaboxMap(){
		//classification.readClassifyFile("D:\\workspace\\Ukmodel\\ClassTree.txt");
		aboxMap = generateClass.aboxMap;
		System.out.println("size = "+aboxMap.size());
	}*/
	
	/*
	 * ABox数据存放在aboxMap中，若数据aboxMap中包含dataMap的子类，则将子类的aboxMap中的value值插值进dataMap中父类的aboxMap数据里
	 */
	
	public Map<String,ArrayList<String>> Generation(Map<String,ArrayList<String>> aboxMap,String pathABox,String path){
		//getaboxMap();
		System.out.println("GenerateClass size = "+aboxMap.size());

		
		getDataMap(path);
		for(String s:tboxsubPropertyOfMap.keySet()){
			//System.out.println("s "+s+aboxMap.get(s));
			//father数组包含了s的父亲，父亲的父亲，。。。。
			ArrayList<String> father = getFather(s);
			//System.out.println("father "+father);
			
			//if判断：TBox中若有包含规则，但实际ABox中没有实例数据则忽略
			if(aboxMap.containsKey(s)){
			while(father.size()>0){
					for(Iterator<String> it=aboxMap.get(s).iterator();it.hasNext();){
						//如果aboxMap中有s父亲这个关键字，并且他的value值中没有s的这个值，就要在他的value中加入。新产生的三元组加入本地abox
						if(aboxMap.containsKey(father.get(0))){
							String itnext = it.next();
							if(!aboxMap.get(father.get(0)).contains(itnext)){
								aboxMap.get(father.get(0)).add(itnext);
								
								String[] strSpace = itnext.split(" ");
								String add = strSpace[0]+" "+father.get(0)+" "+strSpace[1]+" .";
								writeall.add(add);
							}
						}
						//如果aboxMap中没有s父亲这个关键字，则先在aboxMap中加入他，然后再将s的值加入他的value队列。新产生的三元组加入本地abox
						else{
							aboxMap.put(father.get(0), new ArrayList<String>());
							String itnext = it.next();
							aboxMap.get(father.get(0)).add(itnext);
							
							String[] strSpace = itnext.split(" ");
							String add = strSpace[0]+" "+father.get(0)+" "+strSpace[1]+" .";
							writeall.add(add);
						}
					}
					father.remove(0);	
				}
				
				
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
	/*public void readFromFileAndWrite2File(){
		
		 * 获取SubPropertyOf中的数据保存
		 
		getDataMap();
		fileIn=new File(pathIn);
		fileOut=new File(pathOut);
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
			reader = new BufferedReader(new FileReader(fileIn));
			writer= new BufferedWriter(new FileWriter(fileOut));
			String str = null;
			while ((str = reader.readLine()) != null) {
				//System.out.println(str);
				
				String input=str;
				String output=str;
				String[] temp = input.split(" ");
				String[] sonTemp= temp[1].split("#");
				String son = sonTemp[1].replace(">", "").trim();
				//System.out.println(son);
				ArrayList<String> father = getFather(son);
				//System.out.println(father.size());
				if(temp.length==4){
					if(temp[0].charAt(0)=='<'){
//						System.out.println(temp[0].charAt(0));
						String[] flag = temp[0].split("<");
						//System.out.println("1"+output);
						writer.write("10"+output+"\r\n");
						//System.out.println("1111111");
						while(father.size()>0){
							//System.out.println(output);
							son=father.get(0);
							//System.out.println(son);
							output = temp[0]+" "+sonTemp[0]+"#"+son+">"+" "+temp[2]+" "+temp[3];
							//System.out.println("0"+output);
							writer.write("00"+output+"\r\n");
							father.remove(0);
							}
						}else {
							
							if(temp[0].charAt(0)=='1'){
								writer.write(output+"\r\n");
							}
							if(temp[0].charAt(0)=='0'){
								temp[0]=temp[0].replaceFirst("0", "1");
								writer.write(temp[0]+" "+temp[1]+" "+temp[2]+" "+temp[3]+"\r\n");
								//System.out.println(temp[0]+" "+temp[1]+" "+temp[2]+" "+temp[3]+"\r\n");
								//System.exit(0);
								while(father.size()>0){
									//System.out.println(output);
									son=father.get(0);
									output = temp[0]+" "+sonTemp[0]+"#"+son+">"+" "+temp[2]+" "+temp[3];
									//System.out.println(output);
									writer.write("00"+output+"\r\n");
									father.remove(0);
									}
								}
						}
				}
			}				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				reader.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}*/
}
