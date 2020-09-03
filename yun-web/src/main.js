import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './router/guard'
import store from './store'
import lodash from 'lodash'
// global css
import './assets/styles/index.scss'
// 导入字体图标
import './assets/fonts/user/iconfont.css'

import './plugins/element.js'
import './assets/icons' // icon

Vue.config.devtools = true // 控制F12时是否可以使用vue-devtool插件
Vue.config.productionTip = false
Vue.prototype.$_ = lodash

// 设置浏览器标题
Vue.directive('title', {
  inserted: function (el, binding) {
    document.title = el.dataset.title
  }
})

// 全局时间格式化过滤器
Vue.filter('dateFormat', originVal => {
  const dt = new Date(originVal)
  const y = dt.getFullYear()
  const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  const d = (dt.getDate() + '').padStart(2, '0')
  const hh = (dt.getHours() + '').padStart(2, '0')
  const mm = (dt.getMinutes() + '').padStart(2, '0')
  const ss = (dt.getSeconds() + '').padStart(2, '0')
  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
})

new Vue({
  store,
  router: router,
  render: h => h(App)
}).$mount('#app')
