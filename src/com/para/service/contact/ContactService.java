package com.para.service.contact;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.contact.ContactDao;
import com.para.object.contact.Contact;
import com.para.object.landing.LandingParticipant;
import com.paraframework.common.PageUtil;
import com.paraframework.common.StringCryPto;

@Service
public class ContactService implements ContactDao {
	@Autowired
	private ContactDao dao;

	@Override
	public int InsertContact(Contact contact) {
		// TODO Auto-generated method stub
		return dao.InsertContact(contact);
	}

	@Override
	public int UpdateContact(Contact contact) {
		// TODO Auto-generated method stub
		return dao.UpdateContact(contact);
	}

	@Override
	public int DeleteContact(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteContact(id);
	}

	@Override
	public Contact getContactById(int id) throws Exception {
		// TODO Auto-generated method stub
		Contact c = dao.getContactById(id);
		if (c != null) {
			LandingParticipant participant = c.getParticipant();
			participant.setCompany(StringCryPto.decrypt("CustomerLandingResult", participant.getCompany()));
			participant.setName(StringCryPto.decrypt("CustomerLandingResult", participant.getName()));
		}
		return c;
	}

	@Override
	public List<Contact> getContactByYearAndMonth(String start_date, String end_date) {
		// TODO Auto-generated method stub
		return dao.getContactByYearAndMonth(start_date, end_date);
	}

	@Override
	public Contact getContactByParticipant(int id) throws Exception {
		// TODO Auto-generated method stub
		Contact c = dao.getContactByParticipant(id);
		if (c != null) {
			LandingParticipant participant = c.getParticipant();
			participant.setCompany(StringCryPto.decrypt("CustomerLandingResult", participant.getCompany()));
			participant.setName(StringCryPto.decrypt("CustomerLandingResult", participant.getName()));
		}
		return c;
	}

	@Override
	public int CountByParticipant(int id) {
		// TODO Auto-generated method stub
		return dao.CountByParticipant(id);
	}

	@Override
	public Contact getContactByDate(String date) throws Exception {
		// TODO Auto-generated method stub
		Contact c = dao.getContactByDate(date);
		if (c != null) {
			LandingParticipant participant = c.getParticipant();
			participant.setCompany(StringCryPto.decrypt("CustomerLandingResult", participant.getCompany()));
			participant.setName(StringCryPto.decrypt("CustomerLandingResult", participant.getName()));
		}
		return c;
	}

	@Override
	public int ValidContactByDate(String date) {
		// TODO Auto-generated method stub
		return dao.ValidContactByDate(date);
	}

	@Override
	public int CountContact() {
		// TODO Auto-generated method stub
		return dao.CountContact();
	}

	@Override
	public List<Contact> getContactByPaging(PageUtil page) throws Exception {
		// TODO Auto-generated method stub
		List<Contact> contact_list = dao.getContactByPaging(page);
		
		if (contact_list != null && contact_list.size() > 0) {
			for (Contact c : contact_list) {
				if (c != null) {
					LandingParticipant participant = c.getParticipant();
					participant.setCompany(StringCryPto.decrypt("CustomerLandingResult", participant.getCompany()));
					participant.setName(StringCryPto.decrypt("CustomerLandingResult", participant.getName()));
				}
			}
		}
		
		return contact_list;
	}

	@Override
	public String getMemoByContactId(int id) {
		// TODO Auto-generated method stub
		return dao.getMemoByContactId(id);
	}
}
