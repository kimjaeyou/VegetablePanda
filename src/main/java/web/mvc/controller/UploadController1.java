package web.mvc.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import web.mvc.dto.FileVO1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@Slf4j
public class UploadController1 {
	@RequestMapping("/uploadForm")
	public void formFile() {	   
	}

	@PostMapping("/upload1")
	public ModelAndView saveFile(FileVO1 vo) {
	     String fileName =  vo.getUploadFile().getOriginalFilename();
       log.info(vo.getSubject());
	     byte[] content = null;
	     ModelAndView mav = new ModelAndView();
	     mav.setViewName("uploadForm");
		 String path = "/uploadtest";
		 File isDir = new File(path);
		 if (!isDir.isDirectory()) {
			isDir.mkdirs();
		 }
	     try {
	    	 content =  vo.getUploadFile().getBytes();
	    	 File f = new File("/uploadtest/"+fileName);
	    	 if ( f.exists() ) {
	    		 mav.addObject("msg", fileName + " : 파일이 이미 존재해요!!");
	    	 } else {
	    		 FileOutputStream fos = new FileOutputStream(f);
	    		 fos.write(content);
	    		 fos.close();
	    		 mav.addObject("msg", fileName + ": 파일이 저장되었어요!!");
	    	 }
	     } catch (IOException e) {
	    	 e.printStackTrace();
	    	 mav.addObject("msg", "오류가 발생했어요!!");
	     }	    
	    return mav;
	}
}

