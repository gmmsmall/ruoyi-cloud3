package com.ruoyi.fabric.config;

import lombok.Data;

import java.io.File;

@Data
public class NetworkConfig {
    public static final String DATA_PATH ="src/main";  //"/home/dapp/.data","D:/data"，src/main/;

    public static final String CONFIG_PATH = DATA_PATH+"/resources/connection-org1.yml";

    public static final String WALLET_PATH = DATA_PATH+"/resources/wallet";

    public static final String ORG1_MSP = "Org1MSP";

    public static final String ORG1 = "org1.peer0";

    public static final String ADMIN = "admin";

    public static final String ADMIN_PASSWORD = "adminpw";

    public static final String ORG1_USR_BASE_PATH = "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org1.mingbyte.com" + File.separator + "users" + File.separator + "Admin@org1.mingbyte.com"
            + File.separator + "msp";
    public static final String ORG1_USR_ADMIN_PK = ORG1_USR_BASE_PATH + File.separator + "keystore";
    public static final String ORG1_USR_ADMIN_CERT = ORG1_USR_BASE_PATH + File.separator + "admincerts";

    public static final String CHANNEL_NAME = "dev-academic-channel";

    public static final String CA_ORG1_NAme =  "ca.org1.mingbyte.com";
    public static final String CA_ORG1_URL = "http://192.168.8.95:7054";

    public static final String ORDERER_NAME = "orderer0.mingbyte.com";
    public static final String ORDERER_URL = "grpcs://192.168.8.95:7050";

    public static final String ORG1_PEER_0 = "peer0.org1.mingbyte.com";
    public static final String ORG1_PEER_0_URL = "grpcs://192.168.8.95:7051";

    public static final String CHAINCODE_NAME = "dev_academicport";


}
