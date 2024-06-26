package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Animations.ButtonsPaneAnimation;
import sample.DataBaseHandler;
import sample.Order;
import sample.User;

import java.util.Arrays;
import java.util.List;
import java.sql.SQLException;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button adminButton;

    @FXML
    private AnchorPane buttonsPane;

    @FXML
    private Button employerButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button mainButton;

    @FXML
    private AnchorPane mainButtonPane;

    @FXML
    private AnchorPane mainPage;

    @FXML
    private Button orderButton;

    @FXML
    private AnchorPane orderButtonPane;

    @FXML
    private AnchorPane orderPage;

    @FXML
    private TableColumn<Order, String> order_carColumn;

    @FXML
    private Button order_clearButton;

    @FXML
    private TableColumn<Order, Integer> order_idColumn;

    @FXML
    private TableView<Order> order_ordersTable;

    @FXML
    private TableColumn<Order, String> order_problemColumn;

    @FXML
    private Button order_sendButton;

    @FXML
    private TableColumn<Order, String> order_statusColumn;

    @FXML
    private TextField order_textCarModel;

    @FXML
    private TextField order_textCarNumber;

    @FXML
    private DatePicker order_textDate;

    @FXML
    private TextArea order_textProblem;

    @FXML
    private ChoiceBox<String> order_textTime;

    @FXML
    private Button profileButton;

    @FXML
    private AnchorPane profileButtonPane;

    @FXML
    private Button profile_applyButton;

    @FXML
    private TextArea profile_dataText;

    @FXML
    private TextField profile_nameText;

    @FXML
    private TextField profile_surnameText;

    @FXML
    private AnchorPane profilePage;

    @FXML
    private Text welcomeText;

    @FXML
    void hideButtonsPane(MouseEvent event) { ButtonsPaneAnimation.moveRight(buttonsPane); }

    @FXML
    void showButtonsPane(MouseEvent event) { ButtonsPaneAnimation.moveLeft(buttonsPane); }

    public static User current_user;
    public static ObservableList<String> times = FXCollections.observableArrayList(Arrays.asList("09:00", "12:00", "15:00", "18:00", "21:00"));

    @FXML
    void initialize() {
        initSettings();

        welcomeText.setText(String.format("Добро пожаловать,\n%s!\nВы вошли как %s.",current_user.getFullName(), current_user.getRole()));
        exitButton.setOnAction(event -> onExitButtonClick());
        mainButton.setOnAction(event -> onMainButtonClick());
        orderButton.setOnAction(event -> { try { onOrderButtonClick(); } catch (SQLException throwables) { throwables.printStackTrace(); } });
        profileButton.setOnAction(event -> onProfileButtonClick());
        employerButton.setOnAction(event -> onEmployerButtonClick());
        adminButton.setOnAction(event -> onAdminButtonClick());

        order_clearButton.setOnAction(event -> order_onClearButtonClick());
        order_sendButton.setOnAction(event -> { try { order_onSendButtonClick(); } catch (SQLException e) { e.printStackTrace(); } });

        order_idColumn.setCellValueFactory(new PropertyValueFactory<Order,Integer>("id"));
        order_carColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("carModel"));
        order_problemColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("problemType"));
        order_statusColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("status"));

        profile_applyButton.setOnAction(event -> { try { profile_onApplyButtonClick(); } catch (SQLException e) { e.printStackTrace(); } });
    }

    // CONTROLS --------------------------------------------------------------------------------------------------------

    public void initSettings(){
        onMainButtonClick();
        current_user = LoginWindowController.current_user;
        buttonsPane.setLayoutX(-300);
    }

    public void hideAllPages(){
        mainPage.setVisible(false);
        orderPage.setVisible(false);
        profilePage.setVisible(false);
    }

    public void recolorAllButtons(){
        mainButtonPane.setStyle("-fx-background-color: #BDD5EA; -fx-background-radius: 25;");
        orderButtonPane.setStyle("-fx-background-color: #BDD5EA; -fx-background-radius: 25;");
        profileButtonPane.setStyle("-fx-background-color: #BDD5EA; -fx-background-radius: 25;");
        mainButton.setStyle("-fx-background-color: #495867; -fx-background-radius: 25;");
        orderButton.setStyle("-fx-background-color: #495867; -fx-background-radius: 25;");
        profileButton.setStyle("-fx-background-color: #495867; -fx-background-radius: 25;");
    }

    public void onExitButtonClick(){
        current_user = null;
        openNewScene("/sample/Templates/loginWindow.fxml");
    }

    public void onMainButtonClick(){
        hideAllPages();
        recolorAllButtons();
        mainPage.setVisible(true);
        mainButton.setStyle("-fx-background-color: #FE5F55; -fx-background-radius: 25;");
        mainButtonPane.setStyle("-fx-background-color: #a3bfd7; -fx-background-radius: 25;");
    }

    public void onEmployerButtonClick(){
        // Создание "сообщения"
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);

        if(!current_user.getRole().equals("employer") && !current_user.getRole().equals("admin")){
            alert.setContentText("Отказано в доступе!");
            alert.showAndWait();
            return;
        }
        openNewScene("/sample/Templates/employerWindow.fxml");
    }

    public void onAdminButtonClick(){
        // Создание "сообщения"
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);

        if(!current_user.getRole().equals("admin")){
            alert.setContentText("Отказано в доступе!");
            alert.showAndWait();
            return;
        }
        openNewScene("/sample/Templates/adminWindow.fxml");
    }

    public void onProfileButtonClick(){
        hideAllPages();
        recolorAllButtons();
        profilePage.setVisible(true);
        profileButton.setStyle("-fx-background-color: #FE5F55; -fx-background-radius: 25;");
        profileButtonPane.setStyle("-fx-background-color: #a3bfd7; -fx-background-radius: 25;");
        profile_init();
    }

    public void onOrderButtonClick() throws SQLException {
        hideAllPages();
        recolorAllButtons();
        orderPage.setVisible(true);
        orderButton.setStyle("-fx-background-color: #FE5F55; -fx-background-radius: 25;");
        orderButtonPane.setStyle("-fx-background-color: #a3bfd7; -fx-background-radius: 25;");
        order_init();
    }

    public void openNewScene(String window) {
        Stage currentStage = (Stage) exitButton.getScene().getWindow();
        currentStage.close();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    // ORDER PAGE ------------------------------------------------------------------------------------------------------

    public void order_init() throws SQLException {
        order_onClearButtonClick();
        order_tableUpdate();
    }

    public void order_tableUpdate() throws SQLException {
        List<Order> orders = new DataBaseHandler().getOrdersByUserId(current_user.getId());
        ObservableList<Order> data = FXCollections.observableArrayList(orders);
        order_ordersTable.setItems(data);
    }

    public void order_onClearButtonClick(){
        order_textCarModel.setText(null);
        order_textCarNumber.setText(null);
        order_textProblem.setText(null);
        order_textDate.setValue(null);
        order_textTime.setItems(times);
        order_textTime.setValue("Выберите время");
    }

    public void order_onSendButtonClick() throws SQLException {
        // Создание "сообщения"
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        // Проверки
        try {
            String carModel = order_textCarModel.getText();
            String carNumber = order_textCarNumber.getText();
            String problem = order_textProblem.getText();
            String date = order_textDate.getValue().toString();
            String time = order_textTime.getValue();

            // 1 - Не выбрано время
            if(time.equals("Выберите время")){
                alert.setContentText("Не выбрано время!");
                alert.showAndWait();
                return;
            }
            // 2 - Свободно время
            if (new DataBaseHandler().isDateTimeInTable(date, time)) {
                alert.setContentText("Выбранное время занято!\nПопробуйте другое.");
                alert.showAndWait();
                return;
            }
            alert.setContentText("Заявка создана! Ожидайте рассмотрения");
            alert.showAndWait();
            Order order = new Order(current_user.getId(),carModel,carNumber,problem,date,time,"Рассматривается");
            new DataBaseHandler().addOrder(order);

            order_tableUpdate();
            }
        // 3 - Заполнена заявка
        catch (NullPointerException e) {
            alert.setContentText("Заполните заявку полностью!");
            alert.showAndWait();
            return;
        }
    }

    // PROFILE PAGE ----------------------------------------------------------------------------------------------------

    public void profile_init(){
        profile_showInformation();
    }

    public void profile_showInformation(){
        profile_dataText.setText(String.format("Имя: %s\n\nФамилия: %s\n\nЛогин: %s\n\nРоль: %s\n",
                                 current_user.getName(),current_user.getSurname(),current_user.getLogin(),
                                 current_user.getRole()));
    }

    public void profile_onApplyButtonClick() throws SQLException {
        // Создание "сообщения"
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Информация обновлена!");
        alert.showAndWait();

        String name = profile_nameText.getText();
        if(name.equals("")) name = current_user.getName();
        String surname = profile_surnameText.getText();
        if(surname.equals("")) surname = current_user.getSurname();
        current_user.setName(name);
        current_user.setSurname(surname);
        new DataBaseHandler().updateUserById(current_user);
        profile_showInformation();
    }
}
