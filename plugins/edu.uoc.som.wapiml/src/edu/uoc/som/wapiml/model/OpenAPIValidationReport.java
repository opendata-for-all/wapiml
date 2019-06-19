package edu.uoc.som.wapiml.model;

import java.util.ArrayList;
import java.util.List;

import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;

public class OpenAPIValidationReport {

	private boolean success = false; 
	private List<String> warning;
	private List<String> error;
	private List<String> fatal;
	
	
	public OpenAPIValidationReport(ProcessingReport report) {
		success = report.isSuccess();
		warning = new ArrayList<>();
		error = new ArrayList<>();
		fatal = new ArrayList<>();
		report.forEach((e)-> {
			if(e.getLogLevel().equals(LogLevel.ERROR))
				error.add(e.getMessage());
			if(e.getLogLevel().equals(LogLevel.WARNING))
				warning.add(e.getMessage());
			if(e.getLogLevel().equals(LogLevel.FATAL))
				fatal.add(e.getMessage());
			
			
		});
		
	}


	public boolean isSuccess() {
		return success;
	}


	public List<String> getWarning() {
		return warning;
	}


	public List<String> getError() {
		return error;
	}


	public List<String> getFatal() {
		return fatal;
	}


	
	
	

	
}
