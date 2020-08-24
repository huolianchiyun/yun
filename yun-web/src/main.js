import Vue from 'vue'
import App from './App.vue'
import index from './router'
import store from './store'

// global css
import './assets/styles/index.scss'
// 导入字体图标
import './assets/fonts/user/iconfont.css'

import './plugins/element.js'
import './assets/icons' // icon

import axios from 'axios'
// 打印
import Print from 'vue-print-nb'
// 导入NProgress包对应的JS和CSS
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import lodash from 'lodash'

// 配置请求的根路径
axios.defaults.baseURL = 'http://127.0.0.1:8081/yun/'
Vue.prototype.$http = axios
Vue.prototype.$_ = lodash
Vue.config.devtools = true // 控制F12时是否可以使用vue-devtool插件
Vue.config.productionTip = false
// 注册
Vue.use(Print)

// 在request拦截器中展示进度条
axios.interceptors.request.use(config => {
  NProgress.start()
  config.headers.authorization = window.sessionStorage.getItem('token')
  return config // 必须return config
})

// 在response拦截器中隐藏进度条
axios.interceptors.response.use(async config => {
  NProgress.done()
  const { data: res } = config
  if (res.meta.message !== undefined && res.meta.message === 'Authorization失效，请重新登录') {
    await index.push('/login')
    return
  }
  return config // 必须return config
})

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
  router: index,
  render: h => h(App),
  created () {
    // 在页面加载时读取sessionStorage里的状态信息
    if (sessionStorage.getItem('store')) {
      this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(sessionStorage.getItem('store'))))
    }

    // 在页面刷新时将vuex里的信息保存到sessionStorage里
    window.addEventListener('beforeunload', () => {
      sessionStorage.setItem('store', JSON.stringify(this.$store.state))
    })
  }
}).$mount('#app')
