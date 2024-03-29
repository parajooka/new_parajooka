package com.para.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paraframework.common.ControllerCommonMethod;

@Controller
@RequestMapping(value="/video")
public class VideoController extends ControllerCommonMethod {

	/*@RequestMapping(value="/{video}.{file_type}", method=RequestMethod.GET)
	public void index(HttpServletRequest request, HttpServletResponse response, @PathVariable String video, @PathVariable String file_type) {
		String video_name = video + "." + file_type;
		
		if (video_name != null && video_name.length() > 1) {
			String path = "C:\\res\\movie\\" + video_name;
			
			File file = new File(path);
			
			if (file.exists()) {
				try {
					MultipartFileSender.fromFile(file).with(request).with(response).serveResource();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				alertMessageByString("삭제되었거나 존재하지않는 영상입니다.", "/", request, response);
			}
		}
	}*/
	
	@RequestMapping(value="/{video}.{file_type}", method=RequestMethod.GET) 
	public String getContentMediaVideo (HttpServletRequest request, HttpServletResponse response, @PathVariable String video, @PathVariable String file_type) throws UnsupportedEncodingException, IOException  {
		String video_name = video + "." + file_type;
		String path = "C:\\res\\movie\\" + video_name;
		// 확장자 확인 // 
		//progressbar 에서 특정 위치를 클릭하거나 해서 임의 위치의 내용을 요청할 수 있으므로 //파일의 임의의 위치에서 읽어오기 위해 RandomAccessFile 클래스를 사용한다. 
		//해당 파일이 없을 경우 예외 발생 
		File file = new File(path);
		
		if (file.exists()) {
			if( ! file.exists() ) throw new FileNotFoundException(); 
			RandomAccessFile randomFile = new RandomAccessFile(file, "r"); 
			long rangeStart = 0; 
			//요청 범위의 시작 위치 
			long rangeEnd = 0; 
			//요청 범위의 끝 위치 
			boolean isPart=false; 
			//부분 요청일 경우 true, 전체 요청의 경우 false //randomFile 을 클로즈 하기 위하여 try~finally 사용 
			try { 
				//동영상 파일 크기 
				long movieSize = randomFile.length();
				//스트림 요청 범위, request의 헤더에서 range를 읽는다. 
				String range = request.getHeader("range"); 
				//브라우저에 따라 range 형식이 다른데, 기본 형식은 "bytes={start}-{end}" 형식이다. //range가 null이거나, reqStart가 0이고 end가 없을 경우 전체 요청이다. 
				//요청 범위를 구한다. 
				if(range!=null) { //처리의 편의를 위해 요청 range에 end 값이 없을 경우 넣어줌 
					if(range.endsWith("-")){ 
						range = range+(movieSize - 1); 
					} 
					
					int idxm = range.trim().indexOf("-"); 
					//"-" 위치 
					rangeStart = Long.parseLong(range.substring(6,idxm)); 
					rangeEnd = Long.parseLong(range.substring(idxm+1)); 
					
					if(rangeStart > 0){
						isPart = true; 
					} 
					
				}else{ 
					//range가 null인 경우 동영상 전체 크기로 초기값을 넣어줌. 0부터 시작하므로 -1 
					rangeStart = 0; 
					rangeEnd = movieSize - 1; 
				} 
				//전송 파일 크기 
				long partSize = rangeEnd - rangeStart + 1; 
				//전송시작
				response.reset(); 
				//전체 요청일 경우 200, 부분 요청일 경우 206을 반환상태 코드로 지정 
				response.setStatus(isPart ? 206 : 200); 
				//mime type 지정 
				response.setContentType("video/mp4"); 
				//전송 내용을 헤드에 넣어준다. 마지막에 파일 전체 크기를 넣는다. 
				response.setHeader("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+movieSize); 
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("Content-Length", ""+partSize); 
				OutputStream out = response.getOutputStream(); 
				//동영상 파일의 전송시작 위치 지정 
				randomFile.seek(rangeStart); 
				//파일 전송... java io는 1회 전송 byte수가 int로 지정됨 
				//동영상 파일의 경우 int형으로는 처리 안되는 크기의 파일이 있으므로 
				//8kb로 잘라서 파일의 크기가 크더라도 문제가 되지 않도록 구현 
				int bufferSize = 8*1024;
				byte[] buf = new byte[bufferSize];
				do { 
					int block = partSize > bufferSize ? bufferSize : (int)partSize; 
					int len = randomFile.read(buf, 0, block); 
					out.write(buf, 0, len); 
					partSize -= block; 
				}
				
				while(partSize > 0); 
				
			} catch(IOException e) { 
					//전송 중에 브라우저를 닫거나, 화면을 전환한 경우 종료해야 하므로 전송취소. 
					// progressBar를 클릭한 경우에는 클릭한 위치값으로 재요청이 들어오므로 전송 취소. 
			}finally{ 
				randomFile.close(); 
			} 
		} else {
			alertMessage("존재하지 않는 경로입니다.", request, response);
		}
		
		return null; 
	}
}

