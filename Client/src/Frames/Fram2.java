package Frames;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import Ressources.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Fram2 extends JFrame {
    private JPanel panel1;
    private JButton convoButton;
    private JButton filterButton;
    private JButton temperatureButton;
    private JButton humilityButton;
    private JButton chooseImageButton;
    private JLabel welcome;
    private JLabel image;
    private JPanel chartt;
    private JTabbedPane tabbedPane1;
    private JButton Halfton;
    public static float[][] kerenl = null;
    public static boolean connect = false;
    public static BufferedImage imageTr = null;
    private static final long serialVersionUID = 1L;
    public static String[][] data = null;

    //For connecting to MainServer using socket
    public static int MainServer_port;
    public static String MainServer_host;

    public static CountDownLatch latch;


    public Fram2(String name) {
        setContentPane(panel1);
        setSize(900, 650);
        setTitle("Welcome");
        // welcome.setText("Welcome " + name + " To your Home");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setVisible(true);
        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(panel1);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        imageTr = ImageIO.read(fileChooser.getSelectedFile());
                        ImageIcon originalImage = new ImageIcon(ImageIO.read(fileChooser.getSelectedFile()));
                        int newWidth = 200;
                        int newHeight = 200;
                        originalImage.setImage(originalImage.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
                        image.setIcon(originalImage);

                        //! Enable buttons
                        convoButton.setEnabled(true);
                        filterButton.setEnabled(true);
                        Halfton.setEnabled(true);


                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        temperatureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sensorToServer("T_");
                LocalDate D = LocalDate.now();
                JFreeChart chart = ChartFactory.createXYLineChart("La Temperateur  " + D, "Time", "°C", createDataset());

                // Create a panel to hold the chart


                // Add the chart to the panel
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(500, 400));

                //ResultImage.add(chartPanel);
                // panel.add(chartPanel);
                chartt.removeAll();
                chartt.revalidate();
                chartt.repaint();
                // Add the panel to the frame
                chartt.add(chartPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, true));
                // chartt.add(jButton, new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
                setSize(801, 501);
                //new com.intellij.uiDesigner.core.GridConstraints(1, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
            }

        });
        humilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sensorToServer("H_");
                LocalDate D = LocalDate.now();

                JFreeChart chart = ChartFactory.createXYLineChart("Humidity " + D, "Time", "%", createDataset());

                // Create a panel to hold the chart
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                // Add the chart to the panel
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(500, 400));

                //ResultImage.add(chartPanel);
                // panel.add(chartPanel);
                chartt.removeAll();
                chartt.revalidate();
                chartt.repaint();
                // Add the panel to the frame
                chartt.add(chartPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, true));
                // chartt.add(jButton, new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
                setSize(801, 501);
                //new com.intellij.uiDesigner.core.GridConstraints(1, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
            }

        });
        convoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                // Disable the button to prevent multiple clicks
                //  convoButton.setEnabled(false);

                // Start the background task
                //SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    /*@Override
                    protected Void doInBackground() throws Exception {*/
                /*latch = new CountDownLatch(1);*/
                JPanel panel = new JPanel();
                JRadioButton radio1 = new JRadioButton("3x3");
                JRadioButton radio2 = new JRadioButton("5x5");
                JRadioButton radio3 = new JRadioButton("7x7");
                ButtonGroup group = new ButtonGroup();
                group.add(radio1);
                group.add(radio2);
                group.add(radio3);
                panel.add(radio1);
                panel.add(radio2);
                panel.add(radio3);
                int result = JOptionPane.showOptionDialog(null, panel, "Select Size of Frames.Fram2.Matrix",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (result == JOptionPane.YES_OPTION) {
                    if (radio1.isSelected()) {
                        new Thread(new Matrix(3, chartt)).start();
                    } else if (radio2.isSelected()) {
                        new Thread(new Matrix(5, chartt)).start();
                    } else if (radio3.isSelected()) {
                        new Thread(new Matrix(7, chartt)).start();
                    }


                } else if (result == JOptionPane.NO_OPTION) {
                    // User selected radio2
                }

            }


        });
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Fram2.SendServer("SMART BLUR FILTER", chartt);
            }
        });

        Halfton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Fram2.SendServer("COLOR HALFTONE FILTER", chartt);
            }
        });
        tabbedPane1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane1.getSelectedIndex();
                String tabTitle = tabbedPane1.getTitleAt(selectedIndex);
                if (tabTitle.compareTo("Sencor") == 0) {
                    image.setIcon(null);
                    image.removeAll();
                    image.revalidate();
                    image.repaint();
                    // chartt.setIcon(null);
                    chartt.removeAll();
                    chartt.revalidate();
                    chartt.repaint();
                }
            }
        });

    }


    public static void SendServer(String taskName, JPanel myPanel) {

        try {
            System.out.println("Adresse de MainServer");
            System.out.println("=> " + MainServer_host + ":" + MainServer_port);
            Socket socket = new Socket(MainServer_host, MainServer_port);
            System.out.println("connected");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            String state = (String) input.readObject();
            int[][] img = bufferedImageToIntArray(imageTr);
            if (state.compareToIgnoreCase("active") == 0) {
                output.writeObject(taskName);

                if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                    DataConvolution dataConvolution = new DataConvolution(img, kerenl);
                    output.writeObject(dataConvolution);
                }
                if (taskName.compareToIgnoreCase("COLOR HALFTONE FILTER") == 0) {
                    DataResult dataNoise = new DataResult(img);
                    output.writeObject(dataNoise);
                }

                if (taskName.compareToIgnoreCase("SMART BLUR FILTER") == 0) {
                    DataResult dataGray = new DataResult(img);
                    output.writeObject(dataGray);
                }
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                DataResult pixels = (DataResult) in.readObject();
                String mes = pixels.statue;
                if (mes != null) {
                    JOptionPane.showMessageDialog(null, mes, "ERROR", JOptionPane.ERROR_MESSAGE);

                }
                BufferedImage filteredImage = intArrayToBufferedImage(pixels.image);
                //filteredImageLabel.setIcon(new ImageIcon(filteredImage));


                //Show the filtred image
                myPanel.removeAll();
                myPanel.revalidate();
                myPanel.repaint();
                JLabel result = new JLabel();
                // Add the panel to the frame

                result.setIcon(new ImageIcon(new ImageIcon(filteredImage).getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT)));
                // filteredImage.setImage(filteredImage.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));

                myPanel.add(result, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, true));
                // chartt.add(jButton, new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
                //myPanel.setSize(801, 501);

            }
            socket.close();


            if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                //! Task in CONVOLUTION FILTER

            } else if (taskName.compareToIgnoreCase("GRAY FILTER") == 0) {
                //! Task in GRAY FILTER

            } else {
                System.out.println("Error: invalid taskName");
            }
        } catch (Exception e) {

        }
    }

    public static void sensorToServer(String taskName) {

        try {
            System.out.println("Adresse de MainServer");
            System.out.println("=> " + MainServer_host + ":" + MainServer_port);
            Socket socket = new Socket(MainServer_host, MainServer_port);
            System.out.println("connected");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            String state = (String) input.readObject();
            System.out.println(state);
            if (state.compareToIgnoreCase("active") == 0) {
                output.writeObject(taskName);
                SensorData value = (SensorData) input.readObject();

                data = value.getDonnees();
            } else {
                System.out.println("");

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static int[][] bufferedImageToIntArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x] = image.getRGB(x, y);
            }
        }
        return result;
    }

    public static BufferedImage intArrayToBufferedImage(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.setRGB(x, y, pixels[y][x]);
            }
        }
        return result;
    }


    public static XYDataset createDataset() {

        // Create the data for the chart
        XYSeries series = new XYSeries("Data ");
        // TimeSeries series = new TimeSeries("Monthly Sales");
        for (int i = 0; i < data.length; i++) {
            String t = data[i][0].split(";")[1];
            float d = (Float.parseFloat(data[i][1]) + getRandomNumberInRange(-4, 3));
            series.add(timeToSec(t), d);

        }
        // series.add(0, 1);
        // series.add(1, 2);
        // series.add(2, 3);
        // series.add(3, 4);
        // series.add(4, 5);

        // Add the data to the dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // TimeSeriesCollection dataset = new TimeSeriesCollection();
        // dataset.addSeries(series);
        return dataset;
    }


    public static int timeToSec(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = format.parse(timeString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            int seconds = cal.get(Calendar.SECOND);
            int totalSeconds = hours * 3600 + minutes * 60 + seconds;
            return totalSeconds;
        } catch (Exception e) {
            System.out.println("Unable to parse the time: " + timeString);

            return 0;
        }
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), 3, 3));
        panel1.setBackground(new Color(-14737343));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-13811899));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setBackground(new Color(-9906529));
        label1.setIcon(new ImageIcon(getClass().getResource("/Icons/Logo_ThreadStorm_200.png")));
        label1.setText("");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setBackground(new Color(-14737343));
        panel3.setEnabled(true);
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        image = new JLabel();
        image.setText("");
        panel3.add(image, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setBackground(new Color(-14737343));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 200), null, 0, false));
        chartt = new JPanel();
        chartt.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        chartt.setBackground(new Color(-14737343));
        chartt.setForeground(new Color(-13811899));
        panel4.add(chartt, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 400), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(500, -1), null, 0, false));
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setBackground(new Color(-1));
        tabbedPane1.setEnabled(true);
        Font tabbedPane1Font = this.$$$getFont$$$("Consolas", Font.BOLD, 14, tabbedPane1.getFont());
        if (tabbedPane1Font != null) tabbedPane1.setFont(tabbedPane1Font);
        tabbedPane1.setForeground(new Color(-16777216));
        tabbedPane1.setTabLayoutPolicy(0);
        panel1.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.setBackground(new Color(-7673198));
        panel5.setForeground(new Color(-14737343));
        tabbedPane1.addTab("Filters", panel5);
        convoButton = new JButton();
        convoButton.setBackground(new Color(-1516675));
        convoButton.setEnabled(false);
        convoButton.setForeground(new Color(-16777216));
        convoButton.setText("Convolution");
        convoButton.putClientProperty("hideActionText", Boolean.FALSE);
        convoButton.putClientProperty("html.disable", Boolean.FALSE);
        panel5.add(convoButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filterButton = new JButton();
        filterButton.setBackground(new Color(-1516675));
        filterButton.setEnabled(false);
        filterButton.setText("SMART BLUR");
        panel5.add(filterButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chooseImageButton = new JButton();
        chooseImageButton.setBackground(new Color(-1516675));
        chooseImageButton.setEnabled(true);
        chooseImageButton.setForeground(new Color(-16777216));
        chooseImageButton.setText("Choose image");
        chooseImageButton.putClientProperty("hideActionText", Boolean.FALSE);
        chooseImageButton.putClientProperty("html.disable", Boolean.FALSE);
        panel5.add(chooseImageButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Halfton = new JButton();
        Halfton.setBackground(new Color(-1516675));
        Halfton.setEnabled(false);
        Halfton.setText("COLOR HALFTONE ");
        panel5.add(Halfton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.setBackground(new Color(-7673198));
        panel6.setForeground(new Color(-7673198));
        tabbedPane1.addTab("Sencor", panel6);
        temperatureButton = new JButton();
        temperatureButton.setBackground(new Color(-1516675));
        temperatureButton.setEnabled(true);
        temperatureButton.setText("Temperature");
        panel6.add(temperatureButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        humilityButton = new JButton();
        humilityButton.setBackground(new Color(-1516675));
        humilityButton.setEnabled(true);
        humilityButton.setText("Humidity");
        panel6.add(humilityButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    private class GridLayoutManager implements LayoutManager {
        public GridLayoutManager(int i, int i1, Insets insets, int i2, int i3) {

        }

        @Override
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return null;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {

        }
    }

    public static class Matrix implements Runnable {
        public static int size = 3;
        public static JTable finalTable;
        public JPanel myPanel;

        public Matrix(int n, JPanel myPanel) {
            size = n;
            this.myPanel = myPanel;
        }

        private void main1() {
            JTable table = null;
            JFrame frame = new JFrame();
            frame.setLayout(null);
            JLabel text = new JLabel("Enter a matrix");
            Font font = text.getFont();  // get the existing font
            font = font.deriveFont(16f);  // create a new font with size 24
            text.setFont(font);  // set the new font
            text.setBounds(20, 0, 140, 30);
            JButton btn = new JButton("Enter");

            //  frame.setSize(400,400);
            if (size == 3) {
                // create a JTable
                table = new JTable(new DefaultTableModel(new Object[][]{{"1", "1", "1"}, {"1", "1", "1"}, {"1", "1", "1"}}, new Object[]{"", "", ""}));
                frame.setSize(160, 150);
                table.setBounds(5, 33, 150, 100);
                btn.setBounds(30, 90, 90, 20);
            } else if (size == 5) {
                // create a JTable
                table = new JTable(new DefaultTableModel(new Object[][]{{"1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1"}}, new Object[]{"", "", "", "", ""}));
                frame.setSize(205, 180);
                table.setBounds(3, 33, 190, 110);
                btn.setBounds(50, 120, 90, 20);
            } else if (size == 7) {
                // create a JTable
                frame.setSize(300, 250);
                table = new JTable(new DefaultTableModel(new Object[][]{{"1", "1", "1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1", "1", "1"}, {"1", "1", "1", "1", "1", "1", "1"}}, new Object[]{"", "", "", "", "", "", ""}));
                table.setBounds(3, 33, 280, 120);
                btn.setBounds(90, 170, 90, 20);
            }
            JTable finalTable = table;

            //System.out.println(table.getValueAt(table.getSelectedRow(),table.getEditingColumn()));
            // create the custom cell renderer
            class TextFieldTableCellRenderer extends JTextField implements TableCellRenderer {
                public TextFieldTableCellRenderer() {
                    setBorder(null);
                }

                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    if (isSelected) {
                        setForeground(table.getSelectionForeground());
                        setBackground(table.getSelectionBackground());
                    } else {
                        setForeground(table.getForeground());
                        setBackground(table.getBackground());
                    }
                    setText((value == null) ? "" : value.toString());
                    //finalTable =table;
                    return this;
                }
            }

            // set the custom cell renderer for the desired column
            TableColumn column = table.getColumnModel().getColumn(1);
            column.setCellRenderer(new TextFieldTableCellRenderer());
            // add the JTable to a JFrame

            // JTable finalTable1 = table;
            finalTable.getModel().addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    finalTable.clearSelection();
                }
            });
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    int row = finalTable.getModel().getRowCount();
                    float[][] Data = new float[row][row];
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < row; j++) {
                            try {
                                Data[j][i] = Float.parseFloat((String) finalTable.getValueAt(i, j));
                            } catch (Exception error) {
                                JOptionPane.showMessageDialog(frame, finalTable.getValueAt(i, j) + "is not a number!!");
                                break;

                            }
                        }
                    }
                    kerenl = Data;
                    connect = true;
                    //Fram2.latch.countDown();
                    frame.dispose();
                    SendServer("CONVOLUTION FILTER", myPanel);

                }

            });
            frame.add(text);
            text.setForeground(Color.WHITE);
            frame.add(btn);
            // JScrollPane tab=new JScrollPane(table);
            frame.add(table);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(Color.black);
            //frame.pack();
            //frame.setSize(200,150);
            frame.setVisible(true);


        }

        @Override
        public void run() {
            this.main1();

        }


    }
}
