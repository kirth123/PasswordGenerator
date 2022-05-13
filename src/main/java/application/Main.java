package application;
	
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
	
	protected static void alert(Boolean warning, String header, String content) {
		Clipboard clipboard = Clipboard.getSystemClipboard();
    		ClipboardContent cc = new ClipboardContent(); //clipboard saves data to computer memory
    		Alert a;
    	
		if(warning) { //if warning returned, inform user to fill out more fields
			a = new Alert(AlertType.WARNING);
			a.setHeaderText(header);
   	     		a.setContentText(content);
   	     		a.show();
		}
		else {// if no warning, issue confirmation alert and save password to clipboard
			a = new Alert(AlertType.CONFIRMATION);
	    		a.setHeaderText(header);
   	     		a.setContentText(content);
   	     		a.show();
	    		cc.putString(content);
	    		clipboard.setContent(cc);
		}
	}
	
	protected static String genPass(Integer length, Boolean upper, Boolean lower, Boolean num, String special) {
		String template = "";
		String pswd = "";
		String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowercase = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		Random rand = new Random();
		Integer index;
		ArrayList<Character> charset = new ArrayList<Character>();
		
		if(upper) {
			template += uppercase;
		}
		if(lower) {
			template += lowercase;
		}
		if(num) {
			template += numbers;
		}
		if(!special.isEmpty()) {
			template += special;
		}

		for (char c:template.toCharArray()) {
		  charset.add(c);
		}
		Collections.shuffle(charset); //scramble the character list to make it more random
		
		if(charset.size() > 0) {
			for(int i = 0; i < length; i++) {
				index = rand.nextInt(charset.size());
				pswd += charset.get(index);
			}
		}
		else {
			return "";
		}
		
		return pswd.toString();
	}
	
	protected static Integer countInst(char letter, String entry) { //counts number of times character appears
	    int count = 0;
	    for(int i = 0; i < entry.length(); i++) {
	        if(entry.charAt(i) == letter) {
	            count++;
	        }
	    }
	    return count;
	}
	
	protected static Double calcStrength(String password) {
        Double score = 0.0;
        String uppercase = ".*[A-Z].*"; 
        String lowercase = ".*[a-z].*"; 
        String numbers = ".*\\d.*"; 
        String special = ".*[^A-Za-z0-9].*"; 
        Pattern p;
        Matcher m;
        boolean repeats = false;
        
        Controller.msg.setLength(0); //clears variable of previous msg
        
        if(password.length() >= 8) { //checks if password is at least 8 characters long
        	score += 2; 
        	Controller.msg.append("Password is at least 8 characters long\n");
        }
        else {
        	Controller.msg.append("Password should have at least 8 characters\n");
        	Controller.score = "Password Score: 0%"; 
        	return 0.0;
        }
        
        /*
         following block of code matches password against pattern expressions
         looking to see if password matches certain criteria 
         */
        
        p = Pattern.compile(uppercase);
        m = p.matcher(password);      
        if(m.find()) {
        	score += 2;
        	Controller.msg.append("Password has at least one uppercase letter\n");
        }
        else {
        	Controller.msg.append("Password should have at least one uppercase letter\n");
        }
        
        p = Pattern.compile(lowercase);
        m = p.matcher(password);      
        if(m.find()) {
        	score += 2;
        	Controller.msg.append("Password has at least one lowercase letter\n");
        }
        else {
        	Controller.msg.append("Password should have at least one lowercase letter\n");
        }
        
        p = Pattern.compile(numbers);
        m = p.matcher(password);      
        if(m.find()) {
        	score += 2;
        	Controller.msg.append("Password has at least one number\n");
        }
        else {
        	Controller.msg.append("Password should have at least one number\n");
        }
        
        p = Pattern.compile(special);
        m = p.matcher(password);      
        if(m.find()) {
        	score += 2;
        	Controller.msg.append("Password has at least one special character\n");
        }    
        else {
        	Controller.msg.append("Password should have at least one special character\n");
        }
        
        score += 2; //add 2 points by default (assumes no character repetitions)  
        for(int i = 0; i < password.length(); i++) {//iterates through password string and checks for repeats of characters
    	   char index = password.charAt(i);
   		   if(Main.countInst(index, password) > 1) { //if any character repeats in password, subtract 2 points from score and break from loop
    		   score -= 2; //subtracts 2 points from score and breaks from loop
    		   repeats = true;
    		   break;
   		   }
    	 }    	
        if(!repeats) {
        	Controller.msg.append("Password does not have any repeating characters\n");
        }
        else {
        	Controller.msg.append("Password should not have repeating characters\n");
        }
        
        score = score / 12; //set score to proportion of max score 12	
    	double percent = Math.round(score * 100); //return value will be percentage  	
    	Controller.score = "Password Score: " + Double.toString(percent) + "%"; 
        return percent; 
    }
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
			Scene scene = new Scene(root);
			Image image = new Image(String.valueOf(Main.class.getResource("images/title.jpg")));
			primaryStage.getIcons().add(image);
			primaryStage.setTitle("Password Generator");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
