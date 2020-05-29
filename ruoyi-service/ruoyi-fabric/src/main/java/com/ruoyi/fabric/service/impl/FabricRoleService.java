package com.ruoyi.fabric.service.impl;

import com.ruoyi.fabric.service.IBlockRole;
import com.ruoyi.fabric.utils.Page;
import com.ruoyi.system.domain.Aos;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.Token;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FabricRoleService implements IBlockRole {

    @Override
    public String add(SysRole role) {
//        byte[] createCarResult =null;
//        try {
//            //获取gatewayClient，得到gateway与wallet
//            Gateway gateway =  GatewayClient.getGatewayClient("user1586403082831");
//
//            // Obtain a smart contract deployed on the network.
//            Network network = gateway.getNetwork(NetworkConfig.CHANNEL_NAME);
//
//            Contract contract = network.getContract(NetworkConfig.CHAINCODE_NAME);
//
//            // Submit transactions that store state to the ledger.
//            createCarResult = contract.createTransaction("createCar").submit(
//                    role.getRoleName());
//                    //car.getCarNum(),
//                    //car.getCarName(),
//                    //car.getCarBrand(),
//                    //car.getCarColour(),
//                    //car.getCarOwner());//"CAR10", "VW", "Polo", "Grey", "Mary"
//
//        } catch (ContractException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//        return new String(createCarResult, StandardCharsets.UTF_8);
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public String update(SysRole role) {
        //具体实现暂时直接返回字段
        return "0";
    }

    @Override
    public Page query(int pageNum, int pageSize, String roleName, String remark) {
        String[] js = new String[2];
        js[0] = "s1";
        js[1] = "s2";

        SysRole role = new SysRole();
        role.setAosNos(js);
        role.setRemark("描述信息");
        role.setRoleId(278223223L);
        role.setRoleName("管理员");
        role.setTokenNos(js);

        List<SysRole> list = new ArrayList<>();
        list.add(role);
        list.add(role);
        Page page = new Page();
        page.setTotalPage(23);
        page.setCurrentPage(3);
        page.setTotalCount(550);
        page.setRows(list);
        return page;
    }

    @Override
    public int delete(String roleIds) {
        return 0;
    }

    @Override
    public Map queryRolePerms(String roleId) {

        Token fabricToken = new Token();
        fabricToken.setName("用户管理");
        fabricToken.setOrderNum(1);
        fabricToken.setParentNo("1585559448441");
        fabricToken.setPerms("user:view");
        fabricToken.setTokenNo("1585559448441");
        fabricToken.setType(1);

        List<Token> fabricTokenList = new ArrayList<>();
        fabricTokenList.add(fabricToken);
        fabricTokenList.add(fabricToken);

        Aos aos = new Aos();
        aos.setAosCnname("中国科学院");
        aos.setAosEnname("China");
        aos.setAosHomePage("1585559448441");
        aos.setAosLogoUrl("https://ssss");
        aos.setAosNo("1585559444441");
        aos.setCountryId("1");

        List<Aos> aosList = new ArrayList<>();
        aosList.add(aos);
        aosList.add(aos);

        Map<String, List> map1 = new HashMap<String, List>();
        //给map中添加元素
        map1.put("tokenList" , fabricTokenList);
        Map<String, List> map2 = new HashMap<String, List>();
        //给map中添加元素
        map2.put("aosList" , aosList);
        map1.putAll(map2);
        return map1;
    }
}
