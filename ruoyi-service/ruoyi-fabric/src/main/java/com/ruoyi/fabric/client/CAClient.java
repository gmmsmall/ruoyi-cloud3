package com.ruoyi.fabric.client;

import com.ruoyi.fabric.bean.UserContext;
import com.ruoyi.fabric.utils.Util;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CAClient {
    String caUrl;
    Properties caProperties;
    HFCAClient instance;
    UserContext adminContext;

    public UserContext getAdminUserContext() {
        return adminContext;
    }

    public void setAdminUserContext(UserContext userContext) {
        this.adminContext = userContext;
    }

    public CAClient(String caUrl, Properties caProperties) throws MalformedURLException, IllegalAccessException, InstantiationException, ClassNotFoundException, CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException {
        this.caUrl = caUrl;
        this.caProperties = caProperties;
        init();
    }

    public void init() throws MalformedURLException, IllegalAccessException, InstantiationException, ClassNotFoundException, CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException {
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        instance = HFCAClient.createNewInstance(caUrl, caProperties);
        instance.setCryptoSuite(cryptoSuite);
    }

    public UserContext enrollAdminUser(String username, String password) throws Exception {
        UserContext userContext = Util.readUserContext(adminContext.getAffiliation(), username);
        if (userContext != null) {
            Logger.getLogger(CAClient.class.getName()).log(Level.WARNING, "CA -" + caUrl + " admin is already enrolled.");
            return userContext;
        }
        Enrollment adminEnrollment = instance.enroll(username, password);
        adminContext.setEnrollment(adminEnrollment);
        Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + caUrl + " Enrolled Admin.");
        Util.writeUserContext(adminContext);
        return adminContext;
    }
    public UserContext enrollAdminUserTLS(String username, String password) throws Exception {
        UserContext userContext = Util.readUserContext(adminContext.getAffiliation(), username+"TLS");
        if (userContext != null) {
            Logger.getLogger(CAClient.class.getName()).log(Level.WARNING, "CA -" + caUrl + " admin is already enrolled.");
            return userContext;
        }
        EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.setProfile("tls");
        Enrollment adminEnrollment = instance.enroll(username, password,enrollmentRequestTLS);
        adminContext.setEnrollment(adminEnrollment);
        Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + caUrl + " Enrolled AdminTLS.");
        Util.writeUserContext(adminContext);
        return adminContext;
    }

    public String registerUser(String username, String organization) throws Exception {
        UserContext userContext = Util.readUserContext(adminContext.getAffiliation(), username);
        if (userContext != null) {
            Logger.getLogger(CAClient.class.getName()).log(Level.WARNING, "CA -" + caUrl +" User " + username+ " is already registered.");
            return null;
        }
        RegistrationRequest rr = new RegistrationRequest(username, organization);
        //rr.setSecret(password);//设置自定义密码
        String enrollmentSecret = instance.register(rr, adminContext);
        Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + caUrl + " Registered User - " + username);
        return enrollmentSecret;
    }

    public UserContext enrollUser(UserContext user, String secret) throws Exception {
        UserContext userContext = Util.readUserContext(adminContext.getAffiliation(), user.getName());
        if (userContext != null) {
            Logger.getLogger(CAClient.class.getName()).log(Level.WARNING, "CA -" + caUrl + " User " + user.getName()+" is already enrolled");
            return userContext;
        }
        Enrollment enrollment = instance.enroll(user.getName(), secret);
        user.setEnrollment(enrollment);
        Util.writeUserContext(user);
        Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + caUrl +" Enrolled User - " + user.getName());
        return user;
    }
    //重新注册
    public UserContext reEnroll(UserContext user)throws Exception{
        UserContext userContext = Util.readUserContext(adminContext.getAffiliation(), user.getName());
        if (userContext != null) {
            Logger.getLogger(CAClient.class.getName()).log(Level.WARNING, "CA -" + caUrl + " User " + user.getName()+" is already enrolled");
            return userContext;
        }
        Enrollment reEnrollment = instance.reenroll(user);
        System.out.println("重新登记证书成功");
        user.setEnrollment(reEnrollment);
        Util.writeUserContext(user);
        Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + caUrl +" Enrolled User - " + user.getName());
        return user;
    }



}
