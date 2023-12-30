package lk.ijse.MoonstoneMine.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.MoonstoneMine.dto.ItemDto;
import lk.ijse.MoonstoneMine.dto.TicketDto;
import lk.ijse.MoonstoneMine.dto.tm.ItemTm;
import lk.ijse.MoonstoneMine.dto.tm.TicketTm;
import lk.ijse.MoonstoneMine.model.ItemModel;
import lk.ijse.MoonstoneMine.model.TicketModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class TicketFormController {
    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableView<TicketTm> tblTicket;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtType;

    private final TicketModel ticketModel = new TicketModel();

    public void initialize() {
        setCellValueFactory();
        loadAllTickets();
        setListener();
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void loadAllTickets() {
        try {
            List<TicketDto> dtoList = ticketModel.loadAllTickets();

            ObservableList<TicketTm> obList = FXCollections.observableArrayList();

            for(TicketDto dto : dtoList) {
                Button btn = new Button("remove");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tblTicket.getSelectionModel().getSelectedIndex();
                        String code = (String) colCode.getCellData(selectedIndex);

                        deleteTicket(code);
                        dtoList.remove(dto);

                        obList.remove(selectedIndex);
                        tblTicket.refresh();
                    }
                });

                var tm = new TicketTm(
                        dto.getCode(),
                        dto.getType(),
                        dto.getPrice(),
                        dto.getQtyOnHand(),
                        btn
                );
                obList.add(tm);
            }
            tblTicket.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setListener() {
        tblTicket.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new TicketDto(
                            newValue.getCode(),
                            newValue.getType(),
                            newValue.getPrice(),
                            newValue.getQtyOnHand()
                    );
                    setFields(dto);
                });
    }

    private void setFields(TicketDto dto) {
        txtCode.setText(dto.getCode());
        txtType.setText(dto.getType());
        txtPrice.setText(String.valueOf(dto.getPrice()));
        txtQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));
    }

    private void clearFields() {
        txtCode.setText("");
        txtType.setText("");
        txtPrice.setText("");
        txtQtyOnHand.setText("");
    }

    private void deleteTicket(String code) {
        try {
            boolean isDeleted = ticketModel.deleteTicket(code);
            if(isDeleted)
                new Alert(Alert.AlertType.CONFIRMATION, "ticket deleted!").show();
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        boolean isTicketCodeValidated = ValidateTicket();
        boolean isTicketTypeValidated = ValidateTicket();
        boolean isTicketPriceValidated = ValidateTicket();
        boolean isTicketCountValidated = ValidateTicket();

        if (isTicketCodeValidated && isTicketTypeValidated && isTicketPriceValidated && isTicketCountValidated) {

            String code = txtCode.getText();
            String type = txtType.getText();
            double Price = Double.parseDouble(txtPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

            var ticketDto = new TicketDto(code, type, Price, qtyOnHand);

            try {
                boolean isSaved = ticketModel.addTicket(ticketDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "ticket saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean ValidateTicket() {

        String codeText = txtCode.getText();
        boolean isTicketCodeValidated = Pattern.compile("[0-9]{3}").matcher(codeText).matches();
        if (!isTicketCodeValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid ticket code!").show();
            return false;
        }

        String typeText = txtType.getText();
        boolean isTicketTypeValidated = Pattern.compile("[A-z](.*)").matcher(typeText).matches();
        if (!isTicketTypeValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid ticket type!").show();
            return false;
        }

        String priceText = txtPrice.getText();
        boolean isTicketPriceValidated = Pattern.compile("[0-9](.*)").matcher(priceText).matches();
        if (!isTicketPriceValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid ticket price!").show();
            return false;
        }

        String countText = txtQtyOnHand.getText();
        boolean isTicketCountValidated = Pattern.compile("[0-9](.*)").matcher(countText).matches();
        if (!isTicketCountValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid ticket count!").show();
            return false;
        }
        return true;
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String type = txtType.getText();
        double Price = Double.parseDouble(txtPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

//        var model = new ItemModel();
        try {
            boolean isUpdated = ticketModel.updateTicket(new TicketDto(code, type, Price, qtyOnHand));
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Ticket updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSerachOnAction(ActionEvent event) {
        String code = txtCode.getText();

        try {
            TicketDto dto = ticketModel.searchTicket(code);
            if (dto != null) {
                setFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Ticket not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

}
