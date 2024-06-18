package de.kobich.tictactoe.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import de.kobich.tictactoe.FieldState;
import de.kobich.tictactoe.GameState;
import de.kobich.tictactoe.Player;
import de.kobich.tictactoe.PlayingStrength;
import de.kobich.tictactoe.TicTacToeImage;
import de.kobich.tictactoe.control.CalculationResult;
import de.kobich.tictactoe.control.Field;
import de.kobich.tictactoe.control.GameProperties;
import de.kobich.tictactoe.control.Line;
import de.kobich.tictactoe.control.TicTacToeController;
import de.kobich.tictactoe.control.TicTacToeStatistics;

/**
 * Main UI for TIC TAC TOE
 */
public class TicTacToeFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 313;
	private static final int HEIGHT = 372;

	private final TicTacToeStatistics m_statistics;
	private final TicTacToeController m_controller;
	private final Field[] m_fields;
	private GameProperties m_currentProperties;
	private GameWhoStarts m_whoStarts;
	private PlayingStrength m_strength;

	// for GUI
	private Container m_contentPane;
	private FieldLabel[] m_fieldLabels;
	private JMenuBar m_menuBar;
	private JMenuItem m_newGame;
	private JTextField m_statusField;

	private AboutDialog m_aboutDialog;
	private StatisticDialog m_statisticDialog;

	static {
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError("Error", "Look And Feel could not be set.");
		}
	}
	
	enum GameWhoStarts {
		COMPUTER_STARTS, USER_STARTS, ALTERNATE;
	}

	public static void startTicTacToe() {
		TicTacToeFrame t = new TicTacToeFrame();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t.setVisible(true);
	}

	private TicTacToeFrame() {
		setSize(WIDTH, HEIGHT);
		setTitle("Tic Tac Toe");
		setIconImage(TicTacToeImage.ICON.getImage());

		m_contentPane = getContentPane();
		m_contentPane.setLayout(new BorderLayout());
		m_fieldLabels = new FieldLabel[10];
		m_statusField = new JTextField();
		m_currentProperties = new GameProperties(Player.USER, PlayingStrength.HIGH);
		m_statistics = new TicTacToeStatistics();
		m_controller = new TicTacToeController();
		m_fields = m_controller.getFields();
		m_whoStarts = GameWhoStarts.ALTERNATE;
		m_strength = PlayingStrength.HIGH;

		makePitch();
		makeMenu();
		makeStatus();
		setStatus("You have the first turn");
		centreDialog();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new FieldKeyListener());
	}

	/**
	 * Processes user events
	 * @param index Index des Feldes, auf das der Anwender setzt
	 */
	private synchronized void processEvent(int index) {
		// start new game if current game is finished
		if (!m_currentProperties.isOpen()) {
			newGame();
			return;
		}
		
		// field is free and the game is still running
		if (m_fields[index].getStatus().isFree() && m_currentProperties.isOpen()) {
			// user's turn
			setFieldState(index, FieldState.USER);
			m_currentProperties.incrementMove();

			// computer's turn
			CalculationResult comResult = m_controller.getComputerField(m_currentProperties);
			switch (comResult.getWhoWins()) {
				case OPEN:
					// the game is not yet over
					if (comResult.hasIndex()) {
						m_currentProperties.incrementMove();
						setFieldState(comResult.getIndex(), FieldState.COMPUTER);
						setStatus("It's your turn");
					}
					else {
						UIUtils.showError("Internal Error", "Error while calculating the next field.");
					}
					break;
				case DRAW:
					setWhoWins(GameState.DRAW);
					
					if (comResult.hasIndex()) {
						m_currentProperties.incrementMove();
						setFieldState(comResult.getIndex(), FieldState.COMPUTER);
					}
					setStatus("Draw");
					break;
				case COMPUTER_WINS:
					setWhoWins(GameState.COMPUTER_WINS);
					
					if (comResult.hasIndex()) {
						m_currentProperties.incrementMove();
						setAnimatedLine(comResult.getWinningLine(), FieldState.COMPUTER_WINS);
						setStatus("Computer has won");
					}
					else {
						UIUtils.showError("Internal Error", "Error while calculating the next field.");
					}
					break;
				case USER_WINS:
					// user winds (rather unlikely)
					setWhoWins(GameState.USER_WINS);
					setAnimatedLine(comResult.getWinningLine(), FieldState.USER_WINS);
					setStatus("You have won");
					break;
			}
		}
		// field is occupied
		else if (!m_fields[index].getStatus().isFree() && m_currentProperties.isOpen()) {
			setStatus("Selected field is already occupied. Plesae choose another one.");
		}
	}

	/**
	 * Initialize fields for a new game
	 */
	private void newGame() {
		for (int i = 1; i < 10; i++) {
			m_fieldLabels[i].newGame();
			m_fields[i].newGame();
		}

		switch (m_whoStarts) {
			case ALTERNATE:
				m_currentProperties = new GameProperties(m_currentProperties.getWhoStarts().getOther(), m_strength);
				break;
			case COMPUTER_STARTS:
				m_currentProperties = new GameProperties(Player.COMPUTER, m_strength);
				break;
			case USER_STARTS:
				m_currentProperties = new GameProperties(Player.USER, m_strength);
				break;
		}

		// check who starts
		if (m_currentProperties.getWhoStarts().isComputer()) {
			// computer starts
			CalculationResult comResult = m_controller.getComputerField(m_currentProperties);

			if (comResult.hasIndex()) {
				setFieldState(comResult.getIndex(), FieldState.COMPUTER);
				m_currentProperties.incrementMove();
				setStatus("It's your turn");
			}
			else {
				UIUtils.showError("Internal Error", "Error while calculating the next field.\n");
			}
		}
		else {
			setStatus("It's your turn");
		}
	}

	/**
	 * Sets a X/O on a field
	 */
	private void setFieldState(int index, FieldState who) {
		m_fieldLabels[index].setSelected(who);
		m_fields[index].setStatus(who);
	}

	/**
	 * Sets who wins
	 */
	private void setWhoWins(GameState gameState) {
		m_currentProperties.setGameState(gameState);
		m_statistics.setWhoWins(gameState);
	}

	/**
	 * Animates the winning line with three equal marks
	 */
	private void setAnimatedLine(Line winningLine, FieldState whoWins) {
		int[] indizes = winningLine.getIndizes();

		for (int i = 0; i < indizes.length; i++) {
			setFieldState(indizes[i], whoWins);
		}
	}

	private void setStatus(String s) {
		m_statusField.setText(" " + s);
	}

	private void makePitch() {
		int i;

		for (i = 1; i < 10; i++) {
			m_fieldLabels[i] = new FieldLabel();
			m_fieldLabels[i].setFocusable(true);
			m_fieldLabels[i].setBorder(BorderFactory.createEtchedBorder());
			m_fieldLabels[i].addMouseListener(new FieldMouseListener(i));
		}

		// add order by NUM block
		JPanel pitch = new JPanel(new GridLayout(3, 3));
		pitch.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		for (i = 7; i <= 9; i++)
			pitch.add(m_fieldLabels[i]);
		for (i = 4; i <= 6; i++)
			pitch.add(m_fieldLabels[i]);
		for (i = 1; i <= 3; i++)
			pitch.add(m_fieldLabels[i]);

		JPanel center = new JPanel(new BorderLayout(10, 10));
		center.add(new JPanel(), BorderLayout.NORTH);
		center.add(new JPanel(), BorderLayout.SOUTH);
		center.add(new JPanel(), BorderLayout.WEST);
		center.add(new JPanel(), BorderLayout.EAST);
		center.add(pitch, BorderLayout.CENTER);
		m_contentPane.add(center, BorderLayout.CENTER);
	}

	private void makeMenu() {
		m_menuBar = new JMenuBar();

		makeMenuFile();
		makeMenuProperties();
		makeMenuHelp();
	}

	private void makeMenuFile() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.addMenuListener(new FileMenuListener());
		fileMenu.setMnemonic('F');
		m_newGame = new JMenuItem("New Game", 'N');
		m_newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!m_currentProperties.isOpen()) {
					newGame();
				}
			}
		});
		m_newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(m_newGame);
		fileMenu.addSeparator();
		JMenuItem statistik = new JMenuItem("Statistics", 'S');
		statistik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (m_statisticDialog == null)
					m_statisticDialog = new StatisticDialog(TicTacToeFrame.this);

				m_statisticDialog.setStatistic(m_statistics.getNumberGames(), m_statistics.getNumberUserWins(), m_statistics.getNumberComputerWins(), m_statistics.getNumberDraws());
				m_statisticDialog.setVisible(true);
			}
		});
		fileMenu.add(statistik);
		fileMenu.addSeparator();
		JMenuItem exitItem = new JMenuItem("Exit", 'E');
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		m_menuBar.add(fileMenu);
	}

	private void makeMenuProperties() {
		JMenu propertiesMenu = new JMenu("Settings");
		propertiesMenu.setMnemonic('S');

		JMenu order = new JMenu("Order");
		order.setMnemonic('O');
		ButtonGroup orderGroup = new ButtonGroup();
		JRadioButtonMenuItem alternate = new JRadioButtonMenuItem("Alternating");
		alternate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_whoStarts = GameWhoStarts.ALTERNATE;
			}
		});
		alternate.setMnemonic('A');
		alternate.setSelected(true);
		order.add(alternate);
		orderGroup.add(alternate);
		JRadioButtonMenuItem userStarts = new JRadioButtonMenuItem("User starts");
		userStarts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_whoStarts = GameWhoStarts.USER_STARTS;
			}
		});
		userStarts.setMnemonic('U');
		order.add(userStarts);
		orderGroup.add(userStarts);
		JRadioButtonMenuItem comStarts = new JRadioButtonMenuItem("Computer starts");
		comStarts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_whoStarts = GameWhoStarts.COMPUTER_STARTS;
			}
		});
		comStarts.setMnemonic('C');
		order.add(comStarts);
		orderGroup.add(comStarts);
		propertiesMenu.add(order);
		propertiesMenu.addSeparator();

		JMenu strength = new JMenu("Playing strength");
		strength.setMnemonic('s');
		ButtonGroup strengthGroup = new ButtonGroup();
		JRadioButtonMenuItem strenght_low = new JRadioButtonMenuItem("Low");
		strenght_low.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_strength = PlayingStrength.LOW;
			}
		});
		strenght_low.setMnemonic('L');
		strength.add(strenght_low);
		strengthGroup.add(strenght_low);
		JRadioButtonMenuItem strenght_high = new JRadioButtonMenuItem("High");
		strenght_high.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_strength = PlayingStrength.HIGH;
			}
		});
		strenght_high.setMnemonic('H');
		strenght_high.setSelected(true);
		m_strength = PlayingStrength.HIGH;
		strength.add(strenght_high);
		strengthGroup.add(strenght_high);
		propertiesMenu.add(strength);
		m_menuBar.add(propertiesMenu);
	}

	private void makeMenuHelp() {
		JMenu help = new JMenu("Help");
		help.setMnemonic('H');
		JMenuItem info = new JMenuItem("About", 'A');
		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Dialog Info
				if (m_aboutDialog == null) {
					m_aboutDialog = new AboutDialog(TicTacToeFrame.this);
				}
				m_aboutDialog.setVisible(true);
			}
		});
		help.add(info);
		m_menuBar.add(help);
		setJMenuBar(m_menuBar);
	}

	private void makeStatus() {
		m_statusField.setBackground(TicTacToeFrame.this.getBackground());
		m_statusField.setEditable(false);
		m_statusField.setForeground(Color.black);
		m_statusField.setFont(new Font("SansSerif", Font.PLAIN, 11));
		m_contentPane.add(m_statusField, BorderLayout.SOUTH);
	}

	private void centreDialog() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setLocation((screenWidth - WIDTH) / 2, (screenHeight - HEIGHT) / 2);
	}

	private class FieldMouseListener extends MouseAdapter {
		public FieldMouseListener(int i) {
			m_index = i;
		}

		public void mouseReleased(MouseEvent e) {
			processEvent(m_index);
		}

		private int m_index;
	}

	private class FieldKeyListener implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() != KeyEvent.KEY_RELEASED) {
				return false;
			}
			
			int keyCode = e.getKeyCode();
			int keyChar = e.getKeyChar();

			if (keyCode == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
			else if (KeyEvent.VK_1 <= keyChar && keyChar <= KeyEvent.VK_9) {
				processEvent(keyChar - '0');
				return true;
			}
			else if (KeyEvent.VK_0 == keyChar) {
				// easter egg to start a new game
				newGame();
				return true;
			}
			return false;
		}
	}

	private class FileMenuListener implements MenuListener {
		public void menuSelected(MenuEvent menuEvent) {
			m_newGame.setEnabled(!m_currentProperties.isOpen());
		}

		public void menuCanceled(MenuEvent menuEvent) {}

		public void menuDeselected(MenuEvent menuEvent) {
			m_newGame.setEnabled(true);
		}
	}
}