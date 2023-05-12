import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import persistance.model.Lap;
import utils.ReportPanelCellRenderer;
import utils.ReportPanelTable;
import utils.UneditableTableModel;

public class ReportPanel extends JFrame {

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAMEWIDTH = Math.round((float)screenSize.getWidth()/3.5f);
    private static final int FRAMEHEIGHT = FRAMEWIDTH*4/3;

    private static JPanel container = new JPanel();
    private static final String[] COLUMNS = {"Numero da volta", "Tempo da volta", "Tempo total", "Diferença das voltas"};
    private static DefaultTableModel reportTableModel = 
    new UneditableTableModel(COLUMNS, 0);
    private static ReportPanelTable reportTable = new ReportPanelTable(reportTableModel);
    private static JScrollPane reportTablePane = new JScrollPane(reportTable);
    private ArrayList<Lap> lapsList;

    public ReportPanel(ArrayList<Lap> lapsList){
        this.lapsList = lapsList;
        clearRows();
        setSize(FRAMEWIDTH,FRAMEHEIGHT);
        setVisible(true);
        setLocationRelativeTo(null);
        drawComponents();
    }

    private void clearRows(){
        reportTableModel.setRowCount(0);
    }

    private void drawComponents(){
        reportTable.setDefaultRenderer(Object.class, new ReportPanelCellRenderer());

        JLabel reportTitle = new JLabel("Relatório:");
        reportTitle.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        reportTitle.setHorizontalAlignment(JLabel.CENTER);

        container.setLayout(new BorderLayout());
        container.add(reportTitle,BorderLayout.PAGE_START);
        container.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        reportTablePane.createVerticalScrollBar();
        reportTablePane.getViewport().setBackground(Color.WHITE);
        addLaps();
        //container.setLayout();
        container.setBackground(Color.WHITE);
        container.add(reportTablePane,BorderLayout.CENTER);
        add(container);
    }

    public float getLapDifference(String currentLap, String previousLap) {
        // currentLap and previousLap expected format --> "00:00.000"

        String[] currentLapSplittedTime = currentLap.split(":");
        float currentLapInSecs = Integer.parseInt(currentLapSplittedTime[0]) * 60;
        currentLapInSecs += Integer.parseInt(currentLapSplittedTime[1].split("\\.")[0]);
        currentLapInSecs += Integer.parseInt(currentLapSplittedTime[1].split("\\.")[1]) / 1000.0f;

        String[] previousLapSplittedTime = previousLap.split(":");
        float previousLapInSecs = Integer.parseInt(previousLapSplittedTime[0]) * 60;
        previousLapInSecs += Integer.parseInt(previousLapSplittedTime[1].split("\\.")[0]);
        previousLapInSecs += Integer.parseInt(previousLapSplittedTime[1].split("\\.")[1]) / 1000.0f;

        if(currentLapInSecs == 0) return 0;

        float diffInSecs = currentLapInSecs - previousLapInSecs;

        return diffInSecs;
    }

    public int getLapInMillis(String lapTime) {
        // lapTiem expected format --> "00:00.000"

        String[] splittedLapString = lapTime.split(":");
        int timeInMillis = Integer.parseInt(splittedLapString[0]) * 60000;
        timeInMillis += Integer.parseInt(splittedLapString[1].split("\\.")[0]) * 1000;
        timeInMillis += Integer.parseInt(splittedLapString[1].split("\\.")[1]);

        return timeInMillis;
    }

    private void addLaps(){
        int bestTimeRow = 0;
        int bestTime = -1;
        List<Integer> increasedLapsList = reportTable.getIncreasedLapsList();
        List<Integer> decreasedLapsList = reportTable.getDecreasedLapsList();
        for(int i = 0;i<lapsList.size();i++){
            ArrayList<String> row = new ArrayList<>();
            row.add(String.format("%02d", i+1));

            float lapDifference;
            String previousLapRelativeTime;
            String currentLapRelativeTime = null;
            if(i > 0) {
                currentLapRelativeTime = PanelRight.getLapRelativeTime(lapsList.get(i), lapsList.get(i-1));
            }
            else currentLapRelativeTime = lapsList.get(i).toString();
            
            row.add(currentLapRelativeTime);
            row.add(lapsList.get(i).toString());

            
            if(i > 0) {
                if(i > 1) {
                    previousLapRelativeTime = PanelRight.getLapRelativeTime(lapsList.get(i-1), lapsList.get(i-2));
                } else {
                    previousLapRelativeTime = PanelRight.getLapRelativeTime(lapsList.get(i-1), lapsList.get(i-1));
                }
                
                lapDifference = getLapDifference(currentLapRelativeTime, previousLapRelativeTime);
                /* String previousLapRelativeTime = PanelRight.getLapRelativeTime(lapsList.get(i-1), lapsList.get(i-2));
                float lapDifference = getLapDifference(currentLapRelativeTime, previousLapRelativeTime);

                if (bestTime == -1) bestTime = getLapInMillis(currentLapRelativeTime);

                if (getLapInMillis(currentLapRelativeTime) <= bestTime) {
                    bestTimeRow = i;
                    bestTime = getLapInMillis(currentLapRelativeTime);
                }

                System.out.println("Lap diff " + lapDifference);
                if (lapDifference < 0) increasedLapsList.add(i);
                else if (lapDifference > 0) decreasedLapsList.add(i);

                row.add(String.format(new Locale("en"),"%+02.3f s", lapDifference)); */
            } else {
                lapDifference = 0.0f;
            };

            if (bestTime == -1) bestTime = getLapInMillis(currentLapRelativeTime);

            if (getLapInMillis(currentLapRelativeTime) <= bestTime) {
                bestTimeRow = i;
                bestTime = getLapInMillis(currentLapRelativeTime);
            }
            
            if (lapDifference < 0) increasedLapsList.add(i);
            else if (lapDifference > 0) decreasedLapsList.add(i);

            row.add(String.format(new Locale("en"),"%+02.3f s", lapDifference));


            reportTableModel.addRow(row.toArray());
        }

        reportTable.setBestTimeRow(bestTimeRow);
    }
    
}
