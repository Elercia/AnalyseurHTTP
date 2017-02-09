package controleur;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Analyseur;
import view.*;

public class CtrlOpen implements ActionListener {

    private JMenuItem ouvrir;
    private View v;
    private Analyseur analyseur;
	/*private CarnetAdresse c;*/


    public CtrlOpen(JMenuItem ouvrir, View vu, Analyseur a) {
        v=vu;
        this.ouvrir=ouvrir;
        analyseur=a;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource()).equals(ouvrir)){
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "*.json", "json");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                analyseur.setFile(chooser.getSelectedFile().getPath());
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getName());
            }
        }
    }
}
