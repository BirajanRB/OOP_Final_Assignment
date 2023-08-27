package graphicsProgramFinalAssignmentOOP;

import uk.ac.leedsbeckett.oop.LBUGraphics;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JOptionPane;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.Random;

import java.sql.DriverManager;
import java.sql.Connection;


public class GraphicsSystem extends LBUGraphics
{	
	//Array of accepted commands.
	private String[] commandCollection = {"about","penup","pendown","turnleft","turnright","forward","backward","black","green","red","white","reset","clear","save","load","square","squarecentered","pencolour","penwidth","backgroundcolour","triangle","hexagram","hexagon","octagram","octagon","circle","rgbmodeon","rgbmodeoff","colorchooser","mousemodeon","mousemodeoff","help","tictactoe","tictactoeexit"};
	
	//save counter
	private boolean saveCounter = true;
	
	//Command Entered Buffer
	private StringBuffer strBufferObj = new StringBuffer();
	
	//RGB mode counter
	private boolean rgbModeBool = false;
	
	//Help menu
	private JFrame helpWindow;
	private JSlider rValue,gValue,bValue;
	private JPanel currentColor;
	
	//Help menu brush size and type selections 
	private String[] brushSizeValue;
	private String[] brushTypeValue;
	private JComboBox <JComboBox> brushType;
	private JComboBox <JComboBox> brushSize;
	
	//default brush = Round Brush
	private String currentBrush = "Round Brush";
	
	//select color button
	private JButton colourChooser;
	
	//image button - open and upload any image to the turtle graphic
	private JButton imageSelector;
	
	//mouse on off buttons for help menu
	private JButton mouseOn;
	private JButton mouseOff;
	
	//mouse mode counter
	private boolean mouseModeBool = false;
	
	//TicTacToe - game mode one or off
	private boolean ticTacToeBoolean = false;
	
	//ticTacToe box occupied counter
	private boolean[] ticTacToeBox = new boolean[9];
	
	//ticTacToe Box occupied player
	private char[] ticTacToeBoxOwner = new char[9];
	
	//Player Turn
	//cross = x
	//circle = o
	private char turn;
	
	//TicTacToe winner
	//default is null - n
	private char winner = 'n';

	public static void main(String[] args)
	{
		try
		{
			//register driver.
			Class.forName("com.mysql.cj.jdbc.Driver");
			//establishing connection to check for Database availability.
			Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/turtlegraphicdata","root","");
			//closing test connection.
			co.close();
			
			//Database login 
			new DatabaseLogin();
			
		}
		catch(Exception e)
		{
			//database link failure - automatically opens turtleGraphic.
			new GraphicsSystem();
		}
	}
	
	
	public GraphicsSystem()
	{
        JFrame MainFrame = new JFrame("TurtleGraphics"); 
        MainFrame.setLocation(20,20);
        MainFrame.setLayout(new FlowLayout());    
        MainFrame.add(this);                       
        MainFrame.setSize(850,450);               
        MainFrame.setVisible(true);   
        MainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        MainFrame.addWindowListener(new WindowListenerClass());
        MainFrame.addMouseMotionListener(new MouseMotionListenerClass());
        MainFrame.addMouseListener(new MouseListenerClass());
        
        
        //about();
        reset();
        penDown();
	}

	
	public void about()
	{
		//overriding the about method.
	
		try
		{
			File loadImage = new File("name.png");
			BufferedImage bufferedImageObjLoad = ImageIO.read(loadImage);
			setBufferedImage(bufferedImageObjLoad);
		}
		catch(Exception e)
		{
			displayMessage("Error loading Name");
		}
		
		//calling the parent class about method.
		super.about();

	}
	
	public void square(int length)
	{	
		penDown();
		int i;
		for(i = 0;i<4;i++)
		{		
			turnLeft();
			forward(length);
		}

	}
	
	public void squareCentered(int length)
	{	
		//initial position.
		int xPos = getxPos();
		int yPos = getyPos();
		
		
		reset();
		forward(length/2);
		turnRight();
		forward(length/2);
		turnLeft();
		
		penDown();
		int i;
		for(i = 0;i<4;i++)
		{		
			turnLeft();
			forward(length);
		}
		
		//going back to the original position after this command has been issued.
		setxPos(xPos);
		setyPos(yPos);
		
		//fixing teleport issues.
		penUp();
		forward(0);
		penDown();
	}
	
	
	public void penColour(int colourR, int colourG, int colourB)
	{
		try
		{
			Color c = new Color(colourR,colourG,colourB);
			setPenColour(c);
		}
		catch(Exception e)
		{
			//In case our of bound arguments are given.
			displayMessage("Color parameter outside of expected range: Red:255 Green:255 Blue:255");
		}
	}
	
	
	public void penWidth(int width)
	{
		setStroke(width);
	}
	
	public void backgroundColour(int colourR, int colourG, int colourB)
	{
		try
		{
			//selected color
			Color c = new Color(colourR,colourG,colourB);
			
			//painting the background
			reset();
			penDown();
			setPenColour(c);
			setStroke(10000);
			setTurtleSpeed(0);
			turnLeft();
			forward(10);
			
			//reset the turtle
			setTurtleSpeed(1);
			setxPos(400);
			setyPos(200);
			reset();
			penColour(255,0,0);
			penDown();
		}
		catch(Exception e)
		{
			//In case our of bound arguments are given.
			displayMessage("Color parameter outside of expected range: Red:255 Green:255 Blue:255");
		}
	}
	
