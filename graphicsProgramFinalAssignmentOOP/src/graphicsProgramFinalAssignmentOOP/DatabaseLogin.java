package graphicsProgramFinalAssignmentOOP;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;


public class DatabaseLogin extends JPanel
{
	//User and Password validation.
	private boolean userTest = false;
	private boolean passwordTest = false;
	
	//Password and UserID
	private JTextField userID = new JTextField(25);
	private JTextField password = new JTextField(25);
	private JTextField email = new JTextField(25);
	
	//LoginMainFrame reference.
	private JFrame loginFrame;
	
	public DatabaseLogin()
	{
		
		//Main Panel Size
		this.setPreferredSize(new Dimension(280,220));
		
		//User ID
		JLabel titleID = new JLabel("UserID");
		titleID.setPreferredSize(new Dimension(240,30));
		
		//Adding User ID
		this.add(titleID);
		this.add(userID);
		
		//Adding Empty spacing
		this.add(Box.createRigidArea(new Dimension(280,5)));
		
		//User Password
		JLabel titlePassword = new JLabel("Password");
		titlePassword.setPreferredSize(new Dimension(240,30));
		
		//Adding User Password
		this.add(titlePassword);
		this.add(password);
		
		//Adding Empty spacing
		this.add(Box.createRigidArea(new Dimension(280,10)));
		
		//inner JPanel - buttons
		JPanel buttons = new JPanel();
		
		//Sign in Button
		JButton signIn = new JButton("Sign in");
		signIn.addActionListener(new SignInListener());
		signIn.setPreferredSize(new Dimension(120,30));
		
		//Sign up button
		JButton signUp = new JButton("Sign up");
		signUp.addActionListener(new SignUpListenerLoginScreen());
		signUp.setPreferredSize(new Dimension(120,30));
		
		//adding sign in and sign up buttons
		buttons.add(signIn);
		buttons.add(signUp);

		//Adding Sign Up and Sign In buttons panel
		this.add(buttons);
		
		//Creating new Frame for Database login
		loginFrame = new JFrame("Database Login");
		loginFrame.setLocation(640,300);
		loginFrame.setSize(350,600);
		loginFrame.setVisible(true);
		loginFrame.getContentPane().add(this);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.pack();
	}
	
