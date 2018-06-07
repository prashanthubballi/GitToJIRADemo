package hookplacer.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import hookplacer.util.CmdExec;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class HookHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	final static String hooksSRC="//a0396-smd0004-s/TeamMaint_Mang/ASPALRTB/Work/phubc";
	final static String localSRC="C:/Git";
	final static String gitHookLog="C:/Users/"+System.getProperty("user.name")+"/.gitHooklog";
	ArrayList<String> HooksExistPath=new ArrayList<String>();
	public HookHandler() {
	}
	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		BufferedReader br =null;
		try {
			File fi = new File(gitHookLog);
			fi.createNewFile();
			br = new BufferedReader(new FileReader(fi));
			String readLine = "";
			while ((readLine = br.readLine()) != null) {
				HooksExistPath.add(readLine);
			}
		}
		catch(Exception e){
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		List<String>result=copyHooks(localSRC);
		String message="";
		if(result.size()>0){
			String msg1="Error :Please check access to following directories ";
			for(int i=0;i<result.size();i++)
				message=message+" "+result.get(i);

			message=msg1+message;
		}else{
			message="Hooks Placed Sucessfully";
		}
		MessageDialog.openInformation(
				window.getShell(),
				"HookPlacer",message);
		return null;
	}
	public List<String> copyHooks(String directoryName) {
		Writer output=null;
		List<String> errList=new ArrayList<String>();
		try{
			output = new BufferedWriter(new FileWriter(gitHookLog, true));
			File directory = new File(directoryName);

			// get all the files from a directory
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isDirectory() && file.getName().equals(".git")) {
					String cmd="robocopy \""+hooksSRC+"\" \""+file.getAbsolutePath()+"\" /E ";
					int res=CmdExec.EXECUTE(cmd);
					if(res==0)
						errList.add(file.getAbsolutePath());
				}else if(file.isDirectory() && !HooksExistPath.contains(file.getParent())){
					output.append(file.getAbsolutePath());
					HooksExistPath.add(file.getAbsolutePath());
					output.append('\n');
					copyHooks(file.getAbsolutePath());
				}
			}
			return errList;
		}catch(Exception e){
			e.printStackTrace();;
		}finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return errList;
	}
}
