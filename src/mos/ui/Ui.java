package mos.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;


import org.eclipse.swt.widgets.Label;


public class Ui extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Ui(Composite parent, int style) {
		super(parent, SWT.BORDER);
		setLayout(null);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(24, 24, 424, 424);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(20, 20, 420, 420);
		lblNewLabel.setImage(new Image(null, "/home/melanie/Dokumente/Beispielbilder/img0001.png"));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
