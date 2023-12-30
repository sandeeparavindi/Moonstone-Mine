package lk.ijse.MoonstoneMine.controller;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.MoonstoneMine.model.CustomerModel;
import lk.ijse.MoonstoneMine.model.EmployeeModel;
import lk.ijse.MoonstoneMine.model.ItemModel;
import lk.ijse.MoonstoneMine.model.UserModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CashierFormController implements Initializable {

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label txtCustomerCount;

    @FXML
    private Label txtEmployeeCount;

    @FXML
    private Label txtIemCount;

    @FXML
    private Label txtUserCount;

    @FXML
    private AnchorPane pane;

    private static CashierFormController controller;

    public CashierFormController() {
        controller = this;
    }

    public static CashierFormController getInstance() {
        return controller;
    }

    private void loadDateandTime() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            lblDate.setText(now.format(dateFormat));
            lblTime.setText(now.format(timeFormat));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setValues() throws SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        EmployeeModel employeeModel = new EmployeeModel();
        CustomerModel customerModel = new CustomerModel();
        ItemModel itemModel = new ItemModel();
        UserModel userModel = new UserModel();

        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());
        loadDateandTime();
        try {
            setValues();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

        try {
            txtEmployeeCount.setText(employeeModel.totalEmployeeCount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            txtCustomerCount.setText(customerModel.totalCustomerCount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            txtIemCount.setText(itemModel.totalItemCount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            txtUserCount.setText(userModel.totalUserCount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml")));

     }
    @FXML
    void btnItemOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/item_form.fxml")));

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/placeOrder_form.fxml")));

    }

    @FXML
    void btnTicketOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/ticket_form.fxml")));
    }

    @FXML
    void btnDailyReportOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/dailyIncome_form.fxml")));
    }

    @FXML
    void btnBookingOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/ticketBooking_form.fxml")));

    }
    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                Stage stage = (Stage) pane.getScene().getWindow();
                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.centerOnScreen();
                stage.show();

            } else if (buttonType == buttonTypeNo) {
                alert.close();
            }
        });
    }

    @FXML
    void btnHomeOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/cashier_form.fxml"))));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnClickOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/barChart_form.fxml")));

    }
}