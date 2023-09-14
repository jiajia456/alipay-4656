<template>
    <el-page-header style="margin-top: 15px;margin-left: 15px">
      <template #title >
        <span style="font-size: 30rem;color: black" ></span>
      </template>
      <template #content>
        <span class="title-container" > Double黑科技 </span>
      </template>
      <template #icon>
          <el-button class="icon-content"  @click="centerDialogVisible = true">
            <el-image class="image" src='/image/搜索.png'/>
            <span class="title-container">查询</span>
          </el-button>
      </template>
    </el-page-header>

  <el-dialog
      class="fullscreen-dialog"
      v-model="centerDialogVisible"
      title="查询"
      width="50%"
      align-center
      :close-on-click-modal="false"
  >
    <div style="display: flex; flex-direction: column; align-items: center;">
      <el-image src="/image/礼物.png" style="width: 10rem; height: 10rem;"></el-image>
      <el-input placeholder="请输入提取码" v-model="password"></el-input>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="goshopping(), centerDialogVisible = false">
          查询
        </el-button>
      </span>
    </template>
  </el-dialog>

</template>
<script setup>
import { ref } from 'vue';
import { ElMessage, ElImage } from 'element-plus';
import {Bottom} from "@element-plus/icons-vue";
import axios from 'axios';
import router from "@/router";
const password=ref("");

const goshopping = () =>{
  if(password.value){
    axios.get('/commodities/find',{
      params:{
        password:password.value,
        n:true
      }
    }).then(response => {
      const data = response.data;
      console.log(data)
      if(data.data){
        sessionStorage.setItem('commodity', JSON.stringify({
          name: data.data.name,
          money: data.data.money,
          password: data.data.password
        }));
        router.push({
          name:'about',
        })
      }else {
        ElMessage.error("无此商品")
      }

    })
  }else {
    ElMessage.error("请输入提取码")
  }
}

const centerDialogVisible = ref(false)
</script>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}

.image{
  width: 35px;
  height: 35px
}

.title-container {
  font-family: 幼圆;
  font-size: 28vm;
  font-weight: bolder;
  padding: 10px;
}

.title-line {
  flex-grow: 1;
  height: 1px;
  background-color: #000;
  margin-right: 10px;
}

.icon-content {
  border: 1px solid #000;
  border-radius: 10px;
  border-color: #0077ff;
  display: flex;
  align-items: center;
  justify-content: start;
  width: 130px;
  height: 45px;
}


@media (max-width: 765px) {
  .title-container{
    font-size: 2rem;
  }
  .icon-content{
    width: 200px;
    height: 55px;
  }
  .image{
    width: 45px;
    height: 45px
  }
}

</style>
