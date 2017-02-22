package controleur;

import model.Analyseur;
import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by E155251B on 22/02/17.
 */
public class CtrlGestBD implements ActionListener{
    private JButton supprimer;
    private View v;
    private Analyseur analyseur;

    public CtrlGestBD(JButton sp, View vu, Analyseur a) {
        v=vu;
        supprimer=sp;
        analyseur=a;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource()).equals(supprimer)){
            analyseur.supprimerCapture(v.getCapture());

            ArrayList<String> ar = analyseur.getCapturesNames();

            String[] tab = new String[ar.size()];
            tab = ar.toArray(tab);

            v.majData(tab);
        }
    }
}

