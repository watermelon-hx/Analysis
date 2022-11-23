package cn.cmcc.oneos.cmiot.hanxu.analysistool.core;

import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ProgressPage{

	public static Display display;
	protected Object result;
	public Shell shell;
	public Text text;
	public ProgressBar progressBar;

	public ProgressPage(Display display) {
		this.display = display;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public void open(ProgressPage gui,HashSet<String> files,String distFile) {
		createContents();
		shell.open();
		shell.layout();

		new Thread(new MainThread(gui,files,distFile)).start();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		DestroyGUI();
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(display, SWT.DIALOG_TRIM | SWT.MIN);
		shell.setSize(732, 361);
		shell.setText("正在生成文件");
		centerNearTopShell(shell);

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 726, 332);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 61, 17);
		lblNewLabel.setText("进度信息");

		text = new Text(composite, SWT.BORDER | SWT.MULTI);
		text.setBounds(10, 33, 681, 224);

		progressBar = new ProgressBar(composite, SWT.SMOOTH);
		progressBar.setBounds(10, 279, 681, 32);

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

	public void UpdateProgressBar(final int selection_value){
		if(progressBar.isDisposed()) return;		
		progressBar.setSelection(selection_value); 
		progressBar.setState(selection_value);
	}

	public void UpdateText(final String updateInfo){
		if(text.isDisposed()) return;		
		text.append(updateInfo);
	}

	public void closeShell(){
		if(shell.isDisposed()) return;		
		shell.close();
	}

	public void centerNearTopShell(Shell shell) {
		final Monitor primary = shell.getDisplay().getPrimaryMonitor();
		final Rectangle bounds = primary.getBounds();
		final Rectangle rect = shell.getBounds();
		final int x = bounds.x + (bounds.width - rect.width) / 2;
		final int y = bounds.y + (bounds.height - rect.height) / 3;
		shell.setLocation(x, y);
	}
}
