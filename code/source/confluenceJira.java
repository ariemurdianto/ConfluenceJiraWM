

// -----( IS Java Code Template v1.2
// -----( CREATED: 2009-10-23 22:18:25 SGT
// -----( ON-HOST: GUN-TANK

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import objectData.*;
import java.util.Properties;
import java.util.regex.Matcher;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SubjectTerm;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import java.io.IOException;
import javax.mail.Flags.Flag;
import org.apache.commons.httpclient.methods.GetMethod;
// --- <<IS-END-IMPORTS>> ---

public final class confluenceJira

{
	// ---( internal utility methods )---

	final static confluenceJira _instance = new confluenceJira();

	static confluenceJira _newInstance() { return new confluenceJira(); }

	static confluenceJira _cast(Object o) { return (confluenceJira)o; }

	// ---( server methods )---




	public static final void ConfluenceMailConnector (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(ConfluenceMailConnector)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required mailServer
		// [i] field:0:required usernameMail
		// [i] field:0:required passwordMail
		// [i] field:0:required inboxServer
		// [i] field:0:required isTlsEnabled
		// [i] field:0:required authentication
		// [o] record:1:required IssuePageData
		// [o] - field:0:required title
		// [o] - field:0:required contentDescription
		// [o] - field:0:required space
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	mailServer = IDataUtil.getString( pipelineCursor, "mailServer" );
			String	usernameMail = IDataUtil.getString( pipelineCursor, "usernameMail" );
			String	passwordMail = IDataUtil.getString( pipelineCursor, "passwordMail" );
			String	inboxServer = IDataUtil.getString( pipelineCursor, "inboxServer" );
			String	isTlsEnabled = IDataUtil.getString( pipelineCursor, "isTlsEnabled" );
			String	authentication = IDataUtil.getString( pipelineCursor, "authentication" );
		pipelineCursor.destroy();
		HashMap<String, String> components = new HashMap<String, String>();
		Properties props = System.getProperties();
		        props.put("mail.smtp.starttls.enable", isTlsEnabled); // added this line
			props.put("mail.smtp.host", mailServer);
			props.put("mail.smtp.user", usernameMail);
			props.put("mail.smtp.password", passwordMail);
			props.put("mail.smtp.auth", authentication);
			Session session = Session.getInstance(props);
		        Pattern mailPattern = Pattern.compile(mailSpace);
			IData[] IssuePageData = null;
		        try
		        {
		            Store store = session.getStore(protocol);
		            store.connect(mailServer,usernameMail, passwordMail);
		            Folder folder = store.getFolder(confluenceFolderName);
		            folder.open(Folder.READ_WRITE);
		
		            SubjectTerm subjectFilter = new SubjectTerm(confluenceFolderName);
		            Message[] messages = folder.search(subjectFilter);
		
		            Folder readFolder = store.getFolder(confluenceReadFolderName);
		            readFolder.open(Folder.READ_WRITE);
		            folder.copyMessages(messages, readFolder);
		
			    int start = 0;
			    IssuePageData = new IData[messages.length];
			    System.out.println("this is the length of the messages: "+messages.length);
		            for(int i=messages.length-1; i>=0; i--)
		            {
				IssuePageData[i] = IDataFactory.create();
				IDataCursor IssuePageDataCursor = IssuePageData[i].getCursor();
		                Message message = messages[i];
				message.setFlag(Flag.DELETED, true);
		                String mailSubject = message.getSubject();
		                Matcher mailSubjectFilter;
		                try
		                {
		                	mailSubject = mailSubject.replace("["+confluenceFolderName+"] ", "");
		                    mailSubjectFilter = mailPattern.matcher(mailSubject);
		                    if(mailSubjectFilter.find())
		                    {
		                    	start = mailSubjectFilter.start();
		                    	components.put(COMPONENT_SUMMARY, mailSubject.substring(start));
		                        components.put(COMPONENT_PROJECT, mailSubject.substring(0, start).trim());
		                        components.put(COMPONENT_DESCRIPTION, message.getContent().toString());
					IDataUtil.put( IssuePageDataCursor, "title", components.get(COMPONENT_SUMMARY));
					IDataUtil.put( IssuePageDataCursor, "contentDescription", components.get(COMPONENT_DESCRIPTION) );
					IDataUtil.put( IssuePageDataCursor, "space", components.get(COMPONENT_PROJECT));
					IssuePageDataCursor.destroy();
		                    }
		                } 
		                catch(Exception e)
		                {
		                    e.printStackTrace();
		                }
		            }
			    folder.close(true);
		            readFolder.close(true);
		//            System.out.println(components.get("Project"));
		//            System.out.println(components.get("Summary"));
		//            System.out.println(components.get("Description"));
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
		
		
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		
		// IssuePageData
		IDataUtil.put( pipelineCursor_1, "IssuePageData", IssuePageData );
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void ConfluenceSpaceCreator (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(ConfluenceSpaceCreator)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required key
		// [i] field:0:required name
		// [i] field:0:required os_username
		// [i] field:0:required os_password
		// [i] field:0:required host
		// [o] field:0:required output
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	key = IDataUtil.getString( pipelineCursor, "key" );
			String	name = IDataUtil.getString( pipelineCursor, "name" );
			String	os_username = IDataUtil.getString( pipelineCursor, "os_username" );
			String	os_password = IDataUtil.getString( pipelineCursor, "os_password" );
			String	host = IDataUtil.getString( pipelineCursor, "host" );
		pipelineCursor.destroy();
		HttpClient httpClient = new HttpClient();
		
			PostMethod postMethod = new PostMethod(host+"/spaces/createspace.action");
		
			System.out.println("the host: "+host+"/spaces/createspace.action");
		        postMethod.addParameter("name", name);
		        postMethod.addParameter("key", key);
		        postMethod.addParameter("os_username", os_username);
		        postMethod.addParameter("os_password",os_password);
			
			String token = "atl_token\"\\s+[a-z]+\\=\"(.)+\"";
			Pattern tokenFilter = Pattern.compile(token);
			
		
		        int status=0;
			String output = "";
			try
			{
			     HttpClient httpClient1 = new HttpClient();
			     PostMethod getMethods = new PostMethod(host+"/spaces/createspace-start.action");
			     getMethods.addParameter("os_username", os_username);
			     getMethods.addParameter("os_password",os_password);
			     httpClient1.executeMethod(getMethods);
			     output = getMethods.getResponseBodyAsString();
		  	     getMethods.releaseConnection();
			}
			catch(Exception e){e.printStackTrace();}
			System.out.println(output);
			Matcher tokenMatcher = tokenFilter.matcher(output);
			String spaceToken = tokenMatcher.group(tokenMatcher.groupCount());
			System.out.println("Space token: "+spaceToken);
		
		        try {
		            status = httpClient.executeMethod(postMethod);
			    output = postMethod.getResponseBodyAsString();
		
		        } catch (HttpException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "output", output);
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void JIRAProjectCreator (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(JIRAProjectCreator)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required name
		// [i] field:0:required key
		// [i] field:0:required lead
		// [i] field:0:required permissionScheme
		// [i] field:0:required os_username
		// [i] field:0:required os_password
		// [o] field:0:required isCreated
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	name = IDataUtil.getString( pipelineCursor, "name" );
			String	key = IDataUtil.getString( pipelineCursor, "key" );
			String	lead = IDataUtil.getString( pipelineCursor, "lead" );
			String	permissionScheme = IDataUtil.getString( pipelineCursor, "permissionScheme" );
			String	os_username = IDataUtil.getString( pipelineCursor, "os_username" );
			String	os_password = IDataUtil.getString( pipelineCursor, "os_password" );
		pipelineCursor.destroy();
		HttpClient httpClient = new HttpClient();
		        PostMethod postMethod = new PostMethod("http://localhost:8080/secure/admin/AddProject.jspa");
		       
		        postMethod.addParameter("name", name);
		        postMethod.addParameter("key", key);
		        postMethod.addParameter("lead", lead);
		        postMethod.addParameter("permissionScheme",permissionScheme);
		        postMethod.addParameter("os_username", os_username);
		        postMethod.addParameter("os_password",os_password);
		        int status=0;
		        try {
		            status = httpClient.executeMethod(postMethod);
			    status = 1;
		        } catch (HttpException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "isCreated", status+"" );
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void JiraMailConnector (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(JiraMailConnector)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required mailServer
		// [i] field:0:required usernameMail
		// [i] field:0:required passwordMail
		// [i] field:0:required inboxServer
		// [i] field:0:required isTlsEnabled
		// [i] field:0:required authentication
		// [o] record:1:required IssuePageData
		// [o] - field:0:required title
		// [o] - field:0:required contentDescription
		// [o] - field:0:required space
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	mailServer = IDataUtil.getString( pipelineCursor, "mailServer" );
			String	usernameMail = IDataUtil.getString( pipelineCursor, "usernameMail" );
			String	passwordMail = IDataUtil.getString( pipelineCursor, "passwordMail" );
			String	inboxServer = IDataUtil.getString( pipelineCursor, "inboxServer" );
			String	isTlsEnabled = IDataUtil.getString( pipelineCursor, "isTlsEnabled" );
			String	authentication = IDataUtil.getString( pipelineCursor, "authentication" );
		pipelineCursor.destroy();
		
		props.put("mail.smtp.starttls.enable", isTlsEnabled); // added this line
		props.put("mail.smtp.host", mailServer);
		props.put("mail.smtp.user", usernameMail);
		props.put("mail.smtp.password", passwordMail);
		props.put("mail.smtp.auth", authentication);
		
		Session session = Session.getInstance(props);
		HashMap<String, String> components = new HashMap<String, String>();
		//document as an output
		IData[] IssuePageData = null;
		
		Pattern mailPattern = Pattern.compile(mailRegexToFetch);
		try
		{
		    Store store = session.getStore(protocol);
		    store.connect(inboxServer, usernameMail, passwordMail);
		    Folder folder = store.getFolder(jiraFolderName);
		    folder.open(Folder.READ_WRITE);            
		    SubjectTerm subjectFilter = new SubjectTerm("jira-tester");
		    Message[] messages = folder.search(subjectFilter);
		    
		    Folder readFolder = store.getFolder(jiraReadFolderName);
		    readFolder.open(Folder.READ_WRITE);
		    folder.copyMessages(messages, readFolder);
		
		//  Message[] messages = folder.getMessages();
		    IssuePageData = new IData[messages.length];
		    int start = 0;
		    int end = 0;
		    for(int i=messages.length-1; i>=0; i--)
		    {
		    	Message message = messages[i];
		        String mailSubject = message.getSubject();
			//message.setFlag(Flag.DELETED, true);
		        Matcher mailSubjectFilter;
		        try
		        {		
		        	mailSubjectFilter = mailPattern.matcher(mailSubject);
		                if(mailSubjectFilter.find())
		                {
					start = mailSubjectFilter.start();
		                        end = mailSubjectFilter.end();
		                 }
		                 components.put("Summary", mailSubject.substring(end));
		                 mailSubject = mailSubject.substring(start, end);
		                 mailSubject = mailSubject.trim();
		                 if(mailSubject.equals(CREATED))
		                 {
				    IssuePageData[i] = IDataFactory.create();
				    IDataCursor IssuePageDataCursor = IssuePageData[i].getCursor();
				    IDataUtil.put( IssuePageDataCursor, "title",  components.get("Summary"));
					
		                    String content = message.getContent().toString();
		                    String[] contentLines = content.split("\n");
		                    for(int j =1; j<contentLines.length; j++)
		                    {
		                       contentLines[j] = contentLines[j].trim();
		                       if(contentLines[j].contains(COMPONENT_PROJECT))
		                       {
		                          contentLines[j] = contentLines[j].replace(COMPONENT_PROJECT, "");
		                    	  components.put("Project", contentLines[j].trim());
					  IDataUtil.put( IssuePageDataCursor, "space", components.get("Project").toString());
		                       }
		                       else 
		                       {
		                          if(j+1 == contentLines.length) break;
		                    	  if(contentLines[j].trim().equals("") && contentLines[j+1].trim().equals(""))
		                    	  {
		                    	      j = j + 2;
		                    	      contentLines[j] = contentLines[j].replace(COMPONENT_DESCRIPTION, "");
		                    	      components.put("Description", contentLines[j].trim());
					      IDataUtil.put( IssuePageDataCursor, "contentDescription", contentLines[j].trim());
		                    	   }
		                    	   if(contentLines[j].equals("--"))
		                    	   break;
		                    			
		                    	}
		                     }
				  IssuePageDataCursor.destroy();
		              }	
		           }
		           catch(Exception e)
		           {
		              e.printStackTrace();
		           }
		        }
		  	folder.close(true);
			readFolder.close(true);
		    	System.out.println("the messages lengthsssss: "+messages.length);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "IssuePageData", IssuePageData );
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static Properties props = System.getProperties();
	final static String CREATED = "Created:";
	final static String jiraFolderName = "jira-tester";
	final static String jiraReadFolderName = "read-jira-tester";
	final static String confluenceFolderName = "confluence-tester";
	final static String confluenceReadFolderName = "read-confluence-tester";
	final static String protocol = "imaps";
	final static String mailRegexToFetch = "\\s[a-zA-Z]+\\:\\s";
	final static String COMPONENT_PROJECT = "Project:";
	final static String COMPONENT_SUMMARY = "Summary:";
	final static String COMPONENT_DESCRIPTION = "Description:";
	
	final static String mailSpace = "\\>\\s+[a-zA-Z0-9\\s]+";
	
	static class Page{
	String space;
	String title;
	String content;
	};
	// --- <<IS-END-SHARED>> ---
}

