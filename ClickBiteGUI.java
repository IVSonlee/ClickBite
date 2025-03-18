import javax.swing.*;
import java.awt.*;

public class ClickBiteGUI extends JFrame {

    public ClickBiteGUI() {
        // Set up JFrame
        setTitle("ClickBite");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------------------- Header Panel ------------------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        Color headerColor = new Color(250, 212, 212);
        headerPanel.setBackground(headerColor);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove any default padding

        // -------------------- Logo and Brand Name ------------------------
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        logoPanel.setOpaque(false);
        ImageIcon logoIcon = new ImageIcon("ClickBite_Images/ClickBite_logo.png");
        JLabel logoLabel = (logoIcon.getIconWidth() > 0) ?
                new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH))) :
                new JLabel("🍔", JLabel.LEFT);
        JLabel brandNameLabel = new JLabel("ClickBite");
        brandNameLabel.setFont(new Font("Arial", Font.BOLD, 28));
        brandNameLabel.setForeground(new Color(255, 56, 56));
        logoPanel.add(logoLabel);
        logoPanel.add(brandNameLabel);
        headerPanel.add(logoPanel, BorderLayout.WEST);

        // -------------------- Navigation ------------------------
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 35));
        navPanel.setOpaque(false);
        String[] navItems = {"Menu", "Order Now", "Contact Us", "About Us"};
        for (String item : navItems) {
            JLabel navLabel = new JLabel(item);
            navLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            navLabel.setForeground(new Color(255, 56, 56));
            navPanel.add(navLabel);
        }
        headerPanel.add(navPanel, BorderLayout.CENTER);

        // -------------------- Account Dropdown ------------------------
        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 30));
        accountPanel.setOpaque(false);
        JLabel accountLabel = new JLabel("Account: ");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        accountLabel.setForeground(new Color(255, 56, 56));
        String[] accountOptions = {"Profile", "My Orders", "My Cart", "Sign Up / Sign In", "Log Out"};
        JComboBox<String> accountDropdown = new JComboBox<>(accountOptions);
        accountDropdown.setPreferredSize(new Dimension(180, 30));
        accountPanel.add(accountLabel);
        accountPanel.add(accountDropdown);
        headerPanel.add(accountPanel, BorderLayout.EAST);

        // ------------------------ Banner Panel with Button ------------------------
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setPreferredSize(new Dimension(getWidth(), 430));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove top spacing

        ImageIcon bannerIcon = new ImageIcon("ClickBite_Images/ClickBite_Banner.png");
        JLabel bannerLabel = (bannerIcon.getIconWidth() > 0) ?
                new JLabel(new ImageIcon(bannerIcon.getImage().getScaledInstance(1400, 430, Image.SCALE_SMOOTH))) :
                new JLabel("Welcome to ClickBite!", JLabel.CENTER);
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

        // ------------------------ Footer Panel ------------------------
        JPanel footerPanel = new JPanel(new GridLayout(1, 4));
        footerPanel.setBackground(new Color(102, 102, 102));
        String[] footerItems = {"Privacy Policy", "Terms & Conditions", "Menu", "About Us"};
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
            if (accountDropdown.getSelectedItem().equals("Sign Up / Sign In")) {
                showSignUpSignInOptions();
            }
        });

        setVisible(true);
    }

    // ------------------------- Popup Forms -------------------------

    private void showSignUpSignInOptions() {
        String[] options = {"Customer Sign Up", "Customer Sign In", "Admin Sign Up", "Admin Sign In"};
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
        JTextField[] fields = createFields(panel, "Full name", "Email Address", "Username", "Street Address", "City", "Postal/ZIP Code");
        JPasswordField password = new JPasswordField(), confirmPassword = new JPasswordField();
        JCheckBox termsCheck = new JCheckBox("I agree to Admin terms & Privacy Policy");
        panel.add(new JLabel("Password:")); panel.add(password);
        panel.add(new JLabel("Confirm Password:")); panel.add(confirmPassword);
        panel.add(termsCheck); panel.add(new JLabel());
        JOptionPane.showConfirmDialog(this, panel, "Customer Sign Up", JOptionPane.OK_CANCEL_OPTION);
    }

    private void showCustomerSignInForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JCheckBox rememberMe = new JCheckBox("Remember Me");
        panel.add(new JLabel("Email:")); panel.add(email);
        panel.add(new JLabel("Password:")); panel.add(password);
        panel.add(rememberMe); panel.add(new JLabel());
        panel.add(new JLabel("Forgot Password? Click here to reset")); panel.add(new JLabel());
        JOptionPane.showConfirmDialog(this, panel, "Customer Sign In", JOptionPane.OK_CANCEL_OPTION);
    }

    private void showAdminSignUpForm() {
        JPanel panel = new JPanel(new GridLayout(8, 2));
        JTextField[] fields = createFields(panel, "Full name", "Email Address", "Username");
        JPasswordField password = new JPasswordField(), confirmPassword = new JPasswordField();
        panel.add(new JLabel("Password:")); panel.add(password);
        panel.add(new JLabel("Confirm Password:")); panel.add(confirmPassword);
        panel.add(new JLabel("Role:")); panel.add(new JLabel("Order Manager"));
        JOptionPane.showConfirmDialog(this, panel, "Admin Sign Up", JOptionPane.OK_CANCEL_OPTION);
    }

    private void showAdminSignInForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField userOrEmail = new JTextField();
        JPasswordField password = new JPasswordField();
        JCheckBox rememberMe = new JCheckBox("Remember Me");
        panel.add(new JLabel("Username or Email Address:")); panel.add(userOrEmail);
        panel.add(new JLabel("Password:")); panel.add(password);
        panel.add(rememberMe); panel.add(new JLabel());
        panel.add(new JLabel("Forgot Password? Click here to reset")); panel.add(new JLabel());
        JOptionPane.showConfirmDialog(this, panel, "Admin Sign In", JOptionPane.OK_CANCEL_OPTION);
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
}
