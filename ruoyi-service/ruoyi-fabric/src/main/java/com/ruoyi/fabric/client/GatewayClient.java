package com.ruoyi.fabric.client;

import com.ruoyi.fabric.config.NetworkConfig;
import com.ruoyi.fabric.utils.YamlUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.hyperledger.fabric.gateway.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class GatewayClient{
    private static volatile GatewayClient gatewayclient = null;
    private static Map<String, Gateway> gatewayClientMap = null;
    private static YamlUtils yamlUtils = null;
    private   GatewayClient() {}
    public static Gateway getGatewayClient() throws IOException {
        if (gatewayclient == null ) {
            synchronized (GatewayClient.class) {
                if (gatewayclient == null) {
                    try {
                        gatewayclient = new GatewayClient();

                        //set tls key and cert to client
                        //YamlUtils.setTLSToConfig("admin");

                        // Load an existing wallet holding identities used to access the network.
                        URI walletUri = null;
                        try {
                            walletUri = GetResource.class.getClassLoader().getResource("wallet").toURI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Path walletDirectory = Paths.get(walletUri);
                        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

                        // Path to a common connection profile describing the network.
                        URI uri = null;
                        try {
                            uri = GetResource.class.getClassLoader().getResource("connection-org1.yml").toURI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Path networkConfigFile = Paths.get(uri);
                        File yml = networkConfigFile.toFile();
                        //File yml= ResourceUtils.getFile("classpath:connection-org1.yml");
                        InputStream fileIn = new FileInputStream(yml);//将path读取的文件转为InputStream数据流

                        // Configure the gateway connection used to access the network.
                        Gateway.Builder builder = null;
                        builder = Gateway.createBuilder().identity(wallet, "admin").networkConfig(fileIn);//不直接读地址文件，而是地址对应文件的数据流

                        // Create a gateway connection
                        Gateway gateway = builder.connect();
                        Map<String, Gateway> gatewayClientMap =  new HashMap<String, Gateway>();

                        //add gatewayclient to gatewayclientmap
                        gatewayClientMap.put("admin",gateway);
                        gatewayclient.setGatewayClientMap(gatewayClientMap);

                        //add config-object to gatewayclient
                        yamlUtils.setYmlFile(yml);
                        gatewayclient.setYamlUtils(yamlUtils);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            if(gatewayclient.getGatewayClientMap().get("admin") == null){

                try {
                    //给客户端注入tls私钥和证书
                    //YamlUtils.setTLSToConfig("admin");

                    // Load an existing wallet holding identities used to access the network.
                    Path walletDirectory = Paths.get(NetworkConfig.WALLET_PATH);

                    Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

                    // Path to a common connection profile describing the network.
                    Path networkConfigFile = Paths.get(NetworkConfig.CONFIG_PATH);

                    // Configure the gateway connection used to access the network.
                    Gateway.Builder builder = null;
                    builder = Gateway.createBuilder().identity(wallet, "admin").networkConfig(networkConfigFile);

                    // Create a gateway connection
                    Gateway gateway = builder.connect();

                    //add gatewayclient to gatewayclientmap
                    gatewayclient.getGatewayClientMap().put("admin",gateway);

                    //add config-object to gatewayclient
                    File yml = networkConfigFile.toFile();
                    yamlUtils.setYmlFile(yml);
                    gatewayclient.setYamlUtils(yamlUtils);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        System.out.println("current-gateway-size:"+gatewayclient.getGatewayClientMap().size()+"--------------");
        return gatewayclient.getGatewayClientMap().get("admin");
    }

    public  YamlUtils getYamlUtils() {
        return yamlUtils;
    }

    private  void setYamlUtils(YamlUtils yamlUtils) {
        GatewayClient.yamlUtils = yamlUtils;
    }

    public  Map<String, Gateway> getGatewayClientMap() {

        return gatewayClientMap;
    }

    private   void setGatewayClientMap(Map<String, Gateway> gatewayClientMap) {
        GatewayClient.gatewayClientMap = gatewayClientMap;
    }

}