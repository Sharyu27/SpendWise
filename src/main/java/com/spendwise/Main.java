package com.spendwise;

import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.model.User;
import com.spendwise.service.AnalyticsService;
import com.spendwise.service.AuthService;
import com.spendwise.service.BudgetService;
import com.spendwise.service.DemoDataStore;
import com.spendwise.service.ReportService;
import com.spendwise.service.TransactionService;
import com.spendwise.util.DatabaseConnection;
import com.spendwise.util.MoneyUtil;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Main extends Application {
    private final DemoDataStore demoDataStore = new DemoDataStore();
    private final AuthService authService = new AuthService(demoDataStore);
    private final TransactionService transactionService = new TransactionService(demoDataStore);
    private final AnalyticsService analyticsService = new AnalyticsService();
    private final BudgetService budgetService = new BudgetService(analyticsService);
    private final ReportService reportService = new ReportService(analyticsService);

    private Stage stage;
    private User currentUser;
    private TableView<Transaction> transactionTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("SpendWise");
        showSplash();
        stage.show();
    }

    private void showSplash() {
        VBox content = new VBox(14);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("splash");

        Label logo = new Label("SpendWise");
        logo.getStyleClass().add("app-logo");
        Label tagline = new Label("Track Smarter. Spend Wiser. Save Better.");
        tagline.getStyleClass().add("tagline");
        Label loading = new Label("Loading your financial dashboard...");
        loading.getStyleClass().add("muted");

        content.getChildren().addAll(logo, tagline, loading);
        setScene(content, 980, 680);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.6));
        pause.setOnFinished(event -> showLogin());
        pause.play();
    }

    private void showLogin() {
        BorderPane root = authShell("Welcome back");
        VBox form = new VBox(12);
        form.getStyleClass().add("auth-card");

        TextField email = new TextField("demo@spendwise.app");
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setText("Demo1234");

        Button login = primaryButton("Login");
        Button register = secondaryButton("Create account");
        Label dbStatus = new Label(DatabaseConnection.isAvailable() ? "Connected to MySQL" : "Demo mode: MySQL not connected");
        dbStatus.getStyleClass().add("muted");

        login.setOnAction(event -> authService.login(email.getText(), password.getText())
                .ifPresentOrElse(user -> {
                    currentUser = user;
                    showDashboard();
                }, () -> showError("Login failed", "Incorrect email or password.")));
        register.setOnAction(event -> showRegister());

        form.getChildren().addAll(
                label("Email"), email,
                label("Password"), password,
                login, register, dbStatus
        );
        root.setCenter(form);
        setScene(root, 980, 680);
    }

    private void showRegister() {
        BorderPane root = authShell("Create your account");
        VBox form = new VBox(12);
        form.getStyleClass().add("auth-card");

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        TextField budget = new TextField("10000");
        budget.setPromptText("Monthly budget");

        Button create = primaryButton("Create account");
        Button back = secondaryButton("Back to login");

        create.setOnAction(event -> {
            try {
                BigDecimal monthlyBudget = new BigDecimal(budget.getText().trim());
                currentUser = authService.register(name.getText(), email.getText(), password.getText(), monthlyBudget);
                showDashboard();
            } catch (RuntimeException exception) {
                showError("Registration failed", exception.getMessage());
            }
        });
        back.setOnAction(event -> showLogin());

        form.getChildren().addAll(
                label("Name"), name,
                label("Email"), email,
                label("Password"), password,
                label("Monthly Budget"), budget,
                create, back
        );
        root.setCenter(form);
        setScene(root, 980, 680);
    }

    private BorderPane authShell(String title) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("auth-shell");
        VBox intro = new VBox(10);
        intro.setPadding(new Insets(42));
        intro.setAlignment(Pos.CENTER_LEFT);
        Label app = new Label("SpendWise");
        app.getStyleClass().add("app-logo");
        Label heading = new Label(title);
        heading.getStyleClass().add("hero-title");
        Label copy = new Label("Remember what you purchased, not just where you paid.");
        copy.getStyleClass().add("hero-copy");
        intro.getChildren().addAll(app, heading, copy);
        root.setLeft(intro);
        BorderPane.setMargin(intro, new Insets(0, 24, 0, 0));
        return root;
    }

    private void showDashboard() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-shell");

        HBox header = new HBox(16);
        header.setPadding(new Insets(20, 24, 10, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Hello, " + currentUser.getName());
        title.getStyleClass().add("page-title");
        Label month = new Label(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + LocalDate.now().getYear());
        month.getStyleClass().add("muted");
        Button logout = secondaryButton("Logout");
        logout.setOnAction(event -> showLogin());
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(title, month, spacer, logout);

        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
                tab("Dashboard", dashboardView()),
                tab("Add Expense", addExpenseView()),
                tab("Add Income", addIncomeView()),
                tab("History", historyView()),
                tab("Budget", budgetView()),
                tab("Analytics", analyticsView()),
                tab("Monthly Report", reportView())
        );
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        root.setTop(header);
        root.setCenter(tabs);
        setScene(root, 1180, 760);
    }

    private VBox dashboardView() {
        List<Transaction> transactions = transactionService.getTransactions(currentUser.getUserId());
        BigDecimal income = analyticsService.totalIncome(transactions);
        BigDecimal expenses = analyticsService.totalExpenses(transactions);
        BigDecimal balance = income.subtract(expenses);
        BigDecimal remainingBudget = budgetService.remainingBudget(currentUser.getMonthlyBudget(), transactions);
        BigDecimal safeToday = budgetService.safeDailyLimit(currentUser.getMonthlyBudget(), transactions);
        int score = reportService.financialScore(currentUser.getMonthlyBudget(), transactions);

        VBox view = page();
        HBox cards = new HBox(14);
        cards.getChildren().addAll(
                statCard("Balance", MoneyUtil.format(balance)),
                statCard("Income", MoneyUtil.format(income)),
                statCard("Expenses", MoneyUtil.format(expenses)),
                statCard("Budget left", MoneyUtil.format(remainingBudget)),
                statCard("Safe today", MoneyUtil.format(safeToday)),
                statCard("Score", score + " / 100")
        );

        Label insight = new Label(smartInsight(transactions));
        insight.getStyleClass().add("insight");

        TableView<Transaction> recent = transactionTable(transactionService.recentTransactions(currentUser.getUserId(), 6));
        recent.setPrefHeight(250);

        view.getChildren().addAll(sectionTitle("Overview"), cards, sectionTitle("Smart Insight"), insight, sectionTitle("Recent Transactions"), recent);
        return view;
    }

    private VBox addExpenseView() {
        VBox view = page();
        GridPane form = formGrid();
        TextField amount = new TextField();
        TextField merchant = new TextField();
        TextField item = new TextField();
        ComboBox<String> category = combo("Food", "Travel", "Shopping", "Education", "Groceries", "Bills", "Health", "Other");
        ComboBox<String> payment = combo("UPI", "Cash", "Card", "Bank Transfer");
        DatePicker date = new DatePicker(LocalDate.now());
        TextArea notes = new TextArea();
        notes.setPrefRowCount(3);
        Button save = primaryButton("Save Expense");

        addRow(form, 0, "Amount", amount);
        addRow(form, 1, "Merchant", merchant);
        addRow(form, 2, "Item Purchased", item);
        addRow(form, 3, "Category", category);
        addRow(form, 4, "Payment Method", payment);
        addRow(form, 5, "Date", date);
        addRow(form, 6, "Notes", notes);

        save.setOnAction(event -> {
            try {
                transactionService.addExpense(
                        currentUser.getUserId(),
                        new BigDecimal(amount.getText().trim()),
                        merchant.getText(),
                        item.getText(),
                        category.getValue(),
                        payment.getValue(),
                        date.getValue(),
                        notes.getText()
                );
                showInfo("Expense saved", "Your item-level expense was recorded.");
                showDashboard();
            } catch (RuntimeException exception) {
                showError("Could not save expense", exception.getMessage());
            }
        });

        view.getChildren().addAll(sectionTitle("Add Expense"), form, save);
        return view;
    }

    private VBox addIncomeView() {
        VBox view = page();
        GridPane form = formGrid();
        TextField amount = new TextField();
        TextField source = new TextField();
        DatePicker date = new DatePicker(LocalDate.now());
        TextArea notes = new TextArea();
        notes.setPrefRowCount(3);
        Button save = primaryButton("Save Income");

        addRow(form, 0, "Amount", amount);
        addRow(form, 1, "Source", source);
        addRow(form, 2, "Date", date);
        addRow(form, 3, "Notes", notes);

        save.setOnAction(event -> {
            try {
                transactionService.addIncome(currentUser.getUserId(), new BigDecimal(amount.getText().trim()), source.getText(), date.getValue(), notes.getText());
                showInfo("Income saved", "Your income was recorded.");
                showDashboard();
            } catch (RuntimeException exception) {
                showError("Could not save income", exception.getMessage());
            }
        });

        view.getChildren().addAll(sectionTitle("Add Income"), form, save);
        return view;
    }

    private VBox historyView() {
        VBox view = page();
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        TextField search = new TextField();
        search.setPromptText("Search merchant, item, category, notes");
        TextField amountSearch = new TextField();
        amountSearch.setPromptText("Binary search amount");
        Button searchButton = secondaryButton("Search");
        Button amountButton = secondaryButton("Search Amount");
        Button undo = secondaryButton("Undo Delete");
        controls.getChildren().addAll(search, searchButton, amountSearch, amountButton, undo);

        transactionTable = transactionTable(transactionService.getTransactions(currentUser.getUserId()));
        transactionTable.setPrefHeight(430);

        Button delete = primaryButton("Delete Selected");
        delete.setOnAction(event -> {
            Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                transactionService.delete(selected);
                transactionTable.setItems(FXCollections.observableArrayList(transactionService.getTransactions(currentUser.getUserId())));
            }
        });
        searchButton.setOnAction(event -> transactionTable.setItems(FXCollections.observableArrayList(transactionService.searchByText(currentUser.getUserId(), search.getText()))));
        amountButton.setOnAction(event -> {
            try {
                transactionTable.setItems(FXCollections.observableArrayList(transactionService.searchByAmount(currentUser.getUserId(), new BigDecimal(amountSearch.getText().trim()))));
            } catch (RuntimeException exception) {
                showError("Invalid amount", "Enter a valid amount to search.");
            }
        });
        undo.setOnAction(event -> {
            transactionService.undoLastDelete();
            transactionTable.setItems(FXCollections.observableArrayList(transactionService.getTransactions(currentUser.getUserId())));
        });

        view.getChildren().addAll(sectionTitle("Transaction History"), controls, transactionTable, delete);
        return view;
    }

    private VBox budgetView() {
        List<Transaction> transactions = transactionService.getTransactions(currentUser.getUserId());
        BigDecimal remaining = budgetService.remainingBudget(currentUser.getMonthlyBudget(), transactions);
        BigDecimal safeToday = budgetService.safeDailyLimit(currentUser.getMonthlyBudget(), transactions);
        double progress = analyticsService.totalExpenses(transactions).divide(currentUser.getMonthlyBudget(), 4, java.math.RoundingMode.HALF_UP).doubleValue();

        VBox view = page();
        view.getChildren().addAll(
                sectionTitle("Budget Planner"),
                statCard("Monthly Budget", MoneyUtil.format(currentUser.getMonthlyBudget())),
                labeledProgress("Budget used", Math.min(progress, 1.0)),
                new Label("Remaining budget: " + MoneyUtil.format(remaining)),
                new Label("Safe spending limit today: " + MoneyUtil.format(safeToday)),
                new Separator(),
                sectionTitle("Category Budgets"),
                new Label("Food: Rs.2400 / Rs.3000"),
                labeledProgress("Food", 0.8),
                new Label("Travel: Rs.1100 / Rs.1500"),
                labeledProgress("Travel", 0.73),
                new Label("Shopping: Rs.2650 / Rs.2500"),
                labeledProgress("Shopping", 1.0)
        );
        return view;
    }

    private VBox analyticsView() {
        List<Transaction> transactions = transactionService.getTransactions(currentUser.getUserId());
        Map<String, BigDecimal> totals = analyticsService.categoryTotals(transactions);

        PieChart pieChart = new PieChart();
        totals.forEach((category, amount) -> pieChart.getData().add(new PieChart.Data(category, amount.doubleValue())));
        pieChart.setTitle("Category Distribution");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Monthly Spending");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");
        series.getData().add(new XYChart.Data<>("Feb", 5200));
        series.getData().add(new XYChart.Data<>("Mar", 6100));
        series.getData().add(new XYChart.Data<>("Apr", 5400));
        series.getData().add(new XYChart.Data<>("May", 6900));
        series.getData().add(new XYChart.Data<>("Jun", 6400));
        series.getData().add(new XYChart.Data<>("Jul", analyticsService.totalExpenses(transactions)));
        barChart.getData().add(series);

        VBox topCategories = new VBox(8);
        topCategories.getChildren().add(sectionTitle("Top Spending Categories"));
        for (Map.Entry<String, BigDecimal> entry : analyticsService.topCategories(transactions, 3)) {
            topCategories.getChildren().add(new Label(entry.getKey() + ": " + MoneyUtil.format(entry.getValue())));
        }

        VBox view = page();
        HBox charts = new HBox(18, pieChart, barChart);
        charts.getStyleClass().add("chart-row");
        view.getChildren().addAll(sectionTitle("Analytics"), charts, topCategories, new Label(analyticsService.weekendInsight(transactions)));
        return view;
    }

    private VBox reportView() {
        List<Transaction> transactions = transactionService.getTransactions(currentUser.getUserId());
        BigDecimal income = analyticsService.totalIncome(transactions);
        BigDecimal expenses = analyticsService.totalExpenses(transactions);
        BigDecimal savings = income.subtract(expenses);
        int score = reportService.financialScore(currentUser.getMonthlyBudget(), transactions);

        VBox view = page();
        view.getChildren().addAll(
                sectionTitle("Monthly Report"),
                statCard("Income", MoneyUtil.format(income)),
                statCard("Expenses", MoneyUtil.format(expenses)),
                statCard("Savings", MoneyUtil.format(savings)),
                statCard("Financial Score", score + " / 100"),
                sectionTitle("Expense Memory"),
                paragraph(reportService.expenseMemory(transactions))
        );
        return view;
    }

    private TableView<Transaction> transactionTable(List<Transaction> transactions) {
        TableView<Transaction> table = new TableView<>(FXCollections.observableArrayList(transactions));
        TableColumn<Transaction, String> date = column("Date", transaction -> transaction.getTransactionDate().toString());
        TableColumn<Transaction, String> type = column("Type", transaction -> transaction.getType().name());
        TableColumn<Transaction, String> item = column("Item / Source", transaction -> transaction.getType() == TransactionType.EXPENSE ? transaction.getItemName() : transaction.getMerchant());
        TableColumn<Transaction, String> merchant = column("Merchant", Transaction::getMerchant);
        TableColumn<Transaction, String> category = column("Category", Transaction::getCategory);
        TableColumn<Transaction, String> amount = column("Amount", transaction -> MoneyUtil.format(transaction.getAmount()));
        table.getColumns().setAll(List.of(date, type, item, merchant, category, amount));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return table;
    }

    private TableColumn<Transaction, String> column(String title, java.util.function.Function<Transaction, String> mapper) {
        TableColumn<Transaction, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(mapper.apply(data.getValue())));
        return column;
    }

    private String smartInsight(List<Transaction> transactions) {
        List<Map.Entry<String, BigDecimal>> top = analyticsService.topCategories(transactions, 1);
        if (top.isEmpty()) {
            return "Add expenses to unlock spending insights.";
        }
        Map.Entry<String, BigDecimal> highest = top.get(0);
        return "Highest spending category: %s at %s. Keep today's spending below %s to stay on track."
                .formatted(highest.getKey(), MoneyUtil.format(highest.getValue()), MoneyUtil.format(budgetService.safeDailyLimit(currentUser.getMonthlyBudget(), transactions)));
    }

    private Tab tab(String title, VBox content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll");
        return new Tab(title, scrollPane);
    }

    private VBox page() {
        VBox page = new VBox(18);
        page.setPadding(new Insets(24));
        page.getStyleClass().add("page");
        return page;
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private Label paragraph(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("paragraph");
        return label;
    }

    private VBox statCard(String title, String value) {
        VBox card = new VBox(8);
        card.getStyleClass().add("stat-card");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private HBox labeledProgress(String title, double progress) {
        Label label = new Label(title);
        ProgressBar bar = new ProgressBar(progress);
        bar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(bar, Priority.ALWAYS);
        return new HBox(12, label, bar);
    }

    private GridPane formGrid() {
        GridPane form = new GridPane();
        form.setHgap(14);
        form.setVgap(12);
        form.getStyleClass().add("form-grid");
        return form;
    }

    private void addRow(GridPane form, int row, String label, javafx.scene.Node input) {
        form.add(label(label), 0, row);
        form.add(input, 1, row);
    }

    private Label label(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }

    private ComboBox<String> combo(String... values) {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(values));
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

    private Button primaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-button");
        return button;
    }

    private Button secondaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        return button;
    }

    private void setScene(javafx.scene.Parent root, int width, int height) {
        Scene scene = new Scene(new StackPane(root), width, height);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

