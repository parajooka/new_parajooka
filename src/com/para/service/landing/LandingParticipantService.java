package com.para.service.landing;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.landing.LandingParticipantDao;
import com.para.object.landing.LandingParticipant;
import com.paraframework.common.PageUtil;
import com.paraframework.common.StringCryPto;

@Service
public class LandingParticipantService implements LandingParticipantDao {
	@Autowired
	private LandingParticipantDao dao;

	@Override
	public int InsertParticipant(LandingParticipant participant) {
		// TODO Auto-generated method stub
		return dao.InsertParticipant(participant);
	}

	@Override
	public int UpdateParticipant(LandingParticipant participant) {
		// TODO Auto-generated method stub
		return dao.UpdateParticipant(participant);
	}

	@Override
	public int DeleteParticipant(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteParticipant(id);
	}

	@Override
	public LandingParticipant getParticipant(int id) throws Exception {
		// TODO Auto-generated method stub
		LandingParticipant participant = dao.getParticipant(id);
		if (participant != null) {
			//요소들을 복호화후 데이터에 삽입
			participant.setCompany(StringCryPto.decrypt("CustomerLandingResult", participant.getCompany()));
			participant.setIp_address(StringCryPto.decrypt("CustomerLandingResult", participant.getIp_address()));
			participant.setName(StringCryPto.decrypt("CustomerLandingResult", participant.getName()));
			participant.setRecord(StringCryPto.decrypt("CustomerLandingResult", participant.getRecord()));
			participant.setStart_time(StringCryPto.decrypt("CustomerLandingResult", participant.getStart_time()));
			participant.setEnd_time(StringCryPto.decrypt("CustomerLandingResult", participant.getEnd_time()));
			
			String test = participant.toString();
			byte[] euckrStringBuffer  = test.getBytes(Charset.forName("UTF-8"));
			// euc-kr 로 변환된 byte 문자열을 다시 유니코드 String 으로 변환.
			// String 생성자의 
			// 첫 번째 인자로 문자열 byte 배열을  넣어주고, 
			// 두 번째 인자로 byte 배열의 인코딩 값을 넣어준다.
			String decodedHelloString = new String(euckrStringBuffer, "UTF-8");
			
			System.out.println(decodedHelloString);
		}
		
		return participant;
	}

	@Override
	public List<LandingParticipant> getParticipantByPaging(PageUtil page) throws Exception {
		// TODO Auto-generated method stub
		
		List<LandingParticipant> list = dao.getParticipantByPaging(page);
	
		if (list != null && list.size() > 0) {
			//요소들을 복호화후 데이터에 삽입
			for (LandingParticipant participant : list) {
				participant.setCompany(StringCryPto.decrypt("CustomerLandingResult", participant.getCompany()));
				participant.setIp_address(StringCryPto.decrypt("CustomerLandingResult", participant.getIp_address()));
				participant.setName(StringCryPto.decrypt("CustomerLandingResult", participant.getName()));
				participant.setStart_time(StringCryPto.decrypt("CustomerLandingResult", participant.getStart_time()));
				participant.setEnd_time(StringCryPto.decrypt("CustomerLandingResult", participant.getEnd_time()));
			}
		}
		
		return list;
	}

	@Override
	public int CountParticipant() {
		// TODO Auto-generated method stub
		return dao.CountParticipant();
	}

	@Override
	public int ValidParticipant(String ip) throws Exception {
		// TODO Auto-generated method stub
		String crypto_ip = StringCryPto.encrypt("CustomerLandingResult", ip);
		return dao.ValidParticipant(crypto_ip);
	}

	@Override
	public LandingParticipant getParticipantByIp(String ip) throws Exception {
		// TODO Auto-generated method stub
		String crypto_ip = StringCryPto.encrypt("CustomerLandingResult", ip);
		LandingParticipant participant = dao.getParticipantByIp(crypto_ip);
		
		participant.setCompany(StringCryPto.decrypt("CustomerLandingResult", participant.getCompany()));
		participant.setIp_address(StringCryPto.decrypt("CustomerLandingResult", participant.getIp_address()));
		participant.setName(StringCryPto.decrypt("CustomerLandingResult", participant.getName()));
		participant.setStart_time(StringCryPto.decrypt("CustomerLandingResult", participant.getStart_time()));
		participant.setEnd_time(StringCryPto.decrypt("CustomerLandingResult", participant.getEnd_time()));
		
		return participant;
	}

}
