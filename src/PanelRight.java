import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import persistance.dao.CarDAO;
import persistance.dao.LapDAO;
import persistance.model.Car;
import persistance.model.Lap;
import utils.UneditableTableModel;

//import model.Lap;
//import model.LapsDAO;

public class PanelRight extends JPanel{
    private static int millis;
    private static int secs;
    private static int mins;

    private static Timer timer;
    private static JLabel timerLabel = new JLabel("00:00.000",JLabel.CENTER);

    /* 
    private static DefaultListModel<String> laps = new DefaultListModel<String>();
    private static JList<String> lapsJList = new JList<String>(laps);
    //Table with laps and times
    */
    private static final String[] COLUMNS = {
        "Numero da volta",
        "Tempo da volta",
        "Tempo total"
    };
    private static DefaultTableModel tbModelLapsTimes
     = new UneditableTableModel(COLUMNS, 0);
    private static JTable tableLapsTimes = new JTable(tbModelLapsTimes);
    
    private static JScrollPane lapsScrollPane = new JScrollPane(tableLapsTimes);
    private static JButton startButton = new JButton("Iniciar");
    private static JButton stopButton = new JButton("Parar");
    private static JButton lapButton = new JButton("Volta");
    private static JButton resetButton = new JButton("Resetar");
    private static JButton reportButton = new JButton("Relatório");
    private ArrayList<Lap> lapsList = new ArrayList<>();
    private String selectedCar;

    public PanelRight(JPanel container, String selectedCar){
        setPreferredSize(new Dimension(
            Math.round(container.getWidth()*0.66f),
            container.getHeight())
            );
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        
        setSelectedCar(selectedCar);;
        JPanel buttonsPanel = new JPanel(new FlowLayout(
            FlowLayout.CENTER,
            10,
            15
            )
        );
        startButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        stopButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        lapButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        resetButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        reportButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));

        buttonsPanel.setBackground(Color.WHITE);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        }
        );
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        }
        );
        lapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lapTimer();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(askUserForReset()) {
                    resetTimer();
                };
            }
        });
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReportPanel(lapsList);
            }
        });
        
        startButton.setBackground(Color.WHITE);
        stopButton.setBackground(Color.WHITE);
        lapButton.setBackground(Color.WHITE);
        resetButton.setBackground(Color.WHITE);
        reportButton.setBackground(Color.WHITE);

        startButton.setFocusable(false);
        stopButton.setFocusable(false);
        lapButton.setFocusable(false);
        resetButton.setFocusable(false);
        reportButton.setFocusable(false);

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        lapButton.setEnabled(false);
        resetButton.setEnabled(false);

        lapsScrollPane.createVerticalScrollBar();

        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(lapButton);
        buttonsPanel.add(resetButton);

        timerLabel.setFont(new Font("trebuchet ms",Font.PLAIN,50));
        timerLabel.setForeground(Color.BLACK);
        c.insets = new Insets(10,10,10,10);
        c.gridwidth=2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(timerLabel, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridy=1;
        add(lapsScrollPane,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        add(reportButton,c);
        c.gridy = 2;
        add(buttonsPanel,c);

       //jf.add(rightPanel);
        //jf.revalidate();
    }

    public void changeVisibility(boolean shouldShow) {
        
        if(shouldShow && selectedCar != null){
            tbModelLapsTimes.setRowCount(0);
            
            lapsList = LapDAO.getByCar(CarDAO.getByName(new Car(selectedCar)));
            // if the selectedCar has laps store in db
            if(lapsList.size() > 0) {
                // if the selectedCar has laps stored in db AND the timer has not been started by other car
                if (mins > 0 || secs > 0 || millis > 0) {
                    stopTimer();
                    resetCounter();
                    updateTimerLabel();

                    lapButton.setEnabled(false);
                    stopButton.setEnabled(false);
                }

                resetButton.setEnabled(true);
                for (int i = 0; i < lapsList.size(); i++) {
                    tbModelLapsTimes.addRow(new String[]{
                        String.format("%02d", i+1),
                        (i>0) ? getLapRelativeTime(lapsList.get(i), lapsList.get(i - 1)) : lapsList.get(i).toString(),
                        lapsList.get(i).toString(),
                    });
                }
            // if the selectedCar doesn't have laps stored in db, then has the timer been started by other car?
            } else if (mins > 0 || secs > 0 || millis > 0){
                resetButton.setEnabled(true);
            // if the selectedCar doesn't have laps stored in db AND the timer has not been started by other car
            } else {
                resetButton.setEnabled(false);
            }

            for(Component component : getComponents()){
                component.setVisible(true);
            }

        }else{
            for(Component component : getComponents()){
                component.setVisible(false);
            }
            
        }

        SwingUtilities.updateComponentTreeUI(this);
    }

    public static String getLapRelativeTime(Lap currentLap, Lap previousLap) {
        // Lap.toString() return format --> "00:00.000"

        String[] currentLapSplittedTime = currentLap.toString().split(":");
        int currentLapInMillis = Integer.parseInt(currentLapSplittedTime[0]) * 60000;
        currentLapInMillis += Integer.parseInt(currentLapSplittedTime[1].split("\\.")[0]) * 1000;
        currentLapInMillis += Integer.parseInt(currentLapSplittedTime[1].split("\\.")[1]);

        String[] previousLapSplittedTime = previousLap.toString().split(":");
        int previousLapInMillis = Integer.parseInt(previousLapSplittedTime[0]) * 60000;
        previousLapInMillis += Integer.parseInt(previousLapSplittedTime[1].split("\\.")[0]) * 1000;
        previousLapInMillis += Integer.parseInt(previousLapSplittedTime[1].split("\\.")[1]);

        int diffInMillis = currentLapInMillis - previousLapInMillis;
        int diffMins = (diffInMillis / (1000 * 60));
        int diffSecs = (diffInMillis % (1000 * 60)) / 1000;
        int diffMillis = (diffInMillis % (1000 * 60)) % 1000;

        return String.format("%02d:%02d.%03d", diffMins, diffSecs, diffMillis);
    }

    public void updateTimerLabel() {
        timerLabel.setText(
            String.format(
                "%02d:%02d.%03d",
                mins,
                secs,
                millis
            )
        );
    }

    public void setSelectedCar(String selectedCar){
        if (selectedCar != null) this.selectedCar = selectedCar;
        else this.selectedCar = "";
    }

    public String getCurrentTimer(){
        return timerLabel.getText();
    }

    public ArrayList<Lap> getLapsList(){
        return lapsList;
    }

    public void resetCounter() {
        mins = 0; 
        secs = 0;
        millis = 0;
    }

    public boolean askUserForReset() {
        return JOptionPane.showConfirmDialog(null,"Deseja excluir as voltas já gravadas e zerar o timer?", "Confirmação", JOptionPane.OK_CANCEL_OPTION) == 0;
    }

    public void timerRun(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                millis++;
                
                if (millis >=1000) {
                    secs++;
                    millis = 0;
                    
                    if (secs >= 60) {
                        mins++;
                        secs = 0;
                    }
                }
                updateTimerLabel();
            }
        }, 0, 1);
    }

    public void startTimer(){
        if (timer == null) {
            timer = new Timer();
        }


        if (lapsList.size() > 0){
            if(askUserForReset()) {
                resetTimer();
                timer = new Timer();
                timerRun();
                stopButton.setEnabled(true);
                lapButton.setEnabled(true);
            }
        } else {
            timerRun();
            stopButton.setEnabled(true);
            lapButton.setEnabled(true);
            resetButton.setEnabled(true);
        }


    }
        

    public void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