	public void databaseSignUpWindow()
	{
		loginFrame.dispose();
		
		JPanel databaseSignUpWindow = new JPanel();
		
		//Main Panel Size
		databaseSignUpWindow.setPreferredSize(new Dimension(280,220));
		
		//User ID
		JLabel titleID = new JLabel("UserID");
		titleID.setPreferredSize(new Dimension(240,30));
		
		//Adding User ID
		databaseSignUpWindow.add(titleID);
		databaseSignUpWindow.add(userID);
		
		//Adding Empty spacing
		databaseSignUpWindow.add(Box.createRigidArea(new Dimension(280,5)));
		
		//User Password
		JLabel titlePassword = new JLabel("Password");
		titlePassword.setPreferredSize(new Dimension(240,30));
		
		//Adding User Password
		databaseSignUpWindow.add(titlePassword);
		databaseSignUpWindow.add(password);
		
		//Adding Empty spacing
		databaseSignUpWindow.add(Box.createRigidArea(new Dimension(280,5)));
		
		//Email 
		JLabel titleEmail = new JLabel("Email");
		titleEmail.setPreferredSize(new Dimension(240,30));
		
		//Adding Email
		databaseSignUpWindow.add(titleEmail);
		databaseSignUpWindow.add(email);
		
		//Adding Empty spacing
		databaseSignUpWindow.add(Box.createRigidArea(new Dimension(280,10)));
		
		//inner JPanel - buttons
		JPanel buttons = new JPanel();
		
		//Sign up button
		JButton signUp = new JButton("Sign up");
		signUp.addActionListener(new SignUpListenerSignUp());
		signUp.setPreferredSize(new Dimension(120,30));
		
		//cancel button
		JButton cancle = new JButton("Cancle");
		cancle.addActionListener(new SignUpListenerCancle());
		cancle.setPreferredSize(new Dimension(120,30));
		
		//adding sign up and cancel button
		buttons.add(signUp);
		buttons.add(cancle);
		

		//Adding Sign Up and Sign In buttons panel
		databaseSignUpWindow.add(buttons);
		
		//Creating new Frame for Database login
		loginFrame = new JFrame("Database Sign in");
		loginFrame.setLocation(640,300);
		loginFrame.setSize(300,330);
		loginFrame.setVisible(true);
		loginFrame.getContentPane().add(databaseSignUpWindow);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//loginFrame.pack();
	}
	
	
	public void databaseAccountChecker(String user,String pass)
	{
		
		try
		{
			//register driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//establishing connection
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/turtlegraphicdata","root","");
			
			//creating statement
			Statement statement = connection.createStatement();
			
			//result from the database
			ResultSet resultSet = statement.executeQuery("SELECT userid,password FROM userinfo");
			
			
			while(resultSet.next())
			{
				if(user.equals(resultSet.getString("userid")))
				{
					userTest = true;
					
					if(pass.equals(resultSet.getString("password")))
					{
						passwordTest = true;
					}
				}

			}
			
			//closing the connection
			connection.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	
	public void databaseAccountSignUp(String user,String pass, String email)
	{
		
		databaseAccountChecker(user,"");
		
		if(userTest == true)
		{
			//User Already exist
			JOptionPane.showMessageDialog(null, "UserID Already Exist!");
			
			//resets after each attempts.
			userTest = false;
			
		}
		else if("".equals(user))
		{
			//empty user name
			JOptionPane.showMessageDialog(null, "UserID Cannot be Empty!");
		}
		else if("".equals(pass))
		{
			//empty password given
			JOptionPane.showMessageDialog(null, "Password Cannot be Empty!");
		}
		else
		{
			try
			{
				//register driver
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				//establishing connection
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/turtlegraphicdata","root","");
				
				//creating statement
				Statement statement = connection.createStatement();
				
				//Inserting new User into the database
				statement.executeUpdate("INSERT INTO userinfo(userid,password,email) VALUES "+"("+"'"+user+"'"+","+"'"+pass+"'"+","+"'"+email+"'"+")"+";");
				
				//closing the connection
				connection.close();

				JOptionPane.showMessageDialog(null, "New Account Created!");
				
				//closes the sign in window
				loginFrame.dispose();
				
				//re-Opens Login window
				new DatabaseLogin();
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
		}
		
	}

	//Inner Listener class for Sign in.
	public class SignInListener implements ActionListener
	{
			
		public void actionPerformed(ActionEvent event)
		{
			String idEntered = userID.getText();
			String passwordEntered = password.getText();
			
			databaseAccountChecker(idEntered,passwordEntered);
			
			if(userTest == false)
			{
				//invalid user
				JOptionPane.showMessageDialog(null, "Invalid User!");
			}
			else if(userTest == true && passwordTest == false)
			{
				//valid user but wrong password
				JOptionPane.showMessageDialog(null, "Invalid Password!");
			}
			else if(userTest == true && passwordTest == true)
			{
				//successfully logged in
				JOptionPane.showMessageDialog(null, "Welcome back!");
				loginFrame.dispose();
				
				//calls turtle graphic if login is successful
				new GraphicsSystem();
				
			}
			
			//resets after each attempts.
			userTest = false;
			passwordTest = false;

		}
	}
	
	//Inner Listener class for Sign up (login screen).
	public class SignUpListenerLoginScreen implements ActionListener
	{
		
		public void actionPerformed(ActionEvent event)
		{
			databaseSignUpWindow();
		}
	}
	
	
	//Inner Listener class for Sign up window.
	//Sign up button listener.
	public class SignUpListenerSignUp implements ActionListener
	{
		
		public void actionPerformed(ActionEvent event)
		{
			String idEntered = userID.getText();
			String passwordEntered = password.getText();
			String emailEntered = email.getText();
					
			databaseAccountSignUp(idEntered, passwordEntered, emailEntered);
			
		}
	}
	
	//Cancel button listener.
	public class SignUpListenerCancle implements ActionListener
	{
		
		public void actionPerformed(ActionEvent event)
		{
			loginFrame.dispose();
			new DatabaseLogin();
			
		}
	}
	


}
