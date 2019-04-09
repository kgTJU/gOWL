package fdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class Testnew {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*String str=null;int in=0;String str2=null;
		Restriction restriction = new Restriction(str,in,str2);
		
		ArrayList<Restriction> restrictionList = new ArrayList<Restriction>();
		restrictionList=restriction.readFile("D:\\workspace\\Ukmodel\\lubm-ex-10.nt");
		
		GenerateRole generaterole = new GenerateRole();
		generaterole.restrictionAssertion(restrictionList);*/
		//Restriction.writeFile("fudaoxun", "person");
		/*Removal r = new Removal();
		r.cancelRepeat();*/
		
		long t1,t2,total;
        t1=System.currentTimeMillis();
		String pathold = "uobm-ontology.nt";
		String path = "uobm-ontology-rewrite.nt";
		String pathABox = "uobm1.nt";
        
		 /** 1.1 生成类树*/
	 	
		long t11,t12,total112;
	    t11=System.currentTimeMillis(); 
		
		Reason reason = new Reason();
		reason.generateClassTree(pathold);
		
		t12=System.currentTimeMillis();
		total112=t12-t11;
		System.out.println("ClassTree time:"+total112+"ms");
        
		
		/*
		 * mch增加部分：近似处理，对原始TBox进行近似改写，结果写入path中。
		 */		
		EquivalentClassinter equc = new EquivalentClassinter();
		equc.readfile(pathold, path);
		
		
		/*
		 * 5.1  解析Restriction成三元組，并将新生成的类包含写入原TBox中
		 */
        long t511,t512,total5112;
        t511=System.currentTimeMillis();
        
		String str=null;int in=0;String str2=null;
		//String path = "D:\\workspace\\Ukmodel\\lubm-ex-10.nt";

		Restriction restriction = new Restriction(str,in,str2);
		ArrayList<Restriction> restrictionList = new ArrayList<Restriction>();
		//更改后，restrictionList由原来的关键字组成的三元组变成了包含前缀的三元组，即每个关键字前边的前缀都没有省略
		restrictionList=restriction.readFile(path);
		
		t512=System.currentTimeMillis();
		total5112=t512-t511;
		System.out.println("Restriction time:"+total5112+"ms");
		

		
		 /** 1.2 解析类树成Map*/
		 
		//类包含推理中还需要获取类树，所以这里单独写没意义。
		/*Classification classification = new Classification();
		//classification.readClassifyFile("D:\\workspace\\Ukmodel\\ClassTree.txt");
		classification.readClassifyFile("ClassTree.txt");*/
		
		//classification.printData();
		
		/*MapTest mapTest = new MapTest();
		mapTest.readABox();*/
		
		 /** 1.3 类包含推理（生成ABox，无重复）*/
		
		long t131,t132,total1312;
	    t131=System.currentTimeMillis();
		
		GenerateClass generateClass = new GenerateClass();
		//generateClass.readFromFile("D:\\workspace\\Ukmodel\\data\\resultNew");
		//generateClass.readFromFile("data/resultNew");

		Map<String,ArrayList<String>> aboxMap = generateClass.Generation(pathABox,path);
		
		t132=System.currentTimeMillis();
		total1312=t132-t131;
		System.out.println("generateclass time:"+total1312+"ms");
		
		 /** 2.1 解析角色包含关系成Map*/
		 
		/*SubpropertyOf subpropertyOf = new SubpropertyOf();
		subpropertyOf.readFromeFileAndWrite2File();*/
		
		
		 /** 2.2 角色包含推理subProperty（生成ABox,无重复）*/
		
		long t221,t222,total2212;
	    t221=System.currentTimeMillis();
		
		GenerateSubpropertyOf generateSubpropertyOf = new GenerateSubpropertyOf();
		Map<String,ArrayList<String>> aboxMap0 = generateSubpropertyOf.Generation(aboxMap,pathABox,path);
		//generateSubpropertyOf.readFromFileAndWrite2File();
		//System.out.println(generateSubpropertyOf.fileIn.length()+"-"+generateSubpropertyOf.fileOut.length()+"="+(generateSubpropertyOf.fileOut.length()-generateSubpropertyOf.fileIn.length()));
		
		t222=System.currentTimeMillis();
		total2212=t222-t221;
		System.out.println("generatesubproperty time:"+total2212+"ms");
		
		
        /** 2.3 对称关系推理SymmetricProperty（生成ABox,无重复）*/
		
		long t231,t232,total2312;
	    t231=System.currentTimeMillis();
		
		GenerateSymmetric generatesymm = new GenerateSymmetric();
		Map<String,ArrayList<String>> aboxMap23 = generatesymm.Generation(aboxMap0,pathABox,path);
		//generateSubpropertyOf.readFromFileAndWrite2File();
		//System.out.println(generateSubpropertyOf.fileIn.length()+"-"+generateSubpropertyOf.fileOut.length()+"="+(generateSubpropertyOf.fileOut.length()-generateSubpropertyOf.fileIn.length()));
		
		t232=System.currentTimeMillis();
		total2312=t232-t231;
		System.out.println("generatesymmetricproperty time:"+total2312+"ms");
		
		 /** 2.4 传递关系推理transitiveProperty（生成ABox,无重复）*/
		
		long t241,t242,total2412;
	    t241=System.currentTimeMillis();
		
		GenerateTransitive generatetrans = new GenerateTransitive();
		Map<String,ArrayList<String>> aboxMap1 = generatetrans.Generation(aboxMap23,pathABox,path);
		//generateSubpropertyOf.readFromFileAndWrite2File();
		//System.out.println(generateSubpropertyOf.fileIn.length()+"-"+generateSubpropertyOf.fileOut.length()+"="+(generateSubpropertyOf.fileOut.length()-generateSubpropertyOf.fileIn.length()));
		
		t242=System.currentTimeMillis();
		total2412=t242-t241;
		System.out.println("generatetransitiveproperty time:"+total2412+"ms");
		
		
		 /** 3.1 解析角色逆关系成Map*/
		 
		/*InverseOf inverseOf = new InverseOf();
		inverseOf.readFromFileAndWrite2File();*/
		
		
		 /** 3.2 角色逆推理inverseOf（生成ABox,无重复）*/
		
		long t321,t322,total3212;
	    t321=System.currentTimeMillis();
		
		GenerateInverseOf generateInverseOf = new GenerateInverseOf();
		Map<String,ArrayList<String>> aboxMap2 = generateInverseOf.Generation(aboxMap1,pathABox,path);
		//generateInverseOf.readFromFileAndWrite2File();
		
		t322=System.currentTimeMillis();
		total3212=t322-t321;
		System.out.println("generateinverseof time:"+total3212+"ms");
		
		 /** 4  融合角色的subProperty和inverseOf的ABox*/
		
		long t41,t42,total412;
	    t41=System.currentTimeMillis();
		
		Merge merge = new Merge();
		Map<String,ArrayList<String>> aboxMapMerge = merge.Mergence(aboxMap2,pathABox,path);
		
		t42=System.currentTimeMillis();
		total412=t42-t41;
		System.out.println("generatemerge time:"+total412+"ms");
		
		/** 5.2  生成Restriction ABox*/
		
		long t521,t522,total5212;
	    t521=System.currentTimeMillis();
		
		GenerateRole generaterole = new GenerateRole();
		generaterole.restrictionAssertion(restrictionList,aboxMapMerge,pathABox,path);
		
		t522=System.currentTimeMillis();
		total5212=t522-t521;
		System.out.println("generaterestriction time:"+total5212+"ms");
		
		 /** 6  去重且文件名不变*/
		 
		long t61,t62,total612;
	    t61=System.currentTimeMillis();
		
			Removal r = new Removal();
			r.cancelRepeat(pathABox);
		
		t62=System.currentTimeMillis();
		total612=t62-t61;
		System.out.println("generateremoval time:"+total612+"ms");
			
			t2=System.currentTimeMillis();
			total=t2-t1;
			WriteTime t = new WriteTime();
			String time = Long.toString(total);
			t.write2File(time);
			System.out.println("Reasoning time:"+total+"ms");
		
	}

}
