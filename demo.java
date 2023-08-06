package use_tron_scan;

import java.math.BigDecimal;
import com.alibaba.fastjson.*;
// 导入SDK
import tron_scan.ScanBlock;

public class main {
	
	
	// 初始化扫描器   一定要全局哟
	public static ScanBlock scan = new ScanBlock();
	
	
	
	// 自动切换API节点线程
    static class MyThread extends Thread {
        private ScanBlock websocket;
        public MyThread(ScanBlock websocket) {
            this.websocket = websocket;
        }
        @Override
        public void run() {
        	// 要死循环检测
        	while(true) {
	        	try {
					// 可自行设置节点 格式:https://api.trongrid.io,https://api.tronstack.io,http://3.225.171.164:8090
					// 设置节点时 不要带 后面的 / ，否则会访问失败。API蛮矫情。
	        		websocket.SwitchNode(null);
					System.out.println("Use New Http Api:"+scan.get_uri());
					Thread.sleep(1000 * 30);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					//e.printStackTrace();
				}
        	}
        }
    }
	
	//  回调 转账信息
    public static void transfer_data(String all_data) {
    	
		// 区块没交易  all_data 值为 []
    	JSONArray Json = JSON.parseArray(all_data);
    	
    	for(int i = 0; i < Json.size(); i++) {
    		
    		JSONObject transfer = Json.getJSONObject(i);
			/*
			
			转账格式：
				contractRet：	成功 为 SUCCESS   失败 ...  忘了
				amount：	为 转账金额
				txID：	为 转账hash
				blockNum：	为 当前区块号
				to_address：	为 接收地址
				type：	TriggerSmartContract 为 合约转账、TransferContract 为 TRX 转账
				contract_address： 合约转账时 为 合约地址，TRX转账时为 空
				from_address：	为 发送地址
				timestamp：	为 时间戳
			
			
			合约转账
			
			{"contractRet":"SUCCESS","amount":"500000000","txID":"4636f1434504d9f13dc55d7ccfb829ebee088fb48c074c4f66080bf9dca9d0d7","blockNum":"53483427","to_address":"TU3JJVA4Nhx1pvV1qcC6HK9UmRwrg56LtB","type":"TriggerSmartContract","contract_address":"TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t","from_address":"TY4vaK2iVCkivohAHLi8pAF3Lup72P2rg2","timestamp":"1691072630"}
    		
			
			
			TRX转账
			
			{"contractRet":"SUCCESS","amount":"1","txID":"474d3ae8e1060ed381e7fd0c1cf6885825ee012fb453cea1b04d50994849aa27","blockNum":"53484028","to_address":"TCddPkvAjjh41wQ9tsamK5Hox93aReDtes","type":"TransferContract","contract_address":"","from_address":"THLsVCA3ra9XdSr4tJbEWGfCNbMtvWSMig","timestamp":"1691074442"}
			
			*/
        		if(transfer.getString("contractRet").equals("SUCCESS")) {
        			
        			if(transfer.getString("type").equals("TriggerSmartContract") && transfer.getString("contract_address").equals("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t")) {
                        
					// 这个地方要使用 BigDecimal 进行计算
						
					BigDecimal amount_a = new BigDecimal(transfer.getString("amount"));
                        		BigDecimal divisor = new BigDecimal("1000000");
                        		String amount = amount_a.divide(divisor, 6, BigDecimal.ROUND_HALF_UP).toString();
        				System.out.println("USDT transfer:"+transfer.getString("from_address") + " -> " + transfer.getString("to_address") + " -> " + amount + " -> " + transfer.getString("txID"));
        				
        			}else if(transfer.getString("type").equals("TransferContract")) {
						
					// 这个地方要使用 BigDecimal 进行计算
                        
					BigDecimal amount_a = new BigDecimal(transfer.getString("amount"));
						
					// trx 和 usdt 一样 是 10 ** 6
                        		BigDecimal divisor = new BigDecimal("1000000");
						
					// 这个地方的 6 是保留小数点 后 六位的意思
                        		String amount = amount_a.divide(divisor, 6, BigDecimal.ROUND_HALF_UP).toString();
        				System.out.println("TRX transfer:"+transfer.getString("from_address") + " -> " + transfer.getString("to_address") + " -> " + amount + " -> " + transfer.getString("txID"));
        				
        			}
        			
        		}
        		
			//System.out.println(transfer.toJSONString());
    	}
    }
	
	
	//   入口函数
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
		
		// 设置API key  设置自己的，要不然会触发RQS限制，然后 GG 的。
		// https://www.trongrid.io 申请下欧~ 
		scan.set_api_key("caca1ff2-fe5a-41d8-a413-c305fe39a4d9");
		
		// 查看 正在使用的Key
		System.out.println("Use Key:" + scan.get_api_key());
		
		
		
		// 设置主节点   设置节点时 不要带 后面的 / ，否则会访问失败。API蛮矫情。
		scan.set_uri("https://api.trongrid.io");
		
		
		// 查看正在使用的  URI
		System.out.println("Use Http Api:"+scan.get_uri());
		
		
		
		// 启动自动切换API 线程
		MyThread thread1 = new MyThread(scan);
		thread1.start();
		

		

		
		// 获取最新的区块号
		String block_num = scan.GetNowBlockNum();
		
		
		// 进入死循环扫块操作
		int number = Integer.parseInt(block_num);
		System.out.println("New Block Num:"+ block_num);
		while(true) {
			try {
				// 取 某个 区块的 转账记录 TRX转账 和 标准的 TRC20代币转账（附 过滤解析）
				transfer_data(scan.GetBlockAllData(Integer.toString(number++)));
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		
	}
}
