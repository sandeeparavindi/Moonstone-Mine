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
import lk.ijse.MoonstoneMine.dto.CustomerDto;
import lk.ijse.MoonstoneMine.dto.EmployeeDto;
import lk.ijse.MoonstoneMine.dto.tm.EmployeeTm;
import lk.ijse.MoonstoneMine.model.CustomerModel;
import lk.ijse.MoonstoneMine.model.EmployeeModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeFormContoller {
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
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;
    public void initialize() {
        setCellValueFactory();
        loadAllEmployees();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllEmployees() {
        var model = new EmployeeModel();

        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> dtoList = model.getAllEmployees();

            for(EmployeeDto dto : dtoList) {
                obList.add(
                        new EmployeeTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getPhone(),
                                dto.getEmail()
                        )
                );
            }

            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        boolean isEmployeeIDValidated = ValidateEmployee();
        boolean isEmployeeNameValidated = ValidateEmployee();
        boolean isEmployeeAddressValidated = ValidateEmployee();
        boolean isEmployeePhoneValidated = ValidateEmployee();
        boolean isEmployeeEmailValidated = ValidateEmployee();

        if(isEmployeeIDValidated && isEmployeeNameValidated && isEmployeeAddressValidated && isEmployeePhoneValidated && isEmployeeEmailValidated) {

            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();

            var dto = new EmployeeDto(id, name, address, phone, email);

            var model = new EmployeeModel();
            try {
                boolean isSaved = model.addEmployee(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "employee saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    private void fillFields(EmployeeDto dto) {
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
    private boolean ValidateEmployee() {

        String idText = txtId.getText();
        boolean isEmployeeIDValidated = Pattern.compile("[E][0-9]{3}").matcher(idText).matches();
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee id!").show();
            return false;
        }

        String nameText = txtName.getText();
        boolean isEmployeeNameValidated = Pattern.compile("[A-z](.*)").matcher(nameText).matches();
        if (!isEmployeeNameValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee name!").show();
            return false;
        }

        String addressText = txtAddress.getText();
        boolean isEmployeeAddressValidated = Pattern.compile("[A-z](.*)").matcher(addressText).matches();
        if (!isEmployeeAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee address!").show();
            return false;
        }

        String phoneText = txtPhone.getText();
        boolean isEmployeePhoneValidated = Pattern.compile("[0-9]{10}").matcher(phoneText).matches();
        if (!isEmployeePhoneValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee Phone!").show();
            return false;
        }

        String emailText = txtEmail.getText();
        boolean isEmployeeEmailValidated = Pattern.compile("[^A-Z]+(@gmail.com)").matcher(emailText).matches();
        if (!isEmployeeEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid employee Email!").show();
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

        var employeeModel = new EmployeeModel();
        try {
            boolean isDeleted = employeeModel.deleteEmployee(id);

            if(isDeleted) {
                tblEmployee.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "employee deleted!").show();
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

        var dto = new EmployeeDto(id, name, address, phone, email);

        var model = new EmployeeModel();
        try {
            boolean isUpdated = model.updateEmployee(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSerachOnAction(ActionEvent event) {
        String id = txtId.getText();

        var model = new EmployeeModel();
        try {
            EmployeeDto dto = model.searchEmployee(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
