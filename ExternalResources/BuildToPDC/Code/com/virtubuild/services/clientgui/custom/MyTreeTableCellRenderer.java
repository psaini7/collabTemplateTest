package com.virtubuild.services.clientgui.custom;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeModel;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTreeTableCellRenderer extends JTree implements TableCellRenderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyTreeTableCellRenderer.class);

	protected int visibleRow;

	private MyTreeTable treeTable;

	public static List<String> listPDF = new ArrayList<>();

	String filePath = null;

	int attachmentNumber = 0;

	private File fileToSave;

	private String prodCode = null;

	public MyTreeTableCellRenderer(MyTreeTable treeTable, TreeModel model) {
		super(model);
		this.treeTable = treeTable;
		this.putClientProperty("JTree.lineStyle", "None");

		setRowHeight(getRowHeight());
		
		treeTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int rowNumber = treeTable.getSelectedRow();
				int columnNumber = treeTable.getSelectedColumn();

				// Mouse event listener to open the URL to corresponding Product ID in BOM

				if (columnNumber == 1) {

					URI uri = null;

					try {

						String URIString = (String) treeTable.getModel().getValueAt(rowNumber, 11);

						if (URIString != null) {
							uri = new URI(URIString);
							open(uri);
						}

					} catch (URISyntaxException e1) {

						LOGGER.error("BOM: Error in generating URL " + e1.getMessage());

					}

				}

				else if (columnNumber == 7) {

					// Download all instruction manual corresponding to the product and zip it

					// init hard coded links : Prototype

					/*
					 * prodCode = (String) treeTable.getModel().getValueAt(rowNumber, 1);
					 * 
					 * 
					 * URL url; InputStream in;
					 * 
					 * JFrame saveDialogBox = new JFrame();
					 * 
					 * JFileChooser fileChooser = new JFileChooser();
					 * fileChooser.setDialogTitle("Save instruction manuals");
					 * 
					 * int userSelection = fileChooser.showSaveDialog(saveDialogBox);
					 * 
					 * fileToSave = fileChooser.getSelectedFile();
					 * 
					 * 
					 * if (userSelection == JFileChooser.APPROVE_OPTION) {
					 * 
					 * filePath = fileToSave.getAbsolutePath();
					 * 
					 * }
					 * 
					 */

					try {

						/*
						 * 
						 * new
						 * ProcessBuilder("C:\\Program Files\\Internet Explorer\\iexplore.exe","-new",
						 * "http://www.google.com").start(); new
						 * ProcessBuilder("C:\\Program Files\\Internet Explorer\\iexplore.exe","-new",
						 * "http://www.google.com").start();
						 * 
						 * 
						 * new ProcessBuilder("C:\\Program Files\\Mozilla Firefox\\firefox.exe"
						 * ,"-new-tab http://www.google.com").start(); new
						 * ProcessBuilder("C:\\Program Files\\Mozilla Firefox\\firefox.exe"
						 * ,"-new-tab http://www.google.com").start();
						 * 
						 */

						if (Desktop.isDesktopSupported()) {
							// Desktop.getDesktop().browse(new URI(pdf));

						}
						
						String TypeCode = (String) treeTable.getModel().getValueAt(rowNumber, 2);
						
						System.out.println("TypeCode: " + TypeCode);
						
						String[] URLs = TreeTableMain.mapTypeCodeURL.get(TypeCode).split(",");
						
						if (URLs != null){
							
								for(String URL : URLs){
								
								Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + URL);
								
							}
							
						}
						

						// FileUtils.copyURLToFile(new URL(pdf), new File(filePath + "\\file" +
						// (++attachmentNumber) +".pdf"));

						/*
						 * Commented code for testing.
						 * 
						 * System.out.println("URL: " + pdf);
						 * 
						 * // url = new
						 * URL("http://sparshv2/portals/tpd/Documents/The%20Team_May%202017.pdf");// It
						 * will connect to www.blablabla.com\document1.pdf
						 * 
						 * url = new URL(pdf);
						 * 
						 * in = (InputStream) url.openStream();
						 * 
						 * HttpURLConnection conn1 = (HttpURLConnection) url .openConnection();
						 * 
						 * // Inside Workfolder directory every PDF files in the list will be created
						 * and will automatically name it self like it supposed to be Files.copy(in,
						 * Paths.get("C:\\econftemp\\download" + (attachmentNumber++) +".pdf"),
						 * StandardCopyOption.REPLACE_EXISTING); System.out.print("\nPDF Sved!");
						 * 
						 * in.close();
						 * 
						 */


						// zipFiles();

					} catch (Exception e1) {
						
						// If the no Link present open default catalog
						try {
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "https://search.abb.com/library/Download.aspx?DocumentID=1STC803001D0201&LanguageCode=en&DocumentPartId=&Action=Launch");
						} catch (IOException e2) {
							LOGGER.error("Error downloading pdf files " + e1.getMessage());
						}
						
					}

				}

			}

		});

	}

	/**
	 * This method opens the passed URI in the default browser.
	 */
	private static void open(URI uri) {

		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				LOGGER.error("BOM: Error in opening the URL " + e.getMessage());
			}
		} else {
			LOGGER.error("Cannot open the URL");
		}

	}

	/**
	 * Tree and table must have the same height.
	 */
	public void setRowHeight(int rowHeight) {
		if (rowHeight > 0) {
			rowHeight = rowHeight + 6;
			super.setRowHeight(rowHeight);
			if (treeTable != null && treeTable.getRowHeight() != rowHeight) {
				treeTable.setRowHeight(getRowHeight());
			}
		}
	}

	/**
	 * 
	 * Tree must have the same height as Table.
	 */
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, 0, w, treeTable.getHeight());
	}

	/**
	 * 
	 * Ensures the folders are loaded.
	 */
	public void paint(Graphics g) {
		g.translate(0, -visibleRow * getRowHeight());

		super.paint(g);
	}

	/**
	 * 
	 * Returns the renderer with the matching background color.
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (isSelected) {

			setBackground(table.getSelectionBackground());

		} else {

			setBackground(table.getBackground());
		}

		visibleRow = row;

		return this;

	}
}