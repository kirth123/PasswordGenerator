package application;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;

public class Controller {
    @FXML private TextField passLen;
    @FXML private TextField specialChars;
    @FXML private PasswordField password;
    @FXML private CheckBox upperCase;
    @FXML private CheckBox lowerCase;
    @FXML private CheckBox numbers;
    @FXML private ProgressBar meter;
    @FXML private Label req; 
    @FXML private Label score_alert;
    protected static StringBuilder msg = new StringBuilder(); //is attached to req
    protected static String score = new String(); //is attached to score_alert
    
    @FXML
    protected void execFunc(ActionEvent event) {
    	try {
    		Integer length = Integer.parseInt(passLen.getText());
    		Boolean upper = upperCase.isSelected();
        	Boolean lower  = lowerCase.isSelected();
        	Boolean num = numbers.isSelected();
        	String special = specialChars.getText();
        	
        	if(length > 0) { //if user gave a valid password length, then run function
        	     String pass = Main.genPass(length, upper, lower, num, special); //call genPass() once and store in a variable
        	     
        	     if(!pass.equals("")) { //if genPass() returns a valid password, issue confirmation
        	    	 Main.alert(false, "Your password was copied to memory", pass);
        	     }
        	     else { //if genPass() returns empty string, then issue warning
        	    	 Main.alert(true, "Password Creation Failed", "Select more options, then try again");
        	     }
        	}
        	else { //if no length given, issue warning
        		Main.alert(true, "Password Creation Failed", "Input a number for password length");
        	}
    	}
    	catch(NumberFormatException e) {
    		Main.alert(true, "Password Creation Failed", "Input a number for password length");
    	}
    }	 
    
    @FXML
    protected void execTest(ActionEvent event) {
    	if(!password.getText().isEmpty()) { //if password field is not empty, run calcStrength()
    			double progress = Main.calcStrength(password.getText());
    			meter.setProgress(progress/100);
    			req.setText(Controller.msg.toString());
    			score_alert.setText(score);
    	}
    	else {
    		Main.alert(true, "Failed to test password", "Input text in the password field");
    	}
    }	 
}
