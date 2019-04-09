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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//import fdx.MapTest;

public class GenerateClass {
	
	/*
	 * 分类
	 */
	Classification classification = new Classification();
	Map<String, String> dataMap = null;
	
	MapTest mapTest = new MapTest();
	Map<String,ArrayList<String>> aboxMap=null;
	
	ArrayList<String> writeall =new ArrayList<String>();
	String type = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
	
	
	/*public static void main(String[] args){
		
		
		GenerateClass generateClass = new GenerateClass();
		generateClass.readFromFile("D:\\workspace\\Ukmodel\\data\\resultNew");
				
	}*/
	
	/**
	 * 获取取得的数据
	 */
	public void getDataMap(String path){
		//classification.readClassifyFile("D:\\workspace\\Ukmodel\\ClassTree.txt");
		classification.readClassifyFile("ClassTree.txt",path);
		dataMap = classification.dataMap;
		//System.out.println("MCH");
		//System.out.println(dataMap);
		
		
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
		while(dataMap.get(son)!=null){
			father.add(dataMap.get(son));
			son=dataMap.get(son);
		}
//		father.add(son);
		return father;
	}
	
	public void getaboxMap(String pathABox){
		//classification.readClassifyFile("D:\\workspace\\Ukmodel\\ClassTree.txt");
		mapTest.readABox(pathABox);
		aboxMap = mapTest.aboxMap;
	}
	
	/*
	 * ABox数据存放在aboxMap中，若数据aboxMap中包含dataMap的子类，则将子类的aboxMap中的value值插值进dataMap中父类的aboxMap数据里
	 */
	
	public Map<String,ArrayList<String>> Generation(String pathABox,String path) throws IOException{
		getaboxMap(pathABox);
		System.out.println("size  "+aboxMap.size());
		/*for(String s : aboxMap.keySet()){
			System.out.println("key "+s);
			//System.out.println("value "+aboxMap.get(s));
			Iterator<String> it= aboxMap.get(s).iterator();
			for(;it.hasNext();){
				System.out.println(it.next());
			}
			}*/
		
		getDataMap(path);
		
		String str=null;int in=0;String str2=null;
		//String path = "lubm-ex-10-2017.nt";
		Restriction r = new Restriction(str,in,str2);
		
		for(String s:dataMap.keySet()){
			//System.out.println("s "+s+aboxMap.get(s));
			//father是s的父亲，父亲的父亲。。。。（在生成的类树的转换中找，dataMap）
			ArrayList<String> father = getFather(s);

			//System.out.println("father "+father);
			if(aboxMap.containsKey(s)){
			
			while(father.size()>0&&!(father.get(0).equals("Thing"))){
				
				r.writeFile(s, father.get(0), path);
					
					for(Iterator<String> it=aboxMap.get(s).iterator();it.hasNext();){
						if(aboxMap.containsKey(father.get(0))){
							String itnext = it.next();
							if(!aboxMap.get(father.get(0)).contains(itnext)){
								aboxMap.get(father.get(0)).add(itnext);
								String add = itnext+" "+type+" "+father.get(0)+" .";
								writeall.add(add);

							}
						}
						else{
							aboxMap.put(father.get(0), new ArrayList<String>());
							String itnext = it.next();
							aboxMap.get(father.get(0)).add(itnext);
							String add = itnext+" "+type+" "+father.get(0)+" .";
							
							writeall.add(add);

						}
					}
					father.remove(0);	
				}
				
				
			}
			
		}
		writeFile(writeall,pathABox);
		Removal re = new Removal();
		re.cancelRepeat(path);
		
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
	

		

	/**
	 * 将文件中读取的字符输入到新文件中
	 * @param input
	 */
	//Set<String> a;
	/*public void write2File(String input){
		//String path="D:\\workspace\\Ukmodel\\data\\University0_14.nt";
		String path="data/University0_14.nt";
		File file=new File(path);
		
		 * 如果文件不存在创建文件
		 
		if(!file.exists()){
		   try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		Set<String> s = readABox();
		int size=s.size();
		BufferedWriter writer = null;
		try{
			writer= new BufferedWriter(new FileWriter(file,true));
			
			 * 举例A(a)
			 * 取<.../a> <.../#A>中的类名A 且A包含于B
			
			String output = input;
			String[] temp = input.split("#");
			String classNameString = temp[1].replace(">", "").trim();
			//System.out.println("classname= "+classNameString);
			String middleString = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
			
			 * 获取父类保存
			 
			getDataMap();
			ArrayList<String> father = getFather(classNameString);
			String[] outTemp = output.split("\t");
			while(father.size()>0&&!(father.get(0).equals("Thing"))){
				//writer.write(output+"\r\n");
				//用父类名代替子类名输出<.../a> <.../#type> <.../#B>
				outTemp[1] = outTemp[1].replace(classNameString, father.get(0));
				//System.out.println("outTemp[1]= "+outTemp[1]);
				
				 * University以空格为分隔符
				 				
				output = outTemp[0]+" "+middleString+" "+outTemp[1]+" .";
				s.add(output);
			
				if(size!=s.size()){	
					//a.add(output);
					writer.write(output+"\r\n");
					size=s.size();
				}
				//writer.write(output+"\r\n");
				//outTemp[1] = outTemp[1].replace(classNameString, father.get(0));
				classNameString=father.get(0);
				father.remove(0);
			}
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
	
	
	
	 * 读取的是resultNew里面的数据：是从ABox中摘取的所有满足type关系的一元类包含A(a)
	 * 注：以table键为分隔
	 * 举例：<http://www.Department14.University0.edu/GraduateCourse50>	<http://swat.cse.lehigh.edu/onto/univ-bench.owl#GraduateCourse>
	
		public void readFromFile(String filePath){
		String path = filePath;
		File file=new File(path);
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
			while ((str = reader.readLine()) != null) {
				//System.out.println(str);
				if(str.trim()==""||"".equals(str.trim())){
					continue;
				}else{
					//System.out.println(str.charAt(0));
					if(str.charAt(0)=='<'){
						//System.out.println(str);
						write2File(str);//将读取的行进行写入
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
		
		public Set<String> readABox(){
			//String path = "D:\\workspace\\Ukmodel\\data\\University0_14.nt";
			String path = "data/University0_14.nt";
			Set<String> s = new HashSet();
			File file=new File(path);
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
				while ((str = reader.readLine()) != null) {
					//System.out.println(str);
					s.add(str);				
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
			//System.out.println("ABox size is: "+s.size());
			return s;
		}*/
}
