package controleur;

import model.Analyseur;
import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by E155251B on 23/02/17.
 */

public class CtrlModifCap implements ActionListener {

    private JButton modifier;
    private View v;
    private Analyseur analyseur;

    public CtrlModifCap(JButton modif, View vu, Analyseur a) {
        v=vu;
        modifier=modif;
        analyseur=a;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource()).equals(modifier)){
            analyseur.modifierCaptureName(v.getCapture(), v.getNouveauNom());

            ArrayList<String> ar = analyseur.getCapturesNames();

            String[] tab = new String[ar.size()];
            tab = ar.toArray(tab);

            v.majData(tab);
        }
    }
}
