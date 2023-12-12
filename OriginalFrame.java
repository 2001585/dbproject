import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OriginalFrame extends JFrame {
    private JRadioButton regionCheckbox1;
    private JRadioButton regionCheckbox2;
    private JRadioButton regionCheckbox3;
    private JRadioButton typeRadio1;
    private JRadioButton typeRadio2;
    private JButton recommendButton;

    public OriginalFrame() {
        setTitle("전북모여!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        // 지역 선택 라디오버튼 생성
        regionCheckbox1 = new JRadioButton("전주");
        regionCheckbox2 = new JRadioButton("익산");
        regionCheckbox3 = new JRadioButton("군산");

        ButtonGroup regionGroup = new ButtonGroup();
        regionGroup.add(regionCheckbox1);
        regionGroup.add(regionCheckbox2);
        regionGroup.add(regionCheckbox3);

        // 타입 선택 라디오버튼 생성
        typeRadio1 = new JRadioButton("실내");
        typeRadio2 = new JRadioButton("실외");

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(typeRadio1);
        typeGroup.add(typeRadio2);

        // 놀거리 추천 받기 버튼 생성
        recommendButton = new JButton("놀거리 추천 받기");
        recommendButton.setPreferredSize(new Dimension(200, 100));
        recommendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRegion = "";
                if (regionCheckbox1.isSelected()) {
                    selectedRegion = "전주";
                } else if (regionCheckbox2.isSelected()) {
                    selectedRegion = "익산";
                } else if (regionCheckbox3.isSelected()) {
                    selectedRegion = "군산";
                }

                String selectedType = "";
                if (typeRadio1.isSelected()) {
                    selectedType = "실내";
                } else if (typeRadio2.isSelected()) {
                    selectedType = "실외";
                }

                // 오라클 연동하여 놀거리 추천
                String url = "jdbc:oracle:thin:@localhost:1521:xe"; // 오라클 데이터베이스 URL
                String dbUsername = "system"; // 데이터베이스 사용자명
                String dbPassword = "1234"; // 데이터베이스 비밀번호

                Connection connection = null;
                PreparedStatement statement = null;
                ResultSet resultSet = null;

                try {
                    // JDBC 드라이버 로드
                    Class.forName("oracle.jdbc.driver.OracleDriver");

                    // 데이터베이스 연결
                    connection = DriverManager.getConnection(url, dbUsername, dbPassword);

                    // SQL 문 실행
                    String sql = "SELECT place_name, location, place_runtime FROM place WHERE location = ? AND type = ?";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, selectedRegion);
                    statement.setString(2, selectedType);
                    resultSet = statement.executeQuery();

                    // 결과 처리
                    while (resultSet.next()) {
                        String placeName = resultSet.getString("place_name");
                        String location = resultSet.getString("location");
                        String placeRuntime = resultSet.getString("place_runtime");
                        // ...
                    }

                    // 결과 처리 이후 ResultFrame 열기
                    ResultFrame resultFrame = new ResultFrame(selectedRegion, selectedType);
                    resultFrame.setVisible(true);
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    // 자원 해제
                    try {
                        if (resultSet != null)
                            resultSet.close();
                        if (statement != null)
                            statement.close();
                        if (connection != null)
                            connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 컴포넌트 추가
        add(regionCheckbox1);
        add(regionCheckbox2);
        add(regionCheckbox3);
        add(typeRadio1);
        add(typeRadio2);
        add(recommendButton);

        pack();
        setLocationRelativeTo(null);
    }
}
