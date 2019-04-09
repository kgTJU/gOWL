package fdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Restriction {
		private int type;
		private String domain;
		private String range;
//		private int acessNumber=0;
//		StringBuilder acess = new StringBuilder();
		 public Restriction(String domain,int type,String range){
			this.domain=domain;
			this.type=type; 
			this.range=range;
		 }
		 public int getType(){
			 return type;
		 }
		 public String getDomain(){
			 return domain;
		 }
		 public String getRange(){
			 return range;
		 }
		 
		 public ArrayList<Restriction> readFile(String filePath){
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
				ArrayList<Restriction> restrictionListResult;
				try {
					reader = new BufferedReader(new FileReader(file));
					String str = null;
					String domain = null;
					String range = null;
					int type = 0;
					int count=0;
					ArrayList<Restriction> restrictionList = new ArrayList<Restriction>();
					//按行读取T中的内容
					while ((str = reader.readLine()) != null) {
						//count++;
						//System.out.println(count);
						//System.out.println(str);
						
						//按照空格进行分割
						String[] parseStrSpace = str.split(" ");
						//寻找onPropertyOf三元组
						
						if((parseStrSpace[0].startsWith("_"))||(parseStrSpace[2].startsWith("_"))){
//							System.out.println("length="+parseStrSpace.length+parseStrSpace[0]+parseStrSpace[2]);
//							count++;
//							System.out.println(count);
							
							//case1:domain is class;type is 0 or 1;range is property;
							//将subclassof的条件更细化，不是只要有开头不是“_:”结尾是的情况就是subclassof，还可能是equivalentclass
							if(!(parseStrSpace[0].startsWith("_"))&&(parseStrSpace[2].startsWith("_"))&&(parseStrSpace[1].contains("subClassOf"))){
//								System.out.println("length="+parseStrSpace.length+parseStrSpace[0]+parseStrSpace[2]);
//								count++;
//								System.out.println(count);
								//类的URL前缀的长度是48，<http://swat.cse.lehigh.edu/onto/univ-bench.owl#
								//domain=parseStrSpace[0].substring(48,parseStrSpace[0].length()-1);
								//对原来的进行改动，不要去掉前缀，以便对其他数据集也能适用
								  domain=parseStrSpace[0];
								//System.out.println("domain="+domain);
								while ((str = reader.readLine()) != null){
									String[] parseStrSpaceNew = str.split(" ");
									//case1.1:domain is class;type is 0;range is property(即C包含于存在R)
									if((parseStrSpaceNew[1].contains("#onProperty>"))&&!(parseStrSpaceNew[2].startsWith("_"))){
										//在UOBM中这种情况还不能保证是“存在R.T”，所以还要读取下一条内容
										String strnext=reader.readLine();
										String[] strnextspace=strnext.split(" ");
										//判断onproperty的下一条是不是谓词是someValuesFrom并且宾语是thing，是的话符合fdx原本的设计，加入
										if((strnextspace[1].contains("someValuesFrom"))&&(strnextspace[2].contains("Thing"))){
											type=0;
											//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
											range=parseStrSpaceNew[2];
											//System.out.println("range="+range);
											Restriction restriction = new Restriction(domain,type,range);
											restrictionList.add(restriction);
											
											
										}
										//不管能不能加入，只要是过了onproperty就跳出这个循环开始判断下一个
										break;			
									}
									//case1.2:domain is class;type is 1;range is property(即C包含于存在R的逆)
									else if((parseStrSpaceNew[1].contains("#inverseOf>"))&&!(parseStrSpaceNew[2].startsWith("_"))){
										//在UOBM中没有这种情况，但是根据“存在R.T”考虑，所以还要读取下一条内容
										String strnext=reader.readLine();
										String[] strnextspace=strnext.split(" ");
										//判断onproperty的下一条是不是谓词是someValuesFrom并且宾语是thing，是的话符合fdx原本的设计，加入
										if((strnextspace[1].contains("someValuesFrom"))&&(strnextspace[2].contains("Thing"))){
											type=1;
											//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
											range=parseStrSpaceNew[2];
											//System.out.println("range="+range);
											Restriction restriction = new Restriction(domain,type,range);
											restrictionList.add(restriction);				
											
										}
										//不管能不能加入，只要是过了inverseof就跳出这个循环开始判断下一个	
										break;
									}
								}
							}
							//case2.1:定义域是存在量词;type is 2,4 or 5
							else if(parseStrSpace[0].startsWith("_")&&(parseStrSpace[1].equals("<http://www.w3.org/2002/07/owl#onProperty>"))&& !(parseStrSpace[2].startsWith("_"))){
								//domain=parseStrSpace[2].substring(48,parseStrSpace[2].length()-1);
								  domain=parseStrSpace[2];
								  
								  //uobm数据并不是onproperty后的都是somevaluefrom thing，但是我们发现_: <onproperty> <>这种三元组的下一条三元组都是_: <hasvalue/somevaluefrom/allvaluefrom/mincardinality/maxcardinality> class/thing
								  //所以为了适合uobm数据以及更普遍的数据，我们还要判断onproperty的下一条是否符合fdx的原有要求，即多了下边的if判断
								  //注意，我们只是判断了“包含于”左边这个形式要符合要求，因为uobm没有这种数据，这一条限制已经可以。还可能出现“存在R1.T包含于存在R2.T等”，这时候while里边的每个判断也要取出对应的下一条判断是不是somevaluefrom thing。
								  String strnext=reader.readLine();
								  String[] strnextspace=strnext.split(" ");
								  if((strnextspace[1].contains("#someValuesFrom"))&&(strnextspace[2].contains("Thing"))){
									  
									//System.out.println("domain="+domain);
										while ((str = reader.readLine()) != null){
											String[] parseStrSpaceNew = str.split(" ");
											//case2.1.1:(即存在R包含于类C)
											if(parseStrSpaceNew[1].equals("<http://www.w3.org/2000/01/rdf-schema#domain>")&&!(parseStrSpaceNew[2].startsWith("_"))){
												type=2;
												//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
												  range=parseStrSpaceNew[2];
												//System.out.println("range="+range);
												Restriction restriction = new Restriction(domain,type,range);
												restrictionList.add(restriction);
												break;
											}
											//case2.1.2:(即存在R1包含于存在R2)
											else if((parseStrSpaceNew[1].equals("<http://www.w3.org/2002/07/owl#onProperty>"))&& !(parseStrSpaceNew[2].startsWith("_"))){
												type=4;
												//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
												  range=parseStrSpaceNew[2];
												//System.out.println("range="+range);
												Restriction restriction = new Restriction(domain,type,range);
												restrictionList.add(restriction);
												break;
											}
											//case2.1.3:(即存在R1包含于存在R2的逆)
											else if((parseStrSpaceNew[1].equals("<http://www.w3.org/2002/07/owl#inverseOf>"))&&!(parseStrSpaceNew[2].startsWith("_"))){
												type=5;
												//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
												  range=parseStrSpaceNew[2];
												//System.out.println("range="+range);
												Restriction restriction = new Restriction(domain,type,range);
												restrictionList.add(restriction);
												break;
											}
										}
									  
								  }
								  
								
							}
							//case2.2:定义域是存在量词的逆；type is 3,6 or 7
							else if(parseStrSpace[0].startsWith("_")&&(parseStrSpace[1].equals("<http://www.w3.org/2002/07/owl#inverseOf>"))&&!(parseStrSpace[2].startsWith("_"))){
								//domain=parseStrSpace[2].substring(48,parseStrSpace[2].length()-1);
								  domain=parseStrSpace[2];
								 
								  //由于uobm数据中没有涉及到inverseOf，所以这一段代码没有该，如果有这种情况，更具lubm数据的显示，这部分的情况应和2.1一样，判断inverseOf的下一条三元组是否谓词是somevaluefrom宾语是thing。
//								  String strnext=reader.readLine();
//								  String[] strnextspace=strnext.split(" ");
//								  if((strnextspace[1].contains("#someValuesFrom"))&&(strnextspace[2].contains("Thing")))
								  
								  //System.out.println("domain="+domain);
								while ((str = reader.readLine()) != null){
									String[] parseStrSpaceNew = str.split(" ");
									//case2.2.1:(即存在R的逆包含于类C)
									if(parseStrSpaceNew[1].equals("<http://www.w3.org/2000/01/rdf-schema#domain>")&&!(parseStrSpaceNew[2].startsWith("_"))){
										type=3;
										//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
										  range=parseStrSpaceNew[2];
										//System.out.println("range="+range);
										Restriction restriction = new Restriction(domain,type,range);
										restrictionList.add(restriction);
										break;
									}
									//case2.2.2:(即存在R1的逆包含于存在R2)
									else if((parseStrSpaceNew[1].equals("<http://www.w3.org/2002/07/owl#onProperty>"))&& !(parseStrSpaceNew[2].startsWith("_"))){
										type=6;
										//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
										  range=parseStrSpaceNew[2];
										//System.out.println("range="+range);
										Restriction restriction = new Restriction(domain,type,range);
										restrictionList.add(restriction);
										break;
									}
									//case2.2.3:(即存在R1的逆包含于存在R2的逆)
									else if((parseStrSpaceNew[1].equals("<http://www.w3.org/2002/07/owl#inverseOf>"))&&!(parseStrSpaceNew[2].startsWith("_"))){
										type=7;
										//原始代码包含下边这条命令，应该有问题，所以去掉
										//domain=parseStrSpaceNew[2].substring(48,parseStrSpace[2].length()-1);
										//range=parseStrSpaceNew[2].substring(48,parseStrSpaceNew[2].length()-1);
										  range=parseStrSpaceNew[2];
										//System.out.println("range="+range);
										Restriction restriction = new Restriction(domain,type,range);
										restrictionList.add(restriction);
										break;
									}
								}
							}
						}
					}
//					System.out.println("RestrictionList.size="+RestrictionList.size());
//					int c=0;
//					for(Restriction u:RestrictionList){
//						c++;
//						System.out.println(c+"domain="+u.getDomain()+" type="+u.getType()+" range="+u.getRange());
//					}
					restrictionListResult=classifyRestriction(restrictionList,path);
					return restrictionListResult;
					
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
				return null;
				
			}
		 
		 	public ArrayList<Restriction> classifyRestriction(ArrayList<Restriction> restrictionList,String path) throws IOException{
		 		/*for(Restriction u:restrictionList){
					System.out.println("before"+restrictionList.size()+"domain="+u.getDomain()+" type="+u.getType()+" range="+u.getRange());
				}*/
		 		int size;
//		 		size = restrictionList.size();
//	 			System.out.println("size="+size);
		 		do{
		 			size = restrictionList.size();
		 			System.out.println("Before size="+size);
		 			for(int c=0;c<size;c++){
						//System.out.println("Cdomain="+restrictionList.get(c).getDomain()+" Ctype="+restrictionList.get(c).getType()+" Crange="+restrictionList.get(c).getRange());
						//System.out.println("c="+c);
						//System.out.println("type="+RestrictionList.get(c).getType());
		 				switch(restrictionList.get(c).getType()){
						case 0:
							//int tmp=0;(两层循环，跳过自己本身，不能直接写成for(int i=0;i<size&&i!=c;i++))
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
									//tmp++;
									//System.out.println("tmp="+tmp);
									//<http://swat.cse.lehigh.edu/onto/univ-bench.owl#Student> <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person> .
									//System.out.println("hahaha");
									if((restrictionList.get(i).getType()==2) && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
										//System.out.println("hahaha");
										Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),8,restrictionList.get(i).getRange());
										//System.out.println("YES"+"domain: "+restrictionList.get(c).getDomain()+"range: "+restrictionList.get(i).getRange());
										if(testRepeat(restriction,restrictionList)){
											restrictionList.add(restriction);
											//System.out.println("YES"+"domain: "+restrictionList.get(c).getDomain()+"range: "+restrictionList.get(i).getRange());
											writeFile(restrictionList.get(c).getDomain(),restrictionList.get(i).getRange(),path);
											
										}
										continue;
									}
									else if(restrictionList.get(i).getType()==4 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
										Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),0,restrictionList.get(i).getRange());
										if(testRepeat(restriction,restrictionList)){
											restrictionList.add(restriction);
										}
										continue;
									}
									else if(restrictionList.get(i).getType()==5 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
										Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),1,restrictionList.get(i).getRange());
										if(testRepeat(restriction,restrictionList)){
											restrictionList.add(restriction);
										}
										continue;
									}
								}
								
							}
						break;
						case 1:
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
									if(restrictionList.get(i).getType()==3 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
										Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),8,restrictionList.get(i).getRange());
										if(testRepeat(restriction,restrictionList)){
											restrictionList.add(restriction);										
											writeFile(restrictionList.get(c).getDomain(),restrictionList.get(i).getRange(),path);
											
										}
										continue;
									}
									else if(restrictionList.get(i).getType()==6 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
										Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),0,restrictionList.get(i).getRange());
										if(testRepeat(restriction,restrictionList)){
											restrictionList.add(restriction);
										}
										continue;
									}
									else if(restrictionList.get(i).getType()==7 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
										Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),1,restrictionList.get(i).getRange());
										if(testRepeat(restriction,restrictionList)){
											restrictionList.add(restriction);
										}
										continue;
									}
								}
								}
						break;
						case 2:
							for(int i=0;i<size;i++){
								if(i==c)
								continue;
							else{
								if(restrictionList.get(i).getType()==0 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),4,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==1 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),5,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
							}
							}
						break;
						case 3:
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
								if(restrictionList.get(i).getType()==0 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),6,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}								
									continue;
								}
								else if(restrictionList.get(i).getType()==1 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),7,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
							}
							}
						break;
						case 4:
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
								if(restrictionList.get(i).getType()==2 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),2,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==4 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),4,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==5 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),5,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
							}
							}
						break;
						case 5:
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
								if(restrictionList.get(i).getType()==3 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),2,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==6 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),4,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==7 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),5,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
							}
							}
						break;
						case 6:
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
								if(restrictionList.get(i).getType()==2 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),3,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==4 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),6,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==5 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),7,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
							}
							}
						break;
						case 7:
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
								if(restrictionList.get(i).getType()==3 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),3,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==6 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),6,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==7 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),7,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
							}
							}
						break;
						case 8:
							for(int i=0;i<size;i++){
								if(i==c)
									continue;
								else{
								if(restrictionList.get(i).getType()==0 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),0,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
								else if(restrictionList.get(i).getType()==1 && restrictionList.get(c).getRange().equals(restrictionList.get(i).getDomain())){
									Restriction restriction = new Restriction(restrictionList.get(c).getDomain(),1,restrictionList.get(i).getRange());
									if(testRepeat(restriction,restrictionList)){
										restrictionList.add(restriction);
									}
									continue;
								}
							}
							}
						break;
						}
					}
		 		}while(size!=restrictionList.size());
		 		
		 		System.out.println("After RestrictionList.size="+restrictionList.size());
		 		
		 		
		 		Iterator<Restriction> iter = restrictionList.iterator();
		 		while(iter.hasNext()){
		 			Restriction next = iter.next();
		 			System.out.println(next);
		 			}
		 	
		 		
				/*int c=0;
				for(Restriction u:restrictionList){
					c++;
					System.out.println(c+"domain="+u.getDomain()+" type="+u.getType()+" range="+u.getRange());
				}*/
				return restrictionList;
				//RestrictionAssertion(restrictionList);
				
		 	}

		 	//查重：即每次添加Restriction规则之前先检查和现有的有无重复
		 	public boolean testRepeat(Restriction restriction,ArrayList<Restriction> restrictionList){
		 		for(int i=0;i<restrictionList.size();i++){
		 				if(restrictionList.get(i).getDomain().equals(restriction.getDomain()) && restrictionList.get(i).getType()==restriction.getType() && restrictionList.get(i).getRange().equals(restriction.getRange())){
		 					//RestrictionList.remove(j);
		 					return false;
		 				}
		 			}
		 		return true;
		 	}
		 	
		     public void writeFile(String domain,String range,String path){
				//String path="D:\\workspace\\Ukmodel\\lubm-ex-10test.nt";
				//由于改成通用性去掉了上边对前缀的缩写，所以不需要在添加到T时，重新加前缀
		    	//String prefix="<http://swat.cse.lehigh.edu/onto/univ-bench.owl#";
				String subClassOf="<http://www.w3.org/2000/01/rdf-schema#subClassOf>";
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
					//对添加的内容进行更改，不要再添加前缀
					//String output =prefix+domain+"> "+subClassOf+" "+prefix+range+"> .";

					String output =domain+" "+subClassOf+" "+range+" .";
					writer.write(output+"\r\n");
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

