import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'
import Config from '@/settings'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import { filterAsyncRouter } from '@/store/modules/permission'
import { buildMenus } from '@/api/system/menu'

NProgress.configure({ showSpinner: false })// NProgress Configuration

// 路由懒加载
const Login = () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
const Layout = () => import(/* webpackChunkName: "layout" */ '../components/layout/index')
const Home = () => import(/* webpackChunkName: "layout" */ '../views/Home')

Vue.use(VueRouter)

export const constantRouterMap = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  {
    path: '/layout',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: '/home',
        component: Home,
        meta: { title: '首页', icon: 'index', affix: true, noCache: true }
      }
    ]
  }
]

const router = new VueRouter({
  routes: constantRouterMap
})

// 解决push报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

/**
 * 挂载路由导航守卫
 * to:将要访问的路径
 * from:代表从哪个路径跳转而来
 * next:是一个函数，表示放行（next():放行， next('/login'):强制跳转）
 */
router.beforeEach((to, from, next) => {
  NProgress.start()
  try {
    if (to.meta.title) {
      document.title = to.meta.title + ' - ' + Config.title
    }
    if (to.path === '/login') return next()

    if (!window.sessionStorage.getItem('token')) { // 未登录
      return next(`/login?redirect=${to.fullPath}`)
    } else { // 已登录
      if (store.getters.roles.length === 0) { // 判断当前用户是否已拉取完user信息
        debugger
        store.dispatch('GetInfo').then(res => { // 拉取user_info
          // 动态路由，拉取菜单
          loadMenus(next, to)
        }).catch((err) => {
          console.log(err)
          store.dispatch('LogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        })
        // 登录时未拉取 菜单，在此处拉取
      } else if (store.getters.loadMenus) {
        // 修改成false，防止死循环
        store.dispatch('updateLoadMenus').then(res => {
        })
        loadMenus(next, to)
      } else {
        next()
      }
    }
  } catch (e) {
    console.log(e)
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})

export const loadMenus = (next, to) => {
  debugger
  buildMenus().then(res => {
    const asyncRouter = filterAsyncRouter(res)
    asyncRouter.push({ path: '*', redirect: '/404', hidden: true })
    store.dispatch('GenerateRoutes', asyncRouter).then(() => { // 存储路由
      router.addRoutes(asyncRouter) // 动态添加可访问路由表
      next({ ...to, replace: true })
    })
  })
}

export default router
