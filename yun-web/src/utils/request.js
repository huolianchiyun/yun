import Vue from 'vue'
import Axios from 'axios'
import router from '@/router'
import store from '../store'
import Config from '@/settings'
import Cookies from 'js-cookie'
import { Notification } from 'element-ui'
import { getToken } from '@/utils/auth'
// 导入NProgress包对应的JS和CSS
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// 再次封装是因为axios.interceptors.response#user.error 中会自动将Notification变为_Notification导致无法识别
const notifyError = function (options) {
  Notification.error(options)
}

// 创建axios实例
const axios = Axios.create({
  baseURL: 'http://localhost:8081/yun/', // api 的 base_url
  timeout: Config.timeout // 请求超时时间
})

// 在request拦截器中展示进度条
axios.interceptors.request.use(config => {
  NProgress.start()
  config.headers['Authorization'] = getToken()
  config.headers['Content-Type'] = 'application/json'
  return config // 必须return config
})

// 在response拦截器中隐藏进度条
axios.interceptors.response.use(async response => {
  NProgress.done()
  const code = response.status
  if (code < 200 || code > 300) {
    Notification.error({
      title: response.message
    })
    // eslint-disable-next-line prefer-promise-reject-errors
    return Promise.reject('error')
  } else {
    return response.data
  }
},
error => {
  NProgress.done()
  let code = 0
  try {
    code = error.response.status
  } catch (e) {
    if (error.toString().indexOf('Error: timeout') !== -1) {
      notifyError({
        title: '网络请求超时',
        duration: 5000
      })
      return Promise.reject(error)
    }
  }
  if (code) {
    if (code === 401) {
      store.dispatch('LogOut').then(() => {
        // 用户登录界面提示
        Cookies.set('point', 401)
        location.reload()
      })
    } else if (code === 403) {
      router.push({ path: '/401' })
    } else {
      const errorMsg = error.response.data.message || error.response.data
      if (errorMsg !== undefined) {
        notifyError({
          title: '接口请求异常',
          duration: 5000
        })
      }
    }
  } else {
    notifyError({
      title: '接口请求失败',
      duration: 5000
    })
  }
  return Promise.reject(error)
})

Vue.prototype.$http = axios // 挂载vue上，vue组件可以直接用
export default axios
