<style scoped>
/* 修改整体页面样式 */
body {
  font-family: Arial, sans-serif;
  background-color: #f0f0f0; /* 设置页面背景色 */
  margin: 0;
  padding: 0;
}

/* 头部样式 */
.head {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 让内容在头部左、中、右对齐 */
  background-color: #007bff; /* 头部背景颜色 */
  color: #fff; /* 文字颜色 */
  padding: 10px 20px; /* 添加内边距，增加可读性 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

/* 返回按钮样式 */
.black {
  cursor: pointer; /* 鼠标悬停时显示手型光标 */
}

/* 主要内容区域样式 */
.content {
  text-align: center; /* 让内容居中对齐 */
  padding: 20px;
  background-color: #fff; /* 内容区域背景颜色 */
  border-radius: 5px; /* 圆角边框 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

/* 商品信息样式 */
.product-info {
  margin-top: 20px;
}

/* 补充样式，你可以根据需要继续完善 */
/* .order-info {}
.product-name {}
.price {} */

</style>
<template>
  <div>
    <!-- 头部 -->
    <div class="head">
      <div class="black">返回</div>
      <span>支付成功</span>
      <div>商家名称</div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content">
      <!-- 补充内容，根据需求添加订单号、商品名、价格等信息 -->
      <div class="order-info">
        <div class="product-name">商品名称</div>
        <div class="price">价格</div>
        <!-- 添加其他信息，如订单号 -->
      </div>
    </div>
  </div>
</template>

<script>
// 在这里可以添加页面交互的JavaScript代码，如返回按钮的点击事件等。
</script>
