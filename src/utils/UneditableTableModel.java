package utils;

import javax.swing.table.DefaultTableModel;

public class UneditableTableModel extends DefaultTableModel {
    public UneditableTableModel(String[] col, int row) {
        super(col, row);
    }

    @Override
    public boolean isCellEditable(int row, int column){  
        return false;  
    }

}
