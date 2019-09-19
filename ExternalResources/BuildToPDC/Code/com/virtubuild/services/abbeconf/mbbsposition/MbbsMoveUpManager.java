package com.virtubuild.services.abbeconf.mbbsposition;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.frame.FrameController;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;
import com.virtubuild.core.state.StateVar;
import com.virtubuild.custom.abbeconf.general.Switchboard;
import com.virtubuild.custom.abbeconf.utils.VariableUtils;

public class MbbsMoveUpManager {

    private final Logger logger;
    
    private List<BaseComponent> columns;
    private Map<BaseComponent,RackController> componentsToMove      = new HashMap<>();
    private Map<BaseComponent,RackController> componentsToReduce    = new HashMap<>();
    private Map<BaseComponent,RackController> componentsToDelete    = new HashMap<>();
    
    private int currentMbbsPosition;
    private int currentFirstRow;
    private List<BaseComponent> currentCandidates = new LinkedList<>();
    
    private static final String MBBS_POSITION_VAR   = "main_busbar_position";
    private static final String HEIGHT_VAR          = "height";
    private static final String KIT_FRAMES_TOA      = "kit_frames";
    private static final String TOUCHGUARD_TOA      = "kit_touchguard_combilineN";

    private Component switchboard;
    
    public MbbsMoveUpManager( Configuration configuration, Logger logger ) {
        this.logger = logger;
        setColumns( configuration );
        setSwitchboard( configuration );
    }

    public void run() {
        
        currentMbbsPosition = getMbbsPosition(); // busbar position will always be the same across all columns
        
        if(currentMbbsPosition == 0) {
            return;
        }
        
        // for each column - determine if it is possible to move the components / or delete the components right above. Only try to move/delete one step at a time
        goToNext:
        for(BaseComponent column : columns) {
            
            // TODO: only do at mbbs busbar in column
            if( !hasRackController(column) ) {
                logger.info("No rack for column '{}'. Skipping.", column.getID());
                continue goToNext; // ignore column if no rack
            }
            
            RackController rack = getRackController(column);
            
            // find first row above the mbbs component
            currentFirstRow = currentMbbsPosition + getMbbsComponentHeight(rack);

            currentCandidates = getAllCandidates(rack);
            
            if( currentCandidates.isEmpty() ) {
                logger.info("No components to move for column {}. Skipping.", column.getID());
                continue goToNext; // ignore column if no candidate components
            }
            
            // We need to look at ALL components above mbbs position (or the mbbs component)
            // because: if there is free space at the top of the column, the components should all be moved up
            //          if there is free space just above the mbbs component, then just move mbbs position up
            //          if there is no free space just above the mbbs component, then see if there is free space (entire row) above that line - if so move up
            //          if there is not free space anywhere, then check if any of the components can be reduced in size or deleted to make room to move up. Start with the lowest row.
            // For each column, save reference to the columns that can be moved (and possibly the move/delete/reduce components for the columns
            // if ANY column cannot have components moved/reduced/deleted, -> do nothing at all!
            // if it is possible to make room in ALL columns (1 row at a time) use the stored columns and components to move up
            // set the mbbs position to +1 of stored position
//            boolean canMoveUpColumn = canMoveUpColumn(rack);
            if( !canMoveUpColumn(rack) ) {
                // do not move or reduce components
                return;
            }

        }

        // delete
        deleteComponents();
        // reduce
        reduceComponents();
        // move
        moveComponents();
        
        // move mbbsPosition up one row
        moveMbbsPosition();

    }
    
    
    private boolean canMoveUpColumn(RackController rack) {
        
        // check for empty row - find first
        int emptyRow = checkForEmptyRow( rack, currentFirstRow );
        
        if( emptyRow > -1 ) {
            // if row is empty, mark all component in rows below empty row (and above mbbs component) to be moved up
            findMoveCandidates(rack, emptyRow-1);
            return true;
        }
        // check if can create empty room by reducing height of components (and moving up)
        // check reduce heigh or remove
        return canCreateEmptyRow(rack);
        
    }
    
