package com.ruoyi.fabric.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.fabric.client.GatewayClient;
import com.ruoyi.fabric.config.NetworkConfig;
import com.ruoyi.fabric.service.IBlockExplorer;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class FabricExplorerService implements IBlockExplorer {

    @Override
    public long getBlockHeight() {

        long height = -1;
        try {
            Gateway gateway = GatewayClient.getGatewayClient();
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork(NetworkConfig.CHANNEL_NAME);
            height = network.getChannel().queryBlockchainInfo().getHeight();

            System.out.println(height);
        } catch (ProposalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return height;
    }

    @Override
    public long getTransactionCount() {
        long sum = 0;
        long height = -1;
        try {
            Gateway gateway = GatewayClient.getGatewayClient();
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork(NetworkConfig.CHANNEL_NAME);
            height = network.getChannel().queryBlockchainInfo().getHeight();
            for (int i = 0; i < height; i++) {
                BlockInfo bi = network.getChannel().queryBlockByNumber(i);
                sum = sum + bi.getEnvelopeCount();
            }
            System.out.println(sum);
        } catch (ProposalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sum;
    }

    @Override
    public long getBlockBaseInfo() throws IOException {
        // Obtain a smart contract deployed on the network.
        Network network = GatewayClient.getGatewayClient().getNetwork(NetworkConfig.CHANNEL_NAME);
        String channelName = network.getChannel().getName();
        //查询orderer
        Collection<Orderer> orderers = network.getChannel().getOrderers();
        for (Orderer orderer : orderers) {
            System.out.println(orderer.getName() + orderer.getUrl());
        }
        Collection<Peer> peers = network.getChannel().getPeers();
        for (Peer peer : peers) {
            //查询所以peer的名字和url
            System.out.println(peer.getName() + peer.getUrl());
            try {
                //查询peer节点下已经实例化的链码名
                List<Query.ChaincodeInfo> ccs = network.getChannel().queryInstantiatedChaincodes(peer);
                for (Query.ChaincodeInfo cc : ccs) {
                    System.out.println(cc.getName());
                }
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } catch (ProposalException e) {
                e.printStackTrace();
            }
        }
        //查询mspid
        Collection<String> msps = network.getChannel().getPeersOrganizationMSPIDs();
        for (String msp : msps) {
            System.out.println(msp);
        }
        //查询已经初始化，但未实例化的链码名
        Collection<String> chaincodeNames = network.getChannel().getDiscoveredChaincodeNames();
        for (String chaincodeName : chaincodeNames) {
            System.out.println(chaincodeName);
        }

        return 0;
    }

    @Override
    public String getBlockInfoByheight(long height) {
        String jsonStrInfo = null;
        try {
            Gateway gateway = GatewayClient.getGatewayClient();
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork(NetworkConfig.CHANNEL_NAME);
            BlockInfo blockInfo = network.getChannel().queryBlockByNumber(height);
            //将javabean转化成json字符串
            jsonStrInfo = JSON.toJSONString(blockInfo.getBlock().toString());

        } catch (ProposalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonStrInfo;
    }
}
