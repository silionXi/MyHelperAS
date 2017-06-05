package com.lee.my.helper.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CMDExecute {

	public synchronized String run(String[] cmd, String workdirectory)
			throws IOException {
		String result = null;
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			// ≈‰÷√1∏ˆ¬∑æ∂
			if (workdirectory != null)
				builder.directory(new File(workdirectory));
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1);
			result = new String(re);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
