import Vue from 'vue'



import 'normalize.css/normalize.css'

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/common/styles/index.scss' // 全局样式

import App from './App'
import router from './router'
import store from './store'

import i18n from './common/lang' // 国际化
import './common/icons' // icon
import './errorLog'// 错误日志


Vue.use(Element, {
  size: 'medium', // 设置 element-ui 默认大小
  i18n: (key, value) => i18n.t(key, value)
})




Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  template: '<App/>',
  components: { App }
})
