import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ResultDetailFrame extends JFrame {
    private JTextArea resultTextArea;
    private JLabel imageLabel;

    public ResultDetailFrame(ResultFrame.Recommendation recommendation) {
        setTitle("결과 상세 정보");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        resultTextArea = new JTextArea();
        resultTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        resultTextArea.setEditable(false);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 400)); // 사진의 크기를 조정하려면 이 부분을 수정하세요.

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 텍스트 패널에 여백 추가
        textPanel.add(resultTextArea, BorderLayout.CENTER);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 사진 패널에 여백 추가
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(700, 500)); // 창의 크기를 조정하려면 이 부분을 수정하세요.
        add(scrollPane, BorderLayout.CENTER);

        loadDataFromRecommendation(recommendation);

        pack();
        setLocationRelativeTo(null);
    }

    private void loadDataFromRecommendation(ResultFrame.Recommendation recommendation) {
        String text = recommendation.getText();
        String imageUrl = recommendation.getUrl();

        resultTextArea.setText(text);

        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            ImageIcon imageIcon = new ImageIcon(image);
            imageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}