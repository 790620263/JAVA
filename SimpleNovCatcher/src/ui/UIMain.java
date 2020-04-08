package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UIMain extends Application
{
	public static void main(String[] args)
	{
		launch(args);

	}

	TextArea area;TextField field;Button btn_upd;Button btn_choose;
	double perWidth;
	double perHeight;
	Scene root;
	public void start(Stage stage) throws Exception
	{
		stage.setTitle("SimpleNovCatch");
		
		Parent parent = FXMLLoader.load(getClass().getResource("UI.fxml"));
		root=new Scene(parent);
		stage.setScene(root);
		
		
		//窗口关闭结束程序
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent arg0)
			{
				System.exit(0);
			}
		});
		
		
		area=(TextArea) parent.lookup("#consoleArea");
		field=(TextField) parent.lookup("#text");
		btn_upd=(Button) parent.lookup("#button");
		btn_choose=(Button) parent.lookup("#button_choose");
		
		
		//添加监听器
		perWidth=root.getWidth()/30;
		perHeight=root.getHeight()/20;
		StageChangeListener listener=new StageChangeListener();
		stage.addEventHandler(MouseEvent.MOUSE_MOVED,listener);
		listener.setSize();
		listener.setLocale();
		
		//载入配置文件
		loadConfig();
		
		stage.show();
		
		
	}
	
	void loadConfig() throws FileNotFoundException 
	{
		TextPrintStream stream=new TextPrintStream(new TextOutPutStream());
		
		//重定向系统输出流
		System.setOut(stream);
		System.setErr(stream);
		
		field.setText(System.getProperty("user.dir"));
		System.out.println(field.getText());
		String configPath=System.getProperty("user.dir")+File.separator+"config.ini";
		System.out.println("ConfigPath="+configPath);
		
		
		
		Control.toDownload=new ArrayList<String>();
		BufferedReader r=new BufferedReader(new InputStreamReader(new FileInputStream(configPath), Charset.forName("UTF-8")));
		String str;
		try
		{
			while((str =r.readLine())!=null)
			{
				//跳过开头标记有#的书
				if(str.startsWith("#"))continue;
				
				System.out.println(str);
				//@后面是书名注释
				str=str.split("@")[0];
				Control.toDownload.add(str);
			}
			
			r.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	class TextOutPutStream extends OutputStream
    {
		public void write(int b) throws IOException
		{
			Platform.runLater(() -> 
			{
				area.appendText(String.valueOf((char)b));
			});
			
		}
    	
    }
    class TextPrintStream extends PrintStream{
		public TextPrintStream(OutputStream out)
		{
			super(out);
		}

		public void println(String str)
    	{
    		Platform.runLater(()->
    		{
    			area.appendText(str);
    			area.appendText("\n");
    		});
//			synchronized (area) {
//				area.appendText(str);
//    			area.appendText("\n");
//			}
    			
    	}
    }
	
	
	
	
	
	class StageChangeListener implements EventHandler<MouseEvent>
	{
		void setSize()
		{
			area.setMinSize(perWidth*28, perHeight*13);
			area.setMaxSize(perWidth*28, perHeight*13);
			
			field.setMinSize(perWidth*16, perHeight*2);
			field.setMaxSize(perWidth*16, perHeight*2);
			
			btn_choose.setMinSize(perWidth*5,perHeight*4);
			btn_choose.setMinSize(perWidth*5,perHeight*4);
			
			btn_upd.setMinSize(perWidth*5,perHeight*4);
			btn_upd.setMaxSize(perWidth*5,perHeight*4);
		}
		
		void setLocale()
		{
			area.setLayoutX(perWidth);
			area.setLayoutY(perHeight);
			
			field.setLayoutX(perWidth);
			field.setLayoutY(perHeight*16);
			
			btn_choose.setLayoutX(perWidth*18);
			btn_choose.setLayoutY(perHeight*15);
			
			btn_upd.setLayoutX(perWidth*24);
			btn_upd.setLayoutY(perHeight*15);
		}
		
		public void handle(MouseEvent arg0)
		{
			perWidth=root.getWidth()/30;
			perHeight=root.getHeight()/20;
			
			setSize();
			setLocale();
		}

	}



	

}
