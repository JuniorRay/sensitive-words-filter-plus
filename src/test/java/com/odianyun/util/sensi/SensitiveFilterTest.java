package com.odianyun.util.sensi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class SensitiveFilterTest extends TestCase{
	/**
	 * 使用测试
	 * @author
	 * @throws Exception
	 */
	public void test() throws Exception{
		
		// 使用默认单例（加载默认词典）
		SensitiveFilter filter = SensitiveFilter.DEFAULT;
		// 向过滤器增加一个词
		filter.put("婚礼上唱春天在哪里");
		
		// 待过滤的句子
		String sentence = "中国，然后，市长在婚礼上唱春天在哪里。";
//		String sentence = "爱你";
		/** 注释原先源码使用方法
		// 进行过滤
		String filted = filter.filter(sentence, '*');
		
		// 如果未过滤，则返回输入的String引用
		if(sentence != filted){
			// 句子中有敏感词
			System.out.println(filted);
		}*/

		/**
		 * Junior 修改源码添加新的使用方式，支持敏感信息汇总
		 */
		Map<String, Object> retMap = filter.filter(sentence, '*');
		Boolean isReplaced = (Boolean)retMap.get(RetEnum.IS_REPLACED.getKey());
		String replacedWords = (String)retMap.get(RetEnum.REPLACED_WORDS.getKey());
		Set sensitiveWordSet = (Set)retMap.get(RetEnum.SENSITIVE_WORD_SET.getKey());

		System.out.println("替换前的句子为:"+sentence);

		// 如果未过滤，则返回输入的String引用
		if(isReplaced){
			// 句子中有敏感词
			System.out.println("句子中有敏感词:"+sensitiveWordSet);
		}

		System.out.println("替换后的句子为:"+replacedWords);
	}

	/**
	 * 逻辑测试
	 * @author  Junior ray
	 */
	public void testLogic(){
		
		SensitiveFilter filter = new SensitiveFilter();
		
		filter.put("你好");
		filter.put("你好1");
		filter.put("你好2");
		filter.put("你好3");
		filter.put("你好4");
		
		System.out.println(filter.filter("你好3天不见", '*'));
		
	}

	/**
	 * @author  junior Ray
	 * 检测大量文本小说，计算耗时
	 * @throws Exception
	 */
	public void testSpeed() throws Exception{
		//输出文件
		PrintStream ps = new PrintStream("D://data/敏感词替换结果.txt");
		//输入检测文件夹（里面包含很多个文件）
		File dir = new File("D:/data/小说文件夹");
		if(ps == null || dir == null){
			System.out.println("文件不存在，比对失败");
			return ;
		}
		List<String> testSuit = new ArrayList<String>(1048576);
		long length = 0;

		for(File file: dir.listFiles()){
			if(file.isFile() && file.getName().endsWith(".txt")){
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb18030"));
				for(String line = br.readLine(); line != null; line = br.readLine()){
					if(line.trim().length() > 0){
						testSuit.add(line);
						length += line.length();
					}
				}
				br.close();
			}
		}

		System.out.println(String.format("待过滤文本共 %d 行，%d 字符。", testSuit.size(), length));


		SensitiveFilter filter = SensitiveFilter.DEFAULT;

		int replaced = 0;

		for(String sentence: testSuit){
			//注释原有的使用方式
			/*String result = filter.filter(sentence, '*');
			if(result != sentence){
				ps.println(sentence);
				ps.println(result);
				ps.println();
				replaced ++;
			}*/

			/**
			 * Junior 修改源码添加新的使用方式，支持敏感信息汇总
			 */
			Map<String, Object> retMap = filter.filter(sentence, '*');
			Boolean isReplaced = (Boolean)retMap.get(RetEnum.IS_REPLACED.getKey());
			String replacedWords = (String)retMap.get(RetEnum.REPLACED_WORDS.getKey());
			Set sensitiveWordSet = (Set)retMap.get(RetEnum.SENSITIVE_WORD_SET.getKey());

//			System.out.println("替换前的句子为:"+sentence);

			// 如果未过滤，则返回输入的String引用
			if(isReplaced){
				// 句子中有敏感词
				ps.println("句子中有敏感词:"+sensitiveWordSet);
				ps.println(sentence);
				ps.println(replacedWords);
				ps.println();
				replaced ++;
			}
		}
		ps.close();

		long timer = System.currentTimeMillis();
		for(String line: testSuit){
			filter.filter(line, '*');
		}
		timer = System.currentTimeMillis() - timer;
		System.out.println(String.format("过滤耗时 %1.3f 秒， 速度为 %1.1f字符/毫秒", timer * 1E-3, length / (double) timer));
		System.out.println(String.format("其中 %d 行有替换", replaced));

	}

}
