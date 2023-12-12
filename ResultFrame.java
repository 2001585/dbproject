import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

public class ResultFrame extends JFrame {
    private JList<String> resultList;
    private DefaultListModel<String> listModel;
    private JButton closeButton;

    public ResultFrame(String selectedRegion, String selectedTheme) {
        setTitle("놀거리 추천 결과");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 선택된 지역과 테마를 기반으로 놀거리 추천 결과를 생성
        ArrayList<Recommendation> recommendations = generateRecommendations(selectedRegion, selectedTheme);

        listModel = new DefaultListModel<>();
        for (Recommendation recommendation : recommendations) {
            listModel.addElement(recommendation.getPlaceName());
        }

        resultList = new JList<>(listModel);
        resultList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = resultList.getSelectedIndex();
                    if (index != -1) {
                        Recommendation selectedRecommendation = recommendations.get(index);
                        showSelectedResult(selectedRecommendation);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        add(scrollPane, BorderLayout.CENTER);

        closeButton = new JButton("닫기");
        closeButton.setPreferredSize(new Dimension(100, 50));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private ArrayList<Recommendation> generateRecommendations(String selectedRegion, String selectedTheme) {
        ArrayList<Recommendation> recommendations = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1234")) {
            String query = "SELECT p.id, p.place_name, p.location, p.place_runtime, d.text, d.url " +
                    "FROM place p JOIN detail d ON p.id = d.id " +
                    "WHERE p.location = ? AND p.type = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedRegion);
            statement.setString(2, selectedTheme);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String placeName = resultSet.getString("place_name");
                String location = resultSet.getString("location");
                String placeRuntime = resultSet.getString("place_runtime");
                String text = resultSet.getString("text");
                String url = resultSet.getString("url");
                Recommendation recommendation = new Recommendation(id, placeName, location, placeRuntime, text, url);
                recommendations.add(recommendation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("디비 연동 실패");
        }

        return recommendations;
    }

    private void showSelectedResult(Recommendation recommendation) {
        ResultDetailFrame detailFrame = new ResultDetailFrame(recommendation);
        detailFrame.setVisible(true);
    }

    static class Recommendation {
        private int id;
        private String placeName;
        private String location;
        private String placeRuntime;
        private String text;
        private String url;

        public Recommendation(int id, String placeName, String location, String placeRuntime, String text, String url) {
            this.id = id;
            this.placeName = placeName;
            this.location = location;
            this.placeRuntime = placeRuntime;
            this.text = text;
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public String getPlaceName() {
            return placeName;
        }

        public String getLocation() {
            return location;
        }

        public String getPlaceRuntime() {
            return placeRuntime;
        }

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }
    }
}
