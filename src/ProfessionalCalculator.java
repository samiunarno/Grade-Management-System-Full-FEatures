import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.UUID;
import java.awt.geom.RoundRectangle2D;

public class ProfessionalCalculator {
    private JFrame frame;
    private JTextField display;
    private String currentExpression = "";
    private String calcId;
    private boolean darkMode = false;
    private Color[] themeColors;
    private static final Font DISPLAY_FONT = new Font("Segoe UI", Font.BOLD, 36);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public ProfessionalCalculator() {
        calcId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        initializeTheme();
        initializeGUI();
    }

    private void initializeTheme() {
        themeColors = darkMode ? 
            new Color[]{
                new Color(30, 30, 30),     // Background
                new Color(50, 50, 50),     // Display bg
                new Color(220, 220, 220),  // Display text
                new Color(60, 60, 60),     // Button bg
                new Color(240, 240, 240),  // Button text
                new Color(0, 150, 255),    // Operator color
                new Color(50, 205, 50),    // Equals color
                new Color(255, 99, 71),    // Clear color
                new Color(100, 100, 100)   // Special color
            } :
            new Color[]{
                new Color(245, 245, 245),  // Background
                new Color(255, 255, 255),  // Display bg
                new Color(51, 51, 51),     // Display text
                new Color(240, 240, 240),  // Button bg
                new Color(60, 60, 60),     // Button text
                new Color(30, 144, 255),   // Operator color
                new Color(50, 205, 50),    // Equals color
                new Color(255, 99, 71),    // Clear color
                new Color(180, 180, 180)   // Special color
            };
    }

