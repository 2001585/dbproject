import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class RegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JTextField nameField; // 이름 필드
    private JPasswordField passwordField1; // 패스워드 필드 1
    private JPasswordField passwordField2; // 패스워드 필드 2
    private JButton registerButton;

    public RegistrationFrame() {
        setTitle("회원가입");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        // 아이디 입력 필드
        JLabel usernameLabel = new JLabel("아이디:");
        usernameField = new JTextField();
        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.add(usernameLabel, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        // 이름 입력 필드
        JLabel nameLabel = new JLabel("이름:");
        nameField = new JTextField();
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(nameField, BorderLayout.CENTER);

        // 패스워드 입력 필드 1
        JLabel passwordLabel1 = new JLabel("비밀번호:");
        passwordField1 = new JPasswordField();
        JPanel passwordPanel1 = new JPanel(new BorderLayout());
        passwordPanel1.add(passwordLabel1, BorderLayout.WEST);
        passwordPanel1.add(passwordField1, BorderLayout.CENTER);

        // 패스워드 입력 필드 2
        JLabel passwordLabel2 = new JLabel("비밀번호 확인:");
        passwordField2 = new JPasswordField();
        JPanel passwordPanel2 = new JPanel(new BorderLayout());
        passwordPanel2.add(passwordLabel2, BorderLayout.WEST);
        passwordPanel2.add(passwordField2, BorderLayout.CENTER);

        // 회원가입 버튼
        registerButton = new JButton("가입하기");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String name = nameField.getText();
                String password1 = new String(passwordField1.getPassword());
                String password2 = new String(passwordField2.getPassword());

                // 패스워드 일치 여부 확인
                if (!password1.equals(password2)) {
                    JOptionPane.showMessageDialog(RegistrationFrame.this, "패스워드가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 회원가입 정보 처리
                // 예를 들어, DB에 저장하거나 다른 작업 수행
                insertUserToTable(username, name, password1);

                JOptionPane.showMessageDialog(RegistrationFrame.this, "회원가입이 완료되었습니다.", "회원가입 성공", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        // 컴포넌트 추가
        add(usernamePanel);
        add(namePanel);
        add(passwordPanel1);
        add(passwordPanel2);
        add(registerButton);

        // 컴포넌트 크기 조정
        usernamePanel.setPreferredSize(new Dimension(400, 50)); // 아이디 입력 필드 크기 조정
        namePanel.setPreferredSize(new Dimension(400, 50)); // 이름 입력 필드 크기 조정
        passwordPanel1.setPreferredSize(new Dimension(400, 50)); // 패스워드 입력 필드 1 크기 조정
        passwordPanel2.setPreferredSize(new Dimension(400, 50)); // 패스워드 입력 필드 2 크기 조정
        registerButton.setPreferredSize(new Dimension(400, 50)); // 회원가입 버튼 크기 조정

        pack();
        setLocationRelativeTo(null);
    }

    private void insertUserToTable(String username, String name, String password) {
        // 데이터베이스 연결 정보 설정
        String url = "jdbc:oracle:thin:@localhost:1521:xe"; // 데이터베이스 URL
        String user = "system"; // 데이터베이스 사용자명
        String pass = "1234"; // 데이터베이스 비밀번호

        // 데이터베이스 연결
        try (Connection conn = DriverManager.getConnection(url, user, pass)){
            // INSERT 쿼리 실행
            String sql = "INSERT INTO users (id, name, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, name);
                stmt.setString(3, password);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
