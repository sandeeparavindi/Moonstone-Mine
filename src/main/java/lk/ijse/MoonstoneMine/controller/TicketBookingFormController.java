package lk.ijse.MoonstoneMine.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.MoonstoneMine.dto.*;
import lk.ijse.MoonstoneMine.dto.tm.CartTm;
import lk.ijse.MoonstoneMine.dto.tm.TicketCartTm;
import lk.ijse.MoonstoneMine.model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketBookingFormController {
    private final CustomerModel customerModel = new CustomerModel();
    private final TicketModel ticketModel = new TicketModel();
    private final BookingModel bookingModel = new BookingModel();
    private final ObservableList<TicketCartTm> obList = FXCollections.observableArrayList();

    @FXML
    private JFXButton btnAddToCart;

    @FXML
    private JFXComboBox<String> cmbCustomerId;

    @FXML
    private JFXComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTicketCode;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private Label lblBookingDate;

    @FXML
    private Label lblBookingId;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblType;

    @FXML
    private TableView<TicketCartTm> tblBookingCart;

    @FXML
    private TextField txtQty;

    private final TicketBookingModel ticketBookingModel = new TicketBookingModel();

    public void initialize() {
        setCellValueFactory();
        generateNextBookingId();
        setDate();
        loadCustomerIds();
        loadTicketCodes();
    }

    private void setCellValueFactory() {
        colTicketCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("tot"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void generateNextBookingId() {
        try {
            String bookingId = bookingModel.generateNextBookingId();
            lblBookingId.setText(bookingId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTicketCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<TicketDto> ticketList = ticketModel.loadAllTickets();

            for (TicketDto ticketDto : ticketList) {
                obList.add(ticketDto.getCode());
            }

            cmbItemCode.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<CustomerDto> cusList = customerModel.loadAllCustomers();

            for (CustomerDto dto : cusList) {
                obList.add(dto.getId());
            }
            cmbCustomerId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());
        lblOrderDate.setText(date);
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String code = cmbItemCode.getValue();
        String type = lblType.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double Price = Double.parseDouble(lblPrice.getText());
        double total = qty * Price;
        Button btn = new Button("remove");
        btn.setCursor(Cursor.HAND);

        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> types = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (types.orElse(no) == yes) {
                int index = tblBookingCart.getSelectionModel().getSelectedIndex();
                obList.remove(index);
                tblBookingCart.refresh();

                calculateNetTotal();
            }

        });

        for (int i = 0; i < tblBookingCart.getItems().size(); i++) {
            if (code.equals(colTicketCode.getCellData(i))) {
                qty += (int) colQty.getCellData(i);
                total = qty * Price;

                obList.get(i).setQty(qty);
                obList.get(i).setTot(total);

                tblBookingCart.refresh();
                calculateNetTotal();
                return;
            }
        }

        obList.add(new TicketCartTm(
                code,
                type,
                qty,
                Price,
                total,
                btn
        ));

        tblBookingCart.setItems(obList);
        calculateNetTotal();
        txtQty.clear();
    }
    private void calculateNetTotal() {
        double total = 0;
        for (int i = 0; i < tblBookingCart.getItems().size(); i++) {
            total += (double) colTotal.getCellData(i);
        }

        lblNetTotal.setText(String.valueOf(total));
    }


    @FXML
    void btnNewCustomerOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Customer Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnPlaceBookingOnAction(ActionEvent event) {
        String bookingId = lblBookingId.getText();
        String cusId = cmbCustomerId.getValue();
        LocalDate date = LocalDate.parse(lblOrderDate.getText());

        List<TicketCartTm> tmList = new ArrayList<>();

        for (TicketCartTm cartTm : obList) {
            tmList.add(cartTm);
        }

        var ticketBookingDto = new TicketBookingDto(
                bookingId,
                cusId,
                date,
                tmList
        );
       // ObservableList<TicketCartTm> data = tblBookingCart.getItems();
        try {
            boolean isSuccess = ticketBookingModel.ticketBooking(ticketBookingDto);
            if(isSuccess) {
                new Alert(Alert.AlertType.CONFIRMATION, "booking completed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void cmbCustomerOnAction(ActionEvent event) throws SQLException {
        String id = cmbCustomerId.getValue();
        CustomerDto dto = customerModel.searchCustomer(id);

        lblCustomerName.setText(dto.getName());
    }

    @FXML
    void cmbItemOnAction(ActionEvent event) {
        String code = cmbItemCode.getValue();

        txtQty.requestFocus();

        try {
            TicketDto dto = ticketModel.searchTicket(code);

            lblType.setText(dto.getType());
            lblPrice.setText(String.valueOf(dto.getPrice()));
            lblQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddToCartOnAction(event);
    }
}
