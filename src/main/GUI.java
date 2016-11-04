package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by mgmalana on 04/11/2016.
 */
public class GUI {
    private JComboBox <ImageRetrieve.imageRetrieval> comboBox1;
    private JButton button1;
    private JButton generateResultsButton;
    private JPanel panel;
    private JLabel filepath;
    private Main main;
    private File file;

    public GUI() {
        this.main = new Main();

        comboBox1.setModel(new DefaultComboBoxModel<ImageRetrieve.imageRetrieval>(ImageRetrieve.imageRetrieval.values()));
        comboBox1.updateUI();
        generateResultsButton.setEnabled(false);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file = main.initFileChooser();
                if(file != null){
                    filepath.setText(file.getName());
                    generateResultsButton.setEnabled(true);
                }

            }
        });
        generateResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.doRetrieval(file, comboBox1.getItemAt(comboBox1.getSelectedIndex()));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
