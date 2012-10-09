import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.imageio.ImageIO;


class ScrollPanel extends JPanel{
  Map map;
  Map house;
  Map meadow;
  BufferedImage player;
  BufferedImage playerBack;
  BufferedImage playerLeft;
  BufferedImage playerLeft2;
  BufferedImage playerLeft3;
  BufferedImage playerRight;
  BufferedImage exclamation;
  BufferedImage image;
  Point[][] points;
  Point playerPosition;
  int universalI = 0;
  int universalJ = 0;
  int height;
  int width;
  int h;
  int w;
  int w2;
  int h2;
  int x;
  int y;
  boolean u = false;
  boolean d = false;
  boolean l = false;
  boolean r = false;
  int rl = 0;
  int ud = 0;
  boolean check = true;
  javax.swing.Timer t;
  javax.swing.Timer t2;
  boolean outside = true;
  int warpx = 13;
  int warpy = 6;



  public ScrollPanel() {
    super();
	meadow = new Map(1);
	house = new Map(2);
	map = meadow;
	points = new Point[11][9];
	try{
		player = Toolkit.getDefaultToolkit().getImage(getClass().getResource("character.png"));
		playerBack = Toolkit.getDefaultToolkit().getImage(getClass().getResource("characterback.png"));
		playerLeft = Toolkit.getDefaultToolkit().getImage(getClass().getResource("characterleft.png"));
		playerLeft2 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("characterleft2.png"));
		playerLeft3 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("characterleft3.png"));
		playerRight = Toolkit.getDefaultToolkit().getImage(getClass().getResource("characterright.png"));
		exclamation = Toolkit.getDefaultToolkit().getImage(getClass().getResource("exclamation.png"));
		image = player;
	}catch(IOException e){
	  System.out.println(e);
	}
	this.t = new javax.swing.Timer(10, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
	  if (d) {
	    ud = ud - 2;
		repaint();
		if (ud <= -1 * h) {
		  t.stop();
		  d = false;
		  ud = 0;
		  universalJ++;
		}
	  }	 
	  if (u) {
	    ud = ud + 2;
		repaint();
		if (ud >= h) {
		  t.stop();
		  u = false;
		  ud = 0;
		  universalJ--;
		}
	  }
	  if (r) {
	    rl = rl - 2;
		repaint();
		if (rl <= -1 * w) {
		  t.stop();
		  r = false;
		  rl = 0;
		  universalI++;
		}
	  }
	  if (l) {
	    rl = rl + 2;
		repaint();
		if (rl >= w) {
		  t.stop();
		  l = false;
		  rl = 0;
		  universalI--;
		}
	  }
	  
	  //warp codez
      if (outside) {
	    if (universalI == 13 && universalJ == 6) {
	      universalI = 4;
		  universalJ = 5;
		  t2.stop();
	      map = house;
		  outside = false;
		  repaint();
		}
	  }
	  else {
		  if ((universalI == 4 && universalJ == 6) || (universalI == 5 && universalJ == 6)) {
			map = meadow;
			t2.start();
		    universalI = 13;
			universalJ = 7;
			outside = true;
			repaint();
		  }
		}
	}
  });
  this.t2 = new javax.swing.Timer(2000, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
	  if (check) {
	    if (!(universalI == 3 && universalJ == 11)) {
	      map.tiles[8][15] = map.gary2;
	      map.tiles[8][16] = map.grass;
		  check = false;
		}
		else {
		  map.tiles[8][16] = map.gary2;
		}
	  }
	  else {
	    if (!(universalI == 3 && universalJ == 12)) {
	      map.tiles[8][15] = map.grass;
		  map.tiles[8][16] = map.gary;
		  map.tiles[8][14] = map.grass;
		  check = true;
		}
		else {
		  map.tiles[8][15] = map.gary;
		  map.tiles[8][14] = new Tile(exclamation, false);
		}
	  }
	  repaint();
	}
  });
  t2.start();
  }
  
  public void keyPressed(KeyEvent key) {
    if (u || d || l || r) { }
	else {
    if (key.getKeyCode() == key.VK_DOWN) {
      image = player;
      if (map.tiles[universalI + 5][universalJ + 5].walkable != false) {
		  d = true;
		  t.start();
	  }
	}
	else if (key.getKeyCode() == key.VK_UP) {
      image = playerBack;
      if (map.tiles[universalI + 5][universalJ + 3].walkable != false) {
		u = true;
	    t.start();
	  }
	}
	else if (key.getKeyCode() == key.VK_RIGHT) {
      image = playerRight;
      if (map.tiles[universalI + 6][universalJ + 4].walkable != false) {
		r = true;
	    t.start();
	  }
	}
	else if (key.getKeyCode() == key.VK_LEFT){
      image = playerLeft;
      if (map.tiles[universalI + 4][universalJ + 4].walkable != false) {
		l = true;
		t.start();
	  }
	}
    repaint();
	}
  }
  
  public void keyTyped(KeyEvent key){ }   
  public void keyReleased(KeyEvent key){ }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
	
	//draws the background
	    for(int i = 0; i < 11; i++) {
	      for (int j = 0; j < 9; j++) {
		    Point p = points[i][j];
	        g.drawImage(map.tiles[universalI + i][universalJ + j].getImage(), p.getX() + rl, p.getY() + ud, w, h, this);
	      }
	    }

	g.drawImage(image, playerPosition.getX(), playerPosition.getY(), w, h, this);
	System.out.println(universalI);
	System.out.println(universalJ);
  }

  void getSizes() {
  	this.height = getSize().height;
	this.width = getSize().width;
	this.h = (int) height / 7;
	this.w = (int) width / 9;
	this.w2 = (int) width % 9 / 2;
	this.h2 = (int) height % 7 / 2;
	this.x = w2 - w;
	this.y = h2 - h;
	System.out.println("I'm being resized!");
	setPoints();
  }
  
  void setPoints() {
	//fills the point array
	for(int i = 0; i < 11; i++) {
	  for (int j = 0; j < 9; j++) {
	    points[i][j] = new Point(x, y);
		y = y + h;
	  }
	  y = h2 - h;
	  x = x + w;
	}
	playerPosition = new Point(4 * this.w + w2,3 * this.h + h2);
	repaint();
  }
  
  
    public void componentHidden(ComponentEvent e) { }

    public void componentMoved(ComponentEvent e) { }

    public void componentResized(ComponentEvent e) {
        getSizes();        
    }
    public void componentShown(ComponentEvent e) { }
  
}

