package mos.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class Uiui {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Uiui window = new Uiui();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(30, 30, 400, 400);
		lblNewLabel.setImage(new Image(null, "/home/melanie/Dokumente/Beispielbilder/img0001.png"));
		
		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setBounds(460, 30, 130, 20);
		lblNewLabel_3.setText("Picture");
		
		Label lblLabel_4 = new Label(shell, SWT.NONE);
		lblLabel_4.setBounds(460, 60, 130, 20);
		lblLabel_4.setText("Mosaique Directory");
		
		Label lblNewLabel_5 = new Label(shell, SWT.NONE);
		lblNewLabel_5.setBounds(460, 90, 130, 20);
		lblNewLabel_5.setText("Number of Threads");
		
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(460, 120, 130, 20);
		lblNewLabel_1.setText("Waiting Time");
		
		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(480, 150, 130, 20);
		lblNewLabel_2.setText("Destination");
		
		Label lblNewLabel_4 = new Label(shell, SWT.NONE);
		lblNewLabel_4.setBounds(480, 180, 130, 20);
		lblNewLabel_4.setText("New Label");
		
		text = new Text(shell, SWT.BORDER);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				gainedFocus(e);
			}
		});
		text.setBounds(615, 30, 130, 20);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(615, 60, 130, 20);
		text_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				gainedFocus(e);
			}
		});
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(615, 90, 130, 20);
		text_2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				gainedFocus(e);
			}
		});
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(615, 120, 130, 20);
		text_3.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				gainedFocus(e);
			}
		});
		text_4 = new Text(shell, SWT.BORDER);
		text_4.setBounds(615, 150, 130, 20);
		text_4.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				gainedFocus(e);
			}
		});
		text_5 = new Text(shell, SWT.BORDER);
		text_5.setBounds(615, 180, 130, 20);
		text_5.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				gainedFocus(e);
			}
		});


	}

	protected void gainedFocus(FocusEvent e) {
		System.out.println(e.getSource() + "gained focus");
	}
}
