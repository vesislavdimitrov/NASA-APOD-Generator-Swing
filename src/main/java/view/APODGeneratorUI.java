package view;

import control.APODDataParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class APODGeneratorUI {

    public void generateAPODFrame() {
        JFrame loadingFrame = createLoadingFrame();
        APODDataParser apodDataParser = new APODDataParser();

        new Thread(() -> {
            try {
                BufferedImage image = ImageIO.read(new URL(apodDataParser.parseImageToJson()));

                JLabel imageLabel = createImageLabel(image);
                JScrollPane descriptionLabel = createDescriptionLabel(apodDataParser.parseDescriptionToJson());

                JPanel mainPanel = createPanel(imageLabel, descriptionLabel);

                JFrame frame = createFrame(apodDataParser.parseTitleToJson(), mainPanel);
                loadingFrame.dispose();
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private JFrame createLoadingFrame() {
        JLabel loadingLabel = new JLabel("Loading astronomy picture of the day...");
        loadingLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        loadingLabel.setForeground(Color.WHITE);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setForeground(Color.ORANGE);
        progressBar.setBackground(Color.BLACK);
        progressBar.setIndeterminate(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.add(loadingLabel, BorderLayout.PAGE_START);
        panel.add(progressBar, BorderLayout.CENTER);

        JFrame loadingFrame = new JFrame();
        loadingFrame.add(panel);
        loadingFrame.setSize(new Dimension(400, 100));
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setResizable(false);
        loadingFrame.setVisible(true);
        return loadingFrame;
    }

    private JLabel createImageLabel(BufferedImage image) {
        return new JLabel(new ImageIcon(image));
    }

    private JScrollPane createDescriptionLabel(String description) {
        JLabel descriptionLabel = new JLabel("<html><body style='width: 600px; color: white'>" + description + "</body></html>");
        descriptionLabel.setVerticalAlignment(JLabel.TOP);
        descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
        descriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        descriptionLabel.setOpaque(true);
        descriptionLabel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(descriptionLabel);
        scrollPane.setPreferredSize(new Dimension(700, 200));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(1, 5, 5, 5));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBackground(Color.BLACK);
        return scrollPane;
    }

    private JPanel createPanel(JLabel imageLabel, JScrollPane descriptionLabel) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(descriptionLabel, BorderLayout.PAGE_END);
        return mainPanel;
    }

    private JFrame createFrame(String title, JPanel mainPanel) {
        DateFormat dateFormat = new java.text.SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        String formattedDate = dateFormat.format(new Date());
        title = title + " (" + formattedDate + ")";

        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        return frame;
    }
}
