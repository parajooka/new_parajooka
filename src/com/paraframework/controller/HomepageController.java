package com.paraframework.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.handler.HomepageInfoHandler;
import com.paraframework.common.handler.TempJspHandler;
import com.paraframework.object.Homepage;
import com.paraframework.service.HomepageService;

@Controller
@RequestMapping(value= {ControllerCommonMethod.admin_page_path, ControllerCommonMethod.admin_page_path + "/homepage"})
public class HomepageController extends ControllerCommonMethod {
	@Autowired
	private HomepageService service;

	@RequestMapping(value= "", method=RequestMethod.GET)
	public String main(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return RedirectPage(request, ControllerCommonMethod.admin_page_path + "/homepage/index");
	}
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String main(HttpServletRequest request, Model model, Homepage homepage) throws MalformedURLException {
		// TODO Auto-generated method stub
		Homepage orign_homepage = (Homepage) request.getServletContext().getAttribute("homepage");
		
		if (orign_homepage == null) {
			model.addAttribute("homepage", homepage);
		} else {
			model.addAttribute("homepage", orign_homepage);
		}
		
		request.setAttribute("menu", "홈페이지 기본 설정");
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse insertHompage(MultipartHttpServletRequest request, @Valid Homepage homepage, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/homepage/index";
		String success_message = "홈페이지 기본정보를 등록하였습니다.";
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			List<MultipartFile> files = request.getFiles("logo_img");
			files.addAll(request.getFiles("favicon_img"));
			
			for (MultipartFile file : files) {
				if (checkImgfile(file.getOriginalFilename()) == false) {
					res.setMessage("파일은 이미지 파일만 등록이 가능합니다.");
					res.setProcessing_result(true);
	
					return res;
				}
			}
			
			List<String> favicon_img = InjectionFile(request, "C:\\res\\img\\admin\\homepage", "favicon_img");
			List<String> logo_img = InjectionFile(request, "C:\\res\\img\\admin\\homepage", "logo_img");
			
			if (favicon_img != null && favicon_img.size() > 0 && logo_img != null && logo_img.size() > 0) {
				homepage.setFavicon(favicon_img.get(0));
				homepage.setLogo(logo_img.get(0));
			} else {
				res.setMessage("로고와 파비콘을 등록해주세요.");
				res.setProcessing_result(true);
	
				return res;
			}
			
			service.insertHompage(homepage);
			
			//홈페이지를 context에 등록
			HomepageInfoHandler handler = new HomepageInfoHandler();
			handler.UploadHomepageObject(request.getServletContext());
		}
		
		res.setProcessing_result(true);
			
		
		return res;
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deidtHomepage(MultipartHttpServletRequest request, @Valid Homepage homepage, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/homepage/index";
		String success_message = "홈페이지 기본정보를 수정하였습니다.";
		
		Homepage orign_homepage = service.getHomepage();
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			List<MultipartFile> files = new ArrayList<>();
			
			files.addAll(request.getFiles("logo_img"));
			files.addAll(request.getFiles("favicon_img"));
			
			for (MultipartFile file : files) {
				if (checkImgfile(file.getOriginalFilename()) == false) {
					res.setMessage("파일은 이미지 파일만 등록이 가능합니다.");
					res.setProcessing_result(true);
	
					return res;
				}
			}

			List<String> favicon_img = InjectionFile(request, "C:\\res\\img\\admin\\homepage", "favicon_img");
			List<String> logo_img = InjectionFile(request, "C:\\res\\img\\admin\\homepage", "logo_img");
			
			if ((favicon_img == null || favicon_img.size() == 0) && (logo_img == null || logo_img.size() == 0)) { //파일이 아무것도 없을 경우
				homepage.setFavicon(orign_homepage.getFavicon());
				homepage.setLogo(orign_homepage.getLogo());
			} else if (favicon_img != null && favicon_img.size() > 0 && logo_img != null && logo_img.size() > 0) { //파일이 두개모두 업데이트된 경우
				//기존 파일 삭제 -- 시작
				String path = "C:\\res\\img\\admin\\homepage\\"
						+ orign_homepage.getLogo();

				removeFile(path);

				String path2 = "C:\\res\\img\\admin\\homepage\\"
						+ orign_homepage.getFavicon();

				removeFile(path2);
				
				//기존 파일 삭제 -- 종료
				
				homepage.setFavicon(favicon_img.get(0));
				homepage.setLogo(logo_img.get(0));
			} else if (logo_img != null && logo_img.size() > 0) { //로고 이미지가 업로드된 경우
				//기존 파일 삭제 -- 시작
				String path = "C:\\res\\img\\admin\\homepage\\"
						+ orign_homepage.getLogo();
				
				removeFile(path);
				//기존 파일 삭제 -- 종료
				
				homepage.setLogo(logo_img.get(0));
				homepage.setFavicon(orign_homepage.getFavicon());
			} else if (favicon_img != null && favicon_img.size() > 0) {
				String path = "C:\\res\\img\\admin\\homepage\\"
						+ orign_homepage.getFavicon();
				
				removeFile(path);
				
				homepage.setLogo(orign_homepage.getLogo());
				homepage.setFavicon(favicon_img.get(0));
			}
			
			service.updateHomepage(homepage);
			
			//홈페이지를 context에 등록
			HomepageInfoHandler handler = new HomepageInfoHandler();
			handler.UploadHomepageObject(request.getServletContext());
			
			TempJspHandler jspHandler = new TempJspHandler();
			jspHandler.ReCreateAllTempJsp(request.getServletContext());
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
}
