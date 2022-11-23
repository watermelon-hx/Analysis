package cn.cmcc.oneos.cmiot.hanxu.analysistool.core;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.swt.widgets.Display;


import cn.cmcc.oneos.cmiot.hanxu.analysistool.service.ProcessService;
import cn.cmcc.oneos.cmiot.hanxu.analysistool.service.impl.ProcessServiceImpl;
public class MainThread implements Runnable {
	
	private ProgressPage gui;
	private HashSet<String> files;
	private String distFile;
	
	public MainThread(ProgressPage gui,HashSet<String> files,String distFile) {
		this.gui = gui;
		this.files = files;
		this.distFile = distFile;
	}
	
	
	@Override
	public void run() {
		try {
			ProcessService service = new ProcessServiceImpl();
			HashMap<String,List<List<String>>> allData = new HashMap<String, List<List<String>>>();
			Display.getDefault().asyncExec(new Runnable() {                        
				public void run() {
					try {
						// TODO Auto-generated method stub
						int perProgressValue = (100/files.size());
						int progress = 2;
						gui.UpdateProgressBar(progress);
						for (String file : files) {	
							gui.UpdateText("开始解析文件：" + file + "\r\n");
							List<List<String>> analysisData = service.AnalysisData(new File(file));
							allData.put(file, analysisData);
							progress += perProgressValue;
							gui.UpdateProgressBar(progress);
							gui.UpdateText("解析 " + file + " 成功\r\n");
						}
						
						gui.UpdateText("开始生成文件" + distFile + "\r\n");
						boolean isWriteSuccess = service.writeFile(allData,distFile);
						if(isWriteSuccess) {
							gui.UpdateProgressBar(100);
							gui.UpdateText("解析完成！");
						}else {
							gui.UpdateText("解析失败，请稍后重试！");
						}
						
						Thread.sleep(2000);
						gui.DestroyGUI();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						gui.UpdateText("解析失败，请稍后重试！");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						gui.DestroyGUI();
					}
					
				}
			});
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
