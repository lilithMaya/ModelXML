package sample.controller;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import sample.model.CodeFile;
import sample.model.ExtensionMapper;

public class CodeFileController {
	
	private ExtensionMapper mapper = new ExtensionMapper();
	
	private String getFileExtension(String fileName) {
		String extension = "";

		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}
	
	public CodeFile readCodeFile(File file) {
		CodeFile code = new CodeFile();
		code.setName(file.getName());
		Double size =  (double) file.length() / 1024;
		DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
		code.setSize(df.format(size) + "KB");
		code.setPath(file.getAbsolutePath());
		code.setType(mapper.getType(this.getFileExtension(file.getName())));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		code.setDateModified(sdf.format(file.lastModified()));
		return code;
	}
	
	public CodeFile readCodeFile(File file, String folder) {
		CodeFile code = new CodeFile();
		code.setName(file.getName());
		Long size =  (long) file.length() / 1024;
		code.setSize(size.toString() + "KB");
		code.setPath(file.getAbsolutePath());
		code.setType(mapper.getType(this.getFileExtension(file.getName())));
		return code;
	}

}
