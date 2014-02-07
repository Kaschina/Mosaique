package mos.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import mos.img.calc.Painter;
import mos.img.calc.TileCalculator;
import mos.init.Initializer;
import mos.init.UiConfiguration;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;

public class Uiuiui {

	private JFrame frame;
	private JTextField text;
	private final String[] extensions = { "jpg", "png" };
	private File source;
	private JLabel sourceImage;
	private final int MIN = 0;
	private final int MAX = 400;
	private BufferedImage bi;
	private JSlider slider;
	private final Initializer init;
	private String info;
	private String destination;
	private String mosaiquesource;
	private final String MS = "mosaiquesource";
	private final String INFO = "info";
	private final String DEST = "destination";
	private final String COMM = "die Daten";
	private final String PDIR = ".properties";
	private final Properties properties;
	private final String WAITTIME = "waittime";
	private final String NOT = "numberOfThreads";
	protected boolean fromInfoFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Uiuiui window = new Uiuiui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Uiuiui() {
		init = new Initializer();
		properties = new Properties(System.getProperties());
		try {
			properties.load(new FileReader(new File(PDIR)));
			properties.list(System.out);
		} catch (FileNotFoundException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		if (!new File(PDIR).exists()) {
			try {
				new File(PDIR).createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		info = getProperty(INFO);
		mosaiquesource = getProperty(MS);

		frame = new JFrame();
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.getContentPane().setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLocation(24, 12);
		frame.getContentPane().add(panel);
		panel.setSize(frame.getWidth(), frame.getHeight());
		panel.setLayout(new BorderLayout());

		JPanel northpanel = new JPanel();
		GridLayout gl = new GridLayout(1, 2);
		northpanel.setLayout(gl);
		JButton label = new JButton("Bilderquelle");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showSource();
			}
		});
		label.setBounds(20, 20, 100, 30);
		northpanel.add(label);

		text = new JTextField();
		text.setBounds(120, 20, 300, 30);
		text.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!text.getText().equals("")) {
					return;
				}
				showSource();
			}
		});
		northpanel.add(text);
		panel.add(BorderLayout.NORTH, northpanel);

		sourceImage = new JLabel();
		source = new File("/home/melanie/Dokumente/Beispielbilder/img0001.png");
		try {
			bi = ImageIO.read(source);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		sourceImage.setIcon(new ImageIcon(bi));
		sourceImage.setHorizontalAlignment(JLabel.CENTER);
		sourceImage.setVerticalAlignment(JLabel.CENTER);
		JScrollPane scroller = new JScrollPane(sourceImage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(BorderLayout.CENTER, scroller);

		JPanel southpanel = new JPanel();
		GridLayout sgl = new GridLayout(1, 2);
		southpanel.setLayout(sgl);
		slider = new JSlider();
		slider.setMinimum(MIN);
		slider.setMaximum(MAX);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(200);
		// Turn on labels at major tick marks.
		slider.setMajorTickSpacing(100);
		slider.setMinorTickSpacing(50);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println(slider.getValue());
				Initializer init = new Initializer();
				int tileSize = init.getOptTileSize(bi, slider.getValue());
				paintGridImage(tileSize);
			}
		});
		southpanel.add(slider, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Konvertierung starten");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				readProperties();
				UiConfiguration config = null;
				if (fromInfoFile) {
					config = new UiConfiguration(source.getAbsolutePath(),
							getProperty(DEST), "", getProperty(INFO), slider
									.getValue(), getIntProperty(WAITTIME),
							getIntProperty(NOT));
				} else {
					config = new UiConfiguration(source.getAbsolutePath(),
							getProperty(DEST), getProperty(MS), "", slider
									.getValue(), getIntProperty(WAITTIME),
							getIntProperty(NOT));
				}
				init.init(config, sourceImage);

			}
		});
		btnNewButton.setBounds(230, 490, 190, 25);
		southpanel.add(btnNewButton);

		panel.add(BorderLayout.SOUTH, southpanel);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		JMenuItem item = new JMenuItem("neue Sourcedatei");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("neue Sourcedatei");
				showSource();
			}
		});
		JMenuItem item2 = new JMenuItem("neues Mosaikverzeichnis");
		item2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getMosaiqueDir();
			}
		});
		JMenuItem item3 = new JMenuItem("neues Zielverzeichnis");
		item3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getDestinationDir();
			}

		});

		JMenuItem item6 = new JMenuItem("Infodatei");
		item6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getInfoFile();
			}
		});
		mnDatei.add(item);
		mnDatei.add(item2);
		mnDatei.add(item3);
		mnDatei.add(item6);

		JMenu mnKonfiguration = new JMenu("Konfiguration");
		menuBar.add(mnKonfiguration);

		JMenuItem item4 = new JMenuItem("Wartezeit festlegen");
		item4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(getIntProperty(WAITTIME));
				showDialog(frame, WAITTIME);
			}
		});
		JMenuItem item5 = new JMenuItem("Anzahl Threads festlegen");
		item5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(getIntProperty(NOT));
				showDialog(frame, NOT);
			}
		});
		JMenuItem item7 = new JMenuItem("Infodatei erzeugen");
		item7.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showInfoDialog();
			}
		});
		mnKonfiguration.add(item4);
		mnKonfiguration.add(item5);
		mnKonfiguration.add(item7);

		final JCheckBoxMenuItem chckbxmntmNewCheckItem = new JCheckBoxMenuItem(
				"Mit Infodatei erzeugen");
		chckbxmntmNewCheckItem.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (chckbxmntmNewCheckItem.isSelected()) {
					System.out.println("mit Info");
					fromInfoFile = true;
				}
			}
		});
		mnKonfiguration.add(chckbxmntmNewCheckItem);

		final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("Test");
		checkBoxMenuItem.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (checkBoxMenuItem.isSelected()) {
					System.out.println("test wurde ausgewählt");
				}
			}
		});
		mnKonfiguration.add(checkBoxMenuItem);
	}

	protected void showInfoDialog() {
		if (JOptionPane
				.showConfirmDialog(
						frame,
						"Soll eine Datei mit Infos über die Mosaikquelle erzeugt werden?\nDas Generieren eines Mosaikbildes beschleunigt sich dadurch") == 0) {
			while (info.equals("")) {
				getInfoFile();
			}
			while (mosaiquesource.equals("")) {
				getMosaiqueDir();
			}
			System.out.println("createFileInfo");
			init.createInfoFile(info, mosaiquesource);
		}
	}

	protected int getIntProperty(String key) {
		String resultString = getProperty(key);
		int result;
		try {
			result = Integer.parseInt(resultString);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	protected void setWaittime() {

	}

	private void getInfoFile() {
		info = "";
		File dir = getPath(false, false, "Infodatei auswählen");
		if (dir == null) {
			return;
		}
		if (!dir.isFile()) {
			try {
				dir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		info = dir.getAbsolutePath();
		setProperty(INFO, info);
	}

	private void getDestinationDir() {
		File dir = getPath(true, false, "Zielverzeichnis auswählen");
		if (dir == null) {
			return;
		}
		destination = dir.getAbsolutePath();
		if (!dir.isDirectory()) {
			dir.mkdir();
		}
		setProperty(DEST, destination);

	}

	private void getMosaiqueDir() {
		File dir = getPath(true, false, "Mosaikverzeichnis auswählen");
		if (dir == null)
			return;
		mosaiquesource = dir.getAbsolutePath();
		setProperty(MS, mosaiquesource);
	}

	private void showSource() {
		source = getPath(false, true, "Bild auswählen");

		if (source == null) {
			System.out.println("no valid picture");
			return;
		}
		try {
			bi = ImageIO.read(source);
			text.setText(source.getAbsolutePath());
			paintGridImage(slider.getValue());
		} catch (Exception e) {

		}
	}

	private void setProperty(String key, String value) {

		File file = new File(PDIR);
		try {
			Writer writer = new FileWriter(file);

			if (!file.isFile()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			properties.setProperty(key, value);
			properties.store(writer, COMM);
		} catch (IOException e1) {
			// TODO Automatisch generierter Erfassungsblock
			e1.printStackTrace();
		}
	}

	private String getProperty(String key) {
		String value = "";

		Reader reader = null;
		try {
			reader = new FileReader(PDIR);
			properties.load(reader);
			value = properties.getProperty(key, "");
			System.out.println(key + " " + value);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	private void readProperties() {

		info = getProperty(INFO);
		mosaiquesource = getProperty(MS);
		destination = getProperty(DEST);

		if (info.equals("") && mosaiquesource.equals("")) {
			getMosaiqueDir();
			getInfoFile();
		}
		if (destination.equals("")) {
			getDestinationDir();

		}
		if (!new File(destination).isDirectory()) {
			System.out.println("ist kein verzeichnis");
			System.out.println(new File(destination).mkdirs());
		}
	}

	public void showDialog(JFrame parent, String type) {
		int value = getIntProperty(type);
		SpinnerNumberModel model = new SpinnerNumberModel(value, 0, 40000, 1);
		JSpinner spinner = new JSpinner(model);
		String result = JOptionPane.showInputDialog(spinner);
		if (!result.equals(""))
			setProperty(type, result);
	}

	private File getPath(boolean isDir, boolean needsFilter, String text) {
		File result = null;
		JFileChooser chooser = new JFileChooser(text);
		chooser.setName(text);

		chooser.setAccessory(new MyAccessory(chooser));

		if (needsFilter) {
			chooser.setFileFilter(new MyFileFilter(extensions));
			chooser.showDialog(null, text);

		} else if (isDir) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.showDialog(null, text);
		} else
			chooser.showDialog(null, text);
		result = chooser.getSelectedFile();
		return result;
	}

	private void paintGridImage(int tileSize) {
		int height = bi.getHeight();
		int width = bi.getWidth();
		int opttileSize = new TileCalculator(height, width)
				.getOptSize(tileSize);
		Painter painter = new Painter(bi, opttileSize);
		BufferedImage buffim = painter.paintGrid();
		sourceImage.setIcon(new ImageIcon(buffim));

	}

	private static void setLookAndFeel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class MyFileFilter extends FileFilter {

	private String[] extensions;

	MyFileFilter(String[] extensions) {
		this.extensions = extensions;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		for (String extension : extensions) {
			System.out.println(extension);
			boolean result = f.getAbsolutePath().toLowerCase()
					.endsWith(extension);
			if (result) {
				System.out.println(f.getAbsolutePath() + " accepted");
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "es sind nur Bilddateien erlaubt";
	}

}

class MyAccessory extends JComponent implements PropertyChangeListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 2626268910546018039L;

	private java.awt.Image image;

	public MyAccessory(JFileChooser chooser) {
		// Listen for changes to the selected file
		chooser.addPropertyChangeListener(this);

		// Set a preferred size
		setPreferredSize(new Dimension(150, 150));
		this.setBounds(0, 10, 0, 0);
		this.setBorder(BorderFactory.createEtchedBorder());
	}

	// This listener listens for changes to the selected file
	public void propertyChange(PropertyChangeEvent evt) {
		if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt
				.getPropertyName())) {
			// Get the new selected file
			File newFile = (File) evt.getNewValue();

			// Prepare the preview data based on the new selected file
			try {
				image = Toolkit.getDefaultToolkit()
						.getImage(newFile.toString());
				MediaTracker mediaTracker = new MediaTracker(this);
				mediaTracker.addImage(image, 0);
				mediaTracker.waitForID(0);
			} catch (Exception ie) {
			}
			;
			// Repaint this component
			repaint();
		}
	}

	public void paint(Graphics g) {
		try {
			g.drawImage(image, 10, 0, 150, 150, this.getBackground(), null);
		} catch (NullPointerException np) {

		}
	}
}
