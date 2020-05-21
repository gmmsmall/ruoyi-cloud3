package com.ruoyi.fabric.bean;

/**
 * 默克尔树节点
 * name:区块(1byte单位) D1开始计数 只有叶子节点有此属性
 * hash:对区块SHA-1加密后的结果
 * @author roc_peng
 *
 */
public class MerkleNode {
	private String name;
	private String hash;
	public MerkleNode() {
	}
	public MerkleNode(String name, String hash) {
		this.name=name;
		this.hash=hash;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
}