	public void triangle(int length)
	{
		//Draws a triangle with equal sides.
		turnLeft();
		penDown();
			
		int i;
		for(i=0;i<3;i++)
		{
			
			forward(length);
			
			if(i<2)
			{
				turnLeft(120);
			}
			
		}
	}
	
	
	public void triangle(int lengthOne, int lengthTwo, int lengthThree)
	{	
		//overloading triangle function.
		//Draws a triangle with specific sides.
	
		double a,b,largestLengthC,angleA,angleB,angleC;
		
		if(lengthOne>lengthTwo && lengthOne>lengthThree)
		{
			//case where lengthOne is the largest
			
			a = lengthTwo;
			b = lengthThree;
			
			largestLengthC = lengthOne;
			
		}
		else if(lengthTwo>lengthOne && lengthTwo>lengthThree)
		{
			//case where lengthTwo is the largest
			a = lengthOne;
			b = lengthThree;
			
			largestLengthC = lengthTwo;
		}
		else
		{	
			//case where lengthThree is the largest
			a = lengthOne;
			b = lengthTwo;
			
			largestLengthC = lengthThree;
		}
		
				
		//Using Cosine Law to find the largest angle vai the longest side.
		//c^2 = a^2 + b^2 -2*a*b*Cos(angle)
		//Cos(angle) =  -(c^2 - (a^2+b^2)) / 2ab
		//angle = cos*-1( -(c^2 - (a^2+b^2)) / 2ab) )
		double value;
		value = -(Math.pow(largestLengthC, 2) - ( Math.pow(a, 2) + Math.pow(b, 2))) /(2*a*b) ;
		angleC = Math.toDegrees(Math.acos(value)); // returns the angleC degree
		
		//using Sine Law to find the other angles.
		//sideA/sinA = sideB/sinB = sideC/sinC
		//A = (sin(c)*a/c)sin^-1
		//B = (sin(c)*b/c)sin^-1
		angleA = Math.toDegrees( Math.asin( Math.sin(Math.toRadians(angleC)) * (a/largestLengthC) ) );
		angleB = Math.toDegrees( Math.asin( Math.sin(Math.toRadians(angleC)) * (b/largestLengthC) ) );
		
		turnLeft();
		forward((int) largestLengthC);
		
		turnLeft(180 - (int) angleB);
		forward((int) a);
		
		turnLeft(180 - (int) angleC );
		forward((int) b);
		
		turnLeft(180 - (int) angleA);
		
	}
	
	public void hexagram(int length)
	{
		//Heron's formula 
		//s = (a+b+c)/2
		//a = sq(s(s-a)(s-b)(s-c))
		double s = (length+length+length)/2;
		double areaOfTriangle = Math.sqrt(s*((s-length)*(s-length)*(s-length)));
		
		//h=(2*A)/b
		//can also be said to be the median
		double heightOfTriangle = (2*areaOfTriangle)/length;
		
		//the distance between the vertex and the centroid of a triangle is 2/3 of the median of the triangle.
		int midPoint = (int) (heightOfTriangle/3)*2;	
		
		triangle(length);
		turnLeft(120);
		turnLeft(30);
		penUp();
		forward((int) midPoint);
		forward((int) midPoint);
		penDown();
		turnLeft(150);
		forward(length);
		turnLeft(120);
		forward(length);
		turnLeft(120);
		forward(length);
	}
	
	public void hexagon(int length)
	{
		turnLeft();
		penDown();
		
		for(int i=0;i<=5;i++)
		{
			forward(length);
			turnLeft(60);
		}

	}
	
	public void octagram(int length)
	{
		square(length);
		
		//fixing position for 2nd sqaure.
		penUp();
		turnLeft();
		forward(length/2);
		turnLeft();
		forward(length/2);
		turnLeft(45);
		forward(length/2);
		turnLeft();
		forward(length/2);
		
		square(length);
	}
	
	public void octagon(int length)
	{
		turnLeft();
		penDown();
		
		for(int i=0;i<=7;i++)
		{
			forward(length);
			turnLeft(45);
		}
	}
	
