package ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import download.BookManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Control
{
	   @FXML
	    private Button button;

	   @FXML
	   private Pane pane;
	    @FXML
	    private TextArea consoleArea;

	    @FXML
	    private TextField text;

	    @FXML
	    private Button button_choose;
	    
	    void setText(String str)
	    {
	    	text.setText(str);
	    }
	    @FXML
	    void handleBtnClick(ActionEvent event) {
	    	if(m!=null)m.shutdown();
	    	updateBook();
	    }
	    

	    @FXML
	    void handleBtnChooseClick(ActionEvent event) {
	    	
	    	
	    	try {
				DirectoryChooser directoryChooser=new DirectoryChooser();
				directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
				File file = directoryChooser.showDialog(new Stage());
				String savePath=file.getCanonicalPath();
				
				text.setText(savePath);
			}catch(IOException e)
			{
				e.printStackTrace();
			}
	    	
	    	
	    }
	    
	    static ArrayList<String> toDownload;
	    BookManager m=null;
	    void updateBook()
		{
	    	if(m==null)
	    	{
	    		m=new BookManager(toDownload,text.getText());
	    	}
	    	m.dlBook();
		}
		
		
		
	    
	    
	    
	    
	    
	    
}
