package com.lee.my.helper.tools;

import java.io.DataOutputStream;

public class Tools {

	public static boolean runRootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
				process.destroy();
			} catch (Exception e) {
				// nothing
				e.printStackTrace();
			}
		}
		return true;
	}

}
