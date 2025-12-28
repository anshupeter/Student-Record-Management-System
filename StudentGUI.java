import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

// Custom panel with gradient background
class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;
    
    public GradientPanel(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}

// Rounded panel with subtle shadow for modern card look
class RoundedPanel extends JPanel {
    private int radius;
    private Color backgroundColor;
    private float shadowAlpha = 0.18f;
    private int shadowSize = 18;

    public RoundedPanel(int radius, Color backgroundColor) {
        this(radius, backgroundColor, 0.95f);
    }

    public RoundedPanel(int radius, Color backgroundColor, float opacity) {
        this.radius = radius;
        this.backgroundColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), Math.round(opacity * 255));
        setOpaque(false);
    }

    public void setShadowAlpha(float alpha) {
        this.shadowAlpha = alpha;
    }

    public void setShadowSize(int size) {
        this.shadowSize = size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw drop shadow
        g2.setComposite(AlphaComposite.SrcOver.derive(shadowAlpha));
        g2.setColor(new Color(21, 33, 68));
        g2.fillRoundRect(shadowSize, shadowSize, width - shadowSize * 2, height - shadowSize * 2, radius, radius);

        // Draw panel background
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(shadowSize / 2, shadowSize / 2, width - shadowSize, height - shadowSize, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}

public class StudentGUI extends JFrame {
    private ArrayList<Student> students = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField rollField, nameField, marksField;
    private JLabel totalStudentsValueLabel, averageMarksValueLabel, topScoreValueLabel;
    private static final String DATA_FILE = "students.txt";
    
    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SECONDARY_COLOR = new Color(245, 247, 250);
    private static final Color ACCENT_COLOR = new Color(255, 107, 107);
    private static final Color SUCCESS_COLOR = new Color(72, 187, 120);
    private static final Color TEXT_COLOR = new Color(45, 55, 72);
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color CARD_BACKGROUND = new Color(255, 255, 255, 235);
    private static final Color MUTED_TEXT_COLOR = new Color(100, 116, 139);
    
    public StudentGUI() {
        setTitle("Student Record Management System - Modern Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create main gradient background panel
        GradientPanel mainPanel = new GradientPanel(
            new Color(240, 248, 255), // Light blue
            new Color(230, 240, 250)  // Slightly darker blue
        );
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);
        
        // Load data from file when application starts
        loadDataFromFile();
        
        initializeComponents();
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600)); // Set minimum window size
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Save data when application closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveDataToFile();
                System.exit(0);
            }
        });
        
        // Add component listener for dynamic resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Force layout update when window is resized
                revalidate();
                repaint();
            }
        });
        
        // Setup keyboard shortcuts
        setupKeyboardShortcuts();
    }
    
    private void initializeComponents() {
        // Assemble north stack with header, metrics, and form
        JPanel headerPanel = createHeaderPanel();
        JPanel dashboardPanel = createDashboardPanel();
        JPanel inputPanel = createInputPanel();

        JPanel northWrapper = new JPanel();
        northWrapper.setLayout(new BoxLayout(northWrapper, BoxLayout.Y_AXIS));
        northWrapper.setOpaque(false);

        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        northWrapper.add(headerPanel);
        northWrapper.add(Box.createVerticalStrut(15));
        northWrapper.add(dashboardPanel);
        northWrapper.add(Box.createVerticalStrut(15));
        northWrapper.add(inputPanel);

        add(northWrapper, BorderLayout.NORTH);

        // Create table
        createTable();

        // Load existing data into table
        loadDataIntoTable();
        updateDashboardMetrics();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 20, 10, 20),
            BorderFactory.createLineBorder(new Color(200, 200, 200, 150), 1, true)
        ));
        scrollPane.getViewport().setBackground(new Color(255, 255, 255, 250));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Make table responsive to window resizing
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        // Create gradient header panel
        GradientPanel headerPanel = new GradientPanel(
            new Color(74, 144, 226),   // Bright blue
            new Color(59, 130, 246)    // Deeper blue
        );
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Student Record Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Manage your students with modern elegance", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(240, 248, 255));
        subtitleLabel.setOpaque(false);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(0, 20, 0, 20));

        JPanel cardsWrapper = new JPanel();
        cardsWrapper.setOpaque(false);
        cardsWrapper.setLayout(new GridLayout(1, 3, 18, 0));

        totalStudentsValueLabel = createMetricValueLabel();
        averageMarksValueLabel = createMetricValueLabel();
        topScoreValueLabel = createMetricValueLabel();
    topScoreValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
    topScoreValueLabel.setText("<html><div style='font-size:26px;font-weight:700;color:#1F2937;'>—</div></html>");

        cardsWrapper.add(createMetricCard("Total Students", totalStudentsValueLabel, "Active records in the system", PRIMARY_COLOR));
        cardsWrapper.add(createMetricCard("Average Marks", averageMarksValueLabel, "Class performance snapshot", new Color(14, 165, 233)));
        cardsWrapper.add(createMetricCard("Top Performer", topScoreValueLabel, "Highest score right now", ACCENT_COLOR));

        container.add(cardsWrapper, BorderLayout.CENTER);

        return container;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, String helperText, Color accentColor) {
        RoundedPanel card = new RoundedPanel(30, CARD_BACKGROUND);
        card.setShadowAlpha(0.24f);
        card.setShadowSize(20);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 26, 22, 26));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setOpaque(false);
        JLabel accentDot = new JLabel("\u25CF");
        accentDot.setFont(new Font("Segoe UI", Font.BOLD, 14));
        accentDot.setForeground(accentColor);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(accentColor.darker());
        titleRow.add(accentDot);
        titleRow.add(titleLabel);

        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        valueLabel.setBorder(new EmptyBorder(12, 4, 8, 4));

        JLabel helperLabel = new JLabel(helperText);
        helperLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        helperLabel.setForeground(MUTED_TEXT_COLOR);

        card.add(titleRow, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(helperLabel, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createMetricValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 36));
        label.setForeground(TEXT_COLOR);
        label.setOpaque(false);
        return label;
    }

    private void updateDashboardMetrics() {
        if (totalStudentsValueLabel == null || averageMarksValueLabel == null || topScoreValueLabel == null) {
            return;
        }

        int total = students.size();
        totalStudentsValueLabel.setText(String.valueOf(total));

        if (total == 0) {
            averageMarksValueLabel.setText("—");
            topScoreValueLabel.setText("<html><div style='font-size:26px;font-weight:700;color:#1F2937;'>—</div></html>");
            return;
        }

        double sum = 0;
        Student topStudent = null;
        double topMarks = Double.NEGATIVE_INFINITY;

        for (Student student : students) {
            sum += student.getMarks();
            if (student.getMarks() > topMarks) {
                topMarks = student.getMarks();
                topStudent = student;
            }
        }

        double averageMarks = sum / total;
        averageMarksValueLabel.setText(String.format("%.2f%%", averageMarks));

        if (topStudent != null) {
            String topHtml = String.format(
                "<html><div style='font-size:26px;font-weight:700;color:#1F2937;text-align:left;'>%s</div><div style='font-size:14px;color:#64748B;'>%.2f marks</div></html>",
                escapeHtml(topStudent.getName()),
                topMarks
            );
            topScoreValueLabel.setText(topHtml);
        } else {
            topScoreValueLabel.setText("<html><div style='font-size:26px;font-weight:700;color:#1F2937;'>—</div></html>");
        }
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
    
    private JPanel createInputPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(10, 20, 30, 20));

        JLabel sectionTitle = new JLabel("Student Details");
        sectionTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_COLOR);
        sectionTitle.setBorder(new EmptyBorder(0, 5, 12, 0));
        mainPanel.add(sectionTitle, BorderLayout.NORTH);

        RoundedPanel formCard = new RoundedPanel(28, CARD_BACKGROUND);
        formCard.setShadowAlpha(0.22f);
        formCard.setShadowSize(20);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Roll Number
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel rollLabel = new JLabel("Roll Number");
        rollLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rollLabel.setForeground(TEXT_COLOR);
        formCard.add(rollLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3; gbc.fill = GridBagConstraints.HORIZONTAL;
        rollField = createResponsiveTextField();
        formCard.add(rollField, gbc);

        // Name
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);
        formCard.add(nameLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5; gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = createResponsiveTextField();
        formCard.add(nameField, gbc);

        // Marks
        gbc.gridx = 4; gbc.gridy = 0;
        gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel marksLabel = new JLabel("Marks (%)");
        marksLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        marksLabel.setForeground(TEXT_COLOR);
        formCard.add(marksLabel, gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.2; gbc.fill = GridBagConstraints.HORIZONTAL;
        marksField = createResponsiveTextField();
        formCard.add(marksField, gbc);

        mainPanel.add(formCard, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JTextField createResponsiveTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // Set minimum and preferred sizes for responsiveness
        field.setPreferredSize(new Dimension(150, 55)); // Base size
        field.setMinimumSize(new Dimension(100, 55));   // Minimum size
        field.setMaximumSize(new Dimension(500, 55));   // Maximum size
        
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180, 150), 2, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        field.setBackground(new Color(255, 255, 255, 250));
        field.setOpaque(true);
        
        // Add focus effects
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 3, true),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180, 150), 2, true),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
        });
        
        // Add keyboard navigation
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Move to next field or add student if on last field
                    if (field == rollField) {
                        nameField.requestFocus();
                    } else if (field == nameField) {
                        marksField.requestFocus();
                    } else if (field == marksField) {
                        addStudent(); // Add student when Enter is pressed on marks field
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    // Let default TAB behavior handle field navigation
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    clearFields();
                }
            }
        });
        
        return field;
    }
    
    // Keep the old method for backward compatibility if needed elsewhere
    private JTextField createStyledTextField(int columns) {
        return createResponsiveTextField();
    }
    
    private void createTable() {
        String[] columns = {"Roll Number", "Student Name", "Marks"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Modern table styling
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(255, 255, 255, 250));
        table.setOpaque(true);
        table.setSelectionBackground(new Color(74, 144, 226, 80));
        table.setSelectionForeground(TEXT_COLOR);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); // Better column resizing
        table.setFillsViewportHeight(true); // Fill available space
        
        // Header styling
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 248, 255));
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setResizingAllowed(true); // Allow column resizing
        
        // Center align content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Add selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String rollText = tableModel.getValueAt(selectedRow, 0).toString();
                    rollField.setText(rollText);
                    
                    String nameText = tableModel.getValueAt(selectedRow, 1).toString();
                    nameField.setText(nameText);
                    
                    String marksText = tableModel.getValueAt(selectedRow, 2).toString();
                    marksField.setText(marksText);
                }
            }
        });
        
        // Add keyboard support for table
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteStudent();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Load selected student data into fields and focus roll field
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        rollField.requestFocus();
                        rollField.selectAll();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    table.clearSelection();
                    clearFields();
                }
            }
        });
    }
    
    private JPanel createButtonPanel() {
        // Create gradient background for button panel
        GradientPanel mainPanel = new GradientPanel(
            new Color(250, 252, 255), // Very light blue
            new Color(240, 248, 255)  // Light blue
        );
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 25, 20));
        
        // Use FlowLayout with CENTER alignment for responsive buttons
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setOpaque(false);
        
        JButton addBtn = createStyledButton("+ Add Student", SUCCESS_COLOR);
        JButton updateBtn = createStyledButton("Edit Student", PRIMARY_COLOR);
        JButton deleteBtn = createStyledButton("Delete Student", ACCENT_COLOR);
        JButton clearBtn = createStyledButton("Clear Fields", new Color(108, 117, 125));
        JButton searchBtn = createStyledButton("Search Student", new Color(111, 66, 193));
        
        // Add button listeners
        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());
        searchBtn.addActionListener(e -> searchStudent());
        
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);
        panel.add(searchBtn);
        
        mainPanel.add(panel, BorderLayout.CENTER);
        
        // Create bottom panel with status and shortcuts button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        
        // Add status bar with keyboard shortcuts hint
        JLabel statusLabel = new JLabel("Ready to manage students | Press F1 for keyboard shortcuts", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setOpaque(false);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        
        // Add small shortcuts button on the right
        JButton shortcutsBtn = new JButton("Shortcuts");
        shortcutsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        shortcutsBtn.setBackground(new Color(150, 150, 150));
        shortcutsBtn.setForeground(Color.WHITE);
        shortcutsBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        shortcutsBtn.setFocusPainted(false);
        shortcutsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        shortcutsBtn.setOpaque(true);
        shortcutsBtn.addActionListener(e -> showKeyboardShortcutsHelp());
        
        // Add hover effect to shortcuts button
        shortcutsBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                shortcutsBtn.setBackground(new Color(120, 120, 120));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                shortcutsBtn.setBackground(new Color(150, 150, 150));
            }
        });
        
        bottomPanel.add(shortcutsBtn, BorderLayout.EAST);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupKeyboardShortcuts() {
        // Get the root pane for global shortcuts
        JRootPane rootPane = getRootPane();
        
        // Set default button (Enter key)
        JButton addBtn = (JButton) findButtonByText("+ Add Student");
        if (addBtn != null) {
            rootPane.setDefaultButton(addBtn);
        }
        
        // Global keyboard shortcuts
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();
        
        // Ctrl+N - Add New Student (focus roll field)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "newStudent");
        actionMap.put("newStudent", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
                rollField.requestFocus();
                showMessage("Ready to add new student! Start typing roll number.", "New Student");
            }
        });
        
        // Ctrl+S - Search Student
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "searchStudent");
        actionMap.put("searchStudent", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        
        // Ctrl+U - Update Student
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK), "updateStudent");
        actionMap.put("updateStudent", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });
        
        // Ctrl+D - Delete Student
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), "deleteStudent");
        actionMap.put("deleteStudent", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        
        // Ctrl+R - Clear/Reset Fields
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), "clearFields");
        actionMap.put("clearFields", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
                rollField.requestFocus();
            }
        });
        
        // F1 - Show Help
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "showHelp");
        actionMap.put("showHelp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                showKeyboardShortcutsHelp();
            }
        });
        
        // F5 - Refresh/Reload Data
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refreshData");
        actionMap.put("refreshData", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // Reload data from file
                students.clear();
                tableModel.setRowCount(0);
                loadDataFromFile();
                loadDataIntoTable();
                clearFields();
                showMessage("Data refreshed from file!", "Refresh Complete");
            }
        });
        
        // Arrow keys for table navigation when not in table
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK), "selectPrevious");
        actionMap.put("selectPrevious", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int currentRow = table.getSelectedRow();
                if (currentRow > 0) {
                    table.setRowSelectionInterval(currentRow - 1, currentRow - 1);
                    table.scrollRectToVisible(table.getCellRect(currentRow - 1, 0, true));
                }
            }
        });
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK), "selectNext");
        actionMap.put("selectNext", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int currentRow = table.getSelectedRow();
                if (currentRow < table.getRowCount() - 1) {
                    table.setRowSelectionInterval(currentRow + 1, currentRow + 1);
                    table.scrollRectToVisible(table.getCellRect(currentRow + 1, 0, true));
                }
            }
        });
    }
    
    private JButton findButtonByText(String text) {
        // Helper method to find button by text
        return findButtonInContainer(getContentPane(), text);
    }
    
    private JButton findButtonInContainer(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals(text)) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton found = findButtonInContainer((Container) comp, text);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private void showKeyboardShortcutsHelp() {
        String helpText = 
            "KEYBOARD SHORTCUTS:\n\n" +
            "FIELD NAVIGATION:\n" +
            "Enter - Move to next field / Add student (on marks field)\n" +
            "Tab - Move to next field\n" +
            "Escape - Clear all fields\n\n" +
            "GLOBAL SHORTCUTS:\n" +
            "Ctrl+N - New student (clear fields & focus roll)\n" +
            "Ctrl+S - Search student\n" +
            "Ctrl+U - Update selected student\n" +
            "Ctrl+D - Delete selected student\n" +
            "Ctrl+R - Clear/Reset all fields\n" +
            "F1 - Show this help\n" +
            "F5 - Refresh data from file\n\n" +
            "TABLE NAVIGATION:\n" +
            "Delete - Delete selected student\n" +
            "Enter - Load student data to fields\n" +
            "Escape - Clear selection\n" +
            "Ctrl+Arrow UP - Select previous student\n" +
            "Ctrl+Arrow DOWN - Select next student\n\n" +
            "TIP: Use arrow keys to navigate the table,\n" +
            "then press Enter to edit the selected student!";
            
        JOptionPane.showMessageDialog(this, helpText, "Keyboard Shortcuts Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addStudent() {
        try {
            int rollNumber = Integer.parseInt(rollField.getText().trim());
            String name = nameField.getText().trim();
            double marks = Double.parseDouble(marksField.getText().trim());
            
            if (name.isEmpty()) {
                showMessage("Please enter student name.", "Input Error");
                return;
            }
            
            // Check if roll number already exists
            for (Student s : students) {
                if (s.getRollNumber() == rollNumber) {
                    showMessage("Student with roll number " + rollNumber + " already exists!", "Duplicate Entry");
                    return;
                }
            }
            
            Student student = new Student(rollNumber, name, marks);
            students.add(student);
            tableModel.addRow(new Object[]{rollNumber, name, marks});
            updateDashboardMetrics();
            clearFields();
            saveDataToFile(); // Save data after adding
            showMessage("Success! Student added successfully!\n\nName: " + name + " (Roll: " + rollNumber + ")\nMarks: " + marks, "Success");
            
        } catch (NumberFormatException ex) {
            showMessage("Please enter valid numbers for Roll Number and Marks.", "Input Error");
        }
    }
    
    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showMessage("Please select a student to update.", "Selection Error");
            return;
        }
        
        try {
            int rollNumber = Integer.parseInt(rollField.getText().trim());
            String name = nameField.getText().trim();
            double marks = Double.parseDouble(marksField.getText().trim());
            
            if (name.isEmpty()) {
                showMessage("Please enter student name.", "Input Error");
                return;
            }
            
            // Find and update student
            Student student = students.get(selectedRow);
            student.setName(name);
            student.setMarks(marks);
            
            // Update table
            tableModel.setValueAt(rollNumber, selectedRow, 0);
            tableModel.setValueAt(name, selectedRow, 1);
            tableModel.setValueAt(marks, selectedRow, 2);
            updateDashboardMetrics();
            
            clearFields();
            saveDataToFile(); // Save data after updating
            showMessage("Updated! Student updated successfully!\n\nName: " + name + " (Roll: " + rollNumber + ")\nNew Marks: " + marks, "Update Success");
            
        } catch (NumberFormatException ex) {
            showMessage("Please enter valid numbers for Roll Number and Marks.", "Input Error");
        }
    }
    
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showMessage("Please select a student to delete.", "Selection Error");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student record?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            String studentName = students.get(selectedRow).getName();
            students.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            updateDashboardMetrics();
            clearFields();
            saveDataToFile(); // Save data after deleting
            showMessage("Deleted! Student deleted successfully!\n\nName: " + studentName + " has been removed from records.", "Deletion Complete");
        }
    }
    
    private void searchStudent() {
        String rollText = JOptionPane.showInputDialog(this, "Enter Roll Number to Search:");
        if (rollText != null && !rollText.trim().isEmpty()) {
            try {
                int rollNumber = Integer.parseInt(rollText.trim());
                
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getRollNumber() == rollNumber) {
                        table.setRowSelectionInterval(i, i);
                        table.scrollRectToVisible(table.getCellRect(i, 0, true));
                        
                        Student s = students.get(i);
                        rollField.setText(String.valueOf(s.getRollNumber()));
                        nameField.setText(s.getName());
                        marksField.setText(String.valueOf(s.getMarks()));
                        
                        showMessage("Found! Student found!\n\nName: " + s.getName() + " (Roll: " + s.getRollNumber() + ")\nMarks: " + s.getMarks(), "Search Success");
                        return;
                    }
                }
                showMessage("Not Found! Student not found!\n\nNo student with roll number " + rollNumber + " exists in the database.", "Search Result");
                
            } catch (NumberFormatException ex) {
                showMessage("Invalid Input! Invalid input!\n\nPlease enter a valid roll number (numbers only).", "Input Error");
            }
        }
    }
    
    private void clearFields() {
        rollField.setText("");
        nameField.setText("");
        marksField.setText("");
        table.clearSelection();
        // Focus the first field for immediate typing
        rollField.requestFocus();
    }
    
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Database methods for data persistence
    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student student : students) {
                // Escape commas in names by wrapping in quotes if needed
                String name = student.getName();
                if (name.contains(",") || name.contains("\"")) {
                    name = "\"" + name.replace("\"", "\"\"") + "\"";
                }
                writer.println(student.getRollNumber() + "," + name + "," + student.getMarks());
            }
            System.out.println("Saved " + students.size() + " students to file.");
        } catch (IOException e) {
            showMessage("Error saving data to file: " + e.getMessage(), "Save Error");
        }
    }
    
    private void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return; // No data file exists yet
        }
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",", -1); // Use -1 to preserve empty fields
                    if (parts.length == 3) {
                        try {
                            int rollNumber = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            
                            // Skip records with empty names
                            if (name.isEmpty()) {
                                System.err.println("Skipping record with empty name: " + line);
                                continue;
                            }
                            
                            double marks = Double.parseDouble(parts[2].trim());
                            students.add(new Student(rollNumber, name, marks));
                            System.out.println("Loaded student: " + rollNumber + ", " + name + ", " + marks);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                        }
                    } else {
                        System.err.println("Invalid line format (expected 3 parts): " + line);
                    }
                }
            }
            System.out.println("Loaded " + students.size() + " students from file.");
        } catch (IOException e) {
            showMessage("Error loading data from file: " + e.getMessage(), "Load Error");
        }
    }
    
    private void loadDataIntoTable() {
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getRollNumber(),
                student.getName(),
                student.getMarks()
            });
        }
        updateDashboardMetrics();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentGUI().setVisible(true);
        });
    }
}