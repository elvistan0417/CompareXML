/*
 * Copyright (c) 2006 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Stylizer extends JFrame {
    // Global value so it can be ref'd by the tree-adapter
    static Document document;

    private JTextField file1Field, file2Field, file3Field;
    private JCheckBox generateReportCheckBox;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextArea progressTextArea;

    public Stylizer() {
        setTitle("XML File Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null); // Centers the window on the screen

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel fileSelectionPanel = createFileSelectionPanel();
        JPanel progressPanel = createProgressPanel();

        cardPanel.add(fileSelectionPanel, "fileSelection");
        cardPanel.add(progressPanel, "progress");

        add(cardPanel);

        setVisible(true);
    }

    private JPanel createFileSelectionPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 1));

        // File 1 selection
        JPanel file1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel file1Label = new JLabel("Select XML File 1:");
        file1Field = new JTextField(20);
        JButton file1Button = new JButton("Browse...");
        file1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile(file1Field);
            }
        });
        file1Panel.add(file1Label);
        file1Panel.add(file1Field);
        file1Panel.add(file1Button);

        // File 2 selection
        JPanel file2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel file2Label = new JLabel("Select XML File 2:");
        file2Field = new JTextField(20);
        JButton file2Button = new JButton("Browse...");
        file2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile(file2Field);
            }
        });
        file2Panel.add(file2Label);
        file2Panel.add(file2Field);
        file2Panel.add(file2Button);


        // File 3 selection
        JPanel file3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel file3Label = new JLabel("Select XML File 3:");
        file3Field = new JTextField(20);
        JButton file3Button = new JButton("Browse...");
        file3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile(file3Field);
            }
        });
        file3Panel.add(file3Label);
        file3Panel.add(file3Field);
        file3Panel.add(file3Button);

        // Checkbox for report generation
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        generateReportCheckBox = new JCheckBox("Generate Report");

        checkboxPanel.add(generateReportCheckBox);

        // Submit button
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file1Path = file1Field.getText();
                String file2Path = file2Field.getText();
                String file3Path = file3Field.getText();
                ArrayList<String> stringList = new ArrayList<>();

                // Check if each string is not empty and add to the ArrayList
                if (!file1Path.isEmpty()) {
                    stringList.add(file1Path);
                }
                if (!file2Path.isEmpty()) {
                    stringList.add(file2Path);
                }
                if (!file3Path.isEmpty()) {
                    stringList.add(file3Path);
                }
                boolean generateReport = generateReportCheckBox.isSelected();

                // Pass the selected file paths and checkbox value to another function
                submitAction(stringList, generateReport);

            }
        });
        submitPanel.add(submitButton);

        mainPanel.add(file1Panel);
        mainPanel.add(file2Panel);
        mainPanel.add(file3Panel);
        mainPanel.add(checkboxPanel);
        mainPanel.add(submitPanel);

        return mainPanel;
    }

    private JPanel createProgressPanel() {
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressTextArea = new JTextArea(10, 30);
        progressTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(progressTextArea);
        progressPanel.add(scrollPane, BorderLayout.CENTER);
        return progressPanel;
    }

    private void selectFile(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().toLowerCase().endsWith(".xml")) {
                textField.setText(selectedFile.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "Please select an XML file.", "Invalid File Type", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void submitAction(ArrayList<String> filePathList, boolean generateReport) {
        if(filePathList.size() < 2) {
            JOptionPane.showMessageDialog(this, "Please select at least 2 XML file.", "Need 2 xml at least", JOptionPane.ERROR_MESSAGE);
        }
        else {
            // Hide file selection panel
            cardLayout.show(cardPanel, "progress");

            // Simulating processing delay
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Simulate progress
                    updateProgress("Processing...\n");

                    try {
                        for(String file: filePathList) {
                            updateProgress("Sorting XML files.. + " + file + ".\n");
                        }
                        ArrayList<String> sortXMLs = processXMLFiles(filePathList);
                        updateProgress("Sorting XML files...Done\n");
                        Thread.sleep(1000); // wait processing
                        updateProgress("Generating report...\n");
                        Thread.sleep(1000); // wait processing
                        generateCompareResult(sortXMLs, generateReport);
                        updateProgress("Process completed.\n");
                        Thread.sleep(1000); // wait exit processing
                        System.exit(0);

                    } catch (Exception e) {
                        e.printStackTrace();
                        updateProgress("Process terminated.\n");
                        updateProgress("Error Message: \n");
                        updateProgress(e.getMessage());
                    }
                }
            }).start();
        }
    }

    private void updateProgress(String status) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressTextArea.append(status);
            }
        });
    }

    private static String removeFileExtension(String filename) {
        if (filename == null) {
            return null;
        }

        int extensionIndex = filename.lastIndexOf('.');
        if (extensionIndex == -1) {
            return filename; // No extension found
        } else {
            return filename.substring(0, extensionIndex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Stylizer();
            }
        });
    }

    public static ArrayList<String> processXMLFiles(ArrayList<String> fileList) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<String> list = new ArrayList<>();
        try {
            File stylesheet = new File("data/sort.xsl");
            for(String file: fileList) {
                File datafile = new File(file);
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(datafile);
                // Use a Transformer for output
                TransformerFactory tFactory = TransformerFactory.newInstance();
                StreamSource stylesource = new StreamSource(stylesheet);
                Transformer transformer = tFactory.newTransformer(stylesource);

                DOMSource source = new DOMSource(document);
                File directory = new File("./output");

                if (!directory.exists()) {
                    // If not, create it
                    boolean created = directory.mkdirs();

                    if (created) {
                        System.out.println("Directory created successfully.");
                    } else {
                        System.err.println("Failed to create directory.");
                        throw new IOException();
                    }
                }
                StreamResult result = new StreamResult(new File("output/sorted_" + datafile.getName()));
                transformer.transform(source, result);

                list.add("sorted_" + datafile.getName());
            }
            return list;
        } catch (Exception e) {
            throw e;
        }
    } // main

    public static void generateCompareResult(ArrayList xmls, boolean generateReport) throws Exception {
        try {
            String command = "";
            String outputFormat = "";
            String reportName = "Compare_";
            for (int i = 0; i < xmls.size(); i++) {
                outputFormat += " \"output\\" + xmls.get(i) + "\"";

                // Add space if not the last element
                if (i < xmls.size() - 1) {
                    outputFormat += "  ";
                }
                String fileExactName = removeFileExtension(xmls.get(i).toString());
                reportName += fileExactName + "_";
            }
            if(!generateReport){
                command = "WinMergeU.exe" + outputFormat;
            }
            else {
                String outputDir = "output";
                command = "WinMergeU.exe -noninteractive" + outputFormat + " -or \"output\\" + reportName + ".html\"";
            }
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.directory(new File(System.getProperty("user.dir"))); // Set working directory
            Process process = processBuilder.start();

            // Wait for the process to finish
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("WinMergeU.exe Command executed successfully.");
            } else {
                throw new Exception("WinMergeU.exe Command execution failed.");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
