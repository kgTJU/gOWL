package fdx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

//import javax.swing.text.html.HTMLDocument.Iterator;

public class MapTest {
	
	 Map<String,ArrayList<String>> aboxMap=new HashMap<String,ArrayList<String>>();
	 Map<String,String> tboxsubPropertyOfMap=new HashMap<String,String>();
	 Map<String,String> tboxinverseOfMap=new HashMap<String,String>();
	 
	 ArrayList<String> tboxtransitiveOfMap=new ArrayList<String>();
	 ArrayList<String> tboxsymmetricOfMap=new ArrayList<String>();
	 
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		//readABox();
		readTBox("lubm-ex-10.nt");
	}*/
	
	public void readABox(String pathABox){
		//String path = "D:\\workspace\\Ukmodel\\data\\University0_14.nt";
		//String path = "LUBM10.nt";
		//Map<String,ArrayList<String>> aboxMap=new HashMap<String,ArrayList<String>>();
		//Set<String> s = new HashSet();
		File file=new File(pathABox);
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
				//s.add(str);	
				if(str.contains("\"")){
					//System.out.println(str);
					continue;
				}
				else if(str.contains("type")&&!str.contains("unknown:namespace")){
					String[] parseStrSpace = str.split(" ");
					//用全称表示
					//String className = parseStrSpace[2].substring(48,parseStrSpace[2].length()-1);
					String className = parseStrSpace[2];
					if(aboxMap.containsKey(className)){
						aboxMap.get(className).add(parseStrSpace[0]);
					}else{
						/*ArrayList<String> list = new ArrayList<String>();
						list.add(parseStrSpace[0]);
						aboxMap.put(className, list);*/
						aboxMap.put(className, new ArrayList<String>());
						aboxMap.get(className).add(parseStrSpace[0]);
					}
					continue;
				}
				else if(!str.contains("unknown:namespace")){
					String[] parseStrSpace = str.split(" ");
					//用全称表示
					//String roleName = parseStrSpace[1].substring(48,parseStrSpace[1].length()-1);
					String roleName = parseStrSpace[1];
					if(aboxMap.containsKey(roleName)){
						aboxMap.get(roleName).add(parseStrSpace[0]+" "+parseStrSpace[2]);
					}else{
						aboxMap.put(roleName, new ArrayList<String>());
						aboxMap.get(roleName).add(parseStrSpace[0]+" "+parseStrSpace[2]);
					}
					continue;
				}
			}
			//System.out.println("size is: "+aboxMap.size());
			//System.out.println("FullProfessor "+aboxMap.get("FullProfessor"));
			/*for(String s : aboxMap.keySet()){
				System.out.println("key "+s);
				//System.out.println("value "+aboxMap.get(s));
				Iterator<String> it= aboxMap.get(s).iterator();
				for(;it.hasNext();){
					System.out.println(it.next());
				}
				}*/
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
		//return s;
	}

	public void readTBox(String path){
		//String path = "D:\\workspace\\Ukmodel\\data\\University0_14.nt";
		//String path = "lubm-ex-10.nt";
		//Map<String,ArrayList<String>> aboxMap=new HashMap<String,ArrayList<String>>();
		//Set<String> s = new HashSet();
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
				//s.add(str);	
				if(str.contains("subPropertyOf")){
					String[] strSubPropertyOf = str.split(" ");
					//用全称表示
					//String roleDomain = strSubPropertyOf[0].substring(48,strSubPropertyOf[0].length()-1);
					//String roleRange = strSubPropertyOf[2].substring(48,strSubPropertyOf[2].length()-1);
					String roleDomain = strSubPropertyOf[0];
					String roleRange = strSubPropertyOf[2];
					tboxsubPropertyOfMap.put(roleDomain, roleRange);
					continue;
				}
				else if(str.contains("inverseOf")&&!str.contains("_")){
					String[] strInverseOf = str.split(" ");
					//用全称表示
					//String roleDomain = strInverseOf[0].substring(48,strInverseOf[0].length()-1);
					//String roleRange = strInverseOf[2].substring(48,strInverseOf[2].length()-1);
					String roleDomain = strInverseOf[0];
					String roleRange = strInverseOf[2];
					tboxinverseOfMap.put(roleDomain, roleRange);
					tboxinverseOfMap.put(roleRange, roleDomain);
					continue;
				}
				//新加入对TransitiveProperty的处理
				else if(str.contains("TransitiveProperty")&&!str.contains("_")){
					String[] strTransitive = str.split(" ");
					String role = strTransitive[0];
					tboxtransitiveOfMap.add(role);
					continue;
				}
				//新加入对SymmetricProperty的处理
				else if(str.contains("SymmetricProperty")&&!str.contains("_")){
					String[] strTransitive = str.split(" ");
					String role = strTransitive[0];
					tboxsymmetricOfMap.add(role);
					continue;
				}
			
			}
			//System.out.println("size is: "+aboxMap.size());
			//System.out.println("FullProfessor "+aboxMap.get("FullProfessor"));
			/*for(String s : aboxMap.keySet()){
				System.out.println("key "+s);
				//System.out.println("value "+aboxMap.get(s));
				Iterator<String> it= aboxMap.get(s).iterator();
				for(;it.hasNext();){
					System.out.println(it.next());
				}
				}*/
			/*for(String s:tboxinverseOfMap.keySet()){
				System.out.println(s+tboxinverseOfMap.get(s));
			}
			for(String s:tboxsubPropertyOfMap.keySet()){
				System.out.println(s+tboxsubPropertyOfMap.get(s));
			}*/
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
		//return s;
	}
	
}
