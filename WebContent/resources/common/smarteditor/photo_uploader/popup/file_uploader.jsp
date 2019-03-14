<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.File"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%

String path = "C:/res/temp";
String filename = "";

if(request.getContentLength() > 10*1024*1024 ){
%>
	<script>alert("업로드 용량(총 10Mytes)을 초과하였습니다.");history.back();</script>
<%
	return;

} else {

	try {
		
		//경로 없으면 자동생성
		File directory_file = new File(path);
		
		if (!directory_file.exists()) {
			directory_file.mkdirs();
			System.out.println("디렉토리 생성");
		}

		
		MultipartRequest multi=new MultipartRequest(request, path, 15*1024*1024, "UTF-8", new DefaultFileRenamePolicy());
	
		java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat ("yyyy_MM_dd_HHmmss", java.util.Locale.KOREA);
		int cnt = 1;
		String upfile = (multi.getFilesystemName("Filedata"));
		if (!upfile.equals("")) {
			String dateString = formatter2.format(new java.util.Date());
			String moveFileName = dateString + upfile.substring(upfile.lastIndexOf(".") );
			String fileExt = upfile.substring(upfile.lastIndexOf(".") + 1);
			File sourceFile = new File(path + File.separator + upfile);
			File targetFile = new File(path + File.separator + moveFileName);
			sourceFile.renameTo(targetFile);
			filename = moveFileName;
/* 			System.out.println("upfile : " + upfile);
			System.out.println("targetFile : " + targetFile);
			System.out.println("moveFileName : " + moveFileName);
			System.out.println("filename : " + filename);
			System.out.println("moveFileName : " + moveFileName); */
			
			sourceFile.delete();
			
			%>
			<form id="fileform" name="fileform" method="post">
				<input type="hidden" name="filename" value="<%=filename%>">
				<input type="hidden" name="path" value="<%=path%>">
				<input type="hidden" name="fcode" value="<%=path%>">
			</form>
			<%
		}
	} catch (Exception e) {
		System.out.println("e : " + e.getMessage());
	}
}
%>

<script type="text/javascript">
	function fileAttach(){ 
		f = document.fileform;
		fpath = f.path.value;
	    fname = f.filename.value; 
	    fcode = fpath + "/" + fname;
	    fcode = fcode.substring(fcode.indexOf("/"), fcode.length);
	    
	    try{
             window.opener.pasteHTML(fcode); 
	    	
	    	 window.close();
	    }catch(e){ 
//             alert(e); 
	    }
	}
	fileAttach();
	this.window.close();
</script>
