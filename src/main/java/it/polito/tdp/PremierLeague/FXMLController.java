
/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.GiocatoreMigliore;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnGiocatoreMigliore"
    private Button btnGiocatoreMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMatch"
    private ComboBox<Match> cmbMatch; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Match m = cmbMatch.getValue();
    	if(m==null) {
    		txtResult.setText("Selezionare un match");
    		return;
    	}
    	
    	model.creaGrafo(m);
    	txtResult.appendText("GRAFO CREATO\n");
    	txtResult.appendText(String.format("# VERTICI: %d\n# ARCHI: %d", model.nVertici(), model.nArchi()));
    	
    	btnSimula.setDisable(false);
    }

    @FXML
    void doGiocatoreMigliore(ActionEvent event) {    	
    	txtResult.clear();
    	GiocatoreMigliore p = model.getGiocatoreMigliore();
    	if(p==null) {
    		txtResult.setText("ERRORE! Crea prima il grafo");
    		return;
    	}
    	
    	txtResult.appendText("Giocatore migliore:\n");
    	txtResult.appendText(p.toString());
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Match m = cmbMatch.getValue();
    	if(m==null) {
    		txtResult.setText("Selezionare un match");
    		return;
    	}
    	
    	int N;
    	try {
    		N = Integer.parseInt(txtN.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero valido di azioni salienti");
    		return;
    	}
    	
    	model.simula(N, m);
    	txtResult.appendText("RISULTATO FINALE:\n");
    	txtResult.appendText(String.format("%s %d - %d %s\n", m.getTeamHomeNAME(), model.getGoalTeam1(), model.getGoalTeam2(), m.getTeamAwayNAME()));
    	txtResult.appendText(String.format("Espulsi %s: %d\n", m.getTeamHomeNAME(), model.getNumEspulsi1()));
    	txtResult.appendText(String.format("Espulsi %s: %d", m.getTeamAwayNAME(), model.getNumEspulsi2()));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGiocatoreMigliore != null : "fx:id=\"btnGiocatoreMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMatch != null : "fx:id=\"cmbMatch\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        btnSimula.setDisable(true);

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbMatch.getItems().addAll(model.getAllMatches());
    }
}
