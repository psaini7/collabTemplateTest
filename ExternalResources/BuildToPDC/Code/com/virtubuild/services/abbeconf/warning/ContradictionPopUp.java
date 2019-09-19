package com.virtubuild.services.abbeconf.warning;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ContradictionPopUp {

  public static void showPopUp(String[] multiLineMsg) {

    JOptionPane pane = new JOptionPane();
    pane.setMessage(multiLineMsg);
    JDialog d = pane.createDialog(null, "Contradiction!");
    d.setVisible(true);

  }

  public static int getSelection(JOptionPane optionPane) {
    int returnValue = JOptionPane.CLOSED_OPTION;

    Object selectedValue = optionPane.getValue();
    if (selectedValue != null) {
      Object options[] = optionPane.getOptions();
      if (options == null) {
        if (selectedValue instanceof Integer) {
          returnValue = ((Integer) selectedValue).intValue();
        }
      } else {
        for (int i = 0, n = options.length; i < n; i++) {
          if (options[i].equals(selectedValue)) {
            returnValue = i;
            break; 
          }
        }
      }
    }
    return returnValue;
  }

}
