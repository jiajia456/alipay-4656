<script setup>
import {reactive} from "vue";
import { ref, computed,onMounted} from 'vue';
import { Shop,Goods,Money} from '@element-plus/icons-vue';
import { ElMessage, ElImage } from 'element-plus';
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';
import QRCode from 'qrcode';
import router from "@/router";
const emailDialogVisible = ref(false)
const centerDialogVisible = ref(false)
const imageUrl = ref('');
const qrCodeImage = ref('');
const uniqueId= uuidv4();
const commodity = JSON.parse(sessionStorage.getItem('commodity'));
const name3 = commodity.name
const money3 = commodity.money
const password3 = commodity.password

const pay_form= reactive({
  name:name3,
  money:money3,
  password:password3,
  email1:'',
  email2:''
})

const money = (money1) => {
  const integerPart = Math.floor(money1) + 9.99;
  return parseFloat(integerPart.toFixed(2))
}

if(pay_form.password=== null || pay_form.password === ''){
  window.location.href='https://coastline.asia'
}

// 邮箱正则验证
const isValidEmail = (email) =>{
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}
// 验证邮箱按钮
const  submitEmail =() =>{
  if(pay_form.money<=0){
    ElMessage.warning("没有此商品")
  }else {
    if(pay_form.email1){
      if(isValidEmail(pay_form.email1)){
        emailDialogVisible.value=true
      }else {
        ElMessage.error("请输入正确的格式的邮箱")
      }
    }else {
      ElMessage.error("请输入你的邮箱,用于接收短信")
    }
  }
}
// 支付接口
const submitForm = () => {
  if(pay_form.email1&&pay_form.password){
    emailDialogVisible.value=true
    if(pay_form.email2 !== pay_form.email1){
      ElMessage.error("请检查你的邮箱，你的邮箱不正确！")
    } else {
      centerDialogVisible.value = true
      axios
          .post('/pay/getQr', {
            password: pay_form.password,
            name: pay_form.name,
            money: pay_form.money,
            requestId: uniqueId,
          }, { responseType: 'json' })
          .then(response => {
            // 创建一个 <canvas> 元素
            const canvas = document.createElement('canvas');
            // 使用 qrcode.js 生成二维码
            QRCode.toCanvas(canvas, response.data.data.code, error => {
              if (error) {
                ElMessage.error("没有此商品")
              } else {
                imageUrl.value=response.data.data.url
                // 获取生成的二维码图片的 data URL
                const qrCodeImageUrl = canvas.toDataURL('image/png');
                // 使用生成的二维码图片
                const imgElement = document.createElement('img');
                imgElement.src = qrCodeImageUrl;
                qrCodeImage.value = imgElement.src;
                sessionStorage.setItem('orderNumber', response.data.data.orderNumber);
                startCountdown();
                startPolling(true);
              }
            });
          });
    }
  }else{
    ElMessage.error("请输入你的邮箱")
  }
};
const checkOrderStatus = () => {
  axios
      .get('pay/searchOrder',{
        params:{
          orderNumber:sessionStorage.getItem("orderNumber")
        }
      })
      .then(response => {
        const orderState = response.data;
        if (orderState.status === 200) {
          // 订单支付成功
          // 更新前端的订单状态，执行相应的操作
          // 可以显示支付成功的提示或者跳转到支付成功页面等
          startPolling(false)
          sessionStorage.setItem('result', JSON.stringify({
            name: orderState.data.subject,
            money: orderState.data.invoiceAmount,
            orderid: orderState.data.notifyId,
            key:orderState.data.cardkey
          }));
          router.push({
            name:'success'
          })
        } else if (orderState.status === 201) {
          // 订单还未支付
          // 可以不执行任何操作，继续轮询
          console.log('还没有支付哦:', orderState.message);
        }
      })
      .catch(error => {
        console.error('发生错误:', error);
      });
};

// 启动轮询定时器的函数
let pollingInterval; // 将定时器变量定义在函数外部，使其在整个函数内部可用

