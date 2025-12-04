package com.va.week13;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;

public class LottoApp extends Application {

    // ====== CHANGE THESE TO MATCH YOUR MYSQL ======
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lotto_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "sadaf";
    // ==============================================

    private static final int MAX_RUNS = 5; // up to 5 runs
    private final SecureRandom random = new SecureRandom();
    private final Hashtable<Integer, Integer> results = new Hashtable<>();

    private int runCount = 0;

    private TextArea logArea;
    private Label lastResultLabel;
    private Button generateBtn;

    @Override
    public void start(Stage stage) {
        lastResultLabel = new Label("Click the button to start lotto.");
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(10);

        generateBtn = new Button("Run Lotto (max 5 runs)");
        generateBtn.setOnAction(e -> startNewRun());

        VBox root = new VBox(10, generateBtn, lastResultLabel, logArea);
        root.setStyle("-fx-padding: 15; -fx-font-size: 14px;");

        Scene scene = new Scene(root, 420, 320);
        stage.setTitle("Lab 6 - Lotto App");
        stage.setScene(scene);
        stage.show();
    }

    private void startNewRun() {
        if (runCount >= MAX_RUNS) {
            lastResultLabel.setText("Max 5 runs reached.");
            return;
        }

        runCount++;
        int currentRun = runCount;

        Thread worker = new Thread(() -> {
            int num = random.nextInt(101); // 0–100

            results.put(currentRun, num);
            saveToDB(currentRun, num);

            Platform.runLater(() -> {
                lastResultLabel.setText("Run " + currentRun + ": " + num);
                logArea.appendText("Run " + currentRun + " -> " + num + "\n");

                if (currentRun == MAX_RUNS) {
                    showFinalResult();
                }
            });
        });

        worker.setDaemon(true);
        worker.start();
    }

    private void showFinalResult() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nFinal Result: [");
        for (int i = 1; i <= MAX_RUNS; i++) {
            Integer value = results.get(i);
            sb.append(value != null ? value : "-");
            if (i < MAX_RUNS) {
                sb.append(", ");
            }
        }
        sb.append("]\n");
        logArea.appendText(sb.toString());
    }

    private void saveToDB(int runNumber, int generatedNumber) {
        String sql = "INSERT INTO lotto_results (run_number, result) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, runNumber);
            ps.setInt(2, generatedNumber);   // MySQL will auto-convert INT to VARCHAR
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            Platform.runLater(() ->
                logArea.appendText("DB error: " + ex.getMessage() + "\n"));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
