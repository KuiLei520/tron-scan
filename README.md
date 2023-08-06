# tron-scan

* 依赖 ：fastjson.jar我用的版本1.2.62。
* 运行环境：JDK 1.8

Tron_Scan.jar 是封装好的Tron扫块SDK。

基于 HTTP API 对区块号进行解析操作。

返回 区块号 的TRX转账以及标准的合约转账信息。

方便了开发人员对TRX网络的开发。

#########################################################

转账返回格式：

* contractRet：	成功 为 SUCCESS   失败 ...  忘了
* amount：	为 转账金额
* txID：	为 转账hash
* blockNum：	为 当前区块号
* to_address：	为 接收地址
* type：	TriggerSmartContract 为 合约转账、TransferContract 为 TRX 转账
* contract_address： 合约转账时 为 合约地址，TRX转账时为 空
* from_address：	为 发送地址
* timestamp：	为 时间戳


合约转账
		{"contractRet":"SUCCESS","amount":"500.000000","txID":"4636f1434504d9f13dc55d7ccfb829ebee088fb48c074c4f66080bf9dca9d0d7","blockNum":"53483427","to_address":"TU3JJVA4Nhx1pvV1qcC6HK9UmRwrg56LtB","type":"TriggerSmartContract","contract_address":"TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t","from_address":"TY4vaK2iVCkivohAHLi8pAF3Lup72P2rg2","timestamp":"1691072630"}


TRX转账
		{"contractRet":"SUCCESS","amount":"0.000001","txID":"474d3ae8e1060ed381e7fd0c1cf6885825ee012fb453cea1b04d50994849aa27","blockNum":"53484028","to_address":"TCddPkvAjjh41wQ9tsamK5Hox93aReDtes","type":"TransferContract","contract_address":"","from_address":"THLsVCA3ra9XdSr4tJbEWGfCNbMtvWSMig","timestamp":"1691074442"}

#########################################################

Tron Scan Blocks V1.2.5  SDK

1.本SDK自带自动切换节点。不怕因网络等问题造成的卡块。

2.请启动一个线程进行自动切换节点。

3.请设置自己的API key。防止被限制。

修复BUG：

*  修复一个已知问题：区块有可能没交易。
*  优化节点切换，由 毫秒 级修改为 秒 级。
*  修复一个因原始数据解码错误。例：c82365fdc6fc16fbc76f4aa4effa45cd312a7b12dc9c88047022c0468462b9df
*  修改 amount 显示方式，因无法统一代币长度，改由调用端进行 BigDecimal.divide 运算。
