package com.virtubuild.services.clientgui.custom;

import com.virtubuild.services.abbeconf.exportcopper.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.util.ArrayMap;
import com.virtubuild.core.Manager;
import com.virtubuild.core.WorldComponent;
import com.virtubuild.core.api.Configuration;



@SuppressWarnings("serial")
public class TreeTableMain extends JPanel  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TreeTableMain.class);

	Color ABBRed = new Color(224, 2, 15); // Color ABB_Red

	static List<MyDataNode> nodesForTable = new ArrayList<>();

	public static List<MyDataNode> listNodes = new ArrayList<>();
	
	// Key=Category, Value=Rows data (each field separated by #)
	//private HashMap<String, List<String>> categorizedData = new HashMap<>();
	
	static List<MyDataNode> listCompleteData;
	
	private List<List<Object>> listOfNodes;
	
	//****************For Copper Export***********************
	public static List<ExcelData> listMasterExcelSyndication = new ArrayList<ExcelData>();
	public static List<ExcelData> listMasterExcelABBLibraryLinks = new ArrayList<ExcelData>();
	public static List<ExcelData> listMasterExcelDataRBBS;
	public static List<ExcelData> listMasterExcelDataN185Mounted;
	public static List<ExcelData> listMasterExcelDataN185Flatpack;
	public static List<ExcelData> listMasterStandardFileData;
	public static String buildDesignDocumentPath;
	public static String buildCustomPath;
	
	//*****************End of Copper Export Region****************
	
	static List<MyDataNode> listOfFilteredNodes = new ArrayList<>();
	
	private static int columnIndexFiltered;

	MyDataNode node11;
	
	
	
	private MyTreeTable myTreeTable = null;

	public static int BOMTableFont = 14;

	public MyAbstractTreeTableModel treeTableModel; 
	
	private static Map<Integer, List<String>> groupedData = new HashMap<Integer, List<String>>();
	
	private static Map<Integer, List<CheckListItem>> filterAppliedColumn = new HashMap<Integer, List<CheckListItem>>();
	
	private JPopupMenu popup;
	
	private static int columnSorted = -1;
	
	private TableRowSorter<TableModel> sorter;
	
	private JCheckBox[] checkBox;
	
	private List<String> checkBoxValues = new ArrayList<>();
	
    static boolean selectAll;
	
	                   // Column #, Filter value
	
	static JList jlist = null;
	
	private JList listCheckBox = null;
	
	private List<CheckListItem> filtersCurrentColumn = new ArrayList<>();

	 static DefaultListModel listModel = null;
	
	// nth idex of the array corrsponds to the nth column (0 to n-1)
	private static boolean isColumnSorted[] = new boolean[10];
	
	private static List<Integer> columnWidth = new ArrayList<>();
	
	// Key=TypeCode, Value=Link1, Link2
	public static HashMap<String, String> mapTypeCodeURL;
	
	// Key=TypeCode, Value=Link1, Link2
    public static HashMap<String, String> mapTypeCodeDrawingURL;
	
	public static String excelSyndicatePath;
	
	public static String excelABBLibraryPath;
	
	public static String excelCopperRBBS;
	
	public static String excelCopperN185;
	
	public static String excelStandardGeneral;
	
	public static boolean isDownloadAllClicked = false;
	
	static Workbook workbookSyndicate;

	static Workbook workbookLibraryLinks;
	
	static Sheet sheetSyndicate;

	static Sheet sheetABBLinks;
	
	// OrderNumber->Data 
    public static HashMap<String, SyndicateBean> dataSyndicate;
	// DocumentID->Hyperlink
	//static HashMap<String, ABBLinksBean> dataABBLinks;
	
	// DocumentID->Hyperlink
	public static MultiMap<String, String> dataABBLinks;

	public static String productLine;

	public TreeTableMain() {
		
	    // Nodes are sorted by Typecode - Column 2 by default
	    isColumnSorted[2] = true;
	    
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
        sorter.setSortable(4, false);
        sorter.setSortable(5, false);
        sorter.setSortable(6, false);
        sorter.setSortable(7, false);
        sorter.setSortable(8, false);
        
        
        myTreeTable.getTableHeader().addMouseListener(new MouseAdapter() {
            
            int col;
            
            int mouseXPrevious, mouseYPrevious, mouseXNow, mouseYNow;
            
            @Override
            public void mousePressed(MouseEvent e) {
                
                mouseXPrevious = e.getX();
                mouseYPrevious = e.getY();
                
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                
                
                col = myTreeTable.columnAtPoint(e.getPoint());
                
                columnIndexFiltered = col;
                
 
                if (SwingUtilities.isRightMouseButton(e)){
                    
                    synchronized (this) {
                        popupCalled(col);
                    }
                 
                 if (e.isPopupTrigger()) {
                        col = myTreeTable.columnAtPoint(e.getPoint());
                        popup.show(e.getComponent(), e.getX(), e.getY());

                    }
                    
                }
                
                
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
                col = myTreeTable.columnAtPoint(e.getPoint());
                
                columnIndexFiltered = col;
                
                
                mouseXNow = e.getX();
                mouseYNow = e.getY();
                
                //System.out.println("MouseX Prev " + mouseXPrevious + " Mouse Y Prev " + mouseYPrevious + " MouseXNow " + mouseXNow + " MouseYNow " + mouseYNow);
                
                                                             // Disable filter for Columns 1, 6 & 8
                if ( SwingUtilities.isRightMouseButton(e) && (col != 0 && col != 5 && col != 7) ){

                    
                    synchronized (this) {
                        popupCalled(col);
                    }
                    
                     if (e.isPopupTrigger()) {
                         col = myTreeTable.columnAtPoint(e.getPoint());
                         popup.show(e.getComponent(), e.getX(), e.getY());

                     }
                     
                     
                }
                
                
                else if(SwingUtilities.isLeftMouseButton(e) && ( mouseXPrevious == mouseXNow && mouseYPrevious == mouseYNow ) ) {
                
                    columnSorted = myTreeTable.columnAtPoint(e.getPoint());
                    
                    isColumnSorted[columnSorted] =  isColumnSorted[columnSorted] ? false : true;
                    
                    // Only rendered for sort enabled columns
                    
                    if (columnSorted != 0 && columnSorted != 5 && columnSorted != 6 && columnSorted != 7){
                        
                        initUI();
                        
                    }
                
                }
                
                else{

                    int maxNoColumns = myTreeTable.getColumnModel().getColumnCount();
                    
                    columnWidth.set(col, myTreeTable.getColumnModel().getColumn(col).getWidth());
                    
                    //System.out.println("COLUMN WIDTH: " + columnWidth.get(col) );
                    
                    // Update the column header width of previous or next column if changed
                    
                    if (col < maxNoColumns && col > 0){
                        
                        columnWidth.set(col-1, myTreeTable.getColumnModel().getColumn(col-1).getWidth());
                        columnWidth.set(col+1, myTreeTable.getColumnModel().getColumn(col+1).getWidth());
                        
                    }
                    
                    
                    
                    
                }
                
            }
            
        });
        


           
        //myTreeTable.getTableHeader().setComponentPopupMenu( popup );
        
        

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

        //myTreeTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        myTreeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        
      /*  myTreeTable.getColumnModel().getColumn(0).setPreferredWidth(220);
        myTreeTable.getColumnModel().getColumn(1).setPreferredWidth(210);
        myTreeTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        myTreeTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        myTreeTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        myTreeTable.getColumnModel().getColumn(5).setPreferredWidth(65);
        myTreeTable.getColumnModel().getColumn(6).setPreferredWidth(1100);
        myTreeTable.getColumnModel().getColumn(7).setPreferredWidth(170);
        myTreeTable.getColumnModel().getColumn(8).setPreferredWidth(110);
        myTreeTable.getColumnModel().getColumn(9).setPreferredWidth(145);
        myTreeTable.getColumnModel().getColumn(10).setMinWidth(0);
        myTreeTable.getColumnModel().getColumn(10).setMaxWidth(0);
        myTreeTable.getColumnModel().getColumn(10).setWidth(0);*/
        
        if(columnWidth.isEmpty()){
        
            columnWidth.add(0, 220);
            columnWidth.add(1, 210);
            columnWidth.add(2, 140);
            columnWidth.add(3, 80);
            columnWidth.add(4, 90);
            columnWidth.add(5, 65);
            columnWidth.add(6, 680);
            columnWidth.add(7, 170);
            columnWidth.add(8, 110);
            columnWidth.add(9, 110);
            columnWidth.add(10, 145);
            columnWidth.add(11, 0);
        }
        
        myTreeTable.getColumnModel().getColumn(11).setMinWidth(0);
        myTreeTable.getColumnModel().getColumn(11).setMaxWidth(0);
        myTreeTable.getColumnModel().getColumn(11).setWidth(0);
        
        // Hiding preview for Twinline
        
        if(null != productLine && productLine.contains("twinline")){
        	myTreeTable.getColumnModel().getColumn(7).setMinWidth(0);
            myTreeTable.getColumnModel().getColumn(7).setMaxWidth(0);
            myTreeTable.getColumnModel().getColumn(7).setWidth(0);
        }
        
        setColumnHeaderWidth();
        
       /* for (int column = 0; column < myTreeTable.getColumnCount(); column++)
        {
            TableColumn tableColumn = myTreeTable.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < myTreeTable.getRowCount(); row++)
            {
                TableCellRenderer cellRenderer = myTreeTable.getCellRenderer(row, column);
                Component c = myTreeTable.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + myTreeTable.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                //  We've exceeded the maximum width, no need to check other rows

                if (preferredWidth >= maxWidth)
                {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth( preferredWidth );
        }*/
        
        //TableColumn column = myTreeTable.getColumnModel().getColumn(2);
        //column.setHeaderRenderer(new KeepSortIconHeaderRenderer(myTreeTable.getTableHeader().getDefaultRenderer()));
        
        
        this.repaint();
        this.revalidate();
	   
        
	    
	}
	
	
	/**
	 * Parses the raw list of nodes to List of List of Objects (Row, Columns) structure.
	 * 
	 * @param nodesList
	 * @return listNodesAsObject
	 */
	private List<List<Object>> parseDataNodeToObject(List<MyDataNode> nodesList){
        
	    List<List<Object>> listNodesAsObject = new ArrayList<>();
	    
	    if ( !nodesList.isEmpty() ){ // to avoid multiple entries of same data
            
	        
            for (MyDataNode currentNode : nodesList){
                    
                    List<Object> currentNodeData = new ArrayList<>();
                    
                    currentNodeData.add(currentNode.getProductType());
                    currentNodeData.add(currentNode.toString()); // Product ID
                    currentNodeData.add(currentNode.getOrderNumber());
                    currentNodeData.add(currentNode.getPosition());
                    currentNodeData.add(currentNode.getQuantity());
                    currentNodeData.add(currentNode.getUoM());
                    currentNodeData.add(currentNode.getDesc());
                    currentNodeData.add(currentNode.getInstructionManual());
                    currentNodeData.add(currentNode.getWeight());
                    currentNodeData.add(currentNode.getGrossPrice());
                    currentNodeData.add(currentNode.getGrossPriceSum());
                    currentNodeData.add(currentNode.getColumnNumber());
                    currentNodeData.add(currentNode.getColumnName());
                    currentNodeData.add(currentNode.getLink());
                    currentNodeData.add(currentNode.getTitle());
                    
                    listNodesAsObject.add(currentNodeData);
                    
            }
            
        
         }
	    
	    return listNodesAsObject;
	}
	
	
	// Creates and populates CheckBoxes with valid values
	
	private synchronized void createCheckBox(int columnIndex){
	    
	    List<List<Object>> completeListForCheckBox = new ArrayList<>();
	    
	    List<String> listValuesInColumn = new ArrayList<>(); 
	    
	    listValuesInColumn.clear();
	    completeListForCheckBox.clear();
	    
	    completeListForCheckBox = parseDataNodeToObject( listCompleteData );
	    
	    //completeListForCheckBox = parseDataNodeToObject( listNodes );
	    
	    listModel = new DefaultListModel();
        jlist = new JList(listModel);
        jlist.setCellRenderer(new CheckListRenderer());
        
        String currentRowValue=null;
        
        
        
        if ( ! completeListForCheckBox.isEmpty() ){

            for (List<Object> currentRow : completeListForCheckBox){
                    
                    currentRowValue = String.valueOf(currentRow.get(columnIndex));
                    
                    if ( ! listValuesInColumn.contains(currentRowValue) ){
                        
                        listValuesInColumn.add(currentRowValue);
                        
                    }
                    
                }
            
        }
        
        
        //List<CheckListItem>  filtersAppliedForColumn = new ArrayList<>();
        //List<String> filtersAppliedString = new LinkedList<>();
        
	    // Fetch the filers that are already applied on the current column
	    
	    
	    /*Iterator it = checkBoxTicked.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        
	        
	        if((Integer)pair.getKey() == columnIndex){
	            
	             pair.getValue();
	            
	        }
	        
	    }
        
        
	    
	    
	    Object listFiltersApplied = checkBoxTicked.get(columnIndex);
	    
	    if(listFiltersApplied != null){
            
            filtersAppliedForColumn = (List<CheckListItem>) listFiltersApplied;
            
            for (CheckListItem checkedItems : filtersAppliedForColumn){
                
                checkBoxValues.add(checkedItems.getLabel());
                filtersAppliedString.add(checkedItems.getLabel());
                
                CheckListItem c = new CheckListItem(checkedItems.getLabel());
                c.setSelected(true);
                
                listModel.addElement(c);
                
            }
            
        }
	    
	    */
        
        // Following line will display check boxes in current filtered list
        Iterator<List<Object>> it = listOfNodes.iterator(); 
        
      /*  List<List<Object>> listCompleteNodes =  parseDataNodeToObject(listCompleteData);
        
        Iterator<List<Object>> it = listCompleteNodes.iterator();*/
        
        
        CheckListItem selectAllCheckBox = new CheckListItem("Select All");
        
        if (selectAll){
            selectAllCheckBox.setSelected(true);
        }
        else{
            selectAllCheckBox.setSelected(false);
        }
        
        listModel.addElement(selectAllCheckBox);
        
        while (it.hasNext()){
            
            currentRowValue = String.valueOf(it.next().get(columnIndex));
            
            /*if ( !checkBoxValues.contains(currentRowValue) &&  !filtersAppliedString.contains(currentRowValue) ){
                
                checkBoxValues.add(currentRowValue);
                
            }*/
            
            
            
            if ( listValuesInColumn.contains(currentRowValue) ){
                
                CheckListItem c = new CheckListItem(currentRowValue);
                
                c.setSelected(true);

                selectAll = true;
                
                listModel.addElement(c);
                
                listValuesInColumn.remove(currentRowValue);
            }
            
        }
        
        
        for ( String currentValue :  listValuesInColumn ){
            
            CheckListItem c = new CheckListItem(currentValue);
                
            c.setSelected(false);
            
            selectAll = false;
            
            listModel.addElement(c);
            
        }
        
        
        if (selectAll){
            
            
            CheckListItem checkBoxAt0 = (CheckListItem) listModel.get(0);
            
            checkBoxAt0.setSelected(true);
            
            listModel.remove(0);
            
            listModel.add(0, checkBoxAt0);
            
        }
        
        else{
            
            CheckListItem checkBoxAt0 = (CheckListItem) listModel.get(0);
            checkBoxAt0.setSelected(false);
            
            listModel.remove(0);
            
            listModel.add(0, checkBoxAt0);
            
        }
        
	    
	    /*for (String checkBoxText : checkBoxValues){
            
            CheckListItem c = new CheckListItem(checkBoxText);
            c.setSelected(false);
            
            listModel.addElement(c);
            
        }*/
	    
	    
	}
	
	private void setColumnHeaderWidth(){
    
	    
	    if ( myTreeTable != null ){
	        
	        
	        int noColumns = myTreeTable.getColumnModel().getColumnCount();
	        
	        for (int i=0; i<noColumns; i++){
	            
	            myTreeTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidth.get(i));
	            
	        }
	        
	    }
	    
	}
	
	
	
	private void updateSelectAllCheckBox() {
        
	    
            if (listCheckBox != null){
                        
                        boolean selectedAll = true;
                        
                        switch (listCheckBox.getModel().getSize()){
                            
                            
                            case 0: break;
                            
                            case 1:
                                
                                CheckListItem selectAllCheckBox =  (CheckListItem) listCheckBox.getModel().getElementAt(0);
                                selectAllCheckBox.setSelected(false);
                                
                                break;
                                
                            
                            default:
                                
                                for ( int i=1; i<listCheckBox.getModel().getSize(); i++ ){
                                    
                                    CheckListItem checkBox =  (CheckListItem) listCheckBox.getModel().getElementAt(i);
                                    
                                    if (!checkBox.isSelected()){
                                        selectedAll = false;
                                        
                                        CheckListItem checkBoxAt0 =  (CheckListItem) listCheckBox.getModel().getElementAt(0);
                                        checkBoxAt0.setSelected(false);
                                        
                                        selectAll = false;
                                        
                                        listModel.remove(0);
                                        
                                        listModel.add(0, checkBoxAt0);
                                        
                                        break;
                                    }
                                    
                                }
                                
                                if (selectedAll){
                                    
                                    CheckListItem checkBoxAt0 =  (CheckListItem) listCheckBox.getModel().getElementAt(0);
                                    checkBoxAt0.setSelected(true);
                                    
                                    selectAll = true;
                                    
                                    listModel.remove(0);
                                    
                                    listModel.add(0, checkBoxAt0);
                                    
                                }
                            
                        }
                        
                    }
	    
        
    }


    private synchronized void checkBoxListener(int columnIndex, CheckListItem item, int index){
	    
	    
	    // If user ticks a checkbox then add it to current list.
	    
	    
	    if (item.getLabel().toString().equals("Select All")){
            
	        if ( ! item.isSelected() ){
	            
	            // Case for selected
	            
	            // Remove all ticked values
	            
	            for (int i=1; i<listCheckBox.getModel().getSize(); i++){
                    
                    CheckListItem checkBox =  (CheckListItem) listCheckBox.getModel().getElementAt(i);
                    
                    checkBox.setSelected(false);
                    
                    filterNodesCompleteListRemove(checkBox.getLabel(), columnIndex);
                    
                }
                
                listOfFilteredNodes.clear();
                listNodes.clear();
	            
	            // Tick the all check boxes
	            
	            for (int i=1; i<listCheckBox.getModel().getSize(); i++){
	                
	                CheckListItem checkBox =  (CheckListItem) listCheckBox.getModel().getElementAt(i);
	                
	                checkBox.setSelected(true);
	                
	                filterNodesCompleteList(checkBox.getLabel(), columnIndex);
	                
	            }
	            
	            //item.setSelected(false);
	            selectAll = true;
	            
	        }
	        
	        else{
	            
	            // Case for Un-ticked select all
	            
	            for (int i=1; i<listCheckBox.getModel().getSize(); i++){
                    
                    CheckListItem checkBox =  (CheckListItem) listCheckBox.getModel().getElementAt(i);
                    
                    checkBox.setSelected(false);
                    
                    filterNodesCompleteListRemove(checkBox.getLabel(), columnIndex);
                    
                }
	            
	            listOfFilteredNodes.clear();
	            
	            //item.setSelected(true);
	            selectAll = false;
	            
	        }
	        
            
        }
        
          
        if ( !item.isSelected() ){
            
            //System.out.println("CheckBox listener Ticked");
            
            filterNodesCompleteList(item.getLabel(), columnIndex);
            
        }
        
        else{
            
            //System.out.println("CheckBox listener UnTicked");
            
            filterNodesCompleteListRemove(item.getLabel(), columnIndex);
            
        }
           
	    
	    
	    //initUI();
	    
	    
	    
	   /* boolean flagFound = false;
	    
	    
	    
	    if ( filtersCurrentColumn.isEmpty() ){
	        
	        filtersCurrentColumn.add(item);
	        flagFound = true;
	        
	    }
	    
	    else{
	        
	        
	        for (Iterator<CheckListItem> it = filtersCurrentColumn.iterator(); it.hasNext(); ) {
	            
	            CheckListItem checkBox = it.next();
	            
	            if ( checkBox.getLabel().equals(item.getLabel()) ){
                    
                    it.remove();
                    flagFound = true;
                    
                }
	        }
	        
	    }
	    
	    
	    if( ! flagFound ){
	        
	        filtersCurrentColumn.add(item);
	        
	    }*/
	    
	    
	}
	
    public synchronized void popupCalled(int columnIndex) {
    	
        int sizeCheckBox = listOfFilteredNodes.isEmpty() ? listCompleteData.size() : listOfFilteredNodes.size();
        
        popup = new JPopupMenu();
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        contentPane.setLayout(new BorderLayout(5, 5));
        checkBox = new JCheckBox[sizeCheckBox];
        
        synchronized (this) {
            createCheckBox(columnIndex);
        }
        
        contentPane.add(new JScrollPane(jlist));
        
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 24));
        textField.setBounds(50, 100, 200, 30);
        textField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	
            	String text = textField.getText();
            	
                if (text.trim().length() == 0) {
                    
                    //sorter.setRowFilter(null);
                    
                } else {
                	//sorter.setRowFilter(RowFilter.regexFilter(text));
                    
                } 
                
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
            	
            }

        });
        
        JPanel headerPanel = new JPanel();
        headerPanel.add(textField);
        
        jlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        jlist.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent event) {
                
                listCheckBox = (JList) event.getSource();
                int index = listCheckBox.locationToIndex(event.getPoint());// Get index of item
                index = listCheckBox.getSelectedIndex();
                CheckListItem item = (CheckListItem) listCheckBox.getModel().getElementAt(index);
                String val = listCheckBox.getModel().getElementAt(index).toString();
                DefaultListModel listModel = (DefaultListModel) listCheckBox.getModel();
                
                
                //Get all components at List
                
                synchronized (this) {
                    checkBoxListener(columnIndex, item, index);
                }
                
                
                
                //filterNodesUpdateTable(item.getLabel() , columnIndex);
                
                //popup.setVisible(false);
                
                //System.out.println("Updated List: " + filtersCurrentColumn);
                
                
                item.setSelected(!item.isSelected()); // Toggle selected state
                
                updateSelectAllCheckBox();
                
                listCheckBox.repaint(listCheckBox.getCellBounds(index, index));// Repaint cell
                
            }
        });
        
        JPanel footerPanel = new JPanel();
        
        Icon icon = new ImageIcon("resources/search.png");
        
        JButton submitButton = new JButton("Search", icon);
        submitButton.setPreferredSize(new Dimension(95, 26));
        //submitButton.setIcon(new ImageIcon("resources/search.png"));
        footerPanel.add(submitButton);
        
        footerPanel.add(Box.createRigidArea(new Dimension(10, 50)));
        
        
        JButton cancelButton = new JButton("Reset");
        cancelButton.setPreferredSize(new Dimension(75, 26));
        cancelButton.setIcon(new ImageIcon("resources/reset.png"));
        footerPanel.add(cancelButton);
        
        cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
			    
			    listNodes.clear();
			    
			    listNodes.addAll(listCompleteData);
			    
			    listOfFilteredNodes.clear();
			    
			    initUI();
			    
				popup.setVisible(false);
			}
		});
        
        
       submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				String text = Pattern.quote(textField.getText());
				
				// Working code commented -- dont' delete
				
				/*
				
				listOfFilteredNodes = new ArrayList<>();
				listOfFilteredNodes.clear();
				
				List<CheckListItem> checkBoxesColumn = new ArrayList<>();
				
				 for ( int i=0; i<listCheckBox.getModel().getSize(); i++ ){
	                    
	                    System.out.println("\n List element " + i +" :" + listCheckBox.getModel().getElementAt(i));
	                    
	                    CheckListItem currentItem = (CheckListItem) listCheckBox.getModel().getElementAt(i);
	                    
	                    System.out.println(" isSelected: " + currentItem.isSelected());
	                    
	                    
	                    if (currentItem.isSelected()){
	                        filterNodesCheckBox(listCheckBox.getModel().getElementAt(i).toString(), columnIndex);
	                    }
	                    
	                    checkBoxesColumn.add(currentItem);
	                    
	                }
				 
				 
				filterAppliedColumn.put(columnIndex, checkBoxesColumn);
		        
		        
		        initUI();
		        
		        popup.setVisible(false);
		        
		        for (String val : checkBoxValues){
		            
		            System.out.println("TEST: " + val);
		            
		        }*/
				
				if(!textField.getText().equals("")){
				    
				    filterNodesUpdateTable(textField.getText(), columnIndex);
	                
	                initUI();
	                
				}
				
				
				popup.setVisible(false);
				
				/*else{
				    
				    initUI();
                    
                    popup.setVisible(false);
				}*/
				
				
		        
			}
		});

        contentPane.add(headerPanel, BorderLayout.PAGE_START);
        contentPane.add(footerPanel, BorderLayout.PAGE_END);
        contentPane.updateUI();
      
        popup.add(contentPane);
        popup.revalidate();
        popup.repaint();
    }
	
	

	public void setListNodes(List<MyDataNode> nodeList) {

		this.listNodes = nodeList;

	}

	/**
	 * The method initializes the list of nodes
	 */
	public void initListNodes() {

		treeTableModel = new MyDataModel(createDataStructureDynamic(listNodes, columnSorted));

	}
	
	
	/**
	 * Method to filter nodes based on the search text
	 * 
	 * @param filterText
	 */
	private void filterNodesUpdateTable(String searchedString, int columnToLookup){

	    if ((columnIndexFiltered != 0) && (columnIndexFiltered != 7) && (columnIndexFiltered != 10) && (columnIndexFiltered != 11) &&  (columnIndexFiltered != 12) ){
	        
	        listOfFilteredNodes = new ArrayList<>();
	        listOfFilteredNodes.clear();
	        
	        for ( List<Object> currentRow : listOfNodes ){
	            
	            try{
	                
	                if ( String.valueOf(currentRow.get(columnIndexFiltered)).toLowerCase().contains(searchedString.toLowerCase()) ){
	                    
	                    MyDataNode newNode = new MyDataNode(String.valueOf(currentRow.get(0)), String.valueOf(currentRow.get(1)), String.valueOf(currentRow.get(2)), String.valueOf(currentRow.get(3)), Float.valueOf(String.valueOf(currentRow.get(4))), String.valueOf(currentRow.get(5)), String.valueOf(currentRow.get(6)), String.valueOf(currentRow.get(7)),Float.valueOf(currentRow.get(8).toString()),Float.valueOf(currentRow.get(9).toString()), Float.valueOf(currentRow.get(10).toString()) ,Integer.valueOf(String.valueOf(currentRow.get(11))), String.valueOf(currentRow.get(12)), null, String.valueOf(currentRow.get(14)));
	                    
	                    newNode.setTitle(String.valueOf(currentRow.get(14)));
	                    
	                    listOfFilteredNodes.add(newNode);
	                    
	                }
	                
	            }catch(Exception e){
	                
	                if ( String.valueOf(currentRow.get(columnIndexFiltered)).contains(searchedString) ){
                        
                        MyDataNode newNode = new MyDataNode(String.valueOf(currentRow.get(0)), String.valueOf(currentRow.get(1)), String.valueOf(currentRow.get(2)), String.valueOf(currentRow.get(3)), Float.valueOf(String.valueOf(currentRow.get(4))), String.valueOf(currentRow.get(5)), String.valueOf(currentRow.get(6)), String.valueOf(currentRow.get(7)),Float.valueOf(currentRow.get(8).toString()),Float.valueOf(currentRow.get(9).toString()), Float.valueOf(currentRow.get(10).toString()) ,Integer.valueOf(String.valueOf(currentRow.get(11))), String.valueOf(currentRow.get(12)), null, String.valueOf(currentRow.get(14)));
                        
                        newNode.setTitle(String.valueOf(currentRow.get(14)));
                        
                        listOfFilteredNodes.add(newNode);
                        
                    }
	                
	            }
	            
	            
	        }
	        
	        if (!listOfFilteredNodes.isEmpty()){
	            
	            listNodes.clear();
	            
	            listNodes.addAll(listOfFilteredNodes);
	            
	            //listNodes = listOfFilteredNodes;
	            
	            
	        }
	        
	    }
	    
	    
	    
	}
	
	
	
	   /**
     * Method to filter nodes based on the selected textbox value
     * 
     * @param filterText
     */
    private void filterNodesCheckBox(String searchedString, int columnToLookup){

        if ((columnIndexFiltered != 0) && (columnIndexFiltered != 7) && (columnIndexFiltered != 10) && (columnIndexFiltered != 11) &&  (columnIndexFiltered != 12) ){
            
            listOfFilteredNodes = new ArrayList<>();
            listOfFilteredNodes.clear();
            
            for ( List<Object> currentRow : listOfNodes ){
                
                try{
                    
                    if ( String.valueOf(currentRow.get(columnIndexFiltered)).toLowerCase().equals(searchedString.toLowerCase()) ){
                        
                        MyDataNode newNode = new MyDataNode(String.valueOf(currentRow.get(0)), String.valueOf(currentRow.get(1)), String.valueOf(currentRow.get(2)), String.valueOf(currentRow.get(3)), Float.valueOf(String.valueOf(currentRow.get(4))), String.valueOf(currentRow.get(5)), String.valueOf(currentRow.get(6)), String.valueOf(currentRow.get(7)),Float.valueOf(currentRow.get(8).toString()),Float.valueOf(currentRow.get(9).toString()), Float.valueOf(currentRow.get(10).toString()) ,Integer.valueOf(String.valueOf(currentRow.get(11))), String.valueOf(currentRow.get(12)), null, String.valueOf(currentRow.get(14)));
                        
                        newNode.setTitle(String.valueOf(currentRow.get(14)));
                        
                        listOfFilteredNodes.add(newNode);
                        
                    }
                    
                }catch(Exception e){
                    
                    if ( String.valueOf(currentRow.get(columnIndexFiltered)).equals(searchedString) ){
                        
                        MyDataNode newNode = new MyDataNode(String.valueOf(currentRow.get(0)), String.valueOf(currentRow.get(1)), String.valueOf(currentRow.get(2)), String.valueOf(currentRow.get(3)), Float.valueOf(String.valueOf(currentRow.get(4))), String.valueOf(currentRow.get(5)), String.valueOf(currentRow.get(6)), String.valueOf(currentRow.get(7)),Float.valueOf(currentRow.get(8).toString()),Float.valueOf(currentRow.get(9).toString()), Float.valueOf(currentRow.get(10).toString()) ,Integer.valueOf(String.valueOf(currentRow.get(11))), String.valueOf(currentRow.get(12)), null, String.valueOf(currentRow.get(14)));
                        
                        newNode.setTitle(String.valueOf(currentRow.get(14)));
                        
                        listOfFilteredNodes.add(newNode);
                        
                    }
                    
                }
                
                
            }
            
            if (!listOfFilteredNodes.isEmpty()){
                
                listNodes.clear();
                listNodes.addAll(listOfFilteredNodes);
                
            }
            
            
            
        }
        
        
        
    }
    
    
    /**
     * Filter when user ticks a checkbox
     * 
     * @param filterText
     */
    private void filterNodesCompleteList(String searchedString, int columnToLookup){

        if ((columnIndexFiltered != 0) && (columnIndexFiltered != 7) && (columnIndexFiltered != 10) && (columnIndexFiltered != 11) &&  (columnIndexFiltered != 12) ){
            
            //listOfFilteredNodes = new ArrayList<>();
            
            List<List<Object>> completeNodes =   parseDataNodeToObject(listCompleteData);
            
            
            for ( List<Object> currentRow : completeNodes ){
                
                try{
                    
                    if ( String.valueOf(currentRow.get(columnIndexFiltered)).toLowerCase().equals(searchedString.toLowerCase()) ){
                    	
                        MyDataNode newNode = new MyDataNode(String.valueOf(currentRow.get(0)), String.valueOf(currentRow.get(1)), String.valueOf(currentRow.get(2)), String.valueOf(currentRow.get(3)), Float.valueOf(String.valueOf(currentRow.get(4))), String.valueOf(currentRow.get(5)), String.valueOf(currentRow.get(6)), String.valueOf(currentRow.get(7)),Float.valueOf(currentRow.get(8).toString()),Float.valueOf(currentRow.get(9).toString()), Float.valueOf(currentRow.get(10).toString()) ,Integer.valueOf(String.valueOf(currentRow.get(11))), String.valueOf(currentRow.get(12)), null, String.valueOf(currentRow.get(14)));
                        
                        newNode.setTitle(String.valueOf(currentRow.get(14)));
                            
                            listOfFilteredNodes.add(newNode);
                            listNodes.add(newNode);
                        
                    }
                    
                }catch(Exception e){
                    
                    if ( String.valueOf(currentRow.get(columnIndexFiltered)).equals(searchedString) ){
                        
                        MyDataNode newNode = new MyDataNode(String.valueOf(currentRow.get(0)), String.valueOf(currentRow.get(1)), String.valueOf(currentRow.get(2)), String.valueOf(currentRow.get(3)), Float.valueOf(String.valueOf(currentRow.get(4))), String.valueOf(currentRow.get(5)), String.valueOf(currentRow.get(6)), String.valueOf(currentRow.get(7)),Float.valueOf(currentRow.get(8).toString()), Float.valueOf(currentRow.get(9).toString()), Float.valueOf(currentRow.get(10).toString()) ,Integer.valueOf(String.valueOf(currentRow.get(11))), String.valueOf(currentRow.get(12)), null, String.valueOf(currentRow.get(14)));
                        
                        newNode.setTitle(String.valueOf(currentRow.get(14)));
                        

                            listOfFilteredNodes.add(newNode);
                            listNodes.add(newNode);
                        
                    }
                    
                }
                
                
            }
            
            
            initUI();
            
            if (!listOfFilteredNodes.isEmpty()){
                
                //listNodes = listOfFilteredNodes;
                
                
            }
            
            
            
        }
        
        
        
    }
    
    
    
    /**
     * Filter when user un-ticks a check box, the rows are removed
     * 
     * @param filterText
     */
    private void filterNodesCompleteListRemove(String searchedString, int columnToLookup){

        if ((columnIndexFiltered != 0) && (columnIndexFiltered != 7) && (columnIndexFiltered != 10) && (columnIndexFiltered != 11) &&  (columnIndexFiltered != 12) ){
            
            
            List<MyDataNode> listNodesToRemove = new ArrayList<>();
           
            if ( listOfFilteredNodes.isEmpty() && ! searchedString.equals("Select All") ){
                
                //System.out.println("filter method listOfFilteredNodes is empty!");
                
                listOfFilteredNodes.addAll(listCompleteData);
                
            }
            
            for (MyDataNode node : listOfFilteredNodes){
                
                
                switch(columnToLookup){
                    
                    case 1:
                        
                        if(node.getProductType().toString().equals(searchedString)){
                            
                            listNodesToRemove.add(node);
                            
                        }
                        
                        break;
                        
                        
                    case 2:
                        
                        if(node.getOrderNumber().toString().equals(searchedString)){
                            
                            listNodesToRemove.add(node);
                            
                        }
                        
                        break;
                        
                    case 3:
                        
                        if(node.getPosition().toString().equals(searchedString)){
                            
                            listNodesToRemove.add(node);
                            
                        }
                        
                        break;
                        
                    case 4:
                        
                        if(node.getQuantity().toString().equals(searchedString)){
                                
                                listNodesToRemove.add(node);
                                
                           }
                            
                            break;
                            
                     
                    case 6:
                        
                        if(node.getDesc().toString().equals(searchedString)){
                            
                            listNodesToRemove.add(node);
                            
                        }
                        
                        break;
                        
                        
                    case 8:
                        
                        if(node.getGrossPrice().toString().equals(searchedString)){
                            
                            listNodesToRemove.add(node);
                            
                        }
                        
                        break;
                        
                        
                    case 9:
                        
                        
                        if(node.getGrossPriceSum().toString().equals(searchedString)){
                            
                            listNodesToRemove.add(node);
                            
                        }
                        
                        break;
                            
                    
                }
                
                
            }
                
            
            if (!listNodesToRemove.isEmpty()){
                
                listOfFilteredNodes.removeAll(listNodesToRemove);
                
                listNodes.clear();
                listNodes.addAll(listOfFilteredNodes);
                
                
                initUI();
            }
            
        }
        
        
    }
    
    private List<MyDataNode> fetchWeightFromExcel(List<MyDataNode> dataNodeList){
    	
    	getExcelPath();
    	
    	excelSyndicatePath = "C:/econftemp/Syndication_SYN85233(02-12-2019)(11-37-CET)_WithLinks.xlsx";
    	
    	Workbook workbookSyndicate;
    	Sheet sheetSyndicate;
    	
    	try {
    		
			workbookSyndicate = WorkbookFactory.create(new File(excelSyndicatePath));
			
			sheetSyndicate = workbookSyndicate.getSheetAt(0);
			
			for (int j=0; j<dataNodeList.size(); j++){
				
				 MyDataNode node = dataNodeList.get(j);
				 
				 ArrayList<Row> rowsSyndicate = searchSheet(node.getOrderNumber(), sheetSyndicate);
				 
				 node.setWeight(Float.valueOf(rowsSyndicate.get(0).getCell(36).toString()));
				 
				 listNodes.set(j, node);
				
			}
			
			
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			LOGGER.error("Error in reading weight info ", e);
			//System.out.println("Error in reading weight info " +e.getMessage());
		}
    	
    	return dataNodeList;
    }
    
    
    
    public static void readInputExcelFiles(){
    	
    	//getExcelPath();
    	
    	//excelSyndicatePath = "C:/econftemp/Syndication_SYN85233(02-12-2019)(11-37-CET)_WithLinks.xlsx";
    	
    	//excelABBLibraryPath = "C:/econftemp/SPEP ABB Library links.xlsx";
    	
    	dataSyndicate = new HashMap<>();
    	dataABBLinks = new MultiValueMap<>();
    	
    	listMasterExcelSyndication.clear();
    	listMasterExcelABBLibraryLinks.clear();
    	
    	try {
    		
    		//LOGGER.error("Path: " + excelSyndicatePath);
    		
			workbookSyndicate = WorkbookFactory.create(new File(excelSyndicatePath));
			workbookLibraryLinks = WorkbookFactory.create(new File(excelABBLibraryPath));
			sheetSyndicate = workbookSyndicate.getSheetAt(0);
			sheetABBLinks = workbookLibraryLinks.getSheetAt(0);
			
	        // Reading rows from Syndicate excel
	        
	    	for (int j = sheetSyndicate.getFirstRowNum(); j <= sheetSyndicate.getLastRowNum(); j++) {

	    		ExcelData excelData = new ExcelData();
	    		
	    		SyndicateBean syndicateRow = new SyndicateBean();
	    		
	    		Cell cellValue;
	    		
	    		String key = null;
	    		
	    		cellValue = sheetSyndicate.getRow(j).getCell(6);
	    		
	    		if(cellValue != null){
	    			key = cellValue.toString();
	    			excelData.sBOMCode = key;
	    		}
	    			
	    		cellValue = sheetSyndicate.getRow(j).getCell(12);
	    		
	    		if(cellValue != null){
	    			syndicateRow.setInstructionManualID(cellValue.toString());
	    			excelData.sInsmanText = cellValue.toString();
	    		}
	    			
	            
	    		cellValue = sheetSyndicate.getRow(j).getCell(15);
	    		
	            if(cellValue != null)
	            	syndicateRow.setMechanicalDrawings(cellValue.toString());
	            
	            cellValue = sheetSyndicate.getRow(j).getCell(36);
	            
	            if(cellValue != null)
	            	syndicateRow.setWeight(cellValue.toString());
	            
	            cellValue = sheetSyndicate.getRow(j).getCell(24);
	            
	            if(cellValue != null){
	            	syndicateRow.setLength(cellValue.toString());
	            	excelData.sLength = cellValue.toString();
	            }
	            	
	            
	            cellValue = sheetSyndicate.getRow(j).getCell(30);
	            
	            if(cellValue != null){
	            	syndicateRow.setDepth(cellValue.toString());
	            	excelData.sD = cellValue.toString();
	            }
	            	
	            
	            cellValue = sheetSyndicate.getRow(j).getCell(42);
	            
	            if(cellValue != null){
	            	syndicateRow.setWidth(cellValue.toString());
	            	excelData.sW = cellValue.toString();
	            }
	            	
	            
	            dataSyndicate.put(key, syndicateRow);
	            listMasterExcelSyndication.add(excelData);
	            
	            
	        }
	    	
	    	
	    	for (int j = sheetABBLinks.getFirstRowNum(); j <= sheetABBLinks.getLastRowNum(); j++) {
	    		
	    		ExcelData abbLinksExcelData = new ExcelData();
	    		
	    		Cell cellValue;
	    		
	    		String key = null;
	    		
	    		cellValue = sheetABBLinks.getRow(j).getCell(9);
	    		
	    		if(cellValue != null){
	    			key = cellValue.toString();
	    			abbLinksExcelData.sInsmanText = key;
	    		}
	    			
	    		
	    		cellValue = sheetABBLinks.getRow(j).getCell(13);
	    		
	    		if(cellValue != null && null != cellValue.getHyperlink() ){
	    			dataABBLinks.put(key, cellValue.getHyperlink().getAddress().toString());
	    		}
	    			
	    		cellValue = sheetABBLinks.getRow(j).getCell(31);
	    		
	    		if(cellValue != null)
	    			abbLinksExcelData.sInsmanLink = cellValue.toString();
	    			
	    			listMasterExcelABBLibraryLinks.add(abbLinksExcelData);
	    		
	    	}
	    	
	    	
	    	
	    	//System.out.println("Syndication: " + dataSyndicate);
	    	//System.out.println("ABBLinks Size: " +  dataABBLinks.size() +"Data:"  + dataABBLinks);
			
			
		} catch (EncryptedDocumentException | InvalidFormatException | IOException  e) {
			LOGGER.error("Error reading from input excel files. ", e);
		}
		 
    }
    
    private List<MyDataNode> fetchHyperLinksForInstructionManual(List<MyDataNode> dataNodeList){
    	
    	/*getExcelPath();
    	
    	String pathSyndicate = "C:/econftemp/Syndication_SYN85233(02-12-2019)(11-37-CET)_WithLinks.xlsx";
    	String pathABBLib = "C:/econftemp/SPEP ABB Library links.xlsx";
    	
    	Workbook workbookSyndicate, workbookLibraryLinks;
    	
    	Sheet sheetSyndicate, sheetABBLinks;*/
    	
    	// ProductID to lookup in ABBLinks sheet
    	String[] productIDs = null;
    	String[] mechanicalDrawings = null;
    	
    	mapTypeCodeURL = new HashMap<>();
    	mapTypeCodeDrawingURL = new HashMap<>();
		
		try {
			
			 /*workbookSyndicate = WorkbookFactory.create(new File(excelSyndicatePath));
			 
			 workbookLibraryLinks = WorkbookFactory.create(new File(excelABBLibraryPath));
			 
			 sheetSyndicate = workbookSyndicate.getSheetAt(0);
			 
			 sheetABBLinks = workbookLibraryLinks.getSheetAt(0);
			 */
			
			 for (int j=0; j<dataNodeList.size(); j++){
				 
				 MyDataNode node = dataNodeList.get(j);
				 
				 //ArrayList<Row> rowsSyndicate = searchSheet(node.getOrderNumber(), sheetSyndicate);
				 
				 ///productIDs = rowsSyndicate.get(0).getCell(12).toString().split(",");
				 
				 SyndicateBean sydicateData = dataSyndicate.get(node.getOrderNumber());
				 
				 if(sydicateData != null){ //Proceed only if data is present in Syndicate 
					 
					 
					 productIDs = sydicateData.getInstructionManualID().split(",");
					 
					//Cell cellMechanicalDrawing = rowsSyndicate.get(0).getCell(15);
					 
					 
					 String mechanicalData =  sydicateData.getMechanicalDrawings();
					 
					 if( null != mechanicalData){
						 mechanicalDrawings = mechanicalData.split(",");
					 }
					 
					 //node.setWeight(Float.valueOf(rowsSyndicate.get(0).getCell(36).toString()));
					 
					 node.setWeight(Float.valueOf(sydicateData.getWeight()));
					 
					 listNodes.set(j, node);
					 
					 for(String productID : productIDs){
						 
						 //ArrayList<Row> rowsABBLinks =  searchSheet(productID, sheetABBLinks);
						 
						List<String> hyperLinks = (List<String>) dataABBLinks.get(productID);
						 
						 if(null != hyperLinks && !hyperLinks.isEmpty()){
							 
							 for (String hyperlink : hyperLinks){
								 
								 //System.out.println("Hyperlink for " + productID + " : " + link );
								 
								 String URLs = mapTypeCodeURL.get(node.getOrderNumber());
								 
								 if(null != hyperlink){
									 
									 if(URLs == null){
										 
										 URLs = hyperlink;
										 
									 }
									 
									 else{
										 
										 URLs += "," + hyperlink;
										 
									 }
									 
									 mapTypeCodeURL.put(node.getOrderNumber(), URLs);
									 
								 }
								 
								 
							 }
							 
							/* Row rowLink = rowsABBLinks.get(0);
							 
							 String link = rowLink.getCell(13).getHyperlink().getAddress();
							 
							 //System.out.println("Hyperlink for " + productID + " : " + link );
							 
							 String URLs = mapTypeCodeURL.get(productID);
							 
							 if(URLs == null){
								 
								 URLs = link;
								 
							 }
							 
							 else{
								 
								 URLs += "," + link;
								 
							 }
							 
							 mapTypeCodeURL.put(node.getOrderNumber(), URLs);*/
							 
							 
						 }
						 
					 }
					 
					 
					 isDownloadAllClicked = true;
					 
					 // Download mechanical drawing only if the Download All button is clicked
					 if (isDownloadAllClicked && null != mechanicalDrawings){
						 
						 for (String drawingNumber : mechanicalDrawings){
							
							 // Search for Drawing hyper links
							 ArrayList<Row> rowsABBLinks =  searchSheet(drawingNumber, sheetABBLinks);
							 
							 for (Row rowDrawing : rowsABBLinks){
								 
								 String link = rowDrawing.getCell(13).getHyperlink().getAddress();
								 
								 String URLs = mapTypeCodeDrawingURL.get(node.getOrderNumber());
								 
								 if(null != link){
									 
									 if(URLs == null){
										 
										 URLs = link;
										 
									 }
									 
									 else{
										 
										 URLs += "," + link;
										 
									 }
									 
									 mapTypeCodeDrawingURL.put(node.getOrderNumber(), URLs);
									 
								 }
								 
								 
							 }
							 
							 
						 }
						 
						 
						 isDownloadAllClicked = false;
						 
					 }
					 
					 
				 }
				
				 
			 }
			 
			 //workbookSyndicate.close();
			 //workbookLibraryLinks.close();
			 
			 //System.out.println("Mechanical drawings Map "  + mapTypeCodeDrawingURL);
			 
		} catch (EncryptedDocumentException e) {
			
			LOGGER.error("Unable to read from Syndication or ABBLibraryLinks Excel file ",  e);
			
		}
		
		catch(Exception e){
			
			LOGGER.error("Unable to read from Syndication or ABBLibraryLinks Excel file ", e);
			
		}
    	
		return dataNodeList;
    	
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
    
    
	/**
	 * Fetched path of Syndication excel file
	 */
	public static void getExcelPath(){
		
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

        excelABBLibraryPath = path[0] + "design document/SPEP ABB Library links.xlsx";
        
        excelSyndicatePath = excelSyndicatePath.replaceAll("%20", " ");
        
        excelABBLibraryPath = excelABBLibraryPath.replaceAll("%20", " ");
        
        excelSyndicatePath = excelSyndicatePath.replaceAll("file:/", "");
        
        excelABBLibraryPath = excelABBLibraryPath.replaceAll("file:/", "");
        
        
        
    }
	

	/**
	 * Method creates the BOM hierarchy structure
	 * 
	 * @param dataNodeList
	 * @return MyDataNode
	 */
	public MyDataNode createDataStructureDynamic(List<MyDataNode> dataNodeList, int columnToSort) {
		
		List<MyDataNode> dataNodeListWithWeight = new ArrayList<>();
		
		//Search only the quotes that are not in the dataNodeList (for newly added column)
		
		//if ( (null == mapTypeCodeURL || mapTypeCodeURL.isEmpty()) && !listNodes.isEmpty()){
			dataNodeListWithWeight = fetchHyperLinksForInstructionManual(dataNodeList);
		//}
		
		//dataNodeListWithWeight = fetchWeightFromExcel(dataNodeList);
		
		//dataNodeListWithWeight.addAll(dataNodeList);
		
		List<String> lstTitle = new ArrayList<>();
		MyDataNode node0 = null;
		MyDataNode node2 = null;
		MyDataNode node3 = null;

		List<MyDataNode> parentNodes = new ArrayList<MyDataNode>();

    	Collections.sort(dataNodeListWithWeight, new Comparator<MyDataNode>() {
    
    			@Override
    			public int compare(MyDataNode arg0, MyDataNode arg1) {
    
    				return arg0.getColumnNumber().compareTo(arg1.getColumnNumber());
    
    			}
    		});
    	
    	
    	
		ArrayMap<String, Integer> columnNumbers = new ArrayMap<String, Integer>();
		
		for (MyDataNode dataNode : dataNodeListWithWeight) {
			
			Integer value = dataNode.getColumnNumber();
			
			String columnName = dataNode.getColumnName();
			
			if (null != columnName){
				
				if( columnName.trim().equals("") ){
					columnNumbers.put("Column-" + value.toString() + "_" + value.toString(), value);
					//System.out.println("Column-" + value.toString());
				}
				
				else if(columnName.trim().equals("All strings")){
					// Null or empty values from Build are ignored
				}
				
				else{
					
					columnNumbers.put(columnName + "_" + value.toString(), value);
					
					//System.out.println(columnName + "_" + value.toString() + " Typecode: "+ dataNode.getOrderNumber());
					
				}
				
			}
			
		}

		ArrayMap<String, List<MyDataNode>> entireMap = new ArrayMap<>();
		Iterator it = columnNumbers.entrySet().iterator();
		while (it.hasNext()) {
			List<MyDataNode> listOfSpecificColNum = new ArrayList<>();
			Map.Entry pair = (Map.Entry) it.next();
			for (MyDataNode node : dataNodeListWithWeight) {
				if (node.getColumnNumber() == pair.getValue()) {
					listOfSpecificColNum.add(node);
				}
			}
			
			// Column No., Columns pair
			entireMap.put(pair.getKey().toString(), listOfSpecificColNum);
		}

		for (MyDataNode dataNode : dataNodeListWithWeight) {
			if (lstTitle.contains(dataNode.getTitle()) == false) {
				lstTitle.add(dataNode.getTitle());
			}
		}
		
		
		// init nodes for filtering
		
		listOfNodes = parseDataNodeToObject(dataNodeListWithWeight);
		
		

		Float sumAll = new Float(0);
		Float sumColumnPrice = new Float(0);
		Float sumGross = new Float(0);
		
		Float sumWeightSwitchboard = new Float(0);
		
		for (Map.Entry<String, List<MyDataNode>> entry : entireMap.entrySet()) {
			
			Float sumWeightColumn = new Float(0);
			
			MyDataNode node1 = null;
			List<MyDataNode> rootNodes = new ArrayList<MyDataNode>();
			for (int i = 0; i < lstTitle.size(); i++) {
			    
				List<MyDataNode> children = new ArrayList<>();
				for (MyDataNode dataNode : entry.getValue()) {
					if (dataNode.getTitle().equalsIgnoreCase(lstTitle.get(i))) {
						
						if(null == dataNode.getWeight()){
							dataNode.setWeight(0f);
						}
						
						Float netWeight = dataNode.getWeight() * dataNode.getQuantity();
						
						
						node1 = new MyDataNode(null, "<html><font color='blue'> <u> "+ dataNode.getProductType() + "</u> </font></html>", dataNode.getOrderNumber(),
								dataNode.getPosition(), dataNode.getQuantity(), dataNode.getUoM(), dataNode.getDesc(), "<html><font color='blue'><u>" +"Preview" + "</u> </font></html>", netWeight,
								dataNode.getGrossPrice(), dataNode.getGrossPriceSum(), dataNode.getColumnNumber(), dataNode.getColumnName(), null, dataNode.getLink());
						sumAll += dataNode.getGrossPriceSum();
						dataNode.setGrossPriceSumAll(sumAll);
						
						sumWeightSwitchboard += netWeight;
						sumWeightColumn += netWeight;
						
						children.add(node1);
						
						/*
						//Prod ID, Typecode, Position, Qty, UoM, Desc, Gross price, Gross price sum (8 columns to filter)
						String newCategorizedData = dataNode.getProductType() + "#" + dataNode.getOrderNumber()+ "#" + dataNode.getPosition()+ "#" + dataNode.getQuantity()+ "#" + dataNode.getUoM()+ "#" + dataNode.getDesc()+ "#" + Float.valueOf(dataNode.getGrossPrice())+ "#" + Float.valueOf(dataNode.getGrossPriceSum());
 						
						if ( categorizedData.containsKey(dataNode.getTitle()) ){
						    
						    List<String> rowsForCategory = categorizedData.get(dataNode.getTitle());
						    
						    if(rowsForCategory.isEmpty()){
						        rowsForCategory = new ArrayList<>();
						    }
						    
						    
						    rowsForCategory.add(newCategorizedData);
						    
						    categorizedData.put(dataNode.getTitle(), rowsForCategory);
						    
						}
						else{
						    
						    List<String> rowsForCategory = new ArrayList<>();
						    
						    rowsForCategory.add(newCategorizedData);
						    
						    categorizedData.put(dataNode.getTitle(), rowsForCategory);
						    
						}
						*/
						

					}
					
					
					
				}
				

				if (!children.isEmpty()) {
				    
				    // by default sort all rows by Typecode
				    
				    Collections.sort(children, new Comparator<MyDataNode>() {

                        @Override
                        public int compare(MyDataNode arg0, MyDataNode arg1) {

                            return arg0.getOrderNumber().compareTo(arg1.getOrderNumber());

                        }
                        
                    });
				    
				    if (columnToSort == 2){
				        
				            if(isColumnSorted[2]){
				                
				                
				                Collections.sort(children, new Comparator<MyDataNode>() {

                                    @Override
                                    public int compare(MyDataNode arg0, MyDataNode arg1) {

                                        return arg0.getOrderNumber().compareTo(arg1.getOrderNumber());

                                    }
                                    
                                });
	                            
	                        }
	                        
	                        else{
	                            
	                            // reverse sort if already sorted
                                
                                Collections.sort(children, new Comparator<MyDataNode>() {

                                    @Override
                                    public int compare(MyDataNode arg0, MyDataNode arg1) {

                                        return arg1.getOrderNumber().compareTo(arg0.getOrderNumber());

                                    }

                                });
	                            
	                        }
				    }
				    
					
					if (columnToSort == 1){
					    
					    if(isColumnSorted[1]){
					        
					        Collections.sort(children, new Comparator<MyDataNode>() {
	                            
	                            @Override
	                            public int compare(MyDataNode arg0, MyDataNode arg1) {
	                
	                                return arg0.getProductID().compareTo(arg1.getProductID());
	                
	                            }
	                        });
					        
					    }
					    
					    else{
					        
					        Collections.sort(children, new Comparator<MyDataNode>() {
	                            
	                            @Override
	                            public int compare(MyDataNode arg0, MyDataNode arg1) {
	                
	                                return arg1.getProductID().compareTo(arg0.getProductID());
	                
	                            }
	                        });
					        
					    }
					    
                    }
					
					if (columnToSort == 4){
					    
					    
					    if(isColumnSorted[4]){
					        
					        Collections.sort(children, new Comparator<MyDataNode>() {
	                            
	                            @Override
	                            public int compare(MyDataNode arg0, MyDataNode arg1) {
	                
	                                return arg0.getQuantity().compareTo(arg1.getQuantity());
	                
	                            }
	                        });
					        
					    }
					    
					    else{
					        
					        Collections.sort(children, new Comparator<MyDataNode>() {
	                            
	                            @Override
	                            public int compare(MyDataNode arg0, MyDataNode arg1) {
	                
	                                return arg1.getQuantity().compareTo(arg0.getQuantity());
	                
	                            }
	                        });
					        
					    }
			           
			            
			        }
			        
			        else if(columnToSort == 3){
			            
			            if(isColumnSorted[3]){
			                
			                Collections.sort(children, new Comparator<MyDataNode>() {
	                            
	                            @Override
	                            public int compare(MyDataNode arg0, MyDataNode arg1) {
	                
	                                return arg0.getPosition().compareTo(arg1.getPosition());
	                
	                            }
	                        });
			                
			            }
			            
			            else{
			                
			                Collections.sort(children, new Comparator<MyDataNode>() {
                                
                                @Override
                                public int compare(MyDataNode arg0, MyDataNode arg1) {
                    
                                    return arg1.getPosition().compareTo(arg0.getPosition());
                    
                                }
                            });

			            }
			               
			            
			        }
			        
			        else if(columnToSort == 8){
			            
			            if(isColumnSorted[8]){
			                
			                Collections.sort(children, new Comparator<MyDataNode>() {
	                            
	                            @Override
	                            public int compare(MyDataNode arg0, MyDataNode arg1) {
	                
	                                return arg0.getGrossPrice().compareTo(arg1.getGrossPrice());
	                
	                            }
	                        });
			                
			            }
			            
			            else{
			                
			                Collections.sort(children, new Comparator<MyDataNode>() {
                                
                                @Override
                                public int compare(MyDataNode arg0, MyDataNode arg1) {
                    
                                    return arg1.getGrossPrice().compareTo(arg0.getGrossPrice());
                    
                                }
                            });
			                
			            }
			            
			            
			        }
			        
			        else if(columnToSort == 9){
			            
			            if(isColumnSorted[9]){
			               
			                Collections.sort(children, new Comparator<MyDataNode>() {
	                            
	                            @Override
	                            public int compare(MyDataNode arg0, MyDataNode arg1) {
	                
	                                return arg0.getGrossPriceSum().compareTo(arg1.getGrossPriceSum());
	                
	                            }
	                        });
			                
			            }
			            
			            else{
			                
			                Collections.sort(children, new Comparator<MyDataNode>() {
                                
                                @Override
                                public int compare(MyDataNode arg0, MyDataNode arg1) {
                    
                                    return arg1.getGrossPriceSum().compareTo(arg0.getGrossPriceSum());
                    
                                }
                            });

			            }


			        }
			    
					

					MyDataNode node = new MyDataNode(lstTitle.get(i), null, null, null, null, null, null, null, null, null, null, null, null,
							children, null);

					rootNodes.add(node);
				}
			}
					
			sumColumnPrice = sumAll;
			sumGross += sumColumnPrice;
			
			// GROUPING impl
			
			/* 
			
			for (MyDataNode dataNode : rootNodes){
				
				for(MyDataNode childNode: dataNode.getChildren()){
					
					System.out.println("Children Data:\n" + "Title: " + childNode.getTitle() + " OrderNumber: " + childNode.getOrderNumber() + " Quantity: " + childNode.getQuantity());
					
					listNodesSorter.add(childNode);
				}
				
			}
			
			
			
			Map<Float, Set<String>> result = listNodesSorter.stream().collect(Collectors.groupingBy(MyDataNode::getQuantity, Collectors.mapping(MyDataNode::getOrderNumber, Collectors.toSet())));
			
			System.out.println("----------------------- GROUP BY -----------------------");
			
			System.out.println(result);
			
			for(Map.Entry<Float, Set<String>> groups : result.entrySet()) {
				
			    float key = groups.getKey();
			    			 
			    System.out.println("SAM-KEYS: " + key);
			    
			    int key_int = (int) key;
			    
			    List<String> data = groupedData.get(key_int);
			    
			    System.out.println("Data before: " +data);
			    
			    if(null == data){
			    	data = new ArrayList<>();
			    }
			    
			    Set<String> setString = groups.getValue();
			    
			    for (String typecode : setString){
			    	data.add(typecode);
			    }
			    
			    System.out.println("Data after: " +data);
			    
			    groupedData.put(key_int, data);
			    
			    
			}
			
			*/
			
			
			String[] columnNameWithNumber = entry.getKey().split("_");
			
			
			node3 = new MyDataNode("<html><font color='blue'; size='4'><u>" + columnNameWithNumber[1] + "</u></font> " + columnNameWithNumber[0] + "</html>", null, null, null, null, null, null, null, sumWeightColumn, null, sumColumnPrice, null, null, rootNodes, null);

			sumAll = 0.0f;
			sumColumnPrice = 0.0f;
			parentNodes.add(node3);

		}
		
	/*	MyDataNode dummy = new MyDataNode();

		for (int c = 0; c < (parentNodes.size() - 1); c++) {
			for (int d = 1; d < parentNodes.size() - c; d++) {
				int i = customCompare(parentNodes.get(d - 1).getProductType(), parentNodes.get(d).getProductType());
				
				//int i = parentNodes.get(d - 1).getColumnNumber() - parentNodes.get(d).getColumnNumber();
				
				if (i > 0) {
					dummy = parentNodes.get(d - 1);
					parentNodes.set(d - 1, parentNodes.get(d));
					parentNodes.set(d, dummy);

				}
			}
		}
*/
		node2 = new MyDataNode("Total Gross", null, null, null, null, null, null, null, sumWeightSwitchboard, null, sumGross, null, null, null, null);
		parentNodes.add(node2);

		node0 = new MyDataNode("Parent root node", null, null, null, null, null, null, null, null, null, null, null, null, parentNodes, null);
		
	/*	System.out.println("------------------------ FINAL GROUPED DATA ----------------------");
		System.out.println(groupedData);*/

		return node0;
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
