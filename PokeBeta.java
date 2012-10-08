/*
 * A Java Swing experiment.
 * A PFrame has three custom panels, and can switch between them. It also restricts a program to creating only one instance of a PFrame by keeping a static instance of itself.
 * PokeBeta uses serialization to add a save/load feature.
 * Every serializable class must contain a private serialVersionUID to ensure a loaded class corresponds exactly to a serialized object.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

class PFrame extends JFrame implements Serializable{
  private static final long serialVersionUID = -4840769033005188509L;
  private static PFrame myInstance; //an instance of itself, used to restrict the number of instances of a PFrame to a single shared instance among all components
  public HomeScreen home;
  public OverWorld ow;
  public BattleScreen battle;
   
  private PFrame(String title){
    super(title);
	home = new HomeScreen(this, "homescreen.png");
	ow = new OverWorld(this, "overworld.png");
	battle = new BattleScreen(this, "battlescreen.png");
	//home.setVisible(true);
	ow.setVisible(false);
	battle.setVisible(false);
	this.setContentPane(home);  //replaces the content pane with the home panel; using frame.getContentPane() will return the home panel
	this.setJMenuBar(this.createMenuBar());
	this.getJMenuBar().setVisible(false);
  }//end PFrame(String) constructor
  
  public static PFrame getInstance(){
    if (myInstance == null)
	  myInstance = new PFrame("");
	
	return myInstance;
  }//end getInstance()

  public static PFrame getInstance(String title){
    if (myInstance == null)
	  myInstance = new PFrame(title);
	  
	return myInstance;
  }//end getInstance(String)
  
  public void destroy(){
    this.dispose();
	myInstance = null;
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
	menuBar.add(menu);
	
	return menuBar;
  }//end createMenuBar()
  
  public int confirmQuit(){
	Object[] options = {"Leave now.", "Play some more!"};
	return JOptionPane.showOptionDialog(myInstance, "You sure?", "Don't leave yet!",
                                 JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
  }
  
  public int confirmRun(){
	Object[] options = {"Run like a girl.", "Fight like a man."};
	return JOptionPane.showOptionDialog(myInstance, "You sure?", "Don't be a pussy.",
                                 JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);   
  }
  
  public static void updateMenuBar(){
    JMenuBar menuBar = PFrame.myInstance.getJMenuBar();
	JMenu menu;
		
    if (myInstance.home.isVisible()){
	  menuBar.setVisible(false);
	}else if (myInstance.ow.isVisible()){
	  menuBar.remove(3); //remove Run (or Quit) and replace with Quit
	  menu = new JMenu("Quit");
	  menu.setMnemonic(KeyEvent.VK_Q);
	  menu.addMouseListener(new MouseAdapter(){
	      public void mouseClicked(MouseEvent e){
		    if (myInstance.confirmQuit() == 0){
  		      myInstance.ow.setVisible(false);
		      myInstance.setContentPane(myInstance.home);
		      myInstance.home.setVisible(true);
		      PFrame.updateMenuBar();
			}
		  }//end mouseClicked()
		}//end MouseAdapter()
	  );//end addMouseListener()

	  menuBar.add(menu);
	  menuBar.revalidate(); //refresh menubar to display changes
	  menuBar.setVisible(true);
	}else if (myInstance.battle.isVisible()){
	  menuBar.remove(3); //remove Quit and replace with Run
	  menu = new JMenu("Run");
	  menu.setMnemonic(KeyEvent.VK_R);
	  menu.addMouseListener(new MouseAdapter(){
	      public void mouseClicked(MouseEvent e){
		    if (myInstance.confirmRun() == 0){
  		      myInstance.battle.setVisible(false);
		      myInstance.setContentPane(myInstance.ow);
		      myInstance.ow.setVisible(true);
		      PFrame.updateMenuBar();
			}
		  }//end mouseClicked()
		}//end MouseAdapter()
	  );//end addMouseListener()
	  
	  menuBar.add(menu);
	  menuBar.revalidate(); //refresh menubar to display changes
	  menuBar.setVisible(true);
	}
  }//end updateMenuBar()
  
  public void save(){
    try{
	  //open up a stream for writing objects and saving them to a tmp file
	  FileOutputStream fileOut = new FileOutputStream("save.ser");
	  ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
	  
	  //write a custom GUI object to disk, flush and close the output stream
	  outStream.writeObject(myInstance);//myInstance, PFrame's instance of itself
	  outStream.flush();
	  outStream.close();
	  
	  JOptionPane.showMessageDialog(myInstance, "Rejoice, for you have been saved.", "Save successful!", JOptionPane.INFORMATION_MESSAGE);
	}catch(IOException ex){
	  JOptionPane.showMessageDialog(myInstance, "Something's gone wrong.", "Save unsuccessful D:", JOptionPane.ERROR_MESSAGE);
	  System.out.println("Exception during save: " + ex);
	}//end try-catch block
  }//end save()
  
  public void load(){
    PFrame.getInstance().destroy(); //dispose of current PFrame
    try{
      //open up a stream for reading objects
	  FileInputStream fileIn = new FileInputStream("save.ser");
	  ObjectInputStream inStream = new ObjectInputStream(fileIn);
	  
	  try{
	    //read saved PFrame object from file
		PFrame newObject = (PFrame)inStream.readObject();
		newObject.myInstance = newObject;
		//reload the background images of the panels; BufferedImage does not serialize
		newObject.home.image = ImageIO.read(new File(newObject.home.imageURL));
		newObject.ow.image = ImageIO.read(new File(newObject.ow.imageURL));
		newObject.battle.image = ImageIO.read(new File(newObject.battle.imageURL));
		
		//invoke one of the objects methods to instantiate and register
		//listener objects on the components in the PFrame, thereby (re)activating it
		newObject.activate();
	  }catch(ClassNotFoundException ex){
	    System.out.println("Class not found: " + ex);
	  }//end try-catch block
	  
	  inStream.close();	  
	}catch(IOException ex){
	  System.out.println("IOException during load: " + ex);
	}//end try-catch block
  }//end load()
  
  //sets event listeners and frame configuration
  public void activate(){	
	//activate menuBar
	JMenuBar menuBar = myInstance.getJMenuBar();
	JMenu menu;
	
	menu = menuBar.getMenu(2); //save menu
	menu.addMouseListener(new MouseAdapter(){
	    public void mouseClicked(MouseEvent e){
		  myInstance.save();
		  System.out.println("I'm saved!");
		}//end mouseClicked()
	  }//end MouseAdapter()
	);//end addMouseListener()
	
	menu = menuBar.getMenu(3); //quit menu
	if (battle.isVisible()){
      menu.addMouseListener(new MouseAdapter(){
	      public void mouseClicked(MouseEvent e){
  		    battle.setVisible(false);
		    myInstance.setContentPane(ow);
		    ow.setVisible(true);
		    PFrame.updateMenuBar();
		  }//end m	ouseClicked()
		}//end MouseAdapter()
	  );//end addMouseListener()
	}else{ //run menu
	  menu.addMouseListener(new MouseAdapter(){
	      public void mouseClicked(MouseEvent e){
  		    myInstance.ow.setVisible(false);
		    myInstance.setContentPane(myInstance.home);
		    myInstance.home.setVisible(true);
		    PFrame.updateMenuBar();
		  }//end mouseClicked()
		}//end MouseAdapter()
	  );//end addMouseListener()
	}//end if-else block
		
	//activate panel buttons
	JButton button;
	
	button = home.start;
	button.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent e){
          home.setVisible(false);
	      myInstance.setContentPane(ow);
	      ow.setVisible(true);
	      PFrame.updateMenuBar();
        }//end actionPerformed(ActionEvent)
	  }//end ActionListener()
	);//end addActionListener()
	
	button = home.load;
	button.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent e){
		  myInstance.load();
		  System.out.println("I'm loaded!");
		}//end actionPerformed()
	  }//end ActionListener()
	);//end addActionListener()
	
	button = ow.button;
	button.addActionListener(new ActionListener(){    
	    public void actionPerformed(ActionEvent e){
		  ow.setVisible(false);
		  myInstance.setContentPane(battle);
		  battle.setVisible(true);
		  PFrame.updateMenuBar();
	    }//end actionPerformed(ActionEvent)
	  }//end ActionListener()
	);//end addActionListener()
	
	//configure frame
	myInstance.setDefaultCloseOperation(3);
	myInstance.setSize(new Dimension(735, 515));
	//for some reason pack() doesn't work quite right when loading; the y-dimension is expanded a bit
	// myInstance.pack(); //sets the size to the preferred size to contain its components
	myInstance.setVisible(true);
	myInstance.repaint(); //refreshes /updates the PFrame
  }//end activate()
}//end PFrame
//=============================================================================//
class ImagePanel extends JPanel implements Serializable{
  private static final long serialVersionUID = -5240337832879965429L;
  public transient BufferedImage image; //does not serialize! needs to be reloaded
  public String imageURL;
  
  public ImagePanel(){
    this.image = null;
	this.imageURL = null;
  }//end no-arg constructor
  public ImagePanel(String url){
	try{
	  this.imageURL = url;
	  this.image = ImageIO.read(new File(url));
	}catch(IOException ex){
	  ex.printStackTrace();
	}
  }//end ImagePanel(String) constructor
  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
	g.drawImage(this.image, 0, 0, null);
  }//end paintComponent(Graphics)
}//end ImagePanel
//=============================================================================//
class HomeScreen extends ImagePanel implements Serializable{
  private static final long serialVersionUID = -8143264756541624637L;
  public PFrame parent;
  public JPanel contentPane;
  public JButton start, load;
  
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
	
	start = new JButton("START");
	start.setPreferredSize(new Dimension(75, 50));
	
	load = new JButton("CONTINUE");
	load.setPreferredSize(new Dimension(100, 50));
	
	this.contentPane.add(Box.createRigidArea(new Dimension(350, 600))); //offsets our button
	this.contentPane.add(start);
	this.contentPane.add(load);
  }//end HomeScreen(PFrame, String) constructor
  
  public JPanel getContentPane(){
    return this.contentPane;
  }//end getContentPane()
}//end HomeScreen
//=============================================================================//
class OverWorld extends ImagePanel implements Serializable{
  private static final long serialVersionUID = -6255601745199140851L;
  public PFrame parent;
  public JPanel contentPane;
  public JButton button;
  
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
	
    button = new JButton("Battle");
	button.setPreferredSize(new Dimension(75, 50));
	
	this.contentPane.add(Box.createRigidArea(new Dimension(200, 400))); //offsets our button
	this.contentPane.add(button);
	
	this.add(this.contentPane, BorderLayout.CENTER);
  }//end OverWorld(PFrame, String) constructor
  
  public JPanel getContentPane(){
    return this.contentPane;
  }//end getContentPane()
}//end OverWorld
//=============================================================================//
class BattleScreen extends ImagePanel implements Serializable{
  private static final long serialVersionUID = -1565258954189052862L;
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
  }//end BattleScreen(PFrame, String) constructor
  
  public JPanel getContentPane(){
    return this.contentPane;
  }//end getContentPane()
}//end BattleScreen
//=============================================================================//
public class PokeBeta{
  public static void runGUI(){
    PFrame frame = PFrame.getInstance("Serializable PokeBeta");
    frame.activate();
  }//end runGUI()
  
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
  }//end main(String[])
}//end PokeBeta