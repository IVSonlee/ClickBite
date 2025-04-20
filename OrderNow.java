import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class OrderNow extends JFrame {
    private JPanel categoryPanel, foodPanel, cartPanel, headerPanel;
    private JLabel totalLabel;
    private double totalAmount = 0.0;
    private Map<String, List<MenuItem>> foodItemsMap;
    private Map<String, List<CartItem>> cartItemsByCategory;

    public OrderNow() {
        super("Order Now");

        foodItemsMap = new HashMap<>();
        cartItemsByCategory = new HashMap<>();

        // Sample data
        foodItemsMap.put("Best Sellers", Arrays.asList(
                new MenuItem("2pc Chickenjoy", 169, "ClickBite/ClickBite_Images/Best_Sellers/Menu1.png"),
                new MenuItem("6pc Chickenjoy with Palabok Family Plan", 927, "ClickBite/ClickBite_Images/Best_Sellers/Menu2.png"),
                new MenuItem("Big Mac", 100, "ClickBite/ClickBite_Images/Best_Sellers/Menu3.png"),
                new MenuItem("Cheesy Yumburger", 72, "ClickBite/ClickBite_Images/Best_Sellers/Menu4.png"),
                new MenuItem("Sausage Burrito", 80, "ClickBite/ClickBite_Images/Best_Sellers/Menu5.png"),
                new MenuItem("Spicy McCrispy", 90, "ClickBite/ClickBite_Images/Best_Sellers/Menu6.png")
        ));
        foodItemsMap.put("Breakfast", Arrays.asList(
                new MenuItem("1pc Breakfast Chickenjoy", 151, "ClickBite/ClickBite_Images/Breakfast/Menu1.png"),
                new MenuItem("Bacon, Egg & Cheese Biscuit", 55, "ClickBite/ClickBite_Images/Breakfast/Menu2.png"),
                new MenuItem("Breakfast Burger Steak", 127, "ClickBite/ClickBite_Images/Breakfast/Menu3.png"),
                new MenuItem("Egg McMuffin", 65, "ClickBite/ClickBite_Images/Breakfast/Menu4.png"),
                new MenuItem("Longganisa", 148, "ClickBite/ClickBite_Images/Breakfast/Menu5.png"),
                new MenuItem("Sausage McGriddles", 60, "ClickBite/ClickBite_Images/Breakfast/Menu6.png")
        ));
        foodItemsMap.put("Burgers", Arrays.asList(
                new MenuItem("Burger Bundle", 450, "ClickBite/ClickBite_Images/Burgers/Menu1.png"),
                new MenuItem("CheeseBurger", 65, "ClickBite/ClickBite_Images/Burgers/Menu2.png"),
                new MenuItem("Double Cheesy Yumburger", 137, "ClickBite/ClickBite_Images/Burgers/Menu3.png"),
                new MenuItem("Double Quarter Pounder with Cheese", 150, "ClickBite/ClickBite_Images/Burgers/Menu4.png"),
                new MenuItem("McDouble", 120, "ClickBite/ClickBite_Images/Burgers/Menu5.png"),
                new MenuItem("Yumburger Family Savers", 287, "ClickBite/ClickBite_Images/Burgers/Menu6.png")
        ));
        foodItemsMap.put("Chicken & Platters", Arrays.asList(
                new MenuItem("1pc Chickenjoy with Burger Steak", 135, "ClickBite/ClickBite_Images/ChickenPlatters/Menu1.png"),
                new MenuItem("1pc Chickenjoy with Creamy Macaroni Soup", 128, "ClickBite/ClickBite_Images/ChickenPlatters/Menu2.png"),
                new MenuItem("1pc Chickenjoy with Double Rice", 143, "ClickBite/ClickBite_Images/ChickenPlatters/Menu3.png"),
                new MenuItem("1pc Chickenjoy with Jolly Spaghetti", 135, "ClickBite/ClickBite_Images/ChickenPlatters/Menu4.png"),
                new MenuItem("1pc Chickenjoy with Palabok", 205, "ClickBite/ClickBite_Images/ChickenPlatters/Menu5.png"),
                new MenuItem("4pc Chickenjoy Family Box", 338, "ClickBite/ClickBite_Images/ChickenPlatters/Menu6.png")
        ));
        foodItemsMap.put("Desserts & Beverages", Arrays.asList(
                new MenuItem("Chocolate Shake", 60, "ClickBite/ClickBite_Images/DessertsBeverages/Menu1.png"),
                new MenuItem("Chocolate Sundae", 52, "ClickBite/ClickBite_Images/DessertsBeverages/Menu2.png"),
                new MenuItem("Cookies & Cream Sundae", 59, "ClickBite/ClickBite_Images/DessertsBeverages/Menu3.png"),
                new MenuItem("Hot Caramel Sundae", 65, "ClickBite/ClickBite_Images/DessertsBeverages/Menu4.png"),
                new MenuItem("Peach Mango Pie", 43, "ClickBite/ClickBite_Images/DessertsBeverages/Menu5.png"),
                new MenuItem("Strawberry Shake", 70, "ClickBite/ClickBite_Images/DessertsBeverages/Menu6.png")
        ));
        foodItemsMap.put("Fries & Side Dishes", Arrays.asList(
                new MenuItem("Creamy Ranch Sauce", 10, "ClickBite/ClickBite_Images/FriesSide_Dishes/Menu1.png"),
                new MenuItem("Extra Chickenjoy Gravy", 11, "ClickBite/ClickBite_Images/FriesSide_Dishes/Menu2.png"),
                new MenuItem("Jolly Crispy Fries Bucket", 181, "ClickBite/ClickBite_Images/FriesSide_Dishes/Menu3.png"),
                new MenuItem("Jolly Crispy Fries", 53, "ClickBite/ClickBite_Images/FriesSide_Dishes/Menu4.png"),
                new MenuItem("Spicy Buffalo Sauce", 10, "ClickBite/ClickBite_Images/FriesSide_Dishes/Menu5.png"),
                new MenuItem("Tangy Barbeque Sauce", 10, "ClickBite/ClickBite_Images/FriesSide_Dishes/Menu6.jpg")
        ));

        // Layout
        setSize(1200, 700);
        setLayout(new BorderLayout());

        createHeader();
        createCategoryPanel();
        createCartPanel();
        createFoodPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(categoryPanel, BorderLayout.NORTH);
        add(cartPanel, BorderLayout.WEST);
        add(foodPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void createHeader() {
        headerPanel = new JPanel();
        headerPanel.setBackground(Color.ORANGE);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        Label title = new Label("ClickBite - Order Now");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(title);
    }

    private void createCategoryPanel() {
        categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] categories = {
            "Best Sellers", "Breakfast", "Burgers",
            "Chicken & Platters", "Desserts & Beverages", "Fries & Side Dishes"
        };

        for (String category : categories) {
            Button btn = new Button(category);
            btn.addActionListener(e -> showFoodItems(category));
            categoryPanel.add(btn);
        }
    }

    private void createCartPanel() {
        cartPanel = new JPanel();
        cartPanel.setPreferredSize(new Dimension(350, 600));
        cartPanel.setLayout(new BorderLayout());

        Panel cartListPanel = new Panel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));

        totalLabel = new JLabel("Total: ₱0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cartPanel.add(new Label("Your Order:"), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartListPanel), BorderLayout.CENTER);
        cartPanel.add(totalLabel, BorderLayout.SOUTH);
    }

    private void createFoodPanel() {
        foodPanel = new JPanel();
        foodPanel.setLayout(new GridLayout(2, 3, 10, 10));
    }

    private void showFoodItems(String category) {
        foodPanel.removeAll();
        List<MenuItem> items = foodItemsMap.getOrDefault(category, new ArrayList<>());
    
        for (MenuItem item : items) {
            Panel itemPanel = new Panel(new BorderLayout());
            itemPanel.setBackground(Color.LIGHT_GRAY);
            itemPanel.setPreferredSize(new Dimension(250, 200));
    
            // Load and scale image
            ImageIcon icon = new ImageIcon(item.image);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
            Label name = new Label(item.name + " - ₱" + item.price);
            name.setFont(new Font("Arial", Font.BOLD, 14));
            name.setAlignment(Label.CENTER);
    
            Choice qtyChoice = new Choice();
            for (int i = 1; i <= 10; i++) qtyChoice.add(String.valueOf(i));
    
            Button addBtn = new Button("Add to Order");
            addBtn.addActionListener(e -> {
                int qty = Integer.parseInt(qtyChoice.getSelectedItem());
                addToCart(category, item, qty);
            });
    
            Panel bottom = new Panel(new FlowLayout());
            bottom.add(qtyChoice);
            bottom.add(addBtn);
    
            itemPanel.add(imageLabel, BorderLayout.NORTH);
            itemPanel.add(name, BorderLayout.CENTER);
            itemPanel.add(bottom, BorderLayout.SOUTH);
    
            foodPanel.add(itemPanel);
        }
    
        foodPanel.revalidate();
        foodPanel.repaint();
    }
    

    private void addToCart(String category, MenuItem item, int quantity) {
        cartItemsByCategory.putIfAbsent(category, new ArrayList<>());
        List<CartItem> cartList = cartItemsByCategory.get(category);

        for (CartItem cartItem : cartList) {
            if (cartItem.name.equals(item.name)) {
                cartItem.quantity += quantity;
                updateTotal();
                return;
            }
        }

        cartList.add(new CartItem(item.name, item.price, quantity));
        updateTotal();
    }

    private void updateTotal() {
        totalAmount = 0;
        cartPanel.removeAll();

        Panel cartListPanel = new Panel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));

        for (String category : cartItemsByCategory.keySet()) {
            cartListPanel.add(new Label("Category: " + category));
            for (CartItem item : cartItemsByCategory.get(category)) {
                Label itemLabel = new Label(item.name + " x" + item.quantity + " = ₱" + (item.price * item.quantity));
                cartListPanel.add(itemLabel);
                totalAmount += item.price * item.quantity;
            }
        }

        totalLabel = new JLabel("Total: ₱" + totalAmount);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        cartPanel.add(new Label("Your Order:"), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartListPanel), BorderLayout.CENTER);
        cartPanel.add(totalLabel, BorderLayout.SOUTH);

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    public static void main(String[] args) {
        new OrderNow();
    }

    // Inner classes
    class MenuItem {
        String name;
        double price;
        String image;

        MenuItem(String name, double price, String image) {
            this.name = name;
            this.price = price;
            this.image = image;
        }
    }

    class CartItem {
        String name;
        double price;
        int quantity;

        CartItem(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
