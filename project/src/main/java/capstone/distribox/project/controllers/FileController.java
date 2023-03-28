package capstone.distribox.project.controllers;

import capstone.distribox.project.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileController {

	@Autowired
	FileService fileService;

//	@GetMapping("files")
//	public ResponseBody getTestData() {
//		fileService.getFiles();
//		return null;
//	}
}
