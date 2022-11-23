package cn.cmcc.oneos.cmiot.hanxu.analysistool.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.cmcc.oneos.cmiot.hanxu.analysistool.service.ProcessService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

public class ProcessServiceImpl implements ProcessService{

	@Override
	public List<List<String>> AnalysisData(File dataFile) {

		List<List<String>> result = new ArrayList<List<String>>();

		//csv文件直接处理
		if(FileNameUtil.extName(dataFile).equals("csv")) {

			CsvReader reader = CsvUtil.getReader();
			//从文件中读取CSV数据
			CsvData data = reader.read(FileUtil.file(dataFile));
			List<CsvRow> rows = data.getRows();
			//遍历行
			for (CsvRow csvRow : rows) {
				//getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
				result.add(csvRow.getRawList());
			}
			return result;
		}

		//s2p和prn文件特殊处理
		if(FileNameUtil.extName(dataFile).equals("s2p") || FileNameUtil.extName(dataFile).equals("prn")) {

		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeFile(HashMap<String, List<List<String>>> allData,String distFile) {
		// TODO Auto-generated method stub
		//通过工具类创建writer
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 在Excel工作簿中建一工作表，其名为缺省值  
			XSSFSheet sheet = workbook.createSheet();
			int i = 0;
			for (String key : allData.keySet()) {
				List<List<String>> list = allData.get(key);
				XSSFRow row_value = sheet.createRow(list.size());
				for (List<String> rowList : list) {
					for (; i < rowList.size(); i++) {
						// 在索引0的位置创建单元格（左上端）  
						XSSFCell cell = row_value.createCell(i);
						// 在单元格中输入一些内容  
						cell.setCellValue(rowList.get(i));  
					}
				}

			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}


	}

}