	public void rgbModeOn()
	{
		int[] rgbValue = {255,255,255};
		Random randObj = new Random();
		
		rgbModeBool = true;
		
		//Loops through the RGB value until RGB mode is turned off.
		while(rgbModeBool == true)
		{
			int i = randObj.nextInt(0,3);
			
			if(rgbValue[i] == 255)
			{
				while(rgbValue[i] > 0)
				{
					penColour(rgbValue[0],rgbValue[1],rgbValue[2]);
					
					try
					{
						//Updates Help UI
						currentColor.setBackground(getPenColour());
					}
					catch(Exception e)
					{
						//In case Help UI is not visible
					}

					--rgbValue[i];
					
					try 
					{
						Thread.sleep(5);
					} 	
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					
					if(rgbModeBool == false)
					{
						break;
					}
				}
			}
			else if(rgbValue[i]==0)
			{
				while(rgbValue[i] < 255)
				{
					penColour(rgbValue[0],rgbValue[1],rgbValue[2]);
					
					try
					{
						//Updates Help UI
						currentColor.setBackground(getPenColour());
					}
					catch(Exception e)
					{
						//In case Help UI is not visible
					}
					
					++rgbValue[i];
					
					try 
					{
						Thread.sleep(5);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					
					if(rgbModeBool == false)
					{
						break;
					}
				}
			}
		}
		
	}
	
	public void rgbModeOff()
	{
		//Turns off RGB mode.
		rgbModeBool = false;
	}
	
	public void colorChooser()
	{
		Color newColor = JColorChooser.showDialog(null,"Choose Color", getPenColour());
		setPenColour(newColor);
		
		try
		{
			//Updates Help UI 
			currentColor.setBackground(getPenColour());
		}
		catch(Exception e)
		{
			//Does noting if Help window is not opened
		}
	}
	
	public void help()
	{
		try
		{	
			//closes Help window if it already exists
			helpWindow.dispose();
		}
		catch(Exception e)
		{
			
		}
		
		//Help window MainFrame
		helpWindow = new JFrame("Help Window");
		helpWindow.setLocation(860,20);
		helpWindow.setVisible(true);
		helpWindow.setSize(600,600);
		helpWindow.setLayout(new FlowLayout());
		helpWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		helpWindow.addWindowListener(new HelpWindowListenerClass());
		
		//command list panel
		JPanel commandList = new JPanel();
		commandList.setPreferredSize(new Dimension(200,600));
		commandList.setLayout(new BoxLayout(commandList,BoxLayout.Y_AXIS));
		
		//an Array of JLabel class references
		JLabel[] commandAvailable = new JLabel[commandCollection.length];
		
		for(int i=0;i<commandCollection.length;i++)
		{
			//referencing each reference variable with an JLabel object.
			//Specifies each JLabel object with Command names.
			commandAvailable[i] = new JLabel();
			commandAvailable[i].setText(i+1+". "+commandCollection[i]);
			commandAvailable[i].setPreferredSize(new Dimension(200,50));
			
			//adds each JLabel object into commandList panel, one by one.
			commandList.add(commandAvailable[i]);
		}
		
		
		//UI panel
		JPanel helpUI = new JPanel();
		helpUI.setPreferredSize(new Dimension(400,600));
		helpUI.setLayout(new BoxLayout(helpUI,BoxLayout.Y_AXIS));
		
		//RGB value slider
		rValue = new JSlider(JSlider.HORIZONTAL,0,255,255);
		rValue.setPaintTicks(true);
		rValue.setPaintLabels(true);
		rValue.setMajorTickSpacing(51);
		rValue.addChangeListener(new SliderListenerClass());
		
		gValue = new JSlider(JSlider.HORIZONTAL,0,255,0);
		gValue.setPaintTicks(true);
		gValue.setPaintLabels(true);
		gValue.setMajorTickSpacing(51);
		gValue.addChangeListener(new SliderListenerClass());
		
		bValue = new JSlider(JSlider.HORIZONTAL,0,255,0);
		bValue.setPaintTicks(true);
		bValue.setPaintLabels(true);
		bValue.setMajorTickSpacing(51);
		bValue.addChangeListener(new SliderListenerClass());
		
		//adding sliders into HelpUI panel
		helpUI.add(new JLabel("R"));
		helpUI.add(rValue);
		helpUI.add(Box.createRigidArea(new Dimension(400,30)));
		
		helpUI.add(new JLabel("G"));
		helpUI.add(gValue);
		helpUI.add(Box.createRigidArea(new Dimension(400,30)));
		
		helpUI.add(new JLabel("B"));
		helpUI.add(bValue);
		helpUI.add(Box.createRigidArea(new Dimension(400,30)));
		
		
		//CurrentColor checker
		currentColor = new JPanel();
		currentColor.setPreferredSize(new Dimension(50,50));
		currentColor.setBackground(getPenColour());
		currentColor.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//adding current color panel to helpUI panel
		helpUI.add(currentColor);
		helpUI.add(Box.createRigidArea(new Dimension(400,30)));
		
		
		//Brush size and type selection.
		JPanel uiBrushSizeType = new JPanel();
		uiBrushSizeType.setLayout(new FlowLayout());
		
		//Brush size
		brushSizeValue = new String[100];
		for(int i=1;i<=brushSizeValue.length;i++)
		{
			//brush size range 1 -100.
			brushSizeValue[i-1] = i+"";
		}
		
		//Brush types
		brushTypeValue = new String[]{"Round Brush","Square Brush","Circle Brush","Rectangle Brush","3D Rectangle Brush","Arc Brush","Fill Arc Brush"};
		
		//Adding the values to the JComboBoxs
		brushSize = new JComboBox(brushSizeValue);
		brushSize.addActionListener(new HelpMenuBrushListenerClass());
		brushType = new JComboBox(brushTypeValue);
		brushType.addActionListener(new HelpMenuBrushListenerClass());
		
		//adding brush size and type to uiBrushSizeShape panel
		uiBrushSizeType.add(new JLabel("  Brush Size: "));
		uiBrushSizeType.add(brushSize);
		uiBrushSizeType.add(new JLabel("  Brush Type: "));
		uiBrushSizeType.add(brushType);
		 
		//adding uiBrushSizeShape to helpUI panel
		helpUI.add(uiBrushSizeType);
		
		
		//Open image & Color picker panel for HelpUI
		JPanel panelColourChooserAndImageSelector= new JPanel();
		panelColourChooserAndImageSelector.setLayout(new FlowLayout());
		
		//Open any image in turtleGraphics for editing from help window + color chooser button
		colourChooser = new JButton("Select Color");
		colourChooser.addActionListener(new HelpMenuColorChooserListenerClass());
		imageSelector = new JButton("Select Image");	
		imageSelector.addActionListener(new HelpMenuSelectImageListenerClass());
		
		//adding color chooser and image selector buttons to the panelColourChooserAndImageSelector Panel
		panelColourChooserAndImageSelector.add(colourChooser);
		panelColourChooserAndImageSelector.add(imageSelector);
		
		//adding panelColourChooserAndImageSelector to helpUI panel
		helpUI.add(panelColourChooserAndImageSelector);
		
		
		//Mouse Mode on and off panel for HelpUI
		JPanel mouseMode = new JPanel();
		mouseMode.setLayout(new FlowLayout());
		
		//Mouse buttons
		mouseOn = new JButton("Mouse On");
		mouseOn.addActionListener(new HelpMenuMouseButtonListenerClass());
		mouseOff  = new JButton("Mouse Off");
		mouseOff.addActionListener(new HelpMenuMouseButtonListenerClass());
		
		//Adding Mouse buttons to MouseMode Panel
		mouseMode.add(mouseOn);
		mouseMode.add(mouseOff);
		
		//Adding the MouseMode panel to helpUI panel
		helpUI.add(mouseMode);
		
		
		//Adding helpUI & commandList panels into the Main frame
		helpWindow.getContentPane().add(commandList);
		helpWindow.getContentPane().add(helpUI);
		helpWindow.pack();
	}
	
	public void ticTacToe()
	{
		clear();
		reset();
		
		//indicating the game is called.
		ticTacToeBoolean = true;
		
		for(int i=0;i<ticTacToeBox.length;i++)
		{
			//setting all ticTacToeBox references false for initial game starting
			//indicates weather it is occupied or not.
			ticTacToeBox[i] = false;
		}
		
		for(int i=0;i<ticTacToeBoxOwner.length;i++)
		{
			//setting all ticTacToeBoxOwner references as null-'n' for initial game starting.
			ticTacToeBoxOwner[i] = 'n';
		}
		
		//initial turn for circle
		turn = 'o';
		
		//Resets the winner each game.
		winner = 'n';
		
		
		penDown();
		penWidth(6);
		setPenColour(Color.white);
		//each position area is of 100*100
		
		//vertical line
		setxPos(300);
		setyPos(50);
		forward(300);
		
		setxPos(400);
		setyPos(50);
		forward(300);
		
		//horizontal line
		setxPos(200);
		setyPos(150);
		turnLeft();
		forward(300);
		
		setxPos(200);
		setyPos(250);
		forward(300);
		
		//default position for turtle
		reset();
		penUp();
		penWidth(6);
		setxPos(600);
		setyPos(100);
		forward(1);
		setTurtleSpeed(0);
		
		
	}
	
	public void ticTacToeExit()
	{
		//indicating the game has ended.
		
		if(ticTacToeBoolean == true)
		{
			//only execute if the game is running
			ticTacToeBoolean = false;
			
			
			clear();
			reset();
			penDown();
		}
		
		//resets the speed to normal
		setTurtleSpeed(1);

	}
	
	
	public void processCommand(String command)
	{	
		//Prints the Command Entered.
		displayMessage("Command Entered: "+command);
		
		//Splitting Entered Command into An array of String objects.
		String[] commandEntered = command.split(" ");
		
		//Method parameter values.
		int[] enteredParameterValue = new int[3];
		
		//Collection Class ArrayList for accepted commands.
		ArrayList acceptedCommandCollection = new ArrayList();
		
		for(String i: commandCollection)
		{	
			//Adding Accepted Commands
			acceptedCommandCollection.add(i);
		}
		
		
		//Command validation is done first.
		//parameter validation is done second.
		if(acceptedCommandCollection.contains(commandEntered[0].toLowerCase()))
		{
			//Valid Command Case.
			
			//Case where too many parameters are entered.
			if(commandEntered.length>4)
			{	
				displayMessage("Too many parameters entered.");
				
				//Ends function Execution in case of error.
				return;
			}
			
			//Parameter Filtering & conversion.
			switch(commandEntered.length)
			{		
				case 2:
					//One parameter Case
					try
					{
						//Converting Entered String parameter into integer value.
						enteredParameterValue[0] = Integer.parseInt(commandEntered[1]);
					}
					catch(NumberFormatException exceptionName)
					{
						displayMessage("Non numeric data for parameter.");
						
						//Ends function Execution in case of error.
						return;
					}
					finally
					{
						//negative or non sensible values validation.
						if(enteredParameterValue[0]<0)
						{
							displayMessage("Negative or non sensible value is entered.");
							//Ends function Execution in case of negative or non sensible values.
							return;
						}
					}
					break;
					
				case 3:
					//two parameter Case.
					try
					{
						//Converting Entered String parameter into integer value.
						enteredParameterValue[0] = Integer.parseInt(commandEntered[1]);
						enteredParameterValue[1] = Integer.parseInt(commandEntered[2]);
					}
					catch(NumberFormatException exceptionName)
					{
						displayMessage("Non numeric data for parameter.");
						
						//Ends function Execution//Ends function Execution in case of error.
						return;
					}
					finally
					{
						//negative or non sensible values validation.
						if(enteredParameterValue[0]<0 || enteredParameterValue[1]<0)
						{
							displayMessage("Negative or non sensible value is entered.");
							
							//Ends function Execution in case of negative or non sensible values.
							return;
						}
					}
					break;
								
				case 4:	
					//Three parameter Case.
					try
					{
						//Converting Entered String parameter into integer value.
						enteredParameterValue[0] = Integer.parseInt(commandEntered[1]);
						enteredParameterValue[1] = Integer.parseInt(commandEntered[2]);
						enteredParameterValue[2] = Integer.parseInt(commandEntered[3]);
					}
					catch(NumberFormatException exceptionName)
					{
						displayMessage("Non numeric data for parameter.");
						
						//Ends function Execution in case of error.
						return;
					}
					finally
					{
						//negative or non sensible values validation.
						if(enteredParameterValue[0]<0 || enteredParameterValue[1]<0 || enteredParameterValue[2]<0)
						{
							displayMessage("Negative or non sensible value is entered.");
							
							//Ends function Execution in case of negative or non sensible values.
							return;
						}
					}
					break;		
			}
			
			
			
			//Command filtering.
			switch(commandEntered[0].toLowerCase())
			{
				case "about":
					about();
					
					//save counter False to symbolize Unsaved status as changes have been made.
					saveCounter = false;
					break;
					
				case "reset":
					setxPos(400);
					setyPos(200);
					reset();
					penColour(255,0,0);
					penDown();
					break;
					
				case "penup":
					penUp();
					break;
					
				case "pendown":
					penDown();
					break;
					
				case "turnleft":
					if(commandEntered.length>1)
					{
						//Case where 2 parameter are given - command + value.
						turnLeft(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						turnLeft();
					}
					break;
					
				case "turnright":
					
					if(commandEntered.length>1)
					{
						//Case where 2 parameter are given - command + value.
						turnRight(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						turnRight();
					}				
					break;
					
				case "forward":
					if(commandEntered.length>1)
					{
						//Case where 2 parameter are given - command + value.
						forward(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;	
					
				case "backward":
					if(commandEntered.length>1)
					{
						//Case where 2 parameter are given - command + value.
						forward(enteredParameterValue[0]-(enteredParameterValue[0]*2));
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made.
					saveCounter = false;
					break;
					
				case "black":					
					//Sets pen color to black.
					setPenColour(Color.black);
					break;
					
				case "green":
					//Sets pen color to green.
					setPenColour(Color.green);
					break;
					
				case "red":				
					//Sets pen color to red.
					setPenColour(Color.red);			
					break;
					
				case "white":
					//Sets pen color to white.
					setPenColour(Color.white);					
					break;
					
				case "clear":
					if(saveCounter == true)
					{
						//Clears the display
						clear();
						
						//Clears the String buffer.
						strBufferObj.delete(0,strBufferObj.length());
						
						return;
					}
					else
					{
						//Non-Saved image case.
						
						int choice = JOptionPane.showConfirmDialog(null, "Current image/commands is not saved! Do you wish to Continue?");
						
						if(choice == JOptionPane.YES_OPTION)
						{
							//clears the display
							clear();
							
							//Clears the String buffer after each save.
							strBufferObj.delete(0,strBufferObj.length());
							
							//save counter True to symbolize saved status.
							saveCounter = true;
						}
						
						//returns statement to avoid being added to the string command buffer
						return;
					}
					
				case "save":
					try
					{
						//Save Image
						File savedImage = new File("savedImage.png");
						BufferedImage bufferedImageObjSave  = getBufferedImage();
						ImageIO.write(bufferedImageObjSave,"png", savedImage);
						
						//Saves Command
						File savedCommand = new File("savedCommand.txt");
						FileWriter fWriterObj = new FileWriter(savedCommand);
						BufferedWriter bWriterObj = new BufferedWriter(fWriterObj);
						bWriterObj.write(strBufferObj.toString());
						bWriterObj.close();
						
						//Clears the String buffer after each save.
						strBufferObj.delete(0,strBufferObj.length());
						
						//save counter True to symbolize saved status.
						saveCounter = true;
						
						//save dialog box
						JOptionPane.showMessageDialog(null, "Image & command Saved!");
						
						//returns statement to avoid being added to the string command buffer
						return;						
					}
					catch(Exception e)
					{
						displayMessage("Error Saving Image");
					}	
					break;
					
				case "load":
					
					if(saveCounter == true)
					{
						try
						{	
							//reset turtle position.
							reset();
							penDown();
							
							//Load Image
							File loadImage = new File("savedImage.png");
							BufferedImage bufferedImageObjLoad = ImageIO.read(loadImage);
							setBufferedImage(bufferedImageObjLoad);
							
							
							//load Saved Commands.
							File savedCommand = new File("savedCommand.txt");
							FileReader fReaderObj = new FileReader(savedCommand);
							BufferedReader bReaderObj = new BufferedReader(fReaderObj);
													
							String line = bReaderObj.readLine(); //reads the first line of command file.
							while(line != null)
							{	
								processCommand(line);
								line = bReaderObj.readLine();
							}
							bReaderObj.close();
							
							//save counter True to symbolize saved status.
							saveCounter = true;
							
							//returns statement to avoid being added to the string command buffer
							return;
						}
						catch(Exception e)
						{
							displayMessage("Error Loading Image");
						}
					}
					else
					{	
						//Non-Saved image case.
						
						int choice = JOptionPane.showConfirmDialog(null, "Current image/commands is not saved! Do you wish to Continue?");
						
						if(choice == JOptionPane.YES_OPTION)
						{
							//save counter True to symbolize saved status.
							saveCounter = true;
							
							//load the old image
							processCommand("load");
						}
						
						//returns statement to avoid being added to the string command buffer
						return;
					}
					break;
					
					
				case "square":
					if(commandEntered.length>1)
					{
						//Case where parameter is given - command + value.
						square(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;	
					
					
				case "squarecentered":
					//Centered version of square method.
					if(commandEntered.length>1)
					{
						//Case where parameter is given - command + value.
						squareCentered(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;	
					
				case "pencolour":
					if(commandEntered.length==4)
					{
						//Case where parameter is given - command + value.
						penColour(enteredParameterValue[0], enteredParameterValue[1], enteredParameterValue[2]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					break;
					
					
				case "penwidth":
					if(commandEntered.length>1)
					{
						//Case where parameter is given - command + value.
						penWidth(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					break;
					
				case "backgroundcolour":
					if(commandEntered.length==4)
					{
						//Case where parameter is given - command + value.
						//changes the background color
						backgroundColour(enteredParameterValue[0], enteredParameterValue[1], enteredParameterValue[2]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					break;
					
					
				case "triangle":
					if(commandEntered.length == 2)
					{
						//Case where one parameter is given - command + value.
						triangle(enteredParameterValue[0]);
					}
					else if(commandEntered.length == 4)
					{
						//Case where three parameter are given - command + parameter list.
						triangle(enteredParameterValue[0], enteredParameterValue[1], enteredParameterValue[2]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;	
					
				case "hexagram":
					if(commandEntered.length >= 2)
					{
						//Case where one parameter is given - command + value.
						hexagram(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;
					
				case "hexagon":
					if(commandEntered.length >= 2)
					{
						//Case where one parameter is given - command + value.
						hexagon(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;
					
				case "octagram":
					if(commandEntered.length >= 2)
					{
						//Case where one parameter is given - command + value.
						octagram(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;	
					
				case "octagon":
					if(commandEntered.length >= 2)
					{
						//Case where one parameter is given - command + value.
						octagon(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;	
					
					
				case "circle":
					if(commandEntered.length >= 2)
					{
						//Case where one parameter is given - command + value.
						circle(enteredParameterValue[0]);
					}
					else
					{
						//case where no parameter is given.
						displayMessage("Valid command with missing parameter.");
					}
					
					//save counter False to symbolize Unsaved status as changes have been made..
					saveCounter = false;
					break;	
					
				case "rgbmodeon":
					rgbModeOn();
					
					//Avoids being added to the string buffer/ saved commands
					return;
					
					
				case "rgbmodeoff":
					rgbModeOff();
					
					//Avoids being added to the string buffer/ saved commands
					return;
					
				case "colorchooser":
					colorChooser();
					
					//Avoids being added to the string buffer/ saved commands
					return;
					
				case "mousemodeon":
					mouseModeBool = true;
					
					//Avoids being added to the string buffer/ saved commands
					return;
					
				case "mousemodeoff":
					mouseModeBool = false;
					
					//Avoids being added to the string buffer/ saved commands
					return;
					
				case "help":
					help();
					
					//Avoids being added to the string buffer/ saved commands
					return;
					
				case "tictactoe":
					//TicTacToe - The game.
					ticTacToe();
					
					//Avoids being added to the string buffer/ saved commands
					return;
					
				case "tictactoeexit":
					//TicTacToe - The game escape.
					ticTacToeExit();
					
					//Avoids being added to the string buffer/ saved commands
					return;
			}
			
		}
		else
		{	
			//invalid Command Case.
			displayMessage("Invalid Command.");
		}
		
		
		//Commands Entered into string buffer
		strBufferObj.append(command+"\n");
		
		try
		{
			//Updates Help UI 
			currentColor.setBackground(getPenColour());
		}
		catch(Exception e)
		{
			//Does noting if Help window is not opened
		}
		
	}
	
	
	
	//inner classes
	//WindowListener for Main Frame.
	private class WindowListenerClass extends WindowAdapter
	{

		public void windowClosing(WindowEvent event)
		{
			
			if(saveCounter == true)
			{
				event.getWindow().dispose();
			}
			else
			{
				int result = JOptionPane.showConfirmDialog(null, "The current image is not saved, do you wish to exit?");
				
				if(result == JOptionPane.YES_OPTION)
				{
					event.getWindow().dispose();
				}
			}
			
		}

	}
	
	//MouseMotionListener class for Main Frame
	private class MouseMotionListenerClass extends MouseMotionAdapter
	{
		
		public void mouseMoved(MouseEvent mEvent)
		{	
			if(mouseModeBool == true)
			{
				//sets turtle positions relative to the mouse position
				setxPos(mEvent.getX()-40);
				setyPos(mEvent.getY()-40);
				turnLeft(2);
				  
			}
		}
		
		public void mouseDragged(MouseEvent mEvent)
		{
			if(mouseModeBool == true)
			{
			    //sets turtle positions relative to the mouse position
				setxPos(mEvent.getX()-40);
				setyPos(mEvent.getY()-40);
				turnRight(2);
				
				//gets the graphical context on the invocation frame
			    Graphics frameGraphicContext = getGraphicsContext();
			    
			    //setColor
			    frameGraphicContext.setColor(getPenColour());  
			    
			    //initial width set to 0 for fillRoundRect()
			    int penSize = (int) getStroke()+1;
			    
				//Drawing on the Turtle graphical context frame.
				if(currentBrush.equals("Round Brush"))
				{
					//Round Brush
					frameGraphicContext.fillRoundRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,penSize,penSize);
				}
				else if(currentBrush.equals("Square Brush"))
				{
					//Square Brush
					frameGraphicContext.fillRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize);
				}
				else if(currentBrush.equals("Circle Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.drawOval(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize);
				}
				else if(currentBrush.equals("Rectangle Brush"))
				{
					//Rectangle Brush
					frameGraphicContext.drawRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize);
				}
				else if(currentBrush.equals("3D Rectangle Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.draw3DRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,true);
				}
				else if(currentBrush.equals("Arc Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.drawArc(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,80,80);
				}
				else if(currentBrush.equals("Fill Arc Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.fillArc(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,80,80);
				}

				//save counter False to symbolize Unsaved status as changes have been made..
				saveCounter = false;
			}
		}
		
	}
	
	//MouseMotionListener class for Main Frame
	private class MouseListenerClass extends MouseAdapter
	{
		public void mousePressed(MouseEvent mEvent)
		{
			if(mouseModeBool == true)
			{
				//Same as mouseDragged method but only executed every time the mouse is clicked.
				
				setxPos(mEvent.getX()-40);
				setyPos(mEvent.getY()-40);
				turnRight(2);
				
				//gets the graphical context on the invocation frame
			    Graphics frameGraphicContext = getGraphicsContext();
			    
			    //setColor
			    frameGraphicContext.setColor(getPenColour());  
			    
			    //initial width set to 0 for fillRoundRect()
			    int penSize = (int) getStroke()+1;
			    
				//Drawing on the Turtle graphical context frame.
				if(currentBrush.equals("Round Brush"))
				{
					//Round Brush
					frameGraphicContext.fillRoundRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,penSize,penSize);
				}
				else if(currentBrush.equals("Square Brush"))
				{
					//Square Brush
					frameGraphicContext.fillRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize);
				}
				else if(currentBrush.equals("Circle Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.drawOval(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize);
				}
				else if(currentBrush.equals("Rectangle Brush"))
				{
					//Rectangle Brush
					frameGraphicContext.drawRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize);
				}
				else if(currentBrush.equals("3D Rectangle Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.draw3DRect(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,true);
				}
				else if(currentBrush.equals("Arc Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.drawArc(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,80,80);
				}
				else if(currentBrush.equals("Fill Arc Brush"))
				{
					//3D Rectangle Brush
					frameGraphicContext.fillArc(mEvent.getX()-40,mEvent.getY()-40,penSize,penSize,80,80);
				}
				
				//save counter False to symbolize Unsaved status as changes have been made..
				saveCounter = false;
			}
		}
		
		public void mouseClicked(MouseEvent mEvent)
		{
			//if TicTacToe is Started
			if(ticTacToeBoolean == true)
			{
				//mouse co-od
				int x = mEvent.getX();
				int y = mEvent.getY();
				
				//default position for turtle
				reset();
				penUp();
				penWidth(6);
				setxPos(600);
				setyPos(100);
				forward(1);


				if(x<=300 && x>200)
				{
					if(y<=150 && y>50)
					{
						//box 1
						if(ticTacToeBox[0] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[0] = 'o';
									
									setxPos(200);
									setyPos(150);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[0] = 'x';
									
									drawLine(getPenColour(),200, 150, 300, 50);
									drawLine(getPenColour(),300, 150, 200, 50);
										
									
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[0] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
					else if(y<=250 && y>150)
					{
						//box 2
						if(ticTacToeBox[1] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[1] = 'o';
									
									setxPos(200);
									setyPos(250);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[1] = 'x';
									
									drawLine(getPenColour(),200, 250, 300, 150);
									drawLine(getPenColour(),300, 250, 200, 150);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[1] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
					else if(y<=350 && y>250)
					{
						//box 3
						if(ticTacToeBox[2] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[2] = 'o';
									
									setxPos(200);
									setyPos(350);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[2] = 'x';
									
									drawLine(getPenColour(),200, 350, 300, 250);
									drawLine(getPenColour(),300, 350, 200, 250);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[2] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
				}
				
				else if(x<=400 && x>300)
				{
					if(y<=150 && y>50)
					{
						//box 4
						if(ticTacToeBox[3] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[3] = 'o';
									
									setxPos(300);
									setyPos(150);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[3] = 'x';
									
									drawLine(getPenColour(),300, 150, 400, 50);
									drawLine(getPenColour(),400, 150, 300, 50);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[3] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
					else if(y<=250 && y>150)
					{
						//box 5
						if(ticTacToeBox[4] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[4] = 'o';
									
									setxPos(300);
									setyPos(250);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[4] = 'x';
									
									drawLine(getPenColour(),300, 250, 400, 150);
									drawLine(getPenColour(),400, 250, 300, 150);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[4] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
					else if(y<=350 && y>250)
					{
						//box 6
						if(ticTacToeBox[5] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[5] = 'o';
									
									setxPos(300);
									setyPos(350);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[5] = 'x';
									
									drawLine(getPenColour(),300, 350, 400, 250);
									drawLine(getPenColour(),400, 350, 300, 250);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[5] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
				}
				
				else if(x<=500 && x>400)
				{
					if(y<=150 && y>50)
					{
						//box 7
						if(ticTacToeBox[6] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[6] = 'o';
									
									setxPos(400);
									setyPos(150);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[6] = 'x';
									
									drawLine(getPenColour(),400, 150, 500, 50);
									drawLine(getPenColour(),500, 150, 400, 50);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[6] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
						
					}
					else if(y<=250 && y>150)
					{
						//box 8
						if(ticTacToeBox[7] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[7] = 'o';
									
									setxPos(400);
									setyPos(250);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[7] = 'x';
									
									drawLine(getPenColour(),400, 250, 500, 150);
									drawLine(getPenColour(),500, 250, 400, 150);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[7] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
					else if(y<=350 && y>250)
					{
						//box 9
						if(ticTacToeBox[8] == false)
						{
							//Unused box case
							switch(turn)
							{
								case 'o':
									//circle's turn
									ticTacToeBoxOwner[8] = 'o';
									
									setxPos(400);
									setyPos(350);
									turnLeft();
									forward(50);
									turnLeft();
									forward(50);
									
									penDown();
									circle(30);
									
									//next turn for cross
									turn = 'x';
									break;
									

								case 'x':
									//cross's turn
									ticTacToeBoxOwner[8] = 'x';
									
									drawLine(getPenColour(),400, 350, 500, 250);
									drawLine(getPenColour(),500, 350, 400, 250);
										
									//next turn for circle
									turn = 'o';
									break;	
							}
							
							//symbolize used
							ticTacToeBox[8] = true;
							
						}
						else
						{
							//box already in use
							JOptionPane.showMessageDialog(null, "Already selected!");
						}
					}
				}
				
				for(int i=0;i<9;i+=3)
				{
					//vertical win checker
					if(ticTacToeBoxOwner[i] == 'o' && ticTacToeBoxOwner[i+1] == 'o' && ticTacToeBoxOwner[i+2]=='o')
					{
						winner = 'o';
						JOptionPane.showMessageDialog(null, "Player Circle Wins!");
					}
					else if(ticTacToeBoxOwner[i] == 'x' && ticTacToeBoxOwner[i+1] == 'x' && ticTacToeBoxOwner[i+2]=='x')
					{
						winner = 'x';
						JOptionPane.showMessageDialog(null, "Player Cross Wins!");
					}
				}
				
				for(int i=0;i<3;i++)
				{
					//Horizontal win checker
					if(ticTacToeBoxOwner[i] == 'o' && ticTacToeBoxOwner[i+3] == 'o' && ticTacToeBoxOwner[i+6]=='o')
					{
						winner = 'o';
						JOptionPane.showMessageDialog(null, "Player Circle Wins!");
					}
					else if(ticTacToeBoxOwner[i] == 'x' && ticTacToeBoxOwner[i+3] == 'x' && ticTacToeBoxOwner[i+6]=='x')
					{
						winner = 'x';
						JOptionPane.showMessageDialog(null, "Player Cross Wins!");
					}
				}
				
				//diagonal lines win checker
				if(ticTacToeBoxOwner[0] == 'o' && ticTacToeBoxOwner[4] == 'o' && ticTacToeBoxOwner[8]=='o')
				{
					winner = 'o';
					JOptionPane.showMessageDialog(null, "Player Circle Wins!");
				}
				else if(ticTacToeBoxOwner[0] == 'x' && ticTacToeBoxOwner[4] == 'x' && ticTacToeBoxOwner[8]=='x')
				{
					winner = 'x';
					JOptionPane.showMessageDialog(null, "Player Cross Wins!");
				}
				
				if(ticTacToeBoxOwner[2] == 'o' && ticTacToeBoxOwner[4] == 'o' && ticTacToeBoxOwner[6]=='o')
				{
					winner = 'o';
					JOptionPane.showMessageDialog(null, "Player Circle Wins!");
				}
				else if(ticTacToeBoxOwner[2] == 'x' && ticTacToeBoxOwner[4] == 'x' && ticTacToeBoxOwner[6]=='x')
				{
					winner = 'x';
					JOptionPane.showMessageDialog(null, "Player Cross Wins!");
				}
				
				//Draw case
				if(ticTacToeBox[0] == true && ticTacToeBox[1] == true && ticTacToeBox[2] == true && ticTacToeBox[3] == true && ticTacToeBox[4] == true && ticTacToeBox[5] == true && ticTacToeBox[6] == true && ticTacToeBox[7] == true && ticTacToeBox[8] == true)
				{
					if(winner == 'n')
					{
						JOptionPane.showMessageDialog(null, "Draw!");
					}
				}


			}
			
			
		}
	}
	
	//Slider Listener class for Help menu
	//Windows Listener class for the Help menu
	//Action Listener class for Help menu Brush
	
	//Slider Listener class for Help menu
	private class SliderListenerClass implements ChangeListener
	{
		public void stateChanged(ChangeEvent e) 
		{
			penColour(rValue.getValue(),gValue.getValue(),bValue.getValue());
			currentColor.setBackground(getPenColour());
		}
	}
	
	//Windows Listener class for help menu
	private class HelpWindowListenerClass extends WindowAdapter
	{

		public void windowClosing(WindowEvent event)
		{
			//close MainFrame window
			event.getWindow().dispose();
		}
		
	}
	
	//Action Listener class for help menu brushes
	private class HelpMenuBrushListenerClass implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{	
			int size = Integer.parseInt(brushSizeValue[brushSize.getSelectedIndex()]);
			penWidth(size);
			
			currentBrush = brushTypeValue[brushType.getSelectedIndex()];
		}	
	}
	
	
	//Action Listener class for help menu color chooser button
	private class HelpMenuColorChooserListenerClass implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{	
			processCommand("colorchooser");
		}	
	}
	
	//Action Listener class for help menu Image file open button
	private class HelpMenuSelectImageListenerClass implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{	
			if(saveCounter == true)
			{
				imageSelector();
			}
			else
			{	
				//Non-Saved image case.
				
				int choice = JOptionPane.showConfirmDialog(null, "Current image/commands is not saved! Do you wish to Continue?");
				
				if(choice == JOptionPane.YES_OPTION)
				{
					imageSelector();
				}
			}
		}
		
		
		public void imageSelector()
		{
			try
			{	
				//reset turtle position.
				reset();
				penDown();
				
				//File chooser - opens on the current directory
				JFileChooser fileChosen = new JFileChooser(new File("."));
				
				//Adding optional file filter.
				FileFilter fFilter = new FileNameExtensionFilter("SupportedFileFilter","gif","jpeg","jpg","png");
				fileChosen.addChoosableFileFilter(fFilter);

				int userChoice = fileChosen.showOpenDialog(null);
	
				if(userChoice == JFileChooser.CANCEL_OPTION)
				{
					//case no file is chosen.
				}
				else if(userChoice == JFileChooser.APPROVE_OPTION)
				{
					//file is chosen
					File selectedImage = fileChosen.getSelectedFile();
					BufferedImage bufferedImageObj = ImageIO.read(selectedImage);
					setBufferedImage(bufferedImageObj);
					
					//save counter False to symbolize saved status.
					saveCounter = false;
				}
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null,"Error Loading the image.");
			}
		}
	}
	
	//Action Listener class for help menu mouse buttons
	private class HelpMenuMouseButtonListenerClass implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{	
			if(event.getSource() == mouseOn)
			{
				processCommand("mousemodeon");
			}
			else if(event.getSource() == mouseOff)
			{
				processCommand("mousemodeoff");
			}
		}	
	}
	
}
