---
typora-root-url: pic
---

# Fabric byfn 实例解读

[TOC]



## 1 byfn 简介

### 1.1 byfn 构成

​	byfn 将创建1个Orderer+4个Peer+1个CLI的网络结构

#### 1.1.1 cli

​	交易发起端，cli 通过命令行执行 peer 命令去完成操作，客户端只需过段时间查询操作结果即可(其中包括到 peer 验证应答后提交数据到 orderer) 

#### 1.1.2 peer 

​	peer 表示组织中的节点（Node) peer节点以区块的形式 从排序服务节点接收有序状态更新，维护状态和账本。

​	peer 节点功能：

​	a.接收 cli 交易请求，b.验证交易，c.模拟交易(peer 的子服务 chaincode)，d.背书交易返回cli，f.将 orderer 排序后形成的区块写入账本 e.将最终交易状态返回给cli

​	其中 peer 节点中可以细分为以下两种角色：

- 背书节点：根据指定的策略调用智能合约，对结果进行背书，返回提案响应到客户端
- 提交节点：验证数据并保存至账本中

#### 1.1.3 Orderer

​	orderer 服务又叫共识服务，目的是将各个 客户端发送的交易请求进行排序共识，保证数据的唯一性，将更新内容(block)同步广播给各个连接的 peer 节点，使peer 更新各自的本地账本

​	图 1.2 Fabric 网络结构，可以更清晰看到各节点在 Fabric 网络中扮演的角色，注意观察 endorsing peer 节点和 committing peer 节点，也就是分别对应“背书节点”和“提交节点”

![](/Fabric 网络交易流程图.png)

​																		图 1.1  Fabric 网络结构图



### 1.2 交易流程简介

​	1 客户端通过命令行方式发送请求到 peer 节点(一个或多个) 

​	2 peer 节点分别对客户端发送的请求进行一系列验证通过之后再执行模拟交易(通过 chaincode，但是并不将执行结果提交到本地的账本中)

​	3 参与背书的 peer 将执行结果(包括自身对背书结果的签名)返回给客户端

​	4 客户端收到各个 peer 的应答后，打包到一起组成一个交易并签名，发送给 orderer

内部后续操作：

​	1 共识：orderer 对接收到的交易进行排序共识，然后按照区块生成策略将一批交易打包到一起，生成新的区块

​	2 同步：orderer 执行完共识后，通过初始注册的消息通道分发给给各个已注册的 peer 节点

​	3 交易校验：peer 节点收到区块后，会对区块中的每笔交易进行校验，检查交易依赖的输入输出是否符合当前区块链的状态，完成后将区块写入账本，并修改 K-V 状态（state）数据

​    如图1.2 Fabric 网络交易流程图所示，为 Fabric 网络从客户端产生最初交易提案（transaction proposal)到最终 peer 节点把区块加入区块链、将写集写入状态数据库的整个流程，应结合上面的分析熟悉这张流程图。

![](/Fabric网络节点构成.png)

​																				图 1.2 Fabric 网络交易流程图

### 1.3 fabric 实例部署

