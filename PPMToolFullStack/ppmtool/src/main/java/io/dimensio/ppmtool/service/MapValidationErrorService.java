package io.dimensio.ppmtool.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class MapValidationErrorService {
	
	public ResponseEntity<?> MapValidationService(BindingResult result){
		
		if(result.hasErrors()) {
			//error format/easy to understand,easy to fix
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : result.getFieldErrors()) {
				errorMap.put(error.getField(),error.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(errorMap,HttpStatus.BAD_REQUEST);
		}
		return null;
	}
	
}
