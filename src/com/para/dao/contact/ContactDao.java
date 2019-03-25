package com.para.dao.contact;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.para.object.contact.Contact;
import com.paraframework.common.PageUtil;

public interface ContactDao {
	public int InsertContact(Contact contact);
	
	public int UpdateContact(Contact contact);
	
	public int DeleteContact(int id);
	
	public Contact getContactById(int id) throws Exception;
	
	public Contact getContactByParticipant(int id) throws Exception;
	
	public int CountByParticipant(int id);
	
	public List<Contact> getContactByYearAndMonth(@Param("start_date") String start_date, @Param("end_date") String end_date);
	
	public Contact getContactByDate(@Param("date") String date) throws Exception;
	
	public int ValidContactByDate(@Param("date") String date);
	
	public int CountContact();
	
	public List<Contact> getContactByPaging(PageUtil page) throws Exception;
	
	public String getMemoByContactId(int id);
	
	public Contact getContactBySoonTimer() throws Exception;
}