class Point{
  int x;
  int y;

  Point(int x, int y) {
    this.x = x;
	this.y = y;
  }
  
  int getX() {
    return this.x;
  }
  
  int getY() {
    return this.y;
  }
  
  void setX(int x){
    this.x = x;
  }
  
  void setY(int y) {
    this.y = y;
  }
  
  //double getDistance(Point other) {
    //return Math.sqrt(Math.pow(other.getX() - this.getX(), 2) + Math.pow(other.getY() - this.getY(), 2));
  //}
}

class Tile{
  BufferedImage image;
  boolean walkable;
  
  Tile(BufferedImage image, boolean walkable) {
	this.image = image;
	this.walkable = walkable;
  }
  
  BufferedImage getImage(){
    return this.image;
  }
}

class Map{
  String name;
  Tile[][] tiles;
  Tile gary;
  Tile gary2;
  Tile grass;
  
  Map(String name, Tile[][] tiles) {
    this.name = name;
	this.tiles = tiles;
	try{
		this.gary = new Tile(ImageIO.read(new File("character2.png")), false);
		this.gary2 = new Tile(ImageIO.read(new File("character2back.png")), false);
		this.grass = new Tile(ImageIO.read(new File("grass.png")), true);
	}catch(IOException e){
		System.out.println(e);
	}
  }
  
