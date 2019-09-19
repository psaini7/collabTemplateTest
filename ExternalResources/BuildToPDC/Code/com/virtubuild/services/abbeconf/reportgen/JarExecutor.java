package com.virtubuild.services.abbeconf.reportgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.clientgui.ext.CustomUnit;
import com.virtubuild.core.WorldComponent;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.StateValue;

public class JarExecutor extends CustomUnit {
	private static final Logger LOGGER = LoggerFactory.getLogger(JarExecutor.class);
	private static final long serialVersionUID = 1L;
	private BufferedReader error;
	private BufferedReader op;
	private int exitVal;
	static final Class<?> referenceClass = JarExecutor.class;
	private String osDir = System.getenv("SystemDrive") + "\\econftemp\\";
	private static final String SWITCHBOARD_TWINLINE = "switchboard_twinline";
	private static final String SWITCHBOARD_RBBS = "switchboard_rbbs";
	private static final String SWITCHBOARD_N815 = "switchboard_n185";

	public void executeJar(Configuration configuration, String outputDir) throws JarException {
		try {
			Runtime re = Runtime.getRuntime();
			if (configuration.getComponentFromToa(SWITCHBOARD_TWINLINE, 0) != null) {
				Process command = re.exec(
						"cmd /c start /wait javaw -jar " + this.osDir + "\\ReportGenerator\\ReportGenerator.jar -i "
								+ this.osDir + "\\ReportGenerator\\input\\input.xml -t " + this.osDir
								+ "\\ReportGenerator\\documentTwinline -o " + this.osDir + "\\ReportGenerator\\output");

				this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
				this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));

				command.waitFor();

				this.exitVal = command.exitValue();
				if (this.exitVal != 0) {
					throw new IOException("Failed to execure jar, " + getExecutionLog());
				}
				openGeneratedFileTwinline(outputDir);
			} else if (configuration.getComponentFromToa(SWITCHBOARD_RBBS, 0) != null) {
				Process command = re.exec(
						"cmd /c start /wait javaw -jar " + this.osDir + "\\ReportGenerator\\ReportGenerator.jar -i "
								+ this.osDir + "\\ReportGenerator\\input\\input.xml -t " + this.osDir
								+ "\\ReportGenerator\\documentRbbs -o " + this.osDir + "\\ReportGenerator\\output");

				this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
				this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));

				command.waitFor();

				this.exitVal = command.exitValue();
				if (this.exitVal != 0) {
					throw new IOException("Failed to execure jar, " + getExecutionLog());
				}
				openGeneratedFileRbbs(outputDir);
			} else if (configuration.getComponentFromToa(SWITCHBOARD_N815, 0) != null) {
				Process command = re.exec(
						"cmd /c start /wait javaw -jar " + this.osDir + "\\ReportGenerator\\ReportGenerator.jar -i "
								+ this.osDir + "\\ReportGenerator\\input\\input.xml -t " + this.osDir
								+ "\\ReportGenerator\\documentn185 -o " + this.osDir + "\\ReportGenerator\\output");

				this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
				this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));

				command.waitFor();

				this.exitVal = command.exitValue();
				if (this.exitVal != 0) {
					throw new IOException("Failed to execure jar, " + getExecutionLog());
				}
				openGeneratedFileN185(outputDir);
			} else {
				Process command = re.exec(
						"cmd /c start /wait javaw -jar " + this.osDir + "\\ReportGenerator\\ReportGenerator.jar -i "
								+ this.osDir + "\\ReportGenerator\\input\\input.xml -t " + this.osDir
								+ "\\ReportGenerator\\document -o " + this.osDir + "\\ReportGenerator\\output");

				this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
				this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));

				command.waitFor();

				this.exitVal = command.exitValue();
				if (this.exitVal != 0) {
					throw new IOException("Failed to execure jar, " + getExecutionLog());
				}
				openGeneratedFile(outputDir);
			}
		} catch (IOException | InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public String getExecutionLog() {
		String error = "";
		try {
			String line;
			while ((line = this.error.readLine()) != null) {
				error = error + "\n" + line;
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		String output = "";
		try {
			String line;
			while ((line = this.op.readLine()) != null) {
				output = output + "\n" + line;
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		try {
			this.error.close();
			this.op.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		return "exitVal: " + this.exitVal + ", error: " + error + ", output: " + output;
	}

	public void openGeneratedFile(String outputDir) throws IOException, InterruptedException {
		Configuration configuration = getConfiguration();

		WorldComponent world = configuration.getWorldComponent();
		StateValue doc_format = world.getStateVector().getVar("doc_format").getValue();
		if (doc_format.toString().equalsIgnoreCase("pdf (.pdf)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\tender_doc.pdf").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\tender_doc.pdf");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".pdf");

				if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success");

						Process p = Runtime.getRuntime()
								.exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
						p.waitFor();
					} else {
						infoBox("File tender_doc.pdf is already open. Please close it and run again.", "Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime()
							.exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		} else if (doc_format.toString().equalsIgnoreCase("docx (.docx)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\tender_doc.docx").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\tender_doc.docx");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".docx");

				if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success Message");

						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
						p.waitFor();
					} else {
						infoBox("File tender_doc.docx is already open. Please close it and run again.", "Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		}
	}

	public void openGeneratedFileTwinline(String outputDir) throws IOException, InterruptedException {
		Configuration configuration = getConfiguration();

		WorldComponent world = configuration.getWorldComponent();

		StateValue doc_format = world.getStateVector().getVar("doc_format").getValue();
		if (doc_format.toString().equalsIgnoreCase("pdf (.pdf)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_twinline_tender_text.pdf").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_twinline_tender_text.pdf");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".pdf");

				if (outputDir == "") {
					LOGGER.info("You choose cancel.");
				} else if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success");

						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
						p.waitFor();
					} else {
						infoBox("File eConfigure_twinline_tender_text.pdf is already open. Please close it and run again.",
								"Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		} else if (doc_format.toString().equalsIgnoreCase("docx (.docx)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_twinline_tender_text.docx").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_twinline_tender_text.docx");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".docx");

				if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success Message");

						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
						p.waitFor();
					} else {
						infoBox("File eConfigure_twinline_tender_text.docx is already open. Please close it and run again.",
								"Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		}
	}

	public void openGeneratedFileRbbs(String outputDir) throws IOException, InterruptedException {
		Configuration configuration = getConfiguration();

		WorldComponent world = configuration.getWorldComponent();

		StateValue doc_format = world.getStateVector().getVar("doc_format").getValue();
		if (doc_format.toString().equalsIgnoreCase("pdf (.pdf)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_RBBS_Tender_Text.pdf").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_RBBS_Tender_Text.pdf");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".pdf" );

				if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success");

						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
						p.waitFor();
					} else {
						infoBox("File eConfigure_RBBS_Tender_Text.pdf is already open. Please close it and run again.",
								"Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		} else if (doc_format.toString().equalsIgnoreCase("docx (.docx)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_RBBS_Tender_Text.docx").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_RBBS_Tender_Text.docx");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".docx");

				if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success Message");

						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
						p.waitFor();
					} else {
						infoBox("File eConfigure_RBBS_Tender_Text.docx is already open. Please close it and run again.",
								"Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		}
	}

	public void openGeneratedFileN185(String outputDir) throws IOException, InterruptedException {
		Configuration configuration = getConfiguration();

		WorldComponent world = configuration.getWorldComponent();
		StateValue doc_format = world.getStateVector().getVar("doc_format").getValue();
		if (doc_format.toString().equalsIgnoreCase("pdf (.pdf)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_N185_Tender_Text.pdf").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_N185_Tender_Text.pdf");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".pdf");

				if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success");

						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
						p.waitFor();
					} else {
						infoBox("File eConfigure_N185_Tender_Text.pdf is already open. Please close it and run again.",
								"Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".pdf");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		} else if (doc_format.toString().equalsIgnoreCase("docx (.docx)")) {
			if (new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_N185_Tender_Text.docx").exists()) {

				File sourceFile = new File(this.osDir + "\\ReportGenerator\\output\\eConfigure_N185_Tender_Text.docx");
				
				//String timeStampStr = new SimpleDateFormat("MM.dd.HH.mm.ss").format(new Date());
				
				File targetFile = new File(outputDir + ".docx");

				if (targetFile.exists()) {
					if (targetFile.renameTo(targetFile)) {
						copyFile(sourceFile, targetFile);

						deleteDirectory(this.osDir + "\\ReportGenerator");

						infoBox("Report Generated Successfully", "Success Message");

						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
						p.waitFor();
					} else {
						infoBox("File eConfigure_N185_Tender_Text.docx is already open. Please close it and run again.",
								"Warning");
					}
				} else {
					copyFile(sourceFile, targetFile);

					deleteDirectory(this.osDir + "\\ReportGenerator");

					infoBox("Report Generated Successfully", "Success");

					Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + outputDir + ".docx");
					p.waitFor();
				}
			} else {
				LOGGER.error("File does not exist");
			}
		}
	}

	private static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, 1);
	}

	private void copyFile(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
	}

	private void deleteDirectory(String source) throws IOException {
		FileUtils.deleteDirectory(new File(source));
	}

	protected void refresh() {
	}

	public void init() {
	}

	public void cleanUp() {
	}
}
