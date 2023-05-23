package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Optional;



public class Controller {
	private String fichierCSV = "C:\\Users\\saadr\\OneDrive\\Documents\\Ecole\\Informatique\\Langage\\Java\\GestionnaireContact\\src\\application\\bddContact.csv";
	
	@FXML
	private Button btnAdd = new Button();
	public void add() {
	    TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Ajouter un contact");
	    dialog.setHeaderText(null);
	    dialog.setContentText("Nom :");
	    Optional<String> result = dialog.showAndWait();
	    if (result.isPresent()) {
	        String nom = result.get();
	        dialog.setContentText("Numero :");
	        result = dialog.showAndWait();
	        String numero = result.get();
	        dialog.setContentText("Mail :");
	        result = dialog.showAndWait();
	        String mail = result.get();
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichierCSV, true))) {
	            writer.write("," + nom + "," + numero + "," + mail);
	            writer.newLine();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        // Créez un nouvel objet Contact avec les informations fournies
	        Contact nouveauContact = new Contact(nom, numero, mail);

	        // Ajoutez le nouvel objet Contact à la liste de données de la TableView
	        tableViewContact.getItems().add(nouveauContact);
	    }
	}

	
	@FXML
	private Button btnModify = new Button();
	public void modify() {
	    int selectedIndex = tableViewContact.getSelectionModel().getSelectedIndex();
	    
	    // Vérifier si une ligne est sélectionnée
	    if (selectedIndex >= 0) {
	        // Récupérer l'objet Contact correspondant à la ligne sélectionnée
	        Contact selectedContact = tableViewContact.getItems().get(selectedIndex);
	        
	        // Créer une boîte de dialogue de saisie des informations de contact
	        TextInputDialog dialog = new TextInputDialog();
	        dialog.setTitle("Modifier le contact");
	        dialog.setHeaderText(null);
	        
	        // Pré-remplir les champs de saisie avec les valeurs actuelles de l'objet Contact
	        dialog.setContentText("Nom :");
	        dialog.getEditor().setText(selectedContact.getNom());
	        Optional<String> resultNom = dialog.showAndWait();
	        
	        dialog.setContentText("Numero :");
	        dialog.getEditor().setText(selectedContact.getNumero());
	        Optional<String> resultNumero = dialog.showAndWait();
	        
	        dialog.setContentText("Mail :");
	        dialog.getEditor().setText(selectedContact.getMail());
	        Optional<String> resultMail = dialog.showAndWait();
	        
	        // Vérifier si l'utilisateur a appuyé sur OK et récupérer les nouvelles valeurs saisies
	        if (resultNom.isPresent() && resultNumero.isPresent() && resultMail.isPresent()) {
	            String nom = resultNom.get();
	            String numero = resultNumero.get();
	            String mail = resultMail.get();
	            
	            // Mettre à jour les valeurs de l'objet Contact avec les nouvelles valeurs saisies
	            selectedContact.setNom(nom);
	            selectedContact.setNumero(numero);
	            selectedContact.setMail(mail);
	            
	            // Rafraîchir la ligne sélectionnée dans le TableView
	            tableViewContact.getItems().set(selectedIndex, selectedContact);
	            
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichierCSV))) {
	                for (Contact c : tableViewContact.getItems()) {
	                    writer.write("," + c.getNom() + "," + c.getNumero() + "," + c.getMail());
	                    writer.newLine();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}


	
	@FXML
	private Button btnRemove = new Button();
	public void remove() {
		// Récupérer l'index de la ligne sélectionnée dans le TableView
		int selectedIndex = tableViewContact.getSelectionModel().getSelectedIndex();

		// Vérifier si une ligne est sélectionnée
		if (selectedIndex >= 0) {
		    // Supprimer la ligne sélectionnée de la liste des éléments de la TableView
		    tableViewContact.getItems().remove(selectedIndex);
		}
		deleteLineInCSV(fichierCSV, selectedIndex);
	}
	public void deleteLineInCSV(String filePath, int lineIndex) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        List<String> lines = new ArrayList<>();

	        // Copier toutes les lignes du fichier dans une liste
	        String line;
	        while ((line = br.readLine()) != null) {
	            lines.add(line);
	        }

	        // Vérifier si l'index de la ligne est valide
	        if (lineIndex >= 0 && lineIndex < lines.size()) {
	            // Supprimer la ligne de la liste
	            lines.remove(lineIndex);

	            // Réécrire le fichier avec les lignes mises à jour
	            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
	                for (String updatedLine : lines) {
	                    bw.write(updatedLine);
	                    bw.newLine();
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	@FXML
	private Button btnSearch = new Button();
	public void searchName() {
		//TODO: 
		TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Rechercher un contact");
	    dialog.setHeaderText(null);
	    dialog.setContentText("Nom :");
	    Optional<String> result = dialog.showAndWait();
	    if (result.isPresent()) {
	        String nom = result.get();
	        ObservableList<Contact> contactList = tableViewContact.getItems();
	        boolean found = false;

	        for (Contact contact : contactList) {
	            if (contact.getNom().equalsIgnoreCase(nom)) {
	                // Le nom a été trouvé dans la colonne 1
	                found = true;
	                int rowIndex = contactList.indexOf(contact);
	                tableViewContact.getSelectionModel().select(rowIndex);
	                tableViewContact.scrollTo(rowIndex);
	                break;
	            }
	        }

	        if (found) {
	            System.out.println("Le nom '" + nom + "' a été trouvé dans la colonne 1.");
	        } else {
	            System.out.println("Le nom '" + nom + "' n'a pas été trouvé dans la colonne 1.");
	        }
	    }
	}
	
	
	@FXML
	private Button btnClose = new Button();
	public void close() {
		Platform.exit();
	}
	
	
	@FXML
    private TableView<Contact> tableViewContact = new TableView<Contact>();

    @FXML
    private TableColumn<Contact, String> nameColumn;

    @FXML
    private TableColumn<Contact, Integer> ageColumn;

    @FXML
    private TableColumn<Contact, String> emailColumn;
	
	
	private void ContactBDD() {
	    ArrayList<Contact> listeContact = new ArrayList<Contact>();
	    try (BufferedReader br = new BufferedReader(new FileReader(fichierCSV))) {
	        String line;
	        String nom;
	        String num;
	        String mail;
	        
	        while ((line = br.readLine()) != null) {
	            String[] donnee = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	            nom = donnee[1];
	            num = donnee[2];
	            mail = donnee[3];
	            
	            Contact contact = new Contact(nom,num, mail);
	            listeContact.add(contact);
	        }
	        
	        br.close();
	        
	        // Mettre à jour les données dans la première colonne du tableView
	        // Pour le nom
	        @SuppressWarnings("unchecked")
			TableColumn<Contact, String> existingColumn = (TableColumn<Contact, String>) tableViewContact.getColumns().get(0);
	        existingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
	        // Pour le numéro
	        @SuppressWarnings("unchecked")
			TableColumn<Contact, String> existingColumnNum = (TableColumn<Contact, String>) tableViewContact.getColumns().get(1);
	        existingColumnNum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
	        // Pour le mail
	        @SuppressWarnings("unchecked")
			TableColumn<Contact, String> existingColumnMail = (TableColumn<Contact, String>) tableViewContact.getColumns().get(2);
	        existingColumnMail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMail()));
	        
	        // Mettre à jour les données dans le tableView
	        ObservableList<Contact> contactData = FXCollections.observableArrayList(listeContact);
	        tableViewContact.setItems(contactData);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	@FXML
	public void initialize() {
		ContactBDD();
		BooleanBinding isItemSelected = tableViewContact.getSelectionModel().selectedItemProperty().isNull();
	    btnModify.disableProperty().bind(isItemSelected);
	    btnRemove.disableProperty().bind(isItemSelected);
	    btnAdd.setOnAction(event -> {
	    	add();
	    });
	    btnModify.setOnAction(event -> {
	    	modify();
	    });
	    btnRemove.setOnAction(event -> {
	    	remove();
	    });
	    btnSearch.setOnAction(event -> {
	    	searchName();
	    });
		btnClose.setOnAction(event -> {
			close();
		});
		
	}
	
}
