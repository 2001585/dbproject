import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    public MainFrame() {
        setTitle("전북모여!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // 아이디 입력 필드
        JLabel usernameLabel = new JLabel("아이디:");
        usernameField = new JTextField();
        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.add(usernameLabel, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        // 비밀번호 입력 필드
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordField = new JPasswordField();
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        // 로그인 버튼
        loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateUser(username, password)) {
                    // 로그인 성공
                    openOriginalFrame();
                } else {
                    // 로그인 실패
                    JOptionPane.showMessageDialog(MainFrame.this, "아이디 또는 비밀번호가 일치하지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 회원가입 버튼
        signupButton = new JButton("회원가입");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationFrame();
            }
        });

        // 컴포넌트 추가
        add(usernamePanel);
        add(passwordPanel);
        add(loginButton);
        add(signupButton);

        // 컴포넌트 크기 조정
        usernamePanel.setPreferredSize(new Dimension(400, 50)); // 아이디 입력 필드 크기 조정
        passwordPanel.setPreferredSize(new Dimension(400, 50)); // 비밀번호 입력 필드 크기 조정
        loginButton.setPreferredSize(new Dimension(400, 50)); // 로그인 버튼 크기 조정
        signupButton.setPreferredSize(new Dimension(400, 50)); // 회원가입 버튼 크기 조정

        pack();
        setLocationRelativeTo(null);
    }

    private boolean authenticateUser(String username, String password) {
        // Oracle 데이터베이스 연동 및 사용자 정보 조회
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "system";
        String pass = "1234";
        String driver = "oracle.jdbc.OracleDriver";

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            Class.forName(driver);

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM USERS WHERE id = '" + username + "' AND password = '" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);

            return resultSet.next(); // 결과가 있으면 인증 성공, 없으면 인증 실패
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // 예외 처리 코드를 작성합니다.
        }

        return false; // 예외 발생 시 인증 실패
    }

    private void openOriginalFrame() {
        // 로그인 성공 후 원래 창 열기
        OriginalFrame originalFrame = new OriginalFrame();
        originalFrame.setVisible(true);
        dispose();
    }

    private void openRegistrationFrame() {
        // 회원가입 창 열기
        RegistrationFrame registrationFrame = new RegistrationFrame();
        registrationFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