    private boolean canCreateEmptyRow( RackController rack ) {
        Map<BaseComponent, String> candidates = new HashMap<>();
        
        // TODO: move this to new method
        for ( BaseComponent candidate : currentCandidates ) {
            if( isReductionCandidate(rack, candidate) ) {
                candidates.put(candidate, "R");
                continue;
            }
            // else check if can remove
            if( isDeletionCandidate(candidate) ) {
                candidates.put(candidate, "D");
            }
        }
        
        if( candidates.isEmpty() ) {
            return false;
        }
            
        // TODO: move this to new method
        // check per row if reducing height creates new row
        for( int i = currentFirstRow; i <= rack.getAvailableRackSize().height; i++ ) {
            if( rowCanBeCleared(i, rack, candidates) ) {
                return true;
            }
            
        }
        return false;
    }
        
    
    private boolean rowCanBeCleared(int row, RackController rack, Map<BaseComponent, String> candidates) {
        
        List<BaseComponent> finalCandidates = new LinkedList<>();
        for( int i = 1 ; i <= rack.getAvailableRackSize().width ; i ++ ) {
            Position position = Position.of(i, row);
            if( isEmptyPosition(rack,position) ) {
                continue;
            }
            
            BaseComponent occupying = getOccupyingComponent(rack, position );
            if( !candidates.containsKey(occupying) ) {
                return false;
            }
            if(!finalCandidates.contains(occupying) ) {
                finalCandidates.add(occupying);
            }
        }
        
        // add finalCandiates to reduce or delete depending on their type
        for( BaseComponent candidate : finalCandidates ) {
            String action = candidates.get(candidate); // get the action (reduce/delete) for the candidate from the input Map
            
            updateComponentsLists(candidate, action, rack);
        }
        
        // find any candidates below the empty row, to move them up
        findMoveCandidates(rack,row-1);
        
        return true;
    }
    
    private void updateComponentsLists(BaseComponent component, String action, RackController rack) {
        switch( action ) {
            case "R":
                addToComponentsLists(component, rack, componentsToReduce);
                break;
            case "D":
                addToComponentsLists(component, rack, componentsToDelete);
                break;
            default:
                logger.error("Unknown action {} to execute for Component {}.", action, component.getID() );
                // TODO: skip, delete or reduce?
        }
    }
    
    private void addToComponentsLists(BaseComponent component, RackController rack, Map<BaseComponent,RackController> map) {
        if( !alreadyMarkedForAction(component) ) {
            map.put(component,rack);
        }       
    }
    
    private boolean isEmptyPosition(RackController rack, Position position) {
        BaseComponent occupying = getOccupyingComponent(rack, position );
        return occupying == null;
    }
    
//    private void sortBasedOnYPosition(List<BaseComponent> list) {
//        
//        list.sort( new Comparator<BaseComponent>( )  {
//            public int compare(BaseComponent comp1, BaseComponent comp2) {
//                String y1 = comp1.getStateVector().getVar("kit_posy").getValue().getID();
//                String y2 = comp2.getStateVector().getVar("kit_posy").getValue().getID();
//                return y1.compareTo(y2);
//            }
//                        
//        });
//        
//    }
    
    private boolean isDeletionCandidate(BaseComponent component) {
        return component.isTypeOfActor(TOUCHGUARD_TOA);
    }
    
    private boolean isReductionCandidate(RackController rack, BaseComponent component ) {
        int height = rack.getSizeOrDefault(component).height;
        return isLegalHeight( component, height-1);
    }
    
    private boolean isLegalHeight(BaseComponent component, int value) {
        StateVar heightVar = getVariable(component, HEIGHT_VAR);
        if( heightVar == null ) {
            return false;
        }
        return isLegalValue(heightVar, String.valueOf(value) );
    }
    
    private boolean isLegalValue(StateVar variable, String value) {

//        List<StateValue> legalValues = variable.getLegalValues();
//        boolean legal = variable.isLegalValue(value);
        
        return variable.isLegalValue(value);
    }
    
    private StateVar getVariable(BaseComponent component, String variableId) {
        return component.getStateVector().getVar(variableId);
    }
    
    /**
     * Finds and adds candidate to the list of candidates to move
     * @param rack
     * @param endRow
     */
    private void findMoveCandidates(RackController rack, int endRow) {
        for(BaseComponent candidate : currentCandidates) {
            if( isCandidate(candidate, rack, endRow) && !alreadyMarkedForAction(candidate)) {
                componentsToMove.put(candidate, rack);
            }
        }
    }
    
