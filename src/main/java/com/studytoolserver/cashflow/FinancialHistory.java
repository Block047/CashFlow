package com.studytoolserver.cashflow;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class FinancialHistory {

    @FXML
    private VBox financialHistory;
    @FXML
    private Text balance;

    public void updateMoney() {
        System.out.println("balance node: " + balance);
        System.out.println("total: " + FinancialData.getTotalCashFlow());
        balance.setText(String.format("$%,.2f", FinancialData.getTotalCashFlow()));
    }

    public void updateTransactions(){
        if (!FinancialData.getTransactions().isEmpty()) {
            financialHistory.getChildren().remove(2);
        }
        for (Transaction t : FinancialData.getTransactions()) {
            Region spacer = new Region();
            spacer.setPrefHeight(11.0);
            financialHistory.getChildren().add(createTransactionRow(t.getAmount(), t.getDescription(), t.getDate()));
            financialHistory.getChildren().add(spacer);
        }
    }

    private HBox createTransactionRow(Double moneyChange, String reasonCost, Long date) {

        HBox row = new HBox();
        row.setPrefHeight(88.0);
        row.setMinHeight(88.0);
        row.setPrefWidth(652.0);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setUserData(date);
        row.setStyle("-fx-border-color: black; -fx-background-radius: 16; -fx-border-radius: 16;");

        Text moneyChangeText = new Text(String.format("$%,.2f", moneyChange));
        if (moneyChange > 0) {
            moneyChangeText.setStyle("-fx-fill: #1b9c1b;");
        } else if (moneyChange < 0) {
            moneyChangeText.setStyle("-fx-fill: #e13b12;");
        } else {
            moneyChangeText.setStyle("-fx-fill: black;");
        }
        moneyChangeText.setStrokeType(StrokeType.OUTSIDE);
        moneyChangeText.setStrokeWidth(0.0);
        moneyChangeText.setTextAlignment(TextAlignment.CENTER);
        moneyChangeText.setWrappingWidth(176.5703125);
        moneyChangeText.setFont(Font.font("Inter Regular", 24.0));

        Region spacer1 = new Region();
        spacer1.setPrefHeight(30.0);
        spacer1.setPrefWidth(25.0);

        Text reasonCostText = new Text(reasonCost);
        reasonCostText.setStrokeType(StrokeType.OUTSIDE);
        reasonCostText.setStrokeWidth(0.0);
        reasonCostText.setTextAlignment(TextAlignment.CENTER);
        reasonCostText.setWrappingWidth(275.4375);
        reasonCostText.setFont(Font.font("Inter Regular", 16.0));

        Region spacer2 = new Region();
        spacer2.setPrefHeight(40.0);
        spacer2.setPrefWidth(28.0);

        VBox rightBox = new VBox();
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPrefHeight(157.0);
        rightBox.setPrefWidth(97.0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy")
                .withZone(ZoneId.systemDefault());

        String formattedDate = formatter.format(Instant.ofEpochMilli(date));
        Text dateText = new Text(formattedDate);

        Region spacer3 = new Region();
        spacer3.setPrefHeight(5.0);
        spacer3.setPrefWidth(97.0);

        Button revertBtn = new Button("Revert");
        revertBtn.setPrefWidth(62.0);
        revertBtn.setMnemonicParsing(false);
        revertBtn.setStyle("-fx-background-color: #e13b12;");
        revertBtn.setOnMouseClicked(this::handleRevert);

        rightBox.getChildren().addAll(dateText, spacer3, revertBtn);

        row.getChildren().addAll(moneyChangeText, spacer1, reasonCostText, spacer2, rightBox);

        return row;
    }

    public void handleNew() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Transaction");
        dialog.setHeaderText("Enter the details of the new transaction.");
        VBox content = new VBox(10);
        TextField amt = new TextField();
        amt.setPromptText("Amount (positive for income, negative for expense)");
        TextField reason = new TextField();
        reason.setPromptText("Reason and purpose of transaction");
        content.getChildren().addAll(amt, reason);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            if (Double.isInfinite(Double.parseDouble(amt.getText()))){
                Dialog<ButtonType> warn = new Dialog<>();
                warn.setTitle("This number is too high!");
                warn.setTitle("Please try a lower number");
                return;
            }

            if (Double.parseDouble(amt.getText()) < 0) {
                Dialog<ButtonType> confirm = new Dialog<>();
                confirm.setTitle("Are you sure you want to make this transaction?");
                confirm.setHeaderText("This transaction will cost you " + String.format("$%,.2f", Double.parseDouble(amt.getText())));
                confirm.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                Optional<ButtonType> confirmResult = confirm.showAndWait();
                if (confirmResult.isEmpty() || confirmResult.get() != ButtonType.OK) {
                    return;
                }
            }
            if (Objects.equals(financialHistory.getChildren().get(2).getId(), "filler")) {
                financialHistory.getChildren().remove(2);
            }
            Region spacer = new Region();
            spacer.setPrefHeight(11.0);

            financialHistory.getChildren().add(financialHistory.getChildren().size(), createTransactionRow(Double.parseDouble(amt.getText()), reason.getText(), System.currentTimeMillis()));
            financialHistory.getChildren().add(financialHistory.getChildren().size(), spacer);
            try {
                new Transaction(Double.parseDouble(amt.getText()), reason.getText(), System.currentTimeMillis());
                updateMoney();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void handleEnter(MouseEvent e){
        Button b = (Button) e.getSource();
        b.setUserData(b.getStyle());
        Color color = (Color) b.getBackground().getFills().getFirst().getFill();
        Color darker = Color.hsb(
                color.getHue(),
                color.getSaturation(),
                color.getBrightness() * 0.7
        );

        String hex = String.format("#%02X%02X%02X",
                (int)(darker.getRed() * 255),
                (int)(darker.getGreen() * 255),
                (int)(darker.getBlue() * 255)
        );

        b.setStyle("-fx-background-color: " + hex + ";");
    }

    public void handleExit(MouseEvent e){
        Button b = (Button) e.getSource();
        if (b.getUserData() != null) {
            b.setStyle((String) b.getUserData());
        }
    }

    public void handleRevert(MouseEvent e) {
        Button button = (Button) e.getSource();
        HBox record = (HBox) button.getParent().getParent();
        VBox container = (VBox) button.getParent().getParent().getParent();
        container.getChildren().remove(record);
        try {
            FinancialData.removeTransactionByDate((Long) record.getUserData());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        updateMoney();
    }
}
