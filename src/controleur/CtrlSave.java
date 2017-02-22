package controleur;

import model.Analyseur;
import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by E155251B on 22/02/17.
 */

public class CtrlSave implements ActionListener {

    private JMenuItem save;
    private View v;
    private Analyseur analyseur;
	/*private CarnetAdresse c;*/


    public CtrlSave(JMenuItem save, View vu, Analyseur a) {
        v=vu;
        this.save=save;
        analyseur=a;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource()).equals(save)){
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                //System.out.println("You chose to open this file: " + chooser.getSelectedFile());

                try {
                    copyFileUsingStream(new File(analyseur.getPath()), chooser.getSelectedFile());
                    analyseur.setFile(chooser.getSelectedFile().getPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }



            ArrayList<String> ar = analyseur.getCapturesNames();

            String[] tab = new String[ar.size()];
            tab = ar.toArray(tab);

            v.majData(tab);
        }
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
