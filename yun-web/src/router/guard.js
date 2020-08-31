import router from './index'
import store from '@/store'
import Config from '@/settings'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import { getToken } from '@/utils/auth' // getToken from cookie
import { buildMenus } from '@/api/system/menu'
import { toRouter } from '@/store/modules/permission'

NProgress.configure({ showSpinner: false })// NProgress Configuration
const whiteList = ['/login']// no redirect whitelist

/**
 * 挂载路由导航守卫
 * to:将要访问的路径
 * from:代表从哪个路径跳转而来
 * next:是一个函数，表示放行（next():放行， next('/login'):强制跳转）
 */
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - ' + Config.title
  }
  NProgress.start()
  if (getToken()) {
    // 已登录且要跳转的页面是登录页
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.user == null) {
        store.dispatch('GetInfo').then(() => {
          loadRouterMenus(next, to)
        }).catch((err) => {
          console.log(err)
          store.dispatch('LogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        })
      } else if (store.getters.loadMenus) { // 登录时未拉取路由菜单，在此处拉取
        // 修改成false，防止死循环
        store.dispatch('updateLoadMenus').then(() => {})
        loadRouterMenus(next, to)
      } else {
        next()
      }
    }
  } else {
    /* has no token */
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

export const loadRouterMenus = (next, to) => {
  buildMenus().then(res => {
    if (res.meta.status === 200) {
      const asyncRouter = toRouter(res.data)
      asyncRouter.push({ path: '*', redirect: '/404', hidden: true })
      store.dispatch('GenerateRoutes', asyncRouter).then(() => { // 存储路由
        router.addRoutes(asyncRouter) // 动态添加可访问路由表
        next({ ...to, replace: true })
      })
    }
  })
}

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
