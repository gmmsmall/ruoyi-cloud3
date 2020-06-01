package com.ruoyi.acad.client;

import com.ruoyi.acad.domain.AcadHomepage;
import com.ruoyi.acad.domain.Email;
import com.ruoyi.acad.domain.Phone;
import com.ruoyi.acad.domain.Sns;
import lombok.Data;

import java.util.List;

@Data
public class ClientEmail {

	private List<Phone> phoneList;
	
	private Sns sns;
	
	private List<Email> emailList;
	
	private AcadHomepage acadHomepage;
	
//	public ClientEmail (Email email) {
//        try {	
//            BeanUtils.copyProperties(this, email);
//        } catch (Exception e) {
//            throw new IllegalStateException("email entity construction is error!");
//        }
//    }
}