  Map(int n) {
    this.name = "Map Demo";
	try{
	Tile water = new Tile(ImageIO.read(new File("water.png")), false);
	Tile tree = new Tile(ImageIO.read(new File("tree.png")), false);
	Tile rock = new Tile(ImageIO.read(new File("rock.png")), false);
	Tile tall = new Tile(ImageIO.read(new File("tall.png")), true);
	Tile pc1 = new Tile(ImageIO.read(new File("pc1.png")), false);
	Tile pc2 = new Tile(ImageIO.read(new File("pc2.png")), true);
	Tile pc3 = new Tile(ImageIO.read(new File("pc3.png")), false);
	Tile pc4 = new Tile(ImageIO.read(new File("pc4.png")), false);
	Tile pc5 = new Tile(ImageIO.read(new File("pc5.png")), false);
	Tile floor = new Tile(ImageIO.read(new File("tile.png")), true);
	Tile wall = new Tile(ImageIO.read(new File("wall.png")), false);
	Tile black = new Tile(ImageIO.read(new File("black.png")), false);
	}catch(IOException e){
		System.out.println(e);
	}
    if (n == 1) {
	Tile[][] tiles = new Tile[30][30];
    //sets grass
	for (int i = 5; i < 25; i++) {
	  for (int j = 4 ; j < 26; j++) {
		tiles[i][j] = grass;
	  }
	}
	//sets lake in middle of map
	for (int i = 7; i < 11; i++) {
	  for (int j = 7; j < 10; j++) {
	    tiles[i][j] = water;
	  }
	}
	
	//sets rocks
	tiles[7][7] = rock;
	tiles[8][7] = rock;
	tiles[9][7] = rock;
	tiles[10][7] = rock;
	tiles[6][7] = rock;
	tiles[6][8] = rock;
	tiles[6][9] = rock;
	tiles[11][7] = rock;
	tiles[11][8] = rock;
	tiles[11][9] = rock;
	
	//sets tall grass
	for (int i = 15; i < 25; i++) {
	  for (int j = 16; j < 26; j++) {
	    tiles[i][j] = tall;
	  }
	}
	
	//sets up the house
	tiles[17][8] = pc3;
	tiles[18][8] = pc4;
	tiles[19][8] = pc5;
	tiles[17][9] = pc1;
	tiles[18][9] = pc1;
	tiles[19][9] = pc1;
	tiles[17][10] = pc1;
	tiles[18][10] = pc2;
	tiles[19][10] = pc1;

	
	//sets trees border for map
	for (int i = 0; i < 5; i++) {
	  for (int j = 0; j < 30; j++) {
	    tiles[i][j] = tree;
	  }
	}
	for (int i = 25; i < 30; i++) {
	  for (int j = 0; j < 30; j++) {
	    tiles[i][j] = tree;
	  }
	}
	for (int j = 0; j < 4; j++) {
	  for (int i = 0; i < 30; i++) {
	    tiles[i][j] = tree;
	  }
	}
	for (int j = 26; j < 30; j++) {
	  for (int i = 0; i < 30; i++) {
	    tiles[i][j] = tree;
	  }
	}
	this.tiles = tiles;
	}
	else {
	  Tile[][] tiles = new Tile[20][15];
	  for (int i = 0; i < 5; i++) {
	    for (int j = 0; j < 15; j++) {
		  tiles[i][j] = black;
		}
	  }
	  
	  for (int i = 15; i < 20; i++) {
	    for (int j = 0; j < 15; j++) {
		  tiles[i][j] = black;
		}
	  }
	  
	  for (int i = 5; i < 15; i++) {
	    for (int j = 4; j < 10; j++) {
		  tiles[i][j] = floor;
		}
	  }
	  
	  for (int i = 5; i < 15; i++) {
	    for (int j = 10; j < 15; j++) {
		  tiles[i][j] = black;
		}
	  }
	  
	  for (int i = 0; i < 5; i++) {
	    for (int j = 0; j < 15; j++) {
		  tiles[i][j] = black;
		}
	  }
	  
	  for(int i = 5; i <15; i++) {
	    for (int j = 0; j < 4; j++) {
		  tiles[i][j] = wall;
		}
	  }
	  
	  tiles[9][10] = floor;
	  tiles[10][10] = floor;
      this.tiles = tiles;
	}
	
  }
}