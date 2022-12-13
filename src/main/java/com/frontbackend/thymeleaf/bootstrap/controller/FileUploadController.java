package com.frontbackend.thymeleaf.bootstrap.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.ChargeBackFile;
import com.frontbackend.thymeleaf.bootstrap.rules.ValidateRules;

@Controller
@RequestMapping("/EDI844")
public class FileUploadController {

	public ValidateRules rules = new ValidateRules(); 
	
    @GetMapping
    public String main() {
        return "index";
    }

    @Value("${localDir}")
    public String localDir;
    
    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        
        File tempFile = new File(localDir , file.getOriginalFilename());
        try {
			file.transferTo(tempFile);
			
			List<File> ediFiles = new ArrayList<>();
			ediFiles.add(tempFile);
			
//			rules.validateEDI844Files(ediFiles);
			
			List<String> lines = FileUtils.readLines(tempFile);
			
			ChargeBackFile cbFile = new ChargeBackFile();
			cbFile.setName(file.getOriginalFilename() );
			cbFile.setStartDate(Calendar.getInstance().getTime());
			cbFile.setTotalRecords(Long.valueOf(lines.size()));
			cbFile.setTotalRejects(Long.valueOf(lines.size()));
			
			TypeReference<List<ChargeBackFile>> ref = new TypeReference<List<ChargeBackFile>>() {};
			File filesJson = new File(localDir, "files.json");
			ObjectMapper objectMapper = new ObjectMapper();
			if(!filesJson.exists()) {
				filesJson.createNewFile();
				List<ChargeBackFile> charbackFiles = new ArrayList<ChargeBackFile>();
				charbackFiles.add(cbFile);
				objectMapper.writeValue(filesJson, charbackFiles);
			}else {
				List<ChargeBackFile> charbackFiles = objectMapper.readValue(filesJson, ref);
				charbackFiles.add(cbFile);
				objectMapper.writeValue(filesJson, charbackFiles);
			}
			
		} catch (IllegalStateException | IOException /* | IllegalAccessException */ e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			tempFile.deleteOnExit();
		}
       

        return "redirect:/";
    }
}