import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

public class JFrameAPS extends JFrame implements ComponentListener{
    //Variables
    private final Clipboard cpb = Toolkit.getDefaultToolkit()
    .getSystemClipboard();
    private final double screenWidth = Toolkit.getDefaultToolkit()
    .getScreenSize().getWidth();
    private final int jframeWidth = Math.round((float) (screenWidth/1.5));
    private final int jframeHeight = Math.round((float) (jframeWidth*3/4));

    //components
    private final JPanel container = new JPanel();
    private PanelLeft panelLeft;
    private PanelRight panelRight;

    /*
     * @author:Marcos Felipe and Rodrigo Chaveiro
     */
    public JFrameAPS(){
        //JFrame configuration
        setTitle("Cronometro");
        setSize(new Dimension(jframeWidth,jframeHeight));
        setMinimumSize(getSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        organizeLayout();
        SwingUtilities.updateComponentTreeUI(this);
        setLocationRelativeTo(null);
        setVisible(true);
        setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        getRootPane().addComponentListener(this);
        //addKeyListener(this);
        KeyboardFocusManager kbFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kbFocusManager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
               if(e.getID() == KeyEvent.KEY_RELEASED){
                    if(e.getKeyCode() == KeyEvent.VK_PRINTSCREEN){
                        StringSelection stringSelection = new StringSelection("");
                        cpb.setContents(stringSelection, null);
                    }
                }else if (e.getID() == KeyEvent.KEY_PRESSED){
                    if(KeyEvent.VK_0<=e.getKeyCode() && e.getKeyCode()<=KeyEvent.VK_9){
                        //if it is a numeric key, select that car
                        panelLeft.setSelectedIndexAtCarList(
                            Integer.parseInt(KeyEvent.getKeyText(e.getKeyCode()-1)));
                    }else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        //if its enter lap the car
                        if (panelLeft.getSelectedIndexAtCarList() != null) panelRight.lapTimer();
                    }
                }
                return false;
            }
        });
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void organizeLayout(){
        //container configuration
        container.setLayout(new BorderLayout(2, 1));
        container.setSize(getSize());
        container.setVisible(true);

        this.panelLeft = new PanelLeft(container);
        this.panelRight = new PanelRight(container, null);
        panelLeft.setPanelRight(panelRight);

        //add panels to container
        container.add(this.panelRight,BorderLayout.CENTER);
        container.add(this.panelLeft,BorderLayout.LINE_START);
        for(Component component : this.panelRight.getComponents()){
            component.setVisible(false);
        }
        
        //add container to JFrame
        add(container);
    }

    //method to update container and panels size
    private void updateLayout(){
        if(getExtendedState() == JFrame.MAXIMIZED_BOTH){
            container.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            SwingUtilities.updateComponentTreeUI(this);
        }else{
            container.setSize(getSize());
            SwingUtilities.updateComponentTreeUI(this);
        }
    }
    //Events implemented by KeyListener
    //Events implemented by WindowListener
    @Override
    public void componentResized(ComponentEvent e) {
        updateLayout();
    }
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
}