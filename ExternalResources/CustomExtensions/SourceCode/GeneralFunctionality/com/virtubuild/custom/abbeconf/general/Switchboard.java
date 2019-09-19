package com.virtubuild.custom.abbeconf.general;

import com.virtubuild.core.api.Component;
import java.util.LinkedList;
import java.util.List;

public class Switchboard
{
  public static final String SWITCHBOARD_COMP   = "switchboard";
  public static final String SWITCHBOARD_TOA    = "switchboard";
  public static final String CONNECTION_VAR     = "connection";
  
  private Component switchboardComponent;
  
  public Switchboard(Component switchboard)
  {
    this.switchboardComponent = switchboard;
  }
  
  public List<Column> getColumns()
  {
    List<Component> components = this.switchboardComponent.getLinkMesh();
    List<Column> columns = new LinkedList();
    for (Component comp : components) {
      columns.add(new Column(comp));
    }
    return columns;
  }
}