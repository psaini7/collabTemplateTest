package com.virtubuild.services.abbeconf.export;

import static de.ksquared.jgaeb.gaeb1990.file.File.Identifier.KE_86;
import static de.ksquared.jgaeb.gaeb1990.group.Group.Name.GR_01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.Exporter;
import com.virtubuild.core.WorldComponent;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.custom.abbeconf.general.Switchboard;

import de.ksquared.jgaeb.gaeb1990.element.Element.Type;
import de.ksquared.jgaeb.gaeb1990.file.FileBuilder;
import de.ksquared.jgaeb.gaeb1990.frame.Frame;
import de.ksquared.jgaeb.gaeb1990.frame.FrameBuilder;
import de.ksquared.jgaeb.gaeb1990.group.GroupBuilder;

public class GAEBExporterTwinlineN extends Exporter {
	private static final Logger LOGGER = LoggerFactory.getLogger(GAEBExporterTwinlineN.class);
	public static String twinlineGaebPath = null;
	static final Class<?> referenceClass = GAEBExporterTwinlineN.class;
	static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";
	private String exporterXml = "";
	private static final String SWITCHBOARD_TWINLINE_TOA = "switchboard_twinline";
	private String gaebFileContent = " ";
	private String outputDir;
	private Configuration configuration;

	@Override
	public String getDataString() {
		prepareData();
		return this.gaebFileContent;
	}

	@Override
	public void prepareData(Collection<String> arg0) {

	}

	private void prepareData() {

		this.outputDir = selectFileAndValidate();

		if (outputDir == "") {
			LOGGER.info("You choose cancle.");
		} else {
			setPath();
			BaseComponent switchboard = getSwitchboard();
			if (switchboard == null || isTwinline(switchboard)) {

				exporterXml = getManager().getExporterController().runExporter("eConfigure_twinline_tender_text");

				File sourceDirectory = new File(twinlineGaebPath);
				File targetDirectory = new File(osDir + "\\TwinlineGaeb\\");

				directoryCopy(sourceDirectory, targetDirectory);

				TouchguardCombilineN touchguardCombilineN = new TouchguardCombilineN();
				touchguardCombilineN.prepareData(exporterXml, targetDirectory.toString());

				generateGaebFile(targetDirectory);

			}
		}
	}

	private void generateGaebFile(File targetDirectory) {
		try {
			FileBuilder twinlineGaebFile;
			GroupBuilder twinlineGaebFileGroup;
			FrameBuilder twinlineGaebFileFrame;

			twinlineGaebFile = new FileBuilder(KE_86); // optional group: GR_03 (this one could also be template of KE_84, with ZA_07 and without ZA_25)
			twinlineGaebFileGroup = twinlineGaebFile.newGroup(GR_01);

			// Open the file
			FileInputStream fstream = new FileInputStream(targetDirectory + "/Twinline_priliminary_tendertext.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				twinlineGaebFileFrame = twinlineGaebFileGroup.newFrame(Frame.Type.ZA_T1);
				twinlineGaebFileFrame.addElement(Type.TEXT, "  " + strLine);
			}
			// Close the input stream
			fstream.close();

			gaebFileContent = twinlineGaebFile.toString();

			FileUtils.writeStringToFile(new File(outputDir), gaebFileContent);

		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

	}

	private void setPath() {
		twinlineGaebPath = getManager().getSystem().getURL("custom/TwinlineGaeb").toString().replaceAll("file:/", "");
		twinlineGaebPath = twinlineGaebPath.replaceAll("%20", " ");

	}

	private static void directoryCopy(File source, File dest) {
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	private String selectFileAndValidate() {
		String sPath = "";
		JFrame saveDialogBox = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");
		this.configuration = getConfiguration();
		if (this.configuration.getComponentFromToa(SWITCHBOARD_TWINLINE_TOA, 0) != null) {
			fileChooser.setSelectedFile(new File("eConfigure_twinline_gaeb.x86"));
		}

		int userSelection = fileChooser.showSaveDialog(saveDialogBox);

		File fileToSave = fileChooser.getSelectedFile();
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			WorldComponent world = configuration.getWorldComponent();
			StateValue doc_format = world.getStateVector().getVar("doc_format").getValue();
			if (doc_format.toString().equalsIgnoreCase("x86 (.x86)")) {
				if (new File(fileToSave.getPath() + ".x86").exists()) {
					JOptionPane.showMessageDialog(new JFrame(), "File already exists!", "ABB e-Configure",
							JOptionPane.INFORMATION_MESSAGE);
					String sAnotherPath = selectFileAndValidate();
					if (sAnotherPath != "") {
						sPath = sAnotherPath;
					}
				} else
					sPath = fileToSave.getPath();
			}
		}
		return sPath;
	}

	/**
	 * checks whether the input BaseComponent is twinline switchboard
	 * 
	 * @param switchboard
	 * @return
	 */
	private boolean isTwinline(BaseComponent switchboard) {
		return switchboard.isTypeOfActor(SWITCHBOARD_TWINLINE_TOA);
	}

	/**
	 * gets the current switchboard
	 * 
	 * @return
	 */
	private BaseComponent getSwitchboard() {
		List<BaseComponent> switchboards = configuration.getTOAComponents(Switchboard.SWITCHBOARD_TOA);
		if (!switchboards.isEmpty()) {
			return switchboards.get(0);
		}

		LOGGER.info("No switchboards in current configuration.");
		return null;
	}

}
