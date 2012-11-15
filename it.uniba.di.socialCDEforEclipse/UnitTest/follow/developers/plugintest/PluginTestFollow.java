package follow.developers.plugintest;

import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

import it.uniba.di.socialcdeforeclipse.action.ActionDynamicUserTimeline;
import it.uniba.di.socialcdeforeclipse.action.ActionLoginPanel;
import it.uniba.di.socialcdeforeclipse.controller.Controller;
import it.uniba.di.socialcdeforeclipse.sharedLibrary.WUser;

import org.eclipse.ui.PlatformUI;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PluginTestFollow extends TestCase {

	/**
	 * Unit test for User story number 11.
	 * 
	 * Action considered: Follow a developer
	 * 
	 * Equivalence classes considered: 
	 * 1.Follow a developer identified by correct id
	 * 2.Follow a developer identified by wrong id (-1) 
	 * 
	 * Note: For test the equivalence class number 1, the sistem select a random developer 
	 * between suggested developers. At the end of test the user selected will be unfollow. 
	 * */
	
	static HashMap<String, Object> dati; 
	Document document; 
	WUser[] users;
	static int positionUser; 
	
	@Before
	public void setUp() throws Exception {
		
		SAXBuilder builder = new SAXBuilder();
		try {
			document = builder.build(new File("./testData.xml").getCanonicalPath());
		} catch (JDOMException e) {
			System.out.println("Eccezione lanciata"); 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("it.uniba.di.socialcdeforeclipse.views.SocialCDEview");
		
		 assertNotNull(Controller.getLoginPanel());
		  dati = Controller.getLoginPanel().getData(); 
		  dati.put("ID_action", "btnLogin"); 
		  dati.put("Event_type", SWT.Selection); 
		  ( (Text)  dati.get("txtProxyHost")).setText(document.getRootElement().getChild("CorrectData").getChild("Proxy").getText());
		  ( (Text)  dati.get("txtUsername")).setText(document.getRootElement().getChild("CorrectData").getChild("Username").getText());
		  ( (Text)  dati.get("txtPassword")).setText(document.getRootElement().getChild("CorrectData").getChild("Password").getText());
		  new ActionLoginPanel(dati);
		assertNotNull(Controller.getHomeWindow()); 
		users = Controller.getProxy().GetSuggestedFriends(Controller.getCurrentUser().Username, Controller.getCurrentUserPassword());
		assertTrue(users.length > 0); 
	}
	
	@Test public void testCase1()
	{
		 positionUser = (int) (Math.random() * (users.length -1)); 
		 Controller.temporaryInformation.put("User_selected", users[positionUser]); 
		 Controller.temporaryInformation.put("User_type", "Suggested");
		 Controller.selectDynamicWindow(3);
		 assertNotNull(Controller.getDynamicUserWindow()); 
		 dati = Controller.getDynamicUserWindow().getData(); 
		 dati.put("ID_action", "labelFollow"); 
		  dati.put("Event_type", SWT.Selection);
		 
		  assertFalse( (Boolean)  dati.get("imageUnFollow")); 
		  assertTrue( (Boolean) dati.get("imageFollow")); 
		  
		  new ActionDynamicUserTimeline(dati); 
		  
		  assertTrue( (Boolean)  dati.get("imageUnFollow")); 
		  assertFalse( (Boolean) dati.get("imageFollow")); 

		  
		  
	}
	
	@Test public void testCase2()
	{
		 positionUser = -1; 
		 WUser phantom = new WUser(); 
		 phantom.Id = -1; 
		 phantom.Avatar = null;
		 phantom.Followers = 0; 
		 phantom.Followings = 0; 
		 phantom.Statuses = 0; 
		 phantom.setUsername("Test"); 
		 
		 Controller.temporaryInformation.put("User_selected", phantom); 
		 Controller.temporaryInformation.put("User_type", "Suggested");
		 Controller.selectDynamicWindow(3);
		 assertNotNull(Controller.getDynamicUserWindow()); 
		 dati = Controller.getDynamicUserWindow().getData(); 
		 dati.put("ID_action", "labelFollow"); 
		  dati.put("Event_type", SWT.Selection);
		 
		  assertFalse( (Boolean)  dati.get("imageUnFollow")); 
		  assertTrue( (Boolean) dati.get("imageFollow")); 
		  
		  new ActionDynamicUserTimeline(dati); 
		  
		  assertTrue( (Boolean)  dati.get("error")); 
	}
	
	@After
	public void tearDown() throws Exception {
		
		if(positionUser != -1)
		{
			assertTrue( (Boolean)  dati.get("imageUnFollow")); 
			assertFalse( (Boolean) dati.get("imageFollow")); 
			assertTrue(Controller.getProxy().UnFollow(Controller.getCurrentUser().Username, Controller.getCurrentUserPassword(), users[positionUser].Id)); 
			  
		}
		
		
		 
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("it.uniba.di.socialcdeforeclipse.views.SocialCDEview"));
	}
	
	
	

	
	

}
