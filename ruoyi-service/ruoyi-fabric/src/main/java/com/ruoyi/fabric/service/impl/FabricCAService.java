package com.ruoyi.fabric.service.impl;

import com.ruoyi.fabric.client.CAClient;
import com.ruoyi.fabric.config.NetworkConfig;
import com.ruoyi.fabric.service.IBlockCA;
import com.ruoyi.fabric.utils.Util;
import com.ruoyi.system.domain.UserContext;
import org.springframework.stereotype.Service;


@Service
public class FabricCAService implements IBlockCA {
    @Override
    public String enrollAdmin() {

        try {
            Util.cleanUp();
            String caUrl = NetworkConfig.CA_ORG1_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(NetworkConfig.ADMIN);
            adminUserContext.setAffiliation(NetworkConfig.ORG1);
            adminUserContext.setMspId(NetworkConfig.ORG1_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(NetworkConfig.ADMIN, NetworkConfig.ADMIN_PASSWORD);
            Util.convertWallet(adminUserContext);

            // Enroll AdminTLS to Org1MSP
            UserContext adminUserTLSContext = new UserContext();
            adminUserTLSContext.setName(NetworkConfig.ADMIN);
            adminUserTLSContext.setAffiliation(NetworkConfig.ORG1);
            adminUserTLSContext.setMspId(NetworkConfig.ORG1_MSP);
            caClient.setAdminUserContext(adminUserTLSContext);
            adminUserTLSContext = caClient.enrollAdminUserTLS(NetworkConfig.ADMIN, NetworkConfig.ADMIN_PASSWORD);
            Util.convertTLSWallet(adminUserTLSContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @Override
    public String registerUser(UserContext userContext) {
        try {
            // 未开启tls
            Util.cleanUp();
            String caUrl = NetworkConfig.CA_ORG1_URL;
            CAClient caClient = new CAClient(caUrl, null);

            /**   ca开启tls
             Util.cleanUp();
             String caUrl = NetworkConfig.CA_ORG1_URL;
             //add tls props
             Properties props = new Properties();
             props.put("pemFile",
             "src/main/resources/crypto-config/peerOrganizations/baoneng.mingbyte.com/tlsca/tlsca.baoneng.mingbyte.com-cert.pem");
             props.put("allowAllHostNames", "true");
             CAClient caClient = new CAClient(caUrl, props);
             **/
            /**(测试版本)
             YmlUtil ymlUtil = new YmlUtil();
             //System.out.println((ymlUtil.getValue("connection-baoneng.yml", "certificateAuthorities")));
             Object pemFile = ymlUtil.getValue("connection-baoneng.yml", "certificateAuthorities:ca.baoneng.mingbyte.com:tlsCACerts:pem");
             System.out.println(pemFile);
             String caUrl = NetworkConfig.CA_ORG1_URL;
             //add tls props
             Properties props = new Properties();
             props.setProperty("pemFile",pemFile.toString());

             props.put("allowAllHostNames", "true");
             CAClient caClient = new CAClient(caUrl, props);
             **/
            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(NetworkConfig.ADMIN);
            adminUserContext.setAffiliation(NetworkConfig.ORG1);
            adminUserContext.setMspId(NetworkConfig.ORG1_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(NetworkConfig.ADMIN, NetworkConfig.ADMIN_PASSWORD);

            // Register and Enroll user to Org1MSP
            String eSecret = caClient.registerUser(userContext.getName(), NetworkConfig.ORG1);
            //Set<String> role = new HashSet<String>();
            //role.add("user");
            //userContext.setRoles(role);
            userContext = caClient.enrollUser(userContext, eSecret);
            //deposit in wallet
            Util.convertWallet(userContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @Override
    public String reEnroll(UserContext userContext) {
        try {
            // 未开启tls
            Util.cleanUp();
            String caUrl = NetworkConfig.CA_ORG1_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(NetworkConfig.ADMIN);
            adminUserContext.setAffiliation(NetworkConfig.ORG1);
            adminUserContext.setMspId(NetworkConfig.ORG1_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(NetworkConfig.ADMIN, NetworkConfig.ADMIN_PASSWORD);

            // Register and Enroll user to Org1MSP
            //String eSecret = caClient.registerUser(userContext.getName(), NetworkConfig.ORG1);
            //userContext.setEnrollmentSecret(eSecret);
            //Set<String> role = new HashSet<String>();
            //role.add("user");
            //userContext.setRoles(role);
            userContext = caClient.reEnroll(userContext);
            //deposit in wallet
            Util.convertWallet(userContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
