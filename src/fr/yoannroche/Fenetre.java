package fr.yoannroche;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

public class Fenetre extends JFrame {

	private JToolBar tool = new JToolBar();
	private JButton button = new JButton(" Load ");
	private JButton refresh = new JButton(" Refresh ");
	private JPanel ecran = new JPanel();
	private String requete = "SELECT * FROM classe";
	private JTextArea texte = new JTextArea(requete);
	private RefreshListener rListener = new RefreshListener();
	private JLabel temps = new JLabel ();
	
	Font police = new Font ("arial", 12,12);
	
public Fenetre () {
	setSize(800,550);
	setTitle("TP Requeteur");
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(false);
	
	
	initToolbar();
	initText();
	initEcran(requete);
	setVisible(true);
	
}

private void initToolbar() {
	tool.setPreferredSize(new Dimension(800,30));
	tool.addSeparator();
	tool.add(button);
	button.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent event){
	        initEcran(texte.getText());
	      }
	    });
	tool.addSeparator();
	tool.addSeparator();
	tool.add(refresh);
	refresh.addActionListener(rListener);
	tool.setBorder(BorderFactory.createLineBorder(Color.black));
	tool.setFloatable(false);
	tool.setBackground(Color.getHSBColor(0.547f, 0.77f, 0.60f));
	refresh.setBorder(BorderFactory.createLineBorder(Color.black));
	refresh.setBackground(Color.LIGHT_GRAY);
	button.setBorder(BorderFactory.createLineBorder(Color.black));
	button.setBackground(Color.LIGHT_GRAY);
	getContentPane().add(tool, BorderLayout.NORTH);
}
private void initText() {
	getContentPane().add(texte,BorderLayout.CENTER);
	texte.setFont(police);
	texte.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(5, 7, 5, 7, Color.DARK_GRAY),BorderFactory.createMatteBorder(2, 4, 2, 4, Color.getHSBColor(0.512f, 0.20f, 0.45f))));
	texte.setBackground(Color.getHSBColor(0.519f, 0.06f, 0.99f));

	
}
private void initEcran(String query) {
	getContentPane().add(ecran,BorderLayout.SOUTH);
	ecran.setPreferredSize(new Dimension(500,350));
	ecran.setBorder(BorderFactory.createLineBorder(Color.black));
	ecran.setBackground(Color.getHSBColor(0.536f, 0.16f, 0.83f));
		
	 try {
		 long start = System.currentTimeMillis();
		Statement conn = Connect.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		 ResultSet res = conn.executeQuery(query);

	      ResultSetMetaData meta = res.getMetaData();

	      Object[] column = new Object[meta.getColumnCount()];

	      for(int i = 1 ; i <= meta.getColumnCount(); i++)
	        column[i-1] = meta.getColumnName(i);

	      res.last();
	      int rowCount = res.getRow();
	      Object[][] data = new Object[res.getRow()][meta.getColumnCount()];

	      res.beforeFirst();
	      int j = 1;

	      while(res.next()){
	        for(int i = 1 ; i <= meta.getColumnCount(); i++)
	          data[j-1][i-1] = res.getObject(i);
					
	        j++;
	      }                 
	      res.close();
	      conn.close();
	      
	      long totalTime = System.currentTimeMillis() - start;

	      ecran.removeAll();
	      JTable table = new JTable(data, column);
	      ecran.add(new JScrollPane(table), BorderLayout.CENTER);
	      table.setPreferredScrollableViewportSize(new Dimension(750,280));
	      table.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	      table.setBackground(Color.getHSBColor(0.512f, 0.10f, 0.95f));
	      ecran.add(temps,BorderLayout.SOUTH);
	      JPanel cadre = new JPanel();
	      ecran.add(cadre,BorderLayout.SOUTH);
	      cadre.setPreferredSize(new Dimension(430,28));
	      cadre.add(temps);
	      cadre.setBackground(Color.getHSBColor(0.124f, 0.45f, 0.95f));
	      cadre.setBorder(BorderFactory.createLineBorder(Color.black));
	      temps.setText("La requête à été exécuter en " + totalTime + " ms et a retourné " + rowCount + " ligne(s)");
	      ecran.revalidate();
				
	    } catch (SQLException e) {	
	      ecran.removeAll();
	      ecran.add(new JScrollPane(new JTable()), BorderLayout.CENTER);
	      ecran.revalidate();
	      
	      JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
	    }	
	  

	
}

class RefreshListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		texte.setText("SELECT * FROM");
	}
	
	
}
}
