package hookplacer.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdExec {
	public static void main(String[] args) {
		String cmd="robocopy \"C:/Users/phubc/Desktop/plugins/PMD 4.0.5\" C:/Users/phubc/Desktop/Robocopy /E /MT:120";
		System.out.println(cmd);
		System.out.println(EXECUTE(cmd));
		
	}
	public static int EXECUTE(String cmd) {
		String output="";
		Runtime rt =null;
		Process pr = null;
		try {
			rt=Runtime.getRuntime();
			pr = rt.exec("cmd /c "+cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line=null;
			while((line=input.readLine()) != null) {
				output=output+line;
				output=output+"\n";
			}
			pr.waitFor();
			if(output.contains("ERROR")){
				return 0;
			}else
				return 1;	
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}finally {
			rt.runFinalization();
			pr.destroy();
		}
	}
}
