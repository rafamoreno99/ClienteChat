package com.rafamoreno.clientechat;

import com.rafamoreno.clientechat.models.ConexionBD;
import com.rafamoreno.clientechat.models.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private Button botonLogin;

    @FXML
    private Button botonRegistro;

    @FXML
    private Button botonVolverInicio;

    @FXML
    private CheckBox cbVerContrasenia;

    @FXML
    private CheckBox cbVerContraseniaLogin;

    @FXML
    private VBox panelChat;

    @FXML
    private VBox panelInicio;

    @FXML
    private VBox panelRegistro;

    @FXML
    private Text tfBienvenido;

    @FXML
    private TextField tfContraseniaLogin;

    @FXML
    private PasswordField tfContraseniaLoginPW;

    @FXML
    private TextField tfContraseniaRegistro;

    @FXML
    private PasswordField tfContraseniaRegistroPW;

    @FXML
    private Text tfIniciaSesion;

    @FXML
    private TextField tfNombreLogin;

    @FXML
    private TextField tfNombreRegistro;

    @FXML
    private Text tfRegistro;

    private ConexionBD conexionBD = new ConexionBD();

    private Usuario usuarioActual = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void cambiaPanelLogin(MouseEvent event) {
        panelInicio.setVisible(true);
        panelRegistro.setVisible(false);
        tfNombreLogin.setText("");
        tfContraseniaLogin.setText("");
    }

    @FXML
    void cambiaPanelRegistro(MouseEvent event) {
        panelInicio.setVisible(false);
        panelRegistro.setVisible(true);
        tfContraseniaRegistro.setText("");
        tfNombreRegistro.setText("");
    }

    @FXML
    void loginAplicacion(ActionEvent event) {
        int id_usuario = 0;

        boolean existe_usuario = false;
        if(compruebaTfLogin()){
            muestraPanel("Error al registrar nuevo usuario","Debe rellenar ambos campos de texto");
            return;
        }
        try{
            ResultSet rsUsuarios = conexionBD.conectar().createStatement().executeQuery("SELECT id_usuario, nombre, contrasenia FROM USUARIOS");
            while(rsUsuarios.next()){
                if(tfNombreLogin.getText().equals(rsUsuarios.getString("nombre"))){
                    existe_usuario = true;
                    if(tfContraseniaLoginPW.getText().trim().equals("")){
                        if(tfContraseniaLogin.getText().equals(rsUsuarios.getString("contrasenia"))){
                            id_usuario = rsUsuarios.getInt("id_usuario");
                            iniciaSesion(new Usuario(id_usuario, rsUsuarios.getString("nombre"),rsUsuarios.getString("contrasenia") , LocalDateTime.now()));
                        } else {
                            muestraPanel("Error al iniciar sesión", "Contraseña incorrecta");
                            return;
                        }
                    } else {
                        if(tfContraseniaLoginPW.getText().equals(rsUsuarios.getString("contrasenia"))){
                            id_usuario = rsUsuarios.getInt("id_usuario");
                            iniciaSesion(new Usuario(id_usuario, rsUsuarios.getString("nombre"),rsUsuarios.getString("contrasenia") , LocalDateTime.now()));
                        } else {
                            muestraPanel("Error al iniciar sesión", "Contraseña incorrecta");
                            return;
                        }
                    }

                }
            }
            if(!existe_usuario){
                muestraPanel("Error al iniciar sesión", "No existe un usuario con ese nombre asociado");
                return;
            }
            rsUsuarios.close();
            PreparedStatement pstmUsuario = conexionBD.conectar().prepareStatement("UPDATE USUARIOS SET fecha_conex_actual = '" + LocalDateTime.now()+ "' WHERE id_usuario = "+id_usuario);
            pstmUsuario.executeUpdate();
            tfNombreLogin.setText("");
            tfContraseniaLogin.setText("");



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void iniciaSesion(Usuario usuarioActual) {
        panelInicio.setVisible(false);
        panelRegistro.setVisible(false);
        panelChat.setVisible(true);
        tfBienvenido.setText(tfBienvenido.getText()+ " "+usuarioActual.getNombreUsuario());

    }

    @FXML
    void registraUsuario(ActionEvent event)  {
        if(compruebaTfRegistro()){
            muestraPanel("Error al registrar nuevo usuario","Debe rellenar ambos campos de texto");
            return;
        }
        try{
            if(compruebaNombreUsuario()){
                muestraPanel("Error al registrar nuevo usuario", "Ese nombre de usuario ya existe");
                return;
            }

            PreparedStatement pstmNuevoUsuario = conexionBD.conectar().prepareStatement("INSERT INTO USUARIOS (nombre, contrasenia, fecha_alta) VALUES (?,?,?)")  ;
            pstmNuevoUsuario.setString(1, tfNombreRegistro.getText());
            if(tfContraseniaRegistroPW.getText().trim().equals("")){
                pstmNuevoUsuario.setString(2, tfContraseniaRegistro.getText());
            } else {
                pstmNuevoUsuario.setString(2, tfContraseniaRegistroPW.getText());
            }
            pstmNuevoUsuario.setString(3, String.valueOf(LocalDateTime.now()));
            pstmNuevoUsuario.executeUpdate();

            pstmNuevoUsuario.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        panelInicio.setVisible(true);
        panelRegistro.setVisible(false);
        tfContraseniaRegistro.setText("");
        tfNombreRegistro.setText("");
    }

    private boolean compruebaNombreUsuario() throws SQLException {
        boolean existe = false;
        ResultSet rsUsuarios = conexionBD.conectar().createStatement().executeQuery("SELECT nombre FROM USUARIOS");
        while(rsUsuarios.next()){
            if(rsUsuarios.getString("nombre").equals(tfNombreRegistro.getText())){
                existe = true;
            }
        }
        rsUsuarios.close();
        return existe;
    }


    private boolean compruebaTfLogin(){
        return   tfNombreLogin.getText().trim().equals("")
                || (tfContraseniaLogin.getText().trim().equals("") && tfContraseniaLoginPW.getText().trim().equals(""));
    }

    private boolean compruebaTfRegistro(){
        return   tfNombreRegistro.getText().trim().equals("")
                || (tfContraseniaRegistro.getText().trim().equals("") && tfContraseniaRegistroPW.getText().trim().equals(""));
    }

    private static void muestraPanel(String titulo, String mensaje) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText(mensaje);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }

    @FXML
    void cambiaTfContrasenia(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        if(!checkBox.isSelected()){
            cbVerContraseniaLogin.setSelected(false);
            cbVerContrasenia.setSelected(false);
            tfContraseniaLoginPW.setText(tfContraseniaLogin.getText());
            tfContraseniaLogin.setText("");

            tfContraseniaRegistroPW.setText(tfContraseniaRegistro.getText());
            tfContraseniaRegistro.setText("");

            tfContraseniaLogin.setVisible(false);
            tfContraseniaLoginPW.setVisible(true);
            tfContraseniaRegistro.setVisible(false);
            tfContraseniaRegistroPW.setVisible(true);

        } else {
            cbVerContraseniaLogin.setSelected(true);
            cbVerContrasenia.setSelected(true);

            tfContraseniaLogin.setText(tfContraseniaLoginPW.getText());
            tfContraseniaLoginPW.setText("");

            tfContraseniaRegistro.setText(tfContraseniaRegistroPW.getText());
            tfContraseniaRegistroPW.setText("");

            tfContraseniaLogin.setVisible(true);
            tfContraseniaLoginPW.setVisible(false);
            tfContraseniaRegistro.setVisible(true);
            tfContraseniaRegistroPW.setVisible(false);
        }
    }

    @FXML
    void volverInicio(ActionEvent event) {
        tfBienvenido.setText("Bienvenido");
        panelChat.setVisible(false);
        panelRegistro.setVisible(false);
        panelInicio.setVisible(true);
    }
}