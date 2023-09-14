# 第一步
获取支付宝的应用密匙、私钥

# 运行项目
http://127.0.0.1:8090/pay/getQr

# 本地布置需要内网穿透
ngrok.exe
Forwarding 的地址复制到 request.setNotifyUrl中
例如
request.setNotifyUrl("https://9a6f-58-16-77-22.ngrok-free.app/pay/payNotification");