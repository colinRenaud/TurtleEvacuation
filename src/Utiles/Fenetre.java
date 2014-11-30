package Utiles;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import turtlekit.kernel.TurtleKit.Option;

public class Fenetre extends JFrame implements MouseListener {
	private JPanel container = new JPanel();
	private JComboBox combo = new JComboBox();
	private JLabel label = new JLabel("Choix de la carte");
	private JLabel label2 = new JLabel("Choix du nombre d'agent:");
	private JLabel label3 = new JLabel("Choix du nombre de feu:");
	private JButton bouton = new JButton("Lancer la simulation");
	private JTextField nbAgent, nbFeu ;
	public boolean souris = false;
	private JPanel Panbagent,PanbFeu,Choix,south,top;


	public Fenetre(){
		// initialisation de la fenetre 
		this.setTitle("Evacuation Incendie");
		this.setSize(500, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());
		combo.setPreferredSize(new Dimension(100, 20)); // menu deroulant
		
		// creation du bouton
		south = new JPanel();
		bouton.addMouseListener(this);
		south.add(bouton);
		
		// ajout du bouton
		container.add(south, BorderLayout.SOUTH);
		
		// ajout du choix du feu et des agents 
		Choix = new JPanel();
		initAgent();
		initFeu();
		Choix.add(PanbFeu, BorderLayout.SOUTH);
		Choix.add(Panbagent, BorderLayout.NORTH);
		container.add(Choix, BorderLayout.CENTER);

		// ajout de la liste de choix de plans
         initPlan();
         
		container.add(top, BorderLayout.NORTH);
		this.setContentPane(container);
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
	}
	public void initFeu(){
		// le nombre de feu
		PanbFeu = new JPanel();
		PanbFeu.setBackground(Color.white);
		PanbFeu.setPreferredSize(new Dimension(220, 60));
		nbFeu = new JTextField();
		nbFeu.setPreferredSize(new Dimension(100, 25));
		PanbFeu.add(label3);
		PanbFeu.add(nbFeu);

	}

	public void initAgent() {
		//Le nombre d'agent
		Panbagent = new JPanel();
		Panbagent.setBackground(Color.white);
		Panbagent.setPreferredSize(new Dimension(220, 60));
		nbAgent = new JTextField();
		nbAgent.setPreferredSize(new Dimension(100, 25));
		Panbagent.add(label2);
		Panbagent.add(nbAgent);
	}

	public String Getplan(){
		return combo.getSelectedItem().toString();
	}
	public String GetNbAgent(){
		return ","+nbAgent.getText();
	}
	public String GetNbFeu(){
		return ","+nbFeu.getText();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		souris = true;
		System.out.println("Vous venez de cliquer sur la fen�tre graphique");    

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}


}