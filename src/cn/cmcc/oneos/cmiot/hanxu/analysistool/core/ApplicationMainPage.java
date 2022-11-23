package cn.cmcc.oneos.cmiot.hanxu.analysistool.core;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;

import java.io.File;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;


import cn.hutool.core.io.file.FileNameUtil;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;

public class ApplicationMainPage{

	private Display display;
	protected Object result;
	protected Shell shell;
	private Table table;
	private Text text_folder;
	private Text text_fileName;

	public HashSet<String> files;
	/**
	 * Create contents of the dialog.
	 */
	private void buildPage() {
		files = new HashSet<String>();
		display = new Display();
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.MIN);
		shell.setSize(690, 487);
		shell.setText("测试数据解析工具");
		centerNearTopShell(shell);

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 684, 458);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 61, 17);
		lblNewLabel.setText("数据文件");

		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setBounds(10, 32, 543, 236);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(98);
		tblclmnNewColumn.setText("文件名");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(114);
		tblclmnNewColumn_1.setText("文件类型");

		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(445);
		tblclmnNewColumn_2.setText("文件路径");

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setBounds(10, 294, 61, 17);
		lblNewLabel_1.setText("生成文件");

		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setBounds(10, 320, 83, 17);
		lblNewLabel_2.setText("选择文件位置：");

		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		lblNewLabel_3.setBounds(10, 350, 83, 17);
		lblNewLabel_3.setText("文件名：");

		text_folder = new Text(composite, SWT.BORDER);
		text_folder.setBounds(99, 317, 374, 23);

		text_fileName = new Text(composite, SWT.BORDER);
		text_fileName.setBounds(99, 347, 454, 23);

		Button btnAddFile = new Button(composite, SWT.NONE);
		btnAddFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog filesChooser = new FileDialog(shell,SWT.MULTI);
				filesChooser.setFilterExtensions(new String[] {"*.csv;*.s2p;*.prn"});
				filesChooser.open();
				String[] fileNames = filesChooser.getFileNames();
				String filterPath = filesChooser.getFilterPath();
				for (String name : fileNames) {
					String fullPath = filterPath +"\\" + name;
					File file = new File(fullPath);
					String mainName = FileNameUtil.mainName(file);
					String extName = FileNameUtil.extName(file);
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] {mainName,extName,filterPath});
					files.add(fullPath);
				}
			}
		});
		btnAddFile.setBounds(577, 75, 80, 27);
		btnAddFile.setText("添加文件");

		Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				table.remove(table.getSelectionIndices());
				TableItem[] items = table.getItems();
				files.clear();
				for (TableItem tableItem : items) {
					String fullPath = tableItem.getText(2) + "\\" + tableItem.getText(0) + "." + tableItem.getText(1);
					files.add(fullPath);
				}
			}
		});
		btnRemove.setText("移除文件");
		btnRemove.setBounds(577, 180, 80, 27);

		Button btnConfirm = new Button(composite, SWT.NONE);
		btnConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(files == null || files.size() == 0) {
					MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
					box.setText("提示");
					box.setMessage("请添加文件！");
					if(box.open() == SWT.OK) {
						return;
					}
				}
				if(text_fileName.getText().equals("") || text_folder.getText().equals("")) {
					MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
					box.setText("提示");
					box.setMessage("请确认生成Excel文件的路径及名称");
					if(box.open() == SWT.OK) {
						return;
					}
				}
				
				String distFile = text_folder.getText() + "\\" + text_fileName.getText() + ".xlsx";
				Display.getDefault().syncExec(new Runnable() {                        
					public void run() {
						ProgressPage gui = new ProgressPage(display);
						gui.open(gui,files,distFile);			
					}
				});		

			}
		});
		btnConfirm.setText("确定");
		btnConfirm.setBounds(473, 409, 80, 27);

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
				System.exit(0);
			}
		});
		btnCancel.setText("取消");
		btnCancel.setBounds(577, 409, 80, 27);

		Button btnBrowse= new Button(composite, SWT.NONE);
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String open = dialog.open();
				text_folder.setText(open);

			}
		});
		btnBrowse.setImage(SWTResourceManager.getImage(ApplicationMainPage.class, "/icon/folder-open.png"));
		btnBrowse.setBounds(492, 317, 61, 23);

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(62, 19, 491, 2);

		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(62, 302, 491, 2);

		shell.open();
		shell.layout();
	}

	public void DestroyGUI(){
		display.dispose();
	}

	public Shell GetShell(){
		return shell;
	}

	public Display GetDisplay(){
		return display; 
	}


	public void centerNearTopShell(Shell shell) {
		final Monitor primary = shell.getDisplay().getPrimaryMonitor();
		final Rectangle bounds = primary.getBounds();
		final Rectangle rect = shell.getBounds();
		final int x = bounds.x + (bounds.width - rect.width) / 2;
		final int y = bounds.y + (bounds.height - rect.height) / 3;
		shell.setLocation(x, y);
	}



	public static void main(String[] args) {
		ApplicationMainPage page = new ApplicationMainPage();
		page.buildPage();

		while(!page.GetShell().isDisposed()){
			if(!page.GetDisplay().readAndDispatch()){
				page.GetDisplay().sleep();
			}
		}
		page.DestroyGUI();
	}
}