/*         lastMins = 0;
        lastSecs = 0;
        lastMillis = 0; */
    }

    public void lapTimer(){
        int minsSnapshot = mins;
        int secsSnapshot = secs;
        int millisSnapshot = millis;

        Car currentCar = CarDAO.getByName(new Car(selectedCar));
        Lap newLap = new Lap(
            String.format("00:%02d:%02d.%03d", minsSnapshot, secsSnapshot, millisSnapshot),
            currentCar
        );
        LapDAO.insert(newLap);
        lapsList.add(newLap);

        // System.out.println(newLap.toString());
        String[] row;
        if (lapsList.size() > 1) {
            row = new String[]{
                String.format("%02d", lapsList.size()), 
                getLapRelativeTime(newLap, lapsList.get(lapsList.size() - 2)),
                String.format("%02d:%02d.%03d", minsSnapshot, secsSnapshot, millisSnapshot)
            };
        } else {
            row = new String[]{
                String.format("%02d", lapsList.size()), 
                String.format("%02d:%02d.%03d", minsSnapshot, secsSnapshot, millisSnapshot),
                String.format("%02d:%02d.%03d", minsSnapshot, secsSnapshot, millisSnapshot)
            };

        }

        tbModelLapsTimes.addRow(row);
    }

    public void resetTimer(){
        if(timer != null){
            stopTimer();
        }

        resetCounter();
        updateTimerLabel();
        clearLaps();
    }
        

    public void clearLaps(){
        int rowCount = tbModelLapsTimes.getRowCount();
        for(int i = rowCount-1;i <= rowCount;i--){
            if(i==-1){
                break;
            }else{
            tbModelLapsTimes.removeRow(i);}
        }

        lapsList.clear();
        LapDAO.deleteAllByCar(CarDAO.getByName(new Car(selectedCar)));
    }
}