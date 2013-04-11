package sample.controller;

import java.io.File;

import sample.model.CodeFile;

public class CodeFileController {
	
	public void readCodeFile(File file) {
		CodeFile code = new CodeFile();
		code.setName(file.getName());
		Double size = (double) (file.length() / 1024);
		code.setSize(size.toString());
	}

}
