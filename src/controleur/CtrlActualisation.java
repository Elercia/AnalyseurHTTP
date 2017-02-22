package controleur;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.JButton;

import model.Analyseur;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import view.*;
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
				//TODO S2LECTION DE LA BD
				HashMap<String, HashMap<String, Object>> data = this.analyseur.getData(v.getData());

				HashMap<String, Object> methodeUsed = data.get("methodeUsed");
				HashMap<String, Object> nbPagesCharged = data.get("nbPagesCharged");
				HashMap<String, Object> poidPageCharged = data.get("poidPagesCharged");
				HashMap<String, Object> nbCookiesCreated = data.get("nbCookiesCreated");

				this.nbPageCharged(nbPagesCharged);
				this.pageLeMostCharge(nbPagesCharged);
				this.nbCookiesCreated(nbCookiesCreated);
				this.poidPageCharged(poidPageCharged, nbPagesCharged);
				this.methodeUsed(methodeUsed);

				JOptionPane.showMessageDialog(null,"Donées actualisées !");
			}catch(Exception exeption){
				JOptionPane.showMessageDialog(null,"Recupération des données impossible","ERREUR",JOptionPane.ERROR_MESSAGE);
				exeption.printStackTrace();
			}
		}
	}

	public void methodeUsed(HashMap<String, Object> methodeUsed){
		v.clearJPMethode();

		JPanel JPmetode = new JPanel();
		JLabel txtmethode = new JLabel("Methode HTTP la plus utilisée : ");
		JLabel methode = new JLabel(methodeUsed.toString());

		JPmetode.add(txtmethode);
		JPmetode.add(methode);
		JPmetode.add(createDemoPanel(methodeUsed));
		v.addJPmethode(JPmetode);
	}

	public void poidPageCharged(HashMap<String, Object> poidPageCharged, HashMap<String, Object> nbPagesCharged){
		String expr="";
		String exprde= "";
		HashMap<String, Object> vincent = new HashMap<String, Object>();
		for (String s : poidPageCharged.keySet()) {
			if(nbPagesCharged.containsKey(s)){
				int val =(int)poidPageCharged.get(s)/(int)nbPagesCharged.get(s);
				vincent.put(s, val);
				expr = expr + s +" : "+ val + ", ";
				exprde = exprde + s+ " : "+ poidPageCharged.get(s);
			}
		}
		if(!expr.isEmpty()){
			v.clearJPweigtChargedPages();

			JPanel JPpoidPageCharged = new JPanel();
			JPpoidPageCharged.setLayout(new BorderLayout());//l,c
			JLabel txtpoidPageCharged = new JLabel("Poid des pages chargées : ");
			JLabel exppoidPageCharged = new JLabel(expr);

			JPpoidPageCharged.add(txtpoidPageCharged, BorderLayout.NORTH);
			JPpoidPageCharged.add(exppoidPageCharged, BorderLayout.NORTH);
			JPpoidPageCharged.add(createDemoPanel(vincent), BorderLayout.CENTER);

			JLabel txtpoidPageChargedde = new JLabel("Poid TOTAL des pages chargées : ");
			JLabel exppoidPageChargedde = new JLabel(exprde);

			JPpoidPageCharged.add(txtpoidPageChargedde, BorderLayout.SOUTH);
			JPpoidPageCharged.add(exppoidPageChargedde, BorderLayout.SOUTH);

			v.addJPweigtChargedPages(JPpoidPageCharged);
		}
	}

	public void nbCookiesCreated(HashMap<String, Object> nbCookiesCreated){
		v.maj_cookie(nbCookiesCreated.toString());
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
			JLabel txtmostViewedSite = new JLabel("Le site le plus utlisé : ");
			JLabel mostViewedSite = new JLabel(strMax);

			JPmostViewedSite.add(txtmostViewedSite);
			JPmostViewedSite.add(mostViewedSite);
			JPmostViewedSite.add(createDemoPanel(nbPagesCharged));
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




	private static PieDataset createDataset(HashMap<String, Object> data) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Map.Entry<String,Object> entry : data.entrySet()) {
			dataset.setValue( entry.getKey() , (int)entry.getValue());
		}
		return dataset;
	}

	private static JFreeChart createChart(PieDataset dataset ) {
		JFreeChart chart = ChartFactory.createPieChart(
				"site",  // chart title
				dataset,        // data
				true,           // include legend
				true,
				false);

		return chart;
	}

	public static JPanel createDemoPanel(HashMap<String, Object> data) {
		JFreeChart chart = createChart(createDataset(data) );
		return new ChartPanel( chart );
	}
}