    /**
     * Check whether a component has already been marked for move/reduce/deletion.
     * @param candidate     BaseComponent to check
     * @return              true if already marked, false otherwise
     */
    private boolean alreadyMarkedForAction(BaseComponent candidate) {
        return     componentsToMove.containsKey(candidate) 
                || componentsToReduce.containsKey(candidate)
                || componentsToDelete.containsKey(candidate);
    }

    /**
     * Get all component candidates for move/reduce/delete. Will get all components in the rack above the mbbs position 
     * (exlucing the component inserted in mbbs position
     * @param rack      Rack to get candidates for
     * @return          List of BaseComponents candidating for move/reduce/delete
     */
    private List<BaseComponent> getAllCandidates(RackController rack){
        
        List<BaseComponent> moveCandidates = new LinkedList<>();
        Collection<BaseComponent> components = rack.getComponents();
        
        for( BaseComponent component : components ) {
            if( isCandidate(component, rack) ) {
                moveCandidates.add(component);
            }
        }
        return moveCandidates;
    }
    
    /**
     * Check wether a component is a candidate for move/reduce/deletion in all rows above the mbbs component.
     * @param component     BaseComponent to check
     * @param rack          rack to check in
     * @return              true if it is a candidate for moving/reducing/deletion
     */
    private boolean isCandidate(BaseComponent component, RackController rack) { 
        return checkPosition( rack, component, currentFirstRow, rack.getAvailableRackSize().height);
    }
    
    /**
     * Checks whether a component is a candidate for move/reduce/delete up to (and including) the specified row. 
     * Starting from the first row above the mbbs component
     * @param component
     * @param rack
     * @param endRow
     * @return
     */
    private boolean isCandidate(BaseComponent component, RackController rack, int endRow) { 
        return checkPosition( rack, component, currentFirstRow, endRow);
    }    
    /**
     * Checks that the position of a component is within the required row range
     * @return
     */
    private boolean checkPosition(RackController rack, BaseComponent component, int startRow, int endRow) {
        
        Position position = rack.getInsertPosition(component);
        
        if( position == null ) {
            return false;
        }
        
        return startRow <= position.y && position.y <= endRow;
    }
    
    private void moveComponents() {
        List<Map.Entry<BaseComponent, RackController>> convertedList = convertToListSortedByPosition( componentsToMove );
//        for( Map.Entry<BaseComponent, RackController> entry : componentsToMove.entrySet() ) {
        for( Map.Entry<BaseComponent, RackController> entry : convertedList) {
          BaseComponent component = entry.getKey();
          RackController rack = entry.getValue();
          Position position = rack.getInsertPosition(component);
          if( position == null ) {
              continue;
          }
          // move component 1 up
          moveComponent( rack, component, Position.of(position.x, position.y+1) );            
        }
    }
    
    /**
     * Takes the input map and returns a sorted list of it's BaseComponent key. 
     * The list is sorted by the BaseComponent's Y position
     * @param map
     * @return
     */
    private List<Map.Entry<BaseComponent,RackController>> convertToListSortedByPosition( Map<BaseComponent,RackController> map ){
        LinkedList<Map.Entry<BaseComponent, RackController>> sorted = new LinkedList<>(map.entrySet());
        sorted.sort( ( c1, c2 ) -> compareYPosition(c1.getKey(),c2.getKey()) );
        return sorted;
    }
    
    /**
     * Compares the input components on their position based on their Y position. Largest -> smallest
     * @param comp1
     * @param comp2
     * @return
     */
    private int compareYPosition( BaseComponent comp1, BaseComponent comp2 ) {
        int pos1 = getYPosition(comp1);
        int pos2 = getYPosition(comp2);
        return Integer.compare(pos2, pos1);
    }
    
    private int getYPosition(BaseComponent component) {
        Integer y = VariableUtils.getIntegerValue((Component) component, "kit_posy");
        if( y == null ) {
            logger.error( "Could not find position." );
            return 0;
        }
        return y;
    }
    
    /**
     * Moves a component in the rack to the position specified.
     * @param rack              RackController to move the component in
     * @param component         BaseComponent to move
     * @param moveToPosition    Position to move the component to
     */
    private void moveComponent( RackController rack, BaseComponent component, Position moveToPosition ) {
        
        if( !rack.remove( component ) ) {
            logger.error("Could not move component {}", component.getID());
            return; 
        }
        rack.insert(moveToPosition, component, true);
    }
    
