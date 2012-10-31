import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class TFrame extends JFrame{
    public final HPanel home;
    public final JPanel ow;
    public final JPanel panels;
    public final String HOMEPANEL = "Home";
    public final String OWPANEL = "Overworld";
    public final CardLayout layout = new CardLayout();
    
    public TFrame(String title){
	super(title);
	this.home = new HPanel();

	this.home.start.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    layout.show(panels, OWPANEL);
		}
	    }
	);
	this.home.setVisible(true);

	this.ow = new ScrollPanel();
	JButton button = new JButton("HOME");
	button.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    layout.show(panels, HOMEPANEL);
		}
	    });
        this.ow.add(button);
	this.panels = new JPanel(this.layout);
	this.panels.add(this.home, this.HOMEPANEL);
        this.panels.add(this.ow, this.OWPANEL);
	
	this.setDefaultCloseOperation(3);
	this.setSize(500,300);
	this.setVisible(true);
    }
}

class HPanel extends JPanel{
    public JPanel contentPane;
    public JButton start, exit;
    
    public HPanel(){
	this.setLayout(new BorderLayout());
	this.setPreferredSize(new Dimension(500, 300));
	this.contentPane = new JPanel();
	this.contentPane.setOpaque(true);
	this.contentPane.setLayout(new FlowLayout());

	this.add(this.contentPane, BorderLayout.CENTER);
	
	this.start = new JButton("START");
	this.start.setPreferredSize(new Dimension(75, 50));
	
	this.exit = new JButton("EXIT");
	this.exit.setPreferredSize(new Dimension(75, 50));
	
	this.contentPane.add(start);
	this.contentPane.add(exit);
    }

    public JPanel getContentPane(){
	return this.contentPane;
    }
}

public class Test{
    public static void createAndRun(){
	TFrame frame = new TFrame("Test Frame");
    }
    
    public static void main(String[] args){
	createAndRun();
    }
}
