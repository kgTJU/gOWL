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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class GenerateRole {
	
	Map<String, String> dataMapsubclassOf = new HashMap<String,String>();
	Classification classification = new Classification();
	
	
	/**
	 * 获取取得的数据
	 */
	public void getDataMapsubclassOf(String tpath){
		//classification.readClassifyFile("D:\\workspace\\Ukmodel\\ClassTree.txt");
		classification.readClassifyFile("ClassTree.txt",tpath);
		dataMapsubclassOf = classification.dataMap;
		//return dataMap;
	}
	
	/**
	 * 根据抽取出来的类，求解所有父类并返回
	 * @param son:子类名
	 * @return
	 */
	public ArrayList<String> getFathersubclassOf(String son){
		ArrayList<String> father = new ArrayList<String>();
		while(dataMapsubclassOf.get(son)!=null){
			father.add(dataMapsubclassOf.get(son));
			son=dataMapsubclassOf.get(son);
		}
//		father.add(son);
		return father;
	}
	
	  Map<String,String> tboxsubPropertyOfMap=new HashMap<String,String>();
	  MapTest mapTest = new MapTest();
	 
	 public  void getDataMapsubpropertyOf(String path){
		    mapTest.readTBox(path);
			tboxsubPropertyOfMap = mapTest.tboxsubPropertyOfMap;
			//return dataMap;
		}
		
		/**
		 * 根据抽取出来的类，求解所有父类并返回
		 * @param son:子类名
		 * @return
		 */
		public  ArrayList<String> getFathersubpropertyOf(String son){
			ArrayList<String> father = new ArrayList<String>();
			while(tboxsubPropertyOfMap.containsKey(son)){
				father.add(tboxsubPropertyOfMap.get(son));
				son=tboxsubPropertyOfMap.get(son);
			}
			return father;
		}
	 
	  Map<String,String> tboxinverseOfMap=new HashMap<String,String>();
	 
	/*Map<String, String> dataMapsubpropertyOf = new HashMap<String,String>();
	SubpropertyOf subpropertyOf = new SubpropertyOf();*/
	
	/*Map<String, String> dataMapinverseOf =  new HashMap<String,String>();
	InverseOf inverseOf = new InverseOf();*/
	
	public  void getDataMapinverseOf(String path){
		/*inverseOf.readFromFileAndWrite2File();
		dataMapinverseOf = inverseOf.dataMap;*/
		//return dataMap;
		mapTest.readTBox(path);
		tboxinverseOfMap = mapTest.tboxinverseOfMap;
	}
	/**
	 * 根据抽取出来的类，求解所有父类并返回
	 * @param son:子类名
	 * @return
	 */
	public  String getInverse(String son){
		String father="";
		if(tboxinverseOfMap.containsKey(son)){
			father = tboxinverseOfMap.get(son);
		}
		return father;
	}
	
	
	
	/*public Map<String,ArrayList<String>> getDataMap(Map<String,ArrayList<String>> aboxMap){
		return aboxMap;
	}*/
	
	String subsumption = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
	//为了使用全称，本页不需要额外添加classname，因为前边的过程中以及将去掉的前缀都加回来了。
	//更改后本页添加的带*的内容中，*后不再是单纯的一个关键词，而是带有前缀的完整的字符串"<...>"
	//String classname = "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#";
	
	Map<String,ArrayList<String>> aboxMapNew = new HashMap<String,ArrayList<String>>(); 
	
	     public void restrictionAssertion(ArrayList<Restriction> restrictionList,Map<String,ArrayList<String>> aboxMap,String pathABox,String path) throws IOException{
	    	 //getDataMap();
	    	 aboxMapNew.putAll(aboxMap);
	    	 //aboxMapNew: 是Map数据结构（key=Concept时，value=概念的list集合；key=role角色时，value=定义域和值域的list集合且中间以空格隔开）
	    	 System.out.println("aboxMap size ="+aboxMapNew.size());
	    	 int sizeaboxOriginal;
	    	 int size=restrictionList.size();
	    	 //所有含有存在量子的公理所构成的list集合
	    	 
	    	 //String path = "D:\\workspace\\Ukmodel\\data\\resultFile\\result";
	    	 //String path = "data/resultFile/result";
	    	 //String path = "University0_14.nt";
				File file=new File(pathABox);
				if(!file.exists()||file.isDirectory())
					try {
						file.createNewFile();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				/*File fileSize=new File(path);
				if(!fileSize.exists()||fileSize.isDirectory())
					try {
						fileSize.createNewFile();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
				
				
				long filesize=0;
				int count = 0;
				int tag=0;
				getDataMapsubclassOf(path);  //结束后，dataMapsubclassOf存储String类型的hashmap <C子类,C父类>
				getDataMapsubpropertyOf(path);//结束后，tboxsubPropertyOfMap存储String类型的hashmap <R子类,R父类>
				getDataMapinverseOf(path);//结束后，tboxinverseOfMap存储String类型的hashmap <R逆1,R逆2> <R逆2,R逆1>
				
				//下边用到的restrictionList中存储的内容为：（xx，0~8，xx）
				do{
					count++;
					System.out.println("循环 count="+count);
					filesize=file.length();
					sizeaboxOriginal = aboxMapNew.size();
					System.out.println("sizeaboxOriginal="+sizeaboxOriginal);
					System.out.println("filesize="+filesize);
					//System.out.println("tag="+tag);
					System.out.println("size="+restrictionList.size());
					for(int c=0;c<restrictionList.size();c++){
						//System.out.println("domain="+restrictionList.get(c).getDomain()+" type="+restrictionList.get(c).getType()+" range="+restrictionList.get(c).getRange());
						//System.out.println("c="+c);
						//System.out.println("type="+restrictionList.get(c).getType());
						
						//long filesizecbefore=file.length();
						switch(restrictionList.get(c).getType()){
						
						case 0:
							//System.out.println("type="+restrictionList.get(c).getType());
							if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
								//判断aboxMapNew中是否含有key值为公理的定义域的（key，value）
								
								for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
									//it：key值为list定义域，其value值组成it迭代器
									//System.out.println("typenew="+restrictionList.get(c).getType());
									if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
										//判断aboxMapNew中是否含有key值为公理的值域的（key，value）
										String itnext = it.next();
										boolean flag = true;
										//System.out.println("typenew="+restrictionList.get(c).getType());
										for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
											//itNew：key值为list值域，其value值组成itNew迭代器
											String itnextNew = itNew.next();
											String[] strSpaceDomain = itnextNew.split(" ");
											//itNew的每一个元素是由角色的定义域和值域实例组成且中间以空格隔开
											
											//C包含于存在R。 将概念C的所有实例和存在R的定义域相比较
											if(strSpaceDomain[0].equals(itnext)){
												flag = true;
												//System.out.println("typenew="+restrictionList.get(c).getType());
												break;
											}
											else{
												flag = false;
											}
										}
										//概念C的每一个实例依次和存在R的所有定义域相比较：若概念C的实例不在存在R的定义域里则flag=false
										if(!flag){
											//eg。itnext=<http://www.Department0.University0.edu/FullProfessor7>
											String prefix=itnext.substring(0, itnext.length()-1);
											//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
											String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
											
											String add = prefix + "*"+ mchlast+">";
											//add=<http://www.Department0.University0.edu/FullProfessor7*R> 注意，由于改成了全称，此时restrictionList.get(c).getRange()不单单是一个关系的关键词表示，而是完整的<...>
											aboxMapNew.get(restrictionList.get(c).getRange()).add(itnext+" "+add);
											String input = itnext+" "+restrictionList.get(c).getRange()+" "+add+" .";
											//input=<http://www.Department0.University0.edu/FullProfessor7> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#R> <http://www.Department0.University0.edu/FullProfessor7*R> .
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),itnext,add,pathABox);
											//System.out.println("typenew="+restrictionList.get(c).getType());
										}
										
									
								}
									//如果aboxMapNew中没有restrictionList中的值域为key值的（key，value）
									else{
										aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
										String itnext = it.next();
										String prefix=itnext.substring(0, itnext.length()-1);
										//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
										String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
										
										String add = prefix + "*"+mchlast+">";
										aboxMapNew.get(restrictionList.get(c).getRange()).add(itnext+" "+add);
										String input = itnext+" "+restrictionList.get(c).getRange()+" "+add+" .";
										writeFile(input,pathABox);
										checkRole(restrictionList.get(c).getRange(),itnext,add,pathABox);
										//System.out.println("typenew="+restrictionList.get(c).getType());
									}
							}
							
						}
						break;
						
						case 1:
							if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
								for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
									if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
										String itnext = it.next();
										boolean flag = true;
										for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
											String itnextNew = itNew.next();
											String[] strSpaceDomain = itnextNew.split(" ");
											if(strSpaceDomain[1].equals(itnext)){
												flag = true;
												break;
											}
											else{
												flag = false;
											}
										}
										if(!flag){
											String prefix=itnext.substring(0, itnext.length()-1);
											//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
											String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
											
											String add = prefix + "*-"+mchlast+">";
											aboxMapNew.get(restrictionList.get(c).getRange()).add(add+" "+itnext);
											String input = add+" "+restrictionList.get(c).getRange()+" "+itnext+" .";
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),add,itnext,pathABox);
										}
										
									
								}
									else{
										aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
										String itnext = it.next();
										String prefix=itnext.substring(0, itnext.length()-1);
										//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
										String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
										
										String add = prefix + "*-"+mchlast+">";
										aboxMapNew.get(restrictionList.get(c).getRange()).add(add+" "+itnext);
										String input = add+" "+restrictionList.get(c).getRange()+" "+itnext+" .";
										writeFile(input,pathABox);
										checkRole(restrictionList.get(c).getRange(),add,itnext,pathABox);
									}
							}
							
						}
						break;
						
						case 2:
							if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
								for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
									String itnext = it.next();
									String[] strSpaceDomain = itnext.split(" ");
									if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
										boolean flag = true;
										for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
											String itnextNew = itNew.next();
											//String[] strSpaceDomain = itnextNew.split(" ");
											if(itnextNew.equals(strSpaceDomain[0])){
												flag = true;
												break;
											}
											else{
												flag = false;
											}
										}
										if(!flag){
											aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpaceDomain[0]);
											String input = strSpaceDomain[0]+" "+subsumption+" "+restrictionList.get(c).getRange()+" .";
											writeFile(input,pathABox);
											checkClass(restrictionList.get(c).getRange(),strSpaceDomain[0],input,pathABox);
										}
										
									
								}
									else{
										aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
										aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpaceDomain[0]);
										String input = strSpaceDomain[0]+" "+subsumption+" "+restrictionList.get(c).getRange()+" .";
										writeFile(input,pathABox);
										checkClass(restrictionList.get(c).getRange(),strSpaceDomain[0],input,pathABox);
									}
							}
							
						}
						break;
						
						case 3:
							if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
								for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
									String itnext = it.next();
									String[] strSpaceDomain = itnext.split(" ");
									if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
										boolean flag = true;
										for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
											String itnextNew = itNew.next();
											//String[] strSpaceDomain = itnextNew.split(" ");
											if(itnextNew.equals(strSpaceDomain[1])){
												flag = true;
												break;
											}
											else{
												flag = false;
											}
										}
										if(!flag){
											aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpaceDomain[1]);
											String input = strSpaceDomain[1]+" "+subsumption+" "+restrictionList.get(c).getRange()+" .";
											writeFile(input,pathABox);
											checkClass(restrictionList.get(c).getRange(),strSpaceDomain[1],input,pathABox);
										}
										
									
								}
									else{
										aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
										aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpaceDomain[1]);
										String input = strSpaceDomain[1]+" "+subsumption+" "+restrictionList.get(c).getRange()+" .";
										writeFile(input,pathABox);
										checkClass(restrictionList.get(c).getRange(),strSpaceDomain[1],input,pathABox);
									}
							}
							
						}
						break;
						
						case 4:
							if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
								for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
									String itnext = it.next();
									String[] strSpace1 = itnext.split(" ");
									if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
										boolean flag = true;
										for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
											String itnextNew = itNew.next();
											String[] strSpace2 = itnextNew.split(" ");
											if(strSpace2[0].equals(strSpace1[0])){
												flag = true;
												break;
											}
											else{
												flag = false;
											}
										}
										if(!flag){
											String prefix=strSpace1[0].substring(0, strSpace1[0].length()-1);
											//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
											String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
											
											String add = prefix + "*"+ mchlast+">";
											aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpace1[0]+" "+add);
											String input = strSpace1[0]+" "+restrictionList.get(c).getRange()+" "+add+" .";
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),strSpace1[0],add,pathABox);
										}
										
									
								}
									else{
										aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
										String prefix=strSpace1[0].substring(0, strSpace1[0].length()-1);
										//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
										String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
										
										String add = prefix + "*"+mchlast+">";
										aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpace1[0]+" "+add);
										String input = strSpace1[0]+" "+restrictionList.get(c).getRange()+" "+add+" .";
										writeFile(input,pathABox);
										checkRole(restrictionList.get(c).getRange(),strSpace1[0],add,pathABox);
									}
							}
							
						}
						break;
						
						case 5:
							if(!restrictionList.get(c).getDomain().equals(restrictionList.get(c).getRange())){
								if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
									for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
										String itnext = it.next();
										String[] strSpace1 = itnext.split(" ");
										if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
											boolean flag = true;
											for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
												String itnextNew = itNew.next();
												String[] strSpace2 = itnextNew.split(" ");
												if(strSpace2[1].equals(strSpace1[0])){
													flag = true;
													break;
												}
												else{
													flag = false;
												}
											}
											if(!flag){
												String prefix=strSpace1[0].substring(0, strSpace1[0].length()-1);
												//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
												String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
												
												String add = prefix + "*-"+mchlast+">";
												aboxMapNew.get(restrictionList.get(c).getRange()).add(add+" "+strSpace1[0]);
												String input = add+" "+restrictionList.get(c).getRange()+" "+strSpace1[0]+" .";
												writeFile(input,pathABox);
												checkRole(restrictionList.get(c).getRange(),add,strSpace1[0],pathABox);
											}
											
										
									}
										else{
											aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
											String prefix=strSpace1[0].substring(0, strSpace1[0].length()-1);
											//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
											String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
											
											String add = prefix + "*-"+mchlast+">";
											aboxMapNew.get(restrictionList.get(c).getRange()).add(add+" "+strSpace1[0]);
											String input = add+" "+restrictionList.get(c).getRange()+" "+strSpace1[0]+" .";
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),add,strSpace1[0],pathABox);
										}
								}
							}
							}
								else{
									if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
										ArrayList<String> sameRoleList= aboxMapNew.get(restrictionList.get(c).getDomain());
										int sameSize=0;
										sameSize=sameRoleList.size();
										boolean flag=true;
										for(int d=0;d<sameSize;d++){
											String[] strSpace1 = sameRoleList.get(d).split(" ");
											for(int r=0;r<sameSize;r++){
												String[] strSpace2 = sameRoleList.get(r).split(" ");
												if(strSpace2[1].equals(strSpace1[0])){
													flag = true;
													break;
												}
												else{
													flag = false;
												}
											}
											if(!flag){
												String prefix=strSpace1[0].substring(0, strSpace1[0].length()-1);
												//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
												String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
												
												String add = prefix + "*-"+mchlast+">";
												aboxMapNew.get(restrictionList.get(c).getRange()).add(add+" "+strSpace1[0]);
												String input = add+" "+restrictionList.get(c).getRange()+" "+strSpace1[0]+" .";
												writeFile(input,pathABox);
												checkRole(restrictionList.get(c).getRange(),add,strSpace1[0],pathABox);
											}
										}
									}
						}
						break;
						
						case 6:
							if(!restrictionList.get(c).getDomain().equals(restrictionList.get(c).getRange())){
								if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
									for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
										String itnext = it.next();
										String[] strSpace1 = itnext.split(" ");
										if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
											boolean flag = true;
											for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
												String itnextNew = itNew.next();
												String[] strSpace2 = itnextNew.split(" ");
												if(strSpace2[0].equals(strSpace1[1])){
													flag = true;
													break;
												}
												else{
													flag = false;
												}
											}
											if(!flag){
												String prefix=strSpace1[1].substring(0, strSpace1[1].length()-1);
												//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
												String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
												
												String add = prefix + "*"+ mchlast+">";
												aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpace1[1]+" "+add);
												String input = strSpace1[1]+" "+restrictionList.get(c).getRange()+" "+add+" .";
												writeFile(input,pathABox);
												checkRole(restrictionList.get(c).getRange(),strSpace1[1],add,pathABox);
											}
											
										
									}
										else{
											aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
											String prefix=strSpace1[1].substring(0, strSpace1[1].length()-1);
											//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
											String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
											
											String add = prefix + "*"+mchlast+">";
											aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpace1[1]+" "+add);
											String input = strSpace1[1]+" "+restrictionList.get(c).getRange()+" "+add+" .";
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),strSpace1[1],add,pathABox);
										}
								}
								
							}
							}
							else{
								if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
									ArrayList<String> sameRoleList= aboxMapNew.get(restrictionList.get(c).getDomain());
									int sameSize=0;
									sameSize=sameRoleList.size();
									boolean flag=true;
									for(int r=0;r<sameSize;r++){
										String[] strSpace1 = sameRoleList.get(r).split(" ");
										for(int d=0;d<sameSize;d++){
											String[] strSpace2 = sameRoleList.get(d).split(" ");
											if(strSpace2[0].equals(strSpace1[1])){
												flag = true;
												break;
											}
											else{
												flag = false;
											}
										}
										if(!flag){
											String prefix=strSpace1[1].substring(0, strSpace1[1].length()-1);
											//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
											String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
											
											String add = prefix + "*"+mchlast+">";
											aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpace1[1]+" "+add);
											String input = strSpace1[1]+" "+restrictionList.get(c).getRange()+" "+add+" .";
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),strSpace1[1],add,pathABox);
										}
									}
									
									/*for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
										String itnext = it.next();
										String[] strSpace1 = itnext.split(" ");
										if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
											boolean flag = true;
											for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
												String itnextNew = itNew.next();
												String[] strSpace2 = itnextNew.split(" ");
												if(strSpace2[0].equals(strSpace1[1])){
													flag = true;
													break;
												}
												else{
													flag = false;
												}
											}
											if(!flag){
												String prefix=strSpace1[1].substring(0, strSpace1[1].length()-1);
												String add = prefix + "*"+ restrictionList.get(c).getRange()+">";
												aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpace1[1]+" "+add);
												String input = strSpace1[1]+" "+classname+restrictionList.get(c).getRange()+"> "+add+" .";
												writeFile(input,pathABox);
												checkRole(restrictionList.get(c).getRange(),strSpace1[1],add,pathABox);
											}
											
										
									}
										else{
											aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
											String prefix=strSpace1[1].substring(0, strSpace1[1].length()-1);
											String add = prefix + "*"+ restrictionList.get(c).getRange()+">";
											aboxMapNew.get(restrictionList.get(c).getRange()).add(strSpace1[1]+" "+add);
											String input = strSpace1[1]+" "+classname+restrictionList.get(c).getRange()+"> "+add+" .";
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),strSpace1[1],add,pathABox);
										}
								}*/
								
							}
							}
						break;
						
						case 7:
							if(aboxMapNew.containsKey(restrictionList.get(c).getDomain())){
								for(Iterator<String> it=aboxMapNew.get(restrictionList.get(c).getDomain()).iterator();it.hasNext();){
									String itnext = it.next();
									String[] strSpace1 = itnext.split(" ");
									if(aboxMapNew.containsKey(restrictionList.get(c).getRange())){
										boolean flag = true;
										for(Iterator<String> itNew=aboxMapNew.get(restrictionList.get(c).getRange()).iterator();itNew.hasNext();){
											String itnextNew = itNew.next();
											String[] strSpace2 = itnextNew.split(" ");
											if(strSpace2[1].equals(strSpace1[1])){
												flag = true;
												break;
											}
											else{
												flag = false;
											}
										}
										if(!flag){
											String prefix=strSpace1[1].substring(0, strSpace1[1].length()-1);
											//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
											String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
											
											String add = prefix + "*-"+mchlast+">";
											aboxMapNew.get(restrictionList.get(c).getRange()).add(add+" "+strSpace1[1]);
											String input = add+" "+restrictionList.get(c).getRange()+" "+strSpace1[1]+" .";
											writeFile(input,pathABox);
											checkRole(restrictionList.get(c).getRange(),add,strSpace1[1],pathABox);
										}
										
									
								}
									else{
										aboxMapNew.put(restrictionList.get(c).getRange(), new ArrayList<String>());
										String prefix=strSpace1[1].substring(0, strSpace1[1].length()-1);
										//为了gstore等引擎能够识别，去掉两个尖括号<http://uob.iodt.ibm.com/univ-bench-dl.owl#worksFor>
										String mchlast=restrictionList.get(c).getRange().substring(1, restrictionList.get(c).getRange().length()-1);
										
										String add = prefix + "*-"+mchlast+">";
										aboxMapNew.get(restrictionList.get(c).getRange()).add(add+" "+strSpace1[1]);
										String input = add+" "+restrictionList.get(c).getRange()+" "+strSpace1[1]+" .";
										writeFile(input,pathABox);
										checkRole(restrictionList.get(c).getRange(),add,strSpace1[1],pathABox);
									}
							}
							
						}
						break;
						
						default:
							break;
					}
						/*long filesizecafter=file.length();
						if(filesizecafter==filesizecbefore){
							restrictionList.remove(c);
						}*/
					}
				//tag++;
				}while(filesize!=file.length() && count <5);
				//while(filesize!=file.length() && count<10);
				//while(sizeaboxOriginal!=aboxMapNew.size() && count <10);
				//while(filesize!=fileSize.length() && count<10);
				//循环终止条件：文件大小不再递增或者循环次数的count值超出10
			
	     }
				
				
				
				
				public void checkClass(String className,String individual,String input,String pathABox){
					if(dataMapsubclassOf.containsKey(className)){
						ArrayList<String> father = getFathersubclassOf(className);
						while(father.size()>0){
							if(aboxMapNew.containsKey(father.get(0))){
								//int aboxMapMergeSize = aboxMapNew.get(father.get(0)).size();
								if(!aboxMapNew.get(father.get(0)).contains(individual)){
									aboxMapNew.get(father.get(0)).add(individual);
									String inputNew = input.replaceFirst(className, father.get(0));
									writeFile(inputNew,pathABox);
								}
								/*aboxMapNew.get(father.get(0)).add(individual);
								
								if(aboxMapMergeSize!=aboxMapNew.get(father.get(0)).size()){
									String inputNew = input.replaceFirst(className, father.get(0));
									writeFile(inputNew,pathABox);
								}*/
								father.remove(0);
							}
							else{
								aboxMapNew.put(father.get(0), new ArrayList<String>());
								if(!aboxMapNew.get(father.get(0)).contains(individual)){
									aboxMapNew.get(father.get(0)).add(individual);
									String inputNew = input.replaceFirst(className, father.get(0));
									writeFile(inputNew,pathABox);
								}
								father.remove(0);
							}
						}
					}
				}
				
				
				public void checkRole(String role,String domain,String range,String pathABox) throws IOException{
					//System.out.println("dataMapsubpropertyOf"+dataMapsubpropertyOf);
					//System.out.println("tboxsubPropertyOfMap"+tboxsubPropertyOfMap);
					long fileMergeSize =0;
					File file=new File(pathABox);
					if(!file.exists()||file.isDirectory())
						try {
							file.createNewFile();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					do{
						fileMergeSize = file.length();
						//System.out.println("fileMergeSize"+fileMergeSize);
						if(tboxsubPropertyOfMap.containsKey(role)){
							ArrayList<String> father = getFathersubpropertyOf(role);
							//String prefix=original.substring(0, original.length()-1);
							if(domain.contains("*")){
								while(father.size()>0){
									//下边两句是为了使gstore中的数据能用，我们将*号后边的role去掉了尖括号，所以加入的时候也要去掉
									String rolenew = role.substring(1, role.length()-1);
									String fathernew = father.get(0).substring(1, father.get(0).length()-1);
									
									String domainNew = domain.replaceFirst(rolenew, fathernew);
									String inputFatherNew = domainNew+" "+father.get(0)+" "+range+" .";
									String inputFather = domain+" "+father.get(0)+" "+range+" .";
									if(aboxMapNew.containsKey(father.get(0))){
										if(!aboxMapNew.get(father.get(0)).contains(domainNew+" "+range)){
											aboxMapNew.get(father.get(0)).add(domainNew+" "+range);
											writeFile(inputFatherNew,pathABox);
											checkRole(father.get(0),domainNew,range,pathABox);
										}
										//子关系中的domain和range也要包含于父关系中。
										if(!aboxMapNew.get(father.get(0)).contains(domain+" "+range)){
											aboxMapNew.get(father.get(0)).add(domain+" "+range);
											writeFile(inputFather,pathABox);
											checkRole(father.get(0),domain,range,pathABox);
										}

										
										father.remove(0);
									}
									else{
										aboxMapNew.put(father.get(0), new ArrayList<String>());
										if(!aboxMapNew.get(father.get(0)).contains(domainNew+" "+range)){
											aboxMapNew.get(father.get(0)).add(domainNew+" "+range);
											writeFile(inputFatherNew,pathABox);
											checkRole(father.get(0),domainNew,range,pathABox);
										}
										if(!aboxMapNew.get(father.get(0)).contains(domain+" "+range)){
											aboxMapNew.get(father.get(0)).add(domain+" "+range);
											writeFile(inputFather,pathABox);
											checkRole(father.get(0),domain,range,pathABox);
										}
										
										father.remove(0);
									}
								}
								
							}
							else{
								while(father.size()>0){
									//下边两句是为了使gstore中的数据能用，我们将*号后边的role去掉了尖括号，所以加入的时候也要去掉
									String rolenew = role.substring(1, role.length()-1);
									String fathernew = father.get(0).substring(1, father.get(0).length()-1);
									
									String rangeNew = range.replaceFirst(rolenew, fathernew);
									//String addNew = prefix + "*"+ father.get(0)+">";
									String inputFatherNew = domain+" "+father.get(0)+" "+rangeNew+" .";
									String inputFather = domain+" "+father.get(0)+" "+range+" .";
									if(aboxMapNew.containsKey(father.get(0))){
										if(!aboxMapNew.get(father.get(0)).contains(domain+" "+rangeNew)){
											aboxMapNew.get(father.get(0)).add(domain+" "+rangeNew);
											writeFile(inputFatherNew,pathABox);
											checkRole(father.get(0),domain,rangeNew,pathABox);
										}
										if(!aboxMapNew.get(father.get(0)).contains(domain+" "+range)){	
											aboxMapNew.get(father.get(0)).add(domain+" "+range);
											writeFile(inputFather,pathABox);
											checkRole(father.get(0),domain,range,pathABox);
										}
										/*int aboxMapMergeSize = aboxMapNew.get(father.get(0)).size();
										aboxMapNew.get(father.get(0)).add(domain+" "+rangeNew);
										if(aboxMapMergeSize!=aboxMapNew.get(father.get(0)).size()){
											writeFile(inputFatherNew,pathABox);
										}
										aboxMapMergeSize = aboxMapNew.get(father.get(0)).size();
										aboxMapNew.get(father.get(0)).add(domain+" "+range);
										if(aboxMapMergeSize!=aboxMapNew.get(father.get(0)).size()){
											writeFile(inputFather,pathABox);
										}*/
										
										father.remove(0);
									}
									else{
										aboxMapNew.put(father.get(0), new ArrayList<String>());
										if(!aboxMapNew.get(father.get(0)).contains(domain+" "+rangeNew)){
											aboxMapNew.get(father.get(0)).add(domain+" "+rangeNew);
											writeFile(inputFatherNew,pathABox);
											checkRole(father.get(0),domain,rangeNew,pathABox);
										}
										if(!aboxMapNew.get(father.get(0)).contains(domain+" "+range)){	
											aboxMapNew.get(father.get(0)).add(domain+" "+range);
											writeFile(inputFather,pathABox);
											checkRole(father.get(0),domain,range,pathABox);
										}
										
										father.remove(0);
									}
								}
							}
							
						}
						//System.out.println("dataMapinverseOf"+dataMapinverseOf);
						//System.out.println("tboxinverseOfMap"+tboxinverseOfMap);
						if(tboxinverseOfMap.containsKey(role)){
							String father = getInverse(role);
							if(domain.contains("*")){
								String prefix=range.substring(0, range.length()-1);
								//下边是为了使gstore中的数据能用，我们将*号后边的role去掉了尖括号，所以加入的时候也要去掉
								String fathernew = father.substring(1, father.length()-1);
								
								String rangeNew = prefix + "*"+ fathernew+">"; 
								String inputFatherNew = range+" "+father+" "+rangeNew+" .";
								String inputFather = range+" "+father+" "+domain+" .";
								
								String inputRoleNew = rangeNew+" "+role+" "+range+" .";
							
								if(aboxMapNew.containsKey(father)){
									if(!aboxMapNew.get(father).contains(range+" "+rangeNew)){
										aboxMapNew.get(father).add(range+" "+rangeNew);
										writeFile(inputFatherNew,pathABox);
										checkRole(father,range,rangeNew,pathABox);
									}
									if(!aboxMapNew.get(father).contains(range+" "+domain)){
										aboxMapNew.get(father).add(range+" "+domain);
										writeFile(inputFather,pathABox);
										checkRole(father,range,domain,pathABox);
									}
									if(!aboxMapNew.get(role).contains(rangeNew+" "+range)){
										aboxMapNew.get(role).add(rangeNew+" "+range);
										writeFile(inputRoleNew,pathABox);
										checkRole(role,rangeNew,range,pathABox);
									}
									/*int aboxMapMergeSize = aboxMapNew.get(father).size();
									aboxMapNew.get(father).add(range+" "+rangeNew);
									if(aboxMapMergeSize!=aboxMapNew.get(father).size()){
										writeFile(inputFatherNew,pathABox);
									}
									aboxMapMergeSize = aboxMapNew.get(father).size();
									aboxMapNew.get(father).add(range+" "+domain);
									if(aboxMapMergeSize!=aboxMapNew.get(father).size()){
										writeFile(inputFather,pathABox);
									}
									
									aboxMapMergeSize = aboxMapNew.get(role).size();
									aboxMapNew.get(role).add(rangeNew+" "+range);
									if(aboxMapMergeSize!=aboxMapNew.get(role).size()){
										writeFile(inputRoleNew,pathABox);
									}*/
									
									
								}
								else{
									aboxMapNew.put(father, new ArrayList<String>());
									if(!aboxMapNew.get(father).contains(range+" "+rangeNew)){
										aboxMapNew.get(father).add(range+" "+rangeNew);
										writeFile(inputFatherNew,pathABox);
										checkRole(father,range,rangeNew,pathABox);
									}
									if(!aboxMapNew.get(father).contains(range+" "+domain)){
										aboxMapNew.get(father).add(range+" "+domain);
										writeFile(inputFather,pathABox);
										checkRole(father,range,domain,pathABox);
									}
									if(!aboxMapNew.get(role).contains(rangeNew+" "+range)){
										aboxMapNew.get(role).add(rangeNew+" "+range);
										writeFile(inputRoleNew,pathABox);
										checkRole(role,rangeNew,range,pathABox);
									}
								}
								
							}
							else{
								String prefix=domain.substring(0, domain.length()-1);
								//下边是为了使gstore中的数据能用，我们将*号后边的role去掉了尖括号，所以加入的时候也要去掉
								String fathernew = father.substring(1, father.length()-1);
								
								String domainNew = prefix + "*-"+ fathernew+">";
								String inputFatherNew = domainNew+" "+father+" "+domain+" .";
								String inputFather = range+" "+father+" "+domain+" .";
								
								String inputRoleNew = domain+" "+role+" "+domainNew+" .";
								
								if(aboxMapNew.containsKey(father)){
									if(!aboxMapNew.get(father).contains(domainNew+" "+domain)){
										aboxMapNew.get(father).add(domainNew+" "+domain);
										writeFile(inputFatherNew,pathABox);
										checkRole(father,domainNew,domain,pathABox);
									}
									if(!aboxMapNew.get(father).contains(range+" "+domain)){
										aboxMapNew.get(father).add(range+" "+domain);
										writeFile(inputFather,pathABox);
										checkRole(father,range,domain,pathABox);
									}
									if(!aboxMapNew.get(role).contains(domain+" "+domainNew)){
										aboxMapNew.get(role).add(domain+" "+domainNew);
										writeFile(inputRoleNew,pathABox);
										checkRole(role,domain,domainNew,pathABox);
									}
									/*int aboxMapMergeSize = aboxMapNew.get(father).size();
									aboxMapNew.get(father).add(domainNew+" "+domain);
									if(aboxMapMergeSize!=aboxMapNew.get(father).size()){
										writeFile(inputFatherNew,pathABox);
									}
									aboxMapMergeSize = aboxMapNew.get(father).size();
									aboxMapNew.get(father).add(range+" "+domain);
									if(aboxMapMergeSize!=aboxMapNew.get(father).size()){
										writeFile(inputFather,pathABox);
									}
									
									aboxMapMergeSize = aboxMapNew.get(role).size();
									aboxMapNew.get(role).add(domain+" "+domainNew);
									if(aboxMapMergeSize!=aboxMapNew.get(role).size()){
										writeFile(inputRoleNew,pathABox);
									}*/
									
									
									
								}
								else{
									aboxMapNew.put(father, new ArrayList<String>());
									if(!aboxMapNew.get(father).contains(domainNew+" "+domain)){
										aboxMapNew.get(father).add(domainNew+" "+domain);
										writeFile(inputFatherNew,pathABox);
										checkRole(father,domainNew,domain,pathABox);
									}
									if(!aboxMapNew.get(father).contains(range+" "+domain)){
										aboxMapNew.get(father).add(range+" "+domain);
										writeFile(inputFather,pathABox);
										checkRole(father,range,domain,pathABox);
									}
									if(!aboxMapNew.get(role).contains(domain+" "+domainNew)){
										aboxMapNew.get(role).add(domain+" "+domainNew);
										writeFile(inputRoleNew,pathABox);
										checkRole(role,domain,domainNew,pathABox);
									}
									
								}
							}
								
						}
						
						
					}while(fileMergeSize!=file.length());
					
					
				}
				
				
				public void writeFile(String input,String pathABox){
					//String path="D:\\workspace\\Ukmodel\\data\\University0_14.nt";
					//String path="LUBM10.nt";
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
						writer.write(input+"\r\n");
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
				
				
				/*public  void main(String[] args){
					String path = "lubm-ex-10.nt";
					getDataMapsubpropertyOf(path);
					getDataMapinverseOf(path);
					for(String s:tboxinverseOfMap.keySet()){
						System.out.println(s+tboxinverseOfMap.get(s)+getInverse(s));
					}
					for(String s:tboxsubPropertyOfMap.keySet()){
						System.out.println(s+tboxsubPropertyOfMap.get(s)+getFathersubpropertyOf(s));
					}
					}*/		
}