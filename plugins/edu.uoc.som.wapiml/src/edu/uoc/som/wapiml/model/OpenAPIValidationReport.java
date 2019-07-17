package edu.uoc.som.wapiml.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;

public class OpenAPIValidationReport {

	private boolean success = false; 
	private List<String> warning;
	private List<String> error;
	private List<String> fatal;
	private ProcessingReport report;
	
	
	public OpenAPIValidationReport(ProcessingReport report) {
		this.report = report;
		
		success = report.isSuccess();
		warning = new ArrayList<>();
		error = new ArrayList<>();
		fatal = new ArrayList<>();
		report.forEach((e)-> {
			if(e.getLogLevel().equals(LogLevel.ERROR))
				error.add(buildMessage(e));
			if(e.getLogLevel().equals(LogLevel.WARNING))
				warning.add(buildMessage(e));
			if(e.getLogLevel().equals(LogLevel.FATAL))
				fatal.add(buildMessage(e));
			
			
		});
		
	}

	private String buildMessage(ProcessingMessage processingMessage) {
		StringBuilder message = new StringBuilder();
		message.append("pointer: \"");
		message.append(processingMessage.asJson().get("instance").get("pointer").asText());
		message.append("\",\n");
		message.append("message: ");
		message.append(processingMessage.getMessage());
		message.append("\n");
		return message.toString();
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


	public ProcessingReport getReport() {
		return report;
	}


	public void setReport(ProcessingReport report) {
		this.report = report;
	}


	
	
	

	
}
