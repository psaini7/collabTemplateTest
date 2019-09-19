package com.virtubuild.services.clientgui.copperforecast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.List;
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
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.Dataset;
import com.virtubuild.core.api.StateControl;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.state.BaseVar;

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
	
	Configuration configuration = getConfiguration();
	
	WorldComponent world = configuration.getWorldComponent();
	
	static ImageIcon loadingIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("resources/loader.gif"));
	
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
		
		//world.getStateVector().getVar("copper_forecast_table_ready").setValue("0");
		
		//initLoading();
		
		/*
		//Checking if without copper is selected
		
		Configuration configuration = getConfiguration();
		List<Component> allComponents = configuration.getAllComponents();
		
		String switchboardID = null;
		
		for (Component component : allComponents){
			
			if ( component.getName().toString().contains("Switchboard") && ! component.getName().toString().contains("Switchboard no.") ){
				
				switchboardID = component.getID();
				
			}
			
		}
		
		if (null != switchboardID){
			
			Component switchboard = configuration.getComponent(switchboardID);
			
			StateValue order = switchboard.getStateVector().getVar("order").getValue();
			
			if( order.getID().equals("without_cu") ){
				
				JPanel panel = new TreeTableMain();
				panel.setVisible(true);

				this.add(panel);

				this.setSize(800, 1100);
				this.setVisible(true);
				this.repaint();
				this.revalidate();
				if (getModelData() != null) {
					initData(getModelData());
				}
				
			}
			
		}
		*/
		
		//System.out.println("initGUI Called!");
		
		
		/*getGUIController().performBigOperation( ()->{
			
			System.out.println("LOADING ICON...");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} ,this ); 
		*/
		
		//getGUIController().performBigOperation( ()->{
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.removeAll();
		
		JPanel panel = new TreeTableMain();
		panel.setVisible(true);
		
		//System.out.println("in init()\n Export button: " + world.getStateVector().getVar("export_copper") + "\n ");
		
		//System.out.println("Copper table ready? " + world.getStateVector().getVar("copper_forecast_table_ready"));
		
		//world.getStateVector().getVar("copper_forecast_table_ready").setValue("1");

		
		//this.remove(loadPanel);
		
		
		this.add(panel);

			
		//} ,this );
		
		this.setSize(800, 1100);
		this.setVisible(true);
		this.repaint();
		this.revalidate();
		if (getModelData() != null) {
			initData(getModelData());
		}

	}

	private static final String CONSTANT_EXPORTERID = "exporterid";

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
		
		//While refreshing disable copper export button
		
		//StateValue stateValue = world.getStateVector().getVar("copper_forecast_table_ready");
		
		//System.out.println("Copper table ready? " + stateValue + " " + stateValue.getID());
		
		// disabling the Export button
		
		//world.getStateVector().getVar("copper_forecast_table_ready").setValue("0");
		
		//initLoading();
		
		//System.out.println("COPPER FORECAST()");
		
		
		//StateValue stateValue = world.getStateVector().getVar("inside_results_tab").getValue();
		
		// Refresh only if the user is inside the Results tab
		//if(stateValue.toString().equals("1") && !refreshCalledOnce){
			
			//refreshCalledOnce = true;
			 
			//System.out.println("Copper parts refresh()");
			
			getGUIController().performBigOperation( ()->{
				
				BOMExporter expDemo = new BOMExporter();

				String xml = getExporterController().runExporter("custom_exporter");

				expDemo.refineData(xml);

				// once done refreshing enable back the copper export button
				
				//world.getStateVector().getVar("copper_forecast_table_ready").setValue("1");
						
				//this.remove(loadPanel);
				
				initGUI();
				
				this.repaint();
				this.revalidate();
				
			} ,this ); 
			
		//}
		
		/*else if(stateValue.toString().equals("0")){
			
			refreshCalledOnce = false;
			
		}*/
		
			
		
	}

	@Override
	protected void refreshNonShowing() {

		
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

}
