package fdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EquivalentClassinter {
	
	
	public void readfile(String path, String pathnew){
		
		File file=new File(path);
		if(!file.exists()||file.isDirectory())
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		BufferedReader reader = null;
		
		ArrayList<String> write=new ArrayList<String>();
		
		String subclassof= "<http://www.w3.org/2000/01/rdf-schema#subClassOf>";
		String somevaluesfrom="<http://www.w3.org/2002/07/owl#someValuesFrom>";
		String thing="<http://www.w3.org/2002/07/owl#Thing>";
		String type="<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
		String classs="<http://www.w3.org/2002/07/owl#Class>";
		String subpropertyof="<http://www.w3.org/2000/01/rdf-schema#subPropertyOf>";
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String strclass = null;
			while((strclass = reader.readLine()) != null) {
				
				String[] strstrclass=strclass.split(" ");
				
				//先处理第一种情况#equivalentClass（其中又包括四中小情况：intersectionOf、one of、unionof、单纯的等价一个不小于组成的集合）
				if(strstrclass[1].contains("#equivalentClass>") && strstrclass[2].startsWith("_:")){
					
					
					String str = null;
					//这个循环要限定在equivalentClass _:node 之内，每一个equivalentClass处理完就应该跳出，做整个文本的按行读入操作
					while((str = reader.readLine())!=null){
						
						String[] strstr=str.split(" ");
						
						//先处理第一种小情况，intersectionOf
						if(strstr[1].contains("#intersectionOf>") && strstr[2].startsWith("_:")){
							
							String stra = null;
							//这个循环要限定在intersectionOf之内，
							while((stra=reader.readLine())!=null){
								
								String[] strstra=stra.split(" ");
								//first 后边直接接class的情况
								if(strstra[1].contains("#first>") && (!strstra[2].startsWith("_:"))){
									write.add(strstrclass[0]+" "+subclassof+" "+strstra[2]+" ."); 
									//为了满足上边的while循环的判断
									String stoptest=reader.readLine();
									//结束intersection的的判断
									if(stoptest.contains("#nil>")){
										break;
									}
									else{continue;}
									
									
								}//first 后边接存在量词、全称量词、基数等情况
								else if (strstra[1].contains("#first>") && strstra[2].startsWith("_:")){
									write.add(strstrclass[0]+" "+subclassof+" "+strstra[2]+" .");
									
									String strb=null;
									while((strb=reader.readLine())!=null && (!strb.contains("#rest>"))){
										//将下边两种情况改写成存在P.T的形式，其余情况现在不做考虑，直接复制过去
										if(strb.contains("#someValuesFrom>") || strb.contains("#minCardinality>")){
											String[] strstrb=strb.split(" ");
											write.add(strstrb[0]+" "+somevaluesfrom+" "+thing+" .");
										}
										else{
											write.add(strb);
										}
										
									}
									//结束intersection的的判断
									if(strb.contains("#nil>")){
										break;
									}
									else{continue;}
									
								}
							}//此时结束了intersectionOf的while循环，break是为了跳出equivalentClass循环
							break;
							
						}

						//因为数据中只有下边这一种情况，所以先按照这种形式处理，如果还有别的形式再添加或者更改
//						    <http://uob.iodt.ibm.com/univ-bench-dl.owl#PeopleWithManyHobbies> <http://www.w3.org/2002/07/owl#equivalentClass> _:node1bth2g0tjx80 .
//							_:node1bth2g0tjx80 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Restriction> .
//							_:node1bth2g0tjx80 <http://www.w3.org/2002/07/owl#onProperty> <http://uob.iodt.ibm.com/univ-bench-dl.owl#like> .
//							_:node1bth2g0tjx80 <http://www.w3.org/2002/07/owl#minCardinality> "3"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> .
						
						//第二种小情况，单纯的等价一个“不小于”组成的集合
						if(strstr[1].contains("type") && (strstr[2].contains("#Restriction>"))){
							write.add(strstrclass[0]+" "+subclassof+" "+strstrclass[2]+" .");
							write.add(str);
							
							String stra=reader.readLine();
							String[] strstra=stra.split(" ");
							if(strstra[1].contains("#onProperty>") && (!strstra[2].startsWith("_:"))){
								write.add(stra);
								
								String strb=reader.readLine();
								String[] strstrb=strb.split(" ");
								if((strstrb[1].contains("#someValuesFrom>") || strstrb[1].contains("#minCardinality>"))&&(!strstrb[2].startsWith("_:"))){
									write.add(strstrb[0]+" "+somevaluesfrom+" "+thing+" .");
								}
							}//跳出equivalentClass循环
							break;
							
						}
						
						
						//处理第3种小情况，unionOf，这种情况现在不做处理原数据存储，但是为了保证新的数据不错位，还是要进行一下判断
						if(strstr[1].contains("#unionOf>") && strstr[2].startsWith("_:")){
							write.add(strclass);
							//为了保证原数据存储，equivalentClass和unionOf中间的那条type class信息也要存储。
							write.add(strstr[0]+" "+type+" "+classs+" .");
							write.add(str);
							
							String stra=null;
							//如果读入的数据包括rest和nil说明到了最后一行了，就退出循环了，不然就把每行数据直接复制到新文件中
							while((stra=reader.readLine())!=null ){
								if(!(stra.contains("#rest>") && stra.contains("#nil>"))){
									write.add(stra);
								}
								else{
									write.add(stra);
									//这个是结束上边这个while循环
									break;
								}
							}
							
							break;
						}
						
						
						//处理第4种小情况，oneOf，这种情况现在不做处理原数据存储，但是为了保证新的数据不错位，还是要进行一下判断
						if(strstr[1].contains("#oneOf>") && strstr[2].startsWith("_:")){
							write.add(strclass);
							//为了保证原数据存储，equivalentClass和unionOf中间的那条type class信息也要存储。
							write.add(strstr[0]+" "+type+" "+classs+" .");
							write.add(str);
							
							String stra=null;
							//如果读入的数据包括rest和nil说明到了最后一行了，就退出循环了，不然就把每行数据直接复制到新文件中
							//问题：如果把结束条件的判断放在while循环中，最后判断用的这个读入并没有写入，所以改成if-else
							while((stra=reader.readLine())!=null ){
								if(!(stra.contains("#rest>") && stra.contains("#nil>"))){
									write.add(stra);
								}
								else{
									write.add(stra);
									//这个是结束上边这个while循环
									break;
								}
								
							}
							
							break;
						}
						
					}
					
					
				}
				//这里可以写除了equivalentClass _:node的其他情况，比如equivalentProperty、somevaluesfrom C等
				
				//处理subclassof (somevaluesfrom\hasvalue\allvaluesfrom\min\maxcardinality C等)情况
				else if(strstrclass[1].contains("#subClassOf>") && strstrclass[2].startsWith("_:")){
					write.add(strclass);
					String stra = null;
					while((stra=reader.readLine())!=null){
						String[] strstra=stra.split(" ");
						//while循环结束的条件是读入的内容包括somevaluesfrom\hasvalue\allvaluesfrom\min\maxcardinality\complementOf 且后边是具体的类而不是_:node.
						//根据现有的数据uobm和lubm可以满足，如果subclassof于somevaluesfrom（-c），根据数据猜测是结束应该是complementOf后接一个具体的类，因为现在没有这样的数据，所以先进行这样的处理
						if((strstra[1].contains("#someValuesFrom>")||strstra[1].contains("#minCardinality>"))&&(!strstra[2].startsWith("_:"))){
							write.add(strstra[0]+" "+somevaluesfrom+" "+thing+" .");
							break;
						}
						else if((strstra[1].contains("#allValuesFrom>")||strstra[1].contains("#maxCardinality>")||strstra[1].contains("#hasValue>")||strstra[1].contains("#complementOf>"))&&(!strstra[2].startsWith("_:"))){
							write.add(stra);
							break;
						}
						else{
							write.add(stra);
						}
					}
					
				}
				
				
				//在uobm数据中就存在一条<http://uob.iodt.ibm.com/univ-bench-dl.owl#like> <http://www.w3.org/2002/07/owl#equivalentProperty> <http://uob.iodt.ibm.com/univ-bench-dl.owl#love> .
				//将equivalentproperty改成两个subpropertyof
				else if(strstrclass[1].contains("#equivalentProperty>") && (!strstrclass[2].startsWith("_:"))){
					write.add(strstrclass[0]+" "+subpropertyof+" "+strstrclass[2]+" .");
					write.add(strstrclass[2]+" "+subpropertyof+" "+strstrclass[0]+" .");
				}
				
				
				//如果不进行处理，就应该直接复制
				else{
					write.add(strclass);
				}
				
				
			}
			writeFile(write,pathnew);
			
		}catch (FileNotFoundException e) {
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
	
	//将改写过后的内容重新写入新的文件
	public void writeFile(ArrayList<String> writetboxnew,String pathnew){
		
		File file=new File(pathnew);
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
			for(String s : writetboxnew){
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
