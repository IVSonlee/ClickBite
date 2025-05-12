import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ContactUsPage extends JFrame {

    public ContactUsPage(JFrame homePage) {
        setTitle("ClickBite - Contact Us");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------------------- Header --------------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 212, 212));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Contact Us");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(255, 56, 56));

        JButton undoButton = new JButton("Exit");
        undoButton.setFont(new Font("Arial", Font.BOLD, 16));
        undoButton.setBackground(new Color(255, 56, 56));
        undoButton.setForeground(Color.WHITE);
        undoButton.setFocusPainted(false);

        undoButton.addActionListener(e -> {
            this.dispose();
            homePage.setVisible(true);
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(undoButton, BorderLayout.EAST);

        // -------------------- Main Content --------------------
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100)); // center-align

        // Contact Info Panel
        JPanel contactInfoPanel = new JPanel();
        contactInfoPanel.setLayout(new BoxLayout(contactInfoPanel, BoxLayout.Y_AXIS));
        contactInfoPanel.setBorder(BorderFactory.createTitledBorder("Contact Information"));
        contactInfoPanel.add(new JLabel(
                "📍 Address: 1234 Mabini Street, Barangay San Antonio, Quezon City, Metro Manila, Philippines, 1100"));
        contactInfoPanel.add(Box.createVerticalStrut(10));
        contactInfoPanel.add(new JLabel("☎ Phone: (123) 456-7890"));
        contactInfoPanel.add(Box.createVerticalStrut(10));
        contactInfoPanel.add(new JLabel("✉ Email: support@clickbite.com"));

        // Contact Form Panel
        JPanel contactFormPanel = new JPanel();
        contactFormPanel.setLayout(new BoxLayout(contactFormPanel, BoxLayout.Y_AXIS));
        contactFormPanel.setBorder(BorderFactory.createTitledBorder("Contact Form"));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextArea messageArea = new JTextArea(5, 20);
        JButton sendButton = new JButton("Send Message");
        sendButton.setBackground(new Color(255, 56, 56));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String message = messageArea.getText().trim();

            if (!name.isEmpty() && !email.isEmpty() && !message.isEmpty()) {
                DatabaseManager.saveContactMessage(name, email, message);
                JOptionPane.showMessageDialog(this, "Message sent successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                emailField.setText("");
                messageArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contactFormPanel.add(new JLabel("Name:"));
        contactFormPanel.add(nameField);
        contactFormPanel.add(Box.createVerticalStrut(10));
        contactFormPanel.add(new JLabel("Email:"));
        contactFormPanel.add(emailField);
        contactFormPanel.add(Box.createVerticalStrut(10));
        contactFormPanel.add(new JLabel("Message:"));
        contactFormPanel.add(new JScrollPane(messageArea));
        contactFormPanel.add(Box.createVerticalStrut(10));
        contactFormPanel.add(sendButton);

        // Social Media Panel
        JPanel socialMediaPanel = new JPanel();
        socialMediaPanel.setLayout(new BoxLayout(socialMediaPanel, BoxLayout.Y_AXIS));
        socialMediaPanel.setBorder(BorderFactory.createTitledBorder("Follow Us"));
        socialMediaPanel.add(new JLabel("🔗 Facebook: @ClickBite"));
        socialMediaPanel.add(Box.createVerticalStrut(10));
        socialMediaPanel.add(new JLabel("🔗 Instagram: @clickbite_official"));
        socialMediaPanel.add(Box.createVerticalStrut(10));
        socialMediaPanel.add(new JLabel("🔗 Twitter: @clickbite"));

        // Add to content panel in order: contact info, form, social links
        contentPanel.add(contactInfoPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(contactFormPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(socialMediaPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        ClickBiteGUI home = new ClickBiteGUI(); // Replace with your homepage frame
        new ContactUsPage(home);
        home.setVisible(false);
    }
}
