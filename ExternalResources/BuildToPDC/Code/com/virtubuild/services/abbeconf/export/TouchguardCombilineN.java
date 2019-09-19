package com.virtubuild.services.abbeconf.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TouchguardCombilineN {

	private static final Logger LOGGER = LoggerFactory.getLogger(TouchguardCombilineN.class);

	public void prepareData(String exporterXml, String targetDirectory) {

		String articleType = "";
		String minimumSectionDepth = "";
		String width = "";
		String quantityCounter = "";
		String height = "";
		String rowspacing = "";
		String placeunits = "";
		String busbarCurrent = "";
		String busbarOrientation ="";
		String busbarSpacing ="";
		String poles = "";
		String sleeveDiameter ="";
		String deviceType = "";
		String deviceWidthUsable="";
		String deviceDepthUsable = "";
		String ratedCurrent="";
		String moduleEquipment="";
		String deviceCount = "";
		String meterType ="";

		String id = "";
		String value = "";

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(exporterXml.getBytes("UTF-8"));
			Document doc = db.parse(is);

			NodeList nodes1 = doc.getElementsByTagName("blocks");

			for (int i1 = 0; i1 < nodes1.getLength(); i1++) {
				Element element1 = (Element) nodes1.item(i1);
				NodeList nodes2 = element1.getElementsByTagName("block");

				for (int i2 = 0; i2 < nodes2.getLength(); i2++) {
					Element element2 = (Element) nodes2.item(i2);
					String blockrefid = element2.getAttribute("refid");

					if (blockrefid.toLowerCase().endsWith("_dinrailmounting")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Dinrailmounting_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					

											if (i7 == 0) {
												rowspacing = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{row_spacing}", rowspacing);
											} else if (i7 == 1) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											}else if (i7 == 2) {
												placeunits = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_DIN_place_units}", placeunits);
											}else if (i7 == 3) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											} else if (i7 == 4) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											} else if (i7 == 5) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											} else if (i7 == 6) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
										

									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_touchguard_combiline")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Touchguard_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					
											if (i7 == 0) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											} else if (i7 == 1) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											} else if (i7 == 2) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											} else if (i7 == 3) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											} else if (i7 == 4) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
										

									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_busbar_combiline")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Busbarsystem_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					

											if (i7 == 0) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											}else if (i7 == 1) {
												busbarOrientation = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_busbar_orientation}", busbarOrientation);
											}else if (i7 == 2) {
												poles = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_poles}", poles);
											}else if (i7 == 3) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											}else if (i7 == 4) {
												busbarCurrent = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_busbar_current}", busbarCurrent);
											}else if (i7 == 5) {
												busbarSpacing = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_busbar_center_spacing}", busbarSpacing);
											}else if (i7 == 6) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											} else if (i7 == 7) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											} else if (i7 == 8) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
										

									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_cableconnection")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Cableconnection_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					

											if (i7 == 0) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											}else if (i7 == 1) {
												sleeveDiameter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_sleeve_diameter}", sleeveDiameter);
											}else if (i7 == 2) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											
											}else if (i7 == 3) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											}else if (i7 == 4) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											}  else if (i7 == 5) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
										

									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_circuitbreaker")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Circuitbreaker_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					

											if (i7 == 0) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											}else if (i7 == 1) {
												moduleEquipment = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_module_equipment}", moduleEquipment);
											}else if (i7 == 2) {
												poles = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_poles}", poles);
											}else if (i7 == 3) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											}else if (i7 == 4) {
												ratedCurrent = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_rated_continuous_current}", ratedCurrent);
											}else if (i7 == 5) {
												deviceWidthUsable = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_device_width_usable}", deviceWidthUsable);
											}else if (i7 == 6) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											}else if (i7 == 7) {
												deviceType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_device_type}", deviceType);
											} else if (i7 == 8) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											} else if (i7 == 9) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
										

									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_metermountingplate")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Metermountingplate_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					

											if (i7 == 0) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											}else if (i7 == 1) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											}else if (i7 == 2) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											}else if (i7 == 3) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											}else if (i7 == 4) {
												meterType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_meter_type}", meterType);
											} else if (i7 == 5) {
												deviceCount = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_device_count}", deviceCount);
											}else if (i7 == 6) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
					
									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_switchdisconnetorOT")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/SwitchdisconnectorOT_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					

											if (i7 == 0) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											}else if (i7 == 1) {
												moduleEquipment = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_module_equipment}", moduleEquipment);
											}else if (i7 == 3) {
												poles = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_poles}", poles);
											}else if (i7 == 4) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											}else if (i7 == 5) {
												ratedCurrent = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_rated_continuous_current}", ratedCurrent);
											}else if (i7 == 6) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											}else if (i7 == 7) {
												deviceType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_device_type}", deviceType);
											}else if (i7 == 8) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											}else if (i7 == 9) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
					
									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_universalswitchdisconnector")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Universalswitchdisconnector_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					

											if (i7 == 0) {
												deviceDepthUsable = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_device_depth_usable}", deviceDepthUsable);
											}
											else if (i7 == 1) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											}else if (i7 == 2) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											}else if (i7 == 3) {
												deviceWidthUsable = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_device_width_usable}", deviceWidthUsable);
											}else if (i7 == 4) {
												ratedCurrent = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_rated_continuous_current}", ratedCurrent);
											}else if (i7 == 5) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											}else if (i7 == 6) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											}else if (i7 == 7) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
					
									}
								}

							}
						}
					}
					if (blockrefid.toLowerCase().endsWith("_cable_fastening")) {

						NodeList nodes3 = element2.getElementsByTagName("datatables");
						for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
							Element element3 = (Element) nodes3.item(i3);

							NodeList nodes4 = element3.getElementsByTagName("datatable");
							for (int i4 = 0; i4 < nodes4.getLength(); i4++) {
								Element element4 = (Element) nodes4.item(i4);

								NodeList nodes5 = element4.getElementsByTagName("rows");
								for (int i5 = 0; i5 < nodes5.getLength(); i5++) {
									Element element5 = (Element) nodes5.item(i5);

									NodeList nodes6 = element5.getElementsByTagName("row");
									for (int i6 = 0; i6 < nodes6.getLength(); i6++) {
										Element element6 = (Element) nodes6.item(i6);

										NodeList nodes7 = element6.getElementsByTagName("datum");
										
										String content = new String(Files.readAllBytes(Paths.get(targetDirectory+"/Cablefastening_tendertext.txt")), StandardCharsets.UTF_8);
										Files.write(Paths.get(targetDirectory + "/Twinline_priliminary_tendertext.txt"), content.getBytes(), StandardOpenOption.APPEND);

										for (int i7 = 0; i7 < nodes7.getLength(); i7++) {
											Element element7 = (Element) nodes7.item(i7);
											
											value = element7.getAttribute("value");
																					
											if (i7 == 0) {
												articleType = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{article_type}", articleType);
											} else if (i7 == 1) {
												minimumSectionDepth = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_minimum_section_depth}", minimumSectionDepth);
											} else if (i7 == 2) {
												width = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_width\\}", width);
											} else if (i7 == 3) {
												quantityCounter = element7.getAttribute("value");
												searchReplace(targetDirectory,targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{quantity_counter\\}", quantityCounter);
											} else if (i7 == 4) {
												height = element7.getAttribute("value");
												searchReplace(targetDirectory, targetDirectory + "/Twinline_priliminary_tendertext.txt",
														"\\{module_height\\}", height);
											}

										}
										

									}
								}

							}
						}
					}

				}

			}
			
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}

	private void searchReplace(String targetDirectory,String absolutePath, String search, String replacement) {
		Path path = Paths.get(absolutePath);
		//Path finalFile = Paths.get(targetDirectory+"/Touchguard_tendertext_final.txt");
		Charset charset = StandardCharsets.UTF_8;

		String content = null;
		try {
			content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll(search, replacement);
			Files.write(path, content.getBytes(charset));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	 
}
