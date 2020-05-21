package com.ruoyi.fabric.utils;

import com.alibaba.fastjson.JSON;
import com.ruoyi.fabric.bean.MerkleNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MerkleUtil {
	
	/**
	 * 读取一个文件,并将文件的每个字节作为merkle树的叶子,然后生成默克尔树
	 *
	 */
	public static String getMerkleTree(List<String> data){
		List<List<MerkleNode>> list=new LinkedList();
		List<MerkleNode> leafList=new LinkedList();
		//首先获得叶子
		for(int i = 0 ; i < data.size() ; i++) {
			MerkleNode leaf=new MerkleNode();
			leaf.setName("BlockD"+i);
			leaf.setHash(EncryptUtil.getSHA(data.get(i)+""));
			leafList.add(leaf);
		}
		//构造二叉树
		getMerkleNode(leafList,list);
		//System.out.println(JSON.toJSONString(list));
		return JSON.toJSONString(list);
	}
	
	public static void getMerkleNode(List<MerkleNode> list, List<List<MerkleNode>> allList){
		allList.add(list);
		if(list.size()==1)return;
		LinkedList<MerkleNode> temp=new LinkedList<MerkleNode>();
		for(int i=0;i+1<list.size();i+=2){
			MerkleNode node1=list.get(i);
			MerkleNode node2=list.get(i+1);
			MerkleNode tempNode=new MerkleNode(node1.getName()+node2.getName(),
					EncryptUtil.getSHA(node1.getHash()+node2.getHash()));
			temp.add(tempNode);
		}
		if(list.size()%2==1){
			MerkleNode tempNode=new MerkleNode(list.get(list.size()-1).getName(), 
					EncryptUtil.getSHA(list.get(list.size()-1).getHash()));
			temp.add(tempNode);
		}
		getMerkleNode(temp,allList);
	}
	
	public static void getMerkleNode2(List<Map<String, String>> list, List<List<Map<String, String>>> allList){
		allList.add(list);
		if(list.size()==1)return;
		LinkedList<Map<String, String>> temp=new LinkedList<Map<String, String>>();
		for(int i=0;i+1<list.size();i+=2){
			Map<String, String> node1=list.get(i);
			Map<String, String> node2=list.get(i+1);
			Map<String, String> node3=new HashMap<String, String>();
			node3.put("name", node1.get("name")+node2.get("name"));
			node3.put("hash", EncryptUtil.getSHA(node1.get("hash")+node2.get("hash")));
			temp.add(node3);
		}
		if(list.size()%2==1){
			Map<String, String> tempNode=new HashMap<String, String>();
			tempNode.put("name", list.get(list.size()-1).get("name"));
			tempNode.put("hash", EncryptUtil.getSHA(list.get(list.size()-1).get("hash")));
			temp.add(tempNode);
		}
		getMerkleNode2(temp,allList);
	}
}
