/*
 * A Java Swing experiment.
 * A PFrame has three custom panels, and can switch between them. It also restricts a program to creating only one instance of a PFrame by keeping a static instance of itself.
 * This demo uses a menubar.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.ImageIO;
import java.io.*;

class PFrame extends JFrame{
  private static PFrame myInstance; //an instance of itself, used to restrict the number of instances of a PFrame to a single shared instance among all components
  public static HomeScreen home;  //since there can only be one instance of PFrame, I thought it fitting (though redundant) to make its fields static (shared among all instances)
  public static OverWorld ow;
  public static BattleScreen battle;
   
  private PFrame(String title){
    super(title);
	home = new HomeScreen(this, "homescreen.png");
	ow = new OverWorld(this, "overworld.png");
	battle = new BattleScreen(this, "battlescreen.png");
	ow.setVisible(false);
	battle.setVisible(false);
	this.setContentPane(home);  //replaces the content pane with the home panel; using frame.getContentPane() will return the home panel
	this.setJMenuBar(this.createMenuBar());
	this.getJMenuBar().setVisible(false);
  }
  
  public static PFrame getInstance(){
    if (myInstance == null)
	  myInstance = new PFrame("");
	
	return myInstance;
  }

  public static PFrame getInstance(String title){
    if (myInstance == null)
	  myInstance = new PFrame(title);
	  
	return myInstance;
  }  
  
  public static JMenuBar createMenuBar(){  //should only ever be called once
	JMenuBar menuBar = new JMenuBar();
	JMenu menu;
	JMenuItem menuItem;
	
	//Build pokemon menu
	menu = new JMenu("Pokemon");
	menu.setMnemonic(KeyEvent.VK_P);
	menuBar.add(menu);
	
	menuItem = new JMenuItem("Charmander", KeyEvent.VK_C);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Pikachu", KeyEvent.VK_P);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Bulbasaur", KeyEvent.VK_B);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Squirtle", KeyEvent.VK_Q);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Charizard", KeyEvent.VK_Z);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Magikarp", KeyEvent.VK_K);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Eeevee", KeyEvent.VK_E);
	menu.add(menuItem);
	
    //Build bag menu
    menu = new JMenu("Bag");
	menu.setMnemonic(KeyEvent.VK_B);
	menuBar.add(menu);
	
	menuItem = new JMenuItem("Potion (4)", KeyEvent.VK_P);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Super Potion (2)", KeyEvent.VK_S);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Rope (23)", KeyEvent.VK_R);
	menu.add(menuItem);
	
	menuItem = new JMenuItem("Rare Candy (0)", KeyEvent.VK_C);
	menu.add(menuItem);
	
	//Build save menu
	menu = new JMenu("Save");
	menu.setMnemonic(KeyEvent.VK_S);
	menuBar.add(menu);
	
	//Build quit menu
	menu = new JMenu("Quit");
    menu.setMnemonic(KeyEvent.VK_Q);
	menu.addMouseListener(new MouseAdapter(){
	  public void mouseClicked(MouseEvent e){
		PFrame.getInstance().ow.setVisible(false);
		PFrame.getInstance().setContentPane(home);
		PFrame.getInstance().home.setVisible(true);
	  }
	});
    // menu.addMenuListener(new MenuListener(){
	  // public void menuCanceled(MenuEvent e){}
	  // public void menuDeselected(MenuEvent e){}
	  // public void menuSelected(MenuEvent e){
	    // PFrame.getInstance().ow.setVisible(false);
	    // PFrame.getInstance().setContentPane(home);
	    // PFrame.getInstance().home.setVisible(true);
	    // PFrame.updateMenuBar();  
	  // }
    // });
	menuBar.add(menu);
	
	return menuBar;
  }
  
  public static void updateMenuBar(){
    JMenuBar menuBar = PFrame.getInstance().getJMenuBar();
	JMenu menu;
		
    if (home.isVisible()){
	  menuBar.setVisible(false);
	}  
	if (ow.isVisible()){
	  menuBar.remove(3); //remove Run (or Quit) and replace with Quit
	  menu = new JMenu("Quit");
	  menu.setMnemonic(KeyEvent.VK_Q);
	  menu.addMouseListener(new MouseAdapter(){
	    public void mouseClicked(MouseEvent e){
		  PFrame.getInstance().ow.setVisible(false);
		  PFrame.getInstance().setContentPane(home);
		  PFrame.getInstance().home.setVisible(true);
		  PFrame.updateMenuBar();
	    }
	  });
	  // menu.addMenuListener(new MenuListener(){
	    // public void menuCanceled(MenuEvent e){}
		// public void menuDeselected(MenuEvent e){}
	    // public void menuSelected(MenuEvent e){
		  // PFrame.getInstance().ow.setVisible(false);
		  // PFrame.getInstance().setContentPane(home);
		  // PFrame.getInstance().home.setVisible(true);
		  // PFrame.updateMenuBar();  
		// }
	  // });
	  menuBar.add(menu);
	  menuBar.revalidate(); //refresh menubar to display changes
	  menuBar.setVisible(true);
	}	
	if (battle.isVisible()){
	  menuBar.remove(3); //remove Quit and replace with Run
	  menu = new JMenu("Run");
	  menu.setMnemonic(KeyEvent.VK_R);
	  menu.addMouseListener(new MouseAdapter(){
	    public void mouseClicked(MouseEvent e){
		  PFrame.getInstance().battle.setVisible(false);
		  PFrame.getInstance().setContentPane(ow);
		  PFrame.getInstance().ow.setVisible(true);
		  PFrame.updateMenuBar();
	    }
	  });
	  // menu.addMenuListener(new MenuListener(){
	    // public void menuCanceled(MenuEvent e){}
		// public void menuDeselected(MenuEvent e){}
	    // public void menuSelected(MenuEvent e){
		  // PFrame.getInstance().battle.setVisible(false);
		  // PFrame.getInstance().setContentPane(ow);
		  // PFrame.getInstance().ow.setVisible(true);
		  // PFrame.updateMenuBar();		
		// }
	  // });
	  menuBar.add(menu);
	  menuBar.revalidate(); //refresh menubar to display changes
	  menuBar.setVisible(true);
	}
  }
}

class ImagePanel extends JPanel{
  BufferedImage image;
  
  public ImagePanel(String url){
	try{
	  this.image = ImageIO.read(new File(url));
	}catch(IOException ex){
	  ex.printStackTrace();
	}
  }
  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
	g.drawImage(this.image, 0, 0, null);
  }
}

class HomeScreen extends ImagePanel implements ActionListener{
  public PFrame parent;
  public JPanel contentPane;
  
  public HomeScreen(PFrame parent, String url){
    super(url);
	this.parent = parent;
	this.setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(719, 476));	
	/*
	 * In a BorderLayout, components are resized to fill the region they're placed in. By adding components
     * to a patsy panel with a different layout, and placing the patsy in a BorderLayout, the panel is the component
	 * that gets resized (set transparent so the background image shows through), but the added component stays its preferred size.
	 * 
	 * Using Box.createRigidArea(), we can add what appears to be empty space (but is actually just a transparent rectangle) between
	 * visible components, to separate them or aid in positioning.
	 */
	 
	this.contentPane = new JPanel();
	this.contentPane.setOpaque(false); //allows the background image to show through
	this.contentPane.setLayout(new FlowLayout());
	
	this.add(this.contentPane, BorderLayout.CENTER);
	
	JButton button = new JButton("START");
	button.setPreferredSize(new Dimension(75, 50));
	button.addActionListener(this);
	
	this.contentPane.add(Box.createRigidArea(new Dimension(350, 600))); //offsets our button
	this.contentPane.add(button);
  }
  
  public JPanel getContentPane(){
    return this.contentPane;
  }
  
  public void actionPerformed(ActionEvent e){
    this.setVisible(false);
	this.parent.setContentPane(this.parent.ow);
	this.parent.ow.setVisible(true);
	PFrame.updateMenuBar();
  }
}

