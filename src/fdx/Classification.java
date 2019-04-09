package fdx;

import java.util.*;
import java.io.*;

import javax.xml.transform.Templates;

import com.clarkparsia.sparqlowl.parser.antlr.SparqlOwlParser.dataPropertyIRI_return;

public class Classification {
	/*
	 * 存放所有数据(类之间的从属包含关系)
	 * key：为子类
	 * value：父类
	 */
	Map<String, String> dataMap =  new HashMap<String, String>();
	/*
	 *记录当前层次的父节点
	 */
	static String currentRoot ="";
	static String currentFour="";
	static String currentSeven="";
	static String currentTen="";
	static String currentThirty="";
	static String currentSixteen="";
	/*
	 * 存放所有数据
	 */
	/*public static void main(String[] args){
		Classification classification = new Classification();
		classification.readClassifyFile("D:\\workspace\\Ukmodel\\ClassTree.txt");
		classification.printData();
	}*/
	/**
	 * 打印数据
	 */
	public void printData(){
		//System.out.println("进入打印函数");
		//System.out.println(dataMap.size());
		Set<String> key = dataMap.keySet();
		if(key==null||"".equals(key)){
			System.out.println("没有元素");
		}else{
			for(String s:key){
				System.out.println(s+":"+dataMap.get(s));
			}
		}
		
	}
	/**
	 * 向map中添加字符串
	 * @param string：添加的字符串
	 * @return
	 */
	public void addElement(String string,String tpath){
		//System.out.println("addElement");
		//System.out.println(string);
		
		//由于类树直接调用的端口，处理出来是简写的关键字，所以我们需要对其进行补全处理，补全的时候用到tbox，从里边找到关键字对应的全称
		File file=new File(tpath);
		if(!file.exists()||file.isDirectory())
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		BufferedReader reader = null;
		
		
		
		if(string.length()!=1){
						
			//注意，是string类型的num，所以如果传入的数字是10，13或者16，num+=string.charAt(i)也是可以的，表示的不是数字1+0或者1+3，而是字符串的连接，也是10，13
			String num="";
			String stringnew=null;
			
			//uobm数据中对于oneof的情况放在父类的后边，而不是树结构，我们先处理树结构，对于oneof以及type的情况后续考虑清楚在添加。
			//例如 univ-bench-dl:Engineering - (univ-bench-dl:Civil_Engineering, univ-bench-dl:Petroleuml_Engineering,...) 
			if(string.contains(" - ")){
				String[] stringsplit=string.split(" - ");
				stringnew=stringsplit[0];
			}else{
				stringnew=string;
			}
			String[] temp = stringnew.split(":");
			for(int i=0;i<string.length();i++){
				//charAt(i)下标i对应的字符
				if(string.charAt(i)>='0'&&string.charAt(i)<'9'){
					num+=string.charAt(i);
				}else{
					break;
				}
			}

			//System.out.println(num);
			
			//由于接口生成的类树第一层是owl:Thing，在TBox中没有原始前缀，所以我们对其不改动
			if(num.trim()=="1"||"1".equals(num.trim())){
				//System.out.println("jinru111111111");
				dataMap.put(temp[1],null);
				currentRoot=temp[1];
			
			//对于类树其他层的信息，根据关键字在TBox中找到它的全称形式，主要适用于<...#关键字>，因为匹配的时候找的是“#关键字>”的形式。	
			}else if(num.trim()=="4"||"4".equals(num.trim())){
				//System.out.println("jinru2222222222");
				
				String tempnew="0";
				try {
					reader = new BufferedReader(new FileReader(file));
					String str = null;
					while ((str = reader.readLine()) != null ) {
						String[] subStr = str.split(" ");
						if(subStr[0].contains("#"+temp[1]+">") ){
						tempnew = subStr[0];
						dataMap.put(tempnew,currentRoot);
						currentFour=tempnew;
						break;
						}
					}
				
				}				
			   catch (FileNotFoundException e) {
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
				
				
			}else if(num.trim()=="7"||"7".equals(num.trim())){
				//System.out.println("jinru333333333");
				
				String tempnew="0";
				try {
					reader = new BufferedReader(new FileReader(file));
					String str = null;
					while ((str = reader.readLine()) != null ) {
						String[] subStr = str.split(" ");
						if(subStr[0].contains("#"+temp[1]+">") ){
						tempnew = subStr[0];
						dataMap.put(tempnew,currentFour);
						currentSeven=tempnew;
						break;
						}
					}
				
				}				
			   catch (FileNotFoundException e) {
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

				
			}else if(num.trim()=="10"||"10".equals(num.trim())){
				//System.out.println("jinru444444444");
				
				String tempnew="0";
				try {
					reader = new BufferedReader(new FileReader(file));
					String str = null;
					while ((str = reader.readLine()) != null ) {
						String[] subStr = str.split(" ");
						if(subStr[0].contains("#"+temp[1]+">") ){
						tempnew = subStr[0];
						dataMap.put(tempnew,currentSeven);
						currentTen=tempnew;
						break;
						}
					}
				
				}				
			   catch (FileNotFoundException e) {
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
				
			}else if(num.trim()=="13"||"13".equals(num.trim())){
				//System.out.println("jinru555555555");
				
				String tempnew="0";
				try {
					reader = new BufferedReader(new FileReader(file));
					String str = null;
					while ((str = reader.readLine()) != null ) {
						String[] subStr = str.split(" ");
						if(subStr[0].contains("#"+temp[1]+">") ){
						tempnew = subStr[0];
						dataMap.put(tempnew,currentTen);
						currentThirty=tempnew;
						break;
						}
					}
				
				}				
			   catch (FileNotFoundException e) {
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
				

			}else if(num.trim()=="16"||"16".equals(num.trim())){
				//System.out.println("jinru6666666666");
				
				String tempnew="0";
				try {
					reader = new BufferedReader(new FileReader(file));
					String str = null;
					while ((str = reader.readLine()) != null ) {
						String[] subStr = str.split(" ");
						if(subStr[0].contains("#"+temp[1]+">")){
						tempnew = subStr[0];
						dataMap.put(tempnew,currentThirty);
						currentSixteen=tempnew;
						break;
						}
					}
				
				}				
			   catch (FileNotFoundException e) {
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
		}
	}
	/**
	 * 读取文件中的内容并标号
	 */
	public void readClassifyFile(String filePath,String tpath){
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
				String str2= str.replaceAll(" ", "1");
				int num=0;
				for(int i =0;i<str2.length();i++){
					if(str2.charAt(i)=='1'){
						num++;
					}else{
						break;
					}
				}
				//str.trim()去掉字符串首尾的空格
				
				String temp = num+str.trim();
				addElement(temp.trim(),tpath);
				//System.out.println(str);
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
}