​	HyperLedger 官方友好的给出了一个 Fabric 简单网络的部署脚本  [fabric-samples](https://github.com/hyperledger/fabric-samples)/first-network/[byfn.sh](https://github.com/hyperledger/fabric-samples/blob/master/first-network/byfn.sh)  所有的部署、初始化都通过脚本和配置文件配置完成，只需要通过 byfn.sh（后续详讲）执行 up/down 即可完成部署和卸载

​	byfn = Build Your First Network

### 1.4 fabric 数据验证

​	数据验证是通过 docker 命令进入 cli 的服务中，执行 peer 相关的各项命令，包括通知 orderer服务 channel 创建，各个模块间通信的建立，各个 peer 下 chaincode 服务的创建、调用 peer的 chaincode 指令，执行各项数据操作等，等实例中各项模块初始化完成后，即可通过初始化账户，查询账户，交易等方式来验证数据的流通性和准确性了。



## 2 fabric-samples 源码结构

### 2.1 fabric-samples 源码结构

​	fabric-samples 是 fabric 提供的整套网络样例，byfn 网络的启动脚本在 first-network 目录下，first-network 目录也同时包含了 byfn 所用到的配置文件，chaincode 目录下存放的是网络所用到的链码也就是智能合约。

![](/fabric-samples目录介绍.png)

### 2.2 chaincode 源码结构

​	chaincode 目录存放的是网络用到的所有链码，也同样保存了 byfn 网络的链码，fabric 1.4 版本链码支持使用 golang、java、node开发

![](/chaincode 目录介绍.png)	

### 2.3 first-network 源码结构

​	first-network 目录下存放网络从搭建部署到安装链码调用链码所需的配置文件，其中 byfn.sh 是 byfn 实例启动、停止的的脚本，有完整的创建网络步骤

![](/first_network目录介绍.png)



## 3 fabric 实例部署

### 3.1 fabric 部署环境

#### 3.1.1 安装所需工具

​	1.安装git 

​	git是一个非常优秀的开源版本管理控制工具，使用git工具可以 方便地下载官方（Golang、Hyperledger Fabric等）在GitHub网站上发 布的相关源代码或其他内容。 

​	安装git工具使用如下命令： 

```shell
$ sudo apt update 

$ sudo apt install git 
```

​	2.安装cURL 

​	使用如下命令安装cURL： 

```shell
$ sudo apt install curl
```

​	3.安装Docker 

​	查看系统中是否已经安装Docker： 

```shell
$ docker --version 
```

​	使用如下命令安装Docker的最新版本： 

```shell
$ sudo apt update 

$ sudo apt install docker.io 
```

​	查看Docker版本信息：`$ docker --version` 

```shell
Docker version 19.03.8, build afacb8b
```

​	4.安装docker-compose 

​	确定系统中是否已安装docker-compose工具： 

```shell
$ docker-compose --version 
```

​	如果系统提示未安装，则使用如下命令安装docker-compose工具：

```shell
$ sudo apt install docker-compose 
```

​	安装成功后，查看docker-compose版本信息： 

```shell
$ docker-compose --version 
```

​	输出类似如下的docker-compose版本信息： 

```shell
docker-compose version 1.25.5, build 8a1c60f6
```

​	5.安装Golang 

​	应按照官方文档的方式安装并配置好 golang 环境变量 eg: 

```shell
export GOROOT=/usr/local/go
export PATH=$PATH:/usr/local/go/bin
export GOPATH=/home/lacon/Works/GoPath
export PATH=$PATH:$GOPATH/bin
```
​	安装成功并配置好环境变量后查看 golang 版本信息 `go version` :

```shell
go version go1.14.2 linux/amd64
```

### 3.2 安装Hyperledger Fabric

#### 3.2.1 下载fabric、fabric-samples与二进制文件

​	说明：本示例使用的是 Fabric 1.4 版本，因此以下操作都是基于 Fabric 1.4 版本

​	为了方便后期管理，在当前登录用户的HOME目录下创建一个空目录并进入该目录： 

```shell
$ mkdir hyfa && cd hyfa
```

- 下载 Fabric 1.4 版本源码

  访问 [fabric1.4](https://github.com/hyperledger/fabric/tree/release-1.4) 到 github 上的 1.4 版本，可以使用：

  ```shell
  git clone https://github.com/hyperledger/fabric.git
  ```

  然后回退到1.4版本：

  ```shell
  $ cd $GOPATH/src/github.com/hyperledger
  
  $ cp -r fabric /home/hyfa && cd /home/hyfa
  
  $ git checkout -b v1.4
  ```

  也可以直接在 github 上下载源码，并解压到：`/home/hyfa/` 下

- 下载 fabric-samples 1.4 版本源码

  方法如上 fabric 1.4 版本，到 [fabric-samples 1.4](https://github.com/hyperledger/fabric-samples/tree/release-1.4) 来进行下载，并将 fabric-samples  放到`/home/hyfa`下

- 将`/home/hyfa/fabric/scripts/bootstrap.sh` 复制到 `/home/hyfa`目录下并更改它的权限为可执行：

  ```shell
  $ chmod +x bootstrap.sh 
  ```

  然后 `vim bootstrap.sh` 找到第137行还有第145行，并将 "nexus." 删除：

  ![](/bootstrap修改.png)

  执行 bootstrap.sh： 

  ```shell
  $ ./bootstrap.sh 
  ```

  该bootstrap.sh可执行脚本文件的作用如下。 

  1）如果当前目录中没有`fabric-samples`，则从`github.com`克隆`fabric-samples`存储库

  2）使用checkout签出对应指定的版本标签

  3）将指定版本的Hyperledger Fabric平台特定的二进制文件和配置文件安装到fabric-samples存储库的根目录中

  4）下载指定版本的Hyperledger Fabric Docker镜像文件

  5）将下载的Docker镜像文件标记为`latest`

  确定网络稳定，否则会导致各种问题，例如下载到一半时网 络超时，下载失败等；由于Docker的各种镜像文件下载时间较长，所以请耐心等待。下载完成后，查看相关输出内容。如果有下载失败的镜像，可再次执行如下命令重新下载，对于已下载的Docker镜像文件，再次执行脚本命令不会重新下载

  安装完成后终端会自动输出：

  ​	===> List out hyperledger docker images

  ​	![](/docker images.png)

- 如下载 fabric 工具不成功可手动获取，进入`/home/hyfa/fabric`执行：

  ```shell
  make release
  ```

  拉取二进制源码工具，执行完成后，在`fabric/release/linux-amd64/bin`的目录下就会有下载好的二进制源码工具了，再执行`cp cryptogen configtxgen configlator /home/hyfa/fabric-samples/bin`命令，fabric-sample 1.4 版本下默认没有`bin`目录，需要手动创建

- 添加环境变量（可选执行命令）

  ```shell
  $ export PATH=<path to download location>/bin:$PATH 
  ```

  <path to download location>表示fabric-samples文件目录所在路 径，例如： 

  ```shell
  $ export PATH=$HOME/hyfa/fabric-samples/bin:$PATH
  ```

  

### 3.3 测试 Hyperledger Fabric 网络

#### 3.3.1 测试 Hyperledger Fabric 环境 

​	之前我们介绍过，使用一个名为 byfn.sh 的脚本实现 Hyperledger Fabric 网络的自动构建并测试，那么我们来看一下 byfn.sh 脚本都有哪些命令可以使用。首先进入 fabric-samples 目录中的 first-networkd 子目录：

```shell
$ cd fabric-samples/first-network 
```

​	在 first-network 目录下有一个自动化脚本 byfn.sh，可以使用`--help` 参数查看相应的可用命令，在命令提示符中输入如下命令： 

```shell
$ ./byfn.sh --help 
```

​	命令执行成功后，会在终端输出如下类似内容（为方便起见，已将其说明翻译成中文）：

- up：启动； 
- down：清除网络； 
- restart：重新启动； 
- generate：生成证书及创世区块； 
- upgrade：将网络从1.1.x升级到1.2.x； 
- -c：用于指定channelName，默认值"mychannel"； 
- -t：CLI timeout时间，默认值10； 
- -d：延迟启动，默认值3； 
- -f：使用指定的网络拓扑结构文件，默认使用docker-compose-cli.yaml； 
- -s：指定使用的数据库，可选 goleveldb/couchdb； 
- -l：指定chaincode使用的语言，可选golang/node； 
- -i：指定镜像tag，默认 "latest"。

#### 3.3.2 构建你的第一个Hyperledger Fabric网络 

 * 生成证书和密钥 

   byfn.sh 自动化脚本文件为各种 Hyperledger Fabric 网络实体生成所有证书和密钥，并且可以实现引导服务启动及配置通道所需的一系列配置文件： 
   
   ```shell
   $ sudo ./byfn.sh -m generate 
   ```

​	   命令成功执行后会生成1个Orderer+4个Peer+1个CLI的网络结构，4个Peer包含在2个Org中，根据提示输入y

- 启动网络

  生成所需要的证书及密钥之后，需要启动网络来确认 Hyperledger Fabric 网络环境是否能够正常工作，使用byfn.sh脚本来实现网络的启动，命令如下： 

  ```shell
  $ sudo ./byfn.sh -m up 
  ```

  命令执行后，终端会输出一个提示信息，根据提示输入y

- 关闭网络

  网络测试成功后，为了方便后期的操作，最好将其关闭，以防止后期启动网络时造成的冲突错误，关闭网络可执行如下命令：

  ```shell
  $ sudo ./byfn.sh -m down 
  ```

  根据提示输入y 

  使用byfn.sh脚本关闭网络之后，将关闭容器，且删除加密文件，并从Docker Registry中删除链码图像



## 4 byfn 解析

### 4.1 byfn 执行图示

![](/byfn详解.png)

### 4.2 详解

#### 4.2.1 第一步，生成 crypto-config 和 channel-artifacts 目录及其相关文件

​	命令行：

```shell
$ cd /home/hyfa/fabric-samples/first-network
$ ./byfn.sh -m generate
```

​	构建网络的第一步是生成 crypto-config 和 channel-artifacts 目录及其相关文件，其命令行如上，将会分别执行以下三个函数：

![](/byfn.sh_generate.png)

##### 4.2.1.1 generateCerts 函数

​	generateCerts 函数中先判断指定路径下是否存在 cryptogen 工具，若不存在提示`Generate certificates using cryptogen tool`并退出。若存在，则通过 `cryptogen generate --config=./crypto-config.yaml` 生成 cli，peer，orderer 所需使用的证书、密钥，生成的密钥存储于 crypty-config 目录中，分为 orderer 和 peer 两类，如图4.1 generateCerts 函数

![](/generateCerts函数.png)

​																			图4.1 generateCerts 函数

##### 4.2.1.2  replacePrivateKey 函数

​	将 docker 配置文件中需要使用到上一步生成私钥文件名进行替换，本实例没有用到可以不用考虑

##### 4.2.1.3 generateChannelArtifacts 函数

![](/generateChannelArtifacts.png)

a. 

```shell
$ configtxgen -profile TwoOrgsOrdererGenesis -outputBlock ./channelartifacts/genesis.block
```

​	在 channel-artifact 中生成配置文件 genesis.block ，该配置文件是orderer 容器服务启动后，通过共享目录访问 genesis.block 创建并初始化 orderer 中创世区块使用

b. 

```shell
$ configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./channelartifacts/channel.tx -channelID $CHANNEL_NAM
```

​	在 channel-artifact 中生成配置文件 channel.block，该配置文件是当后续首次创建 channel 时的配置文件

c.

```shell
$ configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channelartifacts/Org1MSPanchors.tx -channelID  $CHANNEL_NAME  -asOrg  Org1MSP -profile TwoOrgsChannel  -outputAnchorPeersUpdate ./channel  artifacts/Org2MSPanchors.tx  -channelID  $CHANNEL_NAME  -asOrg  Org2MSP
```

​	在 channel artifact 中生成配置文件 Org1MSPanchors.tx 和 Org2MSPanchors.tx ，该配置文件是当后续将当前 org 中的各个 peer 加入到 a 中创建 channel 时的配置文件

#### 4.2.2 第二步，调用 docker-compose

​	命令行：

```shell
$ cd /home/hyfa/fabric-samples/first-network
$ ./byfn.sh -m up
```

​	内部是执行 `docker-compose -f docker-compose-cli.yaml` 命令，生成各类容器，并启动。在该实例的 docker-compose-cli.yaml 配置中共创建并运行了 6 个容器：

 **a、order 服务容器** 

 **b、peer0.org1 服务容器** 

 **c 、peer1.org1 服务容器** 

 **d 、peer0.org2 服务容器** 

 **e、 peer1.org2 服务容器** 

 **f、  cli 客户端模拟容器**

​	其他 5 个容器都是通过命令行直接启动服务。Cli 容器是通过 script.sh 脚本启动测试用例，script.sh 执行步骤：1、**创建 channel** 2、**peer 加到 channel 中** 3、**peer 和 channel 绑定** 4、**初始化 chaincode 服务** 5、**初始化账户** 6、**查询测试** 7、 **交易测试** 8、**初始化新的 chaincode 服务** 9、**验证交易** 10、**手动操作**

##### 4.2.2.1 创建channel

​	createChannel

​	命令：

```shell
$ peer channel create -o orderer.example.com:7050 -c $CHANNEL_NAME -f  ./channelartifacts/channel.tx
```

​	cli 通过调用 peer 命令，模拟 SDK 发送 channel create 请求到 orderer 服务端，orderer 收到后，会通过请求中的 channel.tx 配置创建用于后续共识服务的 channel

##### 4.2.2.2 peer 加到 channel 中

​	joinChannel

​	命令：

```shell
$ peer channel join -b $CHANNEL_NAME.block
```

​	将 peer 加入到 channel 中

##### 4.2.2.3 peer 和 channel 绑定

（1）Updating anchor peers for org1...

​	updateAnchorPeers 0

```shell
$ peer channel update -o orderer.example.com:7050 -c $CHANNEL_NAME -f  ./channelartifacts/ Org1MSPanchors.tx
```

（2）Updating anchor peers for org2...

​	updateAnchorPeers 2

```shell
$ peer channel update -o orderer.example.com:7050 -c $CHANNEL_NAME -f  ./channelartifacts/ Org2MSPanchors.tx
```

##### 4.2.2.4 初始化 chaincode 服务

​	为 peer0.org1.example.com 和 peer1.org2.example.com 初始化各自的 chaincode 服务 

​	（1）Installing chaincode on org1/peer0...

​		installChaincode 0

```shell
$ chaincode install -n mycc -v 1.0 -p /home/hyfa/fabric-samples/chaincode/chaincode_example02/go/chaincode_example02.go
```

​		其中-p 参数指向的是实现了 Init 和 Invoke 接口的 go 包 

​	（2）Install chaincode on org2/peer2...

​		installChaincode 2

```shell
$ chaincode install -n mycc -v 1.0 -p /home/hyfa/fabric-samples/chaincode/chaincode_example02/go/chaincode_example02.go
```

​		其中-p 参数指向的是实现了 Init 和 Invoke 接口的 go 包

##### 4.2.2.5 初始化账户

​	通过某个 peer 初始化两个账户, 实例中是通过 peer1.org2.example.com 节点服务初始化 Instantiate chaincode on Peer2/Org2

​	instantiateChaincode 2

​	命令：

```shell
$ peer chaincode instantiate -o orderer.example.com:7050 -C $CHANNEL_NAME -n mycc  -v 1.0  -c  '{"Args":["init","a","100","b","200"]}'  -P "OR ('Org1MSP.member','Org2MSP.member')"
```

##### 4.2.2.6 查询测试

​	Querying chaincode on org1/peer0...

​	chaincodeQuery 0 100

​	命令：

```shell
$ peer chaincode query -C $CHANNEL_NAME -n mycc -c '{"Args":["query","a"]}'
```

##### 4.2.2.7 交易测试

​	Sending invoke transaction on org1/peer0...

​	chaincodeInvoke 0

​	通过向 peer0.org1 发送交易请求，由 a 给 b 转账 10

​	命令： 

```shell
$ peer chaincode invoke -o orderer.example.com:7050 -C $CHANNEL_NAME -n mycc -c '{"Args":["invoke","a","b","10"]}'
```

##### 4.2.2.8 初始化新的 chaincode 服务

​	给 peer2.org2.example.com 初始化 chaincode 服务 

​	Installing chaincode on org2/peer3...

​	installChaincode 3

​	命令： 

```shell
$ chaincode install -n mycc -v 1.0 -p /home/hyfa/fabric-samples/chaincode/chaincode_example02/go/chaincode_example02.go
```

##### 4.2.2.9 验证交易

​	通过新创建的 peer2.org2.example.com 的 chaincode 查询 a 账户是否还剩余 90

​	Query on chaincode on Peer3/Org2, check if the result is 90

​	chaincodeQuery 3 90

​	命令：

```shell
$ peer chaincode query -C $CHANNEL_NAME -n mycc -c '{"Args":["query","a"]}'
```

##### 4.2.2.10 手动操作

​	在上述交易完成后，说明 fabric 的实例模型 e2e_cli 实例已经搭建完成，通过

```shell
$ docker exec -it cli bash
```

​	登录到 cli 容器内部，模拟客户端像 peer 发起交易请求：

```shell
$ peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /home/hyfa/fabric-samples/first-network/crypto-config/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc -c '{"Args":["invoke","a","b","20"]}'
```

​	让 a 再向 b 转账 20。 由于在第 e 步时，初始化的 a 和 b 账户余额分别为 100,200, 然后通过脚本执行的时候 a 转账给 b 数目为 10，执行这一步之前应该是 a:90, b:210。通过这一步执行后，应该是 a:70, b:230。 接下来我们模拟客户端通过 peer chaincode 指令查询实际账户信息：

```shell
$ peer chaincode query -C mychannel -n mycc -c '{"Args":["query","a"]}'

$ peer chaincode query -C mychannel -n mycc -c '{"Args":["query","b"]}'
```

​	可以通过返回的打印日志即可验证此次交易是否完成

