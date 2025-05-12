import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.*;

public class ClickBiteGUI extends JFrame {
    // Store user data
    private Map<String, String> currentUser = null;
    private JComboBox<String> accountDropdown; // Declare at class level
    private OrderNow orderWindow = null;

    public ClickBiteGUI() {
        // Set up JFrame
        DatabaseManager.initializeDatabase();
        setTitle("ClickBite");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------------------- Header Panel ------------------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        Color headerColor = new Color(250, 212, 212);
        headerPanel.setBackground(headerColor);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // -------------------- Logo and Brand Name ------------------------
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        logoPanel.setOpaque(false);
        ImageIcon logoIcon = new ImageIcon("ClickBite/ClickBite_Images/ClickBite_logo.png");
        JLabel logoLabel = (logoIcon.getIconWidth() > 0)
                ? new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)))
                : new JLabel("\uD83C\uDF54", JLabel.LEFT);
        JLabel brandNameLabel = new JLabel("ClickBite");
        brandNameLabel.setFont(new Font("Arial", Font.BOLD, 28));
        brandNameLabel.setForeground(new Color(255, 56, 56));
        logoPanel.add(logoLabel);
        logoPanel.add(brandNameLabel);
        headerPanel.add(logoPanel, BorderLayout.WEST);

        // -------------------- Navigation ------------------------
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 35));
        navPanel.setOpaque(false);

        String[] navItems = { "My Cart", "Order Now", "Contact Us", "About Us" };

        for (String item : navItems) {
            JButton navButton = new JButton(item);
            String itemCopy = item; // capture the current value

            navButton.addActionListener(e -> {
                switch (itemCopy) {
                    case "Contact Us":
                        new ContactUsPage(orderWindow); // Ensure this is a JFrame or JDialog subclass
                        break;
                    case "My Cart":
                        System.out.println("My Cart clicked");
                        break;
                    case "Order Now":
                        String email = currentUser != null ? currentUser.get("email") : null;
                        new OrderNow(email);
                        break;
                    case "About Us":
                        System.out.println("About Us clicked");
                        break;
                }
            });

            navPanel.add(navButton);
        }

        headerPanel.add(navPanel, BorderLayout.CENTER);

        // -------------------- Account Dropdown ------------------------
        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 30));
        accountPanel.setOpaque(false);
        JLabel accountLabel = new JLabel("Account: ");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        accountLabel.setForeground(new Color(255, 56, 56));
        String[] accountOptions = { "Profile", "My Orders", "My Cart", "Sign Up / Sign In", "Log Out" };
        accountDropdown = new JComboBox<>(accountOptions); // Initialize the class-level variable
        accountDropdown.setPreferredSize(new Dimension(180, 30));
        accountPanel.add(accountLabel);
        accountPanel.add(accountDropdown);
        headerPanel.add(accountPanel, BorderLayout.EAST);

        // ------------------------ Banner Panel with Button ------------------------
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setPreferredSize(new Dimension(getWidth(), 430));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        ImageIcon bannerIcon = new ImageIcon("ClickBite/ClickBite_Images/ClickBite_Banner.png");
        JLabel bannerLabel = (bannerIcon.getIconWidth() > 0)
                ? new JLabel(new ImageIcon(bannerIcon.getImage().getScaledInstance(1400, 430, Image.SCALE_SMOOTH)))
                : new JLabel("Welcome to ClickBite!", JLabel.CENTER);
        bannerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton orderNowButton = new JButton("Order Now");
        orderNowButton.setFont(new Font("Arial", Font.BOLD, 18));
        orderNowButton.setBackground(new Color(255, 56, 56));
        orderNowButton.setForeground(Color.WHITE);
        orderNowButton.setFocusPainted(false);
        orderNowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderNowButton.setMaximumSize(new Dimension(200, 40));

        centerPanel.add(bannerLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(orderNowButton);
        centerPanel.add(Box.createVerticalStrut(20));

        orderNowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (orderWindow == null || !orderWindow.isVisible()) {
                    String email = currentUser != null ? currentUser.get("email") : null;
                    orderWindow = new OrderNow(email); // Pass user email
                    orderWindow.setVisible(true);
                    setVisible(false);
                } else {
                    orderWindow.toFront();
                }
            }
        });

        // ------------------------ Footer Panel ------------------------
        JPanel footerPanel = new JPanel(new GridLayout(1, 4));
        footerPanel.setBackground(new Color(102, 102, 102));
        String[] footerItems = { "Privacy Policy", "Terms & Conditions", "Menu", "About Us" };
        for (String item : footerItems) {
            JLabel label = new JLabel(item, JLabel.CENTER);
            label.setForeground(Color.WHITE);
            footerPanel.add(label);
        }

        // ------------------------ Add Panels to Frame ------------------------
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // ---------------- Dropdown Action -------------------
        accountDropdown.addActionListener(e -> {
            String selected = (String) accountDropdown.getSelectedItem();
            if (selected.equals("Sign Up / Sign In")) {
                showSignUpSignInOptions();
            } else if (selected.equals("Profile")) {
                if (currentUser != null) {
                    if (currentUser.containsKey("role") && "Order Manager".equals(currentUser.get("role"))) {
                        showAdminPanel(); // ADMIN ONLY
                    } else {
                        showProfilePopup();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please sign in first", "Profile",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (selected.equals("Log Out")) {
                currentUser = null;
                JOptionPane.showMessageDialog(this, "Logged out successfully", "Log Out",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            // Reset the dropdown to avoid staying on the selected option
            accountDropdown.setSelectedIndex(0);
        });

        setVisible(true);

        int customerCount = DatabaseManager.getUserCount();
        JLabel countLabel = new JLabel("Total Registered Customers: " + customerCount);
        countLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Add the label to the GUI
        this.add(countLabel);
    }

    private void showSignUpSignInOptions() {
        String[] options = { "Customer Sign Up", "Customer Sign In", "Admin Sign Up", "Admin Sign In" };
        int choice = JOptionPane.showOptionDialog(this, "Choose an option:", "Sign Up / Sign In",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> showCustomerSignUpForm();
            case 1 -> showCustomerSignInForm();
            case 2 -> showAdminSignUpForm();
            case 3 -> showAdminSignInForm();
        }
    }

    private void showCustomerSignUpForm() {
        JPanel panel = new JPanel(new GridLayout(10, 2));
        JTextField fullNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField postalField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        JCheckBox termsCheck = new JCheckBox("I agree to terms & Privacy Policy");

        panel.add(new JLabel("Full name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Email Address:"));
        panel.add(emailField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Street Address:"));
        panel.add(streetField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("Postal/ZIP Code:"));
        panel.add(postalField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        panel.add(termsCheck);
        panel.add(new JLabel());

        int result = JOptionPane.showConfirmDialog(this, panel, "Customer Sign Up", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!termsCheck.isSelected()) {
                JOptionPane.showMessageDialog(this, "You must agree to the terms", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, String> userData = new HashMap<>();
            userData.put("fullName", fullNameField.getText());
            userData.put("email", emailField.getText());
            userData.put("username", usernameField.getText());
            userData.put("street", streetField.getText());
            userData.put("city", cityField.getText());
            userData.put("postal", postalField.getText());
            userData.put("password", new String(passwordField.getPassword()));

            saveUserToDatabase(userData);
            currentUser = userData;

            JOptionPane.showMessageDialog(this, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showCustomerSignInForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JCheckBox rememberMe = new JCheckBox("Remember Me");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(rememberMe);
        panel.add(new JLabel());
        panel.add(new JLabel("Forgot Password? Click here to reset"));
        panel.add(new JLabel());

        int result = JOptionPane.showConfirmDialog(this, panel, "Customer Sign In", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            String sql = "SELECT * FROM users WHERE email = ?";
            try (Connection conn = DatabaseManager.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        // Load user data from database
                        currentUser = new HashMap<>();
                        currentUser.put("email", rs.getString("email"));
                        currentUser.put("fullName", rs.getString("fullName"));
                        currentUser.put("username", rs.getString("username"));
                        currentUser.put("street", rs.getString("street"));
                        currentUser.put("city", rs.getString("city"));
                        currentUser.put("postal", rs.getString("postal"));
                        currentUser.put("password", storedPassword);

                        JOptionPane.showMessageDialog(this, "Sign in successful!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void showAdminSignUpForm() {
        JPanel panel = new JPanel(new GridLayout(8, 2));
        JTextField fullNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        JCheckBox termsCheck = new JCheckBox("I agree to terms & Privacy Policy");

        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        panel.add(new JLabel("Role:"));
        panel.add(new JLabel("Order Manager")); // Default role
        panel.add(termsCheck);
        panel.add(new JLabel());

        int result = JOptionPane.showConfirmDialog(this, panel, "Admin Sign Up", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!termsCheck.isSelected()) {
                JOptionPane.showMessageDialog(this, "You must agree to the terms", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Save admin to database
            String sql = "INSERT INTO admins (email, fullName, username, role, password) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, emailField.getText());
                pstmt.setString(2, fullNameField.getText());
                pstmt.setString(3, usernameField.getText());
                pstmt.setString(4, "Order Manager"); // Default role
                pstmt.setString(5, new String(passwordField.getPassword()));
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Admin registration successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void showAdminSignInForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JCheckBox rememberMe = new JCheckBox("Remember Me");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(rememberMe);
        panel.add(new JLabel());
        panel.add(new JLabel("Forgot Password? Click here to reset"));
        panel.add(new JLabel());

        int result = JOptionPane.showConfirmDialog(this, panel, "Admin Sign In", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            String sql = "SELECT * FROM admins WHERE email = ?";
            try (Connection conn = DatabaseManager.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        // Load admin data
                        currentUser = new HashMap<>();
                        currentUser.put("email", rs.getString("email"));
                        currentUser.put("fullName", rs.getString("fullName"));
                        currentUser.put("username", rs.getString("username"));
                        currentUser.put("role", rs.getString("role"));
                        currentUser.put("password", storedPassword);

                        JOptionPane.showMessageDialog(this, "Admin sign in successful!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Admin not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void showAdminPanel() {
        JFrame adminFrame = new JFrame("Admin Dashboard");
        adminFrame.setSize(900, 600);
        adminFrame.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("ClickBite Admin Panel", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        adminFrame.add(title, BorderLayout.NORTH);

        // === LEFT PANEL: Customers + Messages ===
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Customer Messages"));

        Map<String, String> messages = DatabaseManager.getAllContactMessages(); // You must add this method
        DefaultListModel<String> customerListModel = new DefaultListModel<>();
        for (String name : messages.keySet())
            customerListModel.addElement(name);

        JList<String> customerList = new JList<>(customerListModel);
        JTextArea messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);

        customerList.addListSelectionListener(e -> {
            String selected = customerList.getSelectedValue();
            if (selected != null) {
                messageArea.setText(messages.get(selected));
            }
        });

        leftPanel.add(new JScrollPane(customerList), BorderLayout.WEST);
        leftPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // === RIGHT PANEL: User Count ===
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("User Overview"));

        int count = DatabaseManager.getUserCount(); // You must add this method
        JLabel userCountLabel = new JLabel("Total Customers: " + count);
        userCountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(userCountLabel);

        // More widgets like cart orders can be added here

        // === Combine Panels ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        adminFrame.add(splitPane, BorderLayout.CENTER);

        adminFrame.setLocationRelativeTo(this);
        adminFrame.setVisible(true);
    }

    private void showProfilePopup() {
        if (currentUser == null)
            return;

        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel profilePicLabel;
        ImageIcon profileIcon = new ImageIcon("ClickBite_Images/profile_pic.png");
        if (profileIcon.getIconWidth() > 0) {
            profilePicLabel = new JLabel(
                    new ImageIcon(profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        } else {
            profilePicLabel = new JLabel("👤", JLabel.CENTER);
            profilePicLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        profilePanel.add(profilePicLabel, gbc);

        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel personalInfoLabel = new JLabel("Personal Information");
        personalInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profilePanel.add(personalInfoLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        profilePanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 2;
        profilePanel.add(new JLabel(currentUser.get("fullName")), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        profilePanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 2;
        profilePanel.add(new JLabel(currentUser.get("email")), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        profilePanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 2;
        profilePanel.add(new JLabel(currentUser.get("username")), gbc);

        if (currentUser.containsKey("role")) {
            gbc.gridx = 1;
            gbc.gridy = 4; // Adjust position as needed
            profilePanel.add(new JLabel("Role:"), gbc);
            gbc.gridx = 2;
            profilePanel.add(new JLabel(currentUser.get("role")), gbc);
        }

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profilePanel.add(addressLabel, gbc);

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        profilePanel.add(new JLabel("Street:"), gbc);
        gbc.gridx = 2;
        profilePanel.add(new JLabel(currentUser.get("street")), gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        profilePanel.add(new JLabel("City:"), gbc);
        gbc.gridx = 2;
        profilePanel.add(new JLabel(currentUser.get("city")), gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        profilePanel.add(new JLabel("Postal Code:"), gbc);
        gbc.gridx = 2;
        profilePanel.add(new JLabel(currentUser.get("postal")), gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton editButton = new JButton("Edit Profile");
        editButton.setBackground(new Color(255, 56, 56));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> showEditProfileForm());
        profilePanel.add(editButton, gbc);

        JDialog profileDialog = new JDialog(this, "My Profile", true);
        profileDialog.setContentPane(profilePanel);
        profileDialog.pack();
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setVisible(true);
    }

    private void showEditProfileForm() {
        if (currentUser == null)
            return;

        JPanel panel = new JPanel(new GridLayout(8, 2));
        JTextField fullNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField postalField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Profile", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String oldEmail = currentUser.get("email");
            String newEmail = emailField.getText();

            String sql = "UPDATE users SET email=?, fullName=?, username=?, street=?, city=?, postal=?, password=? WHERE email=?";
            try (Connection conn = DatabaseManager.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, newEmail);
                pstmt.setString(2, fullNameField.getText());
                pstmt.setString(3, usernameField.getText());
                pstmt.setString(4, streetField.getText());
                pstmt.setString(5, cityField.getText());
                pstmt.setString(6, postalField.getText());
                pstmt.setString(7, new String(passwordField.getPassword()));
                pstmt.setString(8, oldEmail);
                pstmt.executeUpdate();

                currentUser.put("fullName", fullNameField.getText());
                currentUser.put("email", newEmail);
                currentUser.put("username", usernameField.getText());
                currentUser.put("street", streetField.getText());
                currentUser.put("city", cityField.getText());
                currentUser.put("postal", postalField.getText());
                currentUser.put("password", new String(passwordField.getPassword()));

                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error during update: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private JTextField[] createFields(JPanel panel, String... labels) {
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField();
            panel.add(fields[i]);
        }
        return fields;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClickBiteGUI::new);
    }

    private void saveUserToDatabase(Map<String, String> userData) {
        String sql = "INSERT INTO users (email, fullName, username, street, city, postal, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userData.get("email"));
            pstmt.setString(2, userData.get("fullName"));
            pstmt.setString(3, userData.get("username"));
            pstmt.setString(4, userData.get("street"));
            pstmt.setString(5, userData.get("city"));
            pstmt.setString(6, userData.get("postal"));
            pstmt.setString(7, userData.get("password"));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}