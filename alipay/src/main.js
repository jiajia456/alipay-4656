

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { ElImage } from 'element-plus'; // 添加此行
import 'element-plus/dist/index.css'
import App from './App.vue'
import ElementPlus from 'element-plus';
import router from './router'
import axios from "axios";
import store from './stores/index.js';   // 引入 Vuex store
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/style/preview.css';



import Prism from 'prismjs';

VueMarkdownEditor.use(vuepressTheme, {
    Prism,
});


const app = createApp(App);

app.use(VueMarkdownEditor);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
axios.defaults.baseURL = 'http://127.0.0.1:8090'
app.use(store);  // 使用 Vuex store
app.use(createPinia())
app.use(router)
app.use(ElImage);
app.use(ElementPlus)

app.mount('#app')
