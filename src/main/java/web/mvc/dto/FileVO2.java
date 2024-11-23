package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileVO2 {
	private  MultipartFile[] uploadFiles;
}
