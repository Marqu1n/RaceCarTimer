package utils;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReportPanelTable extends JTable {
    public ReportPanelTable(DefaultTableModel tblModel) {
        super(tblModel);
    }

    private int bestTimeRow = -1;
    private List<Integer> increasedLaps = new ArrayList<>();
    private List<Integer> decreasedLaps = new ArrayList<>();

    public int getBestTimeRow() {
        return this.bestTimeRow;
    }

    public void setBestTimeRow(int bestTimeRow) {
        this.bestTimeRow = bestTimeRow;
    }

    public List<Integer> getIncreasedLapsList() {
        return this.increasedLaps;
    }

    public List<Integer> getDecreasedLapsList() {
        return this.decreasedLaps;
    }

}
