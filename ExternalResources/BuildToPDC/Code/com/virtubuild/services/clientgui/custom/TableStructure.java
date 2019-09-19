package com.virtubuild.services.clientgui.custom;

import java.awt.BorderLayout;
import com.virtubuild.services.abbeconf.exportcopper.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.clientgui.ext.CustomUnit;
import com.virtubuild.core.WorldComponent;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.Dataset;
import com.virtubuild.core.api.StateControl;
import com.virtubuild.core.api.StateValue;

public class TableStructure extends CustomUnit {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableStructure.class);

	private JTextArea text = new JTextArea();
	private String exporterid = "myexporter";

	public static Integer dynamicFlag = 0;
	public static boolean changeOccured = false;

	public static boolean bomCompareStatus = false;
	public static boolean bolCompareStatus = false;
	public static boolean engBolCompareStatus = false;
	
	private JPanel loadPanel;

	private JLabel loadLabel;
	
	static ImageIcon loadingIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("resources/loader.gif"));
	
	private static final String CONSTANT_EXPORTERID = "exporterid";
	
	BOMExporter expDemo;
	
	Configuration configuration = getConfiguration();
	
	WorldComponent world = configuration.getWorldComponent();
	
	static boolean refreshCalledOnce = false;

	static {
		Runnable gui = new Runnable() {

			public void run() {
				try {

					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.put("Tree.paintLines", Boolean.FALSE);

				} catch (Exception e) {
					LOGGER.error("Error " + e.getMessage());
				}
			}
		};

		GraphicsEnvironment ge = null;
		try {
			ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
					Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/ABBvoice_Lt.ttf")));
			for (Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
				Object key = entry.getKey();
				Object value = UIManager.get(key);
				if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
					Font font2 = new Font("ABBvoice Light", Font.PLAIN, 14);
					UIManager.put(key, font2.deriveFont(14));
				}
			}

		} catch (FontFormatException e) {
			LOGGER.error("Font in bombol not being read " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Font file in bombol not being read " + e.getMessage());
		}

		SwingUtilities.invokeLater(gui);
	}

	@Override
	public void init() {
		
		//System.out.println("Called! inside init()");

		expDemo = new BOMExporter();
		
		TreeTableMain.excelSyndicatePath = getGUIController().getManager().getSystem().getURL("design document/Syndication_SYN85233(02-12-2019)(11-37-CET)_WithLinks.xlsx").toString();
		
		TreeTableMain.excelABBLibraryPath = getGUIController().getManager().getSystem().getURL("design document/SPEP ABB Library links.xlsx").toString();
		
		TreeTableMain.excelSyndicatePath = TreeTableMain.excelSyndicatePath.replaceAll("%20", " ");
	        
		TreeTableMain.excelABBLibraryPath = TreeTableMain.excelABBLibraryPath.replaceAll("%20", " ");
	        
		TreeTableMain.excelSyndicatePath = TreeTableMain.excelSyndicatePath.replaceAll("file:/", "");
	        
		TreeTableMain.excelABBLibraryPath = TreeTableMain.excelABBLibraryPath.replaceAll("file:/", "");
		
		//**************For Copper Export****************************************
		String sExcelRBBSCopper=getGUIController().getManager().getSystem().getURL("design document/BOM SPEP RBBS V9.xlsx").toString();
		String sExcelN185Copper=getGUIController().getManager().getSystem().getURL("design document/BOM_SPEP_185_Rev6.xlsx").toString();
		String sExcelStandard=getGUIController().getManager().getSystem().getURL("design document/Standardized components SPEP RBBS_Rev1.xlsx").toString();
		
		TreeTableMain.buildDesignDocumentPath = getGUIController().getManager().getSystem().getURL("design document").toString();
		TreeTableMain.buildDesignDocumentPath = TreeTableMain.buildDesignDocumentPath.replaceAll("%20", " ");
		TreeTableMain.buildDesignDocumentPath = TreeTableMain.buildDesignDocumentPath.replaceAll("file:/", "");
		
		
		TreeTableMain.buildCustomPath = getGUIController().getManager().getSystem().getURL("custom").toString();
		TreeTableMain.buildCustomPath = TreeTableMain.buildCustomPath.replaceAll("%20", " ");
		TreeTableMain.buildCustomPath = TreeTableMain.buildCustomPath.replaceAll("file:/", "");
		
		sExcelRBBSCopper= sExcelRBBSCopper.replaceAll("%20", " ");
		sExcelRBBSCopper= sExcelRBBSCopper.replaceAll("file:/", "");
		sExcelN185Copper= sExcelN185Copper.replaceAll("%20", " ");
		sExcelN185Copper= sExcelN185Copper.replaceAll("file:/", "");
		sExcelStandard= sExcelStandard.replaceAll("%20", " ");
		sExcelStandard= sExcelStandard.replaceAll("file:/", "");
		TreeTableMain.excelCopperRBBS= sExcelRBBSCopper;
		TreeTableMain.excelCopperN185= sExcelN185Copper;
		TreeTableMain.excelStandardGeneral= sExcelStandard;
		//**************End of Copper Export*************************************
		
		TreeTableMain.readInputExcelFiles();
		TreeTableMain.listMasterStandardFileData=CopperCheck.ReadStandardExcelFile2(TreeTableMain.excelStandardGeneral);
		
		
		initGUI();
		
	}
	
	/**
     * This method initializes UI components like Buttons,ImageIcons etc at the time of application
     * loading
     */
    public void initLoading() {
    	
        this.removeAll();
        
        loadLabel = new JLabel(loadingIcon, SwingConstants.CENTER);
        loadLabel.setText("In Progress");
        loadLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        loadLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        loadPanel = new JPanel(new BorderLayout());
        
        loadPanel.add(loadLabel, BorderLayout.CENTER);

        this.add(loadPanel, BorderLayout.CENTER);

        this.repaint();
        this.revalidate();
        

    }
	
	
	public void initGUI() {

		this.removeAll();
		
		//initLoading();
		
		JPanel panel = new TreeTableMain();
		panel.setVisible(true);
		
		panel.setBackground(Color.WHITE);

		// Once initialized, remove the loading icon panel
		
		//this.remove(loadPanel);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.add(panel);

		this.setSize(800, 1100);
		this.setVisible(true);
		this.repaint();
		this.revalidate();
		
		if (getModelData() != null) {
			initData(getModelData());
		}

	}
	

	private void initData(Dataset data) {
		if (data.existConstant(CONSTANT_EXPORTERID)) {
			exporterid = data.getConstantValue(CONSTANT_EXPORTERID);
			if (!getExporterController().existExporter(exporterid)) {
				LOGGER.error("The exporter '{}' was not found in the model", exporterid);
			} else {
				LOGGER.info("Found exporter '{}' ", exporterid);
			}
		}
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public void refresh() {
		
		TreeTableMain.productLine = world.getStateVector().getVar("system").getValue().toString();
		
		//StateValue flagInsideResultsTab = world.getStateVector().getVar("inside_results_tab").getValue();
		
		//StateValue flagBOMRefreshCalled = world.getStateVector().getVar("bom_refreshed_called").getValue();
		
		//System.out.println("inside Results? " + stateValue);
		
		//initLoading();
		
		//System.out.println("Inside refresh()");
		
		// Refresh only if the user is inside the Results tab
		//if(flagInsideResultsTab.toString().equals("1") && flagBOMRefreshCalled.toString().equals("0")){
			
			    //refreshCalledOnce = true;
			
			    //world.getStateVector().getVar("bom_refreshed_called").setValue("1");
			
				//System.out.println("refresh() Refreshing...");
			
				getGUIController().performBigOperation( ()->{
			
				String xml = getExporterController().runExporter("custom_exporter");
		
				expDemo.refineData(xml);
				
				//this.remove(loadPanel);
		
				initGUI();
		
				this.repaint();
				this.revalidate();
			
			} ,this ); 
		
		//}
		
		/*else if(flagInsideResultsTab.toString().equals("0")){
			
			//refreshCalledOnce = false;
			 world.getStateVector().getVar("bom_refreshed_called").setValue("1");
			
		}*/
		
	}
	
	
	@Override
	protected void refreshNonShowing() {

		
		LOGGER.info("refreshNonShowing called");
		LOGGER.debug("BOM");
		String sourcetext = null;
		text.setText(sourcetext);

		LOGGER.info("refreshNOnShowing refresh done : ");
	}

	/**
	 * Returns the value of variable passed to the method
	 * @param input
	 * @return String
	 */
	public String getConfig(String input) {
		String output = null;

		Configuration c = getConfiguration();
		WorldComponent world = c.getWorldComponent();
		StateControl var = world.getStateVector().getVar(input);
		output = var.getValue().toString();
		return output;
	}

	/**
	 * Sets the value of the variable passed to the method
	 * @param input
	 * @param setValue
	 * @return boolean
	 */
	public boolean setConfig(String input, String setValue) {
		boolean status = true;
		try {
			Configuration config = getConfiguration();
			WorldComponent worldComp = config.getWorldComponent();
			StateControl stateVar = worldComp.getStateVector().getVar(input);
			stateVar.setValue(setValue);
		} catch (Exception e) {
			LOGGER.error("Error in setConfig. Error: " + e.getMessage());
		}
		return status;
	}

	//************************************Section to Read All the excel files***********************
	
	//**********************************End of Region***********************************************
}