const startPolling = (statu) => {
  if (statu) {
    pollingInterval = setInterval(checkOrderStatus, 5000); // 5000毫秒（5秒）为间隔时间
  } else {
    clearInterval(pollingInterval); // 停止轮询的函数
  }
};

const remainingTimeFormatted = ref('');
const remainingTime = ref(180); // 初始剩余时间为3分钟，以秒为单位
let countdownTimer; // 声明计时器变量

const startCountdown = () => {
  clearInterval(countdownTimer); // 清除之前的计时器
  remainingTime.value = 180; // 重置剩余时间为3分钟
  countdownTimer = setInterval(() => {
    if (remainingTime.value > 0) {
      const minutes = Math.floor(remainingTime.value / 60);
      const seconds = remainingTime.value % 60;
      remainingTime.value--; // 每秒减少1秒
      const formattedTime = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
      remainingTimeFormatted.value = formattedTime; // 更新格式化后的时间
    } else {
      clearInterval(countdownTimer); // 倒计时结束，清除计时器
      centerDialogVisible.value = false; // 关闭对话框
      startPolling(false)
    }
  }, 1000); // 每秒执行一次
};
const handleDialogClose = () => {
  startPolling(false); // 停止轮询
  centerDialogVisible.value = false; // 关闭对话框
}

</script>

<template>

  <div>
    <router-view></router-view>
  </div>
  <div class="main">
    <div class="main-box" style="border-bottom: 1px solid #f7f7f7;margin-right: 10px">
      <el-icon><Shop /></el-icon>
      <span>商品详细</span>
    </div>
    <div class="main-box">
      <div class="main-aline">
        <el-icon><Goods /></el-icon>
        <span class="pay-title">{{pay_form.name }}</span>
        <span class="small-tips">自动发货</span>
      </div>
    </div>
    <div class="main-bo">
      <div class="main-aline">
        <div class="current-price">
          ¥ <span class="money">
            {{ pay_form.money }}元
          </span>
          <span class="money1">[¥{{money(pay_form.money)}}]</span>
        </div>
      </div>
    </div>
    <div class="main-aline">
      <div class="main-box" style="display: flex; align-items: flex-start; justify-content: left;">
        <div class="email-input" style="display: flex; align-items: center; justify-content: center;">
          <span style="color: #999; width: 72px; display: inline-block; vertical-align: middle; text-align: center; text-indent: -0.5em;">邮箱：</span>
          <el-input v-model="pay_form.email1" placeholder="请输入邮箱，用于自动发货"></el-input>
        </div>
      </div>
    </div>



    <div class="add-to-cart-buttons main-box" style="text-align: center;margin-top: 0px;padding-bottom: 20px">
      <button class="add-to-cart-button" @click="submitEmail()">支付宝支付</button>
    </div>
  </div>
  <el-dialog v-model="centerDialogVisible" class="fullscreen-dialog" center :close-on-click-modal="false" @before-close="handleDialogClose">
    <div class="dialog-content">
      <div class="qr-code-wrapper">
        <img src="image/payment-alipay.png"/>
      </div>
      <p  style="font-weight: bold; font-size: 12px;color: #ef2c2c">支付剩余时间:{{ remainingTimeFormatted }}</p>
      <div v-if="qrCodeImage" class="qr-code-wrapper">
        <el-image ref="qrImage" :src="qrCodeImage" fit="contain" alt="支付二维码"></el-image>
      </div>
      <p  style="font-weight: bold; font-size: 13px">支付完成后请等待5秒左右，期间请勿关闭此页面</p>
      <p  style="font-weight: bold; font-size: 12px">手机端可&nbsp;<a :href="imageUrl">点击打开支付宝付款</a></p>
    </div>
  </el-dialog>


  <el-dialog v-model="emailDialogVisible" class="fullscreen-dialog"
             center
             :close-on-click-modal="false"
  >
    <div class="dialog-content">
      <span class="title" style="margin-bottom: 20px">第二次确认邮箱</span>
      <div class="email-input" style="display: flex; align-items: center; justify-content: center;">
        <span style="color: #999; width: 72px; display: inline-block; vertical-align: middle; text-align: center; text-indent: -0.5em;">邮箱：</span>
        <el-input v-model="pay_form.email2" placeholder="请再次输入你的邮箱"></el-input>
      </div>
      <button class="add-to-cart-button" @click="submitForm()" style="margin-top: 20px">确定</button>
    </div>
  </el-dialog>


  <div class="main1" style="margin-top: -180px;position: relative;">
    <div class="main-box" style="margin-right: 10px">
      <el-icon><Shop /></el-icon>
      <span>购买须知</span>
    </div>

    <div class="title" style="text-align: center">
      商品购买成功后，一律不退货
    </div>
  </div>

