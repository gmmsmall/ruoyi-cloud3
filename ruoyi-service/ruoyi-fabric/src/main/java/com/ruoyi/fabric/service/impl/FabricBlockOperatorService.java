package com.ruoyi.fabric.service.impl;

import com.ruoyi.fabric.client.GatewayClient;
import com.ruoyi.fabric.config.NetworkConfig;
import com.ruoyi.fabric.service.IBlockOperator;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

//Service统一接口
@Service
public class FabricBlockOperatorService implements IBlockOperator {

    @Override
    public String invoke(String chaincodeFunction, String[] args) {
        byte[] createCarResult = null;
        try {
            //获取gatewayClient，得到gateway与wallet
            Gateway gateway = GatewayClient.getGatewayClient();

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork(NetworkConfig.CHANNEL_NAME);

            Contract contract = network.getContract(NetworkConfig.CHAINCODE_NAME);

            // Submit transactions that store state to the ledger.
            createCarResult = contract.createTransaction(chaincodeFunction).submit(
                    args);
        } catch (ContractException | IOException | TimeoutException | InterruptedException e) {
            if (e.getMessage().contains("角色id绑定了用户")){
                return "fail";
            }
            return "error";
        }
        return new String(createCarResult, StandardCharsets.UTF_8);
    }

    @Override
    public String query(String chaincodeFunction, String[] args) {
        byte[] queryAllCarsResult = null;

        // Create a gateway connection
        try {

            //获取gatewayClient，得到gateway与wallet
            Gateway gateway = GatewayClient.getGatewayClient();

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork(NetworkConfig.CHANNEL_NAME);
            Contract contract = network.getContract(NetworkConfig.CHAINCODE_NAME);

            // Evaluate transactions that query state from the ledger.
            queryAllCarsResult = contract.evaluateTransaction(chaincodeFunction, args);
        } catch (ContractException | IOException e) {
            e.printStackTrace();
            return "error";
        }
        return new String(queryAllCarsResult, StandardCharsets.UTF_8);
    }


}