class OverWorld extends ImagePanel implements ActionListener{
  public PFrame parent;
  public JPanel contentPane;
  
  public OverWorld(PFrame parent, String url){
    super(url);
	this.parent = parent;
	this.setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(719, 476));	
	/*
	 * In a BorderLayout, components are resized to fill the region they're placed in. By adding components
     * to a patsy panel with a different layout, and placing the patsy in a BorderLayout, the panel is the component
	 * that gets resized (set transparent so the background image shows through), but the added component stays its preferred size.
	 * 
	 * Using Box.createRigidArea(), we can add what appears to be empty space (but is actually just a transparent rectangle) between
	 * visible components, to separate them or aid in positioning.
	 */
	 
	this.contentPane = new JPanel();
	this.contentPane.setOpaque(false); //allows the background image to show through
	this.contentPane.setLayout(new FlowLayout());
	
    JButton button = new JButton("Battle");
	button.setPreferredSize(new Dimension(75, 50));
	button.addActionListener(this);
	
	this.contentPane.add(Box.createRigidArea(new Dimension(200, 400))); //offsets our button
	this.contentPane.add(button);
	
	this.add(this.contentPane, BorderLayout.CENTER);
  }
  
  public JPanel getContentPane(){
    return this.contentPane;
  }
  
  public void actionPerformed(ActionEvent e){
    this.setVisible(false);
	this.parent.setContentPane(this.parent.battle);
	this.parent.battle.setVisible(true);
	PFrame.updateMenuBar();
  }
}

class BattleScreen extends ImagePanel implements ActionListener{
  public PFrame parent;
  public JPanel contentPane;
  
  public BattleScreen(PFrame parent, String url){
    super(url);
	this.parent = parent;
	this.setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(719, 476));	
	/*
	 * In a BorderLayout, components are resized to fill the region they're placed in. By adding components
     * to a patsy panel with a different layout, and placing the patsy in a BorderLayout, the panel is the component
	 * that gets resized (set transparent so the background image shows through), but the added component stays its preferred size.
	 * 
	 * Using Box.createRigidArea(), we can add what appears to be empty space (but is actually just a transparent rectangle) between
	 * visible components, to separate them or aid in positioning.
	 */
	 
	this.contentPane = new JPanel();
	this.contentPane.setOpaque(false); //allows the background image to show through
	this.contentPane.setLayout(new FlowLayout());
	
	this.add(this.contentPane, BorderLayout.CENTER);
  }
  
  public JPanel getContentPane(){
    return this.contentPane;
  }
 
  public void actionPerformed(ActionEvent e){
    this.setVisible(false);
	this.parent.setContentPane(this.parent.ow);
	this.parent.ow.setVisible(true);
	PFrame.updateMenuBar();
  }
}

public class PokeBeta{
  public static void runGUI(){
    PFrame frame = PFrame.getInstance("!");
	frame.setDefaultCloseOperation(3);
	frame.setSize(719, 476);
	frame.pack();    
	frame.setVisible(true);
  }
  
  public static void main(String[] args){
	//Schedule a job for the event dispatch thread:
	//creating and showing this application's GUI.
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
	      //Turn off metal's use of bold fonts
		  UIManager.put("swing.boldMetal", Boolean.FALSE);
		  runGUI();
		}
	});
  }
}