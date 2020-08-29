import Vue from 'vue'
import request from '@/utils/request'

const Login = {
  login: function (user) {
    return request({
      url: 'auth/login',
      method: 'post',
      data: user
    })
  },
  getInfo () {
    return request({
      url: 'auth/info',
      method: 'get'
    })
  },
  getCodeImg () {
    return request({
      url: 'auth/code',
      method: 'get'
    })
  },
  logout () {
    return request({
      url: 'auth/logout',
      method: 'delete'
    })
  }
}
Vue.prototype.$login = Login
export default Login
