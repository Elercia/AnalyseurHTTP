package controleur;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import view.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class Control_ouvrir implements ActionListener {

    private JMenuItem ouvrir;
    private Vue v;
	/*private CarnetAdresse c;*/


    public Control_ouvrir(JMenuItem ouvrir, Vue vu) {
        v=vu;
        this.ouvrir=ouvrir;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource()).equals(ouvrir)){
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "*.csv", "csv");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getName());
            }
        }
    }
}
