package Utiles;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import PlanEvac.*;
import madkit.kernel.Agent;
import turtlekit.kernel.TurtleKit;
import turtlekit.kernel.TurtleKit.Option;

public class window extends JFrame implements  ActionListener{
	private JPanel container = new JPanel();
	private JComboBox combo = new JComboBox();
	private JLabel label = new JLabel("Choix de la carte");
	private JLabel labelLeader = new JLabel("Choix du nombre de leader:");
	private JLabel labelFeu = new JLabel("Choix du nombre de départ de feu:");
	private JLabel labelLost = new JLabel("Choix du nombre de perdu:");
	private JLabel labelFollower = new JLabel("Choix du nombre de suiveur:");
	private JButton bouton = new JButton("Lancer la simulation");
	private JTextField nbLeader, nbFeu, nbLost,nbFollower ;
	private JLabel viewer;
	private JPanel panLeader,panLost,panFollow,PanbFeu,choise,south,top,choise2,masterChoise;
	private Image Logo;
	private JTabbedPane onglets;
	private JFileChooser personalisation; 
	
	//new JLabel(new ImageIcon(image));
	 
	//JPanel panel = new JPanel(new BorderLayout());
	//panel.add(viewer);


	public window(){
	
		// initialisation de la fenetre 
		this.setTitle("Evacuation Incendie");
		this.setSize(550, 550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setBackground(Color.WHITE);
		
		//container.setBackground(Color.white);
		container.setLayout(new BorderLayout());
		combo.setPreferredSize(new Dimension(100, 20)); // menu deroulant
		
		// creation du bouton
		south = new JPanel();
		bouton.addActionListener( this);
		south.add(bouton);
		container.add(south, BorderLayout.SOUTH);// ajout du bouton
		
		// importation de l'image qui servira de logo
		try {
			Logo = ImageIO.read(getClass().getResource("/Images/logo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		viewer = new JLabel(new ImageIcon(Logo));
		JPanel image = new JPanel();
		// ajout du choix du feu et des agents 
		choise = new JPanel();
		choise2 = new JPanel(); 
		masterChoise = new JPanel();
		initLeader();
		initFollower();
		initLost();
		initFeu();
		initPlan();// ajout de la liste de choix de plans
		
       
		choise.add(PanbFeu, BorderLayout.SOUTH);
		choise.add(panLeader, BorderLayout.NORTH);
		choise.setBackground(Color.white);
		choise2.add(panLost, BorderLayout.EAST);
		choise2.add(panFollow, BorderLayout.WEST);
		choise2.setBackground(Color.white);
		choise.setPreferredSize(new Dimension(240,130));
		choise2.setPreferredSize(new Dimension(240,130));
		masterChoise.add(choise, BorderLayout.SOUTH);
		masterChoise.add(choise2,-1);
		masterChoise.setBackground(Color.white);
		image.add(top,BorderLayout.NORTH);
		image.add(masterChoise, BorderLayout.NORTH);
		image.add(viewer,-1);
		image.setBackground(Color.white);
		
		//creation d'onglets. onglet selection standart et onlget selection avancé 
		onglets = new JTabbedPane(SwingConstants.TOP);
         
         //on créé l'onglet 1 selection standart qui va contenir tout les Jpanel avec le logo
         onglets.addTab("selection standart", image);
         personalisation = new JFileChooser();
         
         // création de l'onglet 2 selection personnalisée
     onglets.addTab("selection avancé", personalisation);
          
        // onglets.setOpaque(true);
         
		//container.add(top, BorderLayout.NORTH);
        container.setBackground(Color.white);
		container.add(onglets);
		this.getContentPane().add(container);
		this.setVisible(true);  
	}

	@SuppressWarnings("unchecked")
	public void initPlan() {
		String[] tab = {"plan.png", "plan2.jpg", "Option 3", "Option 4"};
		combo = new JComboBox(tab);
		combo.setPreferredSize(new Dimension(100, 20));
		combo.setForeground(Color.blue);
		top = new JPanel();
		top.add(label);
		top.add(combo);
		top.setBackground(Color.white);
	}
	public void initFeu(){
		// le nombre de feu
		PanbFeu = new JPanel();
	    //PanbFeu.setBackground(Color.white);
		PanbFeu.setPreferredSize(new Dimension(220, 60));
		nbFeu = new JTextField("1");
		nbFeu.setPreferredSize(new Dimension(100, 25));
		PanbFeu.add(labelFeu);
		PanbFeu.add(nbFeu);

	}

	public void initLeader() {
		//Le nombre d'agent
		panLeader = new JPanel();
		//panLeader.setBackground(Color.white);
		panLeader.setPreferredSize(new Dimension(220, 60));
		nbLeader = new JTextField("10");
		nbLeader.setPreferredSize(new Dimension(100, 25));
		panLeader.add(labelLeader);
		panLeader.add(nbLeader);
	}
	public void initLost() {
		//Le nombre d'agent
		panLost = new JPanel();
		//panLost.setBackground(Color.white);
		panLost.setPreferredSize(new Dimension(220, 60));
		nbLost = new JTextField("10");
		nbLost.setPreferredSize(new Dimension(100, 25));
		panLost.add(labelLost);
		panLost.add(nbLost);
	}
	public void initFollower() {
		//Le nombre d'agent
		panFollow = new JPanel();
		//panFollow.setBackground(Color.white);
		panFollow.setPreferredSize(new Dimension(220, 60));
		nbFollower = new JTextField("10");
		nbFollower.setPreferredSize(new Dimension(100, 25));
		panFollow.add(labelFollower);
		panFollow.add(nbFollower);
	}
/**
 * 
 * @return this String reprensant a map 
 */
	public String Getplan(){
		return combo.getSelectedItem().toString();
	}
	public String GetNbLeader(){
		return ","+nbLeader.getText();
	}
	public String GetNbFeu(){
		return ","+nbFeu.getText();
	}
	public String GetNbLost(){
		return ","+nbLost.getText();
	}
	public String GetNbFollower(){
		return ","+nbFollower.getText();
	}

public void actionPerformed(ActionEvent arg0) {
		new TurtleKit(
				Option.turtles.toString(),Panic.class.getName()+GetNbFollower()
				+";"+Leader.class.getName()+GetNbLeader()
				+";"+Lost.class.getName()+GetNbLost()
				+ ";"+Feu.class.getName()+GetNbFeu()
				,Option.viewers.toString(),MyViewer.class.getName()				
				//,Option.startSimu.toString()
				,"--plan",Getplan()
				,"--pheroName","feu"
				,Option.environment.toString(),PlanEvacuation.class.getName()
				);
}
		
	public static void main(String[] args) {
		new window();
	}
  
}

  

  

