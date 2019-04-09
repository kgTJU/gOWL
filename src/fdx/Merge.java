package fdx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Merge {

	public Map<String,ArrayList<String>> Mergence(Map<String,ArrayList<String>> aboxMap,String pathABox,String path) {
		// TODO Auto-generated method stub
		//Classification classification = new Classification();
		//根据文件进行树形构造
		//classification.readClassifyFile("G:\\data\\ClassTree.txt");
		//classification.printData();
		
		/*
		 * 参照其他部分代码修改GenerateClass文件操作为覆盖原文件，当前代码时追加模式
		 */
//		GenerateClass generateClass = new GenerateClass();
//		generateClass.readFromFile("G:\\data\\resultNew2");
		/*
		 * 其他文件可自动覆盖
		 */
		/*GenerateSubpropertyOf generateSubpropertyOf = new GenerateSubpropertyOf();
		generateSubpropertyOf.readFromFileAndWrite2File();
		System.out.println("初始化父子关系大小变化为："+(generateSubpropertyOf.fileOut.length()-generateSubpropertyOf.fileIn.length()));
		
		GenerateInverseOf generateInverseOf = new GenerateInverseOf();
		generateInverseOf.readFromFileAndWrite2File();
		System.out.println("初始化逆关系大小变化为："+(generateInverseOf.fileOut.length()-generateInverseOf.fileIn.length()));
		//generateSubpropertyOf.pathIn="D:\\workspace\\Ukmodel\\data\\resultFile\\inverse_output_University0_14.nt";
		generateSubpropertyOf.pathIn="data/resultFile/inverse_output_University0_14.nt";
		int times=0;
		
		System.out.println("循环操作开始前大小变化为："+(generateInverseOf.fileOut.length()-generateInverseOf.fileIn.length()));
		while(generateInverseOf.fileIn.length()!=generateInverseOf.fileOut.length()){
			times++;
			generateSubpropertyOf.readFromFileAndWrite2File();
			if(generateSubpropertyOf.fileIn.length()==generateSubpropertyOf.fileOut.length()){
				System.out.println("第"+times+"次循环操作处理结束时，输入文件大小为："+generateSubpropertyOf.fileIn.length());
				System.out.println("第"+times+"次循环操作处理结束时，输出文件大小为："+generateSubpropertyOf.fileOut.length());
				System.out.println("第"+times+"循环操作结时输入输出变化为："+(generateSubpropertyOf.fileOut.length()-generateSubpropertyOf.fileIn.length()));
				
				break;
			}else{
				System.out.println("第"+times+"次循环操作处理结束时，输入文件大小为："+generateSubpropertyOf.fileIn.length());
				System.out.println("第"+times+"次循环操作处理结束时，输出文件大小为："+generateSubpropertyOf.fileOut.length());
				System.out.println("第"+times+"循环操作结时输入输出变化为："+(generateSubpropertyOf.fileOut.length()-generateSubpropertyOf.fileIn.length()));
				generateInverseOf.readFromFileAndWrite2File();
		
				System.out.println("第"+times+"次循环操作处理结束时，输入文件大小为："+generateSubpropertyOf.fileIn.length());
				System.out.println("第"+times+"次循环操作处理结束时，输出文件大小为："+generateSubpropertyOf.fileOut.length());
				System.out.println("循环操作开始先输入输出变化为："+(generateInverseOf.fileOut.length()-generateInverseOf.fileIn.length()));

			}
			
		}
		
		AdjustFormat adjustFormat = new AdjustFormat();
		adjustFormat.readFromFileAndWrite2File();*/
		
		int size ;
		Map<String,ArrayList<String>> aboxMap1;
		Map<String,ArrayList<String>> aboxMap2;
		do{
			size = aboxMap.size();
			GenerateSubpropertyOf generateSubpropertyOf = new GenerateSubpropertyOf();
			aboxMap1 = generateSubpropertyOf.Generation(aboxMap,pathABox,path);
			
			GenerateInverseOf generateInverseOf = new GenerateInverseOf();
			aboxMap2 = generateInverseOf.Generation(aboxMap1,pathABox,path);
			aboxMap.putAll(aboxMap2);
			System.out.println("copysize = "+aboxMap.size());
		}while(size!=aboxMap.size());
		
		return aboxMap;
		
		//generateSubpropertyOf.readFromFileAndWrite2File();
		//System.out.println(generateSubpropertyOf.fileIn.length()+"-"+generateSubpropertyOf.fileOut.length()+"="+(generateSubpropertyOf.fileOut.length()-generateSubpropertyOf.fileIn.length()));
		
		
		 
		
	}
	

}