</template>



<style scoped>


.main-aline{
  margin-left: 30%;
}

.money1{
  text-decoration: line-through;
  font-size: 18px;
  color: black;
  margin-left: 10px;
}

.email-input{
  display: inline-block;
  padding: 0 5px;
  height: 35px;
  width: 300px;
  font-weight: 500;
  font-size: 14px;
  color: #999;
  background: #fff;
  border: 1px solid #f0f0f0;
  -webkit-box-shadow: 0 4px 10px 0 rgba(135, 142, 154, .07);
  box-shadow: 0 4px 10px 0 rgba(135, 142, 154, .07);
  border-radius: 4px;
  overflow: hidden;
}

.money{
  font-family: "微软雅黑", KaiTi, serif;
  font-size: 28px;
}

.choice{
  padding-bottom: 10px;
  font-family: "微软雅黑", KaiTi, serif;

}

.passage{
  font-family: "微软雅黑", KaiTi, serif;
}
.pay-title{
  font-size: 18px;
}

.small-tips{
  background: #dff7ea;
  color: #28C76F;
  display: inline-block;
  padding: 1px 5px;
  border-radius: 3px;
  font-size: 11px;
  margin-left: 5px;
  line-height: initial;
}

.main{
  position: relative;
  left: 50%;
  transform: translate(-50%, -50%);
  max-width: 60%;
  border-radius: 20px;
  background-color: #ffffff;
  margin: 250px 0px 0px;
}

.main1{
  max-width: 60%;
  border-radius: 20px;
  background-color: #ffffff;
  margin: 250px auto;
}

.main-box{
  padding-top: 20px;
  margin-left: 10px;
  margin-top: 10px;
  text-align: left;

  font-family: "楷体", KaiTi, serif;
  font-weight: bold;
  font-size: 18px;
}

@media (max-width: 765px) {
  .main1 {
    max-width: 90%;
  }
  .main-aline{
    margin-left: 20%;
  }
  .main{
    position: relative;
    left: 50%;
    transform: translate(-50%, -50%);
    max-width: 90%;
    border-radius: 20px;
    margin: 250px 0px 0px;
  }
  .main-box{
    padding-top: 10px;
    margin-left: 5px;
    margin-bottom: 10px; /* 添加这一行 */
    margin-top: 10px;
    text-align: left;
    font-family: "楷体", KaiTi, serif;
    font-weight: bold;
    font-size: 24px;
  }
  .email-input{
    width: calc(100% - 95px);
  }

}


.option input[type="radio"] {
  display: none;
}

.option input[type="radio"]:checked + .checkmark {
  display: none;
  background-color: transparent; /* 移除选中时的背景颜色 */
  border-color: #2196f3; /* 设置选中时的边框颜色为蓝色 */
}


.fullscreen-dialog {
  width: 100vw;
  height: 100vh;
}
.dialog-content {

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.qr-code-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
}

.qr-code-wrapper img {
  max-width: 100%;
  max-height: 100%;
}




.product-image .el-image {
  width: 100%;
  height: 400px;
}


.current-price {
  color: #ff4f0d;
  font-size: 24px;
  font-weight: bold;
}

.add-to-cart-buttons {
  margin-top: 20px;
}

.add-to-cart-button {
  display: inline-block;
  width: 200px;
  height: 60px;
  border-radius: 30px;
  background-color: #00a9ed;
  color: #f8fafa;
  font-size: 20px;
  font-weight: bold;
  border: 1px solid #00a9ed;
}


</style>
