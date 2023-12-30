package lk.ijse.MoonstoneMine.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.MoonstoneMine.dto.CustomerDto;
import lk.ijse.MoonstoneMine.dto.tm.CustomerTm;
import lk.ijse.MoonstoneMine.model.CustomerModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerFormContoller {
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    public void initialize() {
        setCellValueFactory();
        loadAllCustomers();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllCustomers() {
        var model = new CustomerModel();

        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<CustomerDto> dtoList = model.getAllCustomers();

            for(CustomerDto dto : dtoList) {
                obList.add(
                        new CustomerTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getPhone(),
                                dto.getEmail()
                        )
                );
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        boolean isCustomerIDValidated = ValidateCustomer();
        boolean isCustomerNameValidated = ValidateCustomer();
        boolean isCustomerAddressValidated = ValidateCustomer();
        boolean isCustomerPhoneValidated = ValidateCustomer();
        boolean isCustomerEmailValidated = ValidateCustomer();

        if(isCustomerIDValidated && isCustomerNameValidated && isCustomerAddressValidated && isCustomerPhoneValidated && isCustomerEmailValidated) {

            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();

            var dto = new CustomerDto(id, name, address, phone, email);

            var model = new CustomerModel();
            try {
                boolean isSaved = model.addCustomer(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    private boolean ValidateCustomer() {

        String idText = txtId.getText();
        boolean isCustomerIDValidated = Pattern.compile("[C][0-9]{3}").matcher(idText).matches();
        if (!isCustomerIDValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid customer id!").show();
            return false;
        }

        String nameText = txtName.getText();
        boolean isCustomerNameValidated = Pattern.compile("[A-z](.*)").matcher(nameText).matches();
        if (!isCustomerNameValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid customer name!").show();
            return false;
        }

        String addressText = txtAddress.getText();
        boolean isCustomerAddressValidated = Pattern.compile("[A-z](.*)").matcher(addressText).matches();
        if (!isCustomerAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid customer address!").show();
            return false;
        }

        String phoneText = txtPhone.getText();
        boolean isCustomerPhoneValidated = Pattern.compile("[0-9]{10}").matcher(phoneText).matches();
        if (!isCustomerPhoneValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid customer Phone!").show();
            return false;
        }

        String emailText = txtEmail.getText();
        boolean isCustomerEmailValidated = Pattern.compile("[^A-Z]+(@gmail.com)").matcher(emailText).matches();
        if (!isCustomerEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid customer Email!").show();
            return false;
        }
        return true;
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        var customerModel = new CustomerModel();
        try {
            boolean isDeleted = customerModel.deleteCustomer(id);

            if(isDeleted) {
                tblCustomer.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        var dto = new CustomerDto(id, name, address, phone, email);

        var model = new CustomerModel();
        try {
            boolean isUpdated = model.updateCustomer(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    void txtSerachOnAction(ActionEvent event) {
        String id = txtId.getText();

        var model = new CustomerModel();
        try {
            CustomerDto dto = model.searchCustomer(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fillFields(CustomerDto dto) {
        txtId.setText(dto.getId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtPhone.setText(dto.getPhone());
        txtEmail.setText(dto.getEmail());
    }

    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }

}
