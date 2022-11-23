package cn.cmcc.oneos.cmiot.hanxu.analysistool.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface ProcessService {
	
	
	/**
	 * 分析文件数据，返回数据集合
	 * 
	 * */
	public List<List<String>> AnalysisData(File dataFile); 
	
	
	/**
	 * 写入数据
	 * 
	 * */
	
	public boolean writeFile(HashMap<String,List<List<String>>> allData,String distFile);
}