    private void reduceComponents() {
        for( Map.Entry<BaseComponent, RackController> entry : componentsToReduce.entrySet() ) {
                BaseComponent component = entry.getKey();
                RackController rack = entry.getValue();
                Position position = rack.getInsertPosition(component);
                if( position == null ) {
                    logger.error("Component {} does not have an insert position", component.getID());
                    continue;
                }
                reduceHeightComponent( component );
                moveComponent( rack, component, Position.of(position.x, position.y +1) );
          }        
    }
    
    /**
     * Reduces the height of the input component by 1
     * @param component
     */
    private void reduceHeightComponent( BaseComponent component ) {
        Integer height = VariableUtils.getIntegerValue( (Component) component, HEIGHT_VAR);
        if( height == null) {
            logger.error( "Cannot set variable {} on component {}.", HEIGHT_VAR, component.getID() );
            return;
        }
        // set height variable to height - 1
        component.getStateVector().getVar(HEIGHT_VAR).setValue( String.valueOf(height - 1) );
    }

    private void deleteComponents() {
        for( Map.Entry<BaseComponent, RackController> entry : componentsToDelete.entrySet() ) {
            Component component = (Component) entry.getKey();
            component.delete();
        }
    }
    
    /**
     * Find first empty row above the startRow.
     * @param rack
     * @param startRow
     * @return  first empty row. -1 if no empty rows
     */
    private int checkForEmptyRow(RackController rack, int startRow) {

        for( int i = startRow ; i <= rack.getAvailableRackSize().height ; i ++) {
             if( isEmptyRow(rack, i ) ) {
                 return i;
             }
        }
        return -1;
    }
    
    private boolean isEmptyRow(RackController rack, int row) {
        for( int i = 1 ; i <= rack.getAvailableRackSize().width ; i++ ) {
            boolean occupied = rack.isOccupied( Position.of(i,row) );
            if( occupied ) {
                return false;
            }
        }
        return true;
    }
    
    
    private void setColumns( Configuration configuration ) {
        columns = configuration.getTOAComponents( KIT_FRAMES_TOA );
    }
    
    private int getMbbsPosition() {
       
        Integer position = VariableUtils.getIntegerValue( switchboard, MBBS_POSITION_VAR );
        if( position == null ) {
            logger.error("Variable {} not found on column {}", MBBS_POSITION_VAR, switchboard.getID() );
            return 0;
        }
        return position; 
    }
    
    private void moveMbbsPosition() {
        switchboard.getStateVector().getVar(MBBS_POSITION_VAR).setValue(String.valueOf(currentMbbsPosition+1));
    }
    
    private void setSwitchboard(Configuration configuration) {
        List<BaseComponent> switchboards = configuration.getTOAComponents( Switchboard.SWITCHBOARD_COMP );
        
        // FIXIT: update if more than one switchboard is possible (there is a switchboard holder component)
        if( !switchboards.isEmpty() ) {
           switchboard = (Component) switchboards.get(0);
        }
    }
    
    private boolean hasRackController(BaseComponent column) {
        ComponentState comp = (ComponentState) column;
        FrameController rackController = comp.getFrameController(FrameControllerType.RACK);

        if (rackController == null) {
          logger.error("Called on '{}' which does not have a rack", column);
          return false;
        }
        return true;
    }
    
    private RackController getRackController(BaseComponent column ) {
        ComponentState comp = (ComponentState) column;
        return (RackController) comp.getFrameController(FrameControllerType.RACK);

    }
    
    private int getMbbsComponentHeight(RackController rack) {
       
       BaseComponent mbbsComponent = getOccupyingComponent( rack, Position.of(1, currentMbbsPosition) );

       if(mbbsComponent == null) {
           logger.info("No mbbs component in rack.");
           return 0;
       }

       Integer height = VariableUtils.getIntegerValue( (Component) mbbsComponent, HEIGHT_VAR);
       if( height == null) {
           return 0;
       }

       return height;
    }
    
    private BaseComponent getOccupyingComponent(RackController rack, Position position) {
        
        Collection<BaseComponent> occupyingComponent = rack.getOccupyingComponents( position ); 
        if( !occupyingComponent.isEmpty() ) {
            return occupyingComponent.iterator().next();
        }
        return null;
    }
    
  
}
