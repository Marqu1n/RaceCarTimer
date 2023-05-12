import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import persistance.dao.CarDAO;
import persistance.model.Car;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelLeft extends JPanel implements ActionListener{
    //TODO-Get elements from sqlDB using JDBC
    //Test array
    private DefaultListModel<String> carsListModel = new DefaultListModel<>();
    private JList<String> carsJList = new JList<String>(carsListModel);
    private JScrollPane listScrollPane = new JScrollPane();

    private JButton addButton;
    private JButton removeButton;
    private JPanel container;
    private PanelRight panelRight;
    
    public PanelLeft(JPanel container){
        //left panel configuration
        this.container = container;
        setPreferredSize(new Dimension(
            Math.round(container.getWidth()*0.33f),
            container.getHeight())
            );
        setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        setAlignmentX(LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        add(listPanel());
        add(buttonsPanel());
    }

    public JPanel listPanel(){
        //List initialization and configuration
        JPanel panelList = new JPanel();
        JLabel titleLabel = new JLabel("Lista de Carros:");
        titleLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        panelList.add(titleLabel);
        panelList.setLayout(new FlowLayout(FlowLayout.CENTER,0,15));
        
        carsJList.setFixedCellHeight(25);
        /* carsJList.setPreferredSize(listScrollPane.getSize()); */

        listScrollPane.setViewportView(carsJList);
        listScrollPane
        .setPreferredSize(new Dimension(Math.round(container.getWidth()*0.28f),
        Math.round(container.getHeight()*0.75f)));
        listScrollPane.createVerticalScrollBar();

        carsJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    if(!carsJList.isSelectionEmpty()) {
                        panelRight.setSelectedCar(getSelectedIndexAtCarList());
                        panelRight.changeVisibility(true);
                    } else {
                        panelRight.changeVisibility(false);
                    }
                }
            }
        });

        carsJList.setLayoutOrientation(JList.VERTICAL);
        panelList.setBackground(Color.WHITE);

        for(Car car: CarDAO.getAll()) {
            carsListModel.addElement(car.getName());
        }

        //add list panel to main panel
        panelList.add(listScrollPane,BorderLayout.CENTER);
        return panelList;
        
    }

    public JPanel buttonsPanel(){
        //Button panel
        JPanel containerButtons = 
        new JPanel(new FlowLayout(FlowLayout.CENTER,15,5));
        containerButtons.setBackground(Color.WHITE);

        //Add button
        addButton = new JButton("+");
        addButton.setFont(new Font("trebuchet ms"
        ,Font.PLAIN,50));
        addButton.setPreferredSize(new Dimension(100,100));
        addButton.addActionListener(this);
        addButton.setBackground(Color.WHITE);
        addButton.setFocusable(false);

        //Remove button
        removeButton = new JButton("-");
        removeButton.setFont(new Font("trebuchet ms"
        ,Font.PLAIN,50));
        removeButton.setPreferredSize(new Dimension(100,100));
        removeButton.addActionListener(this);
        removeButton.setBackground(Color.WHITE);
        removeButton.setFocusable(false);

        containerButtons.add(addButton);
        containerButtons.add(removeButton);

        return containerButtons;
    }

    public void setPanelRight(PanelRight panelRight){
        this.panelRight = panelRight;
    }

    public DefaultListModel<String> getCarList(){
        return this.carsListModel;
    }

    public void setSelectedIndexAtCarList(int index){
        carsJList.setSelectedIndex(index);
    }

    public String getSelectedIndexAtCarList(){
        return carsJList.getSelectedValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getActionCommand().equals("+")){
                //get carName
                String carName = JOptionPane.showInputDialog(this.getParent()
                ,"Insira o nome do Carrinho:"
                ,"Adicionar novo Carrinho"
                ,JOptionPane.PLAIN_MESSAGE);
                //verify if is already on the list or the user didn't put a name
                if(carName != null && carName.length() != 0){
                    if(!carsListModel.contains(carName)){
                        CarDAO.insert(new Car(carName));

                        carsListModel.addElement(carName);
                        JOptionPane.showConfirmDialog(this.getParent()
                        ,"Clique em algum carrinho pra começar a cronometrar"
                        ,"Ajuda"
                        ,JOptionPane.DEFAULT_OPTION);
                    }else{
                        throw new ArrayStoreException();
                    }
                }else if(carName != null){
                    throw new IllegalArgumentException();
                }
            }else{
                //remove selected car from list
                if(e.getActionCommand().equals("-")){
                    if(!carsJList.isSelectionEmpty()) {
                        CarDAO.deleteById(
                            CarDAO.getByName(new Car(getSelectedIndexAtCarList())));
                    }
                    carsListModel.removeElementAt(carsJList.getSelectedIndex());
                }
            }

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this.getParent(),
            "Digite algo para adicionar à lista",
            "Erro ao adicionar elemento",
            JOptionPane.ERROR_MESSAGE);

        }catch (ArrayStoreException exception) {
            JOptionPane.showMessageDialog(this.getParent(),
            "Elemento já presente na lista",
            "Erro ao adicionar elemento",
            JOptionPane.ERROR_MESSAGE);

        }catch(ArrayIndexOutOfBoundsException exception){
            JOptionPane.showMessageDialog(this.getParent(),
            "Selecione algum elemento para remover",
            "Erro ao remover elemento",
            JOptionPane.ERROR_MESSAGE);
        }
    }
}
