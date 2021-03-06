package controleur;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import model.Analyseur;
import view.View;
//import ihm.modele.*;

public class CtrlActualisation implements ActionListener {

	private JButton actu;
	private View v;
	private Analyseur analyseur;
	/*private CarnetAdresse c;*/


	public CtrlActualisation(JButton actu, View vu, Analyseur a) {
		v=vu;
    	this.actu=actu;
		this.analyseur=a;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(actu)){
			try{
				ArrayList<String> ar = analyseur.getCapturesNames();
				for (String s : ar) {
					System.out.println(s);
				}




				HashMap<String, HashMap<String, Object>> data = this.analyseur.getData(v.getData());

				HashMap<String, Object> methodeUsed = data.get("methodeUsed");
				HashMap<String, Object> nbPagesCharged = data.get("nbPagesCharged");
				HashMap<String, Object> poidPageCharged = data.get("poidPagesCharged");
				HashMap<String, Object> nbCookiesCreated = data.get("nbCookiesCreated");
				HashMap<String, Object> tempSite = data.get("temps");

				this.tempSite(tempSite);
				this.nbPageCharged(nbPagesCharged);
				this.pageLeMostCharge(nbPagesCharged);
				this.nbCookiesCreated(nbCookiesCreated);
				this.poidPageCharged(poidPageCharged, nbPagesCharged);
				this.methodeUsed(methodeUsed);

				//JOptionPane.showMessageDialog(null,"Donées actualisées !");
			}catch(Exception exeption){
				JOptionPane.showMessageDialog(null,"Recupération des données impossible","ERREUR",JOptionPane.ERROR_MESSAGE);
				exeption.printStackTrace();
			}
		}
	}

	public void methodeUsed(HashMap<String, Object> methodeUsed){
		v.clearJPMethode();

		JPanel JPmetode = new JPanel();
		JPmetode.setLayout(new BoxLayout(JPmetode, BoxLayout.Y_AXIS));
		JLabel txtmethode = new JLabel("<html>Methode HTTP la plus utilisée : " + methodeUsed.toString()+"<br><br>");

		JPmetode.add(txtmethode);
		JPmetode.add(createDemoPanel(methodeUsed, "Methode HTTP la plus utilisée"));
		v.addJPmethode(JPmetode);
	}

	public void poidPageCharged(HashMap<String, Object> poidPageCharged, HashMap<String, Object> nbPagesCharged){
		String expr="<ul>";
		String exprde= "<ul>";
		HashMap<String, Object> vincent = new HashMap<String, Object>();
		for (String s : poidPageCharged.keySet()) {
			if(nbPagesCharged.containsKey(s)){
				int val =(int)poidPageCharged.get(s)/(int)nbPagesCharged.get(s);
				vincent.put(s, val);
				expr = expr + "<li>" + s +" : "+ val + "</li>";
				exprde = exprde +"<li>"+ s+ " : "+ poidPageCharged.get(s)+" </li> ";
			}
		}
		expr=expr+"</ul>";
		exprde=exprde+"</ul>";

		if(!expr.isEmpty()){
			v.clearJPweigtChargedPages();

			JPanel JPpoidPageCharged = new JPanel();
			JPpoidPageCharged.setLayout(new BoxLayout(JPpoidPageCharged, BoxLayout.Y_AXIS));

			JLabel txtpoidPageCharged = new JLabel("<html> Poid des pages chargées : "+expr + " <br><br><br> ");


			JPpoidPageCharged.add(txtpoidPageCharged);
			JPpoidPageCharged.add(createDemoPanel(vincent, "Poid des pages chargées"));

			JLabel txtpoidPageChargedde = new JLabel("<html><br><br><br> Poid TOTAL des pages chargées : "+ exprde);

			JPpoidPageCharged.add(txtpoidPageChargedde);

			v.addJPweigtChargedPages(JPpoidPageCharged);
		}
	}

	public void nbCookiesCreated(HashMap<String, Object> nbCookiesCreated){
		String expr="<ul>";
		for (String s : nbCookiesCreated.keySet()) {
			if(nbCookiesCreated.containsKey(s)){
				expr = expr + "<li>" + s +" : "+ nbCookiesCreated.get(s) + "</li>";
			}
		}

		expr = expr + "</ul>";
		JLabel txtnbCookiesCreated = new JLabel("<html> Liste des cookie cree : "+ expr);
		JPanel g = new JPanel();
		g.add(txtnbCookiesCreated);
		v.clearJPCookie();
		v.addJPCookie(g);
	}

	public void pageLeMostCharge(HashMap<String, Object> nbPagesCharged){
		long max=0;
		String strMax ="";
		for (String s : nbPagesCharged.keySet()) {
			if(max < (int)nbPagesCharged.get(s)){
				max = (int)nbPagesCharged.get(s);
				strMax= s+" -> " + nbPagesCharged.get(s)+" ";
			}
		}
		if(!strMax.isEmpty()){
			v.clearJPMostViewedSite();

			JPanel JPmostViewedSite = new JPanel();
			JPmostViewedSite.setLayout(new BoxLayout(JPmostViewedSite, BoxLayout.Y_AXIS));
			JLabel txtmostViewedSite = new JLabel("<html>Le site le plus utlisé : "+strMax +"<br><br>");

			JPmostViewedSite.add(txtmostViewedSite);
			JPmostViewedSite.add(createDemoPanel(nbPagesCharged, "site le plus utlisé"));
			v.addJPmostViewedSite(JPmostViewedSite);
		}
	}

	public void nbPageCharged(HashMap<String, Object> nbPagesCharged){
		String expr="";
		long res=0;
		for (String s : nbPagesCharged.keySet()) {
			res+=(int)nbPagesCharged.get(s);
		}
		expr="->"+res;
		v.maj_pagecharge(expr);
	}


	public void tempSite(HashMap<String, Object> temps){
		String str = new String("<ul>");
		for (String s : temps.keySet()) {
			str=str+"<li>"+ s+" -> " + temps.get(s)+" </li>";
		}
		str=str+"</ul>";
		v.cleartempsSite();

		JPanel JPtempsSite = new JPanel();
		JPtempsSite.setLayout(new BoxLayout(JPtempsSite, BoxLayout.Y_AXIS));
		JLabel txttempsSite = new JLabel("<html> Temps par site : "+ str);

		JPtempsSite.add(txttempsSite);
		JPtempsSite.add(createDemoPanel(temps, "Temps par site"));
		v.addtempsSite(JPtempsSite);
	}





	private static PieDataset createDataset(HashMap<String, Object> data) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Map.Entry<String,Object> entry : data.entrySet()) {
			dataset.setValue( entry.getKey() , (int)entry.getValue());
		}
		return dataset;
	}

	private static JFreeChart createChart(PieDataset dataset , String title) {
		JFreeChart chart = ChartFactory.createPieChart(
				title,  // chart title
				dataset,        // data
				true,           // include legend
				true,
				false);

		return chart;
	}

	public static JPanel createDemoPanel(HashMap<String, Object> data, String title) {
		JFreeChart chart = createChart(createDataset(data), title );
		return new ChartPanel( chart );
	}
}
