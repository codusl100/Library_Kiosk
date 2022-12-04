package library;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class extension extends JFrame {
	Database db = new Database(); // db 연동
	// 이동 버튼
	private JButton btn1 = new JButton("대출");
	private JButton btn2 = new JButton("예약");
	private JButton btn3 = new JButton("연장");
	private JButton btn_home = new JButton("홈");
	
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JLabel id = new JLabel("학번");
	private JLabel name = new JLabel("이름");
	private JTextField idtf = new JTextField(18);
	private JTextField nametf = new JTextField(18);
	
	private JLabel lb = new JLabel("연장가능 횟수를 확인하고 표를 클릭하여 예약하세요.");
	private JButton exten = new JButton("연장");
	private JTextArea db_connect = new JTextArea("db연동");

	Connection con = null;
	Statement stmt = null;

	public extension() {

		con = null;
		stmt = null;
		String url = "jdbc:mysql://124.56.138.3:30/db2019110340"; // dbstudy 스키마
		String username = "2019110340";
		String password = "test1234!@#$QWER"; // 본인이 설정한 root 계정의 비밀번호를 입력하면 된다.

		setTitle("연장");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(null);

		// 학번, 이름 표시하는 패널 설정
		panel1.add(id);
		panel1.add(idtf);
		panel2.add(name);
		panel2.add(nametf);
		panel1.setSize(560, 61);
		panel1.setLocation(59, 115);
		panel2.setSize(560, 61);
		panel2.setLocation(59, 170);

		// 연장 방법 알려주는 라벨 설정
		lb.setHorizontalAlignment(NORMAL); // 라벨 가운데 정렬
		lb.setLocation(40, 250);
		lb.setSize(600, 34);

		Font plain_font = new Font("NanumSquare", Font.PLAIN, 25); // 폰트 객체 생성
		id.setFont(plain_font);
		name.setFont(plain_font);
		idtf.setFont(plain_font);
		nametf.setFont(plain_font);
		lb.setFont(plain_font);

		// 테이블 넣을 패널 위치와 크기 설정
		JPanel jPanel_Book = new JPanel();
		jPanel_Book.setSize(690, 300);
		jPanel_Book.setLocation(0, 300);

		// 테이블
		// 1. 테이블 정보정의
		String header[] = { "대여ID", "도서명", "대출일자", "반납가능일자", "연장횟수" };
		String contents[][] = {};

		// 2. jtable 클래스의 객체 생성
		DefaultTableModel Book_model = new DefaultTableModel(contents, header);
		JTable Book_table = new JTable(Book_model);
		Book_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane Book_scrollPane = new JScrollPane(Book_table);
		Book_scrollPane.setPreferredSize(new Dimension(527, 300));
		// 3.
		jPanel_Book.add(Book_scrollPane);
		c.add(jPanel_Book);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();

			String temp = "select 회원아이디, 이름 FROM 학생 where loginYN = 1;";
			ResultSet r = stmt.executeQuery(temp);
			r.next();
			String userId = r.getString("회원아이디");
			String userName = r.getString("이름");
			idtf.setText(userId);
			nametf.setText(userName);
			System.out.println(userId);

			String sql = "select A.대여ID, B.도서명, A.대출일자, A.반납가능일자, A.연장횟수 From 대여 AS A JOIN 도서 AS B ON A.도서ID = B.`도서 ID`where A.회원학번 = "
					+ userId;
			ResultSet result = stmt.executeQuery(sql);

			while (result.next()) {

				Object data[] = { result.getString("A.대여ID"), result.getString("B.도서명"), result.getString("A.대출일자"),
						result.getString("A.반납가능일자"), result.getString("A.연장횟수") };

				Book_model.addRow(data);
			}

			// for (int i = 0; i < data.length; i++) {}

		} catch (Exception e) {
			System.out.println("MySQL 서버 연동 실패 > " + e.toString());
		}

		// 버튼 설정
		btn1.setLocation(1, 0);
		btn2.setLocation(233, 0);
		btn3.setLocation(466, 0);
		btn1.setSize(233, 100);
		btn2.setSize(233, 100);
		btn3.setSize(233, 100);
		btn1.setBackground(Color.white);
		btn2.setBackground(Color.white);
		btn3.setBackground(new Color(255, 204, 153));

		Font big_font = new Font("NanumSquare", Font.BOLD, 30); // 폰트 객체 생성
		btn1.setFont(big_font);
		btn2.setFont(big_font);
		btn3.setFont(big_font);

		// 연장버튼 생성 및 설정
		exten.setSize(210, 100);
		exten.setLocation(128, 670);
		exten.setForeground(new Color(255, 255, 255));
		exten.setBackground(new Color(255, 153, 051));
		exten.setFont(big_font);

		// 홈버튼 생성 및 설정
		ImageIcon homeIcon = new ImageIcon("images/home.png");
		Image homeimg = homeIcon.getImage();
		homeimg = homeimg.getScaledInstance(52, 52, java.awt.Image.SCALE_SMOOTH);
		homeIcon = new ImageIcon(homeimg);

		JButton btn_home = new JButton("Home", homeIcon);

		btn_home.setBackground(Color.gray);
		btn_home.setFont(big_font); // 폰트 설정
		btn_home.setForeground(new Color(255, 255, 255)); // 텍스트 색 설정
		btn_home.setSize(210, 100);
		btn_home.setLocation(363, 670);

		// 리스너 달기
		btn1.addActionListener(new inActionListener());
		btn2.addActionListener(new inActionListener());
		btn3.addActionListener(new inActionListener());
		btn_home.addActionListener(new inActionListener());
		exten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value1 = 0;
				int value2 = 0;
				if (Book_table.getSelectedRow() == -1) {
					return;
				} else {
					int row = Book_table.getSelectedRow();

					System.out.println((Book_table.getValueAt(row, 4)).getClass().getTypeName());
					System.out.println((Book_table.getValueAt(row, 0)).getClass().getTypeName());
					value1 = Integer.valueOf((String) Book_table.getValueAt(row, 4));
					value2 = Integer.valueOf((String) Book_table.getValueAt(row, 0));
					// System.out.println(value1);
					// System.out.println(value2);
					if (value1 > 0) {
						try {

							String sql = "update 대여 set `연장횟수` = `연장횟수` - 1, 반납가능일자 = DATE_FORMAT(DATE_ADD(반납가능일자 , INTERVAL 14 DAY), '%Y-%m-%d') where `대여ID`="
									+ value2;
							System.out.println(sql);
							int result = stmt.executeUpdate(sql);
							System.out.println(result);
							JOptionPane.showMessageDialog(null, "연장되었습니다.", "오류 메세지", JOptionPane.INFORMATION_MESSAGE);
							new extension();
							dispose();

						} catch (Exception ex) {
							System.out.println("MySQL 서버 연동 실패 > " + ex.toString());
						}
					} else {
						JOptionPane.showMessageDialog(null, "연장횟수를 초과하였습니다.", "오류 메세지", JOptionPane.ERROR_MESSAGE);
					}
				}

			}

		});
		
		// 컨테이너에 컴포넌트들 추가
		c.add(exten);
		c.add(btn1);
		c.add(btn2);
		c.add(btn3);
		c.add(panel1);
		c.add(panel2);
		c.add(lb);

		c.add(btn_home);
		c.add(db_connect);

		setSize(700, 800);
		setVisible(true);
	}

	// 이동 버튼
	class inActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			if (b.getText().equals("대출")) {
				new borrow();
				dispose();
			} else if (b.getText().equals("예약")) {
				new reservation();
				setVisible(false);
			} else if (b.getText().equals("연장")) {
				new extension();
				setVisible(false);
			} else if (b.getText().equals("Home")) {
				new library_main();
				// db.logout();
				dispose();
			}
		}
	}
}
