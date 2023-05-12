package utils;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ReportPanelCellRenderer extends DefaultTableCellRenderer {


        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
    
            //Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            ReportPanelTable reportTable = (ReportPanelTable) table; 
            l.setBackground(Color.WHITE);
            l.setForeground(Color.BLACK);

            List<Integer> increasedLapsList = reportTable.getIncreasedLapsList();
            if(increasedLapsList != null) {
                if(increasedLapsList.contains(row) && col == 3) {
                    l.setForeground(Color.GREEN);
                }
            }

            List<Integer> decreasedLapsList = reportTable.getDecreasedLapsList();
            if(decreasedLapsList != null) {
                if(decreasedLapsList.contains(row) && col == 3) {
                    l.setForeground(Color.RED);
                }
            }

            if(reportTable.getBestTimeRow() != -1 && reportTable.getBestTimeRow() == row) {
                l.setBackground(Color.MAGENTA);
                
                /* if (col != 3) */ l.setForeground(Color.WHITE);
                if (!(increasedLapsList.contains(row) || decreasedLapsList.contains(row))) l.setForeground(Color.WHITE);
            }

            //Return the JLabel which renders the cell.
            return l;
        }
    
}
