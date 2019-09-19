package com.virtubuild.services.clientgui.copperforecast;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.services.clientgui.custom.SyndicateBean;

@SuppressWarnings("serial")
public class TreeTableMain extends JPanel  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TreeTableMain.class);

	Color ABBRed = new Color(224, 2, 15); // Color ABB_Red

	static List<MyDataNode> nodesForTable = new ArrayList<>();

	static List<MyDataNode> listNodes = new ArrayList<>();
	
	static List<MyDataNode> listCompleteData;
	
	private List<List<Object>> listOfNodes;
	
	static List<MyDataNode> listOfFilteredNodes = new ArrayList<>();
	
	private static int columnIndexFiltered;

	MyDataNode node11;
	
	private MyTreeTable myTreeTable = null;

	public static int BOMTableFont = 14;

	public MyAbstractTreeTableModel treeTableModel; 
	
	private static Map<Integer, List<String>> groupedData = new HashMap<Integer, List<String>>();
	
	public static List<MyDataNode> listNodesForCopper= new ArrayList<>();
	
	private static Map<Integer, List<CheckListItem>> filterAppliedColumn = new HashMap<Integer, List<CheckListItem>>();
	
	private JPopupMenu popup;
	
	private static int columnSorted = -1;
	
	private TableRowSorter<TableModel> sorter;
	
	private JCheckBox[] checkBox;
	
	private List<String> checkBoxValues = new ArrayList<>();
	
    static boolean selectAll;
	
	static JList jlist = null;
	
	private JList listCheckBox = null;
	
	private List<CheckListItem> filtersCurrentColumn = new ArrayList<>();

	 static DefaultListModel listModel = null;
	
	// nth idex of the array corrsponds to the nth column (0 to n-1)
	private static boolean isColumnSorted[] = new boolean[10];
	
	private static List<Integer> columnWidth = new ArrayList<>();
	
	private HashMap<String, Float> mapCopperWidthDepth = new HashMap<>();

	private HashMap<Integer, HashMap<String, Float>> mapSortedColumnCopperParts;
	
	private String excelSyndicatePath;
	

	public TreeTableMain() {
	    
	    columnWidth.clear();
	    
	    initUI();
	    
	}
	
	
	
	public void initUI(){
	    
        RoundedButton expandAll = new RoundedButton("EXPAND ALL");
        expandAll.setMaximumSize(new Dimension(30, 30));
        expandAll.setBackground(ABBRed);
        expandAll.setForeground(Color.WHITE);
        expandAll.setFont(new Font("ABBvoice", Font.BOLD, 14));
        
        expandAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                MyTreeTable.expandAllNodes();

                MyTreeTable.treeNodesExpanded = true;

            }
        });

        RoundedButton collapseAll = new RoundedButton("COLLAPSE ALL");
        collapseAll.setMaximumSize(new Dimension(30, 30));
        collapseAll.setBackground(ABBRed);
        collapseAll.setForeground(Color.WHITE);
        collapseAll.setFont(new Font("ABBvoice", Font.BOLD, 14));
        

        collapseAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                MyTreeTable.collapseAllNodes();

                MyTreeTable.treeNodesExpanded = false;

            }
        });

        this.removeAll();
        
        treeTableModel = new MyDataModel(createDataStructureDynamic(listNodes, columnSorted));

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.setBackground(Color.WHITE);
        buttonPane.add(Box.createHorizontalGlue());

        RoundedButton zoomIn = new RoundedButton("+");
        zoomIn.setMaximumSize(new Dimension(30, 30));
        zoomIn.setFont(new Font("ABBvoice", Font.PLAIN, 25));
        zoomIn.setBackground(ABBRed);
        zoomIn.setForeground(Color.WHITE);

        RoundedButton zoomOut = new RoundedButton("-");
        zoomOut.setMaximumSize(new Dimension(30, 30));
        zoomOut.setFont(new Font("ABBvoice", Font.PLAIN, 30));
        zoomOut.setBackground(ABBRed);
        zoomOut.setForeground(Color.WHITE);

        buttonPane.add(zoomIn);

        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

        buttonPane.add(zoomOut);

        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

        buttonPane.add(expandAll);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(collapseAll);
        
            
        myTreeTable = new MyTreeTable(treeTableModel);
        
            
        sorter = new TableRowSorter<>(myTreeTable.getModel());
        
        myTreeTable.setRowSorter(sorter);
        
        sorter.setSortable(0, false);
        sorter.setSortable(1, false);
        sorter.setSortable(2, false);
        sorter.setSortable(3, false);
        sorter.setSortable(4, false);
        sorter.setSortable(5, false);
        
           
        
        zoomIn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                if (BOMTableFont <= 18)
                    BOMTableFont += 2;

                myTreeTable.setFont(new Font("ABBvoice Light", Font.PLAIN, BOMTableFont));

            }
        });

        zoomOut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                if (BOMTableFont > 10)
                    BOMTableFont -= 2;

                myTreeTable.setFont(new Font("ABBvoice Light", Font.PLAIN, BOMTableFont));

            }
        });

        JScrollPane scrollPane = new JScrollPane(myTreeTable);
        scrollPane.setBounds(40, 40, 1100, 400);
        scrollPane.setBackground(Color.WHITE);

        add(buttonPane);

        add(scrollPane);

        repaint();
        revalidate();
        setVisible(true);
        setSize(1100, 1100);

        myTreeTable.setFont(new Font("ABBvoice Light", Font.PLAIN, BOMTableFont));


        myTreeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        
        if(columnWidth.isEmpty()){
        
            columnWidth.add(0, 220);
            columnWidth.add(1, 220);
            columnWidth.add(2, 220);
            columnWidth.add(3, 220);
            columnWidth.add(4, 220);
            columnWidth.add(5, 300);
            
        }
        
        
        // Setting fixed width of the columns
        
        for (int i=0; i< columnWidth.size() ; i++){
        	
        	myTreeTable.getColumnModel().getColumn(i).setMinWidth(columnWidth.get(i));
            myTreeTable.getColumnModel().getColumn(i).setMaxWidth(columnWidth.get(i));
            myTreeTable.getColumnModel().getColumn(i).setWidth(columnWidth.get(i));
        	
        }
        
        
        this.repaint();
        this.revalidate();
	    
	    
	    
	}
	

	/**
	 * Method creates the BOM hierarchy structure
	 * 
	 * @param dataNodeList
	 * @return MyDataNode
	 */
	public MyDataNode createDataStructureDynamic(List<MyDataNode> dataNodeList, int columnToSort) {
		
	    listNodesForCopper= new ArrayList<>();
	    
		MyDataNode node0 = null;
		MyDataNode node2 = null;
		MyDataNode node3 = null;

		List<MyDataNode> parentNodes = new ArrayList<MyDataNode>();
		
		Iterator<MyDataNode> iterator = dataNodeList.iterator();
		
		while(iterator.hasNext()){
			 
			MyDataNode node = (MyDataNode) iterator.next();
			
			if (node.getColumnTitle().equals("Copper Parts")){
				
				//System.out.println("Copper part: " + node.getArticleNumber());
				
				listNodesForCopper.add(node);
			}
			
		}
		
		
		if(!listNodesForCopper.isEmpty()){
			readDataFromExcel(listNodesForCopper);
		}
		
		
		//System.out.println("Sorted based on column: " + mapSortedColumnCopperParts);
		
		Double sumGrossWeight = new Double(0);
		
		
		if (null != mapSortedColumnCopperParts && !mapSortedColumnCopperParts.isEmpty()){
			
			for (Entry<Integer, HashMap<String, Float>> entry : mapSortedColumnCopperParts.entrySet()) {
				
				Double sumColumnWeight = new Double(0);
				
				List<MyDataNode> rootNodes = new ArrayList<MyDataNode>();
				
				HashMap<String, Float> widthDepthPair = entry.getValue();
				
				String columnNumber = entry.getKey().toString();
				String customColumnName = "Column-"+ columnNumber;
				
				for( Entry<String, Float> widthDepthEntry : widthDepthPair.entrySet()){
					
					MyDataNode node1 = null;
					
					String[] widthAndDepth = widthDepthEntry.getKey().toString().split(",");
					
					Float width = Float.valueOf(widthAndDepth[0]);
					Float depth = Float.valueOf(widthAndDepth[1]);
					String columnName = widthAndDepth[2];
					
					if(!columnName.equals("#")){
						
						customColumnName = columnName;
						
					}
					
					Float length = widthDepthEntry.getValue();
					
					Double densityCopper =  0.0000089;
					
					Double weight = depth * width * length * densityCopper;
					
					sumGrossWeight += weight;
					
					sumColumnWeight += weight;
							
					node1 = new MyDataNode(null, null, null, null, null, null, width, depth , length, weight , "kg", null);
					
					rootNodes.add(node1);
					
				}
				
						
				node3 = new MyDataNode("<html><font color='blue'; size='4'><u>" + columnNumber + "</u></font> " + customColumnName + "</html>", null, null, null, null, null, null, null, null, sumColumnWeight, null, rootNodes);

				parentNodes.add(node3);

			}
			
		}
		

		node2 = new MyDataNode("Total Weight", null, null, null, null, null, null, null, null, sumGrossWeight, "kg", null);
		
		parentNodes.add(node2);
		
		node0 = new MyDataNode("Parent root node", null, null, null, null, null, null, null, null, null, null, parentNodes);

		return node0;
	}

	private void readDataFromExcel(List<MyDataNode> listNodesForCopper) {
		
		// Path of Syndication excel file
		
		//Workbook workbook;
		
		//getExcelPath();
		
		//String pathSyndicate = "C:/econftemp/Syndication_SYN85233(02-12-2019)(11-37-CET)_WithLinks.xlsx";
		
		// Read data for each product type code
		
		mapSortedColumnCopperParts = new HashMap<>();
			
		try {
			
			// workbook = WorkbookFactory.create(new File(pathSyndicate));
			 
			 for (MyDataNode copperPart : listNodesForCopper){
				 
				 //Sheet sheet = workbook.getSheetAt(0);
				 
				 //ArrayList<Row> rows = searchSheet(copperPart.getArticleNumber(), sheet);
				 
				 
				 SyndicateBean syndicateData = com.virtubuild.services.clientgui.custom.TreeTableMain.dataSyndicate.get(copperPart.getArticleNumber());
				 
				 //for (Row row : rows){
				 
				 //System.out.println("Copper part data: " + copperPart.getArticleNumber());
				 
				 if(syndicateData != null){
					 
					 //System.out.println("Typecode: " + syndicateData.getOrderNumber() + " and " + copperPart.getArticleNumber());
					 
					 String strWidth = syndicateData.getWidth();
					 String strDepth = syndicateData.getDepth();
					 String strLength = syndicateData.getLength();
					 
					 if(!(strDepth.contains(",") || strDepth.contains(",") || strLength.contains(","))){
						 
						 Float width = Float.valueOf(strWidth);
						 Float depth = Float.valueOf(strDepth);
						 Float length = Float.valueOf(strLength);
						 
						 //System.out.println("W: " + width + " D: " + depth + " L: " + length);
						 
						 HashMap<String, Float> columnNKeyValue =  mapSortedColumnCopperParts.get(copperPart.getColumnNumber());
						 
						 if (columnNKeyValue == null){
							 columnNKeyValue = new HashMap<>();
						 }
						 
						 String columnName = copperPart.getColumnName().trim();
						 
						 // If column name is blank then replace with delimiter
						 if(columnName.equals("")){
							 columnName = "#";
						 }
						 
						 Float current_length = columnNKeyValue.get("" + width + "," + depth + "," + columnName);
						 
						 if (current_length == null){
							 
							 current_length = 0f;
							 
						 }
						 
						 columnNKeyValue.put("" + width + "," + depth + "," + columnName, current_length + length * copperPart.getQuantity());
							 
						 
						 mapSortedColumnCopperParts.put(copperPart.getColumnNumber(), columnNKeyValue);
						 
						 
					 }
					 
					 
				 }
					 
			 }
			 
             //workbook.close();
			
		} catch (EncryptedDocumentException e) {
			
			LOGGER.error("Unable to read from Syndication Excel file ", e);
			
		}
		
		catch(Exception e){
			
			LOGGER.error("Unable to read from Syndication Excel file ", e);
			
		}
			
		
	}
	
	private void getExcelPath(){
		
        final ProtectionDomain domain;
        final CodeSource       source;
        final URL              url;

        domain = TreeTableMain.class.getProtectionDomain();
        source = domain.getCodeSource();
        url    = source.getLocation();
        
        excelSyndicatePath = url.toString();
        
        
        String path[];
        
        path = excelSyndicatePath.split("custom");
        
        excelSyndicatePath = path[0] + "design document/Syndication_SYN85233(02-12-2019)(11-37-CET)_WithLinks.xlsx";
        
        
        excelSyndicatePath = excelSyndicatePath.replaceAll("%20", " ");
        
        excelSyndicatePath = excelSyndicatePath.replaceAll("file:/", "");
        
    }
	
	public static ArrayList<Row> searchSheet(String searchText, Sheet sheet) {
        // This parameter is for appending sheet rows to mergedSheet in the end
        
        Double doubleValue = null;
        Boolean booleanValue = null;
        ArrayList<Row> filteredRows = new ArrayList<Row>();

        //Get double value if searchText is double
        try {
            doubleValue = Double.parseDouble(searchText);
        } catch(Exception e) {  
        }
        
        //Get boolean value if searchText is boolean
        try {
            booleanValue = Boolean.parseBoolean(searchText);
        } catch(Exception e) {  
        }
        
        //Iterate rows
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {

            Row row = sheet.getRow(j);
           
            //Iterate columns
            for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
            	
            	Cell cell = row.getCell(k);
            	
            	if(cell != null){
            		
	            		 //Search value based on cell type
	                    switch (cell.getCellType()) {
	                     case XSSFCell.CELL_TYPE_NUMERIC:
	                         if(doubleValue != null && doubleValue.doubleValue() == cell.getNumericCellValue()) {
	                             filteredRows.add(row);
	                         }
	                         break;
	                     case XSSFCell.CELL_TYPE_STRING:
	                         if(searchText != null && searchText.equals(cell.getStringCellValue())) {
	                             filteredRows.add(row);
	                         }
	                         break;
	                     case XSSFCell.CELL_TYPE_BOOLEAN:
	                         if(booleanValue != null && booleanValue.booleanValue() == cell.getBooleanCellValue()) {
	                             filteredRows.add(row);
	                         }
	                         break;
	                     default:
	                         if(searchText != null && searchText.equals(cell.getStringCellValue())) {
	                             filteredRows.add(row);
	                         }
	                         break;
	                     }
            	}
            	
              
            }
        }
        return filteredRows;
    }

	public int customCompare(String productType, String productType2) {
		int str1 = Integer.parseInt(productType.split("_")[1]);
		int str2 = Integer.parseInt(productType2.split("_")[1]);

		return str1 - str2;
	}

};

/**
 * A renderer class for BOMTable header.
 *
 */
class KeepSortIconHeaderRenderer implements TableCellRenderer {
     
    private TableCellRenderer defaultRenderer;
     
    public KeepSortIconHeaderRenderer(TableCellRenderer defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
 
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            label.setFont(new Font("Consolas", Font.BOLD, 14));
            label.setIcon(new ImageIcon("resources/sort_off.png"));
            label.setBorder(BorderFactory.createEtchedBorder());
        }
         
        return comp;
    }
 
}