    private void initializeGUI() {
        frame = new JFrame("Professional Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 400, 600, 30, 30));

        createMenuBar();
        
        JPanel mainPanel = createMainPanel();
        createDisplay(mainPanel);
        createButtonPanel(mainPanel);
        
        frame.add(mainPanel);
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        setupKeyboardSupport();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(darkMode ? new Color(40, 43, 45) : new Color(60, 63, 65));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JMenu settingsMenu = createMenu("Settings", new String[]{
            "Calculator ID: " + calcId,
            darkMode ? "Switch to Light Mode" : "Switch to Dark Mode",
            "Exit"
        }, new ActionListener[]{
            e -> showCalculatorId(),
            e -> toggleTheme(),
            e -> System.exit(0)
        });

        JMenu aboutMenu = createMenu("About", new String[]{
            "About Calculator",
            "Version Info"
        }, new ActionListener[]{
            e -> showAboutDialog(),
            e -> showVersionInfo()
        });

        JMenu helpMenu = createMenu("Help", new String[]{
            "User Guide",
            "Keyboard Shortcuts"
        }, new ActionListener[]{
            e -> showHelpDialog(),
            e -> showShortcutsDialog()
        });

        menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
    }

    private JMenu createMenu(String title, String[] items, ActionListener[] actions) {
        JMenu menu = new JMenu(title);
        menu.setForeground(Color.WHITE);
        menu.setFont(MENU_FONT);
        
        for (int i = 0; i < items.length; i++) {
            JMenuItem item = new JMenuItem(items[i]);
            item.addActionListener(actions[i]);
            menu.add(item);
        }
        return menu;
    }

    private JPanel createMainPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, themeColors[0],
                    getWidth(), getHeight(), themeColors[0].brighter()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                g2.setColor(themeColors[0].darker());
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
                g2.dispose();
            }
        };
    }

    private void createDisplay(JPanel mainPanel) {
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setOpaque(false);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        display = new JTextField("0");
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(DISPLAY_FONT);
        display.setEditable(false);
        display.setOpaque(false);
        display.setForeground(themeColors[2]);
        display.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel displayContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, themeColors[1],
                    0, getHeight(), themeColors[1].darker()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(new Color(0, 0, 0, 30));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
        
        displayContainer.setLayout(new BorderLayout());
        displayContainer.add(display, BorderLayout.CENTER);
        displayPanel.add(displayContainer, BorderLayout.CENTER);
        
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(displayPanel, BorderLayout.NORTH);
    }

    private void createButtonPanel(JPanel mainPanel) {
        String[][] buttonLabels = {
            {"Back", "CE", "C", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"0", ".", "±", "="}
        };

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        for (String[] row : buttonLabels) {
            for (String label : row) {
                buttonPanel.add(createButton(label));
            }
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = getButtonColor(label);
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    0, getHeight(), bgColor.darker()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(bgColor.darker());
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                
                g2.setFont(getFont());
                g2.setColor(getButtonTextColor(label));
                
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(label)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                g2.drawString(label, x, y);
                g2.dispose();
            }
        };
        
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        button.addMouseListener(new ButtonHoverEffect(getButtonColor(label)));
        button.addActionListener(e -> handleButtonClick(label));

        return button;
    }

    private Color getButtonColor(String label) {
        if (label.matches("[÷×\\-+=]")) {
            return themeColors[5]; // Operator color
        } else if (label.equals("C")) {
            return themeColors[7]; // Clear color (red)
        } else if (label.equals("=")) {
            return themeColors[6]; // Equals color (green)
        } else if (label.equals("±")) {
            return themeColors[5]; // Sign change (operator color)
        } else if (label.equals("Back") || label.equals("CE")) {
            return themeColors[8]; // Special color (gray)
        } else {
            return themeColors[3]; // Number color
        }
    }

    private Color getButtonTextColor(String label) {
        if (label.matches("[÷×\\-+=]") || label.equals("C") || label.equals("=") || label.equals("±")) {
            return Color.WHITE;
        } else if (label.equals("Back") || label.equals("CE")) {
            return darkMode ? Color.WHITE : Color.BLACK; // Special text color
        } else {
            return themeColors[4]; // Number text color
        }
    }

    private void handleButtonClick(String label) {
        switch (label) {
            case "C": // Clear All
                currentExpression = "";
                display.setText("0");
                break;
                
            case "CE": // Clear Entry (backspace)
                if (!currentExpression.isEmpty()) {
                    currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                    display.setText(currentExpression.isEmpty() ? "0" : currentExpression);
                }
                break;
                
            case "Back": // Clear All (same as C)
                currentExpression = "";
                display.setText("0");
                break;
                
            case "=":
                calculateResult();
                break;
                
            case "±":
                toggleSign();
                break;
                
            case "÷":
                currentExpression += "/";
                display.setText(currentExpression);
                break;
                
            case "×":
                currentExpression += "*";
                display.setText(currentExpression);
                break;
                
            case ".":
                if (!currentExpression.contains(".") || isLastNumberComplete()) {
                    currentExpression += ".";
                    display.setText(currentExpression);
                }
                break;
                
            default:
                if (currentExpression.equals("0") && label.matches("[0-9]")) {
                    currentExpression = label;
                } else {
                    currentExpression += label;
                }
                display.setText(currentExpression);
                break;
        }
    }

    private boolean isLastNumberComplete() {
        String[] parts = currentExpression.split("[+\\-*/]");
        return parts.length == 0 || !parts[parts.length - 1].contains(".");
    }

    private void calculateResult() {
        try {
            double result = evaluateExpression(currentExpression);
            currentExpression = String.valueOf(result);
            
            if (currentExpression.endsWith(".0")) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 2);
            }
            
            display.setText(currentExpression);
            animateDisplaySuccess();
            
        } catch (Exception ex) {
            showError("Calculation error");
        }
    }

    private double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        return new ExpressionParser(expression).parse();
    }

    private void toggleSign() {
        if (!currentExpression.isEmpty() && !currentExpression.equals("0")) {
            try {
                double value = evaluateExpression(currentExpression);
                value = -value;
                currentExpression = String.valueOf(value);
                if (currentExpression.endsWith(".0")) {
                    currentExpression = currentExpression.substring(0, currentExpression.length() - 2);
                }
                display.setText(currentExpression);
            } catch (Exception ex) {
                if (currentExpression.startsWith("-")) {
                    currentExpression = currentExpression.substring(1);
                } else {
                    currentExpression = "-" + currentExpression;
                }
                display.setText(currentExpression);
            }
        }
    }

    private void showError(String message) {
        display.setText("Error");
        currentExpression = "";
        
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        
        Timer timer = new Timer(1000, e -> display.setText("0"));
        timer.setRepeats(false);
        timer.start();
    }

    private void animateDisplaySuccess() {
        Color originalColor = display.getForeground();
        Color successColor = themeColors[6];
        
        Timer animTimer = new Timer(50, null);
        animTimer.addActionListener(new ActionListener() {
            int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < 10) {
                    float ratio = step / 10.0f;
                    Color blended = blendColors(originalColor, successColor, ratio);
                    display.setForeground(blended);
                    step++;
                } else {
                    animTimer.stop();
                    display.setForeground(originalColor);
                }
            }
        });
        animTimer.start();
    }

    private Color blendColors(Color color1, Color color2, float ratio) {
        int r = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
        int g = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
        int b = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
        return new Color(r, g, b);
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        initializeTheme();
        updateTheme();
    }

    private void updateTheme() {
        frame.repaint();
        frame.getJMenuBar().setBackground(darkMode ? new Color(40, 43, 45) : new Color(60, 63, 65));
        display.setForeground(themeColors[2]);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void setupKeyboardSupport() {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        
        frame.setFocusable(true);
        frame.requestFocus();
        
        // ESC to exit
        frame.getRootPane().registerKeyboardAction(
            e -> System.exit(0),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Enter for equals
        frame.getRootPane().registerKeyboardAction(
            e -> handleButtonClick("="),
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Backspace for CE
        frame.getRootPane().registerKeyboardAction(
            e -> handleButtonClick("CE"),
            KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Delete for C
        frame.getRootPane().registerKeyboardAction(
            e -> handleButtonClick("C"),
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private void handleKeyPress(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();
        
        if (Character.isDigit(keyChar)) {
            handleButtonClick(String.valueOf(keyChar));
        } else {
            switch (keyChar) {
                case '+': handleButtonClick("+"); break;
                case '-': handleButtonClick("-"); break;
                case '*': handleButtonClick("×"); break;
                case '/': handleButtonClick("÷"); break;
                case '.': handleButtonClick("."); break;
            }
        }
        
        // Additional key handling
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE: System.exit(0); break;
            case KeyEvent.VK_BACK_SPACE: handleButtonClick("CE"); break;
            case KeyEvent.VK_DELETE: handleButtonClick("C"); break;
            case KeyEvent.VK_ENTER: handleButtonClick("="); break;
        }
    }

    private void showCalculatorId() {
        JOptionPane.showMessageDialog(frame,
            "Calculator Unique ID: " + calcId,
            "Calculator ID",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        String aboutText = "<html><div style='text-align: center;'>" +
            "<h2>Professional Calculator</h2>" +
            "<p>Version: 3.0</p>" +
            "<p>ID: " + calcId + "</p>" +
            "<p>Features:</p>" +
            "<ul style='text-align: left;'>" +
            "<li>Basic Arithmetic Operations</li>" +
            "<li>Back, CE, C buttons for clear operations</li>" +
            "<li>Dark/Light Theme</li>" +
            "<li>Keyboard Support</li>" +
            "<li>Unique Calculator ID</li>" +
            "</ul>" +
            "</div></html>";
        
        JOptionPane.showMessageDialog(frame, aboutText,
            "About Calculator", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showVersionInfo() {
        JOptionPane.showMessageDialog(frame,
            "Professional Calculator v3.0\n" +
            "Build: 2024.01\n" +
            "Calculator ID: " + calcId + "\n" +
            "Java Version: " + System.getProperty("java.version"),
            "Version Info",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHelpDialog() {
        String helpText = "<html><div style='width: 350px;'>" +
            "<h3>Calculator Guide</h3>" +
            "<p><b>Clear Functions:</b></p>" +
            "<ul>" +
            "<li><b>C</b>: Clear All - Clears everything</li>" +
            "<li><b>CE</b>: Clear Entry - Removes last character</li>" +
            "<li><b>Back</b>: Clear All - Same as C</li>" +
            "<li><b>±</b>: Change sign</li>" +
            "</ul>" +
            "<p><b>Keyboard Shortcuts:</b></p>" +
            "<ul>" +
            "<li><b>ESC</b>: Exit Calculator</li>" +
            "<li><b>0-9</b>: Numbers</li>" +
            "<li><b>+ - * /</b>: Operators</li>" +
            "<li><b>Enter</b>: Equals (=)</li>" +
            "<li><b>Backspace</b>: CE (Clear Entry)</li>" +
            "<li><b>Delete</b>: C (Clear All)</li>" +
            "<li><b>.</b>: Decimal point</li>" +
            "</ul>" +
            "</div></html>";
        
        JOptionPane.showMessageDialog(frame, helpText,
            "Help Guide", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showShortcutsDialog() {
        String shortcuts = "<html><div style='width: 300px;'>" +
            "<h3>Keyboard Shortcuts</h3>" +
            "<table border='0' cellpadding='5'>" +
            "<tr><td><b>ESC</b></td><td>Exit Calculator</td></tr>" +
            "<tr><td><b>0-9</b></td><td>Numbers</td></tr>" +
            "<tr><td><b>+ - * /</b></td><td>Operators</td></tr>" +
            "<tr><td><b>Enter</b></td><td>Equals (=)</td></tr>" +
            "<tr><td><b>Backspace</b></td><td>CE (Clear Entry)</td></tr>" +
            "<tr><td><b>Delete</b></td><td>C (Clear All)</td></tr>" +
            "<tr><td><b>.</b></td><td>Decimal point</td></tr>" +
            "</table></div></html>";
        
        JOptionPane.showMessageDialog(frame, shortcuts,
            "Keyboard Shortcuts", JOptionPane.INFORMATION_MESSAGE);
    }

    // Expression Parser Class
    class ExpressionParser {
        private String expression;
        private int pos = -1, ch;
        
        ExpressionParser(String expression) {
            this.expression = expression;
        }
        
        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < expression.length()) {
                throw new RuntimeException("Unexpected character: " + (char)ch);
            }
            return x;
        }
        
        private void nextChar() {
            ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
        }
        
        private boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }
        
        private double parseExpression() {
            double x = parseTerm();
            while (true) {
                if (eat('+')) x += parseTerm();
                else if (eat('-')) x -= parseTerm();
                else return x;
            }
        }
        
        private double parseTerm() {
            double x = parseFactor();
            while (true) {
                if (eat('*')) x *= parseFactor();
                else if (eat('/')) x /= parseFactor();
                else return x;
            }
        }
        
        private double parseFactor() {
            if (eat('+')) return parseFactor();
            if (eat('-')) return -parseFactor();
            
            double x;
            int startPos = this.pos;
            
            if (eat('(')) {
                x = parseExpression();
                if (!eat(')')) throw new RuntimeException("Missing ')'");
            } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(expression.substring(startPos, this.pos));
            } else {
                throw new RuntimeException("Unexpected character: " + (char)ch);
            }
            
            return x;
        }
    }

    // Button Hover Effect
    class ButtonHoverEffect extends MouseAdapter {
        private Color originalColor;
        
        ButtonHoverEffect(Color originalColor) {
            this.originalColor = originalColor;
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            setButtonColor(e, originalColor.brighter());
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            setButtonColor(e, originalColor);
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            setButtonColor(e, originalColor.darker());
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            setButtonColor(e, originalColor);
        }
        
        private void setButtonColor(MouseEvent e, Color color) {
            JButton button = (JButton) e.getSource();
            button.setBackground(color);
            button.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new ProfessionalCalculator();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}