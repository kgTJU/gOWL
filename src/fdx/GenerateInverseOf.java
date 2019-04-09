package fdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.processing.Filer;

import org.semanticweb.owlapi.CreateValuePartition;

public class GenerateInverseOf {
	/*
	 * dataMap存放InverseOf的逆关系？
	 * key：互为逆关系A
	 * value：互为逆关系B
	 */
	/*Map<String, String> dataMap =  new HashMap<String, String>();
	InverseOf inverseOf = new InverseOf();*/
	
	MapTest mapTest = new MapTest();
	Map<String,String> tboxinverseOfMap=new HashMap<String,String>();
	ArrayList<String> writeall = new ArrayList<String>();
	/*
	 * 
	 */
	//String pathOut="D:\\workspace\\Ukmodel\\data\\resultFile\\inverse_output_University0_14.nt";
	/*String pathOut="data/resultFile/inverse_output_University0_14.nt";
	static File fileOut;
	BufferedWriter writer;*/
	/*
	 * 
	 */
	//String pathIn = "D:\\workspace\\Ukmodel\\data\\resultFile\\output_University0_14.nt";
	/*String pathIn = "data/resultFile/output_University0_14.nt";
	static File fileIn;
	BufferedReader reader;*/
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		GenerateInverseOf generateInverseOf = new GenerateInverseOf();
		generateInverseOf.readFromFileAndWrite2File();
		System.out.println(fileOut.length()+"-"+fileIn.length()+"="+(fileOut.length()-fileIn.length()));
	}*/
	/**
	 * 获取取得的数据
	 */
	public void getDataMap(String path){
		/*inverseOf.readFromFileAndWrite2File();
		dataMap = inverseOf.dataMap;*/
		mapTest.readTBox(path);
		tboxinverseOfMap = mapTest.tboxinverseOfMap;
	}
	/**
	 * 根据抽取出来的类，求解所有父类并返回
	 * @param son:子类名
	 * @return
	 */
	public String getInverse(String son){
		String father="";
		if(tboxinverseOfMap.containsKey(son)){
			father = tboxinverseOfMap.get(son);
		}
		return father;
	}
	
	public Map<String,ArrayList<String>> Generation(Map<String,ArrayList<String>> aboxMap,String pathABox,String path){
		//getaboxMap();
		//System.out.println("GenerateSubpropertyOf size = "+aboxMap.size());
		/*for(String s : aboxMap.keySet()){
			System.out.println("key "+s);
			//System.out.println("value "+aboxMap.get(s));
			Iterator<String> it= aboxMap.get(s).iterator();
			for(;it.hasNext();){
				System.out.println(it.next());
			}
			}*/
		
		getDataMap(path);
		for(String s:tboxinverseOfMap.keySet()){
			//System.out.println("s "+s+aboxMap.get(s));
			String father = getInverse(s);
			//System.out.println("father "+father);
			if(aboxMap.containsKey(s)){
					for(Iterator<String> it=aboxMap.get(s).iterator();it.hasNext();){
						if(aboxMap.containsKey(father)){
							String itnext = it.next();
							String[] strSpace = itnext.split(" ");
							String itnextNew = strSpace[1]+" "+strSpace[0];
							if(!aboxMap.get(father).contains(itnextNew)){
								aboxMap.get(father).add(itnextNew);
								
								String add = strSpace[1]+" "+father+" "+strSpace[0]+" .";
								writeall.add(add);
							}
						}
						else{
							aboxMap.put(father, new ArrayList<String>());
							String itnext = it.next();
							String[] strSpace = itnext.split(" ");
							String itnextNew = strSpace[1]+" "+strSpace[0];
							aboxMap.get(father).add(itnextNew);
							
							String add = strSpace[1]+" "+father+" "+strSpace[0]+" .";
							writeall.add(add);
						}
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
		fileIn= new File(pathIn);
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
			String str;
			while ((str = reader.readLine())!=null) {
				//writer.write(str+"\r\n");
				String input =str;
				String output = input;
				String[] temp = input.split(" ");
				String[] inverseTemp= temp[1].split("#");
				String ship = inverseTemp[1].replace(">", "").trim();
				String shipInverse = getInverse(ship);
				if(temp[0].charAt(1)=='1'){
					writer.write(output+"\r\n");
				}else{
					String[] flag = temp[0].split("<");

					if(flag[0].charAt(1)=='1'){
						output = flag[0]+"<"+flag[1]+" "+temp[1]+" "+temp[2]+" "+temp[3];
						writer.write(output+"\r\n");
					}else{
						flag[0]=flag[0].charAt(0)+"1";
						output = flag[0]+"<"+flag[1]+" "+temp[1]+" "+temp[2]+" "+temp[3];
						writer.write(output+"\r\n");
						//System.out.println(output);
						if(shipInverse!=null&&!"".equals(shipInverse)){
							String[] flag2 = temp[2].split("<");
							flag2[0]="01";
							temp[2]=flag2[0]+"<"+flag2[1];
							String []flag3=temp[0].split("<");
							temp[0]="<"+flag3[1];
							output = temp[2]+" "+inverseTemp[0]+"#"+shipInverse+">"+" "+temp[0]+" "+temp[3];
							writer.write(output+"\r\n");
							//System.out.println(output);
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
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
}
