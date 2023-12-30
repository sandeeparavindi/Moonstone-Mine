package lk.ijse.MoonstoneMine.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.MoonstoneMine.dto.ItemDto;
import lk.ijse.MoonstoneMine.dto.tm.ItemTm;
import lk.ijse.MoonstoneMine.model.ItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ItemFormController {
    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<ItemTm> tblItem;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;

    private final ItemModel itemModel = new ItemModel();

    public void initialize() {
        setCellValueFactory();
        loadAllItems();
        setListener();
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void loadAllItems() {
        try {
            List<ItemDto> dtoList = itemModel.loadAllItems();

            ObservableList<ItemTm> obList = FXCollections.observableArrayList();

            for(ItemDto dto : dtoList) {
                Button btn = new Button("remove");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = tblItem.getSelectionModel().getSelectedIndex();
                        String code = (String) colCode.getCellData(selectedIndex);

                        deleteItem(code);
                        dtoList.remove(dto);

                        obList.remove(selectedIndex);
                        tblItem.refresh();
                    }
                });

                var tm = new ItemTm(
                        dto.getCode(),
                        dto.getDescription(),
                        dto.getUnitPrice(),
                        dto.getQtyOnHand(),
                        btn
                );
                obList.add(tm);
            }
            tblItem.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setListener() {
        tblItem.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new ItemDto(
                            newValue.getCode(),
                            newValue.getDescription(),
                            newValue.getUnitPrice(),
                            newValue.getQtyOnHand()
                    );
                    setFields(dto);
                });
    }

    private void setFields(ItemDto dto) {
        txtCode.setText(dto.getCode());
        txtDescription.setText(dto.getDescription());
        txtUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));
    }

    private void clearFields() {
        txtCode.setText("");
        txtDescription.setText("");
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
    }

    private void deleteItem(String code) {
        try {
            boolean isDeleted = itemModel.deleteItem(code);
            if(isDeleted)
                new Alert(Alert.AlertType.CONFIRMATION, "item deleted!").show();
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        boolean isItemCodeValidated = ValidateItem();
        boolean isItemDescriptionValidated = ValidateItem();
        boolean isItemPriceValidated = ValidateItem();
        boolean isItemCountValidated = ValidateItem();

        if (isItemCodeValidated && isItemDescriptionValidated && isItemPriceValidated && isItemCountValidated) {

            String code = txtCode.getText();
            String description = txtDescription.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

            var itemDto = new ItemDto(code, description, unitPrice, qtyOnHand);

            try {
                boolean isSaved = itemModel.addItem(itemDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "item saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    private boolean ValidateItem() {

        String codeText = txtCode.getText();
        boolean isItemCodeValidated = Pattern.compile("[0-9]{3}").matcher(codeText).matches();
        if (!isItemCodeValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid item code!").show();
            return false;
        }

        String descriptionText = txtDescription.getText();
        boolean isItemDescriptionValidated = Pattern.compile("[A-z](.*)").matcher(descriptionText).matches();
        if (!isItemDescriptionValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid item description!").show();
            return false;
        }

        String priceText = txtUnitPrice.getText();
        boolean isItemPriceValidated = Pattern.compile("[0-9](.*)").matcher(priceText).matches();
        if (!isItemPriceValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid item price!").show();
            return false;
        }

        String countText = txtQtyOnHand.getText();
        boolean isItemCountValidated = Pattern.compile("[0-9](.*)").matcher(countText).matches();
        if (!isItemCountValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid item count!").show();
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
        String description = txtDescription.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

//        var model = new ItemModel();
        try {
            boolean isUpdated = itemModel.updateItem(new ItemDto(code, description, unitPrice, qtyOnHand));
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "item updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void codeSerachOnAction(ActionEvent event) {
        String code = txtCode.getText();

        try {
            ItemDto dto = itemModel.searchItem(code);
            if (dto != null) {
                setFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "item not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
