import $login from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router from '../../router'
import store from '@/store'
import { buildMenus } from '@/api/system/menu'
import { toRouter } from '@/store/modules/permission'

const user = {
  state: {
    token: getToken(),
    user: {
      groups: []
    },
    urlRights: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USER: (state, user) => {
      state.user = user
    },
    SET_URL_RIGHTS: (state, urlRights) => {
      state.urlRights = urlRights
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      const rememberMe = userInfo.rememberMe
      return new Promise((resolve, reject) => {
        $login.login(userInfo)
          .then(res => {
            if (res.meta.status === 200) {
              setToken(res.data.token, rememberMe)
              commit('SET_TOKEN', res.data.token)
              setUserInfo(res.data.user, commit)
            }
            return res // return 后边的 then 会拿到这个返回值
          }).then(res => {
            if (res.meta.status === 200) {
              buildMenus().then(response => {
                if (response.meta.status === 200) {
                  const asyncRouter = toRouter(response.data)
                  asyncRouter.push({ path: '*', redirect: '/404', hidden: true })
                  store.dispatch('GenerateRoutes', asyncRouter).then(() => { // 存储路由
                    router.addRoutes(asyncRouter) // 动态添加可访问路由表
                  })
                }

                /*
                 1.不管resolve调用多少次，调用方只会拿到第一个resolve(xx), 后面的都会忽略的
                 2. 调用方先收到resolve(xx)，下来才会继续执行后续的then(f)
                 */
                resolve(response)
              })
            } else {
              resolve(res)
            }
          }).catch(error => {
            reject(error)
          })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        $login.getInfo().then(res => {
          if (res.meta.status !== 200) {
            reject(res.meta)
            return
          }
          setUserInfo(res.data, commit)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 退出
    LogOut ({ commit }) {
      return new Promise((resolve, reject) => {
        $login.logout().then(res => {
          logOut(commit)
          resolve()
        }).catch(error => {
          logOut(commit)
          reject(error)
        })
      })
    }
  }
}

export const logOut = (commit) => {
  commit('SET_TOKEN', '')
  commit('SET_URL_RIGHTS', [])
  removeToken()
}

export const setUserInfo = (res, commit) => {
  // 如果没有任何权限，则赋予一个默认的权限，避免请求死循环
  if (res.urlRights.length === 0) {
    commit('SET_URL_RIGHTS', ['ROLE_SYSTEM_DEFAULT'])
  } else {
    commit('SET_URL_RIGHTS', res.urlRights)
  }
  commit('SET_USER', res.user)
}

export default user
